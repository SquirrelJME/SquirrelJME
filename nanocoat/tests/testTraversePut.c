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
 * Tests putting values into the iteration.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraversePut)
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
	
	/* There should be a root and zero facing leaf. */
	sjme_unit_notEqualP(test, NULL, traverse->root,
		"Root node missing?");
	sjme_unit_notEqualP(test,
		(void*)SJME_TRAVERSE_LEAF_KEY, (void*)traverse->root->leaf.key,
		"Root node not a node?");
	sjme_unit_notEqualP(test, NULL, traverse->root->node.zero,
		"There is no zero node?");
	sjme_unit_equalP(test, NULL, traverse->root->node.one,
		"There is a one node?");
	sjme_unit_equalP(test,
		(void*)SJME_TRAVERSE_LEAF_KEY,
		(void*)traverse->root->node.zero->leaf.key,
		"Zero facing is not a leaf?");
	sjme_unit_equalI(test,
		0, memcmp(&value,
			&traverse->root->node.zero->leaf.value[0],
			sizeof(test_data)),
		"Incorrect value?");
	
	/* Store second value. */
	memset(&value, 0, sizeof(value));
	value.a = 2;
	value.b = 20;
	if (sjme_error_is(test->error = sjme_traverse_put(traverse,
		&value, 1, 1, test_data, 0)))
		return sjme_unit_fail(test, "Could not put in second value?");
	
	/* There should be a root and one facing leaf. */
	sjme_unit_notEqualP(test, NULL, traverse->root,
		"Root node missing?");
	sjme_unit_notEqualP(test,
		(void*)SJME_TRAVERSE_LEAF_KEY, (void*)traverse->root->leaf.key,
		"Root node not a node?");
	sjme_unit_notEqualP(test, NULL, traverse->root->node.zero,
		"There is no zero node?");
	sjme_unit_notEqualP(test, NULL, traverse->root->node.one,
		"There is no one node?");
	sjme_unit_equalP(test,
		(void*)SJME_TRAVERSE_LEAF_KEY,
		(void*)traverse->root->node.one->leaf.key,
		"One facing is not a leaf?");
	sjme_unit_equalI(test,
		0, memcmp(&value,
			&traverse->root->node.one->leaf.value[0],
			sizeof(test_data)),
		"Incorrect value?");
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
