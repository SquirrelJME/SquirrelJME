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
	sjme_alloc_link* link;

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

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
