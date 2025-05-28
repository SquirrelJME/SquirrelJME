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
		GROW_SIZE, &inConn->peers, inPeer, sjme_scritchaudio_connection, 0)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
#undef GROW_SIZE
}

static sjme_errorCode sjme_scritchaudio_core_peerNone(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn)
{
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrong owner? */
	if (inState != inConn->inState)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Call no-peers handler if one is specified. */
	if (inConn->noPeers != NULL)
	{
		/* Call handler. */
		if (sjme_error_is(error = inConn->noPeers(inState, inConn)))
			return sjme_error_default(error);

		/* Invalidate so it cannot be called again. */
		inConn->noPeers = NULL;
	}

	/* Free peer list. */
	if (inConn->peers != NULL)
	{
		sjme_alloc_free(inConn->peers);
		inConn->peers = NULL;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_core_peerNoneSource(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource)
{
	sjme_errorCode error;
	sjme_scritchaudio_stream inStream;
	sjme_list_sjme_scritchaudio_source* sources;
	sjme_jint i, n;
	
	if (inState == NULL || inSource == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Wrong owner? */
	if (inState != inSource->connection.inState)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Generic no-peer handler. */
	if (sjme_error_is(error = sjme_scritchaudio_core_peerNone(inState,
		SJME_AS_AUDIO_CONN(inSource))))
		return sjme_error_default(error);
	
	/* Unlink the source from the stream. */
	inStream = inSource->inStream;
	if (inStream != NULL)
	{
		/* Disconnecting from source. */
		if (sjme_error_is(error = sjme_scritchaudio_core_peerDisconnect(
			inState, SJME_AS_AUDIO_CONN(inSource),
			SJME_AS_AUDIO_CONN(inStream), SJME_JNI_TRUE)))
			return sjme_error_default(error);
		
		/* Disassociate. */
		inSource->inStream = NULL;
		
		/* Grab the lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&inStream->connection.lock)))
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
			&inStream->connection.lock, NULL)))
			return sjme_error_default(error);
	}

	/* Free the source. */
	sjme_alloc_free(inSource);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_core_peerNoneDispatch(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInValue sjme_jboolean explicit)
{
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If this is not an explicit no-peer, do not kill the stream. */
	if (!explicit && inConn->type == SJME_SCRITCHAUDIO_CONN_STREAM)
		return SJME_ERROR_NONE;
	
	/* No-peer is a full disconnect, it can only happen once. */
	if (!sjme_atomic_sjme_jint_compareSet(&inConn->disconnecting,
		0, 1))
		return SJME_ERROR_NONE;

	/* Call sub-handler. */
	switch (inConn->type)
	{
		case SJME_SCRITCHAUDIO_CONN_SOURCE:
			return sjme_scritchaudio_core_peerNoneSource(inState,
				SJME_AS_AUDIO_SOURCE(inConn));

		default:
			return sjme_scritchaudio_core_peerNone(inState, inConn);
	}
}

sjme_errorCode sjme_scritchaudio_core_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_scritchaudio_connection peer;
	sjme_list_sjme_scritchaudio_connection* peers;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inConn->inState != inState)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Lock current connection. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inConn->lock)))
		return sjme_error_default(error);

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
				goto fail_peerDisconnect;

			/* Go back. */
			i = 0;
		}

		/* Otherwise check the next peer. */
		else
			i++;
	}

	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inConn->lock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_peerDisconnect:
	sjme_thread_spinLockRelease(&inConn->lock, NULL);
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
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
	sjme_message("%p <==> %p", inConn, inPeer);

	/* Grab peer lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inPeer->lock)))
		goto fail_peerGrab;

	/* Connect forwards, then backwards. */
	if (sjme_error_is(error = sjme_scritchaudio_peerConnectSub(
		inState, inConn, inPeer)))
		goto fail_forwards;
	if (sjme_error_is(error = sjme_scritchaudio_peerConnectSub(
		inState, inPeer, inConn)))
		goto fail_backwards;
	
	/* Release peer lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inPeer->lock,
		NULL)))
		goto fail_releaseLock;

	/* Success! */
	return SJME_ERROR_NONE;

fail_backwards:
fail_forwards:
fail_peerGrab:
	sjme_thread_spinLockRelease(&inPeer->lock, NULL);
fail_releaseLock:
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_core_peerDisconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer,
	sjme_attrInValue sjme_jboolean explicit)
{
	sjme_errorCode error;
	sjme_list_sjme_scritchaudio_connection* peers;
	sjme_scritchaudio_connection check;
	sjme_jint numPeers, i, n;
	sjme_jboolean wasFound;
	
	if (inState == NULL || inConn == NULL || inPeer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inState != inConn->inState && inState != inPeer->inState)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
	sjme_message("%p <//> %p", inConn, inPeer);

	/* Lock current connection. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inConn->lock)))
		return sjme_error_default(error);

	/* Count number of active peers, and also find the peer to disconnect. */
	numPeers = 0;
	peers = inConn->peers;
	wasFound = SJME_JNI_FALSE;
	if (peers == NULL)
		goto skip_noPeers;

	/* Scan through looking for the peer we want disconnected. */
	for (i = 0, n = peers->length; i < n; i++)
	{
		/* Is this the peer we want to remove? */
		check = peers->elements[i];
		if (check == inPeer)
		{
			/* Clear peer and do not count it. */
			wasFound = SJME_JNI_TRUE;
			peers->elements[i] = NULL;
			
			/* Reverse peer disconnect. */
			/* It is never explicit as a reverse disconnect is always */
			/* implicit since the other side just got cut off. */
			if (sjme_error_is(error = inState->intern->peerDisconnect(
				inState, inPeer, inConn, SJME_JNI_FALSE)))
				goto fail_reverse;
			
			/* Call disconnect handler if there is one. */
			if (inConn->peerDisconnect != NULL)
				if (sjme_error_is(error = inConn->peerDisconnect(
					inState, inConn, inPeer, explicit)))
					goto fail_subDisconnect;
		}

		/* Count up peer otherwise. */
		else if (check != NULL)
			numPeers++;
	}

skip_noPeers:
	/* Release current connection. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inConn->lock,
		NULL)))
		return sjme_error_default(error);

	/* Do not call no-peer if the peer was never in here. */
	if (!wasFound)
		return SJME_ERROR_NONE;
	
	/* All peers were removed, dispatch the no-peer handler. */
	if (numPeers <= 0)
		if (sjme_error_is(error = sjme_scritchaudio_core_peerNoneDispatch(
			inState, inConn, SJME_JNI_FALSE)))
			return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_subDisconnect:
fail_reverse:
	sjme_thread_spinLockRelease(&inConn->lock, NULL);
	return sjme_error_default(error);
}
