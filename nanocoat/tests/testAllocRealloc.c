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

/**
 * Tests reallocation of block data, with potential resize accordingly if
 * applicable.
 * 
 * @since 2023/11/28
 */
SJME_TEST_DECLARE(testAllocRealloc)
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
	if (SJME_IS_ERROR(sjme_alloc_poolInitStatic(&pool,
		chunk, chunkLen)) || pool == NULL)
		return sjme_unitFail(test, "Could not initialize static pool?");

	sjme_todo("Implement this?");
	return SJME_TEST_RESULT_FAIL;
}
