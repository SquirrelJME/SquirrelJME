/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "proto.h"
#include "sjme/alloc.h"
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

/**
 * Tests random allocations and frees.
 * 
 * @since 2023/11/29
 */
SJME_TEST_DECLARE(testAllocRandom)
{
	void* chunk;
	jint chunkLen;
	uint8_t* block;
	sjme_alloc_pool* pool;
	sjme_alloc_link* link;
	sjme_random random;
	
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
	
	/* Setup PRNG. */
	memset(&random, 0, sizeof(random));
	if (!sjme_randomInit(&random, 0x12345678,
		0x7ABCDEF0))
		return sjme_unitFail(test, "Could not initialize randomness.");
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
