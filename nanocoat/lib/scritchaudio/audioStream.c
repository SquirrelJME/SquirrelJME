/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchaudio/scritchaudio.h"
#include "lib/scritchaudio/scritchaudioIntern.h"

sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc renderFunc,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
#define GROW_SIZE 8
	sjme_errorCode error;
	sjme_scritchaudio_source result;
	sjme_list_sjme_scritchaudio_source* sources;
	sjme_jint i, n, freeSlot;
	
	if (inState == NULL || inStream == NULL || outSource == NULL ||
		renderFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate resultant source. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Initialize state. */
	result->connection.inState = inState;
	result->inStream = inStream;
	result->renderFunc = renderFunc;
	if (initFrontEnd != NULL)
		memmove(&result->frontEnd, initFrontEnd, sizeof(*initFrontEnd));

	/* Forward API init. */
	if (sjme_error_is(error = inState->impl->sourceAttach(inState,
		inStream, result)))
		goto fail_implInit;

	/* Lock the stream. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inStream->connection.lock)))
		goto fail_lockGrab;

	/* Find a free slot in the sources. */
	freeSlot = -1;
	sources = inStream->sources;
	n = 0;
	if (sources != NULL)
		for (i = 0, n = sources->length; i < n; i++)
			if (sources->elements[i] == NULL)
			{
				freeSlot = i;
				break;
			}

	/* No room? Grow the list. */
	if (freeSlot < 0)
	{
		/* Grow the list. */
		if (sjme_error_is(error = sjme_list_replace(inState->pool,
			n + GROW_SIZE, &sources, sjme_scritchaudio_source, 0)) ||
			sources == NULL)
			goto fail_growList;

		/* Free slot is at the end. */
		freeSlot = n;
	}

	/* Store into this slot. */
	sources->elements[freeSlot] = result;

	/* Connect peers. */
	if (sjme_error_is(error = inState->intern->peerConnect(inState,
		SJME_AS_AUDIO_CONN(inStream),
		SJME_AS_AUDIO_CONN(result))))
		goto fail_peerConnect;

	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inStream->connection.lock, NULL)))
		goto fail_lockRelease;

	/* Success! */
	*outSource = result;
	return SJME_ERROR_NONE;

fail_peerConnect:
fail_growList:
fail_lockGrab:
	sjme_thread_spinLockRelease(&inStream->connection.lock, NULL);
	
fail_lockRelease:
fail_implInit:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_default(error);
#undef GROW_SIZE
}

sjme_errorCode sjme_scritchaudio_core_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels)
{
#define GROW_SIZE 8
	sjme_errorCode error;
	sjme_scritchaudio_stream result;
	
	if (inState == NULL || outStream == NULL || inName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inFormat != SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC &&
		(inFormat < 0 || inFormat >= SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inRate != SJME_SCRITCHAUDIO_RATE_AUTOMATIC &&
		(inRate <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	if (inChannels != SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC &&
		(inChannels <= 0))
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Missing? */
	if (inState->impl->streamCreate == NULL)
		return sjme_error_notImplemented(0);

	/* Forward to implementation. */
	result = NULL;
	if (sjme_error_is(error = inState->impl->streamCreate(inState,
		&result, inName, inFormat, inRate, inChannels)) || result == NULL)
		goto fail_implCreate;

	/* Grab the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inState->lock)))
		goto fail_grabLock;

	/* Fill in. */
	if (sjme_error_is(error = sjme_list_injectGrow(inState->pool,
		GROW_SIZE, &inState->streams, result, sjme_scritchaudio_stream, 0)))
		goto fail_inject;
	
	/* Release lock before returning. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inState->lock,
		NULL)))
		goto fail_releaseLock;

	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_releaseLock:
fail_inject:
	/* Release lock before failing. */
	sjme_thread_spinLockRelease(&inState->lock, NULL);
fail_grabLock:
fail_implCreate:
	return sjme_error_default(error);
#undef GROW_SIZE
}
