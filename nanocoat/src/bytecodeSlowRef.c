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
#include "sjme/nvm/classy.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(InvokeStatic)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryMember* member;
	sjme_jclass classy;
	sjme_lpcstr binaryName, methodName, methodType;
	sjme_nvm_frame newFrame;
	sjme_jvalueTyped* argV;
	sjme_jmethodID methodId;
	sjme_nvm_class_methodInfo target;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_default(error);

	/* Extract member information. */
	member = &entry->member;
	binaryName = (sjme_lpcstr)&member->inClass->descriptor->chars[0];
	methodName = (sjme_lpcstr)&member->nameAndType->name->chars[0];
	methodType = (sjme_lpcstr)&member->nameAndType->descriptor->chars[0];

	/* Debug. */
	sjme_message("invokestatic(%s:%s%s)",
		binaryName, methodName, methodType);
	
	/* Locate target class. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inFrame->inThread->inTask->classLoader,
		&classy,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || classy == NULL)
		return sjme_error_default(error);
	
	/* Locate method to execute. */
	methodId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		classy, inFrame->inThread,
		SJME_NVM_CLASS_MEMBER_STATIC,
		methodName, methodType, &methodId)) || methodId == NULL)
		return sjme_error_default(error);

	/* Get the non-virtual target info. */
	target = methodId->info[SJME_NVM_CALL_NON_VIRTUAL];

	/* Check permissions to call the target. */
	sjme_message("TODO: Check invoke*() permissions.");

	/* Allocate pushed arguments. */
	argV = sjme_alloca(sizeof(*argV) * (target->argC + 1));
	if (argV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Pull in stack arguments for the call. */
	if (target->argC != 0)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(
			inFrame, target->argC, target->argT, argV)))
			return sjme_error_default(error);

	/* Enter new stack frame for the target method, or at least try. */
	newFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnter(
		inFrame->inThread,
		&newFrame,
		methodId,
		SJME_NVM_CALL_NON_VIRTUAL,
		target->argC, argV)) || newFrame == NULL)
		return sjme_error_default(error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
