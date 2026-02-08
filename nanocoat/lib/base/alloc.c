/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#include "sjme/binary.h"

/* Include Valgrind if it is available? */
#if defined(SJME_CONFIG_HAS_VALGRIND)
	#include <valgrind.h>
	#include <memcheck.h>
#endif

#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/atomic.h"
#include "sjme/multithread.h"
#include "sjme/dylib.h"

/** The minimum size permitted for allocation pools. */
#define SJME_ALLOC_MIN_SIZE (((SJME_SIZEOF_ALLOC_POOL(0) + \
	(SJME_SIZEOF_ALLOC_LINK(0) * 3)) | 0x1FF))

/** The minimum size for splits. */
#define SJME_ALLOC_SPLIT_MIN_SIZE 64

/** The front guard value. */
#define SJME_ALLOC_GUARD_FRONT INT32_C(0x53716B21)

/** The back guard value. */
#define SJME_ALLOC_GUARD_BACK INT32_C(0x6C65783F)

#if defined(SJME_CONFIG_DEBUG)
/**
 * Prints information on a given link and returns.
 *
 * @param pool The pool this is in.
 * @param atLink The link to print info for.
 * @param trigger The trigger for the failure.
 * @return Always @link SJME_JNI_TRUE @endlink .
 * @since 2023/12/29
 */
static sjme_inline sjme_jboolean sjme_alloc_corruptFail(
	sjme_alloc_pool pool,
	sjme_alloc_link atLink,
	const char* trigger)
{
	sjme_message("Corrupted Link %p: %s", (void*)atLink, trigger);

	/* Ignore if null. */
	if (atLink == NULL)
		return SJME_JNI_TRUE;

	/* Dump everything about the link. */
	sjme_message("link->guardFront: %08x", atLink->guardFront);
	sjme_message("link->pool: %p (should be %p)",
		(void*)atLink->pool, (void*)pool);
	sjme_message("link->prev: %p", sjme_atomic_pg(&atLink->prev));
	sjme_message("link->next: %p", sjme_atomic_pg(&atLink->next));
	if (atLink->space == SJME_ALLOC_POOL_SPACE_USED)
		sjme_message("link->space: USED");
	else if (atLink->space == SJME_ALLOC_POOL_SPACE_FREE)
		sjme_message("link->space: FREE");
	else if (atLink->space == SJME_NUM_ALLOC_POOL_SPACE)
		sjme_message("link->space: NUM");
	else
		sjme_message("link->space: %d", (int)atLink->space);
	sjme_message("link->weak: %p", sjme_atomic_pg(&atLink->weak));
	sjme_message("link->freePrev: %p", sjme_atomic_pg(&atLink->freePrev));
	sjme_message("link->freeNext: %p", sjme_atomic_pg(&atLink->freeNext));
	sjme_message("link->allocSize: %d", (int)atLink->allocSize);
	sjme_message("link->blockSize: %d", (int)atLink->blockSize);
	sjme_message("link->guardBack: %08x", atLink->guardBack);
	
	/* Abort. */
	if (sjme_debug_handlers != NULL && sjme_debug_handlers->abort != NULL)
		sjme_debug_handlers->abort(SJME_ERROR_MEMORY_CORRUPTION);

	/* Always indicate failure here. */
	return SJME_JNI_TRUE;
}
#else
/**
 * Prints information on a given link and returns.
 *
 * @param pool The pool this is in.
 * @param atLink The link to print info for.
 * @param trigger The trigger for the failure.
 * @return Always @link SJME_JNI_TRUE @endlink .
 * @since 2023/12/29
 */
#define sjme_alloc_corruptFail(pool, atLink, trigger) SJME_JNI_TRUE
#endif

static sjme_inline sjme_jboolean sjme_alloc_checkCorruptionRange(
	sjme_alloc_pool pool, uintptr_t poolStart, uintptr_t poolEnd,
	sjme_alloc_link atLink)
{
	uintptr_t check;

	/* Ignore null pointers. */
	if (atLink == NULL)
		return SJME_JNI_FALSE;

	/* Nominal address of the check pointer. */
	check = (uintptr_t)atLink;

	/* Must be in range! */
	if (check < poolStart || check >= poolEnd)
		return sjme_alloc_corruptFail(pool, atLink,
			"Out of range link");

	/* Does not appear corrupt. */
	return SJME_JNI_FALSE;
}

/**
 * Checks the integrity of the memory pool.
 *
 * @param pool The pool to check in.
 * @param atLink The link of the pool.
 * @return If there is corruption or not.
 * @since 2023/12/29
 */
static sjme_jboolean sjme_noOptimize sjme_alloc_checkCorruption(
	sjme_alloc_pool pool,
	sjme_alloc_link atLink)
{
	uintptr_t poolStart, poolEnd;

	if (pool == NULL)
		return SJME_JNI_TRUE;
	
	/* Pool magic number invalid? */
	if (pool->magic != SJME_ALLOC_POOL_MAGIC)
		return sjme_alloc_corruptFail(pool, atLink,
			"Wrong pool magic.");

	/* If no link is specified, ignore. */
	if (atLink == NULL)
		return SJME_JNI_FALSE;
	
	/* Check front and back guards. */
	if (atLink->guardFront != SJME_ALLOC_GUARD_FRONT)
		return sjme_alloc_corruptFail(pool, atLink,
			"Wrong front guard");
	if (atLink->guardBack != SJME_ALLOC_GUARD_BACK)
		return sjme_alloc_corruptFail(pool, atLink,
			"Wrong back guard");

	/* Link is in the wrong pool. */
	if (atLink->pool != pool)
		return sjme_alloc_corruptFail(pool, atLink,
			"Wrong pool");
	
	/* Allocation size larger than block? */
	if (atLink->allocSize > atLink->blockSize)
		return sjme_alloc_corruptFail(pool, atLink,
			"Allocation size larger than block.");

	/* Next link is in the wrong location? */
	if (sjme_atomic_pg(&atLink->next) != NULL &&
		(uintptr_t)sjme_atomic_pg(&atLink->next) !=
		(uintptr_t)&atLink->block[atLink->blockSize])
		return sjme_alloc_corruptFail(pool, atLink,
			"Next not at block end");

	/* Is front/end link? */
	if (atLink == sjme_atomic_g(sjme_alloc_link, &pool->frontLink) ||
		atLink == sjme_atomic_g(sjme_alloc_link, &pool->backLink))
	{
		/* Link space incorrect? */
		if (atLink->space != SJME_NUM_ALLOC_POOL_SPACE)
			return sjme_alloc_corruptFail(pool, atLink,
				"Front/Back link not in correct space");

		/* Size is not zero? */
		if (atLink->blockSize != 0 || atLink->allocSize != 0)
			return sjme_alloc_corruptFail(pool, atLink,
				"Front/back link sizes non-zero");

		/* Does not appear corrupt. */
		return SJME_JNI_FALSE;
	}

	/* Invalid block size? */
	if (atLink->blockSize <= 0)
		return sjme_alloc_corruptFail(pool, atLink,
			"Zero or negative block size");

	/* Used for checking the integrity of pointers. */
	poolStart = (uintptr_t)pool;
	poolEnd = (uintptr_t)&pool->block[pool->size];

	/* Free link only. */
	if (atLink->space == SJME_ALLOC_POOL_SPACE_FREE)
	{
		/* Check free links. */
		if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
				sjme_atomic_g(sjme_alloc_link, &atLink->freePrev)))
			return sjme_alloc_corruptFail(pool, atLink,
				"Corrupt freePrev");
		if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
				sjme_atomic_g(sjme_alloc_link, &atLink->freeNext)))
			return sjme_alloc_corruptFail(pool, atLink,
				"Corrupt freeNext");
	}

	/* Used link only. */
	else if (atLink->space == SJME_ALLOC_POOL_SPACE_USED)
	{
		/* Zero or negative size. */
		if (atLink->allocSize <= 0)
			return sjme_alloc_corruptFail(pool, atLink,
				"Zero/negative used allocSize");

		/* Cannot have any free or previous links. */
		if (sjme_atomic_pg(&atLink->freePrev) != NULL ||
			sjme_atomic_pg(&atLink->freeNext) != NULL)
			return sjme_alloc_corruptFail(pool, atLink,
				"Used has free links");
	}

	/* Link space incorrect? */
	else
		return sjme_alloc_corruptFail(pool, atLink,
			"Incorrect space");

	/* Check common next links. */
	if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
		sjme_atomic_g(sjme_alloc_link, &atLink->prev)))
		return sjme_alloc_corruptFail(pool, atLink,
			"Corrupt prev");
	if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
		sjme_atomic_g(sjme_alloc_link, &atLink->next)))
		return sjme_alloc_corruptFail(pool, atLink,
			"Corrupt next");

	/* Does not appear corrupt. */
	return SJME_JNI_FALSE;
}

static sjme_errorCode sjme_alloc_getLinkOptional(
	sjme_attrInNotNull sjme_pointer addr,
	sjme_attrOutNotNull sjme_alloc_link* outLink,
	sjme_attrInValue sjme_jboolean checkCorruption)
{
	sjme_alloc_link link;

	if (addr == NULL || outLink == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just need to do some reversing math. */
	link = (sjme_alloc_link)(((uintptr_t)addr) -
		offsetof(sjme_alloc_linkBase, block));

	/* Check the integrity of the link. */
	if (checkCorruption)
		sjme_alloc_checkCorruption(link->pool, link);
	
	/* Cannot be a link? */
	if (link->guardFront != SJME_ALLOC_GUARD_FRONT ||
		link->guardBack != SJME_ALLOC_GUARD_BACK)
		return SJME_ERROR_NOT_ALLOC_LINK;
	
	/* Success! */
	*outLink = link;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_alloc_poolInitMalloc(
	sjme_attrOutNotNull sjme_alloc_pool* outPool,
	sjme_attrInPositive sjme_jint size)
{
	sjme_pointer result;
	sjme_jint useSize;

	/* Make sure the size is not wonky. */
	useSize = SJME_SIZEOF_ALLOC_POOL(size);
	if (outPool == NULL || size <= SJME_ALLOC_MIN_SIZE || useSize <= 0 ||
		size > useSize)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Attempt allocation. */
	result = malloc(useSize);
	if (!result)
		return sjme_error_outOfMemory(NULL, useSize);
	
	/* Use static pool initializer to set up structures. */
	return sjme_alloc_poolInitStatic(outPool, result, useSize);
}

sjme_errorCode sjme_noOptimize sjme_alloc_poolInitStatic(
	sjme_attrOutNotNull sjme_alloc_pool* outPool,
	sjme_attrInNotNull sjme_pointer baseAddr,
	sjme_attrInPositive sjme_jint size)
{
	sjme_alloc_pool pool;
	sjme_alloc_link frontLink;
	sjme_alloc_link midLink;
	sjme_alloc_link backLink;
	sjme_alloc_link specialParent;

	if (outPool == NULL || baseAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (size <= SJME_ALLOC_MIN_SIZE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Initialize memory to nothing. */
	memset(baseAddr, 0, size);
	
	/* Setup initial pool structure. */
	pool = baseAddr;
	pool->magic = SJME_ALLOC_POOL_MAGIC;
	pool->rawSize = size;
	pool->size = (size & (~7)) - SJME_SIZEOF_ALLOC_POOL(0);

	/* Use the corrected size. */
	size = pool->size;
	
	/* Setup front link. */
	frontLink = (sjme_pointer)&pool->block[0];
	sjme_atomic_s(sjme_alloc_link, &pool->frontLink, frontLink);
	
	/* Setup back link, keep it off the edge and aligned to be within */
	/* the pool still, some bytes will be wasted here. */
	backLink = (sjme_pointer)&pool->block[(pool->size -
		(SJME_SIZEOF_ALLOC_LINK(1) * 2)) & (~7)];
	sjme_atomic_s(sjme_alloc_link, &pool->backLink, backLink);
	
	/* Setup middle link, which is between the two. */
	midLink = (sjme_pointer)&frontLink->block[0];
	sjme_atomic_s(sjme_alloc_link, &midLink->prev, frontLink);
	sjme_atomic_s(sjme_alloc_link, &frontLink->next, midLink);
	sjme_atomic_s(sjme_alloc_link, &midLink->next, backLink);
	sjme_atomic_s(sjme_alloc_link, &backLink->prev, midLink);
	
	/* Determine size of the middle link, which is free space. */
	midLink->blockSize = (sjme_jint)((uintptr_t)backLink -
		(uintptr_t)&midLink->block[0]);
	
	/* The mid-link is considered free. */
	midLink->space = SJME_ALLOC_POOL_SPACE_FREE;
		
	/* The front and back links are in the "invalid" space. */
	frontLink->space = SJME_NUM_ALLOC_POOL_SPACE;
	backLink->space = SJME_NUM_ALLOC_POOL_SPACE;
	
	/* Determine size that can and cannot be used. */
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].reserved =
		SJME_SIZEOF_ALLOC_LINK(0);
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable = midLink->blockSize;
	
	/* Link in the first and last actual blocks for the free chain. */
	sjme_atomic_s(sjme_alloc_link, &pool->freeFirstLink, frontLink);
	sjme_atomic_s(sjme_alloc_link, &frontLink->freeNext, midLink);
	sjme_atomic_s(sjme_alloc_link, &midLink->freePrev, frontLink);
	sjme_atomic_s(sjme_alloc_link, &pool->freeLastLink, backLink);
	sjme_atomic_s(sjme_alloc_link, &backLink->freePrev, midLink);
	sjme_atomic_s(sjme_alloc_link, &midLink->freeNext, backLink);
	
	/* Guards for all links. */
	frontLink->guardFront = SJME_ALLOC_GUARD_FRONT;
	frontLink->guardBack = SJME_ALLOC_GUARD_BACK;
	midLink->guardFront = SJME_ALLOC_GUARD_FRONT;
	midLink->guardBack = SJME_ALLOC_GUARD_BACK;
	backLink->guardFront = SJME_ALLOC_GUARD_FRONT;
	backLink->guardBack = SJME_ALLOC_GUARD_BACK;

	/* Link in pools. */
	frontLink->pool = pool;
	midLink->pool = pool;
	backLink->pool = pool;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug source line init blocks. */
	sjme_atomic_g(sjme_alloc_link, &pool->frontLink)->debugFile =
		"<FRONT LINK>";
	sjme_atomic_g(sjme_alloc_link, &pool->frontLink)->debugLine = 1;
	sjme_atomic_g(sjme_alloc_link, &pool->frontLink)->debugFunction =
		"<FRONT LINK>";
	
	sjme_atomic_g(sjme_alloc_link, &pool->backLink)->debugFile =
		"<BACK LINK>";
	sjme_atomic_g(sjme_alloc_link, &pool->backLink)->debugLine = 1;
	sjme_atomic_g(sjme_alloc_link, &pool->backLink)->debugFunction =
		"<BACK LINK>";
#endif
	
#if defined(SJME_CONFIG_HAS_VALGRIND)
	/* Reserve front side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(baseAddr,
		((uintptr_t)&midLink->block[0] - (uintptr_t)baseAddr));
		
	/* Reserve back side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(backLink,
		(SJME_SIZEOF_ALLOC_LINK(0)));
#endif

#if defined(SJME_CONFIG_EXPERIMENT_NESTED_LINK)
	/* If this is a valid link then we are allocating a nested pool. */
	specialParent = NULL;
	if (!sjme_error_is(sjme_alloc_getLinkOptional(baseAddr,
		&specialParent, SJME_JNI_FALSE)))
		specialParent->flags |= SJME_ALLOC_LINK_FLAG_NESTED_POOL;
#endif
	
	/* Use the pool. */
	*outPool = pool;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_poolDestroy(
	sjme_attrInNotNull sjme_alloc_pool allocPool)
{
	if (allocPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_alloc_poolSpaceTotalSize(
	sjme_attrInNotNull const sjme_alloc_pool pool,
	sjme_attrOutNullable sjme_jint* outTotal,
	sjme_attrOutNullable sjme_jint* outReserved,
	sjme_attrOutNullable sjme_jint* outUsable,
	sjme_attrOutNullable sjme_jint* outAllocBlocks)
{
	sjme_errorCode error;
	sjme_jint total, i;
	sjme_jint reserved;
	sjme_jint usable;
	sjme_jint allocBlocks;
	sjme_alloc_link link;

	if (pool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outTotal == NULL && outReserved == NULL && outUsable == NULL &&
		outAllocBlocks == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Take ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&pool->spinLock)))
		return sjme_error_default(error);

	/* Run through and tally values for each space. */
	reserved = 0;
	usable = 0;
	if (outTotal != NULL || outReserved != NULL || outUsable != NULL)
		for (i = 0; i < SJME_NUM_ALLOC_POOL_SPACE; i++)
		{
			reserved += pool->space[i].reserved;
			usable += pool->space[i].usable;
		}

	/* Count used blocks. */
	allocBlocks = 0;
	if (outAllocBlocks != NULL)
		for (link = sjme_atomic_g(sjme_alloc_link, &pool->frontLink);
			link != NULL;
			link = sjme_atomic_g(sjme_alloc_link, &link->next))
			if (link->space == SJME_ALLOC_POOL_SPACE_USED)
				allocBlocks++;
	
	/* Release ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);

	/* Total space is both. */
	total = reserved + usable;

	/* Store output values. */
	if (outTotal != NULL)
		*outTotal = total;
	if (outReserved != NULL)
		*outReserved = reserved;
	if (outUsable != NULL)
		*outUsable = usable;
	if (outAllocBlocks != NULL)
		*outAllocBlocks = allocBlocks;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_allocR(
	sjme_attrInNotNull sjme_alloc_pool pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull sjme_pointer* outAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_alloc_link scanLink;
	sjme_alloc_link rightLink;
	sjme_jint splitMinSize, roundSize;
	sjme_jboolean splitBlock, isTiny;
	sjme_alloc_pool nextPool;
	sjme_alloc_link nextFree;
	
	if (pool == NULL || size <= 0 || outAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if defined(SJME_CONFIG_DEBUG_ALLOC)
	if ((size * 8) == SJME_CONFIG_HAS_POINTER)
		sjme_message("Alloc of single pointer in %s (%s:%d).",
			func, file, line);
#endif
	
	/* Is this a tiny block? */
	isTiny = (size <= (sizeof(struct sjme_alloc_weakBase) * 2));
	
	/* Determine the size this will actually take up, which includes the */
	/* link to be created following this. */
	roundSize = (((size & 7) != 0) ? ((size | 7) + 1) : size);
	splitMinSize = roundSize +
		(sjme_jint)SJME_SIZEOF_ALLOC_LINK(SJME_ALLOC_SPLIT_MIN_SIZE) +
		(sjme_jint)SJME_SIZEOF_ALLOC_LINK(0);
	if (size > splitMinSize || splitMinSize < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Take ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&pool->spinLock)))
		return sjme_error_default(error);
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Find the first free link that this fits in. */
	scanLink = NULL;
	splitBlock = SJME_JNI_FALSE;
	for (scanLink = (isTiny ?
			sjme_atomic_g(sjme_alloc_link, &pool->freeLastLink) :
			sjme_atomic_g(sjme_alloc_link, &pool->freeFirstLink));
		scanLink != NULL;
		scanLink = (isTiny ?
			sjme_atomic_g(sjme_alloc_link, &scanLink->freePrev) :
			sjme_atomic_g(sjme_alloc_link, &scanLink->freeNext)))
	{
		/* Has memory been corrupted? */
		nextFree = (isTiny ?
			sjme_atomic_g(sjme_alloc_link, &scanLink->freePrev) :
			sjme_atomic_g(sjme_alloc_link, &scanLink->freeNext));
		if (sjme_alloc_checkCorruption(pool, scanLink) ||
			(nextFree != NULL &&
				sjme_alloc_checkCorruption(pool, nextFree)))
		{
			error = SJME_ERROR_MEMORY_CORRUPTION;
			goto fail_corrupt;
		}

		/* Block is in the "invalid" space, skip it. */
		if (scanLink->space == SJME_NUM_ALLOC_POOL_SPACE)
			continue;
		
		/* Block fits perfectly here, without needing a split? */
		if (scanLink->blockSize == roundSize)
			break;
		
		/* Block fits here when split, try to not split ridiculously small. */
		if (scanLink->blockSize >= splitMinSize)
		{
			splitBlock = SJME_JNI_TRUE;
			break;
		}
	}
	
	/* Out of memory. */
	if (scanLink == NULL)
	{
		/* If there is an adjacent pool, if allocation fails then we shall */
		/* try the next pool, this means multiple pools can work together */
		/* accordingly. */
		nextPool = pool->nextPool;
		if (nextPool != NULL)
		{
			/* Release ownership of lock. */
			if (sjme_error_is(error = sjme_thread_spinLockRelease(
				&pool->spinLock, NULL)))
				return sjme_error_default(error);
			
#if defined(SJME_CONFIG_DEBUG)
			return sjme_allocR(nextPool, size, outAddr,
				file, line, func);
#else
			return sjme_alloc(pool->nextPool, size, outAddr);
#endif
		}
		
		/* Otherwise fail! */
		error = sjme_error_outOfMemory(pool, size);
		goto fail_noMemory;
	}

#if defined(SJME_CONFIG_DEBUG_ALLOC)
	/* Debug. */
	sjme_message("Found link at %p: %d bytes, we need %d with split %d.",
		scanLink, (int)scanLink->blockSize, (int)roundSize, (int)splitBlock);
#endif

	/* Does this block need to be split? */
	if (splitBlock)
	{
		/* Check for link corruption on the adjacent links. */
		if (sjme_alloc_checkCorruption(pool,
				sjme_atomic_g(sjme_alloc_link, &scanLink->next)) ||
			sjme_alloc_checkCorruption(pool,
				sjme_atomic_g(sjme_alloc_link, &scanLink->prev)) ||
			sjme_alloc_checkCorruption(pool,
				sjme_atomic_g(sjme_alloc_link, &scanLink->freeNext)) ||
			sjme_alloc_checkCorruption(pool,
				sjme_atomic_g(sjme_alloc_link, &scanLink->freePrev)))
		{
			error = SJME_ERROR_MEMORY_CORRUPTION;
			goto fail_corrupt;
		}

		/* Make it so this block can actually fit in here. */
		/* If a tiny block, align to the right. */
		if (isTiny)
			rightLink = (sjme_alloc_link)&scanLink->block[
				scanLink->blockSize - (roundSize + SJME_SIZEOF_ALLOC_LINK(0))];
		else
			rightLink = (sjme_alloc_link)&scanLink->block[roundSize];

		/* Initialize block to remove any old data. */
		memset((sjme_pointer)rightLink, 0, sizeof(*rightLink));
		
		/* Guards for link. */
		rightLink->guardFront = SJME_ALLOC_GUARD_FRONT;
		rightLink->guardBack = SJME_ALLOC_GUARD_BACK;

		/* Set the right link's pool accordingly. */
		rightLink->pool = pool;

		/* Make sure this block is marked as free. */
		rightLink->space = SJME_ALLOC_POOL_SPACE_FREE;

		/* Set size of the right link. */
		rightLink->blockSize =
			(sjme_jint)((intptr_t)&scanLink->block[scanLink->blockSize] -
				(intptr_t)&rightLink->block[0]);
		rightLink->allocSize = rightLink->blockSize;

		/* Link in physical links. */
		sjme_atomic_copy(sjme_alloc_link, &rightLink->next, &scanLink->next);
		sjme_atomic_chainGetSet(sjme_alloc_link,
			&rightLink->next, ->prev, rightLink);
		sjme_atomic_s(sjme_alloc_link, &scanLink->next, rightLink);
		sjme_atomic_s(sjme_alloc_link, &rightLink->prev, scanLink);

		/* Link in free links. */
		sjme_atomic_copy(sjme_alloc_link, &rightLink->freeNext,
			&scanLink->freeNext);
		sjme_atomic_chainGetSet(sjme_alloc_link,
			&rightLink->freeNext, ->freePrev, rightLink);
		sjme_atomic_s(sjme_alloc_link, &scanLink->freeNext, rightLink);
		sjme_atomic_s(sjme_alloc_link, &rightLink->freePrev, scanLink);

		/* Set size of the left block. */
		scanLink->blockSize =
			(sjme_jint)((intptr_t)rightLink - (intptr_t)&scanLink->block[0]);
		scanLink->allocSize = scanLink->blockSize;

		/* Adjust reserved and usable space. */
		pool->space[SJME_ALLOC_POOL_SPACE_FREE].reserved +=
			SJME_SIZEOF_ALLOC_LINK(0);
		pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable -=
			SJME_SIZEOF_ALLOC_LINK(0);

		/* Make sure we did not cause corruption. */
		if (sjme_alloc_checkCorruption(pool, scanLink) ||
			sjme_alloc_checkCorruption(pool, rightLink))
		{
			error = SJME_ERROR_MEMORY_CORRUPTION;
			goto fail_corrupt;
		}
		
		/* If tiny, take up the right side space. */
		if (isTiny)
			scanLink = rightLink;
	}

	/* Setup block information. */
	scanLink->space = SJME_ALLOC_POOL_SPACE_USED;

	/* Unlink from free links. */
	if (sjme_atomic_pg(&scanLink->freeNext) != NULL)
		sjme_atomic_chainGetSet(sjme_alloc_link,
			&scanLink->freeNext, ->freePrev,
			sjme_atomic_g(sjme_alloc_link, &scanLink->freePrev));
	if (sjme_atomic_pg(&scanLink->freePrev) != NULL)
		sjme_atomic_chainGetSet(sjme_alloc_link,
			&scanLink->freePrev, ->freeNext,
			sjme_atomic_g(sjme_alloc_link, &scanLink->freeNext));
	sjme_atomic_psNull(&scanLink->freePrev);
	sjme_atomic_psNull(&scanLink->freeNext);

	/* Use our given allocation size. */
	scanLink->allocSize = size;

	/* Adjust space that can actually be used for data. */
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable -= scanLink->blockSize;
	pool->space[SJME_ALLOC_POOL_SPACE_USED].usable += scanLink->blockSize;

	/* Since this block is claimed, the reserved space moves over. */
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].reserved -=
		SJME_SIZEOF_ALLOC_LINK(0);
	pool->space[SJME_ALLOC_POOL_SPACE_USED].reserved +=
		SJME_SIZEOF_ALLOC_LINK(0);

#if defined(SJME_CONFIG_DEBUG)
	/* Set debug info. */
	scanLink->debugFile = file;
	scanLink->debugLine = line;
	scanLink->debugFunction = func;
#endif

	/* Make sure we did not cause corruption. */
	if (sjme_alloc_checkCorruption(pool, scanLink) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &scanLink->prev)) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &scanLink->next)))
	{
		error = SJME_ERROR_MEMORY_CORRUPTION;
		goto fail_corrupt;
	}
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Release ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);
	
	/* Use the given link. */
	*outAddr = (sjme_pointer)&scanLink->block[0];
	return SJME_ERROR_NONE;

fail_corrupt:
fail_noMemory:
	/* Release ownership of lock before we leave. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_alloc_copyR(
	sjme_attrInNotNull sjme_alloc_pool pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull sjme_pointer* outAddr,
	sjme_attrInNotNull sjme_pointer inAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_pointer dest;

	if (pool == NULL || outAddr == NULL || inAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate new copy first. */
	dest = NULL;
	if (sjme_error_is(error = sjme_allocR(
		pool, size, &dest
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) || dest == NULL)
		return sjme_error_default(error);

	/* Copy over. */
	memmove(dest, inAddr, size);
	*outAddr = dest;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_copyWeakR(
	sjme_attrInNotNull sjme_alloc_pool pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData,
	sjme_attrOutNotNull sjme_pointer* outAddr,
	sjme_attrInNotNull sjme_pointer inAddr,
	sjme_attrOutNullable sjme_alloc_weak* outWeak
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_pointer dest;

	if (pool == NULL || outAddr == NULL || inAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate new copy first. */
	dest = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNewR(
		pool, size, inEnqueue, &dest, outWeak
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) || dest == NULL)
		return sjme_error_default(error);

	/* Copy over. */
	memmove(dest, inAddr, size);
	*outAddr = dest;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_formatR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_lpstr* outString,
	SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL SJME_DEBUG_ONLY_COMMA
	sjme_attrInNotNull sjme_attrFormatArg const char* format,
	...)
{
#define BUF_SIZE 512
	char buf[BUF_SIZE];
	va_list arg;
	int len;

	if (allocPool == NULL || outString == NULL || format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Start variable arguments. */
	va_start(arg, format);

	/* Format string to the buffer. */
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, BUF_SIZE - 1, format, arg);

	/* Force to end with a NUL. */
	buf[BUF_SIZE - 1] = 0;

	/* End them. */
	va_end(arg);

	/* Calculate length of string for copying. */
	len = strlen(buf);

	/* Copy it. */
	return sjme_alloc_copyR(allocPool, len + 1,
		(sjme_pointer*)outString, buf
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);
#undef BUF_SIZE
}

sjme_errorCode sjme_alloc_grow(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_pointer* inOutAddr,
	sjme_attrInPositiveNonZero sjme_jint memberSize,
	sjme_attrInNotNull sjme_jint* currentCountP,
	sjme_attrInPositiveNonZero sjme_jint newCount)
{
	sjme_errorCode error;
	sjme_pointer currentP;
	
	if (allocPool == NULL || inOutAddr == NULL || currentCountP == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (memberSize <= 0 || newCount < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Need to allocate? */
	currentP = *inOutAddr;
	if (currentP == NULL && newCount > 0)
	{
		if (sjme_error_is(error = sjme_alloc(allocPool,
			memberSize * newCount, inOutAddr)))
			return sjme_error_default(error);

		*currentCountP = newCount;
		return SJME_ERROR_NONE;
	}

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_alloc_mergeFree(sjme_alloc_link link)
{
	sjme_alloc_pool pool;
	sjme_alloc_link right;
	sjme_alloc_link oldRightFreeNext;
	sjme_alloc_link rightRight;
	sjme_alloc_link checkLeft;
	sjme_jint addedSize;
	
	if (link == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Need pool for all operations. */
	pool = link->pool;
	
	/* If the previous block is free, pivot to there. */
	checkLeft = sjme_atomic_g(sjme_alloc_link, &link->prev);
	if (checkLeft->space == SJME_ALLOC_POOL_SPACE_FREE)
		return sjme_alloc_mergeFree(checkLeft);
	
	/* Is the block on the right a candidate for merge? */
	right = sjme_atomic_g(sjme_alloc_link, &link->next);
	if (right->space != SJME_ALLOC_POOL_SPACE_FREE)
		return SJME_ERROR_NONE;
	
	/* We need the block after to relink. */
	rightRight = sjme_atomic_g(sjme_alloc_link, &right->next);
	
	/* Disconnect in the middle. */
	sjme_atomic_s(sjme_alloc_link, &link->next, rightRight);
	sjme_atomic_s(sjme_alloc_link, &rightRight->prev, link);
	
	/* Remove from the free chain. */
	oldRightFreeNext = sjme_atomic_g(sjme_alloc_link, &right->freeNext);
	sjme_atomic_chainGetSet(sjme_alloc_link, &right->freePrev, ->freeNext,
		sjme_atomic_g(sjme_alloc_link, &right->freeNext));
	oldRightFreeNext->freePrev = right->freePrev;
	
	/* Reclaim the right link data area. */
	addedSize = right->blockSize + SJME_SIZEOF_ALLOC_LINK(0);
	link->blockSize += addedSize;
	
	/* Update pool sizes. */
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable += addedSize;
	pool->space[SJME_ALLOC_POOL_SPACE_FREE].reserved -=
		SJME_SIZEOF_ALLOC_LINK(0);
	
	/* Synchronize allocation size. */
	link->allocSize = link->blockSize;
	
	/* Wipe next side block to remove any stale data. */
	memset((sjme_pointer)right, 0, sizeof(*right));
	
	/* Should not have corrupted the block. */
	if (sjme_alloc_checkCorruption(pool, link) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &link->prev)) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &link->next)) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &link->freePrev)) ||
		sjme_alloc_checkCorruption(pool,
			sjme_atomic_g(sjme_alloc_link, &link->freeNext)))
		return SJME_ERROR_MEMORY_CORRUPTION;
	
	/* We merged a block, so check again. */
	return sjme_alloc_mergeFree(link);
}

sjme_errorCode sjme_noOptimize sjme_alloc_free(
	sjme_attrInNotNull sjme_pointer addr)
{
	sjme_alloc_link link;
	sjme_alloc_pool pool;
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_jint count;

	if (addr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Emit barrier. */
	sjme_atomic_barrier();

	/* Get the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr, &link)))
		return sjme_error_default(error);

	/* Get the pool we are in. */
	pool = link->pool;
		
	/* Take ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&pool->spinLock)))
		return sjme_error_default(error);
	
	/* Check the integrity of the block before we free it. */
	pool = link->pool;
	if (sjme_alloc_checkCorruption(pool, link))
	{
		error = SJME_ERROR_MEMORY_CORRUPTION;
		goto fail_corrupt;
	}
	
	/* If there is a weak reference, clear it. */
	weak = sjme_atomic_g(sjme_alloc_weak, &link->weak);
	if (weak != NULL)
	{
		/* If we are already in this, do not free as we will corrupt */
		/* memory recursing into this. */
		if (!sjme_atomic_cs(sjme_jint, &weak->inEnqueue,
			0, 1))
			goto any_cancelFree;
		
		/* Get the count this would be. */
		count = sjme_atomic_g(sjme_jint, &weak->count);
		
		/* Call enqueue handler. */
		error = SJME_ERROR_NONE;
		if (weak->enqueue != NULL)
			error = weak->enqueue(weak, addr, count,
				SJME_JNI_FALSE, SJME_JNI_TRUE);
			
		/* Unset. */
		sjme_atomic_s(sjme_jint, &weak->inEnqueue, 0);
		
		/* Failed enqueue? */
		if (sjme_error_is(error))
			goto fail_enqueue;
		
		/* Clear weak reference data. */
		sjme_atomic_psNull(&link->weak);
		sjme_atomic_psNull(&weak->link);
		sjme_atomic_psNull(&weak->pointer);
	}

	/* Mark block as free. */
	link->space = SJME_ALLOC_POOL_SPACE_FREE;
	
	/* Clear flags, if any. */
	link->flags = 0;
	
	/* Clear block memory so stale memory is not around. */
	memset((sjme_pointer)&link->block[0], 0, link->blockSize);
	
	/* Restore allocation size to block size. */
	link->allocSize = link->blockSize;

#if defined(SJME_CONFIG_DEBUG)
	/* Remove debug information. */
	link->debugFile = NULL;
	link->debugLine = 0;
	link->debugFunction = NULL;
#endif

	/* Link into free chain. */
	sjme_atomic_s(sjme_alloc_link, &link->freeNext,
		sjme_atomic_chainGetGet(sjme_alloc_link,
		&pool->freeFirstLink, ->freeNext));
	sjme_atomic_chainGetSet(sjme_alloc_link,
		&pool->freeFirstLink, ->freeNext, link);
	sjme_atomic_chainGetSet(sjme_alloc_link,
		&link->freeNext, ->freePrev, link);
	sjme_atomic_copy(sjme_alloc_link, &link->freePrev, &pool->freeFirstLink);

	/* Merge together free blocks. */
	if (sjme_error_is(error = sjme_alloc_mergeFree(link)))
		goto fail_merge;
	
any_cancelFree:
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Release ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
	/* Release ownership of lock. */
fail_enqueue:
fail_corrupt:
fail_merge:
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_alloc_getLink(
	sjme_attrInNotNull sjme_pointer addr,
	sjme_attrOutNotNull sjme_alloc_link* outLink)
{
	return sjme_alloc_getLinkOptional(addr, outLink,
		SJME_JNI_TRUE);
}

sjme_errorCode sjme_alloc_reallocR(
	sjme_attrInOutNotNull sjme_pointer* inOutAddr,
	sjme_attrInPositive sjme_jint newSize
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_link link;
	sjme_pointer result;
	sjme_pointer source;
	sjme_jint limit;
	sjme_errorCode error;

	if (inOutAddr == NULL || *inOutAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (newSize < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Emit barrier. */
	sjme_atomic_barrier();

	/* Alias for free. */
	source = *inOutAddr;
	if (newSize == 0)
	{
		/* Just do a normal free of it since zero was requested. */
		if (sjme_error_is(error = sjme_alloc_free(source)))
			return sjme_error_default(error);

		/* Clear pointer. */
		*inOutAddr = NULL;

		/* Success! */
		return SJME_ERROR_NULL_ARGUMENTS;
	}

	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(source,
		&link)) || link == NULL)
		return sjme_error_default(error);
	
	/* If there is a weak reference, then we cannot touch this. */
	if (sjme_atomic_g(sjme_alloc_weak, &link->weak) != NULL)
		return SJME_ERROR_WEAK_REFERENCE_ATTACHED;

	/* Pointless operation. */
	if (newSize == link->allocSize)
		return SJME_ERROR_NONE;

	/* There are some padding bytes we can consume. */
	else if (newSize > link->allocSize && newSize < link->blockSize)
	{
		/* Just set the new allocation size. */
		link->allocSize = newSize;
		
		/* Emit barrier. */
		sjme_atomic_barrier();

		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* No space to grow or shrink, move it. */
	else
	{
		/* How much do we actually want to copy? */
		if (newSize < link->allocSize)
			limit = newSize;
		else
			limit = link->allocSize;

		/* Debug. */
		sjme_message("Realloc copy %d -> %d (%d)",
			link->allocSize, newSize, limit);

		/* Allocate new block. */
		result = NULL;
		if (sjme_error_is(error = sjme_allocR(
			link->pool, newSize, &result
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
			result == NULL)
			return sjme_error_defaultOr(error,
				sjme_error_outOfMemory(link->pool, limit));

		/* Copy all the data over. */
		memmove(result, source, limit);

		/* Free the old block. */
		if (sjme_error_is(error = sjme_alloc_free(source)))
			return sjme_error_default(error);
		
		/* Emit barrier. */
		sjme_atomic_barrier();

		/* Success! */
		*inOutAddr = result;
		return SJME_ERROR_NONE;
	}
}

sjme_errorCode sjme_alloc_strdupR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_lpstr* outString,
	sjme_attrInNotNull sjme_lpcstr stringToCopy
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_jint charLen;
	
	if (allocPool == NULL || outString == NULL || stringToCopy == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use standard string length, include NUL. */
	charLen = strlen(stringToCopy) + 1;
	
	/* Then just forward to copy. */
#if defined(SJME_CONFIG_DEBUG)
	return sjme_alloc_copyR(allocPool, charLen,
		(sjme_pointer*)outString, (sjme_pointer)stringToCopy,
		file, line, func);
#else
	return sjme_alloc_copy(allocPool, charLen,
		(sjme_pointer*)outString, (sjme_pointer)stringToCopy);
#endif
}

sjme_errorCode sjme_noOptimize sjme_alloc_weakDeleteR(
	sjme_attrInOutNotNull sjme_alloc_weak* inOutWeak
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_jint count, newCount;
	sjme_alloc_link link;
	sjme_pointer block;
	
	if (inOutWeak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Operate on this weak. */
	weak = *inOutWeak;
	
	/* Already free? */
	if (weak == NULL)
		return SJME_ERROR_NONE;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Get the current count, and the next count. */
	count = sjme_atomic_g(sjme_jint, &weak->count);
	newCount = (count > 1 ? count - 1 : 0);
		
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG_ALLOC)
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"Weak ref %p (%p) count down to %d.",
		weak->pointer, weak, newCount);
#endif
	
	/* If zero is reached, it is eligible for free. */
	/* Note that the free could have previously been called! */
	link = sjme_atomic_g(sjme_alloc_link, &weak->link);
	block = sjme_atomic_pg(&weak->pointer);
	if (newCount <= 0)
	{
		/* If we are already in this, do not free as we will corrupt */
		/* memory recursing into this. */
		if (!sjme_atomic_cs(sjme_jint, &weak->inEnqueue,
			0, 1))
			goto any_cancelFree;
		
		/* Call enqueue handler. */
		error = SJME_ERROR_NONE;
		if (weak->enqueue != NULL)
			error = weak->enqueue(weak, block, newCount,
				SJME_JNI_TRUE, SJME_JNI_FALSE);
		
		/* Unset. */
		sjme_atomic_s(sjme_jint, &weak->inEnqueue, 0);
		
		/* Failed enqueue? */
		if (sjme_error_is(error))
			goto fail_enqueue;
		
		/* Clear any weak reference details. */
		sjme_atomic_psNull(&link->weak);
		sjme_atomic_psNull(&weak->link);
		sjme_atomic_psNull(&weak->pointer);
		
		/* Free the block we point to. */
		if (sjme_error_is(error = sjme_alloc_free(block)))
			return sjme_error_default(error);
		
		/* Free the weak reference data as it is now invalid. */
		if (sjme_error_is(error = sjme_alloc_free(weak)))
			return sjme_error_default(error);
		*inOutWeak = NULL;
	}
	
	/* Otherwise, just count it down. */
	else if (newCount >= 1)
	{
		/* Mark down count. */
		sjme_atomic_s(sjme_jint, &weak->count, newCount);
		
		/* If there is an enqueue handler, tell it we counted down. */
		if (weak->enqueue != NULL)
			if (sjme_error_is(error = weak->enqueue(weak,
				sjme_atomic_pg(&weak->pointer), newCount,
				SJME_JNI_FALSE, SJME_JNI_FALSE)))
				goto fail_indicateCountDown;
	}
	
	/* Emit barrier. */
any_cancelFree:
	sjme_atomic_barrier();
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_enqueue:
fail_indicateCountDown:
	return sjme_error_default(error);
}

sjme_errorCode sjme_alloc_weakGetPointer(
	sjme_attrInNotNull sjme_alloc_weak inWeak,
	sjme_attrOutNotNull sjme_pointer* outPointer)
{
	if (inWeak == NULL || outPointer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	if (sjme_atomic_pg(&inWeak->link) == NULL ||
		sjme_atomic_pg(&inWeak->pointer) == NULL)
		*outPointer = NULL;
	else
		*outPointer = sjme_atomic_pg(&inWeak->pointer);
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_noOptimize sjme_alloc_weakRefInternal(
	sjme_attrInNotNull sjme_pointer addr,
	sjme_attrOutNullable sjme_alloc_weak* outWeak,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_alloc_link link;
	sjme_alloc_weak result;
	sjme_jint was;
	
	if (addr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
		
	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr,
		&link)) || link == NULL)
		return sjme_error_default(error);
	
	/* Is there already a weak reference? */
	result = sjme_atomic_g(sjme_alloc_weak, &link->weak);
	if (result != NULL)
	{
		/* Enqueue can be set, but not overwritten. */
		if (inEnqueue != NULL)
		{
			/* Set it? */
			if (result->enqueue == NULL)
				result->enqueue = inEnqueue;
			
			/* Must be the same function. */
			else if (result->enqueue != inEnqueue)
				return SJME_ERROR_ENQUEUE_ALREADY_SET;
		}
		
		/* Count up. */
#if defined(SJME_CONFIG_DEBUG_ALLOC)
		was =
#endif
			sjme_atomic_ga(sjme_jint, &result->count, 1);
		
		/* Emit barrier. */
		sjme_atomic_barrier();
		
		/* Debug. */
#if defined(SJME_CONFIG_DEBUG_ALLOC)
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"Weak ref %p (%p) count up to %d.",
			result->pointer, result, was + 1);
#endif
		
		/* Use it. */
		if (outWeak != NULL)
			*outWeak = result;
		return SJME_ERROR_NONE;
	}
	
	/* We need to allocate the link. */
	result = NULL;
	if (sjme_error_is(error = sjme_allocR(link->pool, sizeof(*result),
		(sjme_pointer*)&result
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)))
		return sjme_error_default(error);
	
	/* Setup link information. */
	sjme_atomic_s(sjme_jint, &result->valid,
		SJME_ALLOC_WEAK_VALID);
	sjme_atomic_s(sjme_alloc_link, &result->link, link);
	sjme_atomic_s(sjme_pointer, &result->pointer, addr);
	result->enqueue = inEnqueue;
	sjme_atomic_s(sjme_jint, &result->count, 0);

	/* Flag link as weak. */
	link->flags |= SJME_ALLOC_LINK_WEAK;
	
	/* Join link back to this. */
	sjme_atomic_s(sjme_alloc_weak, &link->weak, result);
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG_ALLOC)
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"Weak ref new %p (%p).",
		result->pointer, result);
#endif
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Success! */
	if (outWeak != NULL)
		*outWeak = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_alloc_weakNewR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrOutNotNull sjme_pointer* outAddr,
	sjme_attrOutNullable sjme_alloc_weak* outWeak
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_pointer resultPtr;
	sjme_alloc_weak resultWeak;
	sjme_errorCode error;
	
	if (allocPool == NULL || outAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Take ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&allocPool->spinLock)))
		return sjme_error_default(error);
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Attempt block allocation first. */
	resultPtr = NULL;
	if (sjme_error_is(error = sjme_allocR(allocPool, size,
		(sjme_pointer*)&resultPtr
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		resultPtr == NULL)
		goto fail_allocBlock;
	
	/* Then create the weak reference. */
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefInternal(resultPtr,
		&resultWeak, inEnqueue
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		resultWeak == NULL)
		goto fail_allocWeak;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
	
	/* Release ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&allocPool->spinLock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outAddr = resultPtr;
	if (outWeak != NULL)
		*outWeak = resultWeak;
	return SJME_ERROR_NONE;

fail_allocWeak:
fail_allocBlock:
	if (resultPtr != NULL)
		sjme_alloc_free(resultPtr);
	
	/* Release ownership of lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&allocPool->spinLock, NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_alloc_weakRefER(
	sjme_attrInNotNull sjme_pointer addr,
	sjme_attrOutNullable sjme_alloc_weak* outWeak,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_pool pool;
	sjme_errorCode error;
	sjme_alloc_link link;
	
	if (addr == NULL ||
		(inEnqueue == NULL && inEnqueueData != NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
		
	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr,
		&link)) || link == NULL)
		return sjme_error_default(error);
	
	/* No weak reference here? */
	if (sjme_atomic_g(sjme_alloc_weak, &link->weak) == NULL)
		return SJME_ERROR_NOT_WEAK_REFERENCE;
		
	/* Take ownership of lock. */
	pool = link->pool;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&pool->spinLock)))
		return sjme_error_default(error);
	
	/* Forward. */
	error = sjme_alloc_weakRefInternal(addr, outWeak, inEnqueue
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);
		
	/* Release ownership of lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);
	
	/* Failed? */
	return error;
}

sjme_pointer sjme_weakUp(
	sjme_attrInNullable sjme_pointer addr)
{
	sjme_alloc_weak weak;
	sjme_errorCode error;

	/* Propagate null, do not count. */
	if (addr == NULL)
		return NULL;

	/* Count up. */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRef(addr,
		&weak)) || weak == NULL)
		sjme_die("sjme_weakUp(%p): %d", addr, error);

	/* Return self. */
	return addr;
}

sjme_errorCode sjme_alloc_weakRefGet(
	sjme_attrInNotNull sjme_pointer addr,
	sjme_attrOutNullable sjme_alloc_weak* outWeak)
{
	sjme_alloc_pool pool;
	sjme_errorCode error;
	sjme_alloc_link link;
	sjme_alloc_weak weak;
	sjme_intPointer weakAddr;
	
	if (addr == NULL || outWeak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Emit barrier. */
	sjme_atomic_barrier();
		
	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLinkOptional(addr,
		&link, SJME_JNI_FALSE)) || link == NULL)
	{
		/* If the link is not valid, it cannot be a weak reference. */
		if (error == SJME_ERROR_NOT_ALLOC_LINK)
			return SJME_ERROR_NOT_WEAK_REFERENCE;
		
		/* Otherwise, fail... */
		return sjme_error_default(error);
	}
		
	/* This should never be null. */
	pool = link->pool;
	if (pool == NULL || pool->magic != SJME_ALLOC_POOL_MAGIC)
		return SJME_ERROR_NOT_WEAK_REFERENCE;

	/* Quickly check if this is not a weak reference as to not lock. */
	weak = sjme_atomic_g(sjme_alloc_weak, &link->weak);
	if (weak == NULL)
		return SJME_ERROR_NOT_WEAK_REFERENCE;
	
	/* Take ownership of lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&pool->spinLock)))
		return sjme_error_default(error);
	
	/* No weak reference here? Or it changed to something else? */
	/* Also check if de-referencing would exceed the pool bounds. */
	/* Or otherwise not marked valid. */
	weakAddr = (sjme_intPointer)weak;
	if (weak == NULL || weakAddr < ((sjme_intPointer)pool) ||
		weakAddr >= (((sjme_intPointer)pool) + pool->size) ||
		weakAddr < ((sjme_intPointer)&pool->block[0]) ||
		sjme_atomic_pg(&weak->pointer) != addr ||
		sjme_atomic_g(sjme_jint, &weak->valid) !=
			SJME_ALLOC_WEAK_VALID)
		error = SJME_ERROR_NOT_WEAK_REFERENCE;
	else
		error = SJME_ERROR_NONE;
		
	/* Release ownership of lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&pool->spinLock, NULL)))
		return sjme_error_default(error);
	
	/* Did it fail? */
	if (sjme_error_is(error))
		return sjme_error_default(error);
	
	/* Give it! */
	*outWeak = weak;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_weakUnRefR(
	sjme_attrInNotNull sjme_pointer addr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	
	if (addr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Obtain weak reference. */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(addr,
		&weak)) || weak == NULL)
		return sjme_error_default(error);
	
	/* Delete it. */
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_alloc_weakDeleteR(&weak,
		file, line, func)))
#else
	if (sjme_error_is(error = sjme_alloc_weakDelete(&weak)))
#endif
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_jint sjme_alloc_weakRefLeftR(
	sjme_attrInNotNull sjme_pointer addr)
{
	sjme_alloc_weak weak;
	sjme_jint result;
	sjme_alloc_link link;
	
	/* Null is an implicit negative count. */
	if (addr == NULL)
		return -1;

	/* Get the actual weak information here. */
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakRefGet(addr, &weak)) || weak == NULL)
	{
		/* Try to recover the link. */
		link = NULL;
		if (sjme_error_is(sjme_alloc_getLink(addr, &link)) ||
			link == NULL)
			return -1;

		/* Broken weak reference? */
		if ((link->flags & SJME_ALLOC_LINK_WEAK) != 0)
			return INT32_MIN;

		/* Not a weak reference. */
		return -1;
	}

	/* Return the count. */
	result = sjme_atomic_g(sjme_jint, &weak->count);
	if (result < 0)
		return INT32_MIN;
	return result;
}

#if defined(SJME_CONFIG_DEBUG)

sjme_errorCode sjme_alloc_poolDump(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInValue sjme_jboolean onlyUsed)
{
	sjme_alloc_link rover;
	sjme_jint idType, weakLeft;

	if (allocPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Dump information on every link. */
	for (rover = sjme_atomic_g(sjme_alloc_link, &allocPool->frontLink);
		rover != NULL; rover = sjme_atomic_g(sjme_alloc_link, &rover->next))
	{
		/* Check corruption. */
		if (sjme_alloc_checkCorruption(allocPool, rover))
			sjme_messageB("CORRUPTED LINK! %p", rover);
		
		/* Only care about used space? */
		if (onlyUsed && rover->space != SJME_ALLOC_POOL_SPACE_USED)
			continue;
		
		/* Print link information. */
		idType = -1;
		if (allocPool->pointerIdType != NULL &&
			rover->space == SJME_ALLOC_POOL_SPACE_USED)
			idType = allocPool->pointerIdType((sjme_pointer*)&rover->block[0]);
		weakLeft = sjme_alloc_weakRefLeftR((sjme_pointer*)&rover->block[0]);
		
		if (weakLeft >= 0 || weakLeft == INT32_MIN ||
			(rover->flags & SJME_ALLOC_LINK_WEAK) != 0)
			sjme_messageB(
				"Link %d:%p [W%d]: %s %dB in %s (%s:%d)",
					idType, &rover->block[0], weakLeft,
					(rover->space == SJME_ALLOC_POOL_SPACE_USED ?
						"USED" : "FREE"),
					rover->blockSize,
					rover->debugFunction,
					sjme_debug_shortenFile(rover->debugFile),
					rover->debugLine);
		else
			sjme_messageB(
				"Link %d:%p [S]: %s %dB in %s (%s:%d)",
					idType, &rover->block[0],
					(rover->space == SJME_ALLOC_POOL_SPACE_USED ?
						"USED" : "FREE"),
					rover->blockSize,
					rover->debugFunction,
					sjme_debug_shortenFile(rover->debugFile),
					rover->debugLine);
	}

	return SJME_ERROR_NONE;
}

#endif

