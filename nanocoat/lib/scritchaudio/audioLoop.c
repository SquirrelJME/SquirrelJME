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

/** Common get and calculation of poll time, along with giving up time. */
#define sjme_scritchaudio_core_pollTimeGiveUp() \
	/* Get the current polling times for this loop. */ \
	pollMilli = sjme_atomic_g(sjme_jint, &inStream->pollDelayMillis); \
	pollNanos = sjme_atomic_g(sjme_jint, &inStream->pollDelayNanos) + \
		(pollMilli * INT64_C(1000000)); \
	 \
	/* When should the next buffer run be? Always use the same time base */ \
	/* unless we are really behind! */ \
	nextTime = nextTime + pollNanos; \
	diffNanos = nextTime - enterTime.full; \
	if (diffNanos >= SJME_SCRITCHAUDIO_GIVE_UP_NANOS) \
		nextTime = enterTime.full + pollNanos

/** Common end of poll sleep calculation. */
#define sjme_scritchaudio_core_pollLoopSleep() \
	/* Do we have extra time to sleep */ \
	diffNanos = (nextTime - exitTime.full); \
	if (diffNanos >= SJME_SCRITCHAUDIO_MIN_SLEEP_NANOS) \
		sjme_thread_sleep((diffNanos - \
			SJME_SCRITCHAUDIO_HOLD_NANOS) / INT64_C(1000000), 0)

static sjme_errorCode sjme_scritchaudio_core_innerEvent(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream)
{
	sjme_errorCode error;
	const sjme_nal* nal;
	sjme_jint lastEvent, nowEvent;
	sjme_jlong enterTime, exitTime;
	sjme_jlongNative diffNanos, nextTime, pollMilli, pollNanos;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Always start from zero as the last event rather than the actual */
	/* event in the counter, since an event may have already happened. */
	lastEvent = 0;

	/* Enter threading loop. */
	nal = inState->nal;
	for (nextTime = INT64_MIN;;)
	{
		/* Current time entering the loop. */
		enterTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&enterTime)))
			return sjme_error_default(error);

		/* Wait until the event counter changes. */
		while ((nowEvent = sjme_atomic_g(sjme_jint,
			&inStream->data.eventCounter)) == lastEvent)
		{
			sjme_thread_yield();
			sjme_atomic_barrier();
		}

		/* Loop cycle. */
		lastEvent = nowEvent;

		/* Get and calculate polling times, along with giving up. */
		sjme_scritchaudio_core_pollTimeGiveUp();

		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState,
			inStream)))
		{
			if (error != SJME_ERROR_AUDIO_AWAITING)
				return sjme_error_default(error);
		}

		/* Current time exiting the loop. */
		exitTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&exitTime)))
			return sjme_error_default(error);

		/* Handle sleeping. */
		sjme_scritchaudio_core_pollLoopSleep();
	}

	/* Success? */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchaudio_core_innerManual(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream)
{
	sjme_errorCode error;
	const sjme_nal* nal;
	sjme_jlong enterTime, exitTime;
	sjme_jlongNative diffNanos, nextTime, pollMilli, pollNanos;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Enter threading loop. */
	nal = inState->nal;
	for (nextTime = INT64_MIN;;)
	{
		/* Current time entering the loop. */
		enterTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&enterTime)))
			return sjme_error_default(error);

		/* Not yet ready? */
		if (nextTime != INT64_MIN && enterTime.full < nextTime)
		{
			/* Let other threads run. */
			sjme_thread_yield();

			/* Loop again. */
			continue;
		}

		/* Get and calculate polling times, along with giving up. */
		sjme_scritchaudio_core_pollTimeGiveUp();

		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState,
			inStream)))
		{
			if (error != SJME_ERROR_AUDIO_AWAITING)
				return sjme_error_default(error);
		}

		/* Current time exiting the loop. */
		exitTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&exitTime)))
			return sjme_error_default(error);

		/* Handle sleeping. */
		sjme_scritchaudio_core_pollLoopSleep();
	}

	/* Success? */
	return SJME_ERROR_NONE;
}

static sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_core_poll(
	sjme_attrInNotNull sjme_thread_parameter rawStream,
	sjme_attrInNotNull sjme_scritchaudio_loopIterateFunc innerFunc)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_scritchaudio_stream inStream;

	/* Recover stream and state. */
	inStream = rawStream;
	if (inStream == NULL || innerFunc == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	inState = inStream->connection.inState;

	/* Does a binder need to be called? */
	if (inState->bindAudioThread != NULL)
		inState->bindAudioThread(rawStream);

	/* Set the loop as ready. */
	sjme_atomic_s(sjme_jint, &inStream->loopThreadReady, 1);

	/* Enter main polling method. */
	error = innerFunc(inState, inStream);

	/* Invalidate before leaving. */
	sjme_atomic_s(sjme_jint, &inStream->loopThreadReady, 0);

	/* Barrier before leaving. */
	sjme_atomic_barrier();
	sjme_thread_yield();
	sjme_atomic_barrier();

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Polling thread complete! (%d)", error);
#endif

	/* Finished. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchaudio_core_calcRenderInfo(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNullable sjme_scritchaudio_source inSource,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
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
	latency = (sjme_atomic_g(sjme_jint, &inStream->pollDelayMillis) *
		1000000);
	if (sjme_atomic_g(sjme_jint, &inStream->pollDelayNanos) > 0)
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
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream)
{
	sjme_errorCode error;
	
	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Lock the shared lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(inState->lock)))
		return sjme_error_default(error);
	
	/* Forward using the default stream. */
	if (sjme_error_is(error = inState->intern->loopIterateLocked(inState,
		inState->stream)))
		goto fail_iterate;

	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(inState->lock,
		NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_iterate:
	/* Release before failing. */
	sjme_thread_spinLockRelease(inState->lock, NULL);

	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_core_loopIterateLocked(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream)
{
	sjme_jlong now;
	sjme_scritchaudio contextState;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is there a wrapped state? Use that instead and skip any middle */
	/* layer such as the software mixer. */
	contextState = inState->wrappedState;
	if (contextState == NULL)
		contextState = inState;

	/* Update the clock time. */
	inState->nal->nanoTime(&now);
	inState->clock.clock.full = now.full - inState->clock.clockBase.full;

	/* Underlying audio system does not have a loop iterator? */
	if (contextState->impl->loopIterate == NULL)
		return SJME_ERROR_NONE;

	/* Run the implementation loop. */
	return contextState->impl->loopIterate(contextState,
		contextState->stream);
}

sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_core_pollEvent(
	sjme_attrInNotNull sjme_thread_parameter rawStream)
{
	return sjme_scritchaudio_core_poll(rawStream,
		sjme_scritchaudio_core_innerEvent);
}

sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_core_pollManual(
	sjme_attrInNotNull sjme_thread_parameter rawStream)
{
	return sjme_scritchaudio_core_poll(rawStream,
		sjme_scritchaudio_core_innerManual);
}
