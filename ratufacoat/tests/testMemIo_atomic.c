/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "memio/atomic.h"

/** First value. */
#define VALUE_A SJME_JINT_C(0xDEADBEEF)

/** Second value. */
#define VALUE_B SJME_JINT_C(0xCAFEBABE)

/** Third value. */
#define VALUE_C SJME_JINT_C(0xBEEEEEE5)

/** The add value. */
#define ADD_VALUE SJME_JINT_C(12)

/**
 * Tests that atomic work properly.
 * 
 * @since 2021/03/06
 */
SJME_TEST_PROTOTYPE(testMemIo_atomic)
{
	sjme_memIo_atomicInt integer;
	sjme_memIo_atomicPointer pointer;
	sjme_jint* pointerA;
	sjme_jint* pointerB;
	sjme_jint* pointerC;
	sjme_jint pointerValueA;
	sjme_jint pointerValueB;
	sjme_jint pointerValueC;
	
	/* Set value. */
	sjme_memIo_atomicIntSet(&integer, VALUE_A);
	
	/* It should be value A. */
	if (VALUE_A != sjme_memIo_atomicIntGet(&integer))
		return FAIL_TEST(1);
	
	/* Adding should return the old value but update it. */
	if (VALUE_A != sjme_memIo_atomicIntGetThenAdd(&integer,
		ADD_VALUE))
		return FAIL_TEST(2);
	
	/* Getting it should match the add plus the old value. */
	if ((VALUE_A + ADD_VALUE) != sjme_memIo_atomicIntGet(&integer))
		return FAIL_TEST(3);
	
	/* Revert value, old should be the former value. */
	if ((VALUE_A + ADD_VALUE) != sjme_memIo_atomicIntSet(&integer,
		VALUE_A))
		return FAIL_TEST(4);
	
	/* Fail to change the conditional value. */
	if (sjme_memIo_atomicIntCompareThenSet(&integer,
		VALUE_B, VALUE_A))
		return FAIL_TEST(5);
	
	/* Should still be the first value. */
	if (VALUE_A != sjme_memIo_atomicIntGet(&integer))
		return FAIL_TEST(6);
	
	/* Successfully change the value. */
	if (!sjme_memIo_atomicIntCompareThenSet(&integer,
		VALUE_A, VALUE_B))
		return FAIL_TEST(7);
	
	/* Should be the second value. */
	if (VALUE_B != sjme_memIo_atomicIntGet(&integer))
		return FAIL_TEST(8);
	
	/* Setup integer value. */
	pointerValueA = VALUE_A;
	pointerA = &pointerValueA;
	pointerValueB = VALUE_B;
	pointerB = &pointerValueB;
	pointerValueC = VALUE_C;
	pointerC = &pointerValueC;
	
	/* Set pointer value. */
	sjme_memIo_atomicPointerSet(&pointer, pointerA);
		
	/* Read it back, it should be the same. */
	if (sjme_memIo_atomicPointerGet(&pointer) != pointerA)
		return FAIL_TEST(9);
	
	/* Do the same but with a type specified. */
	if (sjme_memIo_atomicPointerGetType(&pointer,
		sjme_jint*) != pointerA)
		return FAIL_TEST(10);
	
	/* Read from the value, it should be value A. */
	if (*sjme_memIo_atomicPointerGetType(&pointer,
		sjme_jint*) != VALUE_A)
		return FAIL_TEST(11);
	
	/* Setting the pointer should return the old value. */
	if (pointerA != sjme_memIo_atomicPointerSetType(&pointer, pointerB,
		sjme_jint*))
		return FAIL_TEST(12);
	
	/* It should be value B here, we cannot set to C. */
	if (sjme_memIo_atomicPointerCompareThenSet(&pointer,
		pointerA, pointerC))
		return FAIL_TEST(13);
	
	/* It should be pointer B here still. */
	if (sjme_memIo_atomicPointerGet(&pointer) != pointerB)
		return FAIL_TEST(14);
	
	/* It should be value B here and we can change it to A. */
	if (!sjme_memIo_atomicPointerCompareThenSet(&pointer,
		pointerB, pointerA))
		return FAIL_TEST(15);
	
	/* It should be pointer A here. */
	if (sjme_memIo_atomicPointerGet(&pointer) != pointerA)
		return FAIL_TEST(16);
	
	/* Everything is okay! */
	return PASS_TEST();
}
