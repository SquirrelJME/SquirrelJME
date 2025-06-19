/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"

/** Determines the binary function to use based on the opcode. */
#define SJME_NVM_BYTECODE_BINARY_MATH_TO_FUNC(x) \
	((sjme_nvm_byteCode_mathBinaryFunc)(((x) - 120) >> 1))

/** Determines the function to use based on the opcode. */
#define SJME_NVM_BYTECODE_MATH_TO_FUNC(x) \
	((sjme_nvm_byteCode_mathFunc)(((x) - 96) >> 2))

SJME_NVM_BYTECODE_SLOW(MathBinaryInt)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the math. */
	switch (SJME_NVM_BYTECODE_BINARY_MATH_TO_FUNC(id))
	{
		case SJME_NVM_BYTECODE_MATH_SHL:
			result.v.i = a.v.i << (b.v.i & 0x1F);
			break;

		case SJME_NVM_BYTECODE_MATH_SHR:
			result.v.i = a.v.i >> (b.v.i & 0x1F);
			break;

		case SJME_NVM_BYTECODE_MATH_USHR:
			result.v.i = (sjme_jint)(((sjme_juint)a.v.i) >>
				((sjme_juint)(b.v.i & 0x1F)));
			break;

		case SJME_NVM_BYTECODE_MATH_AND:
			result.v.i = a.v.i & b.v.i;
			break;

		case SJME_NVM_BYTECODE_MATH_OR:
			result.v.i = a.v.i | b.v.i;
			break;

		case SJME_NVM_BYTECODE_MATH_XOR:
			result.v.i = a.v.i ^ b.v.i;
			break;
		
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathBinaryLong)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathDouble)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathFloat)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathInt)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the math. */
	switch (SJME_NVM_BYTECODE_MATH_TO_FUNC(id))
	{
		case SJME_NVM_BYTECODE_MATH_ADD:
			result.v.i = a.v.i + b.v.i;
			break;
			
		case SJME_NVM_BYTECODE_MATH_SUB:
			result.v.i = a.v.i - b.v.i;
			break;
		
		case SJME_NVM_BYTECODE_MATH_MUL:
			result.v.i = a.v.i * b.v.i;
			break;
		
		case SJME_NVM_BYTECODE_MATH_DIV:
			result.v.i = a.v.i / b.v.i;
			break;
		
		case SJME_NVM_BYTECODE_MATH_REM:
			result.v.i = a.v.i % b.v.i;
			break;

		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathLong)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateDouble)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateFloat)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateInt)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateLong)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
