/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>

#include "unit.h"

/** Variable list declarations. */
#define SJME_VA_DEF va_list inputVa; \
	va_list copyVa

/**
 * Performs a short with the given type.
 * 
 * @param type The type of return value.
 * @since 2023/11/11
 */
#define SJME_VA_SHORT(type) do {\
	va_start(inputVa, format); \
    sjme_unitShortingEmit(file, line, func, test, (type), format, inputVa); \
    va_end(inputVa); \
	} while(JNI_FALSE);

static jboolean sjme_unitShortingEmit(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	sjme_testResult type, const char* format, va_list vaArgs)
{
	if (test == NULL)
		return sjme_die("No test structure?");
		
	/* Emit message. */
	sjme_messageV(file, line, func, format, vaArgs);
	
	/* Hit abort for debugging. */
	abort();

	/* Jump back to the outer code. */
	longjmp(test->jumpPoint, type);
	
	/* Ignored. */
	return (type == SJME_TEST_RESULT_PASS);
}

sjme_testResult sjme_unitEqualIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test, jint a, jint b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	
	if (a != b)
	{
		sjme_messageR(file, line, func, "ASSERT: %d != %d", a, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unitEqualLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	jobject a, jobject b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	
	if (a != b)
	{
		sjme_messageR(file, line, func, "ASSERT: %p != %p", a, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unitFailR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	
	/* Test always fails. */
	sjme_messageR(file, line, func, "ASSERT: Fail");
	SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	return SJME_TEST_RESULT_FAIL;
}
