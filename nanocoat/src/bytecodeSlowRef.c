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
#include "sjme/nvm/instance.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/task.h"

SJME_NVM_BYTECODE_SLOW(CheckCast)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryClass* classRef;
	sjme_jclass desireClass;
	sjme_lpcstr binaryName;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Which class are we going for? */
	classRef = &entry->classRef;
	binaryName = (sjme_lpcstr)&classRef->descriptor->chars[0];
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inFrame->inThread->inTask->classLoader,
		&desireClass,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Pop object from the stack. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPeek(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* Not a match? */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b) */
	if (value.value.l != NULL &&
		!(value.value.l->isClass == desireClass ||
		sjme_nvm_vmClass_isAssignableFrom(desireClass,
			value.value.l->isClass)))
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

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
	sjme_jvalueTyped mleArgR;
	sjme_jboolean callOkay;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Extract member information. */
	member = &entry->member;
	binaryName = (sjme_lpcstr)&member->inClass->descriptor->chars[0];
	methodName = (sjme_lpcstr)&member->nameAndType->name->chars[0];
	methodType = (sjme_lpcstr)&member->nameAndType->descriptor->chars[0];

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("invokestatic(%s:%s%s)",
		binaryName, methodName, methodType);
#endif
	
	/* Locate target class. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inFrame->inThread->inTask->classLoader,
		&classy,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || classy == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Locate method to execute, it is required to be found. */
	methodId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		classy, inFrame->inThread,
		SJME_NVM_CLASS_MEMBER_STATIC, SJME_JNI_TRUE,
		methodName, methodType, &methodId)) || methodId == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check permissions to call the target. */
	callOkay = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_instance_checkPermission(
		inFrame->inClass, SJME_AS_JMEMBERID(methodId), &callOkay)) ||
		!callOkay)
		return sjme_error_vmError(inFrame, sjme_error_defaultOr(error,
			SJME_ERROR_CLASS_CHANGED));

	/* Get the non-virtual target info. */
	target = methodId->info[SJME_NVM_CALL_NON_VIRTUAL];

	/* Allocate pushed arguments. */
	argV = sjme_alloca(sizeof(*argV) * (target->argC + 1));
	if (argV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Pull in stack arguments for the call. */
	if (target->argC != 0)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(
			inFrame, target->argC, target->argT, argV)))
			return sjme_error_vmError(inFrame, error);

	/* If native, perform an MLE call. */
	if (target->flags.native)
	{
		/* Perform the native call. */
		memset(&mleArgR, 0, sizeof(mleArgR));
		mleArgR.type = SJME_JAVA_TYPE_ID_VOID;
		if (sjme_error_is(error = sjme_mle_mleCall(inFrame,
			classy->binaryName,
			(sjme_lpcstr)&target->name->chars[0],
			(sjme_lpcstr)&target->type->chars[0],
			&mleArgR,
			target->argC, argV)))
			return sjme_error_vmError(inFrame, error);

		/* Wrong type? */
		if (mleArgR.type != target->argR)
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Is there a return value being pushed to the stack? */
		if (mleArgR.type != SJME_JAVA_TYPE_ID_VOID)
		{
			/* Count up if an object. */
			if (mleArgR.type == SJME_JAVA_TYPE_ID_OBJECT &&
				mleArgR.value.l != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(
					mleArgR.value.l, NULL)))
					return sjme_error_vmError(inFrame, error);
			
			/* Push */
			if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
				inFrame, &mleArgR)))
				return sjme_error_vmError(inFrame, error);
		}
	}

	/* Enter new stack frame for the target method, or at least try. */
	else
	{
		newFrame = NULL;
		if (sjme_error_is(error = sjme_nvm_task_threadEnter(
			inFrame->inThread,
			&newFrame,
			methodId,
			SJME_NVM_CALL_NON_VIRTUAL,
			target->argC, argV)) || newFrame == NULL)
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
