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
	
#if defined(SJME_CONFIG_DEBUG)
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
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Only forward if not wrapped nor manually polled. */
	if (inState->wrappedState != NULL && !inState->bugs.manualEventPoll)
		return SJME_ERROR_NONE;

	/* Missing implementation of iterate? */
	if (inState->impl->loopIterate == NULL)
		return sjme_error_notImplemented(0);

	/* Perform normal loop calls first. */
	if (sjme_error_is(error = inState->impl->loopIterate(inState, blocking,
		outHasTerminated)))
		return sjme_error_default(error);

	/* If wrapped, do nothing more. */
	if (inState->wrappedState != NULL)
		return error;

#if defined(SJME_CONFIG_HAS_BROKEN_CODE)
#endif

	/* Success? */
	return error;
}
sjme_errorCode sjme_scritchui_core_intern_loopQueueDrain(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_attrOutModify
		sjme_scritchui_loopQueue* inQueue)
{
	sjme_errorCode error;
	sjme_scritchui_loopQueueItem* loopItem;
	sjme_scritchui_loopQueueItem* prevItem;
	sjme_scritchui_loopQueueItem execItem;
	sjme_thread_result result;
	sjme_jint loopCycle;
	
	if (inState == NULL || inQueue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Which loop cycle is being run? */
	loopCycle = sjme_atomic_ga(sjme_jint, &inQueue->loopCycle, 1);
	
	/* Run all loop items. */
	loopItem = NULL;
	for (;;)
	{
		/* Lock the loop queue. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&inQueue->lock)))
			return sjme_error_default(error);
		
		/* Are there any loop queue items to run? */
		memset(&execItem, 0, sizeof(execItem));
		for (prevItem = NULL, loopItem = inQueue->next; loopItem != NULL;
			prevItem = loopItem)
		{
			/* Ignore items intended for another cycle. */
			loopItem = inQueue->next;
			if (loopCycle < loopItem->loopCycle)
			{
				sjme_emitB("SKIP");
				continue;
			}
			
			sjme_emitB("EXEC");
			
			/* Relink next item. */
			if (loopItem == inQueue->next)
				inQueue->next = loopItem->next;
			if (prevItem != NULL)
				prevItem->next = loopItem->next;
			loopItem->next = NULL;

			/* Was this the last item? If it was, then clear it. */
			if (loopItem == inQueue->last)
			{
				inQueue->last = NULL;
				break;
			}

			/* Copy it out. */
			memmove(&execItem, loopItem, sizeof(execItem));

			/* Clear the loop item. */
			memset(loopItem, 0, sizeof(*loopItem));

			/* We are executing this. */
			break;
		}

		/* Release the lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&inQueue->lock, NULL)))
			return sjme_error_default(error);

		/* Nothing left to run? */
		if (execItem.function == NULL)
			break;
		
#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("LQ EXEC %p %p",
			(void*)execItem.function, (void*)execItem.anything);
#endif
		
		/* Execute it. */
		result = execItem.function(execItem.anything);
			
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
	sjme_scritchui_loopQueueChunk* newChunk;
	sjme_scritchui_loopQueueItem* checkItem;
	sjme_jint i, n;
	
	if (inState == NULL || inQueue == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock the loop queue. */
	inQueue = &inState->loopQueue;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inQueue->lock)))
		return sjme_error_default(error);
	
	/* Go through chunks. */
	currentChunk = inQueue->firstChunk;
	for (;;)
	{
		/* Find a free item to claim. */
		if (currentChunk != NULL)
		{
			/* Find an item to place in. */
			for (i = 0, n = SJME_SCRITCHUI_LOOP_SIZE; i < n; i++)
			{
				/* Check this item. */
				checkItem = &currentChunk->items[i];
				
				/* Not claimed? */
				if (checkItem->function == NULL)
				{
					/* Set parameters. */
					checkItem->function = callback;
					checkItem->anything = anything;
					checkItem->loopCycle = sjme_atomic_g(sjme_jint,
						&inQueue->loopCycle) + 1;

#if defined(SJME_CONFIG_DEBUG)
					/* Debug. */
					sjme_message("LQ PUSH %p %p",
						(void*)callback, (void*)anything);
#endif
					
					/* Becomes first item?. */
					if (inQueue->next == NULL)
						inQueue->next = checkItem;
					
					/* Either become the last or be after it. */
					if (inQueue->last == NULL)
						inQueue->last = NULL;
					else
					{
						inQueue->last->next = checkItem;
						inQueue->last = checkItem;
					}
					
					/* Success! So skip to releasing. */
					goto skip_success;
				}
			}
			
			/* If there is a next chunk, check that. */
			if (currentChunk->nextChunk != NULL)
			{
				currentChunk = currentChunk->nextChunk;
				continue;
			}
		}
		
		/* Need to make a new chunk, since nothing is left. */
		newChunk = NULL;
		if (sjme_error_is(error = sjme_alloc(inState->pool,
			sizeof(*newChunk), (sjme_pointer*)&newChunk)))
			goto fail_alloc;
		
		/* Link in new chunk. */
		if (inQueue->firstChunk == NULL)
			inQueue->firstChunk = newChunk;
		else
		{
			newChunk->nextChunk = inQueue->firstChunk;
			inQueue->firstChunk = newChunk;
		}
		
		/* Try this new chunk. */
		currentChunk = newChunk;
	}

	/* Release the lock. */
skip_success:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inQueue->lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_alloc:
	/* Release the before failing lock. */
	sjme_thread_spinLockRelease(&inQueue->lock, NULL);
	
	return sjme_error_default(error);
}
