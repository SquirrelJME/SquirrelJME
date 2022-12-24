/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjmejni/memtag.h"
#include "debug.h"
#include "error.h"
#include "atomic.h"

/** Lower bit protection for the size. */
#define SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT_LOW INT32_C(0x40000000)

/** The number of swaps for tag indicators, for indirections. */
#define SJME_NUM_MEM_TAG_SWAPS 2

/** The starting birth index for groups, used to determine age. */
#define SJME_GROUP_STARTING_BIRTH_INDEX INT32_MIN

/**
 * Internal tagged memory representation.
 *
 * @since 2022/12/20
 */
typedef struct sjme_memTagInternal
{
	/** The key used to check if a tag is valid. */
	sjme_jint checkKey;

	/** The group this tag is a part of. */
	sjme_memTagGroup* group;

	/** The index of when this tag was allocated. */
	sjme_atomicInt birthIndex;

	/** The type of tag and allocation this uses. */
	sjme_memTagType tagType;

	/** The allocation size used currently. */
	sjme_jsize allocSize;

	/** The swap order to use. */
	sjme_atomicInt currentSwap;

	/**
	 * Swaps are used to keep indirect pointers around without freeing them
	 * and hopefully giving enough time for a NULL pointer to be read from
	 * them. When tags are reused, the next swap will be utilize so that
	 * the original indirection pointer is still NULL and points to valid
	 * memory.
	 */
	struct
	{
		/** The key used to detect a given swap. */
		uintptr_t swapDetectKey;

		/** The pointer for the given swap, indirections point here. */
		sjme_atomicPointer ptr;
	} swaps[SJME_NUM_MEM_TAG_SWAPS];
} sjme_memTagInternal;

struct sjme_memTagGroup
{
	/** Estimated memory used in total. */
	sjme_jlong estimatedUsedSize;

	/** The number of total tags allocated. */
	sjme_atomicInt totalTags;

	/** Counts for allocations under tags, used for GC checks. */
	sjme_atomicInt tagCounts[SJME_NUM_MEM_TAG_TYPES];
};

sjme_jboolean sjme_memDirectNew(void** outPtr, sjme_jsize size,
	sjme_error* error)
{
	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	if (size <= 0)
		return sjme_setErrorF(error, SJME_ERROR_NEGATIVE_SIZE, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memTaggedGroupFree(sjme_memTagGroup** inPtr,
	sjme_error* error)
{
	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	if (*inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_DOUBLE_FREE, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memTaggedGroupNew(sjme_memTagGroup** outPtr,
	sjme_error* error)
{
	sjme_memTagGroup* result;

	if (outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Should be a zero initialized pointer. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_POINTER_NOT_NULL, 0);

	/* Allocate memory group. */
	result = NULL;
	if (!sjme_memDirectNew((void**)&result, sizeof(*result), error))
		return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memTaggedFreeZ(void*** inPtr, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	if (inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Prevent double free of memory. */
	if (*inPtr == NULL || **inPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_DOUBLE_FREE, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_TAG_VIOLATION, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_memTaggedNewZ(sjme_memTagGroup* group, void*** outPtr,
	sjme_jsize size, sjme_memTagType tagType, sjme_error* error,
	sjme_jsize protectA, sjme_jsize protectB)
{
	if (group == NULL || outPtr == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Protect values must be the same size as pointers, this is to detect */
	/* cases where tag pointers are not used correctly. */
	if (protectA != sizeof(void*) || protectB != sizeof(void*))
		return sjme_setErrorF(error, SJME_ERROR_PROTECTED_TAG_VIOLATION, 0);

	/* Make sure the correct sizeof() is used and that the value is not */
	/* erroneously zero. */
	if (((size & SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT) !=
		SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT) ||
		(size & SJME_MEM_TAGGED_NEW_SIZE_OF_PROTECT_LOW) != 0)
		return sjme_setErrorF(error, SJME_ERROR_TAGGED_WRONG_SIZE_OF, 0);

	/* We should not smash a pointer that was here already. */
	if (*outPtr != NULL)
		return sjme_setErrorF(error, SJME_ERROR_TAG_NOT_NULL, 0);

	sjme_todo("Implement this?");
	return sjme_false;
}
