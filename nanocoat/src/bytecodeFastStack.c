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
#include "sjme/nvm/bytecodeFast.h"

SJME_NVM_BYTECODE_FAST(DupTwoNarrow)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_BYTECODE_FAST(DupWide)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_BYTECODE_FAST(DupTwoX1Narrow)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_BYTECODE_FAST(DupTwoX1Wide)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_BYTECODE_FAST(DupX1Wide)
{
	sjme_jvalueTyped a, b;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop the top two items on the stack. */
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_STACK_TYPE_WIDE, &b, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, error);
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_STACK_TYPE_WIDE, &a, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, error);

	/* Push them back, duplicate the first popped item. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&b)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&a)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&b)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_FAST(DupX2Narrow)
{
	SJME_NVM_BYTECODE_ENTRY;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_FAST(PopTwoNarrow)
{
	sjme_jvalueTyped a, b;
	SJME_NVM_BYTECODE_ENTRY;

	/* Pop value and discard. */
	memset(&a, 0, sizeof(a));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_STACK_TYPE_NARROW, &a, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, error);
	memset(&b, 0, sizeof(b));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_STACK_TYPE_NARROW, &b, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, error);

	/* If an object, count it down. */
	if (a.t == SJME_JAVA_TYPE_ID_OBJECT)
		if (sjme_error_is(error = sjme_nvm_instance_countDown(
			&a.v.l, NULL)))
			return sjme_error_vmError(inFrame, error);
	if (b.t == SJME_JAVA_TYPE_ID_OBJECT)
		if (sjme_error_is(error = sjme_nvm_instance_countDown(
			&b.v.l, NULL)))
			return sjme_error_vmError(inFrame, error);
	
	SJME_NVM_BYTECODE_EXIT;
}

SJME_NVM_BYTECODE_FAST(PopWide)
{
	sjme_jvalueTyped top;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Pop value and discard. */
	memset(&top, 0, sizeof(top));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_STACK_TYPE_WIDE, &top, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
