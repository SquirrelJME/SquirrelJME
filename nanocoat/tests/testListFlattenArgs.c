/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "sjme/list.h"
#include "test.h"
#include "unit.h"

/** Test argument count. */
static const sjme_jint testArgC = 4;

/** Test argv values. */
static sjme_lpcstr testArgV[4] = {
	"squirrels",
	"are",
	"so",
	"cute!"
};

/**
 * Tests flattening of argc/argv.
 *
 * @since 2023/12/17
 */
SJME_TEST_DECLARE(testListFlattenArgs)
{
	sjme_list_sjme_lpcstr* list;
	sjme_jint i;

	/* Test array load of list. */
	list = NULL;
	if (sjme_error_is(sjme_list_flattenArgCV(test->pool,
		(sjme_list_sjme_lpstr**)&list,
		testArgC, testArgV)) || list == NULL)
		sjme_unit_fail(test, "Could not build flattened list?");

	/* Test resultant list values. */
	sjme_unit_equalI(test, list->length, testArgC, "Length invalid?");
	for (i = 0; i < testArgC; i++)
	{
		/* The strings should be equal. */
		sjme_unit_equalS(test, list->elements[i], testArgV[i],
			"Array element %d not set correctly?", i);

		/* However the pointers should not be the same, as it is a copy. */
		sjme_unit_notEqualP(test, list->elements[i], testArgV[i],
			"Value element %d was not copied?", i);
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
