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

sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc* renderFunc)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_list_sjme_scritchaudio_stream* streams;
	sjme_jint freeSlot, i, n;
	
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

	/* Find free spot on the list. */
	freeSlot = -1;
	streams = inState->streams;
	n = 0;
	if (streams != NULL)
		for (i = 0, n = streams->length; i < n; i++)
			if (streams->elements[i] == NULL)
			{
				freeSlot = i;
				break;
			}

	/* No room? */
	if (freeSlot < -1 || streams == NULL)
	{
		/* Grow the list. */
		if (sjme_error_is(error = sjme_list_replace(inState->pool,
			n + GROW_SIZE, &streams, sjme_scritchaudio_stream, 0)) ||
			streams == NULL)
			goto fail_growList;

		/* Use this list instead. */
		inState->streams = streams;

		/* Set slot at the end. */
		freeSlot = n;
	}

	/* Set stream here. */
	streams->elements[freeSlot] = result;

	/* Release lock before returning. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inState->lock,
		NULL)))
		goto fail_releaseLock;

	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_releaseLock:
fail_growList:
	/* Release lock before failing. */
	sjme_thread_spinLockRelease(&inState->lock, NULL);
fail_grabLock:
fail_implCreate:
	return sjme_error_default(error);
#undef GROW_SIZE
}
