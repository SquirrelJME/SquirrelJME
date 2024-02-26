/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"
#include "sjme/atomic.h"

#define P(x) ((void*)x)

/**
 * Tests atomic values.
 *  
 * @since 2024/01/09 
 */
SJME_TEST_DECLARE(testAtomic)
{
	sjme_atomic_sjme_jint atomInt;
	sjme_atomic_sjme_pointer atomPtr;

	/* Seed to some invalid value. */
	atomInt.value = 666;

	/* Set value, implicit with no check. */
	sjme_atomic_sjme_jint_set(&atomInt, 123);

	/* Set, should have old value. */
	sjme_unit_equalI(test,
		123, sjme_atomic_sjme_jint_set(&atomInt, 456),
		"Int old value was not set first?");

	/* Add to it. */
	sjme_unit_equalI(test,
		456, sjme_atomic_sjme_jint_getAdd(&atomInt, 2),
		"Int old value was not set second?");

	/* Try changing it but failing. */
	sjme_unit_equalZ(test, SJME_JNI_FALSE,
		sjme_atomic_sjme_jint_compareSet(&atomInt,
			666, 123),
		"Int compare mismatch failed?");

	/* Then change it but pass. */
	sjme_unit_equalZ(test, SJME_JNI_TRUE,
		sjme_atomic_sjme_jint_compareSet(&atomInt,
			458, 123),
		"Int compare matched failed?");

	/* Should be the new value. */
	sjme_unit_equalI(test,
		123, sjme_atomic_sjme_jint_get(&atomInt),
		"Int compared value not set?");

	/* Seed to some invalid value. */
	atomPtr.value = P(666);

	/* Set value, implicit with no check. */
	sjme_atomic_sjme_pointer_set(&atomPtr, P(123));

	/* Set, should have old value. */
	sjme_unit_equalP(test,
		P(123), sjme_atomic_sjme_pointer_set(&atomPtr, P(456)),
		"Pointer old value was not set first?");

	/* Add to it. */
	sjme_unit_equalP(test,
		P(456), sjme_atomic_sjme_pointer_getAdd(&atomPtr, 2),
		"Pointer old value was not set second?");

	/* Try changing it but failing. */
	sjme_unit_equalZ(test, SJME_JNI_FALSE,
		sjme_atomic_sjme_pointer_compareSet(&atomPtr,
			P(666), P(123)),
		"Pointer compare mismatch failed?");

	/* Then change it but pass. */
	sjme_unit_equalZ(test, SJME_JNI_TRUE,
		sjme_atomic_sjme_pointer_compareSet(&atomPtr,
			P(458), P(123)),
		"Pointer compare matched failed?");

	/* Should be the new value. */
	sjme_unit_equalP(test,
		P(123), sjme_atomic_sjme_pointer_get(&atomPtr),
		"Pointer compared value not set?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
