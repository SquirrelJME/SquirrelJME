/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "memory.h"
#include "memoryintern.h"

static sjme_jboolean freeCalled = sjme_false;

static sjme_jboolean freeCheck(void* memPtr, sjme_memNode* memNode,
	sjme_error* error)
{
	/* Was called. */
	freeCalled = sjme_true;

	return sjme_true;
}

SJME_TEST_PROTOTYPE(testMemAlloc)
{
	sjme_juint* junk;
	sjme_memNode* node;

	/* Test allocation. */
	junk = sjme_mallocGc(sizeof(*junk), freeCheck,
		&shim->error);
	if (junk == NULL)
		return FAIL_TEST(1);

	/* The node pointer should be the same. */
	if (!sjme_getMemNode(junk, &node, &shim->error))
		return FAIL_TEST(2);
	if (node == NULL)
		return FAIL_TEST(3);
	if ((void*)junk != (void*)&node->bytes[0])
		return FAIL_TEST(4);

	/* Free it. */
	if (!sjme_free(junk, &shim->error))
		return FAIL_TEST(5);

	/* Was free ever called? */
	if (!freeCalled)
		return FAIL_TEST(6);

	/* Success. */
	return PASS_TEST();
}
