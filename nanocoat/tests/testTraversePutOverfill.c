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
 * Tests filling the tree past the maximum.
 *  
 * @since 2024/09/02 
 */
SJME_TEST_DECLARE(testTraversePutOverfill)
{
	sjme_traverse_test_data traverse;
	test_data value;
	sjme_jint i;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* Add in many values. */
	for (i = 0; i < MAX_ELEMENTS * 2; i++)
	{
		memset(&value, 0, sizeof(value));
		value.a = i;
		value.b = i * 10;
		test->error = sjme_traverse_put(traverse,
			&value, i, 32, test_data, 0);
		
		if (i < MAX_ELEMENTS)
			sjme_unit_equalI(test, test->error, SJME_ERROR_NONE,
				"Adding %d failed?", i);
		else
			sjme_unit_equalI(test, test->error, SJME_ERROR_CAPACITY_EXCEEDED,
				"Adding %d succeeded?", i);
	}
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
