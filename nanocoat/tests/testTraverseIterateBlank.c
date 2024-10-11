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
 * Tests iterating a blank traverse.
 *  
 * @since 2024/09/01 
 */
SJME_TEST_DECLARE(testTraverseIterateBlank)
{
	sjme_traverse_test_data traverse;
	sjme_traverse_iterator iterator;
	
	/* Setup traverse. */
	traverse = NULL;
	if (sjme_error_is(test->error = sjme_traverse_new(test->pool,
		&traverse, MAX_ELEMENTS, test_data, 0)) || traverse == NULL)
		return sjme_unit_fail(test, "Could not create traverse?");
	
	/* Iterate tree. */
	memset(&iterator, 0x7F, sizeof(iterator));
	if (sjme_error_is(test->error = sjme_traverse_iterate(
		SJME_AS_TRAVERSE(traverse), &iterator)))
		return sjme_unit_fail(test, "Could not iterate blank tree?");
	
	/* There should be no at node specified, and no bits either. */
	sjme_unit_equalP(test, NULL, iterator.atNode,
		"Node specified in iterator?");
	sjme_unit_equalI(test, 0, iterator.bits,
		"Bits not cleared?");
	sjme_unit_equalI(test, 0, iterator.bitCount,
		"Bit count not cleared?");
	
	/* Destroy traverse. */
	if (sjme_error_is(test->error = sjme_traverse_destroy(
		SJME_AS_TRAVERSE(traverse))))
		return sjme_unit_fail(test, "Could not destroy traverse?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
