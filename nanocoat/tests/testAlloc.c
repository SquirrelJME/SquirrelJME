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

#define TEST_BLOCK_SIZE 1024

sjme_testResult testAlloc(sjme_test* test)
{
	void* chunk;
	jint chunkLen;
	sjme_alloc_pool* pool;
	void* block;
	void* newBlock;
	sjme_alloc_link* link;
	
	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unitSkip(test, "Could not alloca(%d).",
			(int)chunkLen);
	
	/* Initialize the pool. */
	pool = NULL;
	if (!sjme_alloc_poolStatic(&pool, chunk, chunkLen) ||
		pool == NULL)
		return sjme_unitFail(test, "Could not initialize static pool?");
	
	/* Allocate some memory in the pool. */
	block = NULL;
	if (!sjme_alloc(pool, TEST_BLOCK_SIZE, &block) ||
		block == NULL)
		return sjme_unitFail(test, "Could not allocate %d bytes.",
			TEST_BLOCK_SIZE);
	
	/* Obtain the block link. */
	link = NULL;
	if (!sjme_allocLink(block, &link) || link == NULL)
		return sjme_unitFail(test, "Could not obtain block link?");
	
	/* There should be no free prev and next. */
	sjme_unitEqualP(test, link->freeNext, NULL, "Free next not cleared?");
	sjme_unitEqualP(test, link->freePrev, NULL, "Free prev not cleared?");
		
	/* Link should be marked used. */
	sjme_unitEqualI(test, link->space, SJME_ALLOC_POOL_SPACE_USED,
		"Pool space not marked as used?");
	
	/* The pointers should be equal. */
	sjme_unitEqualP(test, block, &link->block[0], "Wrong block pointers?");
	
	/* Next link should be the backlink's previous. */
	sjme_unitEqualP(test, link->next, pool->backLink->prev,
		"Back link previous is not the next free block?");
	
	/* Free the block. */
	if (!sjme_allocFree(block))
		return sjme_unitFail(test, "Could not free block.");
		
	/* Link should be marked free. */
	sjme_unitEqualI(test, link->space, SJME_ALLOC_POOL_SPACE_FREE,
		"Pool space not marked as free?");
	
	/* There should be a previous and next free link. */
	sjme_unitNotEqualP(test, link->freeNext, NULL, "Free next not reset?");
	sjme_unitNotEqualP(test, link->freePrev, NULL, "Free prev not reset?");
	
	/* Next block should be the tail, for merging back in. */
	sjme_unitEqualP(test, link->next, pool->backLink,
		"Block not pointing to the back link?");
	
	/* Success. */
	return SJME_TEST_RESULT_PASS;
}
