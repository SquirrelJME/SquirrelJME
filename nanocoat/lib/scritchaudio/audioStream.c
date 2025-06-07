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

/** The number of bytes per sample. */
const sjme_jint sjme_scritchaudio_bytesPerSample
	[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	1,
	2,
	4,
	4,
};

sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc renderFunc,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
#define GROW_SIZE 8
	sjme_errorCode error;
	sjme_scritchaudio_source result;
	
	if (inState == NULL || inStream == NULL || outSource == NULL ||
		renderFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If any are automatic, use the stream's format. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = inStream->format;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = inStream->rate;
	if (inChannels = SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		inChannels = inStream->channels;

	/* Allocate resultant source. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Initialize state. */
	result->connection.lock = inStream->connection.lock;
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_SOURCE;
	result->inStream = inStream;
	result->renderFunc = renderFunc;
	result->format = inFormat;
	result->rate = inRate;
	result->channels = inChannels;
	if (initFrontEnd != NULL)
		memmove(&result->frontEnd, initFrontEnd, sizeof(*initFrontEnd));

	/* Forward API init. */
	if (sjme_error_is(error = inState->impl->sourceAttach(inState,
		inStream, result)))
		goto fail_implInit;

	/* Lock the stream. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		inStream->connection.lock)))
		goto fail_lockGrab;

	/* Inject into list. */
	if (sjme_error_is(error = sjme_list_injectGrow(inState->pool,
		GROW_SIZE, &inStream->sources, &result, sjme_scritchaudio_source, 0)))
		goto fail_growList;
	
	/* Connect peers. */
	if (sjme_error_is(error = inState->intern->peerConnect(inState,
		SJME_AS_AUDIO_CONN(inStream),
		SJME_AS_AUDIO_CONN(result), SJME_JNI_TRUE)))
		goto fail_peerConnect;

	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		inStream->connection.lock, NULL)))
		goto fail_lockRelease;
	
	/* We attached a source, so make sure the audio playback is faster. */
	sjme_atomic_sjme_jint_set(&inState->pollDelayMillis, 100);
	sjme_atomic_sjme_jint_set(&inState->pollDelayNanos, 0);

	/* Success! */
	*outSource = result;
	return SJME_ERROR_NONE;

fail_peerConnect:
fail_growList:
fail_lockGrab:
	sjme_thread_spinLockRelease(inStream->connection.lock, NULL);
	
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

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Set stream details. */
	result->connection.lock = &result->sharedLock;
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_STREAM;
	result->format = inFormat;
	result->rate = inRate;
	result->channels = inChannels;

	/* Forward to implementation. */
	if (sjme_error_is(error = inState->impl->streamCreate(inState,
		result, inName, inFormat, inRate, inChannels)) || result == NULL)
		goto fail_implCreate;

	/* No stream has been set yet? */
	if (inState->stream == NULL)
		inState->stream = result;
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_implCreate:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_default(error);
#undef GROW_SIZE
}
