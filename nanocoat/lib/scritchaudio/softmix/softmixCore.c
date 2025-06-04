/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchaudio/softmix/softmixIntern.h"
#include "sjme/multithread.h"

/**
 * Software Mixer implementation functions.
 *
 * @since 2025/05/10
 */
const sjme_scritchaudio_implFunctions sjme_scritchaudio_softmixFunctions =
{
	.apiInit = sjme_scritchaudio_softmix_apiInit,
	.disconnect = sjme_scritchaudio_softmix_disconnect,
	.loopIterate = sjme_scritchaudio_softmix_loopIterate,
	.queryMidiPorts = sjme_scritchaudio_softmix_queryMidiPorts,
	.sourceAttach = sjme_scritchaudio_softmix_sourceAttach,
	.streamCreate = sjme_scritchaudio_softmix_streamCreate,
};

static sjme_thread_result sjme_attrThreadCall sjme_scritchaudio_softmix_poll(
	sjme_attrInNotNull sjme_thread_parameter rawState)
{
	sjme_errorCode error;
	sjme_scritchaudio inState;
	sjme_jint nanoSum, milliCarry;

	/* Recover state. */
	inState = rawState;
	if (inState == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Does a binder need to be called? */
	if (inState->bindAudioThread != NULL)
		inState->bindAudioThread(inState);
	
	/* Await loop ready. */
	sjme_atomic_barrier();
	while (sjme_atomic_sjme_jint_get(&inState->loopThreadReady) == 0)
	{
		sjme_atomic_barrier();
		sjme_thread_yield();
		sjme_atomic_barrier();
	}
	
	/* Enter threading loop. */
	for (nanoSum = 0, milliCarry = 0;;)
	{
		/* Call loop iteration handler. */
		if (sjme_error_is(error = inState->api->loopIterate(inState)))
			sjme_message("Audio error: %d", error);

		/* Keep everything at millisecond accuracy. Thus round up nanos */
		/* to millis by carrying. */
		nanoSum += sjme_atomic_sjme_jint_get(&inState->pollDelayNanos);
		if (nanoSum >= 1000000)
		{
			milliCarry++;
			nanoSum -= 1000000;
		}
		
		/* Use the polling delay time to sleep until more audio is ready */
		/* or more data can be pushed to the buffer. */
		/* Use carried milliseconds. */
		sjme_thread_sleep(
			sjme_atomic_sjme_jint_get(&inState->pollDelayMillis) +
			milliCarry, 0);
		milliCarry = 0;
		sjme_thread_yield();
	}

	/* Finished. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchaudio_softmix_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchaudio_softmix_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState)
{
	sjme_errorCode error;
	sjme_scritchaudio wrappedStated;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the wrapped state. */
	wrappedStated = inState->wrappedState;
	if (wrappedStated == NULL)
		return SJME_ERROR_HEADLESS_AUDIO;

	/* If the wrapped state must be manually polled, we like having threaded */
	/* audio. Note that even if there is no thread defined the operating */
	/* system could call back into the audio subroutine. */
	if (wrappedStated->bugs.manualPoll)
	{
		/* There needs to be a loop iterator here. */
		if (wrappedStated->impl->loopIterate == NULL)
		{
			error = sjme_error_notImplemented(0);
			goto fail_noLoopIterate;
		}
		
		/* Create thread that loops infinitely. */
		if (sjme_error_is(error = sjme_thread_new(&inState->loopThread,
			&inState->loopThreadId,
			sjme_scritchaudio_softmix_poll, inState)))
			goto fail_initThread;
	}

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_initThread:
fail_noLoopIterate:
	return sjme_error_default(error);
}
