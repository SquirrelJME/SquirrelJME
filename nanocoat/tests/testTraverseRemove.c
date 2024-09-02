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
 * Tests removal of items from the traversal tree.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraverseRemove)
{
	sjme_traverse_test_data traverse;
	test_data value;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* Store first value. */
	memset(&value, 0, sizeof(value));
	value.a = 1;
	value.b = 10;
	if (sjme_error_is(test->error = sjme_traverse_put(traverse,
		&value, 0, 1, test_data, 0)))
		return sjme_unit_fail(test, "Could not put in first value?");
		
	/* Store second value. */
	memset(&value, 0, sizeof(value));
	value.a = 2;
	value.b = 20;
	if (sjme_error_is(test->error = sjme_traverse_put(traverse,
		&value, 1, 1, test_data, 0)))
		return sjme_unit_fail(test, "Could not put in second value?");
	
	/* Remove first value. */
	if (sjme_error_is(test->error = sjme_traverse_remove(
		SJME_AS_TRAVERSE(traverse), 0, 1)))
		return sjme_unit_fail(test, "Could not remove first value?");
		
	/* There should be a root and one facing leaf. */
	/* The zero node should be gone. */
	sjme_unit_notEqualP(test, NULL, traverse->root,
		"Root node missing?");
	sjme_unit_equalI(test, SJME_TRAVERSE_NODE, traverse->root->type,
		"Root node not a node?");
	sjme_unit_equalP(test, NULL, traverse->root->data.node.zero,
		"There is a zero node?");
	sjme_unit_notEqualP(test, NULL, traverse->root->data.node.one,
		"There is no one node?");
	sjme_unit_equalI(test,
		SJME_TRAVERSE_LEAF, traverse->root->data.node.one->type,
		"One facing is not a leaf?");
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
