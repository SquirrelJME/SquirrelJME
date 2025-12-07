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

	/* Copy everything, if a source is specified it overrides everything. */
	if (inSource != NULL)
	{
		renderInfo->format = inSource->format;
		renderInfo->rate = inSource->rate;
		renderInfo->channels = inSource->channels;
	}
	else
	{
		renderInfo->format = inStream->format;
		renderInfo->rate = inStream->rate;
		renderInfo->channels = inStream->channels;
	}

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

	/* If there is no stream, do not bother. */
	stream = inState->stream;
	if (stream == NULL)
		return SJME_ERROR_NONE;
	
	/* Only forward if the handler supports this. */
	if (inState->impl->loopIterate == NULL)
		return SJME_ERROR_NONE;

	/* Forward. */
	memset(&renderInfo, 0, sizeof(renderInfo));
	return inState->intern->loopIterate(inState, stream, &renderInfo);
}

sjme_errorCode sjme_scritchaudio_core_loopIterateIntern(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;

	if (inState == NULL || inStream == NULL || renderInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only forward if the handler supports this. */
	if (inState->impl->loopIterate == NULL)
		return SJME_ERROR_NONE;

	/* Calculate the render info. */
	if (sjme_error_is(error = inState->intern->calcRenderInfo(inState,
		inStream, NULL, renderInfo)))
		return sjme_error_default(error);
	
	/* Lock the shared lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inStream->sharedLock)))
		return sjme_error_default(error);
	
	/* Run the loop. */
	error = inState->impl->loopIterate(inState, inStream, renderInfo);

	/* Release the lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&inStream->sharedLock,
		NULL)))
		return sjme_error_defaultOr(error, SJME_ERROR_ILLEGAL_STATE);

	/* Return whatever error was given. */
	return error;
}
