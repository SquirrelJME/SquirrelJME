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
 * Tests removal of something from an empty traversal tree.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraverseRemoveEmpty)
{
	sjme_traverse_test_data traverse;
	sjme_jint i;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* Constantly try to remove. */
	for (i = 0; i <= 32; i++)
	{
		test->error = sjme_traverse_remove(
			SJME_AS_TRAVERSE(traverse),
			i, i);
		sjme_unit_equalI(test, test->error, SJME_ERROR_NO_SUCH_ELEMENT,
			"Element was found in the tree?");
	}
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
