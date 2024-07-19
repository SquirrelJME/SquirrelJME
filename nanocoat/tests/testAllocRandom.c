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

/** The number of random allocations to make. */
#define NUM_RANDOM 512

/** Basic allocation link. */
typedef struct testLink testLink;

struct testLink
{
	/** The previous link. */
	testLink* prev;
	
	/** The next link. */
	testLink* next;
};

/**
 * Tests random allocations and frees.
 * 
 * @since 2023/11/29
 */
SJME_TEST_DECLARE(testAllocRandom)
{
	sjme_pointer chunk;
	sjme_jint chunkLen, i, linkLen, desire;
	sjme_alloc_pool* pool;
	sjme_random random;
	testLink* link;
	testLink* lastLink;
	testLink* oldPrev;
	testLink* oldNext;
	
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
	
	/* Initialize the PRNG. */
	memset(&random, 0, sizeof(random));
	if (!sjme_randomInit(&random, 12345, 67890))
		return sjme_unit_fail(test, "Could not initialize PRNG?");
	
	/* Perform many small allocations. */
	lastLink = NULL;
	for (i = 0; i < NUM_RANDOM; i++)
	{
		/* Determine size to allocate. */
		linkLen = 0;
		if (!sjme_randomNextIntMax(&random, &linkLen, 32))
			return sjme_unit_fail(test, "Could not random size %d %d.",
				(int)i, (int)linkLen);
		linkLen += sizeof(linkLen); 
		
		/* Allocate link. */
		link = NULL;
		if (sjme_error_is(sjme_alloc(pool, linkLen, &link)))
			return sjme_unit_fail(test, "Could not allocate link %d %d.",
				(int)i, (int)linkLen);
		
		/* Link in. */
		if (lastLink == NULL)
			lastLink = link;
		else
		{
			/* Connect the two. */
			link->prev = lastLink;
			lastLink->next = link;
			
			/* Replace the old link. */
			lastLink = link;
		}
	}
	
	/* Reallocate all links randomly. */
	for (i = 1; i < NUM_RANDOM; i++)
	{
		/* Which link do we want to clear? */
		desire = -1;
		if (!sjme_randomNextIntMax(&random, &desire,
				NUM_RANDOM))
			return sjme_unit_fail(test, "Could not desire %d.",
				(int)i);
		
		/* Always bump up by one to skip the last link. */
		desire++;
		
		/* Find the target link to free. */
		link = NULL;
		do
		{
			/* Always pivot onto the last link. */
			if (link == NULL)
				link = lastLink->prev;
			
			/* Link left. */
			link = link->prev;
			
			/* Reduce count one. */
			desire--;
		} while (desire > 0);
		
		/* Always pivot onto the last link, again... */
		if (link == NULL)
			link = lastLink->prev;
		
		/* Unlink. */
		oldPrev = link->prev;
		oldNext = link->next;
		
		/* Determine size to allocate. */
		linkLen = 0;
		if (!sjme_randomNextIntMax(&random, &linkLen, 32))
			return sjme_unit_fail(test, "Could not random size %d %d.",
				(int)i, (int)linkLen);
		linkLen += sizeof(linkLen); 
		
		/* Free it. */
		if (sjme_error_is(sjme_alloc_realloc((sjme_pointer*)&link,
			linkLen)))
			return sjme_unit_fail(test, "Could not realloc link %d at %p.",
				(int)i, link);
		
		/* Re-link in. */
		if (oldPrev != NULL)
			oldPrev->next = link;
		if (oldNext != NULL)
			oldNext->prev = link;
	}
	
	/* Finished. */
	return SJME_TEST_RESULT_PASS;
}
