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

	/* Try to allocate over a value. */
	result = (void*)1234;
	if (sjme_memDirectNew(&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(1);

	if (shim->error.code != SJME_ERROR_POINTER_NOT_NULL)
		return FAIL_TEST_SUB(1, 1);

	/* No size. */
	result = NULL;
	if (sjme_memDirectNew(&result, 0, &shim->error))
		return FAIL_TEST(2);

	if (shim->error.code != SJME_ERROR_NEGATIVE_SIZE)
		return FAIL_TEST_SUB(2, 1);

	/* Protection violation, not using &var. */
	result = (void*)1234;
	if (sjme_memDirectNew(result, sizeof(*result), &shim->error))
		return FAIL_TEST(3);

	if (shim->error.code != SJME_ERROR_PROTECTED_MEM_VIOLATION)
		return FAIL_TEST_SUB(3, 1);

	/* Should allocate now. */
	result = NULL;
	if (!sjme_memDirectNew(&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(4);

	/* Should be allocated and zero initialized. */
	if (result == NULL || result->a != 0 || result->b != 0 || result->c != 0)
		return FAIL_TEST(5);

	/* Free it. */
	if (!sjme_memDirectFree((void**)&result, &shim->error))
		return FAIL_TEST(6);

	/* Should be cleared. */
	if (result != NULL)
		return FAIL_TEST(7);

	return PASS_TEST();
}
