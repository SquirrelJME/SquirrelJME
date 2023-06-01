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

typedef struct corruptTestStruct
{
	/** The base structure. */
	testStruct base;

	/** Overboard. */
	sjme_jint over;
} corruptTestStruct;

/** Tagged version of the struct. */
SJME_MEMIO_DECL_TAGGED(corruptTestStruct);

/** The free count. */
static int test_freeCountCorrupt;

/**
 * Function called on free.
 *
 * @param freeingPtr The pointer being freed.
 * @param error Any error.
 * @return Does not fail here.
 * @since 2023/03/24
 */
static sjme_jboolean test_freeFuncCorrupt(sjme_memIo_tagGroup* inGroup,
	void** freeingPtr, sjme_error* error)
{
	test_freeCountCorrupt++;

	return sjme_true;
}

/**
 * Tests basic corruption detection of memory tags.
 *
 * @since 2023/03/24
 */
SJME_TEST_PROTOTYPE(testMemIo_memTagUnownedCorrupt)
{
	SJME_MEMIO_TAGGED(testStruct)* alloc;
	SJME_MEMIO_TAGGED(corruptTestStruct)* corrupted;
	sjme_jint* underValue;

	/* Allocate tag. */
	alloc = NULL;
	if (!sjme_memIo_taggedNewUnowned(&alloc,
			sjme_memIo_taggedNewSizeOf(alloc),
			test_freeFuncCorrupt, &shim->error))
		return FAIL_TEST(1);

	/* Access it. */
	(*alloc)->a = 1;
	(*alloc)->b = 2;
	(*alloc)->c = 3;

	/* Corrupt and set the over value. */
	corrupted = (SJME_MEMIO_TAGGED(corruptTestStruct)*)alloc;
	(*corrupted)->over = INT32_C(0x1A2B3C4D);

	/* Free it, it should fail. */
	test_freeCountCorrupt = 0;
	if (sjme_memIo_taggedFree(&alloc, &shim->error))
		return FAIL_TEST(2);

	/* Should be pointer violation. */
	if (shim->error.code != SJME_ERROR_PROTECTED_MEM_VIOLATION)
		return FAIL_TEST_SUB(2, 1);

	/* Pointer should not be NULL here, it was not freed. */
	if (alloc == NULL)
		return FAIL_TEST(3);

	/* Allocate tag again. */
	alloc = NULL;
	if (!sjme_memIo_taggedNewUnowned(&alloc,
			sjme_memIo_taggedNewSizeOf(alloc),
			test_freeFuncCorrupt, &shim->error))
		return FAIL_TEST(4);

	/* Access it. */
	(*alloc)->a = 1;
	(*alloc)->b = 2;
	(*alloc)->c = 3;

	/* Corrupt and set the undervalue, could be aligned ahead on 64-bit. */
	underValue = (sjme_jint*)((((sjme_jpointer)(&((*alloc)->a)))) -
		(sizeof(sjme_jint) * 2));
#if SJME_POINTER == 64
	underValue[0] = INT32_C(0x1A2B3C4D);
#endif
	underValue[1] = INT32_C(0x5E6F7A8B);

	/* Free it, it should fail. */
	test_freeCountCorrupt = 0;
	if (sjme_memIo_taggedFree(&alloc, &shim->error))
		return FAIL_TEST(5);

	/* Should be pointer violation. */
	if (shim->error.code != SJME_ERROR_PROTECTED_MEM_VIOLATION)
		return FAIL_TEST_SUB(5, 1);

	/* Pointer should not be NULL here, it was not freed. */
	if (alloc == NULL)
		return FAIL_TEST(6);

	/* Free should never be called for corrupt data. */
	if (test_freeCountCorrupt != 0)	/* NOLINT */
		return FAIL_TEST(7);

	/* Success! */
	return PASS_TEST();
}
