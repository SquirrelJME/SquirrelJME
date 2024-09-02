/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"

#include "sjme/traverse.h"
#include "testTraverse.h"

/**
 * Tests iteration.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraverseIterate)
{
	sjme_traverse_test_data traverse;
	sjme_traverse_iterator iterator;
	test_data value;
	sjme_jint bit, maxBit, jump, i;
	test_data* leafValue;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* Store first value, on zero branch. */
	memset(&value, 0, sizeof(value));
	value.a = 1;
	value.b = 10;
	if (sjme_error_is(test->error = sjme_traverse_put(traverse,
		&value, 0, 1, test_data, 0)))
		return sjme_unit_fail(test, "Could not put in first value?");
	
	/* Store second value, on one+one branch. */
	memset(&value, 0, sizeof(value));
	value.a = 2;
	value.b = 20;
	if (sjme_error_is(test->error = sjme_traverse_put(traverse,
		&value, 3, 2, test_data, 0)))
		return sjme_unit_fail(test, "Could not put in second value?");
	
	/* Iterate through sequences. */
	for (maxBit = 1; maxBit <= 2; maxBit++)
		for (bit = 0; bit <= 1; bit++)
		{
			/* Start iterating. */
			memset(&iterator, 0x7F, sizeof(iterator));
			if (sjme_error_is(test->error = sjme_traverse_iterate(
				SJME_AS_TRAVERSE(traverse), &iterator)))
				return sjme_unit_fail(test, "Could not start iteration?");
			
			/* Should be at root node. */
			sjme_unit_equalP(test, traverse->root, iterator.atNode,
				"Iterator not at root node?");
			
			/* Which value to jump to? */
			jump = 0;
			for (i = 0; i < maxBit; i++)
				jump |= (bit << i);
			
			/* Iterate single value. */
			leafValue = NULL;
			test->error = sjme_traverse_iterateNext(traverse,
				&iterator, &leafValue, jump, maxBit, test_data, 0);
			
			/* Single bit to zero. */
			if (maxBit == 1 && bit == 0)
			{
				/* Should be the leftmost leaf. */
				sjme_unit_notEqualP(test, leafValue, NULL,
					"Leaf value was null?");
				sjme_unit_equalP(test,
					iterator.atNode, 
					traverse->root->data.node.zero,
					"At wrong node?");
				sjme_unit_equalP(test,
					leafValue,
					&traverse->root->data.node.zero->data.data,
					"Value pointing to wrong location?");
			}
			
			/* Single bit to one. */
			else if (maxBit == 1 && bit == 1)
			{
				sjme_unit_equalP(test, leafValue, NULL,
					"Leaf value was not null?");
				sjme_unit_equalP(test,
					iterator.atNode, 
					traverse->root->data.node.one,
					"At wrong node?");
			}
				
			/* Double bit to zero. */
			else if (maxBit == 2 && bit == 0)	
			{
				/* Fails because nothing is actually here. */
				sjme_unit_equalI(test, test->error, SJME_ERROR_NO_SUCH_ELEMENT,
					"Zero+zero did not fail?");
			}
			
			/* Double bit to one. */
			else if (maxBit == 2 && bit == 1)
			{
				/* Should be the rightmost leaf. */
				sjme_unit_notEqualP(test, leafValue, NULL,
					"Leaf value was null?");
				sjme_unit_equalP(test,
					iterator.atNode, 
					traverse->root->data.node.one->data.node.one,
					"At wrong node?");
				sjme_unit_equalP(test,
					leafValue,
					&traverse->root->data.node.one->data.node.one->data.data,
					"Value pointing to wrong location?");
			}
			
			/* Skip if this was an error case. */
			if (sjme_error_is(test->error))
				continue;
			
			/* Should be the bit value we placed. */
			sjme_unit_equalI(test, jump, iterator.bits,
				"Wrong bit value?");
			sjme_unit_equalI(test, maxBit, iterator.bitCount,
				"Wrong bit count?");
		}
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
