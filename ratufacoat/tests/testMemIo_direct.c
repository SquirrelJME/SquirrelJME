/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "memio/memdirect.h"

/**
 * A structure.
 *
 * @since 2023/01/01
 */
typedef struct a_structure
{
	/** A. */
	sjme_jint a;

	/** B. */
	sjme_jint b;

	/** C. */
	sjme_jint c;
} a_structure;

/**
 * Tests direct memory allocation.
 *
 * @since 2023/01/01
 */
SJME_TEST_PROTOTYPE(testMemIo_direct)
{
	a_structure* result;

	/* Allocate with null pointer. */
	if (sjme_memDirectNew(NULL, 12, &shim->error))
		return FAIL_TEST(1);

	/* Try to allocate over a value. */
	result = (void*)1234;
	if (sjme_memDirectNew((void**)&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(2);

	/* No size. */
	result = NULL;
	if (sjme_memDirectNew((void**)&result, 0, &shim->error))
		return FAIL_TEST(3);

	/* Should allocate now. */
	if (!sjme_memDirectNew((void**)&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(4);

	/* Should be allocated and zero initialized. */
	if (result == NULL || result->a != 0 || result->b != 0 || result->c != 0)
		return FAIL_TEST(5);

	/* Free it. */
	if (sjme_memDirectFree((void**)&result, &shim->error))
		return FAIL_TEST(6);

	/* Should be cleared. */
	if (result != NULL)
		return FAIL_TEST(7);

	return PASS_TEST();
}
