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
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Adjust PC. */
	pcNew->adjust = 3;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XLoad)
{
	sjme_jint index;
	sjme_javaTypeId type;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Depends on the wideness. */
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
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XLoadZ)
{
	sjme_jint index;
	sjme_javaTypeId type;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Push copy of the local to the stack. */
	index = ((id - 26) & 3);
	type = sjme_nvm_byteCode_xLoadType[(id - 26) >> 2];
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		type,
		index)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XStore)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	sjme_javaTypeId type;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Depends on the wideness. */
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
		type, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Set local. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, index, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XStoreZ)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	sjme_javaTypeId type;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Pop object from the stack. */
	memset(&popped, 0, sizeof(popped));
	type = sjme_nvm_byteCode_xLoadType[(id - 59) >> 2];
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		type, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Set local. */
	index = ((id - 59) & 3);
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, index, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
