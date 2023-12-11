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

#define TEST_BLOCK_SIZE 64

/**
 * Tests subsequent splitting of blocks when allocating.
 *
 * @since 2023/12/10
 */
SJME_TEST_DECLARE(testAllocSplit)
{
	void* chunk;
	sjme_jint chunkLen;
	sjme_alloc_pool* pool;
	void* block;
	sjme_alloc_link* initLink;
	sjme_alloc_link* link;
	sjme_alloc_link* next;
	sjme_alloc_link* prev;
	sjme_jint oldInitLinkBlockSize, oldInitTotal, i, newTotal;

	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unitSkip(test, "Could not alloca(%d).",
			(int)chunkLen);

	/* Initialize the pool. */
	pool = NULL;
	if (SJME_IS_ERROR(sjme_alloc_poolStatic(&pool, chunk,
			chunkLen)) || pool == NULL)
		return sjme_unitFail(test, "Could not initialize static pool?");

	/* There should be a front and back link. */
	sjme_unitNotEqualP(test, pool->frontLink, NULL,
		"There is no front link?");
	sjme_unitNotEqualP(test, pool->backLink, NULL,
		"There is no back link?");

	/* There should be a next to the front link, and it should not be the */
	/* back link. Vice versa as well... */
	sjme_unitNotEqualP(test, pool->frontLink->next, NULL,
		"There is no next after the front link?");
	sjme_unitNotEqualP(test, pool->frontLink->next, pool->backLink,
		"Front link next is the back link?");
	sjme_unitNotEqualP(test, pool->backLink->prev, NULL,
		"There is no prev before the back link?");
	sjme_unitNotEqualP(test, pool->backLink->prev, pool->frontLink,
		"Back link prev is the front link?");

	/* The front and back should point to the same link. */
	sjme_unitEqualP(test, pool->frontLink->next, pool->backLink->prev,
		"Different link in the middle?");

	/* Get the main starting link. */
	initLink = pool->frontLink->next;
	oldInitLinkBlockSize = initLink->blockSize;

	/* Determine the old initial total space. */
	oldInitTotal = 0;
	for (i = 0; i < SJME_NUM_ALLOC_POOL_SPACE; i++)
	{
		oldInitTotal += pool->space[i].usable;
		oldInitTotal += pool->space[i].reserved;
	}

	/* Allocate some memory in the pool. */
	block = NULL;
	if (SJME_IS_ERROR(sjme_alloc(pool, TEST_BLOCK_SIZE,
			&block)) || block == NULL)
		return sjme_unitFail(test, "Could not allocate %d bytes.",
			TEST_BLOCK_SIZE);

	/* Obtain the block link. */
	link = NULL;
	if (SJME_IS_ERROR(sjme_allocLink(block, &link)) ||
		link == NULL)
		return sjme_unitFail(test, "Could not obtain block link?");

	/* The initial link should be this one. */
	sjme_unitEqualP(test, link, initLink,
		"Used different link from the first?");

	/* The two links should be linked together. */
	sjme_unitEqualP(test, link, link->next->prev,
		"Link not linked back?");

	/* The back link's prev should be the new link's next. */
	sjme_unitEqualP(test, link->next, pool->backLink->prev,
		"Back link is not link's next?");
	sjme_unitEqualP(test, link->next->next, pool->backLink,
		"Link's next is not the back link?");

	/* The sizes of both links should be equal. */
	next = link->next;
	sjme_unitEqualI(test, oldInitLinkBlockSize,
		link->blockSize + next->blockSize + SJME_SIZEOF_ALLOC_LINK(0),
		"Block sizes do not add up?");

	/* The next link should be free. */
	sjme_unitEqualI(test, next->space, SJME_ALLOC_POOL_SPACE_FREE,
		"Next link is not in the free space pool?");

	/* Determine the new total space. */
	newTotal = 0;
	for (i = 0; i < SJME_NUM_ALLOC_POOL_SPACE; i++)
	{
		newTotal += pool->space[i].usable;
		newTotal += pool->space[i].reserved;
	}

	/* These total sizes should add up the same. */
	sjme_unitEqualI(test, oldInitTotal, newTotal,
		"Total sizes are different?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
