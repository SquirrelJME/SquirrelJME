/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"

#include "sjme/traverse.h"
#include "testTraverse.h"

/**
 * Tests new and destroy of a traversal tree.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraverseNewDestroy)
{
	sjme_traverse_test_data traverse;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* There should be no root node. */
	sjme_unit_equalP(test, NULL, traverse->root,
		"There was a root node on a fresh tree?");
	
	/* There should be storage, however. */
	sjme_unit_notEqualP(test, NULL, traverse->storage,
		"Storage is missing?");
	sjme_unit_equalP(test, traverse->storage->start, traverse->storage->next,
		"Next storage node not at start?");
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
