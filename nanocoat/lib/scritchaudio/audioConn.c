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

sjme_errorCode sjme_scritchaudio_core_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn)
{
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be of the same state. */
	if (inConn->inState != inState)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_peerConnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer)
{
	sjme_errorCode error;
	
	if (inState == NULL || inConn == NULL || inPeer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

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
