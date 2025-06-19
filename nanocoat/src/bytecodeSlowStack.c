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

SJME_NVM_BYTECODE_SLOW(Dup)
{
	sjme_jvalueTyped top;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Pop)
{
	sjme_jvalueTyped top;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
