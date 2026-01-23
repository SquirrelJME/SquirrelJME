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

#include "sjme/util.h"

/** Gets the current polling time. */
#define sjme_scritchaudio_core_pollTimeGet() \
	/* Get the current polling times for this loop. */ \
	pollMilli = sjme_atomic_g(sjme_jint, &inStream->pollDelayMillis); \
	pollNanos = sjme_atomic_g(sjme_jint, &inStream->pollDelayNanos) + \
		(pollMilli * INT64_C(1000000))

/** Calculate next time and when giving up should occur. */
#define sjme_scritchaudio_core_pollTimeGiveUp() \
	/* When should the next buffer run be? Always use the same time base */ \
	/* unless we are really behind! */ \
	nextTime = nextTime + pollNanos; \
	diffNanos = nextTime - enterTime.full; \
	if (diffNanos >= SJME_SCRITCHAUDIO_GIVE_UP_NANOS) \
		nextTime = enterTime.full + pollNanos

/** Common end of poll sleep calculation. */
#define sjme_scritchaudio_core_pollLoopSleep(targetTime, penalty) \
	/* Do we have extra time to sleep */ \
	diffNanos = ((targetTime) - exitTime.full) - (penalty); \
	if (diffNanos >= SJME_SCRITCHAUDIO_MIN_SLEEP_NANOS) \
		sjme_thread_sleep((diffNanos - \
			SJME_SCRITCHAUDIO_HOLD_NANOS) / INT64_C(1000000), 0)

/** Common pre-enter for triggering? */
#define sjme_scritchaudio_core_preTrigger() \
	triggerCut = (inState->bugs.noTriggering ? 0 : \
		SJME_SCRITCHAUDIO_TRIGGER_NANOS)

static void sjme_scritchaudio_core_triggerBump(sjme_jboolean* triggerNotice,
	sjme_jlongNative from, sjme_jlongNative to)
{
#if 0
	/* Only emit warning once. */
	if (*triggerNotice)
		return;
#endif
	
	/* Emit message, once. */
	*triggerNotice = SJME_JNI_TRUE;
	sjme_emitB("Audio buffer underflow detected (%"PRId64"ns -> %"PRId64"ns).",
		from, to);
}

static sjme_errorCode sjme_scritchaudio_core_innerEvent(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNullable sjme_scritchaudio_stream inStream)
{
	sjme_errorCode error;
	const sjme_nal* nal;
	sjme_jint lastEvent, nowEvent;
	sjme_jlong enterTime, exitTime;
	sjme_jlongNative diffNanos, pollMilli, pollNanos;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Always start from zero as the last event rather than the actual */
	/* event in the counter, since an event may have already happened. */
	lastEvent = 0;
	
	/* Enter threading loop. */
	nal = inState->nal;
	for (;;)
	{
		/* Current time entering the loop. */
		enterTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&enterTime)))
			return sjme_error_default(error);

		/* Wait until the event counter changes. */
		while ((nowEvent = sjme_atomic_g(sjme_jint,
			&inStream->data.eventCounter)) == lastEvent)
		{
			sjme_atomic_barrier();
		}

		/* Loop cycle. */
		lastEvent = nowEvent;

		/* We only need the current poll time */
		sjme_scritchaudio_core_pollTimeGet();

		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState,
			inStream)))
		{
			if (error != SJME_ERROR_AUDIO_AWAITING)
				return sjme_error_default(error);
		}

		/* Flip buffer. */
		inStream->data.renderBuffer ^= 1;

		/* Current time exiting the loop. */
		exitTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&exitTime)))
			return sjme_error_default(error);

		/* Handle sleeping, assume the next event will happen some time */
		/* after our event time. */
		sjme_scritchaudio_core_pollLoopSleep(enterTime.full + pollNanos, 0);
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
	sjme_jlongNative diffNanos, nextTime, pollMilli, pollNanos, triggerCut;
	sjme_jboolean triggerNotice;
	sjme_scritchaudio_renderInfo* renderInfo;
	sjme_scritchaudio_streamBuffer* buffer;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Triggering pre-delay? */
	triggerNotice = SJME_JNI_FALSE;
	sjme_scritchaudio_core_preTrigger();

	/* Rendering info. */
	renderInfo = &inStream->data.renderInfo;

	/* Enter threading loop. */
	nal = inState->nal;
	for (nextTime = INT64_MIN;;)
	{
		/* Current time entering the loop. */
		enterTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&enterTime)))
			return sjme_error_default(error);

		/* Not yet ready? */
		if (nextTime != INT64_MIN &&
			(enterTime.full + triggerCut) < nextTime)
			continue;

		/* Get and calculate polling times, along with giving up. */
		sjme_scritchaudio_core_pollTimeGet();
		sjme_scritchaudio_core_pollTimeGiveUp();

		/* Wipe the render buffer. */
		buffer = &inStream->data.buffers[inStream->data.renderBuffer];
		memset(buffer->buffer,
			(renderInfo->format == SJME_SCRITCHAUDIO_FORMAT_BYTE_U8 ?
				0x80 : 0x00), renderInfo->bufSize);

		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState,
			inStream)))
		{
			if (error != SJME_ERROR_AUDIO_AWAITING)
				return sjme_error_default(error);
		}

		/* Flip buffer. */
		inStream->data.renderBuffer = !inStream->data.renderBuffer;

		/* Current time exiting the loop. */
		exitTime.full = INT64_MIN;
		if (sjme_error_is(error = nal->nanoTime(&exitTime)))
			return sjme_error_default(error);
		
		/* Is the system unable to handle playing audio at this rate? */
		/* If so, increase the triggering amount so buffers load sooner. */
		/* Note, never go over the cap because that does not make much sense */
		/* as likely the system experience a lag spike of some kind. */
		diffNanos = exitTime.full - enterTime.full;
		if (diffNanos > triggerCut &&
			diffNanos < SJME_SCRITCHAUDIO_TRIGGER_CAP_NANOS)
		{
			/* Emit warning if significant. */
			if (diffNanos > SJME_SCRITCHAUDIO_TRIGGER_NANOS)
				sjme_scritchaudio_core_triggerBump(&triggerNotice,
					triggerCut, diffNanos);
			
			/* Increase the cap, but only by the average of these. */
			triggerCut = sjme_min(SJME_SCRITCHAUDIO_TRIGGER_CAP_NANOS,
				(triggerCut + diffNanos) >> 1);
		}

		/* Handle sleeping. */
		sjme_scritchaudio_core_pollLoopSleep(nextTime, triggerCut);
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

sjme_errorCode sjme_scritchaudio_core_allocBuffer(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInPositive sjme_jint headerSize,
	sjme_attrInNotNull const sjme_scritchaudio_renderInfo* renderInfo,
	sjme_attrInNullable sjme_scritchaudio_streamBuffer* outBuffer)
{
	sjme_errorCode error;
	sjme_scritchaudio_streamBuffer* buffer;
	sjme_jint i;

	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Render info missing? */
	if (inStream->data.renderInfo.bufSize == 0 ||
		inStream->data.renderInfo.rate == 0 ||
		inStream->data.renderInfo.channels == 0)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Allocate each buffer. */
	for (i = 0; i < SJME_SCRITCHAUDIO_RENDER_SLICES; i++)
	{
		buffer = &inStream->data.buffers[i];

		/* Allocate the buffer. */
		if (buffer->buffer == NULL)
			if (sjme_error_is(error = sjme_alloc(inState->pool,
				inStream->data.renderInfo.bufSize,
				&buffer->buffer)) ||
				buffer->buffer == NULL)
				return sjme_error_default(error);

		/* Wipe the buffer. */
		memset(buffer->buffer,
			(inStream->data.renderInfo.format ==
				SJME_SCRITCHAUDIO_FORMAT_BYTE_U8 ?
				0x80 : 0x00), inStream->data.renderInfo.bufSize);

		/* Allocate headers? */
		if (inStream->data.headerSize > 0 &&
			buffer->header == NULL)
			if (sjme_error_is(error = sjme_alloc(inState->pool,
				inStream->data.headerSize,
				&buffer->header)) ||
				buffer->header == NULL)
				return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_core_calcRenderInfo(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull const sjme_scritchaudio_renderFormat* inFormat,
	sjme_attrInNotNull const sjme_scritchaudio_latency* inLatency,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_jint latency, freqAt;
	sjme_jint expected48KHzSamples;
	sjme_jint expected44KHzSamples;

	if (inState == NULL || inFormat == NULL || inLatency == NULL ||
		renderInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inFormat->format == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC ||
		inFormat->rate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC ||
		inFormat->channels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Copy format over. */
	renderInfo->format.format = inFormat->format;
	renderInfo->format.rate = inFormat->rate;
	renderInfo->format.channels = inFormat->channels;

	/* Set the clock. */
	renderInfo->clock = inState->clock.clock;
	
	/* Get the latency to determine the sample count. */
	latency = inLatency->pollDelayMillis * 1000000;
	if (inLatency->pollDelayNanos > 0)
		latency += 1000000;

	/* Calculate the expected number of samples. */
	/* rate * latency. */
	expected44KHzSamples = (441 * (latency / 10000)) / 1000;
	expected48KHzSamples = (448 * (latency / 10000)) / 1000;
	
	/* Which base samples do we start at? */
	renderInfo->format.rate = renderInfo->format.rate;
	if ((renderInfo->format.rate % 8000) == 0)
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
	while (freqAt > renderInfo->format.rate)
	{
		renderInfo->samples >>= 2;
		freqAt >>= 2;
	}

	/* Bytes per sample? */
	renderInfo->bytesPerSample = sjme_scritchaudio_bytesPerSample[
		renderInfo->format];

	/* Determine sample buffer size. */
	renderInfo->totalSamples = renderInfo->format.channels *
		renderInfo->samples;
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
