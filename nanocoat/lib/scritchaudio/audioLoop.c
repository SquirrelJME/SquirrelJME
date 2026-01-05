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

sjme_errorCode sjme_scritchaudio_core_calcRenderInfo(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNullable sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;
	sjme_jint latency, freqAt;
	sjme_jint expected48KHzSamples;
	sjme_jint expected44KHzSamples;

	if (inState == NULL || inStream == NULL || renderInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize to automatic. */
	renderInfo->format = SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC;
	renderInfo->rate = SJME_SCRITCHAUDIO_RATE_AUTOMATIC;
	renderInfo->channels = SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC;

	/* Copy everything, if a source is specified it overrides everything. */
	if (inSource != NULL)
	{
		renderInfo->format = inSource->format;
		renderInfo->rate = inSource->rate;
		renderInfo->channels = inSource->channels;
	}
	
	/* Pull from stream if still automatic. */
	if (renderInfo->format == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		renderInfo->format = inStream->format;
	if (renderInfo->rate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		renderInfo->rate = inStream->rate;
	if (renderInfo->channels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		renderInfo->channels = inStream->channels;
	
	/* If still automatic, just choose the best format. */
	if (renderInfo->format == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		renderInfo->format = SJME_SCRITCHAUDIO_FORMAT_INT_S32;
	if (renderInfo->rate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		renderInfo->rate = SJME_SCRITCHAUDIO_RATE_HZ_44100;
	if (renderInfo->channels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		renderInfo->channels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

	/* Set the clock. */
	renderInfo->clock = inState->clock.clock;
	
	/* Get the latency to determine the sample count. */
	latency = (sjme_atomic_g(sjme_jint, &inState->pollDelayMillis) *
		1000000);
	if (sjme_atomic_g(sjme_jint, &inState->pollDelayNanos) > 0)
		latency += 1000000;

	/* Calculate the expected number of samples. */
	/* rate * latency. */
	expected44KHzSamples = (441 * (latency / 10000)) / 1000;
	expected48KHzSamples = (448 * (latency / 10000)) / 1000;
	
	/* Which base samples do we start at? */
	renderInfo->rate = renderInfo->rate;
	if ((renderInfo->rate % 8000) == 0)
	{
		freqAt = 48000;
		renderInfo->samples = expected48KHzSamples;
	}
	else
	{
		freqAt = 44100;
		renderInfo->samples = expected44KHzSamples;
	}
	
	/* Trim down sample count until we match the given set. */
	while (freqAt > renderInfo->rate)
	{
		renderInfo->samples >>= 2;
		freqAt >>= 2;
	}

	/* Bytes per sample? */
	renderInfo->bytesPerSample = sjme_scritchaudio_bytesPerSample[
		renderInfo->format];

	/* Allocate sample buffer */
	renderInfo->totalSamples = renderInfo->channels * renderInfo->samples;
	renderInfo->bufSize = renderInfo->bytesPerSample *
		renderInfo->totalSamples;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_core_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_jlong now;
	sjme_scritchaudio_stream stream;
	sjme_scritchaudio_renderInfo renderInfo;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Update the clock time. */
	inState->nal->nanoTime(&now);
	inState->clock.clock.full = now.full - inState->clock.clockBase.full;
	
	/* Forward using the default stream. */
	memset(&renderInfo, 0, sizeof(renderInfo));
	return inState->intern->loopIterate(inState, inState->stream, &renderInfo);
}

sjme_errorCode sjme_scritchaudio_core_loopIterateIntern(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;
	sjme_scritchaudio contextState;

	if (inState == NULL || renderInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is there a wrapped state? Use that instead and skip any middle */
	/* layer such as the software mixer. */
	contextState = inState->wrappedState;
	if (contextState == NULL)
		contextState = inState;

	/* Underlying audio system does not have a loop iterator? */
	if (contextState->impl->loopIterate == NULL)
		return SJME_ERROR_NONE;
	
	/* Lock the shared lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(contextState->lock)))
		goto fail_lock;
	
	/* Run the loop. */
	error = contextState->impl->loopIterate(contextState,
		contextState->stream, renderInfo);

	/* Release the lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(contextState->lock, NULL)))
	{
		error = SJME_ERROR_ILLEGAL_STATE;
		goto fail_unlock;
	}

	/* Return whatever error was given. */
	return error;
	
fail_calcRender:
fail_unlock:
fail_lock:
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("intern fail: %d", error);
#endif
	
	return sjme_error_default(error);
}
