/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "test.h"
#include "unit.h"
#include "sjme/alloc.h"

#define TEST_BLOCK_SIZE 1033

/**
 * Tests allocation without performing a free.
 *
 * @since 2023/12/10
 */
SJME_TEST_DECLARE(testAllocNoFree)
{
	sjme_pointer chunk;
	sjme_jint chunkLen;
	sjme_alloc_pool* pool;
	sjme_pointer block;
	sjme_alloc_link* link;

	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = sjme_alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unit_skip(test, "Could not alloca(%d).",
			(int)chunkLen);

	/* Initialize the pool. */
	pool = NULL;
	if (sjme_error_is(sjme_alloc_poolInitStatic(&pool, chunk,
			chunkLen)) || pool == NULL)
		return sjme_unit_fail(test, "Could not initialize static pool?");

	/* Allocate some memory in the pool. */
	block = NULL;
	if (sjme_error_is(sjme_alloc(pool, TEST_BLOCK_SIZE,
			&block)) || block == NULL)
		return sjme_unit_fail(test, "Could not allocate %d bytes.",
			TEST_BLOCK_SIZE);

	/* Obtain the block link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(block, &link)) ||
		link == NULL)
		return sjme_unit_fail(test, "Could not obtain block link?");

	/* The allocation size should be of the requested size. */
	sjme_unit_equalI(test, link->allocSize, TEST_BLOCK_SIZE,
		"Allocation size is different?");

	/* The allocation size should be the same or lower always. */
	sjme_unit_lessEqualI(test, link->allocSize, link->blockSize,
		"Allocation size bigger than block size?");

	/* Should always be rounded! */
	sjme_unit_equalI(test, (link->blockSize & 7), 0,
		"Block size not divisible by 8?");

	/* The edge of the block should be the right side. */
	sjme_unit_equalP(test, &link->block[link->blockSize], link->next,
		"Block to the right not at the edge of this one?");

	/* The left edge should be the same as well. */
	sjme_unit_equalP(test, &link->prev->block[link->prev->blockSize], link,
		"This block on at the edge of the left side block?");

	/* There should be no free prev and next. */
	sjme_unit_equalP(test, link->freeNext, NULL, "Free next not cleared?");
	sjme_unit_equalP(test, link->freePrev, NULL, "Free prev not cleared?");

	/* Link should be marked used. */
	sjme_unit_equalI(test, link->space, SJME_ALLOC_POOL_SPACE_USED,
		"Pool space not marked as used?");

	/* The pointers should be equal. */
	sjme_unit_equalP(test, block, &link->block[0], "Wrong block pointers?");

	/* Next link should be the backlink's previous. */
	sjme_unit_equalP(test, link->next, pool->backLink->prev,
		"Back link previous is not the next free block?");

	/* Success. */
	return SJME_TEST_RESULT_PASS;
}
