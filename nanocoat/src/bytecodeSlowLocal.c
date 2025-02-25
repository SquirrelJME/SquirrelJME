/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(ALoadZ)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Push copy of the local to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		SJME_JAVA_TYPE_ID_OBJECT,
		id - 42)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(AStoreZ)
{
	sjme_jvalueTyped popped;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Pop object from the stack. */
	memset(&popped, 0, sizeof(popped));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &popped)))
		return sjme_error_vmError(inFrame, error);

	/* Set local. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, id - 75, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(ILoadZ)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Always a single byte. */
	pcNew->adjust = 1;

	/* Push copy of the local to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalPush(
		inFrame,
		SJME_JAVA_TYPE_ID_INTEGER,
		id - 26)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(IStore)
{
	sjme_jvalueTyped popped;
	sjme_jint index;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Depends on the wideness. */
	pcNew->adjust = 2;

	/* Pop object from the stack. */
	memset(&popped, 0, sizeof(popped));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Set local. */
	index = relRawCode[1] & 0xFF;
	if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
		inFrame, index, &popped)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
