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

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	/** The minimum sleeping time. */
	#define SJME_SCRITCHAUDIO_MIN_SLEEP_MILLIS 16
#else
	/** The minimum sleeping time. */
	#define SJME_SCRITCHAUDIO_MIN_SLEEP_MILLIS 0
#endif

/** Fallback for audio formats. */
static const sjme_scritchaudio_format
	sjme_scritchaudio_formatFallback[SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS] =
{
	SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS,
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8,
	SJME_SCRITCHAUDIO_FORMAT_SHORT_S16,
	SJME_SCRITCHAUDIO_FORMAT_INT_S32,
};

/** Fallback for audio rates. */
static const sjme_scritchaudio_rate sjme_scritchaudio_rateFallback[9] =
{
	SJME_SCRITCHAUDIO_RATE_HZ_48000,
	SJME_SCRITCHAUDIO_RATE_HZ_44100,
	SJME_SCRITCHAUDIO_RATE_HZ_24000,
	SJME_SCRITCHAUDIO_RATE_HZ_22050,
	SJME_SCRITCHAUDIO_RATE_HZ_16000,
	SJME_SCRITCHAUDIO_RATE_HZ_11025,
	SJME_SCRITCHAUDIO_RATE_HZ_8000,
	0,
	0
};

/**
 * Standard API functions.
 *
 * @since 2025/05/10
 */
static const sjme_scritchaudio_apiFunctions sjme_scritchaudio_coreFunctions =
{
	sjme_sm(.disconnect, sjme_scritchaudio_core_disconnect),
	sjme_sm(.loopIterate, sjme_scritchaudio_core_loopIterate),
	sjme_sm(.queryMidiPorts, sjme_scritchaudio_core_queryMidiPorts),
	sjme_sm(.sourceAttach, sjme_scritchaudio_core_sourceAttach),
	sjme_sm(.streamCreate, sjme_scritchaudio_core_streamCreate),
};

static const sjme_scritchaudio_internFunctions sjme_scritchaudio_coreInterns =
{
	sjme_sm(.calcRenderInfo, sjme_scritchaudio_core_calcRenderInfo),
	sjme_sm(.fallbackNext, sjme_scritchaudio_core_fallbackNext),
	sjme_sm(.loopIterate, sjme_scritchaudio_core_loopIterateIntern),
	sjme_sm(.peerConnect, sjme_scritchaudio_core_peerConnect),
	sjme_sm(.peerDisconnect, sjme_scritchaudio_core_peerDisconnect),
	sjme_sm(.peerNoneDispatch, sjme_scritchaudio_core_peerNoneDispatch),
	sjme_sm(.streamCreate, sjme_scritchaudio_core_streamCreate),
};

static sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_core_poll(
	sjme_attrInNotNull sjme_thread_parameter rawState)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_jint nanoSum, milliCarry, milliTime, milliSpent, milliRemain;
	const sjme_nal* nal;
	sjme_jlong enterTime, exitTime;

	/* Recover state. */
	inState = rawState;
	if (inState == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Does a binder need to be called? */
	if (inState->bindAudioThread != NULL)
		inState->bindAudioThread(inState);
	
	/* Set the loop as ready. */
	sjme_atomic_s(sjme_jint, &inState->loopThreadReady, 1);
	
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
			sjme_message("Audio error: %d", error);

		/* What time did this loop end? */
		nal->nanoTime(&exitTime);

		/* How much time was spent in the loop? */
		/* Use carried milliseconds. */
		milliSpent = (sjme_jint)((exitTime.full - enterTime.full) /
			INT64_C(1000000));
		milliRemain = (milliTime + milliCarry) - milliSpent;
		milliCarry = 0;

		/* The audio subsystem could be synchronous or asynchronous, however */
		/* in either case just go for both and sleep if there is still time */
		/* remaining in the loop. */
		if (milliRemain > SJME_SCRITCHAUDIO_MIN_SLEEP_MILLIS)
			sjme_thread_sleep(milliRemain, 0);
	}

	/* Finished. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchaudio_core_initActual(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc,
	sjme_attrInNotNull sjme_scritchaudio wrappedState,
	sjme_attrInValue sjme_jboolean isHigher,
	sjme_attrInNullable sjme_thread_mainFunc bindAudioThread)
{
	sjme_errorCode error;
	sjme_scritchaudio result;
	const sjme_nal* nal;
	
	if (inPool == NULL || outState == NULL || inImplFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return sjme_error_notImplemented(0);

	/* Use the given NAL. */
	nal = &sjme_nal_default;
	if (nal->nanoTime == NULL)
		return sjme_error_notImplemented(0);

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*result),
		(sjme_pointer*)&result)) ||
		result == NULL)
		goto fail_allocResult;

	/* Set base details. */
	result->pool = inPool;
	result->api = &sjme_scritchaudio_coreFunctions;
	result->impl = inImplFunc;
	result->intern = &sjme_scritchaudio_coreInterns;
	result->nal = nal;
	result->bindAudioThread = bindAudioThread;

	/* Is this wrapped? */
	if (wrappedState != NULL)
	{
		/* Use the wrapped lock. */
		result->lock = &wrappedState->baseLock;
		
		/* Set clock base, if wrapped use that as it was first. */
		memmove(&result->clock, &wrappedState->clock,
			sizeof(result->clock));
	}
	
	/* This is a primary driver. */
	else
	{
		/* Use the base lock. */
		result->lock = &result->baseLock;
		
		/* Otherwise, derive from the monotonic clock. */
		result->nal->nanoTime(&result->clock.clockBase);
		memmove(&result->clock.clock, &result->clock.clockBase,
			sizeof(result->clock.clock));
	}

	/* Use a "sleeping" rate so if manually polling the CPU does not burn. */
	sjme_atomic_s(sjme_jint, &result->pollDelayMillis,
		SJME_SCRITCHAUDIO_SLEEP_RATE_MS);
	sjme_atomic_s(sjme_jint, &result->pollDelayNanos,
		SJME_SCRITCHAUDIO_SLEEP_RATE_NS);

	/* Copy front end data. */
	if (initFrontEnd != NULL)
		sjme_frontEnd_copy(&result->frontEnd, initFrontEnd);

	/* Bind wrapped states together? */
	if (wrappedState != NULL)
	{
		/* Bind each other. */
		result->wrappedState = wrappedState;
		sjme_atomic_s(sjme_pointer, &wrappedState->topState, result);

		/* Take the wrapped state's thread information, if applicable. */
		result->loopThread = wrappedState->loopThread;
		result->loopThreadId = wrappedState->loopThreadId;
	}

	/* Call inner initialization. */
	if (sjme_error_is(error = result->impl->apiInit(result)))
		goto fail_apiInit;
	
	/* If the wrapped state must be manually polled, we like having threaded */
	/* audio. Note that even if there is no thread defined the operating */
	/* system could call back into the audio subroutine. */
	if (isHigher && ((wrappedState != NULL &&
		wrappedState->bugs.manualPoll) || result->bugs.manualPoll))
	{
		/* Create thread that loops infinitely. */
		if (sjme_error_is(error = sjme_thread_new(&result->loopThread,
			&result->loopThreadId,
			sjme_scritchaudio_core_poll, result)))
			goto fail_initThread;
		
		/* Await loop ready. */
		sjme_atomic_barrier();
		while (sjme_atomic_g(sjme_jint, &result->loopThreadReady) == 0)
		{
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
		}
	}

	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;

fail_noLoopIterate:
fail_initThread:
fail_noFormats:
fail_apiInit:
fail_allocResult:
	if (result != NULL)
		sjme_scritchaudio_core_destroy(result);
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchaudio_core_destroy(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_core_fallbackNext(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format origFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate origRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels origChannels,
	sjme_attrInOutNotNull sjme_scritchaudio_format* adjustFormat,
	sjme_attrInOutNotNull sjme_scritchaudio_rate* adjustRate,
	sjme_attrInOutNotNull sjme_scritchaudio_channels* adjustChannels)
{
	sjme_jint i;
	
	if (inState == NULL || adjustFormat == NULL || adjustRate == NULL ||
		adjustChannels == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use a fallback audio format. */
	(*adjustFormat) = sjme_scritchaudio_formatFallback[(*adjustFormat)];
	if ((*adjustFormat) == SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS)
	{
		/* Find the next rate to handle. */
		for (i = 0; sjme_scritchaudio_rateFallback[i] != 0; i++)
			if (sjme_scritchaudio_rateFallback[i] <= (*adjustRate))
			{
				(*adjustRate) = sjme_scritchaudio_rateFallback[i + 1];
				break;
			}
		
		/* Stop if the rate gets too low. */
		if ((*adjustRate) < SJME_SCRITCHAUDIO_RATE_HZ_8000)
		{
			/* Maybe the number of channels is not supported? */
			(*adjustChannels) /= 2;
			if ((*adjustChannels) <= 0)
				return SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT;

			/* We reduced the channel count, so revert the rate. */
			(*adjustRate) = origRate;
		}

		/* We reduced the rate, so revert the format. */
		(*adjustFormat) = origFormat;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchaudio_core_init(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc bindAudioThread)
{
	sjme_jboolean isSoftMixWrapper, needSoftMixWrapper;
	sjme_scritchaudio lower, higher;
	sjme_errorCode error;
	
	if (inPool == NULL || outState == NULL || inImplFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do we need the softmix wrapper? */
	isSoftMixWrapper = (inImplFunc == &sjme_scritchaudio_softmixFunctions);
	needSoftMixWrapper = !isSoftMixWrapper;
	
	/* Normal top-level initialization. */
	if (!needSoftMixWrapper)
		return sjme_scritchaudio_core_initActual(inPool, outState,
			initFrontEnd, inImplFunc, NULL, SJME_JNI_TRUE,
			bindAudioThread);

	/* Initialize the lower level state. */
	lower = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&lower, NULL, inImplFunc, NULL, SJME_JNI_FALSE, NULL)) ||
		lower == NULL)
		goto fail_initLower;

	/* Initialize upper wrapper. */
	higher = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&higher, initFrontEnd, &sjme_scritchaudio_softmixFunctions,
		lower, SJME_JNI_TRUE, bindAudioThread)) ||
		higher == NULL)
		goto fail_initHigher;

	/* Use the higher state. */
	*outState = higher;
	return SJME_ERROR_NONE;
	
fail_initHigher:
	if (higher != NULL)
		sjme_scritchaudio_core_destroy(higher);
fail_initLower:
	if (lower != NULL)
		sjme_scritchaudio_core_destroy(lower);
	return sjme_error_default(error);
}
