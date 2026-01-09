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
	sjme_sm(.pollEvent, sjme_scritchaudio_core_pollEvent),
	sjme_sm(.pollManual, sjme_scritchaudio_core_pollManual),
	sjme_sm(.streamCreate, sjme_scritchaudio_core_streamCreate),
};

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

	/* Always use the base lock as there will be an overlying and underlying */
	/* stream. The lock is really only needed for buffer protection. */
	result->lock = &result->baseLock;

	/* Is this wrapped? */
	if (wrappedState != NULL)
	{
		/* Set clock base, if wrapped use that as it was first. */
		memmove(&result->clock, &wrappedState->clock,
			sizeof(result->clock));
	}
	
	/* This is a primary driver. */
	else
	{
		/* Otherwise, derive from the monotonic clock. */
		result->nal->nanoTime(&result->clock.clockBase);
		memmove(&result->clock.clock, &result->clock.clockBase,
			sizeof(result->clock.clock));
	}

	/* Copy front end data. */
	if (initFrontEnd != NULL)
		sjme_frontEnd_copy(&result->frontEnd, initFrontEnd);

	/* Bind wrapped states together? */
	if (wrappedState != NULL)
	{
		/* Bind each other. */
		result->wrappedState = wrappedState;
		sjme_atomic_s(sjme_pointer, &wrappedState->topState, result);
	}

	/* Call inner initialization. */
	if (sjme_error_is(error = result->impl->apiInit(result)))
		goto fail_apiInit;

	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;

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
	sjme_jboolean needSoftMixWrapper;
	sjme_scritchaudio lower, higher;
	sjme_errorCode error;
	
	if (inPool == NULL || outState == NULL || inImplFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do we need the software mixing wrapper? */
	needSoftMixWrapper = !inImplFunc->allFormatsOwnMixing;
	
	/* Normal top-level initialization. */
	if (!needSoftMixWrapper)
		return sjme_scritchaudio_core_initActual(inPool, outState,
			initFrontEnd, inImplFunc, NULL, SJME_JNI_TRUE,
			bindAudioThread);

	/* Initialize the lower level state. */
	lower = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&lower, initFrontEnd, inImplFunc, NULL, SJME_JNI_FALSE,
		bindAudioThread)) ||
		lower == NULL)
		goto fail_initLower;

	/* Initialize upper wrapper. */
	higher = NULL;
	if (sjme_error_is(error = sjme_scritchaudio_core_initActual(inPool,
		&higher, initFrontEnd, &sjme_scritchaudio_softmixFunctions,
		lower, SJME_JNI_TRUE, bindAudioThread)) ||
		higher == NULL)
		goto fail_initHigher;
	
	/* Make sure all the bugs are synced. */
	/* Manual poll. */
	lower->bugs.manualPoll |= higher->bugs.manualPoll;
	higher->bugs.manualPoll |= lower->bugs.manualPoll;

	/* Event poll. */
	lower->bugs.eventPoll |= higher->bugs.eventPoll;
	higher->bugs.eventPoll |= lower->bugs.eventPoll;

	/* Write blocks. */
	lower->bugs.outputBlocks |= higher->bugs.outputBlocks;
	higher->bugs.outputBlocks |= lower->bugs.outputBlocks;

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
