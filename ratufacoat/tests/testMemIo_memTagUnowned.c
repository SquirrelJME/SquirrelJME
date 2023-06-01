/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "memio/memtag.h"
#include "memio/memtaginternal.h"
#include "debug.h"
#include "include/memIoTestStruct.h"

/** The free count. */
static int test_freeCountUnowned;

/**
 * Function called on free.
 *
 * @param freeingPtr The pointer being freed.
 * @param error Any error.
 * @return Does not fail here.
 * @since 2023/03/24
 */
static sjme_jboolean test_freeFuncUnowned(sjme_memIo_tagGroup* inGroup,
	void** freeingPtr, sjme_error* error)
{
	test_freeCountUnowned++;

	return sjme_true;
}

/**
 * Tests unowned memory tags.
 *
 * @since 2023/03/24
 */
SJME_TEST_PROTOTYPE(testMemIo_memTagUnowned)
{
	SJME_MEMIO_TAGGED(testStruct)* alloc;

	/* Allocate tag. */
	alloc = NULL;
	if (!sjme_memIo_taggedNewUnowned(&alloc,
			sjme_memIo_taggedNewSizeOf(alloc),
			test_freeFuncUnowned, &shim->error))
		return FAIL_TEST(1);

	/* Access it. */
	(*alloc)->a = 1;
	(*alloc)->b = 2;
	(*alloc)->c = 3;

	/* Free it. */
	test_freeCountUnowned = 0;
	if (!sjme_memIo_taggedFree(&alloc, &shim->error))
		return FAIL_TEST(2);

	/* Pointer should be NULL now. */
	if (alloc != NULL)
		return FAIL_TEST(3);

	/* This should just be for the single tag. */
	if (test_freeCountUnowned != 1)	/* NOLINT */
		return FAIL_TEST(4);

	/* Success! */
	return PASS_TEST();
}
