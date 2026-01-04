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

/** The number of bytes per sample. */
const sjme_jint sjme_scritchaudio_bytesPerSample
	[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	1,
	2,
	4,
	4,
};

static sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_core_poll(
	sjme_attrInNotNull sjme_thread_parameter rawState)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_jint nanoSum, milliCarry, milliTime, milliSpent, milliRemain;
	const sjme_nal* nal;
	sjme_jlong enterTime, exitTime;
	sjme_jboolean await;

	/* Recover state. */
	inState = rawState;
	if (inState == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Does a binder need to be called? */
	if (inState->bindAudioThread != NULL)
		inState->bindAudioThread(inState);
	
	/* Set the loop as ready. */
	sjme_atomic_s(sjme_jint, &inState->loopThreadReady, 1);
	
	/* Awaiting the first audio stream still? */
	await = SJME_JNI_TRUE;
	
	/* Enter threading loop. */
	nal = inState->nal;
	for (nanoSum = 0, milliCarry = 0;;)
	{
		/* Keep everything at millisecond accuracy. Thus round up nanos */
		/* to millis by carrying. */
		nanoSum += sjme_atomic_g(sjme_jint, &inState->pollDelayNanos);
		if (nanoSum >= 1000000)
		{
			milliCarry++;
			nanoSum -= 1000000;
		}

		/* What is the millisecond time for this cycle? */
		milliTime = sjme_atomic_g(sjme_jint, &inState->pollDelayMillis);

		/* What time did this loop enter? */
		nal->nanoTime(&enterTime);

		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState)))
		{
			/* If the output blocks, we never want to lose our thread. */
			if (inState->bugs.outputBlocks)
				error = SJME_ERROR_NONE;
			
			/* Awaiting the first audio stream? */
			if (error == SJME_ERROR_AUDIO_AWAITING)
			{
				/* Still waiting for it, do not consider this an error. */
				if (await)
					error = SJME_ERROR_NONE;
				
				/* We lost the stream, so consider it gone. */
				else
					error = SJME_ERROR_AUDIO_DESTROYED;
			}
			
			/* Audio was destroyed? */
			if (error != SJME_ERROR_NONE &&
				error != SJME_ERROR_AUDIO_DESTROYED)
				sjme_message("Audio error: %d", error);
		}

		/* What time did this loop end? */
		nal->nanoTime(&exitTime);

		/* How much time was spent in the loop? */
		/* Use carried milliseconds. */
		milliSpent = (sjme_jint)((exitTime.full - enterTime.full) /
			INT64_C(1000000));
		milliRemain = (milliTime + milliCarry) - milliSpent;
		milliCarry = 0;
		
		/* Streaming is done? */
		if (error == SJME_ERROR_AUDIO_DESTROYED)
		{
			/* Go back to the sleeping rate as there is no audio */
			/* playing anymore. */
			sjme_atomic_sjme_jint_set(&inState->pollDelayMillis,
				SJME_SCRITCHAUDIO_SLEEP_RATE_MS);
			sjme_atomic_sjme_jint_set(&inState->pollDelayNanos,
				SJME_SCRITCHAUDIO_SLEEP_RATE_NS);
			
			/* Invalidate before leaving. */
			sjme_atomic_s(sjme_jint, &inState->loopThreadReady, 0);
			
			/* Barrier before leaving. */
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
			
#if defined(SJME_CONFIG_DEBUG)
			/* Debug. */
			sjme_message("Polling thread complete!");
#endif
			
			/* Stop. */
			break;
		}

		/* The audio subsystem could be synchronous or asynchronous, however */
		/* in either case just go for both and sleep if there is still time */
		/* remaining in the loop. */
		/* Do not sleep if the output blocks, since it handles it anyway. */
		if (milliRemain > SJME_SCRITCHAUDIO_MIN_SLEEP_MILLIS &&
			!inState->bugs.outputBlocks)
			sjme_thread_sleep(milliRemain, 0);
	}

	/* Finished. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc renderFunc,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
#define GROW_SIZE 8
	sjme_errorCode error;
	sjme_scritchaudio_source result;
	
	if (inState == NULL || inStream == NULL || outSource == NULL ||
		renderFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("sourceAttach(%p, %d, %d, %d)",
		inStream, inFormat, inRate, inChannels);
#endif

	/* If any are automatic, use the stream's format which could also be */
	/* automatic as well. */
	if (inFormat == SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC)
		inFormat = inStream->format;
	if (inRate == SJME_SCRITCHAUDIO_RATE_AUTOMATIC)
		inRate = inStream->rate;
	if (inChannels == SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC)
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
		sjme_frontEnd_copy(&result->frontEnd, initFrontEnd);

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
	sjme_atomic_s(sjme_jint, &inState->pollDelayMillis, 100);
	sjme_atomic_s(sjme_jint, &inState->pollDelayNanos, 0);

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
	sjme_scritchaudio wrappedState;
	sjme_scritchaudio_stream result;
	sjme_jint nextReady;
	
	if (inState == NULL || outStream == NULL)
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
	
	/* Use a default name. */
	if (inName == NULL)
		inName = "SquirrelJME";

	/* Missing? */
	if (inState->impl->streamCreate == NULL)
		return sjme_error_notImplemented(0);

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Lock the state. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(inState->lock)))
		goto fail_grabLock;
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("createStream(%p, %d, %d, %d)",
		result, inFormat, inRate, inChannels);
#endif

	/* Set stream details. */
	result->connection.lock = inState->lock;
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_STREAM;
	result->format = inFormat;
	result->rate = inRate;
	result->channels = inChannels;

	/* Forward to implementation. */
	if (sjme_error_is(error = inState->impl->streamCreate(inState,
		result, inName, inFormat, inRate, inChannels)) || result == NULL)
		goto fail_implCreate;
	
	/* Release the state. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		inState->lock, NULL)))
		goto fail_releaseLock;

	/* No stream has been set yet? */
	if (inState->stream == NULL)
		inState->stream = result;
	
	/* No thread has been set up yet? */
	/* If the state must be manually polled, we like having */
	/* threaded audio. Note that even if there is no thread defined */
	/* the operating system could call back into the audio subroutine. */
	/* We must be the top-most state! */
	if (((sjme_atomic_g(sjme_pointer, &inState->topState) == NULL &&
		inState->bugs.manualPoll)))
	{
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Polling thread preparing...");
#endif
		
		/* Await loop unready. */
		/* Note if the output blocks, we really do not want to recreate */
		/* the thread dynamically on each stream. */
		if (!inState->bugs.outputBlocks)
		{
			sjme_atomic_barrier();
			while (sjme_atomic_g(sjme_jint, &inState->loopThreadReady) != 0)
			{
				sjme_atomic_barrier();
				sjme_thread_yield();
				sjme_atomic_barrier();
			}
		}
		
		/* Create thread that loops infinitely. */
		if (sjme_atomic_g(sjme_jint, &inState->loopThreadReady) == 0)
		{
			inState->loopThread = SJME_THREAD_NULL;
			if (sjme_error_is(error = sjme_thread_new(&inState->loopThread,
				&inState->loopThreadId, sjme_scritchaudio_core_poll,
				inState)) || inState->loopThread == SJME_THREAD_NULL)
				goto fail_initThread;
		}
		
		/* Await loop ready. */
		sjme_atomic_barrier();
		while (sjme_atomic_g(sjme_jint, &inState->loopThreadReady) == 0)
		{
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
		}
		
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Polling thread ready!");
#endif
	}
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_implCreate:
	/* Release the lock before failing. */
	sjme_thread_spinLockRelease(inState->lock, NULL);
	
fail_noLoopIterate:
fail_initThread:
fail_grabLock:
fail_releaseLock:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_default(error);
#undef GROW_SIZE
}
