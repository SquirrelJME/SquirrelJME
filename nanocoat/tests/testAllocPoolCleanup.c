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
#include "test.h"
#include "unit.h"

/**
 * Tests cleanup of the allocation pool.
 * 
 * @since 2023/11/29
 */
SJME_TEST_DECLARE(testAllocPoolCleanup)
{
	void* chunk;
	sjme_jint chunkLen;
	uint8_t* block;
	sjme_alloc_pool* pool;
	sjme_alloc_link* link;
	
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
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
