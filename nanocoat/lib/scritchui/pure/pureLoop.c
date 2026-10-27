/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/pure/pure.h"

sjme_errorCode sjme_scritchui_pure_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui_loopQueueChunk* currentChunk;
	sjme_scritchui_loopQueueChunk* newChunk;
	sjme_scritchui_loopQueueItem* checkItem;
	sjme_scritchui_loopQueue* loopQueue;
	sjme_jint i, n;
	
	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock the loop queue. */
	loopQueue = &inState->loopQueue;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&loopQueue->lock)))
		return sjme_error_default(error);
	
	/* Go through chunks. */
	currentChunk = loopQueue->firstChunk;
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
					
					/* Becomes first item?. */
					if (loopQueue->next == NULL)
						loopQueue->next = checkItem;
					
					/* Either become the last or be after it. */
					if (loopQueue->last == NULL)
						loopQueue->last = NULL;
					else
					{
						loopQueue->last->next = checkItem;
						loopQueue->last = checkItem;
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
		if (loopQueue->firstChunk == NULL)
			loopQueue->firstChunk = newChunk;
		else
		{
			newChunk->nextChunk = loopQueue->firstChunk;
			loopQueue->firstChunk = newChunk;
		}
		
		/* Try this new chunk. */
		currentChunk = newChunk;
	}

	/* Release the lock. */
skip_success:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&loopQueue->lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_alloc:
	/* Release the before failing lock. */
	sjme_thread_spinLockRelease(&loopQueue->lock, NULL);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_pure_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_message("Pure iterate.");
	
	return SJME_ERROR_NONE;
}
