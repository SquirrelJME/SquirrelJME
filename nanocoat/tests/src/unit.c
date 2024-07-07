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
typedef sjme_jboolean (*sjme_unit_operatorFunc)(size_t size, void* a, void* b);

/**
 * Operator comparison information.
 * 
 * @since 2023/11/20
 */
typedef struct sjme_unit_operatorInfo
{
	/** The operator symbol. */
	sjme_lpcstr symbol;
	
	/** The function for comparison. */
	sjme_unit_operatorFunc function;
} sjme_unit_operatorInfo;

static sjme_jboolean sjme_unit_operatorEqual(size_t size, void* a, void* b)
{
	return memcmp(a, b, size) == 0;
}

static sjme_jboolean sjme_unit_operatorNotEqual(size_t size, void* a, void* b)
{
	return memcmp(a, b, size) != 0;
}

static sjme_jboolean sjme_unit_operatorLessThan(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) < *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->part.hi <= jb->part.hi)
		return ja->part.lo < jb->part.lo;
	return SJME_JNI_FALSE;  
}

static sjme_jboolean sjme_unit_operatorLessEqual(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) <= *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->part.hi <= jb->part.hi)
		return ja->part.lo <= jb->part.lo;
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_unit_operatorGreaterThan(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) > *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->part.hi >= jb->part.hi)
		return ja->part.lo > jb->part.lo;
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_unit_operatorGreaterEqual(size_t size, void* a, void* b)
{
	sjme_jlong *ja;
	sjme_jlong *jb;
	
	if (size == sizeof(sjme_jint))
		return *((sjme_jint*)a) >= *((sjme_jint*)b);
	
	ja = a;
	jb = b;
	if (ja->part.hi >= jb->part.hi)
		return ja->part.lo >= jb->part.lo;
	return SJME_JNI_FALSE;
}

/** Operator information. */
const sjme_unit_operatorInfo sjme_unit_operatorInfos[SJME_NUM_UNIT_OPERATORS] =
{
	/* SJME_UNIT_OPERATOR_EQUAL. */
	{
		"==",
		sjme_unit_operatorEqual
	},

	/* SJME_UNIT_OPERATOR_NOT_EQUAL. */
	{
		"!=",
		sjme_unit_operatorNotEqual
	},

	/* SJME_UNIT_OPERATOR_LESS_THAN. */
	{
		"<",
		sjme_unit_operatorLessThan
	},

	/* SJME_UNIT_OPERATOR_LESS_EQUAL. */
	{
		"<=",
		sjme_unit_operatorLessEqual
	},

	/* SJME_UNIT_OPERATOR_GREATER_THAN. */
	{
		">",
		sjme_unit_operatorGreaterThan
	},

	/* SJME_UNIT_OPERATOR_GREATER_EQUAL. */
	{
		">=",
		sjme_unit_operatorGreaterEqual
	},
};

static sjme_jboolean sjme_unitShortingEmit(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInRange(SJME_TEST_RESULT_PASS, SJME_TEST_RESULT_FAIL)
		sjme_testResult type,
	sjme_attrInNullable sjme_lpcstr format, va_list vaArgs)
{
	if (test == NULL)
		return sjme_die("No test structure?");
		
	/* Emit message. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_messageV(file, line, func, SJME_JNI_FALSE,
		format, vaArgs);
#else
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		format, vaArgs);
#endif
	
	/* Hit abort for debugging. */
	sjme_debug_abort();

	/* Jump back to the outer code. */
	longjmp(test->jumpPoint, type);
	
	/* Ignored. */
	return (type == SJME_TEST_RESULT_PASS);
}

sjme_testResult sjme_unit_operatorIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unit_operator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	const sjme_unit_operatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unit_operatorInfos[operator];
	
	if (!opInfo->function(sizeof(sjme_jint), &a, &b))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: %d %s %d",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: %d %s %d",
			a, opInfo->symbol, b);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unit_operatorLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unit_operator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_jobject a,
	sjme_attrInNullable sjme_jobject b,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	const sjme_unit_operatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unit_operatorInfos[operator];
	
	if (!opInfo->function(sizeof(sjme_jobject), &a, &b))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: %p %s %p",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: %p %s %p",
			a, opInfo->symbol, b);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unit_operatorPR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unit_operator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable void* a,
	sjme_attrInNullable void* b,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	const sjme_unit_operatorInfo* opInfo;
	
	if (operator < 0 || operator >= SJME_NUM_UNIT_OPERATORS)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unit_operatorInfos[operator];
	
	if (!opInfo->function(sizeof(void*), &a, &b))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: %p %s %p",
			a, opInfo->symbol, b);
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: %p %s %p",
			a, opInfo->symbol, b);
	}
	
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unit_operatorSR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unit_operator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_lpcstr a,
	sjme_attrInNullable sjme_lpcstr b,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	const sjme_unit_operatorInfo* opInfo;
	sjme_jint lenA, lenB;
	int abComp;

	/* Strings can only be equal or not equal. */
	if (operator != SJME_UNIT_OPERATOR_EQUAL &&
		operator != SJME_UNIT_OPERATOR_NOT_EQUAL)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unit_operatorInfos[operator];

	/* Get length of both. */
	lenA = (a != NULL ? strlen(a) : -1);
	lenB = (b != NULL ? strlen(b) : -1);

	/* Compare lengths first. */
	if (!opInfo->function(sizeof(sjme_jint), &lenA, &lenB))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: len(%s) %s len(%s)",
			(a != NULL ? a : "NULL"),
			opInfo->symbol,
			(b != NULL ? b : "NULL"));
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: len(%s) %s len(%s)",
			(a != NULL ? a : "NULL"),
			opInfo->symbol,
			(b != NULL ? b : "NULL"));
	}

	/* Stop if either are NULL. */
	if (lenA < 0 || lenB < 0)
		return SJME_TEST_RESULT_PASS;

	/* Compare the two strings, normalize mismatch to 1. */
	abComp = strcmp(a, b);
	if (abComp != 0)
		abComp = 1;

	/* Did this not match? */
	if (abComp != (operator == SJME_UNIT_OPERATOR_EQUAL ? 0 : 1))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: %s %s %s",
			(a != NULL ? a : "NULL"),
			opInfo->symbol,
			(b != NULL ? b : "NULL"));
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: %s %s %s",
			(a != NULL ? a : "NULL"),
			opInfo->symbol,
			(b != NULL ? b : "NULL"));
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unit_operatorZR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unit_operator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInValue sjme_jboolean a,
	sjme_attrInValue sjme_jboolean b,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	const sjme_unit_operatorInfo* opInfo;

	if (operator != SJME_UNIT_OPERATOR_EQUAL &&
		operator != SJME_UNIT_OPERATOR_NOT_EQUAL)
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	opInfo = &sjme_unit_operatorInfos[operator];

	if ((a == b) != (operator == SJME_UNIT_OPERATOR_EQUAL))
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"ASSERT: %s %s %s",
			(a ? "true" : "false"), opInfo->symbol, (b ? "true" : "false"));
		SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	}
	else
	{
		sjme_messageR(file, line, func, SJME_JNI_FALSE,
			"PASS: %s %s %s",
			(a ? "true" : "false"), opInfo->symbol, (b ? "true" : "false"));
	}

	return SJME_TEST_RESULT_PASS;
}

sjme_testResult sjme_unit_failR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	
	/* Test always fails. */
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"ASSERT: Fail");
	SJME_VA_SHORT(SJME_TEST_RESULT_FAIL);
	return SJME_TEST_RESULT_FAIL;
}

sjme_testResult sjme_unit_skipR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr format, ...)
{
	SJME_VA_DEF;
	
	/* Test always fails. */
	sjme_messageR(file, line, func, SJME_JNI_FALSE,
		"Skipped test.");
	SJME_VA_SHORT(SJME_TEST_RESULT_SKIP);
	return SJME_TEST_RESULT_SKIP;
}
