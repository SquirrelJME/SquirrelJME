/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/scritchaudio.h"
#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/softmix/softmixIntern.h"

static sjme_errorCode sjme_scritchaudio_peerConnectSub(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer)
{
#define GROW_SIZE 8
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL || inPeer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Fill in free slot. */
	if (sjme_error_is(error = sjme_list_injectGrow(inState->pool,
		GROW_SIZE, &inConn->peers, &inPeer, sjme_scritchaudio_connection, 0)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
#undef GROW_SIZE
}

static sjme_errorCode sjme_scritchaudio_core_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrong owner? */
	if (inState != inConn->inState)
	{
		/* Debug. */
		sjme_message("(%p, %p ->%d ->%p, %d)", inState, inConn,
			inConn->type, inConn->inState, explicit);
		
		return SJME_ERROR_AUDIO_STATE_MISMATCH;
	}
	
	/* Call no-peers handler if one is specified. */
	if (inConn->noPeers != NULL)
		if (sjme_error_is(error = inConn->noPeers(inState, inConn, explicit)))
			return sjme_error_default(error);

	/* Free peer list. */
	if (explicit && inConn->peers != NULL)
	{
		sjme_alloc_free(inConn->peers);
		inConn->peers = NULL;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_core_peerNoneSource(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	sjme_scritchaudio_stream inStream;
	sjme_list(sjme_scritchaudio_source)* sources;
	sjme_jint i, n;
	
	if (inState == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrong owner? */
	if (inState != inSource->connection.inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* Generic no-peer handler. */
	if (sjme_error_is(error = sjme_scritchaudio_core_peerNone(inState,
		SJME_AS_AUDIO_CONN(inSource), explicit)))
		return sjme_error_default(error);
	
	/* Unlink the source from the stream. */
	inStream = inSource->inStream;
	if (inStream != NULL)
	{
		/* Disassociate. */
		inSource->inStream = NULL;
		
		/* Grab the lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			inStream->connection.lock)))
			return sjme_error_default(error);

		/* Clear out matching source. */
		sources = inStream->sources;
		if (sources != NULL)
			for (i = 0, n = sources->length; i < n; i++)
				if (inSource == sources->elements[i])
				{
					sources->elements[i] = NULL;
					break;
				}

		/* Release lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			inStream->connection.lock, NULL)))
			return sjme_error_default(error);
	}

	/* Free the source, if explicit. */
	if (explicit)
	{
		sjme_alloc_free(inSource);
		return SJME_ERROR_AUDIO_DESTROYED;
	}

	/* Otherwise, stop. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_core_peerNoneDispatch(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Explicit no-peer is a full disconnect, it can only happen once. */
	if (explicit)
		if (!sjme_atomic_cs(sjme_jint, &inConn->disconnecting,
			0, 1))
			return SJME_ERROR_NONE;

	/* Call sub-handler. */
	if (inConn->type == SJME_SCRITCHAUDIO_CONN_SOURCE)
		return sjme_scritchaudio_core_peerNoneSource(inState,
			SJME_AS_AUDIO_SOURCE(inConn), explicit);

	/* No specific handler. */
	return sjme_scritchaudio_core_peerNone(inState, inConn, explicit);
}

sjme_errorCode sjme_scritchaudio_core_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_scritchaudio_connection peer;
	sjme_list(sjme_scritchaudio_connection)* peers;
	sjme_thread_spinLock* sharedLock;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inConn->inState != inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* Lock current connection. */
	sharedLock = inConn->lock;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(sharedLock)))
		return sjme_error_default(error);
	
	/* Notify the audio system that a disconnect is about to happen. */
	if (inState->impl->disconnect != NULL)
		if (sjme_error_is(error = inState->impl->disconnect(inState, inConn)))
			goto fail_notifyDisconnect;

	/* Find the first available peer. */
	for (i = 0, n = 0;;)
	{
		/* Get the list of peers, again. */
		peers = inConn->peers;
		if (peers == NULL)
			break;

		/* Correct length, did we hit the end? */
		n = peers->length;
		if (i >= n)
			break;

		/* Is there a peer here? */
		peer = peers->elements[i];
		if (peer != NULL)
		{
			/* Disconnect this peer. */
			if (sjme_error_is(error = inState->intern->peerDisconnect(
				inState, inConn, peer, SJME_JNI_TRUE)))
			{
				/* Was this destroyed? */
				if (error == SJME_ERROR_AUDIO_DESTROYED)
					goto skip_releaseLock;
				goto fail_peerDisconnect;
			}

			/* Go back. */
			i = 0;
		}

		/* Otherwise check the next peer. */
		else
			i++;
	}

	/* Double check if there is nothing left. */
	if (sjme_error_is(error = inState->intern->peerDisconnect(
		inState, inConn, NULL, SJME_JNI_TRUE)))
	{
		/* Was this destroyed? */
		if (error == SJME_ERROR_AUDIO_DESTROYED)
			goto skip_releaseLock;
		goto fail_peerDisconnect;
	}

	/* Release the lock. */
skip_releaseLock:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(sharedLock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_notifyDisconnect:
fail_peerDisconnect:
	sjme_thread_spinLockRelease(sharedLock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_core_peerConnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL || inPeer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inState != inConn->inState && inState != inPeer->inState)
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("%p <==> %p", inConn, inPeer);
#endif

	/* Grab peer lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(inPeer->lock)))
		goto fail_peerGrab;

	/* Connect forwards, then backwards. */
	if (sjme_error_is(error = sjme_scritchaudio_peerConnectSub(
		inState, inConn, inPeer)))
		goto fail_forwards;
	if (sjme_error_is(error = sjme_scritchaudio_peerConnectSub(
		inState, inPeer, inConn)))
		goto fail_backwards;
	
	/* Release peer lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(inPeer->lock,
		NULL)))
		goto fail_releaseLock;

	/* Success! */
	return SJME_ERROR_NONE;

fail_backwards:
fail_forwards:
fail_peerGrab:
	sjme_thread_spinLockRelease(inPeer->lock, NULL);
fail_releaseLock:
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_core_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNullable sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	sjme_list(sjme_scritchaudio_connection)* peers;
	sjme_scritchaudio_connection check;
	sjme_jint numPeers, i, n;
	sjme_jboolean wasFound;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inState != inConn->inState &&
		(inPeer != NULL && inState != inPeer->inState))
		return SJME_ERROR_AUDIO_STATE_MISMATCH;

	/* Lock current connection. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(inConn->lock)))
		return sjme_error_default(error);

	/* Count number of active peers, and also find the peer to disconnect. */
	numPeers = 0;
	peers = inConn->peers;
	wasFound = SJME_JNI_FALSE;
	if (peers == NULL || inPeer == NULL)
		goto skip_noPeers;

	/* Scan through looking for the peer we want disconnected. */
	for (i = 0, n = peers->length; i < n; i++)
	{
		/* Is this the peer we want to remove? */
		check = peers->elements[i];
		if (check == inPeer)
		{
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
			/* Debug, only when we find a peer to disconnect. */
			sjme_message("%p <//> %p (%s)", inConn, inPeer,
				(explicit ? "EXPLICIT" : "---"));
#endif
			
			/* Call sub-disconnect handler if there is one. */
			if (inConn->peerDisconnect != NULL)
				if (sjme_error_is(error = inConn->peerDisconnect(
					inState, inConn, inPeer, explicit)))
					goto fail_subDisconnect;
			
			/* Clear peer and do not count it. */
			wasFound = SJME_JNI_TRUE;
			peers->elements[i] = NULL;
			
			/* Reverse peer disconnect. */
			if (sjme_error_is(error = inState->intern->peerDisconnect(
				inState, inPeer, inConn, SJME_JNI_FALSE)))
				goto fail_reverse;
		}

		/* Count up peer otherwise. */
		else if (check != NULL)
			numPeers++;
	}

skip_noPeers:
	/* Release current connection. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(inConn->lock,
		NULL)))
		return sjme_error_default(error);

	/* Do not call no-peer if the peer was never in here. */
	/* However, if we are explicit then we always no peer. */
	if (!wasFound && !explicit)
		return SJME_ERROR_NONE;
	
	/* All peers were removed, dispatch the no-peer handler. */
	if (numPeers <= 0)
		if (sjme_error_is(error = inState->intern->peerNoneDispatch(
			inState, inConn, explicit)))
		{
			/* Connection was destroyed, can do nothing more. */
			if (error == SJME_ERROR_AUDIO_DESTROYED)
				return SJME_ERROR_AUDIO_DESTROYED;
			
			return sjme_error_default(error);
		}

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_subDisconnect:
fail_reverse:
	sjme_thread_spinLockRelease(inConn->lock, NULL);
	return sjme_error_default(error);
}
