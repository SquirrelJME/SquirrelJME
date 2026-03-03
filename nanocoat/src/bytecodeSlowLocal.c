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

static const sjme_javaTypeId sjme_nvm_byteCode_xLoadType[5] =
{
	SJME_JAVA_TYPE_ID_INTEGER,
	SJME_JAVA_TYPE_ID_LONG,
	SJME_JAVA_TYPE_ID_FLOAT,
	SJME_JAVA_TYPE_ID_DOUBLE,
	SJME_JAVA_TYPE_ID_OBJECT,
};

static sjme_errorCode sjme_nvm_byteCode_iincAny(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInPositive sjme_jint increment)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	if (inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read the value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameLocalGet(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, index, &value)))
		return sjme_error_default(error);
	
	/* Increment. */
	value.v.i += increment;
	
	/* Set the value, we are setting an integer so logically we should */
	/* never get an actual commit for objects. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(inFrame,
		NULL, index, &value)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

#pragma region(IInc)
SJME_NVM_BYTECODE_SLOW(IInc)
{
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Perform the increment. */
	if (sjme_error_is(error = sjme_nvm_byteCode_iincAny(
		inFrame, (relRawCode[1] & 0xFF), 
		(sjme_jbyte)relRawCode[2])))
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_LOCAL_INVALID_READ));
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(IIncWide)
SJME_NVM_BYTECODE_SLOW(IIncWide)
{
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Perform the increment. */
	if (sjme_error_is(error = sjme_nvm_byteCode_iincAny(
		inFrame, sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[2])),
		sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[4])))))
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_LOCAL_INVALID_READ));
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XLoad)
SJME_NVM_BYTECODE_SLOW(XLoad)
{
	sjme_jint index;
	sjme_javaTypeId type;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Depends on the wideness. */
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	if (id == SJME_NVM_BYTECODE_JAVA_WIDE)
	{
		pcNew->adjust = 4;
		id = relRawCode[1] & 0xFF;
		index = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[2]));
	}
	else
	{
		pcNew->adjust = 2;
		index = relRawCode[1] & 0xFF;
	}

	/* Push copy of the local to the stack. */
	type = sjme_nvm_byteCode_xLoadType[id - 21];
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame, &commit, type, index)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XLoadZ)
SJME_NVM_BYTECODE_SLOW(XLoadZ)
{
	sjme_jint index;
	sjme_javaTypeId type;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Push copy of the local to the stack. */
	index = ((id - 26) & 3);
	type = sjme_nvm_byteCode_xLoadType[(id - 26) >> 2];
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame, &commit, type, index)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XStore)
SJME_NVM_BYTECODE_SLOW(XStore)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	sjme_javaTypeId type;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Depends on the wideness. */
	pcNew->type = SJME_NVM_BYTECODE_PC_RELATIVE;
	if (id == SJME_NVM_BYTECODE_JAVA_WIDE)
	{
		pcNew->adjust = 4;
		id = relRawCode[1] & 0xFF;
		index = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[2]));
	}
	else
	{
		pcNew->adjust = 2;
		index = relRawCode[1] & 0xFF;
	}

	/* Pop object from the stack. */
	memset(&popped, 0, sizeof(popped));
	memset(&commit, 0, sizeof(commit));
	type = sjme_nvm_byteCode_xLoadType[id - 54];
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		type, &commit, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Set local. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, &commit, index, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XStoreZ)
SJME_NVM_BYTECODE_SLOW(XStoreZ)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	sjme_javaTypeId type;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Pop object from the stack. */
	memset(&popped, 0, sizeof(popped));
	memset(&commit, 0, sizeof(commit));
	type = sjme_nvm_byteCode_xLoadType[(id - 59) >> 2];
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		type, &commit, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Set local. */
	index = ((id - 59) & 3);
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, &commit, index, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()
