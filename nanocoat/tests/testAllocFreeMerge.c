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

/** The number of which sequences. */
#define NUM_WHICH (NUM_LINKS + 3)

/** Identifier for the front link. */
#define FRONT (NUM_LINKS + 1)

/** Identifier for the back link. */
#define BACK (NUM_LINKS + 2)

/** Identifier of the end of link list. */
#define END NUM_LINKS

/** Free bit. */
#define FREE 64

/** The mask for removing free. */
#define FREE_MASK 63

/** The scenarios available. */
typedef enum testScenarioId
{
	/** Left to right. */
	SCENARIO_LMR,

	/** Left, Right, then middle. */
	SCENARIO_LRM,

	/** Middle, then left to right. */
	SCENARIO_MLR,

	/** Middle, then right to left. */
	SCENARIO_MRL,

	/** Right to left. */
	SCENARIO_RML,

	/** Right, left, then middle. */
	SCENARIO_RLM,

	/** The number of scenarios. */
	NUM_SCENARIO
} testScenarioId;

/** The sequence of links. */
typedef struct testSequenceType
{
	/** The number of used links. */
	sjme_jint numUsed;

	/** The number of free links. */
	sjme_jint numFree;

	/** Which links are on the entire chain? */
	sjme_jint which[NUM_WHICH];
} testSequenceType;

/** The order of the links. */
typedef struct testOrderType
{
	/** The order used. */
	sjme_jint order[NUM_LINKS];

	/** How many there should be after being freed. */
	testSequenceType sequence[NUM_LINKS];
} testOrderType;

/** The actual order to use. */
const testOrderType testLinkOrder[NUM_SCENARIO] =
{
	{
		{0, 1, 2},
		{
			{2, 1,
				{FRONT, 0 | FREE, 1, 2, BACK,
					END}},
			{1, 1,
				{FRONT, 0 | FREE, 2, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	},
	{
		{0, 2, 1},
		{
			{2, 1,
				{FRONT, 0 | FREE, 1, 2, BACK,
					END}},
			{1, 2,
				{FRONT, 0 | FREE, 1, 2 | FREE, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	},
	{
		{1, 0, 2},
		{
			{2, 1,
				{FRONT, 0, 1 | FREE, 2, BACK,
					END}},
			{1, 1,
				{FRONT, 0 | FREE, 2, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	},
	{
		{1, 2, 0},
		{
			{2, 1,
				{FRONT, 0, 1 | FREE, 2, BACK,
					END}},
			{1, 1,
				{FRONT, 0, 1 | FREE, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	},
	{
		{2, 1, 0},
		{
			{2, 1,
				{FRONT, 0, 1, 2 | FREE, BACK,
					END}},
			{1, 1,
				{FRONT, 0, 1 | FREE, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	},
	{
		{2, 0, 1},
		{
			{2, 1,
				{FRONT, 0, 1, 2 | FREE, BACK,
					END}},
			{1, 2,
				{FRONT, 0 | FREE, 1, 2 | FREE, BACK,
					END}},
			{0, 1,
				{FRONT, 0 | FREE, BACK,
					END}}
		}
	}
};

/**
 * Tests merging of allocation blocks when freeing them accordingly.
 * 
 * @since 2023/11/25
 */
SJME_TEST_DECLARE(testAllocFreeMerge)
{
	void* chunk;
	sjme_jboolean isLast, isFree, wantFree;
	sjme_jint chunkLen, linkNum, scenario, numUsed, numFree, x, atId;
	uint8_t* block;
	sjme_alloc_link* link;
	sjme_alloc_pool* pool;
	sjme_alloc_link* rover;
	sjme_alloc_link* at;
	void* blocks[NUM_LINKS];
	sjme_alloc_link* links[NUM_WHICH];
	const testOrderType* order;
	const testSequenceType* sequence;
	
	/* Allocate data on the stack so it gets cleared. */
	chunkLen = 32768;
	chunk = alloca(chunkLen);
	if (chunk == NULL)
		return sjme_unitSkip(test, "Could not alloca(%d).",
			(int)chunkLen);

	/* Use multiple scenarios regarding the order of blocks to free. */
	for (scenario = 0; scenario < NUM_SCENARIO; scenario++)
	{
		/* Get the order data. */
		order = &testLinkOrder[scenario];

		/* Initialize the pool. */
		pool = NULL;
		if (SJME_IS_ERROR(sjme_alloc_poolInitStatic(&pool,
			chunk, chunkLen)) || pool == NULL)
			return sjme_unitFail(test, "Could not initialize static pool?");

		/* Allocate each of the links. */
		for (linkNum = 0; linkNum < NUM_LINKS; linkNum++)
		{
			/* Is this the last link? */
			isLast = linkNum >= (NUM_LINKS - 1);

			/* Debug. */
			sjme_message("Link %d of %d (last? %d)",
				(linkNum + 1), NUM_LINKS, isLast);

			/* Allocate block. */
			blocks[linkNum] = NULL;
			if (SJME_IS_ERROR(sjme_alloc(pool,
				pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable /
					(isLast ? 1 : 2),
				&blocks[linkNum])) || blocks[linkNum] == NULL)
				return sjme_unitFail(test, "Could not allocate link?");

			/* Get the link. */
			links[linkNum] = NULL;
			if (SJME_IS_ERROR(sjme_allocLink(blocks[linkNum],
				&links[linkNum])) || links[linkNum] == NULL)
				return sjme_unitFail(test, "Could not get link?");

			/* The last block should claim all the free space. */
			if (isLast)
				sjme_unitEqualI(test,
					0, pool->space[SJME_ALLOC_POOL_SPACE_FREE].usable,
					"All of the free space was not taken?");
		}

		/* Seed front and back links which are constant. */
		links[FRONT] = pool->frontLink;
		links[BACK] = pool->backLink;

		/* Free the links in the specified order and test the result. */
		for (linkNum = 0; linkNum < NUM_LINKS; linkNum++)
		{
			/* Get the sequence from the order. */
			sequence = &order->sequence[linkNum];

			/* Which link is this? */
			block = blocks[order->order[linkNum]];
			link = links[order->order[linkNum]];

			/* Free the link. */
			if (SJME_IS_ERROR(sjme_allocFree(block)))
				return sjme_unitFail(test, "Could not free link.");

			/* Go through the entire chain. */
			numUsed = numFree = 0;
			rover = pool->frontLink;
			for (x = 0; x < NUM_WHICH; x++)
			{
				/* End of list? Stop. */
				if (sequence->which[x] == END)
					break;

				/* Get the link this is. */
				atId = x & FREE_MASK;
				at = links[sequence->which[atId]];

				/* Must be this one. */
				sjme_unitEqualP(test, rover, at,
					"Incorrect sequence %d.%d.%d?", scenario, linkNum, x);

				/* Is the block free or not? */
				if (atId != FRONT && atId != BACK)
				{
					/* Is this block free? */
					isFree = (rover->space == SJME_ALLOC_POOL_SPACE_FREE);

					/* The freeness should match. */
					wantFree = !!(sequence->which[atId] & FREE);
					sjme_unitEqualI(test,
						isFree, wantFree,
						"Incorrect freeness %d.%d.%d?", scenario, linkNum, x);

					/* Count up. */
					if (isFree)
						numFree++;
					else
						numUsed++;
				}

				/* Go the next link. */
				rover = rover->next;
			}

			/* Free and used counts should match. */
			sjme_unitEqualI(test, numFree, sequence->numFree,
				"Free match incorrect %d.%d?", scenario, linkNum);
			sjme_unitEqualI(test, numUsed, sequence->numUsed,
				"Free match incorrect %d.%d?", scenario, linkNum);
		}
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
