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
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
sjme_testResult sjme_unitEqualIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue jboolean inverted,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInValue jint a,
	sjme_attrInValue jint b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

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
	JNI_FALSE, __VA_ARGS__)

/**
 * Checks inequality between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualI(...) sjme_unitEqualIR(SJME_DEBUG_FILE_LINE_FUNC, \
	JNI_TRUE, __VA_ARGS__)

/**
 * Checks equality between the two object values.
 * 
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/17
 */
sjme_testResult sjme_unitEqualLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue jboolean inverted,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable jobject a,
	sjme_attrInNullable jobject b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

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
	JNI_FALSE, __VA_ARGS__)

/**
 * Checks inequality between the two object values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualL(...) sjme_unitEqualLR(SJME_DEBUG_FILE_LINE_FUNC, \
	JNI_TRUE, __VA_ARGS__)

/**
 * Checks equality between the two pointer values.
 * 
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/19
 */
sjme_testResult sjme_unitEqualPR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue jboolean inverted,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable void* a,
	sjme_attrInNullable void* b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

/**
 * Checks equality between the two pointer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/19
 */
#define sjme_unitEqualP(...) sjme_unitEqualPR(SJME_DEBUG_FILE_LINE_FUNC, \
	JNI_FALSE, __VA_ARGS__)

/**
 * Checks inequality between the two pointer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualP(...) sjme_unitEqualPR(SJME_DEBUG_FILE_LINE_FUNC, \
	JNI_TRUE, __VA_ARGS__)

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
	sjme_attrInNotNull sjme_test* test,
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

/**
 * Skips the test and stops execution.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is skip.
 * @since 2023/11/19
 */
sjme_testResult sjme_unitSkipR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(4, 5);

/**
 * Skips the test and stops execution.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is skip.
 * @since 2023/11/19
 */
#define sjme_unitSkip(...) sjme_unitSkipR(SJME_DEBUG_FILE_LINE_FUNC, \
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
