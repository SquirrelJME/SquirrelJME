/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

/**
 * Tests flattening of Nul sequenced strings.
 *  
 * @since 2024/02/19 
 */
SJME_TEST_DECLARE(testListFlattenArgsNul)
{
	sjme_list_sjme_lpstr* result;
	
	/* Zero sequence. */
	result = NULL;
	if (sjme_error_is(sjme_list_flattenArgNul(test->pool,
		&result, "\0\0") || result == NULL))
		return sjme_unit_fail(test, "Could not flatten zero?");
	
	sjme_unit_equalI(test, 0, result->length,
		"Incorrect zero length?");
	
	/* Single sequence. */
	result = NULL;
	if (sjme_error_is(sjme_list_flattenArgNul(test->pool,
		&result, "a\0\0") || result == NULL))
		return sjme_unit_fail(test, "Could not flatten single?");
	
	sjme_unit_equalI(test, 1, result->length,
		"Incorrect single length?");
	sjme_unit_equalI(test, 0, strcmp("a", result->elements[0]),
		"Incorrect first single?");
	
	/* Two sequence. */
	result = NULL;
	if (sjme_error_is(sjme_list_flattenArgNul(test->pool,
		&result, "a\0b\0\0") || result == NULL))
		return sjme_unit_fail(test, "Could not flatten two?");
	
	sjme_unit_equalI(test, 2, result->length,
		"Incorrect two length?");
	sjme_unit_equalI(test, 0, strcmp("a", result->elements[0]),
		"Incorrect first two?");
	sjme_unit_equalI(test, 0, strcmp("b", result->elements[1]),
		"Incorrect second two?");
	
	/* Three sequence. */
	result = NULL;
	if (sjme_error_is(sjme_list_flattenArgNul(test->pool,
		&result, "a\0b\0c\0\0") || result == NULL))
		return sjme_unit_fail(test, "Could not flatten three?");
	
	sjme_unit_equalI(test, 3, result->length,
		"Incorrect three length?");
	sjme_unit_equalI(test, 0, strcmp("a", result->elements[0]),
		"Incorrect first three?");
	sjme_unit_equalI(test, 0, strcmp("b", result->elements[1]),
		"Incorrect second three?");
	sjme_unit_equalI(test, 0, strcmp("c", result->elements[2]),
		"Incorrect third three?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
