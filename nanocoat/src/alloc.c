/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

/* Include Valgrind if it is available? */
#if defined(SJME_CONFIG_HAS_VALGRIND)
	#include <valgrind.h>
	#include <memcheck.h>
#endif

#include "sjme/alloc.h"
#include "sjme/debug.h"

/** The minimum size permitted for allocation pools. */
#define SJME_ALLOC_MIN_SIZE (((SJME_SIZEOF_ALLOC_POOL(0) + \
	(SJME_SIZEOF_ALLOC_LINK(0) * 3)) | 0x1FF))

/** The minimum size for splits. */
#define SJME_ALLOC_SPLIT_MIN_SIZE 64

sjme_errorCode sjme_alloc_poolMalloc(
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
	return sjme_alloc_poolStatic(outPool, result, useSize);
}

sjme_errorCode sjme_alloc_poolStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive sjme_jint size)
{
	sjme_alloc_pool* result;
	sjme_alloc_link* frontLink;
	sjme_alloc_link* midLink;
	sjme_alloc_link* backLink;

	if (outPool == NULL || baseAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (size <= SJME_ALLOC_MIN_SIZE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Initialize memory to nothing. */
	memset(baseAddr, 0, size);
	
	/* Setup initial pool structure. */
	result = baseAddr;
	result->size = size & (~7);
	
	/* Setup front link. */
	frontLink = (void*)&result->block[0];
	result->frontLink = frontLink;
	
	/* Setup back link. */
	backLink = (void*)&result->block[result->size - SJME_SIZEOF_ALLOC_LINK(0)];
	result->backLink = backLink;
	
	/* Setup middle link, which is between the two. */
	midLink = (void*)&frontLink->block[0];
	midLink->prev = frontLink;
	frontLink->next = midLink;
	midLink->next = backLink;
	backLink->prev = midLink;
	
	/* Determine size of the middle link, which is free space. */
	midLink->blockSize = (sjme_jint)((uintptr_t)backLink -
		(uintptr_t)&midLink->block[0]);
		
	/* The front and back links are in the "invalid" space. */
	frontLink->space = SJME_NUM_ALLOC_POOL_SPACE;
	backLink->space = SJME_NUM_ALLOC_POOL_SPACE;
	
	/* Determine size that can and cannot be used. */
	result->space[SJME_ALLOC_POOL_SPACE_FREE].reserved =
		(SJME_SIZEOF_ALLOC_LINK(0) * 3);
	result->space[SJME_ALLOC_POOL_SPACE_FREE].usable = midLink->blockSize;
	
	/* Link in the first and last actual blocks for the free chain. */
	result->freeFirstLink = frontLink;
	frontLink->freeNext = midLink;
	midLink->freePrev = frontLink;
	result->freeLastLink = backLink;
	backLink->freePrev = midLink;
	midLink->freeNext = backLink;
	
#if defined(SJME_CONFIG_HAS_VALGRIND)
	/* Reserve front side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(baseAddr,
		((uintptr_t)&midLink->block[0] - (uintptr_t)baseAddr));
		
	/* Reserve back side in Valgrind. */
	VALGRIND_MAKE_MEM_NOACCESS(backLink,
		(SJME_SIZEOF_ALLOC_LINK(0)));
#endif
	
	/* Use the pool. */
	*outPool = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositiveNonZero sjme_jint size,
	sjme_attrOutNotNull void** outAddr)
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
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Debug. */
	sjme_message("Found link at %p: %d bytes, we need %d with split %d.",
		scanLink, (int)scanLink->blockSize, (int)roundSize, (int)splitBlock);

	/* Does this block need to be split? */
	if (splitBlock)
	{
		/* Make it so this block can actually fit in here. */
		rightLink = (sjme_alloc_link*)&scanLink->block[roundSize];

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

	/* Use the given link. */
	*outAddr = &scanLink->block[0];
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_allocFree(
	sjme_attrInNotNull void* addr)
{
	if (addr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_allocLink(
	sjme_attrInNotNull void* addr,
	sjme_attrOutNotNull sjme_alloc_link** outLink)
{
	if (addr == NULL || outLink == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just need to do some reversing math. */
	*outLink = (sjme_alloc_link*)(((uintptr_t)addr) -
		offsetof(sjme_alloc_link, block));
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_allocRealloc(
	sjme_attrInOutNotNull void** inOutAddr,
	sjme_attrInPositive sjme_jint newSize)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
