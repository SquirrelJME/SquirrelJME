/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Unit test support.
 * 
 * @since 2023/11/11
 */

#ifndef SQUIRRELJME_UNIT_H
#define SQUIRRELJME_UNIT_H

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "test.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_UNIT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Checks equality between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
sjme_testResult sjme_unitEqualIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	jint a, jint b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(6, 7);

/**
 * Checks equality between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
#define sjme_unitEqualI(...) sjme_unitEqualIR(SJME_DEBUG_FILE_LINE_FUNC, \
	__VA_ARGS__)

/**
 * Checks equality between the two object values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/17
 */
sjme_testResult sjme_unitEqualLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	jobject a, jobject b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(6, 7);

/**
 * Checks equality between the two object values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/17
 */
#define sjme_unitEqualL(...) sjme_unitEqualLR(SJME_DEBUG_FILE_LINE_FUNC, \
	__VA_ARGS__)

/**
 * Unit test just fails.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is fail.
 * @since 2023/11/11
 */
sjme_testResult sjme_unitFailR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(4, 5);

/**
 * Unit test just fails.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is fail.
 * @since 2023/11/11
 */
#define sjme_unitFail(...) sjme_unitFailR(SJME_DEBUG_FILE_LINE_FUNC, \
	__VA_ARGS__)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_UNIT_H
}
		#undef SJME_CXX_SQUIRRELJME_UNIT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_UNIT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UNIT_H */
