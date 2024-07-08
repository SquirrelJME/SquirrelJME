/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

/* Include Valgrind if it is available? */
#if defined(SJME_CONFIG_HAS_VALGRIND)
	#include <valgrind.h>
	#include <memcheck.h>
#endif

#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/atomic.h"
#include "sjme/multithread.h"

/** The minimum size permitted for allocation pools. */
#define SJME_ALLOC_MIN_SIZE (((SJME_SIZEOF_ALLOC_POOL(0) + \
	(SJME_SIZEOF_ALLOC_LINK(0) * 3)) | 0x1FF))

/** The minimum size for splits. */
#define SJME_ALLOC_SPLIT_MIN_SIZE 64

#if defined(SJME_CONFIG_DEBUG)
/**
 * Prints information on a given link and returns.
 *
 * @param pool The pool this is in.
 * @param atLink The link to print info for.
 * @param trigger The trigger for the failure.
 * @return Always @c SJME_JNI_TRUE .
 * @since 2023/12/29
 */
static sjme_inline sjme_jboolean sjme_alloc_corruptFail(
	sjme_alloc_pool* pool, sjme_alloc_link* atLink, const char* trigger)
{
	sjme_message("Corrupted Link %p: %s", atLink, trigger);

	/* Ignore if null. */
	if (atLink == NULL)
		return SJME_JNI_TRUE;

	/* Dump everything about the link. */
	sjme_message("link->pool: %p (should be %p)", atLink->pool, pool);
	sjme_message("link->prev: %p", atLink->prev);
	sjme_message("link->next: %p", atLink->next);
	if (atLink->space == SJME_ALLOC_POOL_SPACE_USED)
		sjme_message("link->space: USED");
	else if (atLink->space == SJME_ALLOC_POOL_SPACE_FREE)
		sjme_message("link->space: FREE");
	else if (atLink->space == SJME_NUM_ALLOC_POOL_SPACE)
		sjme_message("link->space: NUM");
	else
		sjme_message("link->space: %d", (int)atLink->space);
	sjme_message("link->weak: %p", atLink->weak);
	sjme_message("link->freePrev: %p", atLink->freePrev);
	sjme_message("link->freeNext: %p", atLink->freeNext);
	sjme_message("link->allocSize: %d", (int)atLink->allocSize);
	sjme_message("link->blockSize: %d", (int)atLink->blockSize);

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
 * @return Always @c SJME_JNI_TRUE .
 * @since 2023/12/29
 */
#define sjme_alloc_corruptFail(pool, atLink, trigger) SJME_JNI_TRUE
#endif

static sjme_inline sjme_jboolean sjme_alloc_checkCorruptionRange(
	sjme_alloc_pool* pool, uintptr_t poolStart, uintptr_t poolEnd,
	sjme_alloc_link* atLink)
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
static sjme_jboolean sjme_alloc_checkCorruption(sjme_alloc_pool* pool,
	sjme_alloc_link* atLink)
{
	uintptr_t poolStart, poolEnd;

	if (pool == NULL)
		return SJME_JNI_TRUE;

	/* If no link is specified, ignore. */
	if (atLink == NULL)
		return SJME_JNI_FALSE;

	/* Link is in the wrong pool. */
	if (atLink->pool != pool)
		return sjme_alloc_corruptFail(pool, atLink,
			"Wrong pool");

	/* Next link is in the wrong location? */
	if (atLink->next != NULL && (uintptr_t)atLink->next !=
		(uintptr_t)&atLink->block[atLink->blockSize])
		return sjme_alloc_corruptFail(pool, atLink,
			"Next not at block end");

	/* Is front/end link? */
	if (atLink == pool->frontLink || atLink == pool->backLink)
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
				atLink->freePrev))
			return sjme_alloc_corruptFail(pool, atLink,
				"Corrupt freePrev");
		if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
				atLink->freeNext))
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
		if (atLink->freePrev != NULL || atLink->freeNext != NULL)
			return sjme_alloc_corruptFail(pool, atLink,
				"Used has free links");
	}

	/* Link space incorrect? */
	else
		return sjme_alloc_corruptFail(pool, atLink,
			"Incorrect space");

	/* Check common next links. */
	if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
		atLink->prev))
		return sjme_alloc_corruptFail(pool, atLink,
			"Corrupt prev");
	if (sjme_alloc_checkCorruptionRange(pool, poolStart, poolEnd,
		atLink->next))
		return sjme_alloc_corruptFail(pool, atLink,
			"Corrupt next");

	/* Does not appear corrupt. */
	return SJME_JNI_FALSE;
}

static sjme_errorCode sjme_alloc_getLinkOptional(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_link** outLink,
	sjme_attrInValue sjme_jboolean checkCorruption)
{
	sjme_alloc_link* link;

	if (addr == NULL || outLink == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just need to do some reversing math. */
	link = (sjme_alloc_link*)(((uintptr_t)addr) -
		offsetof(sjme_alloc_link, block));

	/* Check the integrity of the link. */
	if (checkCorruption)
		sjme_alloc_checkCorruption(link->pool, link);
	
	/* Success! */
	*outLink = link;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_poolInitMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive sjme_jint size)
{
	void* result;
	sjme_jint useSize;

	/* Make sure the size is not wonky. */
	useSize = SJME_SIZEOF_ALLOC_POOL(size);
	if (outPool == NULL || size <= SJME_ALLOC_MIN_SIZE || useSize <= 0 ||
		size > useSize)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Attempt allocation. */
	result = malloc(useSize);
	if (!result)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Use static pool initializer to set up structures. */
	return sjme_alloc_poolInitStatic(outPool, result, useSize);
}

sjme_errorCode sjme_alloc_poolInitStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive sjme_jint size)
{
	sjme_alloc_pool* pool;
	sjme_alloc_link* frontLink;
	sjme_alloc_link* midLink;
	sjme_alloc_link* backLink;
	sjme_alloc_link* specialParent;

	if (outPool == NULL || baseAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (size <= SJME_ALLOC_MIN_SIZE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Initialize memory to nothing. */
	memset(baseAddr, 0, size);
	
	/* Setup initial pool structure. */
	pool = baseAddr;
	pool->size = (size & (~7)) - SJME_SIZEOF_ALLOC_POOL(0);
	
	/* Setup front link. */
	frontLink = (void*)&pool->block[0];
	pool->frontLink = frontLink;
	
	/* Setup back link. */
	backLink = (void*)&pool->block[pool->size - SJME_SIZEOF_ALLOC_LINK(0)];
	pool->backLink = backLink;
	
	/* Setup middle link, which is between the two. */
	midLink = (void*)&frontLink->block[0];
	midLink->prev = frontLink;
	frontLink->next = midLink;
	midLink->next = backLink;
	backLink->prev = midLink;
	
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
	pool->freeFirstLink = frontLink;
	frontLink->freeNext = midLink;
	midLink->freePrev = frontLink;
	pool->freeLastLink = backLink;
	backLink->freePrev = midLink;
	midLink->freeNext = backLink;

	/* Link in pools. */
	frontLink->pool = pool;
	midLink->pool = pool;
	backLink->pool = pool;
	
#if defined(SJME_CONFIG_HAS_VALGRIND)
	/* Reserve front side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(baseAddr,
		((uintptr_t)&midLink->block[0] - (uintptr_t)baseAddr));
		
	/* Reserve back side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(backLink,
		(SJME_SIZEOF_ALLOC_LINK(0)));
#endif

	/* If this is a valid link then we are allocating a nested pool. */
#if 0
	specialParent = NULL;
	if (!sjme_error_is(sjme_alloc_getLinkOptional(baseAddr,
		&specialParent, SJME_JNI_FALSE)))
		specialParent->flags |= SJME_ALLOC_LINK_FLAG_NESTED_POOL;
#endif
	
	/* Use the pool. */
	*outPool = pool;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_poolSpaceTotalSize(
	sjme_attrInNotNull const sjme_alloc_pool* pool,
	sjme_attrOutNullable sjme_jint* outTotal,
	sjme_attrOutNullable sjme_jint* outReserved,
	sjme_attrOutNullable sjme_jint* outUsable)
{
	sjme_jint total, i;
	sjme_jint reserved;
	sjme_jint usable;

	if (pool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outTotal == NULL && outReserved == NULL && outUsable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Run through and tally values for each space. */
	reserved = 0;
	usable = 0;
	for (i = 0; i < SJME_NUM_ALLOC_POOL_SPACE; i++)
	{
		reserved += pool->space[i].reserved;
		usable += pool->space[i].usable;
	}

	/* Total space is both. */
	total = reserved + usable;

	/* Store output values. */
	if (outTotal != NULL)
		*outTotal = total;
	if (outReserved != NULL)
		*outReserved = reserved;
	if (outUsable != NULL)
		*outUsable = usable;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc)(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_link* scanLink;
	sjme_alloc_link* rightLink;
	sjme_jint splitMinSize, roundSize;
	sjme_jboolean splitBlock;
	
	if (pool == NULL || size <= 0 || outAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine the size this will actually take up, which includes the */
	/* link to be created following this. */
	roundSize = (((size & 7) != 0) ? ((size | 7) + 1) : size);
	splitMinSize = roundSize +
		(sjme_jint)SJME_SIZEOF_ALLOC_LINK(SJME_ALLOC_SPLIT_MIN_SIZE) +
		(sjme_jint)SJME_SIZEOF_ALLOC_LINK(0);
	if (size > splitMinSize || splitMinSize < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Find the first free link that this fits in. */
	scanLink = NULL;
	splitBlock = SJME_JNI_FALSE;
	for (scanLink = pool->freeFirstLink;
		scanLink != NULL; scanLink = scanLink->freeNext)
	{
		/* Has memory been corrupted? */
		if (sjme_alloc_checkCorruption(pool, scanLink))
			return SJME_ERROR_MEMORY_CORRUPTION;

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
		if (pool->nextPool != NULL)
		{
#if defined(SJME_CONFIG_DEBUG)
			return sjme_allocR(pool->nextPool, size, outAddr,
				file, line, func);
#else
			return sjme_alloc(pool->nextPool, size, outAddr);
#endif
		}
		
		/* Otherwise fail! */
		return SJME_ERROR_OUT_OF_MEMORY;
	}

	/* Debug. */
	sjme_message("Found link at %p: %d bytes, we need %d with split %d.",
		scanLink, (int)scanLink->blockSize, (int)roundSize, (int)splitBlock);

	/* Does this block need to be split? */
	if (splitBlock)
	{
		/* Check for link corruption on the adjacent links. */
		if (sjme_alloc_checkCorruption(pool, scanLink->next) ||
			sjme_alloc_checkCorruption(pool, scanLink->prev) ||
			sjme_alloc_checkCorruption(pool, scanLink->freeNext) ||
			sjme_alloc_checkCorruption(pool, scanLink->freePrev))
			return SJME_ERROR_MEMORY_CORRUPTION;

		/* Make it so this block can actually fit in here. */
		rightLink = (sjme_alloc_link*)&scanLink->block[roundSize];

		/* Initialize block to remove any old data. */
		memset(rightLink, 0, sizeof(*rightLink));

		/* Set the right link's pool accordingly. */
		rightLink->pool = pool;

		/* Make sure this block is marked as free. */
		rightLink->space = SJME_ALLOC_POOL_SPACE_FREE;

		/* Set size of the right link. */
		rightLink->blockSize =
			(sjme_jint)((intptr_t)&scanLink->block[scanLink->blockSize] -
				(intptr_t)&rightLink->block[0]);

		/* Link in physical links. */
		rightLink->next = scanLink->next;
		rightLink->next->prev = rightLink;
		scanLink->next = rightLink;
		rightLink->prev = scanLink;

		/* Link in free links. */
		rightLink->freeNext = scanLink->freeNext;
		rightLink->freeNext->freePrev = rightLink;
		scanLink->freeNext = rightLink;
		rightLink->freePrev = scanLink;

		/* Set size of the left block. */
		scanLink->blockSize =
			(sjme_jint)((intptr_t)rightLink - (intptr_t)&scanLink->block[0]);

		/* Adjust reserved and usable space. */
		pool->space[SJME_ALLOC_POOL_SPACE_FREE].reserved +=
			SJME_SIZEOF_ALLOC_LINK(0);
		pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable -=
			SJME_SIZEOF_ALLOC_LINK(0);

		/* Make sure we did not cause corruption. */
		if (sjme_alloc_checkCorruption(pool, scanLink) ||
			sjme_alloc_checkCorruption(pool, rightLink))
			return SJME_ERROR_MEMORY_CORRUPTION;
	}

	/* Setup block information. */
	scanLink->space = SJME_ALLOC_POOL_SPACE_USED;

	/* Unlink from free links. */
	if (scanLink->freeNext != NULL)
		scanLink->freeNext->freePrev = scanLink->freePrev;
	if (scanLink->freePrev != NULL)
		scanLink->freePrev->freeNext = scanLink->freeNext;
	scanLink->freePrev = NULL;
	scanLink->freeNext = NULL;

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
	if (sjme_alloc_checkCorruption(pool, scanLink))
		return SJME_ERROR_MEMORY_CORRUPTION;
	
	/* Emit barrier. */
	sjme_thread_barrier();
	
	/* Use the given link. */
	*outAddr = &scanLink->block[0];
	return SJME_ERROR_NONE;
}

sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_copy)(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr,
	sjme_attrInNotNull void* inAddr
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	void* dest;

	if (pool == NULL || outAddr == NULL || inAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate new copy first. */
	dest = NULL;
	if (sjme_error_is(error = SJME_DEBUG_IDENTIFIER(sjme_alloc)(
		pool, size, &dest
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) || dest == NULL)
		return sjme_error_default(error);

	/* Copy over. */
	memmove(dest, inAddr, size);
	*outAddr = dest;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_format)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_lpstr* outString,
	SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL SJME_DEBUG_ONLY_COMMA
	sjme_attrInNotNull sjme_attrFormatArg const char* format,
	...)
{
#define BUF_SIZE 512
	char buf[BUF_SIZE];
	va_list arg;
	int len;

	if (inPool == NULL || outString == NULL || format == NULL)
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
	return SJME_DEBUG_IDENTIFIER(sjme_alloc_copy)(inPool, len + 1,
		(void**)outString, buf
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);
#undef BUF_SIZE
}

static sjme_errorCode sjme_alloc_mergeFree(sjme_alloc_link* link)
{
	sjme_alloc_pool* pool;
	sjme_alloc_link* right;
	sjme_alloc_link* oldRightFreeNext;
	sjme_alloc_link* rightRight;
	sjme_alloc_link* checkLeft;
	sjme_jint addedSize;
	
	if (link == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Need pool for all operations. */
	pool = link->pool;
	
	/* If the previous block is free, pivot to there. */
	checkLeft = link->prev;
	if (checkLeft->space == SJME_ALLOC_POOL_SPACE_FREE)
		return sjme_alloc_mergeFree(checkLeft);
	
	/* Is the block on the right a candidate for merge? */
	right = link->next;
	if (right->space != SJME_ALLOC_POOL_SPACE_FREE)
		return SJME_ERROR_NONE;
	
	/* We need the block after to relink. */
	rightRight = right->next;
	
	/* Disconnect in the middle. */
	link->next = rightRight;
	rightRight->prev = link;
	
	/* Remove from the free chain. */
	oldRightFreeNext = right->freeNext;
	right->freePrev->freeNext = right->freeNext;
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
	memset(right, 0, sizeof(*right));
	
	/* Should not have corrupted the block. */
	if (sjme_alloc_checkCorruption(pool, link))
		return SJME_ERROR_MEMORY_CORRUPTION;
	
	/* We merged a block, so check again. */
	return sjme_alloc_mergeFree(link);
}

sjme_errorCode sjme_alloc_free(
	sjme_attrInNotNull void* addr)
{
	sjme_alloc_link* link;
	sjme_alloc_pool* pool;
	sjme_errorCode error;
	sjme_alloc_weak* weak;

	if (addr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr, &link)))
		return sjme_error_default(error);

	/* Check the integrity of it. */
	pool = link->pool;
	if (sjme_alloc_checkCorruption(pool, link))
		return SJME_ERROR_MEMORY_CORRUPTION;
	
	/* If there is a weak reference, clear it. */
	weak = link->weak;
	if (weak != NULL)
	{
		link->weak = NULL;
		weak->link = NULL;
		weak->pointer = NULL;
	}

	/* Mark block as free. */
	link->space = SJME_ALLOC_POOL_SPACE_FREE;
	
	/* Clear flags, if any. */
	link->flags = 0;
	
	/* Restore allocation size to block size. */
	link->allocSize = link->blockSize;

#if defined(SJME_CONFIG_DEBUG)
	/* Remove debug information. */
	link->debugFile = NULL;
	link->debugLine = 0;
	link->debugFunction = NULL;
#endif

	/* Link into free chain. */
	link->freeNext = pool->freeFirstLink->freeNext;
	pool->freeFirstLink->freeNext = link;
	link->freeNext->freePrev = link;
	link->freePrev = pool->freeFirstLink;

	/* Merge together free blocks. */
	if (sjme_error_is(error = sjme_alloc_mergeFree(link)))
		return sjme_error_default(error);
		
	/* Emit barrier. */
	sjme_thread_barrier();

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_getLink(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_link** outLink)
{
	return sjme_alloc_getLinkOptional(addr, outLink,
		SJME_JNI_TRUE);
}

sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_realloc)(
	sjme_attrInOutNotNull void** inOutAddr,
	sjme_attrInPositive sjme_jint newSize
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_alloc_link* link;
	void* result;
	void* source;
	sjme_jint limit;
	sjme_errorCode error;

	if (inOutAddr == NULL || *inOutAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (newSize < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

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
	if (link->weak != NULL)
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
		sjme_thread_barrier();

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
		if (sjme_error_is(error = SJME_DEBUG_IDENTIFIER(sjme_alloc)(
			link->pool, newSize, &result
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
			result == NULL)
			return sjme_error_defaultOr(error,
				SJME_ERROR_OUT_OF_MEMORY);

		/* Copy all the data over. */
		memmove(result, source, limit);

		/* Free the old block. */
		if (sjme_error_is(error = sjme_alloc_free(source)))
			return sjme_error_default(error);
		
		/* Emit barrier. */
		sjme_thread_barrier();

		/* Success! */
		*inOutAddr = result;
		return SJME_ERROR_NONE;
	}
}

sjme_errorCode sjme_alloc_weakDelete(
	sjme_attrInOutNotNull sjme_alloc_weak** inOutWeak)
{
	if (inOutWeak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_alloc_weakGet(
	sjme_attrInNotNull sjme_alloc_weak* inWeak,
	sjme_attrOutNotNull sjme_pointer* outPointer)
{
	if (inWeak == NULL || outPointer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_alloc_weakRefInternal(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_weak** outWeak,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData)
{
	sjme_errorCode error;
	sjme_alloc_link* link;
	sjme_alloc_weak* result;
	
	if (addr == NULL || outWeak == NULL ||
		(inEnqueue == NULL && inEnqueueData != NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr,
		&link)) || link == NULL)
		return sjme_error_default(error);
	
	/* Is there already a weak reference? */
	result = link->weak;
	if (result != NULL)
	{
		/* Enqueue can be set, but not overwritten. */
		if (inEnqueue != NULL)
		{
			/* Set it? */
			if (result->enqueue == NULL)
			{
				result->enqueue = inEnqueue;
				result->enqueueData = inEnqueueData;
			}
			
			/* Must be the same function. */
			else if (result->enqueue != inEnqueue)
				return SJME_ERROR_ENQUEUE_ALREADY_SET;
		}
		
		/* Count up. */
		sjme_atomic_sjme_jint_getAdd(&result->count, 1);
		
		/* Emit barrier. */
		sjme_thread_barrier();
		
		/* Use it. */
		*outWeak = result;
		return SJME_ERROR_NONE;
	}
	
	/* We need to allocate the link. */
	if (sjme_error_is(error = sjme_alloc(link->pool, sizeof(result),
		&result)))
		return sjme_error_default(error);
	
	/* Setup link information. */
	result->link = link;
	result->pointer = addr;
	result->enqueue = inEnqueue;
	result->enqueueData = inEnqueueData;
	sjme_atomic_sjme_jint_set(&result->count, 1);
	
	/* Join link back to this. */
	link->weak = result;
	
	/* Emit barrier. */
	sjme_thread_barrier();
	
	/* Success! */
	*outWeak = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode SJME_DEBUG_IDENTIFIER(sjme_alloc_weakNew)(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData,
	sjme_attrOutNotNull void** outAddr,
	sjme_attrOutNotNull sjme_alloc_weak** outWeak
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	void* resultPtr;
	sjme_alloc_weak* resultWeak;
	sjme_errorCode error;
	
	if (pool == NULL || outAddr == NULL || outWeak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Attempt block allocation first. */
	resultPtr = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, size, &resultPtr)) ||
		resultPtr == NULL)
		return sjme_error_default(error);
	
	/* Then create the weak reference. */
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefInternal(resultPtr,
		&resultWeak, inEnqueue, inEnqueueData)) || resultWeak == NULL)
	{
		/* Cleanup old allocation. */
		sjme_alloc_free(resultPtr);
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outAddr = resultPtr;
	*outWeak = resultWeak;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc_weakRef(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_weak** outWeak,
	sjme_attrInNullable sjme_alloc_weakEnqueueFunc inEnqueue,
	sjme_attrInNullable sjme_pointer inEnqueueData)
{
	sjme_errorCode error;
	sjme_alloc_link* link;
	
	if (addr == NULL || outWeak == NULL ||
		(inEnqueue == NULL && inEnqueueData != NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover the link. */
	link = NULL;
	if (sjme_error_is(error = sjme_alloc_getLink(addr,
		&link)) || link == NULL)
		return sjme_error_default(error);
	
	/* No weak reference here? */
	if (link->weak == NULL)
		return SJME_ERROR_NOT_WEAK_REFERENCE;
	
	/* Forward. */
	return sjme_alloc_weakRefInternal(addr, outWeak, inEnqueue, inEnqueueData);
}
