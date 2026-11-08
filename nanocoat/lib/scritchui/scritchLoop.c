/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "sjme/atomic.h"
#include "sjme/debug.h"

typedef struct sjme_scritchui_core_waitData
{
	/** The signal to trigger on. */
	sjme_atomic(sjme_jint) signal;
	
	/** The callback to execute. */
	sjme_thread_mainFunc callback;
	
	/** Value passed to the callback. */
	sjme_thread_parameter anything;
	
	/** The result of the call. */
	sjme_errorCode result;
} sjme_scritchui_core_waitData;

static sjme_thread_result sjme_attrThreadCall sjme_scritchui_core_waitAdapter(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui_core_waitData* waitData;
	sjme_thread_result result;
	sjme_errorCode error;
	
	/* Recover data. */
	waitData = (sjme_scritchui_core_waitData*)anything;
	if (waitData == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Light barrier to read the wait data. */
	sjme_atomic_light();
	
	/* Synchronous call. */
	result = waitData->callback(waitData->anything);
	error = SJME_THREAD_RESULT_AS_ERROR(result);
	waitData->result = error;
	
	/* Signal that wait is complete. */
	sjme_atomic_s(sjme_jint, &waitData->signal, 1);
	
	/* Heavy barrier for signaling. */
	sjme_atomic_heavy();
	
#if defined(SJME_CONFIG_DEBUG)
	/* Failed loop? */
	if (sjme_error_is(error))
		sjme_message("sui_c_wA() -> %d", (int)error);
#endif
	
	/* This technically is a success. */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_core_loopExecute(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_jboolean inThread;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If an external is specified, use that instead. */
	if (inState->externals != NULL &&
		inState->externals->externalLoopExecute != NULL)
		return inState->externals->externalLoopExecute(inState,
			callback, anything);
	
	/* If there is a native implementation of this, use it as it probably */
	/* knows more about how things should be executed directly or scheduled. */
	if (inState->impl->loopExecute != NULL)
		return inState->impl->loopExecute(inState, callback, anything);
	
	/* Check if we are in the loop thread. */
	inThread = SJME_JNI_FALSE;
	if (sjme_error_is(error = inState->api->loopIsInThread(inState,
		&inThread)))
		return sjme_error_default(error);
	
	/* Are we in the execution loop? Then call directly */
	if (inThread)
		return SJME_THREAD_RESULT_AS_ERROR(callback(anything));

	/* Use standard execution. */
	return inState->api->loopExecuteLater(inState, callback, anything);
}

sjme_errorCode sjme_scritchui_core_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If an external is specified, use that instead. */
	if (inState->externals != NULL &&
		inState->externals->externalLoopExecuteLater != NULL)
		return inState->externals->externalLoopExecuteLater(inState,
			callback, anything);
	
	/* Not implemented? */
	if (inState->impl->loopExecuteLater == NULL)
		return sjme_error_notImplemented(0);
	
	/* Call execution directly. */
	return inState->impl->loopExecuteLater(inState, callback, anything);
}

sjme_errorCode sjme_scritchui_core_loopExecuteWait(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui_core_waitData waitData;
	sjme_jboolean inThread, lockStep, signal;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If an external is specified, use that instead. */
	if (inState->externals != NULL &&
		inState->externals->externalLoopExecuteWait != NULL)
		return inState->externals->externalLoopExecuteWait(inState,
			callback, anything);
	
	/* If executed wait is directly available we do not need a shim as */
	/* the implementation probably knows more than we do. */
	if (inState->impl->loopExecuteWait != NULL)
		return inState->impl->loopExecuteWait(inState, callback, anything);
	
	/* Check if we are in the loop thread. */
	inThread = SJME_JNI_FALSE;
	if (sjme_error_is(error = inState->api->loopIsInThread(inState,
		&inThread)))
		return sjme_error_default(error);
	
	/* Are we in the execution loop? Then call directly */
	if (inThread)
		return SJME_THREAD_RESULT_AS_ERROR(callback(anything));
	
	/* Initialize wait. */
	memset(&waitData, 0, sizeof(waitData));
	sjme_atomic_s(sjme_jint, &waitData.signal, 0);
	waitData.callback = callback;
	waitData.anything = anything;
	
	/* A light sync is needed so the other thread can read this. */
	sjme_atomic_light();
	
	/* Execute call. */
	if (sjme_error_is(error = sjme_scritchui_core_loopExecute(
		inState, sjme_scritchui_core_waitAdapter,
		&waitData)))
		return sjme_error_default(error);
	
	/* Wait for termination. */
	for (lockStep = SJME_JNI_FALSE;; lockStep = !lockStep)
	{
		if (!lockStep)
			signal = (0 != sjme_atomic_gl(sjme_jint, &waitData.signal));
		else
			signal = (0 != sjme_atomic_g(sjme_jint, &waitData.signal));
		
		/* Done? */
		if (signal)
			break;
		
		/* Yield every other step. */
		if (!lockStep)
			sjme_thread_yield();
	}
	
	/* Heavy barrier for full synchronization here. */
	sjme_atomic_heavy();
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Failed loop? */
	if (sjme_error_is(error))
		sjme_message("sui_c_lEW() -> %d", (int)waitData.result);
#endif
	
	/* Return the result of the call. */
	return waitData.result;
}

sjme_errorCode sjme_scritchui_core_loopIsInThread(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_jboolean* outInThread)
{
	sjme_errorCode error;
	sjme_thread self;
	
	if (inState == NULL || outInThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check current thread. */
	self = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(&self)) ||
		self == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* Are we in the loop? */
	*outInThread = sjme_thread_equal(self,
		inState->loopThread);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	sjme_errorCode error;
	sjme_jboolean wrapped;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Implementation handles loop iteration. */
	/* Only forward if not wrapped nor manually polled. */
	wrapped = (inState->wrappedState != NULL);
	if (inState->impl->loopIterate != NULL &&
		wrapped && !inState->bugs.manualEventPoll)
	{
		/* Perform normal loop calls. */
		if (sjme_error_is(error = inState->impl->loopIterate(inState, blocking,
			outHasTerminated)))
			return sjme_error_default(error);
	}
	
	/* Otherwise if blocking, wait until ScritchUI terminates. */
	else if (blocking)
	{
		/* Loop forever. */
		for (;;)
		{
			/* Exiting? */
			if (sjme_atomic_gl(sjme_jint, &inState->isTerminated) != 0)
				return SJME_ERROR_EXIT;
			
			/* Sleep for a while. */
			sjme_thread_sleep(1000, 0);
		}
	}

	/* Success? */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_intern_loopQueueNext(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_attrOutModify
		sjme_scritchui_loopQueue* inQueue,
	sjme_attrOutNotNull sjme_attrOutModify
		sjme_scritchui_loopQueueItem* outItem)
{
	sjme_scritchui_loopQueueItem* atItem;
	sjme_jint loopCycle;
	
	if (inState == NULL || inQueue == NULL || outItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* What is the current cycle? This sets the execution limit. */
	loopCycle = sjme_atomic_g(sjme_jint, &inQueue->loopCycle);
	
	/* Go through each item. */
	for (atItem = inQueue->first; atItem != NULL; atItem = atItem->next)
	{
		/* Item would execute too soon? */
		if (atItem->loopCycle < loopCycle)
			continue;
		
		/* Select this item, copy it out. */
		/* prev/next get wiped as they are not valid. */
		memmove(outItem, atItem, sizeof(*atItem));
		outItem->next = NULL;
		outItem->prev = NULL;
		
		/* No longer first? */
		if (inQueue->first == atItem)
			inQueue->first = atItem->next;
		
		/* No longer last? */
		if (inQueue->last == atItem)
			inQueue->last = atItem->prev;
		
		/* Unlink from the chain. */
		if (atItem->prev != NULL)
			atItem->prev->next = atItem->next;
		if (atItem->next != NULL)
			atItem->next->prev = atItem->prev;
		
		/* Wipe so it can be reclaimed. */
		memset(atItem, 0, sizeof(*atItem));
		
		/* Heavy barrier since we did wipe this. */
		sjme_atomic_heavy();
		
		/* Done! */
		return SJME_ERROR_NONE;
	}
	
	/* Not found! */
	return SJME_ERROR_NO_SUCH_ELEMENT;
}

sjme_errorCode sjme_scritchui_core_intern_loopQueueDrain(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_attrOutModify
		sjme_scritchui_loopQueue* inQueue)
{
	sjme_errorCode error;
	sjme_scritchui_loopQueueItem item;
	sjme_thread_result result;
	sjme_jboolean bumpCycle;
	
	if (inState == NULL || inQueue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cycle starts as initially unbumped. */
	bumpCycle = SJME_JNI_FALSE;
	
	/* Continually execute items in the loop. */
	for (;;)
	{
		/* Lock the loop queue. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&inQueue->lock)))
			return sjme_error_default(error);
		
		/* Go through loop chunks to find an item to execute. */
		memset(&item, 0, sizeof(item));
		if (sjme_error_is(error = sjme_scritchui_core_intern_loopQueueNext(
			inState, inQueue, &item)))
		{
			/* It is valid to find nothing. */
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
			{
				/* Release before fail. */
				sjme_thread_spinLockRelease(&inQueue->lock, NULL);
				
				/* Now fail. */
				return sjme_error_default(error);
			}
			
			/* Ensure this is invalidated. */
			item.function = NULL;
		}
		
		/* Bump cycle if there is an item here? */
		if (!bumpCycle && item.function != NULL)
		{
			/* Can no longer happen again in this run. */
			bumpCycle = SJME_JNI_TRUE;
			
			/* Increment by one. */
			sjme_atomic_ga(sjme_jint, &inQueue->loopCycle, 1);
		}
		
		/* Release the lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&inQueue->lock, NULL)))
			return sjme_error_default(error);
		
		/* Nothing to do? */
		if (item.function == NULL)
			break;
		
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
		/* Debug. */
		sjme_message("LQ EXEC %p %p",
			(void*)item.function, (void*)item.anything);
#endif
		
		/* Execute it. */
		result = item.function(item.anything);
			
		/* If it failed, just warn. */
		if (sjme_error_is(error = SJME_THREAD_RESULT_AS_ERROR(result)))
			sjme_message("Callback error: %d", (int)error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
	
sjme_errorCode sjme_scritchui_core_intern_loopQueuePush(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_attrOutModify
		sjme_scritchui_loopQueue* inQueue,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui_loopQueueChunk* currentChunk;
	sjme_scritchui_loopQueueItem* checkItem;
	sjme_scritchui_loopQueueChunk* atChunk;
	sjme_scritchui_loopQueueItem* atItem;
	sjme_jint i, n;
	
	if (inState == NULL || inQueue == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock the loop queue. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inQueue->lock)))
		return sjme_error_default(error);
	
	/* Light barrier before checking everything. */
	sjme_atomic_light();
	
	/* Find a chunk with a free slot. */
	atChunk = NULL;
	atItem = NULL;
	for (currentChunk = inQueue->firstChunk; currentChunk != NULL;
		currentChunk = currentChunk->nextChunk)
	{
		/* Find an item slot to place in. */
		for (i = 0, n = SJME_SCRITCHUI_LOOP_SIZE; i < n; i++)
		{
			/* Check this item, skip claimed ones. */
			checkItem = &currentChunk->items[i];
			if (checkItem->function != NULL)
				continue;
			
			/* Set found chunk. */
			atChunk = currentChunk;
			atItem = checkItem;
			break;
		}
		
		/* Stop if something was found. */
		if (atChunk != NULL && atItem != NULL)
			break;
	}
	
	/* Need to allocate another chunk? */
	if (atChunk == NULL || atItem == NULL)
	{
		/* Need to make a new chunk, since nothing is left. */
		currentChunk = NULL;
		if (sjme_error_is(error = sjme_alloc(inState->pool,
			sizeof(*currentChunk), (sjme_pointer*)&currentChunk)))
			goto fail_alloc;
		
		/* Link in prev chain. */
		if (inQueue->lastChunk != NULL)
		{
			inQueue->lastChunk->nextChunk = currentChunk;
			currentChunk->prevChunk = inQueue->lastChunk;
			inQueue->lastChunk = currentChunk;
		}
		
		/* First chunk added? */
		if (inQueue->firstChunk == NULL)
			inQueue->firstChunk = currentChunk;
		if (inQueue->lastChunk == NULL)
			inQueue->lastChunk = currentChunk;
		
		/* Use this new chunk, we can automatically use the first item. */
		atChunk = currentChunk;
		atItem = &currentChunk->items[0];
	}
			
	/* Set parameters. */
	atItem->function = callback;
	atItem->anything = anything;
	atItem->loopCycle = sjme_atomic_g(sjme_jint,
		&inQueue->loopCycle) + 1;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("LQ PUSH %p %p %d",
		(void*)callback, (void*)anything,
		(int)atItem->loopCycle);
#endif
	
	/* Link in the prev chain. */
	if (inQueue->last != NULL)
	{
		atItem->prev = inQueue->last;
		inQueue->last->next = atItem;
		inQueue->last = atItem;
	}
	
	/* Is now the first item? */
	if (inQueue->first == NULL)
		inQueue->first = atItem;
	if (inQueue->last == NULL)
		inQueue->last = atItem;
	
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inQueue->lock, NULL)))
		return sjme_error_default(error);
	
	/* Heavy barrier. */
	sjme_atomic_heavy();
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_alloc:
	/* Release the before failing lock. */
	sjme_thread_spinLockRelease(&inQueue->lock, NULL);
	
	return sjme_error_default(error);
}
