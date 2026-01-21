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
	sjme_scritchaudio wrappedState;
	
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

	/* Lock the stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		inStream->connection.lock)))
		goto fail_lockGrab;

	/* Allocate resultant source. */
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult;

	/* Use the same lock the stream uses. */
	result->connection.lock = inStream->connection.lock;

	/* Initialize state. */
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
	sjme_atomic_s(sjme_jint, &inStream->pollDelayMillis,
		SJME_SCRITCHAUDIO_POLL_DELAY_MILLIS);
	sjme_atomic_s(sjme_jint, &inStream->pollDelayNanos, 0);

	/* Success! */
	*outSource = result;
	return SJME_ERROR_NONE;

fail_peerConnect:
fail_growList:
fail_implInit:
fail_allocResult:
	sjme_thread_spinLockRelease(inState->lock, NULL);
	
fail_lockRelease:
fail_lockGrab:
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
	sjme_jboolean hasTop, hasWrapped;
	
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

	/* If manual polling, all locks must use the same state. */
	if (inState->bugs.manualPoll || inState->bugs.eventPoll)
		result->connection.lock = inState->lock;
	else
		result->connection.lock = &result->baseLock;

	/* Set stream details. */
	result->connection.inState = inState;
	result->connection.type = SJME_SCRITCHAUDIO_CONN_STREAM;
	result->format = inFormat;
	result->rate = inRate;
	result->channels = inChannels;

	/* Set a base initial time. */
	sjme_atomic_s(sjme_jint, &result->pollDelayMillis,
		SJME_SCRITCHAUDIO_POLL_DELAY_MILLIS);
	sjme_atomic_s(sjme_jint, &result->pollDelayNanos,
		0);

	/* Forward to implementation. */
	if (sjme_error_is(error = inState->impl->streamCreate(inState,
		result, inName, inFormat, inRate, inChannels)) || result == NULL)
		goto fail_implCreate;
	
	/* Stream initializer did not do its own render calculation? */
	if (result->data.renderInfo.bufSize == 0 ||
		result->data.renderInfo.rate == 0 ||
		result->data.renderInfo.channels == 0)
		if (sjme_error_is(error = inState->intern->calcRenderInfo(
			inState, result, NULL, &result->data.renderInfo)))
			goto fail_calcRender;
	
	/* Allocate sample buffer, if none were allocated. */
	if (result->data.buffer == NULL)
		if (sjme_error_is(error = sjme_alloc(inState->pool,
			result->data.renderInfo.bufSize, &result->data.buffer)) ||
			result->data.buffer == NULL)
			goto fail_allocBuf;
	
	/* Release the state. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		inState->lock, NULL)))
		goto fail_releaseLock;

	/* No stream has been set yet? */
	if (inState->stream == NULL)
		inState->stream = result;

	/* Each stream gets its own thread if manual or event based polling */
	/* is used. This means that the system is not capable of multi-threaded */
	/* audio. */
	/* Note that the lower level that is closer to the sound card owns */
	/* the thread. */
	hasTop = (sjme_atomic_g(sjme_pointer, &inState->topState) != NULL);
	hasWrapped = (inState->wrappedState != NULL);
	if ((inState->bugs.manualPoll || inState->bugs.eventPoll) &&
		(!hasWrapped || hasTop))
	{
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("(%p) Polling thread preparing...",
			inState);
#endif
		
		/* Await loop unready. */
		/* Note if the output blocks, we really do not want to recreate */
		/* the thread dynamically on each stream. */
		if (!inState->bugs.outputBlocks)
		{
			sjme_atomic_barrier();
			while (sjme_atomic_g(sjme_jint, &result->loopThreadReady) != 0)
			{
				sjme_atomic_barrier();
				sjme_thread_yield();
				sjme_atomic_barrier();
			}
		}
		
		/* Create thread that loops infinitely. */
		if (sjme_atomic_g(sjme_jint, &result->loopThreadReady) == 0)
		{
			result->loopThread = SJME_THREAD_NULL;
			if (sjme_error_is(error = sjme_thread_new(&result->loopThread,
				&result->loopThreadId,
				(inState->bugs.eventPoll ? inState->intern->pollEvent :
					inState->intern->pollManual),
				result)) || result->loopThread == SJME_THREAD_NULL)
				goto fail_initThread;
		}
		
		/* Await loop ready. */
		sjme_atomic_barrier();
		while (sjme_atomic_g(sjme_jint, &result->loopThreadReady) == 0)
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

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("createStream(%p, %d, %d, %d) -> Success!",
		result, inFormat, inRate, inChannels);
#endif
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_allocBuf:
fail_calcRender:
fail_implCreate:
	/* Release the lock before failing. */
	sjme_thread_spinLockRelease(inState->lock, NULL);

fail_initThread:
fail_grabLock:
fail_releaseLock:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
	return sjme_error_default(error);
#undef GROW_SIZE
}
