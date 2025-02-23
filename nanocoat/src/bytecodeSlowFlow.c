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

typedef sjme_jboolean (*sjme_nvm_byteCode_compareFunc)(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b);

sjme_jboolean sjme_nvm_byteCode_compareEq(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a == b;
}

sjme_jboolean sjme_nvm_byteCode_compareGe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a >= b;
}

sjme_jboolean sjme_nvm_byteCode_compareGt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a > b;
}

sjme_jboolean sjme_nvm_byteCode_compareLe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a <= b;
}

sjme_jboolean sjme_nvm_byteCode_compareLt(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a < b;
}

sjme_jboolean sjme_nvm_byteCode_compareNe(
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b)
{
	return a != b;
}

static const sjme_nvm_byteCode_compareFunc sjme_nvm_byteCode_compares[6] =
{
	sjme_nvm_byteCode_compareEq,
	sjme_nvm_byteCode_compareNe,
	sjme_nvm_byteCode_compareLt,
	sjme_nvm_byteCode_compareGe,
	sjme_nvm_byteCode_compareGt,
	sjme_nvm_byteCode_compareLe,
};

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
	if (sjme_nvm_byteCode_compares[id - 153](value.value.i, 0))
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
	if (sjme_nvm_byteCode_compares[id - 159](a.value.i, b.value.i))
		pcNew->adjust = offset;

	/* Failed branch. */
	else
		pcNew->adjust = 3;

	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NoOp)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Does nothing except skip the instruction. */
	pcNew->adjust = 1;
	
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
