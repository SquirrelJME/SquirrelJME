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

SJME_LIST_DECLARE(sjme_jint, 2);

/**
 * Tests generic lists.
 *
 * @since 2023/12/17
 */
SJME_TEST_DECLARE(testList)
{
	sjme_list_sjme_jintPP* out;
	sjme_alloc_link* link;

	/* Allocate list. */
	out = NULL;
	if (SJME_IS_ERROR(sjme_list_alloc(test->pool, 10, &out, sjme_jint, 2)) ||
		out == NULL)
		sjme_unitFail(test, "Could not allocate list.");

	/* Get the allocation link. */
	link = NULL;
	if (SJME_IS_ERROR(sjme_alloc_getLink(out, &link)))
		sjme_unitFail(test, "Could not obtain link.");

	/* Was the size set? */
	sjme_unitEqualI(test, out->length, 10,
		"List wrong size?");

	/* Check that the link is the correct size. */
	sjme_unitEqualI(test, link->allocSize,
		SJME_SIZEOF_LIST(sjme_jint, 2, 10),
		"List allocation size incorrect?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
