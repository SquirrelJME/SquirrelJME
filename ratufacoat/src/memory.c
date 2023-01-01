/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Native memory functions.
 *
 * @since 2019/06/25
 */

#include <string.h>

#include "sjmerc.h"
#include "memory.h"
#include "memoryintern.h"
#include "memio/atomic.h"
#include "debug.h"
#include "error.h"
#include "lock.h"
#include "counter.h"
#include "memio/memtag.h"

/** Lock on memory operations to ensure that all of them are atomic. */
static sjme_spinLock sjme_memLock;

/** The last @c sjme_memIo_atomicPointer in the memory chain. */
static sjme_memIo_atomicPointer sjme_lastMemNode;

sjme_memStat sjme_memStats = {{0}, {0}};

sjme_jboolean sjme_getMemNode(void* inPtr, sjme_memNode** outNode,
	sjme_error* error)
{
	sjme_memNode* result;

	/* Check. */
	if (inPtr == NULL || outNode == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}

	/* Determine and check validity of the node. */
	result = SJME_POINTER_OFFSET_LONG(inPtr, -sizeof(sjme_memNode));
	if (result->key != SJME_MEM_NODE_KEY)
	{
		sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, result->key);

		return sjme_false;
	}

	/* Set and use it. */
	*outNode = result;
	return sjme_true;
}

/**
 * Handles garbage collection for memory when the node count reaches zero.
 *
 * @param counter The counter to clear.
 * @param error Any potential error state.
 * @return If the handle was successful.
 * @since 2022/12/10
 */
static sjme_jboolean sjme_mallocGcHandle(sjme_counter* counter,
	sjme_error* error)
{
	sjme_memNode* node;

	/* Forward to free. */
	node = counter->dataPointer;
	return sjme_free(&node->bytes[0], error);
}

void* sjme_mallocGc(sjme_jint size, sjme_freeCallback freeCallback,
	sjme_error* error)
{
	sjme_jint origSize;
	sjme_memNode* result;
	sjme_memNode* checkNode;
	sjme_memNode* lastNode;
	sjme_spinLockKey lockKey;
	
	/* Considered an error. */
	if (size < 0)
	{
		sjme_setError(error, SJME_ERROR_NEGATIVE_SIZE, size);
		return NULL;
	}
	
	/* These will never allocate. */
	if (size == 0)
	{
		sjme_setError(error, SJME_ERROR_ZERO_MEMORY_ALLOCATION, size);
		return NULL;
	}
	
	/* Round size and include extra 4-bytes for size storage. */
	origSize = size;
	size = ((size + SJME_JINT_C(3)) & (~SJME_JINT_C(3))) +
		SJME_JINT_C(4) + sizeof(sjme_memNode);
	
	/* Exceeds maximum permitted allocation size? */
	if (sizeof(sjme_jint) > sizeof(size_t) && size > (sjme_jint)SIZE_MAX)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, size);
		return NULL;
	}

	/* Did not allocate? */
	result = NULL;
	if (!sjme_memDirectNew((void**)&result, size, error))
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, size);
		return NULL;
	}

	/* Clear values to zero. */
	memset(result, 0, size);

	/* Initialize counter. */
	if (!sjme_counterInit(&result->gcCount,
		sjme_mallocGcHandle, result, 0, error))
	{
		if (!sjme_hasError(error))
			sjme_setError(error, SJME_ERROR_INVALID_COUNTER_STATE, 0);
		return NULL;
	}

	/* Set block details. */
	result->key = SJME_MEM_NODE_KEY;
	result->freeCallback = freeCallback;
	result->nodeSize = size;
	result->origSize = origSize;

	/* Sanity check. */
	checkNode = NULL;
	if (!sjme_getMemNode(&result->bytes, &checkNode, error) ||
		checkNode != result)
	{
		sjme_todo("Node correction? %p %d", checkNode);
	}

	/* Lock to link in. */
	if (!sjme_lock(&sjme_memLock, &lockKey, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_LOCK, 1);
		return NULL;
	}

	/* Link into the node tree. */
	lastNode = sjme_memIo_atomicPointerGet(&sjme_lastMemNode);
	sjme_memIo_atomicPointerSet(&result->next, lastNode);
	if (lastNode != NULL)
		sjme_memIo_atomicPointerSet(&lastNode->prev, result);
	sjme_memIo_atomicPointerSet(&sjme_lastMemNode, result);

	/* Unlock. */
	if (!sjme_unlock(&sjme_memLock, &lockKey, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_LOCK, 0);
		return NULL;
	}
	
	/* Return the adjusted pointer. */
	return &result->bytes;
}

void* sjme_realloc(void* ptr, sjme_jint size, sjme_error* error)
{
	sjme_todo("Implement this?");
	return NULL;
#if 0
	void* rv;
	sjme_jint oldSize;
	
	/* These will never allocate. */
	if (size <= 0)
	{
		/* Free pointer? */
		if (ptr != NULL)
			sjme_free(ptr, NULL);
		
		/* Considered an error. */
		if (size < 0)
			sjme_setError(error, SJME_ERROR_NEGATIVE_SIZE, size);
		
		return NULL;
	}
	
	/* Allocate new pointer, keep old pointer if this failed. */
	rv = sjme_malloc(size, NULL);
	if (rv == NULL)
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, size);
		return NULL;
	}
	
	/* Copy old data over? */
	if (ptr != NULL)
	{
		/* Get the old size, to copy the data around. */
		oldSize = *((sjme_jint*)(SJME_POINTER_OFFSET_LONG(ptr, -4)));
		
		/* Only copy the smaller of size. */
		memmove(rv, ptr, (size > oldSize ? oldSize : size));
		
		/* Free the old pointer. */
		sjme_free(ptr, NULL);
	}
	
	/* Return the new pointer. */
	return rv;
#endif
}

sjme_jboolean sjme_free(void* p, sjme_error* error)
{
	sjme_jint size;
	sjme_memNode* node;
	sjme_memNode* lastNode;
	sjme_memNode* prevNode;
	sjme_memNode* nextNode;
	sjme_spinLockKey lockKey;
	
	/* Ignore null pointers. */
	if (p == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}

	/* Get the actual node used. */
	node = NULL;
	if (!sjme_getMemNode(p, &node, error) || node == NULL)
	{
		sjme_setError(error, SJME_ERROR_INVALID_ARGUMENT, 0);
		return sjme_false;
	}

	/* If there is a free function, call it. */
	if (node->freeCallback != NULL)
	{
		/* Invoke the callback. */
		if (!node->freeCallback(p, node, error))
		{
			if (!sjme_hasError(error))
				sjme_setError(error, SJME_ERROR_INVALIDMEMTYPE, 0);
			return sjme_false;
		}

		/* Already called it, so do not call again. */
		node->freeCallback = NULL;
	}

	/* Unlink into the node tree. */
	if (!sjme_lock(&sjme_memLock, &lockKey, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_LOCK, 3);
		return sjme_false;
	}

	/* Unlink from the node tree. */
	prevNode = sjme_memIo_atomicPointerGet(&node->prev);
	nextNode = sjme_memIo_atomicPointerGet(&node->next);

	if (prevNode != NULL)
		sjme_memIo_atomicPointerSet(&prevNode->next, nextNode);
	if (nextNode != NULL)
		sjme_memIo_atomicPointerSet(&nextNode->prev, prevNode);

	sjme_memIo_atomicPointerSet(&node->prev, NULL);
	sjme_memIo_atomicPointerSet(&node->next, NULL);

	lastNode = sjme_memIo_atomicPointerGet(&sjme_lastMemNode);
	if (lastNode == node)
		sjme_memIo_atomicPointerSet(&sjme_lastMemNode, nextNode);

	/* Unlock. */
	if (!sjme_unlock(&sjme_memLock, &lockKey, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_LOCK, 2);
		return sjme_false;
	}

	/* Wipe memory here, to invalidate it. */
	memset(node, 0xBA, node->nodeSize);

	/* Free memory used here. */
	return sjme_memDirectFree((void**)&node, error);
}
