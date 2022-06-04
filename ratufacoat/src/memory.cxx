/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
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

/* Palm OS Functions. */
#if defined(SQUIRRELJME_PALMOS)
	#include <MemoryMgr.h>
	#include <MemGlue.h>
#else
	#include <string.h>
#endif

#include "sjmerc.h"
#include "atomic.h"
#include "memory.h"
#include "error.h"

/**
 * This represents a single node within all of the memory that has been
 * allocated and is being managed by SquirrelJME.
 *
 * @since 2022/02/20
 */
struct sjme_memNode
{
	/** The key to check if this is a valid node or not. */
	sjme_jint key;

	/** The size of this node. */
	sjme_juint size;

	/** The previous link (a @c sjme_memNode) in the chain. */
	sjme_atomicPointer prev;

	/** The next link (a @c sjme_memNode) in the chain. */
	sjme_atomicPointer next;

	/** The data stored within this node. */
	sjme_jbyte bytes[];
};

/** Lock on memory operations to ensure that all of them are atomic. */
static sjme_atomicInt sjme_memLock;

/** The last @c sjme_atomicPointer in the memory chain. */
static sjme_atomicPointer sjme_lastMemNode;

sjme_memStat sjme_memStats = {{0}, {0}};

void* sjme_malloc(sjme_jint size, sjme_error* error)
{
	void* rv;

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
	size = ((size + SJME_JINT_C(3)) & (~SJME_JINT_C(3))) +
		SJME_JINT_C(4);

	/* Exceeds maximum permitted allocation size? */
	if (sizeof(sjme_jint) > sizeof(size_t) && size > (sjme_jint)SIZE_MAX)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, size);
		return NULL;
	}

#if defined(SQUIRRELJME_PALMOS)
	/* Palm OS, use glue to allow greater than 64K. */
	rv = MemGluePtrNew(size);
#else
	/* Use standard C function otherwise. */
	rv = calloc(1, size);
#endif

	/* Did not allocate? */
	if (rv == NULL)
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, size);
		return NULL;
	}

#if defined(SQUIRRELJME_PALMOS)
	/* Clear memory on Palm OS. */
	MemSet(rv, size, 0);
#else
	/* Clear values to zero. */
	memset(rv, 0, size);
#endif

	/* Store the size into this memory block for later free. */
	*((sjme_jint*)rv) = size;

	/* Return the adjusted pointer. */
	return SJME_POINTER_OFFSET_LONG(rv, 4);
}

void* sjme_realloc(void* ptr, sjme_jint size, sjme_error* error)
{
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
}

sjme_jboolean sjme_free(void* p, sjme_error* error)
{
	void* baseP;
	sjme_jint size;

	/* Ignore null pointers. */
	if (p == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}

	/* Base pointer which is size shifted. */
	baseP = SJME_POINTER_OFFSET_LONG(p, -4);
	size = *((sjme_jint*)(baseP));

	/* Wipe memory that was here. */
	memset(baseP, 0xBA, size);

#if defined(SQUIRRELJME_PALMOS)
	/* Use Palm OS free. */
	MemPtrFree(baseP);
#else
	/* Use Standard C free. */
	free(baseP);
#endif

	return sjme_true;
}
