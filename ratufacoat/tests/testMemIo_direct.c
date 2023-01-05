/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "debug.h"
#include "memio/memdirectinternal.h"

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
	sjme_memIo_directChunk badChunk;

	/* Try to allocate over a value. */
	result = (void*)1234;
	if (sjme_memIo_directNew(&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(1);

	if (shim->error.code != SJME_ERROR_POINTER_NOT_NULL)
		return FAIL_TEST_SUB(1, 1);

	/* No size. */
	result = NULL;
	if (sjme_memIo_directNew(&result, 0, &shim->error))
		return FAIL_TEST(2);

	if (shim->error.code != SJME_ERROR_NEGATIVE_SIZE)
		return FAIL_TEST_SUB(2, 1);

	/* Protection violation, not using &var. */
	result = (void*)1234;
	if (sjme_memIo_directNew(result, sizeof(*result), &shim->error))
		return FAIL_TEST(3);

	if (shim->error.code != SJME_ERROR_PROTECTED_MEM_VIOLATION)
		return FAIL_TEST_SUB(3, 1);

	/* Should allocate now. */
	result = NULL;
	if (!sjme_memIo_directNew(&result, sizeof(*result),
		&shim->error))
		return FAIL_TEST(4);

	/* Should be allocated and zero initialized. */
	if (result == NULL || result->a != 0 || result->b != 0 || result->c != 0)
	{
		sjme_message("[%x, %x, %x]", result->a, result->b, result->c);
		return FAIL_TEST(5);
	}

	/* Free it. */
	if (!sjme_memIo_directFree(&result, &shim->error))
		return FAIL_TEST(6);

	/* Should be cleared. */
	if (result != NULL)
		return FAIL_TEST(7);

	/* Try to free a bad chunk with invalid size. */
	badChunk.size = -1234;
	badChunk.magic = (sjme_jsize)(SJME_MEMIO_DIRECT_CHUNK_MAGIC ^
		(~badChunk.size));
	result = (void*)&badChunk.data[0];
	if (sjme_memIo_directFree(&result, &shim->error))
		return FAIL_TEST(8);

	/* Try to free a bad chunk with invalid magic. */
	badChunk.size = 1234;
	badChunk.magic = 4321;
	result = (void*)&badChunk.data[0];
	if (sjme_memIo_directFree(&result, &shim->error))
		return FAIL_TEST(9);

	return PASS_TEST();
}
