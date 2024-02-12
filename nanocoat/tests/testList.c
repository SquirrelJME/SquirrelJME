/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "sjme/alloc.h"
#include "sjme/list.h"
#include "test.h"
#include "unit.h"

/** We do not have a pointer to pointer type. */
SJME_LIST_DECLARE(sjme_jint, 2);

/** List elements. */
static const sjme_jint testListElems[5] = {1, 2, 3, 4, 5};

/**
 * Tests generic lists.
 *
 * @since 2023/12/17
 */
SJME_TEST_DECLARE(testList)
{
	sjme_list_sjme_jintPP* ppList;
	sjme_list_sjme_jint* varList;
	sjme_list_sjme_jint* arrayList;
	sjme_alloc_link* link;
	sjme_jint i;

	/* Allocate list. */
	ppList = NULL;
	if (sjme_error_is(sjme_list_alloc(test->pool, 10, &ppList,
		sjme_jint, 2)) || ppList == NULL)
		sjme_unit_fail(test, "Could not allocate list.");

	/* Get the allocation link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(ppList, &link)))
		sjme_unit_fail(test, "Could not obtain link.");

	/* Was the size set? */
	sjme_unit_equalI(test, ppList->length, 10,
		"List wrong size?");
	sjme_unit_equalI(test, ppList->elementSize, sizeof(sjme_jint**),
		"Wrong element size?");

	/* Check that the link is the correct size. */
	sjme_unit_equalI(test, link->allocSize,
		SJME_SIZEOF_LIST(sjme_jint, 2, 10),
		"List allocation size incorrect?");

	/* Test variadic load of list. */
	varList = NULL;
	if (sjme_error_is(sjme_list_newV(test->pool, sjme_jint, 0, 5, &varList,
		1, 2, 3, 4, 5)) ||
		varList == NULL)
		sjme_unit_fail(test, "Could not build variadic list?");

	/* Test resultant list values. */
	sjme_unit_equalI(test, varList->length, 5,
		"Length invalid?");
	sjme_unit_equalI(test, varList->elementSize, sizeof(sjme_jint),
		"Element size invalid?");
	for (i = 0; i < 5; i++)
		sjme_unit_equalI(test, varList->elements[i], i + 1,
			"Variadic element %d not set correctly?", i);

	/* Test array load of list. */
	arrayList = NULL;
	if (sjme_error_is(sjme_list_newA(test->pool, sjme_jint, 0, 5, &arrayList,
		testListElems)) || arrayList == NULL)
		sjme_unit_fail(test, "Could not build array list?");

	/* Test resultant list values. */
	sjme_unit_equalI(test, arrayList->length, 5,
		"Length invalid?");
	sjme_unit_equalI(test, arrayList->elementSize, sizeof(sjme_jint),
		"Element size invalid?");
	for (i = 0; i < 5; i++)
		sjme_unit_equalI(test, arrayList->elements[i], i + 1,
			"Array element %d not set correctly?", i);

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
