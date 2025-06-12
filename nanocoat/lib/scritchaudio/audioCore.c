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
#include "lib/scritchaudio/softmix/softmixIntern.h"

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
	sjme_sm(.queryMidiPorts, sjme_scritchaudio_core_queryMidiPorts),
	sjme_sm(.loopIterate, sjme_scritchaudio_core_loopIterate),
	sjme_sm(.sourceAttach, sjme_scritchaudio_core_sourceAttach),
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

static sjme_errorCode sjme_scritchaudio_core_initActual(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc,
	sjme_attrInNotNull sjme_scritchaudio wrappedStated,
	sjme_attrInValue sjme_jboolean isHigher,
	sjme_attrInNullable sjme_thread_mainFunc bindAudioThread)
{
	sjme_errorCode error;
	sjme_scritchaudio result;
	sjme_scritchaudio_stream onlyStream;
	const sjme_nal* nal;
	sjme_scritchaudio_format origFormat, inFormat;
	sjme_scritchaudio_rate origRate, inRate;
	sjme_scritchaudio_channels origChannels, inChannels;
	sjme_jint i, n;
	
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

	/* Set clock base, if wrapped use that as it was first. */
	if (wrappedStated != NULL)
		memmove(&result->clock, &wrappedStated->clock,
			sizeof(result->clock));

	/* Otherwise, derive from the monotonic clock. */
	else
	{
		result->nal->nanoTime(&result->clock.clockBase);
		memmove(&result->clock.clock, &result->clock.clockBase,
			sizeof(result->clock.clock));
	}

	/* Use a "sleeping" rate so if manually polling the CPU does not burn. */
	sjme_atomic_sjme_jint_set(&result->pollDelayMillis,
		SJME_SCRITCHAUDIO_SLEEP_RATE_MS);
	sjme_atomic_sjme_jint_set(&result->pollDelayNanos,
		SJME_SCRITCHAUDIO_SLEEP_RATE_NS);

	/* Copy front end data. */
	if (initFrontEnd != NULL)
		memmove(&result->frontEnd, initFrontEnd, sizeof(result->frontEnd));

	/* Bind wrapped states together? */
	if (wrappedStated != NULL)
	{
		/* Bind each other. */
		result->wrappedState = wrappedStated;
		sjme_atomic_sjme_pointer_set(&wrappedStated->topState, result);

		/* Take the wrapped state's thread information, if applicable. */
		result->loopThread = wrappedStated->loopThread;
		result->loopThreadId = wrappedStated->loopThreadId;
	}

	/* Call inner initialization. */
	if (sjme_error_is(error = result->impl->apiInit(result)))
		goto fail_apiInit;

	/* Mark loop thread as ready. */
	sjme_atomic_sjme_jint_set(&result->loopThreadReady, 1);

	/* Only create the stream when this is the higher level layer. */
	if (isHigher)
	{
		/* Use an automatically determined format. */
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
		inFormat = SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32;
#else
		inFormat = SJME_SCRITCHAUDIO_FORMAT_INT_S32;
#endif
		inRate = SJME_SCRITCHAUDIO_RATE_HZ_48000;
		inChannels = SJME_SCRITCHAUDIO_CHANNELS_STEREO;

		/* Remember the original values, for loop returning. */
		origFormat = inFormat;
		origRate = inRate;
		origChannels = inChannels;

		/* Fallback to less precise formats. */
		onlyStream = NULL;
		while (onlyStream == NULL)
		{
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
			/* Debug. */
			sjme_message("streamCreate(%d, %d, %d)",
				inFormat, inRate, inChannels);
#endif
		
			/* Try to use the requested format. */
			if (sjme_error_is(error = result->intern->streamCreate(
				result, &onlyStream, "SquirrelJME",
				inFormat, inRate, inChannels)) ||
				onlyStream == NULL)
			{
				/* Only check against unsupported format. */
				if (error != SJME_ERROR_UNSUPPORTED_AUDIO_FORMAT)
					goto fail_noFormats;

				/* Reduce the rate. */
				if (sjme_error_is(error = result->intern->fallbackNext(
					result, origFormat, origRate, origChannels,
					&inFormat, &inRate, &inChannels)))
					goto fail_noFormats;
			}
		}

		/* Set the only audio stream. */
		result->stream = onlyStream;
	}

	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;

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
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
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
