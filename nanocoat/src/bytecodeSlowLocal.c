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

SJME_NVM_BYTECODE_SLOW(IInc)
{
	sjme_jint index, increment;
	sjme_jvalue* value;
	SJME_NVM_BYTECODE_ENTRY;

	/* Directly access value. */
	value = NULL;
	index = (relRawCode[1] & 0xFF);
	if (sjme_error_is(error = sjme_nvm_task_frameLocalAddr(
		inFrame, SJME_JAVA_TYPE_ID_INTEGER, index,
		&value)) || value == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INVALID_READ);

	/* Increment directly. */
	value->i += (sjme_jbyte)relRawCode[2];
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IIncWide)
{
	sjme_jint index;
	sjme_jvalue* value;
	SJME_NVM_BYTECODE_ENTRY;

	/* Directly access value. */
	value = NULL;
	index = sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[2]));
	if (sjme_error_is(error = sjme_nvm_task_frameLocalAddr(
		inFrame, SJME_JAVA_TYPE_ID_INTEGER, index,
		&value)) || value == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INVALID_READ);

	/* Increment directly. */
	value->i += sjme_big_short(*sjme_util_memUnaligned16(&relRawCode[4]));
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XLoad)
{
	sjme_jint index;
	sjme_javaTypeId type;
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
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		type,
		index)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XLoadZ)
{
	sjme_jint index;
	sjme_javaTypeId type;
	SJME_NVM_BYTECODE_ENTRY;

	/* Push copy of the local to the stack. */
	index = ((id - 26) & 3);
	type = sjme_nvm_byteCode_xLoadType[(id - 26) >> 2];
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame, type, index)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XStore)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	sjme_javaTypeId type;
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
	type = sjme_nvm_byteCode_xLoadType[id - 54];
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		type, NULL, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Set local. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, index, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

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
		inFrame, index, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
