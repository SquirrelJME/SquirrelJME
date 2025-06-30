/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeFast.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(Dup)
{
	sjme_jvalueTyped top;
	SJME_NVM_BYTECODE_ENTRY;

	/* What is at the top of the stack? */
	memset(&top, 0, sizeof(top));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
		0, &top, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be a wide type. */
	if (top.t == SJME_JAVA_TYPE_ID_LONG ||
		top.t == SJME_JAVA_TYPE_ID_DOUBLE)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Push a copy of it. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame, &top)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DupX1)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DupX2)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DupTwo)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DupTwoX1)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(DupTwoX2)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Pop)
{
	sjme_jvalueTyped top;
	SJME_NVM_BYTECODE_ENTRY;

	/* What is at the top of the stack? */
	memset(&top, 0, sizeof(top));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
		0, &top, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* Can only be narrow types. */
	if (SJME_TYPEID_IS_WIDE(top.t))
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Pop value and discard. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		top.t, &top)))
		return sjme_error_vmError(inFrame, error);

	/* If an object, count it down. */
	if (top.t == SJME_JAVA_TYPE_ID_OBJECT)
		if (sjme_error_is(error = sjme_nvm_instance_countDown(
			&top.v.l, NULL)))
			return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_SLOW(PopTwo)
{
	sjme_jvalueTyped top;
	sjme_nvm_byteCode_func fastFunc;
	SJME_NVM_BYTECODE_ENTRY;

	/* What is the topmost item on the stack? */
	memset(&top, 0, sizeof(top));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
		1, &top, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* If a wide type, rewrite to pop wide. */
	if (top.t == SJME_JAVA_TYPE_ID_LONG || top.t == SJME_JAVA_TYPE_ID_DOUBLE)
	{
		relRawCode[0] = SJME_NVM_BYTECODE_FAST_POP_WIDE;
		fastFunc = SJME_NVM_BYTECODE_FAST_NAME(PopWide);
	}

	/* Otherwise, rewrite to pop narrow. */
	else
	{
		relRawCode[0] = SJME_NVM_BYTECODE_FAST_POP_TWO_NARROW;
		fastFunc = SJME_NVM_BYTECODE_FAST_NAME(PopTwoNarrow);
	}

	/* Forward to the new fast function. */
	return fastFunc(inFrame, id, relRawCode, pcNew);
}

SJME_NVM_BYTECODE_SLOW(Swap)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
