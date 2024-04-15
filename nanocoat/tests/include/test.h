/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME test support header.
 * 
 * @since 2023/08/09
 */

#ifndef SQUIRRELJME_TEST_H
#define SQUIRRELJME_TEST_H

#include <setjmp.h>

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "sjme/test/externTest.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TEST_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The result of a test.
 * 
 * @since 2023/08/09
 */
typedef enum sjme_testResult
{
	/** The test passed. */
	SJME_TEST_RESULT_PASS = 0,
	
	/** The test failed. */
	SJME_TEST_RESULT_FAIL = 1,
	
	/** The test skipped. */
	SJME_TEST_RESULT_SKIP = 2,
} sjme_testResult;

/**
 * Basic test information structure.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_test
{
	/** Base jump point for short circuits. */
	jmp_buf jumpPoint;

	/** Allocation pool for tests, so one not need be setup. */
	sjme_alloc_pool* pool;

	/** Any extra global value that is needed. */
	void* global;
} sjme_test;

/**
 * Basic test function.
 * 
 * @param test The current test information.
 * @return The result of the test.
 * @since 2023/08/09
 */
typedef sjme_testResult (*sjme_basicTestFunc)(sjme_test* test);

/**
 * A test which is available.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_availableTest
{
	/** The name of the test. */
	sjme_lpcstr name;
	
	/** The function which contains the test code. */
	sjme_basicTestFunc function;
} sjme_availableTest;

/**
 * Declares a test.
 * 
 * @param name The name of the test.
 * @since 2023/11/21
 */
#define SJME_TEST_DECLARE(name) \
	sjme_attrUnused sjme_testResult name(sjme_attrUnused sjme_test* test)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TEST_H
}
		#undef SJME_CXX_SQUIRRELJME_TEST_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TEST_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TEST_H */
