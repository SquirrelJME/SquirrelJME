/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "atomic.h"

/** First value. */
#define VALUE_A SJME_JINT_C(0xDEADBEEF)

/** Second value. */
#define VALUE_B SJME_JINT_C(0xCAFEBABE)

/** The add value. */
#define ADD_VALUE SJME_JINT_C(12)

/**
 * Tests that atomic work properly.
 * 
 * @since 2021/03/06
 */
SJME_TEST_PROTOTYPE(testAtomic)
{
	sjme_atomicInt integer;
	sjme_atomicPointer pointer;
	sjme_jint* pointerA;
	sjme_jint pointerValueA;
	
	/* Set value. */
	sjme_atomicIntSet(&integer, VALUE_A);
	
	/* It should be value A. */
	if (VALUE_A != sjme_atomicIntGet(&integer))
		return FAIL_TEST(1);
	
	/* Adding should return the old value but update it. */
	if (VALUE_A != sjme_atomicIntGetAndAdd(&integer, ADD_VALUE))
		return FAIL_TEST(2);
	
	/* Getting it should match the add plus the old value. */
	if ((VALUE_A + ADD_VALUE) != sjme_atomicIntGet(&integer))
		return FAIL_TEST(3);
	
	/* Revert value. */
	sjme_atomicIntSet(&integer, VALUE_A);
	
	/* Fail to change the conditional value. */
	if (sjme_atomicIntCompareAndSet(&integer, VALUE_B, VALUE_A))
		return FAIL_TEST(4);
	
	/* Should still be the first value. */
	if (VALUE_A != sjme_atomicIntGet(&integer))
		return FAIL_TEST(5);
	
	/* Successfully change the value. */
	if (!sjme_atomicIntCompareAndSet(&integer, VALUE_A, VALUE_B))
		return FAIL_TEST(6);
	
	/* Should be the second value. */
	if (VALUE_B != sjme_atomicIntGet(&integer))
		return FAIL_TEST(7);
	
	/* Setup integer value. */
	pointerValueA = VALUE_A;
	pointerA = &pointerValueA;
	
	/* Set pointer value. */
	sjme_atomicPointerSet(&pointer, pointerA);
		
	/** Read it back, it should be the same. */
	if (sjme_atomicPointerGet(&pointer) != pointerA)
		return FAIL_TEST(8);
	
	/** Do the same but with a type specified. */
	if (sjme_atomicPointerGetType(&pointer,
		sjme_jint*) != pointerA)
		return FAIL_TEST(9);
	
	/** Read from the value, it should be value A. */
	if (*sjme_atomicPointerGetType(&pointer,
		sjme_jint*) != VALUE_A)
		return FAIL_TEST(10);
	
	/* Everything is okay! */
	return PASS_TEST();
}
