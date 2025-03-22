/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

typedef sjme_jboolean (*sjme_nvm_byteCode_compareAFunc)(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b);

typedef sjme_jboolean (*sjme_nvm_byteCode_compareIFunc)(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b);

sjme_jboolean sjme_nvm_byteCode_compareAEq(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b)
{
	return a == b;
}

sjme_jboolean sjme_nvm_byteCode_compareANe(
	sjme_attrInValue sjme_jobject a,
	sjme_attrInValue sjme_jobject b)
{
	return a != b;
}

sjme_jboolean sjme_nvm_byteCode_compareIEq(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a == b;
}

sjme_jboolean sjme_nvm_byteCode_compareIGe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a >= b;
}

sjme_jboolean sjme_nvm_byteCode_compareIGt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a > b;
}

sjme_jboolean sjme_nvm_byteCode_compareILe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a <= b;
}

sjme_jboolean sjme_nvm_byteCode_compareILt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a < b;
}

sjme_jboolean sjme_nvm_byteCode_compareINe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a != b;
}

static const sjme_nvm_byteCode_compareAFunc sjme_nvm_byteCode_compareAs[2] =
{
	sjme_nvm_byteCode_compareAEq,
	sjme_nvm_byteCode_compareANe,
};

static const sjme_nvm_byteCode_compareIFunc sjme_nvm_byteCode_compareIs[6] =
{
	sjme_nvm_byteCode_compareIEq,
	sjme_nvm_byteCode_compareINe,
	sjme_nvm_byteCode_compareILt,
	sjme_nvm_byteCode_compareIGe,
	sjme_nvm_byteCode_compareIGt,
	sjme_nvm_byteCode_compareILe,
};

static const sjme_javaTypeId sjme_nvm_byteCode_returnTypes[6] =
{
	SJME_JAVA_TYPE_ID_INTEGER,
	SJME_JAVA_TYPE_ID_LONG,
	SJME_JAVA_TYPE_ID_FLOAT,
	SJME_JAVA_TYPE_ID_DOUBLE,
	SJME_JAVA_TYPE_ID_OBJECT,
	SJME_JAVA_TYPE_ID_VOID,
};

SJME_NVM_BYTECODE_SLOW(IfAX)
{
	sjme_jint offset;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	/* Pop single object value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareAs[id - 198](value.value.l, NULL))
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfX)
{
	sjme_jint offset;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	/* Pop single integer value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareIs[id - 153](value.value.i, 0))
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfICmpX)
{
	sjme_jint offset;
	sjme_jvalueTyped a, b;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	/* Pop both integer values. */
	memset(&b, 0, sizeof(b));
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &b)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareIs[id - 159](a.value.i, b.value.i))
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IfACmpX)
{
	sjme_jint offset;
	sjme_jvalueTyped a, b;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));
	
	/* Pop both integer values. */
	memset(&b, 0, sizeof(b));
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &b)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &a)))
		return sjme_error_vmError(inFrame, error);

	/* Successful branch? */
	if (sjme_nvm_byteCode_compareAs[id - 165](a.value.l, b.value.l))
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Goto)
{
	sjme_jint offset;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read the branch value. */
	offset = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[1]));

	/* Jumps according to the offset. */
	pcNew->adjust = offset;
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NoOp)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Does nothing except skip the instruction. */
	pcNew->adjust = 1;
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(ReturnX)
{
	sjme_javaTypeId desire;
	sjme_jvalueTyped result;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Must be returning the same type. */
	desire = sjme_nvm_byteCode_returnTypes[id - 172];
	if (inFrame->inCode->inMethod->argR != desire)
		return sjme_error_vmError(inFrame, SJME_ERROR_WRONG_RETURN_TYPE);

	/* If not returning void, pop value to return onto the parent stack. */
	if (desire != SJME_JAVA_TYPE_ID_VOID)
	{
		/* Must push onto something. */
		if (inFrame->parent == NULL)
			return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);
		
		/* Pop value. */
		memset(&result, 0, sizeof(result));
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			desire, &result)))
			return sjme_error_vmError(inFrame, error);

		/* Push onto the parent stack. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame->parent, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Pop the current frame. */
	pcNew->popFrame = SJME_JNI_TRUE;
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(TableSwitch)
{
	sjme_jint paramBase, lo, hi, tableCount;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Determine the relative base for parameters. */
	paramBase = sjme_util_alignTo((inFrame->pc + 1), 4);

	/* Read low and high values. */
	lo = sjme_big_int(*sjme_util_memUnaligned32(&relRawCode[paramBase + 4]));
	hi = sjme_big_int(*sjme_util_memUnaligned32(&relRawCode[paramBase + 8]));

	/* The table must be valid. */
	tableCount = (hi - lo) + 1;
	if (lo > hi || tableCount <= 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* Read in switch value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Would be a default jump? */
	if (value.value.i < lo || value.value.i > hi)
		pcNew->adjust = sjme_big_int(
			*sjme_util_memUnaligned32(&relRawCode[paramBase]));

	/* In the table. */
	else
		pcNew->adjust = sjme_big_int(*sjme_util_memUnaligned32(
			&relRawCode[paramBase + 8 + (4 * (value.value.i - lo))]));
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
