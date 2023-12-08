/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <string.h>

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
	} while(SJME_JNI_FALSE)

/**
 * Operator comparison function.
 * 
 * @param size The size of the values.
 * @param a The first value.
 * @param b The second value.
 * @return Returns what the comparison is.
 * @since 2023/11/20
 */
typedef sjme_jboolean (*sjme_unitOperatorFunc)(size_t size, void* a, void* b);

/**
 * Operator comparison information.
 * 
 * @since 2023/11/20
 */
typedef struct sjme_unitOperatorInfo
{
	/** The operator symbol. */
	const char* symbol;
	
	/** The function for comparison. */
	sjme_unitOperatorFunc function;
} sjme_unitOperatorInfo;

static sjme_errorCode sjme_unitOperatorEqual(size_t size, void* a, void* b)
{
	return memcmp(a, b, size) == 0;
}

static sjme_errorCode sjme_unitOperatorNotEqual(size_t size, void* a, void* b)
{
	return memcmp(a, b, size) != 0;
}

static sjme_jboolean sjme_unitOperatorLessThan(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) < *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->hi <= jb->hi)
		return ja->lo < jb->lo;
	return SJME_JNI_FALSE;  
}

static sjme_jboolean sjme_unitOperatorLessEqual(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) <= *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->hi <= jb->hi)
		return ja->lo <= jb->lo;
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_unitOperatorGreaterThan(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) > *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->hi >= jb->hi)
		return ja->lo > jb->lo;
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_unitOperatorGreaterEqual(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) >= *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->hi >= jb->hi)
		return ja->lo >= jb->lo;
	return SJME_JNI_FALSE;
}

/** Operator information. */
const sjme_unitOperatorInfo sjme_unitOperatorInfos[SJME_NUM_UNIT_OPERATORS] =
{
	/* SJME_UNIT_OPERATOR_EQUAL. */
	{
		"==",
		sjme_unitOperatorEqual
	},

	/* SJME_UNIT_OPERATOR_NOT_EQUAL. */
	{
		"!=",
		sjme_unitOperatorNotEqual
	},

	/* SJME_UNIT_OPERATOR_LESS_THAN. */
	{
		"<",
		sjme_unitOperatorLessThan
	},

	/* SJME_UNIT_OPERATOR_LESS_EQUAL. */
	{
		"<=",
		sjme_unitOperatorLessEqual
	},

	/* SJME_UNIT_OPERATOR_GREATER_THAN. */
	{
		">",
		sjme_unitOperatorGreaterThan
	},

	/* SJME_UNIT_OPERATOR_GREATER_EQUAL. */
	{
		">=",
		sjme_unitOperatorGreaterEqual
	},
};

static sjme_errorCode sjme_unitShortingEmit(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInRange(SJME_TEST_RESULT_PASS, SJME_TEST_RESULT_FAIL)
		sjme_testResult type,
	sjme_attrInNullable const char* format, va_list vaArgs)
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

sjme_testResult sjme_unitOperatorIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	const sjme_unitOperatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unitOperatorInfos[operator];
	
	if (!opInfo->function(sizeof(sjme_jint), &a, &b))
	{
		sjme_messageR(file, line, func, "ASSERT: %d %s %d",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unitOperatorLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_jobject a,
	sjme_attrInNullable sjme_jobject b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	const sjme_unitOperatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unitOperatorInfos[operator];
	
	if (!opInfo->function(sizeof(sjme_jobject), &a, &b))
	{
		sjme_messageR(file, line, func, "ASSERT: %p %s %p",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unitOperatorPR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable void* a,
	sjme_attrInNullable void* b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	const sjme_unitOperatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unitOperatorInfos[operator];
	
	if (!opInfo->function(sizeof(void*), &a, &b))
	{
		sjme_messageR(file, line, func, "ASSERT: %p %s %p",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unitFailR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	
	/* Test always fails. */
	sjme_messageR(file, line, func, "ASSERT: Fail");
	SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	return SJME_TEST_RESULT_FAIL;
}

sjme_testResult sjme_unitSkipR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
{
	SJME_VA_DEF;
	
	/* Test always fails. */
	sjme_messageR(file, line, func, "Skipped test.");
	SJME_VA_SHORT(SJME_TEST_RESULT_SKIP);
	return SJME_TEST_RESULT_SKIP;
}
