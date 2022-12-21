/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "sjmejni/memtag.h"
#include "debug.h"

/**
 * Structure.
 *
 * @since 2022/12/20
 */
typedef struct testStruct
{
	/** First. */
	sjme_jint a;

	/* Second. */
	sjme_jint b;

	/* Third. */
	sjme_jint c;
} testStruct;

/** Tagged version of the struct. */
SJME_DECL_TAGGED(testStruct);

/**
 * Tests that JNI memory tags work.
 *
 * @since 2022/12/20
 */
SJME_TEST_PROTOTYPE(testJniMemTags)
{
	sjme_memTagGroup* tagGroup;
	SJME_TAGGED(testStruct)* alloc;
	SJME_TAGGED(testStruct)* nullIsh;
	SJME_TAGGED(testStruct)* notValid = (void*)INT32_C(0xCAFE);
	SJME_TAGGED(testStruct)* alreadySet;

	sjme_message("Deref: %d\n", sizeof(**(alloc)));

	/* Setup tag group. */
	tagGroup = NULL;
	if (!sjme_memTaggedNewGroup(&tagGroup, &shim->error))
		return FAIL_TEST(1);

	/* Sizeof the base should be the same. */
	if (sizeof(**(alloc)) != sizeof(testStruct)) /* NOLINT */
		return FAIL_TEST(2);

	/* Allocating with sizeof() should fail. */
	nullIsh = NULL;
	if (sjme_memTaggedNew(tagGroup, nullIsh, sizeof(nullIsh), /* NOLINT */
			SJME_MEM_TAG_STATIC, &shim->error))
		return FAIL_TEST(3);

	if (shim->error.code != SJME_ERROR_NULLARGS)
		return FAIL_TEST_SUB(3, 1);

	/* Allocating with sizeof() should fail. */
	alloc = NULL;
	if (sjme_memTaggedNew(tagGroup, &alloc, sizeof(alloc), SJME_MEM_TAG_STATIC,
			&shim->error))
		return FAIL_TEST(4);

	if (shim->error.code != SJME_ERROR_TAGGED_WRONG_SIZE_OF)
		return FAIL_TEST_SUB(4, 1);

	/* Allocating not using &alloc, should fail due to protection. */
	if (sjme_memTaggedNew(tagGroup, notValid, /* NOLINT */
			sjme_memTaggedNewSizeOf(notValid),
			SJME_MEM_TAG_STATIC, &shim->error))
		return FAIL_TEST(5);

	if (shim->error.code != SJME_ERROR_PROTECTED_TAG_VIOLATION)
		return FAIL_TEST_SUB(5, 1);

	/* Allocating over a pointer that already has something there. */
	alreadySet = (void*)INT32_C(0xCAFE);
	if (sjme_memTaggedNew(tagGroup, &alreadySet,
			sjme_memTaggedNewSizeOf(alreadySet), SJME_MEM_TAG_STATIC,
			&shim->error))
		return FAIL_TEST(6);

	if (shim->error.code != SJME_ERROR_TAG_NOT_NULL)
		return FAIL_TEST_SUB(6, 1);

	/* Try allocating memory. */
	if (!sjme_memTaggedNew(tagGroup, &alloc, sjme_memTaggedNewSizeOf(alloc),
		SJME_MEM_TAG_STATIC, &shim->error))
		return FAIL_TEST(7);

	return PASS_TEST();
}
