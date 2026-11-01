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
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/access.h"
#include "sjme/nvm/instanceInspect.h"

typedef struct sjme_nvm_byteCode_invokeState
{
	/** The frame this is called from. */
	sjme_nvm_frame inFrame;

	/** The thread this is being called in. */
	sjme_nvm_thread inThread;
	
	/** The GC commit. */ 
	sjme_nvm_frame_gcCommit* commit;
	
	/** The instance type. */
	sjme_nvm_class_instanceType instanceType;
	
	/** The call type. */
	sjme_nvm_methodCallType callType;
	
	/** The Method ID. */
	sjme_jmethodID methodId;
	
	/** The error state. */
	sjme_errorCode error;
	
	/** The MLE error state. */
	sjme_errorCode mleError;
	
	/** New frame which was craeted. */
	sjme_nvm_frame newFrame;
	
	/** Argument count. */
	sjme_jint argC;
	
	/** Arguments. */
	sjme_jvalueTyped* argV;
	
	/** Argument parameters. */
	sjme_jvalueTyped* argVParam;
	
	/** Return value. */
	sjme_jvalueTyped mleArgR;
	
	/** Is this a static method? */
	sjme_jboolean isStatic;
	
	/** The instance called. */
	sjme_jobject instance;
	
	/** The target method. */
	sjme_nvm_class_methodInfo target;
	
	/** The method ID. */
	sjme_jmethodID virtualId;

	/** The class this is in, if known. */
	sjme_jclass isClass;
} sjme_nvm_byteCode_invokeState;

static sjme_errorCode sjme_nvm_byteCode_slowInvokeNotStatic(
	sjme_nvm_byteCode_invokeState* invoke)
{
	sjme_errorCode error;

	if (invoke == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Pop. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(
		invoke->inFrame, SJME_JAVA_TYPE_ID_OBJECT, invoke->commit,
		&invoke->argV[0])))
		return sjme_error_vmError(invoke->inFrame, error);

	/* Cannot be null. */
	invoke->instance = invoke->argV[0].v.l;
	if (invoke->instance == NULL)
		return sjme_error_vmError(invoke->inFrame,
			SJME_ERROR_NULL_STACK_POINTER);

	/* Must be the same or a compatible class as the call site. */
	if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
		SJME_F_T(invoke->inFrame),
		sjme_atomic_g(sjme_jclass,
			&invoke->methodId->member.inClass),
		SJME_O_C(invoke->instance))))
	{
		if (error == SJME_ERROR_CLASS_CAST)
			return sjme_error_vmError(invoke->inFrame,
				SJME_ERROR_CLASS_CHANGED);
		return sjme_error_default(error);
	}

	/* Need to relookup the method if virtual, to call the right one. */
	if (invoke->callType == SJME_NVM_CALL_VIRTUAL)
	{
		/* Lookup again. */
		if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
			sjme_atomic_g(sjme_jclass, &invoke->instance->isClass),
			SJME_F_T(invoke->inFrame),
			SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE,
			invoke->methodId->member.name->seq,
			invoke->methodId->member.type->seq, &invoke->virtualId)) ||
			invoke->virtualId == NULL)
			return sjme_error_vmError(invoke->inFrame, error);

		/* Use this one instead. */
		invoke->methodId = invoke->virtualId;

		/* Since the method has changed, we need to check again that */
		/* the target is still valid. This is mostly for sanity. */
		if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(invoke->inFrame),
			sjme_atomic_g(sjme_jclass,
				&invoke->methodId->member.inClass),
			SJME_O_C(invoke->instance))))
		{
			if (error == SJME_ERROR_CLASS_CAST)
				return sjme_error_vmError(invoke->inFrame,
					SJME_ERROR_CLASS_CHANGED);
			return sjme_error_default(error);
		}
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_byteCode_slowInvokeNative(
	sjme_nvm_byteCode_invokeState* invoke,
	sjme_errorCode error)
{
	if (invoke == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Perform the native call. */
	memset(&invoke->mleArgR, 0, sizeof(invoke->mleArgR));
	invoke->mleArgR.t = SJME_JAVA_TYPE_ID_VOID;

	/* Invoke MLE call, if static we use the entry point method */
	/* unless it has been replaced. */
	invoke->mleError = sjme_mle_mleCall(invoke->inFrame,
		(invoke->virtualId != NULL ? invoke->virtualId : invoke->methodId),
		invoke->target,
		&invoke->mleArgR,
		invoke->argC, invoke->argV);

	/* Recover and check MLE error. */
	/* Ignore cancelled calls. */
	error = invoke->mleError;
	if (error != SJME_ERROR_CANCEL_MLE_CALL &&
		sjme_error_is(error))
	{
		/* MLECallError is a valid response. */
		if (error == SJME_ERROR_MLE_CALL)
			return sjme_error_default(error);

		/* Unknown/Unimplemented method. */
		else if (error == SJME_ERROR_UNKNOWN_MLE_SHELF ||
			error == SJME_ERROR_UNKNOWN_MLE_FUNCTION)
		{
#if defined(SJME_CONFIG_DEBUG_MLE)
			sjme_message("Missing MLE: %s.%s %s",
				sjme_charSeq_tempUtf(sjme_atomic_g(sjme_nvm_class_info,
					&invoke->target->inClass)->name->seq),
				sjme_charSeq_tempUtf(invoke->target->name->seq),
				sjme_charSeq_tempUtf(invoke->target->type->seq));
#endif

			return sjme_error_vmError(invoke->inFrame, error);
		}

		/* Emit linkage error otherwise. */
		else if (error == SJME_ERROR_UNKNOWN_NATIVE_FUNCTION ||
			error == SJME_ERROR_UNKNOWN_MLE_SHELF ||
			error == SJME_ERROR_UNKNOWN_MLE_FUNCTION)
		{
			if (sjme_error_is(error = sjme_nvm_task_frameEmit(invoke->inFrame,
				SJME_NVM_COMMON_EXCEPTION_LINKAGE_ERROR,
				NULL, "LINK %s.%s %s",
				sjme_charSeq_tempUtf(sjme_atomic_g(sjme_nvm_class_info,
					&invoke->target->inClass)->name->seq),
				sjme_charSeq_tempUtf(invoke->target->name->seq),
				sjme_charSeq_tempUtf(invoke->target->type->seq))))
				return sjme_error_vmError(invoke->inFrame, error);
		}

		/* Anything else is considered a failure. */
		else
			return sjme_error_vmError(invoke->inFrame, error);
	}

	/* Only push a value if not cancelled. */
	if (error != SJME_ERROR_CANCEL_MLE_CALL)
	{
		/* Wrong type? */
		if (invoke->mleArgR.t != invoke->target->argR)
			return sjme_error_vmError(invoke->inFrame,
				SJME_ERROR_INVALID_METHOD_TYPE);

		/* Is there a return value being pushed to the stack? */
		if (invoke->mleArgR.t != SJME_JAVA_TYPE_ID_VOID)
		{
			/* Push to the stack. */
			if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
				invoke->inFrame, invoke->commit, &invoke->mleArgR)))
				return sjme_error_vmError(invoke->inFrame, error);
		}
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_byteCode_slowInvokeNormal(
	sjme_nvm_byteCode_invokeState* invoke)
{
	sjme_errorCode error;
	sjme_jclass isClass;

	if (invoke == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot be native. */
	if (SJME_NVM_ACC_IS(invoke->target->flags, NATIVE))
		return sjme_error_vmError(invoke->inFrame,
			SJME_ERROR_PURE_VIRTUAL_CALL);
	
	/* Recover the class. */
	isClass = NULL;
	if (invoke->instance != NULL)
		isClass = sjme_atomic_g(sjme_jclass, &invoke->instance->isClass);

	/* Calling a proxy class method? */
	if (invoke->instance != NULL &&
		SJME_NVM_ACC_IS(SJME_CL_I(isClass)->flags, SPECIAL_PROXY) &&
		SJME_NVM_ACC_IS(invoke->target->flags, ABSTRACT))
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Enter the frame. */
	invoke->newFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnter(
		SJME_F_T(invoke->inFrame),
		&invoke->newFrame,
		invoke->methodId,
		invoke->callType,
		invoke->argC, invoke->argV)) || invoke->newFrame == NULL)
		return sjme_error_vmError(invoke->inFrame, error);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_byteCode_slowInvoke(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInNotNull sjme_jmethodID methodId)
{
	sjme_errorCode error;
	sjme_nvm_byteCode_invokeState invoke;

	if (inFrame == NULL || commit == NULL || methodId == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Proxy calls always need an instance. */
	if (callType == SJME_NVM_CALL_PROXY && instance == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Copy state. */
	memset(&invoke, 0, sizeof(invoke));
	invoke.inFrame = inFrame;
	invoke.inThread = SJME_F_T(inFrame);
	invoke.commit = commit;
	invoke.instanceType = instanceType;
	invoke.callType = callType;
	invoke.methodId = methodId;
	
	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToM(
		inFrame, methodId)))
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_CLASS_CHANGED));

	/* Get the non-virtual target info. */
	/* Note that proxies are always virtual. */
	if (callType == SJME_NVM_CALL_PROXY)
		invoke.target = methodId->info[SJME_NVM_CALL_VIRTUAL];
	else
		invoke.target = methodId->info[callType];

	/* Call target information is missing? */
	if (invoke.target == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Static-ness is wrong? */
	invoke.isStatic = SJME_NVM_ACC_IS(invoke.target->flags, STATIC);
	if (invoke.isStatic && instanceType != SJME_NVM_CLASS_MEMBER_STATIC &&
		callType != SJME_NVM_CALL_NON_VIRTUAL)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);
	
	/* Allocate pushed arguments. */
	invoke.argC = invoke.target->argC + (!invoke.isStatic ? 1 : 0);
	invoke.argV = sjme_alloca(sizeof(*invoke.argV) * (invoke.argC + 2));
	if (invoke.argV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(invoke.argV, 0, sizeof(*invoke.argV) * (invoke.argC + 2));
	
	/* Pull in stack arguments for the call. */
	invoke.argVParam = (!invoke.isStatic ? &invoke.argV[1] : invoke.argV);
	if (invoke.target->argC != 0)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(
			inFrame, commit,
			invoke.target->argC, invoke.target->argT, invoke.argVParam)))
			return sjme_error_vmError(inFrame, error);

	/* These may be set later. */
	invoke.instance = NULL;
	invoke.virtualId = NULL;
	invoke.mleError = SJME_ERROR_NONE;

	/* Check argument compatibility */
	if (!invoke.isStatic)
	{
		/* Forward. */
		if (sjme_error_is(error = sjme_nvm_byteCode_slowInvokeNotStatic(
			&invoke)))
			return sjme_error_default(error);
	}

	/* Proxy call? */
	if (callType == SJME_NVM_CALL_PROXY)
	{
		/* Ensure the class has a proxy handler. */
		invoke.isClass = sjme_atomic_g(sjme_jclass, &instance->isClass);
		if (invoke.isClass == NULL || invoke.isClass->proxyHandler == NULL)
			return sjme_error_vmError(inFrame, SJME_ERROR_ILLEGAL_STATE);

		/* Setup return value storage. */
		memset(&invoke.mleArgR, 0, sizeof(invoke.mleArgR));
		invoke.mleArgR.t = SJME_JAVA_TYPE_ID_VOID;

		/* Forward proxy call. */
		if (sjme_error_is(error = invoke.isClass->proxyHandler(inFrame, commit,
			instance, methodId, &invoke.mleArgR, invoke.argC, invoke.argV)))
			return sjme_error_vmError(inFrame, error);

		/* Wrong return type? */
		if (invoke.mleArgR.t != invoke.target->argR)
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Push the value to the stack. */
		if (invoke.mleArgR.t != SJME_JAVA_TYPE_ID_VOID)
			if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
				inFrame, commit, &invoke.mleArgR)))
				return sjme_error_vmError(inFrame, error);
	}

	/* If native, perform an MLE call. */
	else if (SJME_NVM_ACC_IS(invoke.target->flags, NATIVE) && invoke.isStatic)
	{
		/* Note that the MLE call can be cancelled. */
		if (sjme_error_is(error = sjme_nvm_byteCode_slowInvokeNative(
			&invoke, error)))
			return sjme_error_default(error);
	}

	/* Enter new stack frame for the target method, or at least try. */
	else
	{
		/* Normal call. */
		if (sjme_error_is(error = sjme_nvm_byteCode_slowInvokeNormal(
			&invoke)))
			return sjme_error_default(error);
	}

	/* Success? */
	if (sjme_error_is(invoke.mleError))
		return sjme_error_default(invoke.mleError);
	return SJME_ERROR_NONE;
}

#pragma region(InvokeInterface)
SJME_NVM_BYTECODE_SLOW(InvokeInterface)
{
	sjme_jmethodID methodId;
	sjme_jint poolIndex, depth;
	sjme_nvm_class_poolEntryMember* methodRef;
	sjme_jvalueTyped depthRef;
	sjme_nvm_frame_gcCommit commit;
	sjme_jobject instance;
	sjme_jclass isClass;
	sjme_jboolean isProxy, validProxyCall;
	SJME_NVM_BYTECODE_ENTRY;

	/* Always zero. */
	if (0 != relRawCode[4])
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* The count is used to refer to the base object to determine what is */
	/* used to determine the call site. */
	depth = (relRawCode[3] & 0xFF);
	if (depth <= 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, (sjme_nvm_class_poolEntry**)&methodRef,
		SJME_NVM_CLASS_POOL_TYPE_INTERFACE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Depth must be the count as the method reference arguments. */
	if (depth != methodRef->staticArgSlots + 1)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Read in the reference. */
	memset(&depthRef, 0, sizeof(depthRef));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame, depth - 1,
		&depthRef, NULL)))
		return sjme_error_vmError(inFrame, error);

	/* It must be an object type. */
	if (depthRef.t != SJME_JAVA_TYPE_ID_OBJECT)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* It cannot be null. */
	instance = depthRef.v.l;
	if (instance == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* If this is a proxy object, then it needs to go through a handler */
	/* at some point, but we need the real method we are invoking. */
	isClass = sjme_atomic_g(sjme_jclass, &instance->isClass);
	isProxy = SJME_NVM_ACC_IS(isClass->special, SPECIAL_PROXY);
	validProxyCall = SJME_JNI_FALSE;

	/* Lookup interface method, we always want to prioritize concrete */
	/* methods first. */
	methodId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByInterface(
		SJME_F_T(inFrame), SJME_JNI_FALSE,
		&methodId, instance, methodRef)) ||
		methodId == NULL)
	{
		/* If this is not a proxy, then we just outright fail here. */
		if (!isProxy)
			return sjme_error_vmError(inFrame, error);

		/* We allow abstract methods to be targeted if this is a proxy */
		/* class, as these classes are meant to have special handling. */
		if (sjme_error_is(error = sjme_nvm_vmMethod_idByInterface(
			SJME_F_T(inFrame), SJME_JNI_TRUE,
			&methodId, instance, methodRef)) ||
			methodId == NULL)
			return sjme_error_vmError(inFrame, error);

		/* This is a valid abstract proxy call. */
		validProxyCall = SJME_JNI_TRUE;
	}

	/* Perform the invocation. */
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		&commit, instance,
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		(validProxyCall ? SJME_NVM_CALL_PROXY : SJME_NVM_CALL_VIRTUAL),
		methodId)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InvokeSpecial)
SJME_NVM_BYTECODE_SLOW(InvokeSpecial)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass refClass;
	sjme_jclass currentClass;
	sjme_jmethodID refMethod;
	sjme_jvalueTyped rawOnThis;
	sjme_jobject onThis;
	sjme_jboolean inSameClass, inSuper, isInit, isPrivate, isPackagePrivate;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference, which refers to the referenced member. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Determine the referenced class. */
	refClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&refClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || refClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));

	/* The target method needs to be found dynamically. */
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
		refClass, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE, SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq, &refMethod)) ||
		refMethod == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Which class are we calling from? */
	currentClass = inFrame->inClass;
	
	/* Read in the reference. */
	memset(&rawOnThis, 0, sizeof(rawOnThis));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
		entry->member.staticArgSlots,
		&rawOnThis, NULL)))
		return sjme_error_vmError(inFrame, error);

	/* The instance object to call onto, cannot be null. */
	onThis = rawOnThis.v.l;
	if (onThis == NULL)
	{
		/* Emit the exception. */
		if (sjme_error_is(error = sjme_nvm_task_threadEmit(SJME_F_T(inFrame),
			SJME_NVM_COMMON_EXCEPTION_NULL_POINTER,
			NULL,
			"NARG")))
			return sjme_error_vmError(inFrame, error);

		/* Check for recycle, then do nothing except commit. */
		if (sjme_nvm_byteCode_checkRecycleR(inFrame))
			pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		goto skip_commit;
	}

	/* These modify the action to be performed */
	inSameClass = (currentClass == refClass);
	inSuper = sjme_nvm_vmClass_isSuperClass(currentClass,
		refClass);
	isInit = SJME_NVM_CLASS_INIT_IS(refMethod->bits, INSTANCE);
	isPrivate = SJME_NVM_ACC_IS(SJME_M_AF(refMethod), PRIVATE);
	isPackagePrivate = (!SJME_NVM_ACC_IS(SJME_M_AF(refMethod), PRIVATE) &&
		!SJME_NVM_ACC_IS(SJME_M_AF(refMethod), PROTECTED) &&
		!SJME_NVM_ACC_IS(SJME_M_AF(refMethod), PUBLIC));
	
	/* Call superclass method instead? */
	if ((!isPrivate && !isPackagePrivate) && inSuper && !isInit)
	{
		if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
			SJME_C_SU(refClass),
			SJME_F_T(inFrame),
			SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE, SJME_P_M_N(entry)->seq,
			SJME_P_M_T(entry)->seq, &refMethod)) ||
			refMethod == NULL)
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Cannot call a private method that is in another class */
	else if ((isPrivate || (isPackagePrivate && !isInit)) &&
		!inSameClass && !inSuper)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);
	
	/* Invoke this method */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		&commit, onThis,
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_NVM_CALL_NON_VIRTUAL, refMethod)))
		return sjme_error_vmError(inFrame, error);

skip_commit:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InvokeStatic)
SJME_NVM_BYTECODE_SLOW(InvokeStatic)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jmethodID target;
	sjme_jclass refClass;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Determine the referenced class. */
	refClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &refClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || refClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Lookup target method. */
	target = NULL;
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
		refClass, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq, &target)) ||
		target == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));

	/* Perform the invocation. */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		&commit, NULL,
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_NVM_CALL_NON_VIRTUAL, target)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InvokeVirtual)
SJME_NVM_BYTECODE_SLOW(InvokeVirtual)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jmethodID target;
	sjme_jclass refClass;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Determine the referenced class. */
	refClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &refClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || refClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Lookup target method. */
	target = NULL;
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
		refClass, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq, &target)) ||
		target == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Perform the invocation. */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		&commit, NULL,
		SJME_NVM_CLASS_MEMBER_INSTANCE, SJME_NVM_CALL_VIRTUAL,
		target)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()
