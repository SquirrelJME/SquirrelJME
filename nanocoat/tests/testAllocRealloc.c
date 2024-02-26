/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "sjme/alloc.h"
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

/** Small size block. */
#define SMALL_SIZE 512

/** Medium size block. */
#define MEDIUM_SIZE 1024

/** Large size block. */
#define LARGE_SIZE 2048

/**
 * Tests reallocation of block data, with potential resize accordingly if
 * applicable.
 * 
 * @since 2023/11/28
 */
SJME_TEST_DECLARE(testAllocRealloc)
{
	void* chunk;
	sjme_jint chunkLen, j, newLen;
	sjme_alloc_pool* pool;
	void* block;
	void* oldBlockP;
	sjme_alloc_link* link;
	sjme_alloc_link* oldNext;
	
	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = sjme_alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unit_skip(test, "Could not alloca(%d).",
			(int)chunkLen);
	
	/* Initialize the pool. */
	pool = NULL;
	if (sjme_error_is(sjme_alloc_poolInitStatic(&pool,
		chunk, chunkLen)) || pool == NULL)
		return sjme_unit_fail(test, "Could not initialize static pool?");

	/* Start with a medium-sized block. */
	block = NULL;
	if (sjme_error_is(sjme_alloc(pool, MEDIUM_SIZE, &block)) ||
		block == NULL)
		return sjme_unit_fail(test, "Could not allocate medium block?");

	/* Get the link of this block. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(block, &link)) ||
		link == NULL)
		return sjme_unit_fail(test, "Could not get link of block?");

	/* Used for both the small and larger allocation. */
	for (j = 0; j < 2; j++)
	{
		/* Which size are we allocating to? */
		newLen = (j == 0 ? SMALL_SIZE : LARGE_SIZE);

		/* Get the next link. */
		oldNext = link->next;

		/* Reallocate to the small size. */
		oldBlockP = block;
		if (sjme_error_is(sjme_alloc_realloc(&block,
			newLen)))
			return sjme_unit_fail(test, "Could not reallocate link?");

		/* In this scenario the pointer should not have moved at all. */
		sjme_unit_equalP(test, block, oldBlockP,
			"Reallocation moved the block?");

		/* The allocation size should be the new size. */
		sjme_unit_equalI(test, link->allocSize, newLen,
			"Allocation size unchanged or incorrect?");

		/* The next link should have changed position. */
		sjme_unit_notEqualP(test, link->next, oldNext,
			"Next link did not move?");
		sjme_unit_equalP(test, link->next, &link->block[link->blockSize],
			"Next link did not shift over?");

		/* The next's next's link should still be the back link, as */
		/* it should have been merged. */
		sjme_unit_equalP(test, link->next->next, pool->backLink,
			"Next next is not the back link?");
		sjme_unit_equalP(test, link->next, pool->backLink->prev,
			"Backlink previous is not the next block?");
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
