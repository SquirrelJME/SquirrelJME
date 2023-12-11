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

/** The number of links to allocate. */
#define NUM_LINKS 3

/** The scenarios available. */
typedef enum testScenario
{
	/** Left to right. */
	SCENARIO_LMR,

	/** Right to left. */
	SCENARIO_RML,

	/** Middle, then left to right. */
	SCENARIO_MLR,

	/** Middle, then right to left. */
	SCENARIO_MRL,

	/** The number of scenarios. */
	NUM_SCENARIO
} testScenario;

/**
 * Tests merging of allocation blocks when freeing them accordingly.
 * 
 * @since 2023/11/25
 */
SJME_TEST_DECLARE(testAllocFreeMerge)
{
	void* chunk;
	sjme_jboolean isLast;
	sjme_jint chunkLen, j, scenario;
	uint8_t* block;
	sjme_alloc_pool* pool;
	void* blocks[NUM_LINKS];
	sjme_alloc_link* links[NUM_LINKS];
	
	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unitSkip(test, "Could not alloca(%d).",
			(int)chunkLen);

	/* Use multiple scenarios regarding the order of blocks to free. */
	for (scenario = 0; scenario < NUM_SCENARIO; scenario++)
	{
		/* Initialize the pool. */
		pool = NULL;
		if (SJME_IS_ERROR(sjme_alloc_poolInitStatic(&pool,
			chunk, chunkLen)) || pool == NULL)
			return sjme_unitFail(test, "Could not initialize static pool?");

		/* Allocate each of the links. */
		for (j = 0; j < NUM_LINKS; j++)
		{
			/* Is this the last link? */
			isLast = j >= (NUM_LINKS - 1);

			/* Debug. */
			sjme_message("Link %d of %d (last? %d)",
				(j + 1), NUM_LINKS, isLast);

			/* Allocate block. */
			blocks[j] = NULL;
			if (SJME_IS_ERROR(sjme_alloc(pool,
				pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable /
					(isLast ? 1 : 2),
				&blocks[j])) || blocks[j] == NULL)
				return sjme_unitFail(test, "Could not allocate link?");

			/* Get the link. */
			links[j] = NULL;
			if (SJME_IS_ERROR(sjme_allocLink(blocks[j],
				&links[j])) || links[j] == NULL)
				return sjme_unitFail(test, "Could not get link?");

			/* The last block should claim all the free space. */
			if (isLast)
				sjme_unitEqualI(test,
					0, pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable,
					"All of the free space was not taken?");
		}

		sjme_todo("Implement %s", __func__);
		return SJME_TEST_RESULT_FAIL;
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
