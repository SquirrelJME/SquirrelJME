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

/** Double NaN mask. */
#define SJME_NVM_NAN_DOUBLE INT64_C(0x7FF8000000000000)

/** Float NaN mask. */
#define SJME_NVM_NAN_FLOAT INT32_C(0x7F800000)

SJME_NVM_BYTECODE_SLOW(CastDoubleToX)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CastFloatToX)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CastIntToX)
{
	sjme_jvalueTyped in, out;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read input value. */
	memset(&in, 0, sizeof(in));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &in)))
		return sjme_error_vmError(inFrame, error);

	/* Determine output value. */
	memset(&out, 0, sizeof(out));
	switch (id)
	{
		case SJME_NVM_BYTECODE_JAVA_I2L:
			out.t = SJME_JAVA_TYPE_ID_LONG;
			out.v.j.part.lo = in.v.i;
			if ((in.v.i & INT32_C(0x80000000)) != 0)
				out.v.j.part.hi = INT32_C(0xFFFFFFFF);
			break;

		case SJME_NVM_BYTECODE_JAVA_I2F:
			out.t = SJME_JAVA_TYPE_ID_FLOAT;
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
			out.v.f.native = (float)in.v.i;
#else
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
#endif
		break;

		case SJME_NVM_BYTECODE_JAVA_I2D:
			out.t = SJME_JAVA_TYPE_ID_DOUBLE;
#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
			out.v.d.native = (double)in.v.i;
#else
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
#endif
			break;

		case SJME_NVM_BYTECODE_JAVA_I2B:
			out.t = SJME_JAVA_TYPE_ID_INTEGER;
			out.v.i = in.v.i & INT32_C(0xFF);
			if ((in.v.i & INT32_C(0x80)) != 0)
				out.v.i |= INT32_C(0xFFFFFF00);
			break;

		case SJME_NVM_BYTECODE_JAVA_I2C:
			out.t = SJME_JAVA_TYPE_ID_INTEGER;
			out.v.i = in.v.i & INT32_C(0xFFFF);
			break;

		case SJME_NVM_BYTECODE_JAVA_I2S:
			out.t = SJME_JAVA_TYPE_ID_INTEGER;
			out.v.i = in.v.i & INT32_C(0xFFFF);
			if ((in.v.i & INT32_C(0x8000)) != 0)
				out.v.i |= INT32_C(0xFFFF0000);
			break;
		
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&out)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CastLongToX)
{
	sjme_jvalueTyped in, out;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read input value. */
	memset(&in, 0, sizeof(in));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &in)))
		return sjme_error_vmError(inFrame, error);

	/* Determine output value. */
	memset(&out, 0, sizeof(out));
	switch (id)
	{
		case SJME_NVM_BYTECODE_JAVA_L2I:
			out.t = SJME_JAVA_TYPE_ID_INTEGER;
			out.v.i = in.v.j.part.lo;
			break;

		case SJME_NVM_BYTECODE_JAVA_L2F:
			out.t = SJME_JAVA_TYPE_ID_FLOAT;
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
			out.v.f.native = (float)in.v.j.full;
#else
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
#endif
		break;

		case SJME_NVM_BYTECODE_JAVA_L2D:
			out.t = SJME_JAVA_TYPE_ID_DOUBLE;
#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
			out.v.d.native = (double)in.v.j.full;
#else
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
#endif
			break;
		
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&out)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CompareDouble)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_DOUBLE, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_DOUBLE, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Is NaN? */
	if (((a.v.d.longBits & SJME_NVM_NAN_DOUBLE) == SJME_NVM_NAN_DOUBLE) ||
		((b.v.d.longBits & SJME_NVM_NAN_DOUBLE) == SJME_NVM_NAN_DOUBLE))
		result.v.i = (id == SJME_NVM_BYTECODE_JAVA_DCMPL ? -1 : 1);
	
#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/* Compare the values. */
	else if (a.v.d.native > b.v.d.native)
		result.v.i = 1;
	else if (a.v.d.native < b.v.d.native)
		result.v.i = -1;
	else
		result.v.i = 0;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CompareFloat)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_FLOAT, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_FLOAT, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Is NaN? */
	if (((a.v.f.bits & SJME_NVM_NAN_FLOAT) == SJME_NVM_NAN_FLOAT) ||
		((b.v.f.bits & SJME_NVM_NAN_FLOAT) == SJME_NVM_NAN_FLOAT))
		result.v.i = (id == SJME_NVM_BYTECODE_JAVA_FCMPL ? -1 : 1);
	
#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
	/* Compare the values. */
	else if (a.v.f.native > b.v.f.native)
		result.v.i = 1;
	else if (a.v.f.native < b.v.f.native)
		result.v.i = -1;
	else
		result.v.i = 0;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CompareLong)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Compare the values. */
	if (a.v.j.full > b.v.j.full)
		result.v.i = 1;
	else if (a.v.j.full < b.v.j.full)
		result.v.i = -1;
	else
		result.v.i = 0;

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathBinaryInt)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;

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
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathBinaryLong)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the math. */
	switch (SJME_NVM_BYTECODE_BINARY_MATH_TO_FUNC(id))
	{
		case SJME_NVM_BYTECODE_MATH_SHL:
			result.v.j.full = a.v.j.full << (b.v.j.full & 0x3FL);
			break;

		case SJME_NVM_BYTECODE_MATH_SHR:
			result.v.j.full = a.v.j.full >> (b.v.j.full & 0x3FL);
			break;

		case SJME_NVM_BYTECODE_MATH_USHR:
			result.v.j.full = (int64_t)(((uint64_t)a.v.j.full) >>
				((uint64_t)(b.v.j.full & 0x3FL)));
			break;

		case SJME_NVM_BYTECODE_MATH_AND:
			result.v.j.full = a.v.j.full & b.v.j.full;
			break;

		case SJME_NVM_BYTECODE_MATH_OR:
			result.v.j.full = a.v.j.full | b.v.j.full;
			break;

		case SJME_NVM_BYTECODE_MATH_XOR:
			result.v.j.full = a.v.j.full ^ b.v.j.full;
			break;
		
		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_LONG;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathDouble)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathFloat)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathInt)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;

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
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathLong)
{
	sjme_jvalueTyped a, b, result;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in both values. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &b)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the math. */
	switch (SJME_NVM_BYTECODE_MATH_TO_FUNC(id))
	{
		case SJME_NVM_BYTECODE_MATH_ADD:
			result.v.j.full = a.v.j.full + b.v.j.full;
			break;
			
		case SJME_NVM_BYTECODE_MATH_SUB:
			result.v.j.full = a.v.j.full - b.v.j.full;
			break;
		
		case SJME_NVM_BYTECODE_MATH_MUL:
			result.v.j.full = a.v.j.full * b.v.j.full;
			break;
		
		case SJME_NVM_BYTECODE_MATH_DIV:
			result.v.j.full = a.v.j.full / b.v.j.full;
			break;
		
		case SJME_NVM_BYTECODE_MATH_REM:
			result.v.j.full = a.v.j.full % b.v.j.full;
			break;

		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Push the result. */
	result.t = SJME_JAVA_TYPE_ID_LONG;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateDouble)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateFloat)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateInt)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in value to negate. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Negate it. */
	value.v.i = -value.v.i;

	/* Push it back to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(MathNegateLong)
{
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in value to negate. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_LONG, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Negate it. */
	value.v.j.full = -value.v.j.full;

	/* Push it back to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame, &value)))
		return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}
