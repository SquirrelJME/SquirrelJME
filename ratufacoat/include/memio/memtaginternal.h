/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal memory tag structures.
 * 
 * @since 2023/02/04
 */

#ifndef SQUIRRELJME_MEMTAGINTERNAL_H
#define SQUIRRELJME_MEMTAGINTERNAL_H

#include "memio/memtag.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Lower bit protection for the size. */
#define SJME_MEMIO_NEW_TAGGED_PROTECT_LOW INT32_C(0x40000000)

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
	sjme_memIo_tagGroup* group;

	/** The index of when this tag was allocated. */
	sjme_memIo_atomicInt birthIndex;

	/** The type of tag and allocation this uses. */
	sjme_memIo_tagType tagType;

	/** The allocation size used currently. */
	sjme_jsize allocSize;

	/** The swap order to use. */
	sjme_memIo_atomicInt currentSwap;

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
		sjme_memIo_atomicPointer ptr;
	} swaps[SJME_NUM_MEM_TAG_SWAPS];
} sjme_memTagInternal;

struct sjme_memIo_tagGroup
{
	/** Estimated memory used in total. */
	sjme_jlong estimatedUsedSize;

	/** The number of total tags allocated. */
	sjme_memIo_atomicInt totalTags;

	/** Counts for allocations under tags, used for GC checks. */
	sjme_memIo_atomicInt tagCounts[SJME_NUM_MEM_TAG_TYPES];
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMTAGINTERNAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMTAGINTERNAL_H */
