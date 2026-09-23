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

static const sjme_basicTypeId sjme_nvm_byteCode_xArrayType[8] =
{
	SJME_JAVA_TYPE_ID_INTEGER,
	SJME_JAVA_TYPE_ID_LONG,
	SJME_JAVA_TYPE_ID_FLOAT,
	SJME_JAVA_TYPE_ID_DOUBLE,
	SJME_JAVA_TYPE_ID_OBJECT,
	SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
	SJME_BASIC_TYPE_ID_CHARACTER,
	SJME_BASIC_TYPE_ID_SHORT,
};

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
	if (error != SJME_ERROR_CANCEL_MLE_CALL &&
		sjme_error_is(error = invoke->mleError))
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
#if defined(SJME_CONFIG_HAS_BROKEN_CODE)
			/* MLE is not responsible for counting objects. */
			if (mleArgR.t == SJME_JAVA_TYPE_ID_OBJECT)
				mleArgR.v.l = sjme_weakUp(mleArgR.v.l);
#endif

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

	if (invoke == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot be native. */
	if (SJME_NVM_ACC_IS(invoke->target->flags, NATIVE))
		return sjme_error_vmError(invoke->inFrame,
			SJME_ERROR_PURE_VIRTUAL_CALL);

	/* Calling a proxy class method? */
	if (invoke->instance != NULL &&
		SJME_NVM_ACC_IS(sjme_atomic_g(sjme_jclass,
			&invoke->instance->isClass)->info->flags, SPECIAL_PROXY) &&
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

static sjme_errorCode sjme_nvm_byteCode_slowNewArrayMultiSub(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jclass rootComponentType,
	sjme_attrInValue sjme_jint dims,
	sjme_attrInValue sjme_jint skip,
	sjme_attrInNotNull sjme_jvalueTyped* argV,
	sjme_attrInNotNull sjme_jvalueTyped* result)
{
	sjme_errorCode error;
	sjme_jint nextSkip, i, numElem, left;
	sjme_jarray baseArray;
	sjme_jclass componentType;
	sjme_jvalueTyped sub;
	
	if (inFrame == NULL || rootComponentType == NULL ||
		argV == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (dims < 0 || skip < 0 || dims - skip < 0)
		return SJME_ERROR_INVALID_INSTRUCTION;

	/* How many elements are here? */
	numElem = argV[skip].v.i;
	if (numElem < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* How many dimensions are left? */
	left = dims - skip;
	
	/* Determine component type. */
	componentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadArray(
		SJME_F_CL(inFrame), &componentType, SJME_F_T(inFrame),
		rootComponentType, left)))
		return sjme_error_vmError(inFrame, error);

	/* Allocate base array. */
	baseArray = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), &baseArray, componentType, numElem)))
		return sjme_error_vmError(inFrame, error);

	/* Need to allocate a sub-array? */
	if (left > 1)
	{
		/* Allocate sub arrays for each element slot. */
		nextSkip = skip + 1;
		for (i = 0; i < numElem; i++)
		{
			/* Perform sub-allocation logic. */
			memset(&sub, 0, sizeof(sub));
			if (sjme_error_is(error = sjme_nvm_byteCode_slowNewArrayMultiSub(
				inFrame, rootComponentType, dims, nextSkip, argV, &sub)))
				return sjme_error_vmError(inFrame, error);

			/* Store into this array. */
			if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
				&baseArray->e, i, NULL, SJME_VLS_JOBJECT(sub.v.l))))
				return sjme_error_vmError(inFrame, error);
		}
	}

	/* The result gets the base array. */
	result->t = SJME_JAVA_TYPE_ID_OBJECT;
	result->v.l = SJME_AS_JOBJECT(baseArray);
	return SJME_ERROR_NONE;
}

#pragma region(ArrayLength)
SJME_NVM_BYTECODE_SLOW(ArrayLength)
{
	sjme_jarray array;
	sjme_jvalueTyped value, result;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Pop single object value. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (value.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Must be an array type. */
	array = SJME_AS_JARRAY(value.v.l);
	if (array == NULL ||
		!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Push length onto the stack. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	result.v.i = array->e.length;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(CheckCast)
SJME_NVM_BYTECODE_SLOW(CheckCast)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Pop object from the stack. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPeek(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("Is %s a %s?",
		(value.v.l == NULL ? "NULL" :
			sjme_charSeq_tempUtf(SJME_O_C(value.v.l)->fieldName)),
		(desireClass == NULL ? "NULL" :
			sjme_charSeq_tempUtf(desireClass->fieldName)));
#endif

	/* Not a match? */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b) */
	error = SJME_ERROR_NONE;
	if (value.v.l != NULL &&
		!(SJME_O_C(value.v.l) == desireClass ||
		sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame),
			desireClass, SJME_O_C(value.v.l)))))
	{
		/* Not an actual class cast error but something else? */
		if (sjme_error_is(error) && error != SJME_ERROR_CLASS_CAST)
			return sjme_error_default(error);
		
		/* Emit exception. */
		if (sjme_error_is(error = sjme_nvm_task_threadEmit(SJME_F_T(inFrame),
			SJME_NVM_COMMON_EXCEPTION_CLASS_CAST,
			NULL,
			"CAST %s %s",
			(value.v.l == NULL ? "NULL" :
				sjme_charSeq_tempUtf(SJME_O_C(value.v.l)->fieldName)),
					(desireClass == NULL ? "NULL" :
				sjme_charSeq_tempUtf(desireClass->fieldName)))))
			return sjme_error_vmError(inFrame, error);
		
		/* Check for recycle, class load and/or static constructor. */
		if (sjme_nvm_byteCode_checkRecycleR(inFrame))
		{
			pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
			return SJME_ERROR_NONE;
		}
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InstanceAccess)
SJME_NVM_BYTECODE_SLOW(InstanceAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalueTyped result;
	sjme_jvalueTyped instance;
	sjme_jboolean isPut;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Is this a get or a put? */
	isPut = (id == 181);

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_FIELD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &desireClass, SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq, SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmField_idByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not an instance field? */
	if (SJME_NVM_ACC_IS(fieldId->flags, STATIC))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);

	/* Read in value to put. */
	memset(&result, 0, sizeof(result));
	memset(&commit, 0, sizeof(commit));
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (SJME_NVM_ACC_IS(fieldId->flags, FINAL))
		{
			/* Cannot write static final fields. */
			if (SJME_NVM_ACC_IS(fieldId->flags, STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (sjme_atomic_g(sjme_jclass, &fieldId->member.inClass) !=
				inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in an instance initializer. */
			if (!SJME_NVM_FRAME_STATE_IS(inFrame->flags, INIT_INSTANCE))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &commit, &result)))
			return sjme_error_vmError(inFrame, error);
		
		/* Make sure this can actually be stored there. */
		if (sjme_error_is(error = sjme_nvm_access_checkCompatibleField(
			SJME_F_T(inFrame), fieldId, &result)))
			return sjme_error_vmError(inFrame,
				SJME_ERROR_CLASS_CHANGED);
	}
	
	/* Read instance to act on. */
	memset(&instance, 0, sizeof(instance));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &instance)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (instance.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Read/write promotion. */
	if (sjme_error_is(error = sjme_nvm_instance_fieldAccessStack(
		SJME_F_T(inFrame),
		&commit, fieldId, instance.v.l, &result, isPut)))
		return sjme_error_vmError(inFrame, error);

	/* Push result to the stack. */
	if (!isPut)
	{
		result.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &commit, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InstanceOf)
SJME_NVM_BYTECODE_SLOW(InstanceOf)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jvalueTyped check, result;
	sjme_jclass desireClass;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Read in object to check. */
	memset(&check, 0, sizeof(check));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &check)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Is this the given class? */
	memset(&result, 0, sizeof(result));
	if (check.v.l == NULL)
		result.v.i = SJME_JNI_FALSE;
	else
	{
		if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame),
			desireClass, SJME_O_C(check.v.l))))
		{
			if (error != SJME_ERROR_CLASS_CAST)
				return sjme_error_default(error);
			result.v.i = SJME_JNI_FALSE;
		}
		else
			result.v.i = SJME_JNI_TRUE;
	}

	/* Push result to the stack. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

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

#pragma region(Monitor)
SJME_NVM_BYTECODE_SLOW(Monitor)
{
	sjme_jboolean isExit;
	sjme_jvalueTyped instance;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Entry or exit? */
	isExit = (id == 195);

	/* Get the object we are accessing. */
	memset(&instance, 0, sizeof(instance));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &instance)))
		return sjme_error_vmError(inFrame, error);
	
	/* Cannot be null. */
	if (instance.v.l == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Either lock or unlock the object. */
	if (!isExit)
	{
		if (sjme_error_is(error = sjme_nvm_instance_monitorEnter(
			SJME_F_T(inFrame), instance.v.l)))
			return sjme_error_vmError(inFrame, error);
	}
	else
	{
		if (sjme_error_is(error = sjme_nvm_instance_monitorExit(
			SJME_F_T(inFrame), instance.v.l)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(New)
SJME_NVM_BYTECODE_SLOW(New)
{
	SJME_NVM_BYTECODE_ENTRY;
	sjme_jint poolIndex;
	sjme_jvalueTyped result;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_nvm_frame_gcCommit commit;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Allocate new instance of the given object. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(
		SJME_F_T(inFrame), -1, SJME_NVM_STRUCT_OBJECT_INSTANCE,
		&result.v.l, desireClass)) || result.v.l == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Push allocated object instance to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArray)
SJME_NVM_BYTECODE_SLOW(NewArray)
{
	sjme_jvalueTyped length, array;
	sjme_basicTypeId arrayType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &length)))
		return sjme_error_vmError(inFrame, error);

	/* Length is not valid. */
	if (length.v.i < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Map array type. */
	switch (relRawCode[1])
	{
		case 4:
			arrayType = SJME_BASIC_TYPE_ID_BOOLEAN;
			break;
		
		case 5:
			arrayType = SJME_BASIC_TYPE_ID_CHARACTER;
			break;
		
		case 6:
			arrayType = SJME_JAVA_TYPE_ID_FLOAT;
			break;
		
		case 7:
			arrayType = SJME_JAVA_TYPE_ID_DOUBLE;
			break;
		
		case 8:
			arrayType = SJME_BASIC_TYPE_ID_BYTE;
			break;
		
		case 9:
			arrayType = SJME_BASIC_TYPE_ID_SHORT;
			break;
		
		case 10:
			arrayType = SJME_JAVA_TYPE_ID_INTEGER;
			break;
		
		case 11:
			arrayType = SJME_JAVA_TYPE_ID_LONG;
			break;
		
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Create new array. */
	memset(&array, 0, sizeof(array));
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNewT(
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&array.v.l), arrayType,
		length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &array)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArrayA)
SJME_NVM_BYTECODE_SLOW(NewArrayA)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass componentType;
	sjme_jvalueTyped length;
	sjme_jvalueTyped array;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Determine the referenced class. */
	componentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&componentType,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || componentType == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &length)))
		return sjme_error_vmError(inFrame, error);

	/* Length is not valid. */
	if (length.v.i < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Create new array. */
	memset(&array, 0, sizeof(array));
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&array.v.l), componentType,
		length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &array)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArrayMulti)
SJME_NVM_BYTECODE_SLOW(NewArrayMulti)
{
	sjme_nvm_class_poolEntry* entry;
	sjme_javaTypeId* argT;
	sjme_jvalueTyped* argV;
	sjme_jint poolIndex, i, dims;
	sjme_jvalueTyped result;
	sjme_jclass rootComponentType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Lookup the root component type. */
	rootComponentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &rootComponentType, SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq, SJME_JNI_TRUE)) ||
		rootComponentType == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* How many dimensions to read? */
	dims = relRawCode[3] & 0xFF;
	if (dims <= 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* Allocate argument storage. */
	argT = sjme_alloca(sizeof(*argT) * dims);
	argV = sjme_alloca(sizeof(*argV) * dims);
	if (argT == NULL || argV == NULL)
		return sjme_error_outOfMemory(NULL, 0);
	memset(argT, 0, sizeof(*argT) * dims);
	memset(argV, 0, sizeof(*argV) * dims);

	/* Set all types to integer. */
	for (i = 0; i < dims; i++)
		argT[i] = SJME_JAVA_TYPE_ID_INTEGER;

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));

	/* Pop all dimensions at once. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(inFrame,
		NULL, dims, argT, argV)))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Recursively allocate sub-dimensions. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_byteCode_slowNewArrayMultiSub(
		inFrame, rootComponentType, dims, 0, argV, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Push final result to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(StaticAccess)
SJME_NVM_BYTECODE_SLOW(StaticAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalueTyped value;
	sjme_jboolean isPut;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Is this a get or a put? */
	isPut = (id == 179);

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_FIELD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, class load and/or static constructor. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmField_idByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not a static field? */
	if (!SJME_NVM_ACC_IS(fieldId->flags, STATIC))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);
	
	/* Read in value to put. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (SJME_NVM_ACC_IS(fieldId->flags, FINAL))
		{
			/* Cannot write instance final fields. */
			if (!SJME_NVM_ACC_IS(fieldId->flags, STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (sjme_atomic_g(sjme_jclass, &fieldId->member.inClass) !=
				inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in a static initializer. */
			if (!SJME_NVM_FRAME_STATE_IS(inFrame->flags, INIT_STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &commit, &value)))
			return sjme_error_vmError(inFrame, error);
		
		/* Make sure this can actually be stored there. */
		if (sjme_error_is(error = sjme_nvm_access_checkCompatibleField(
			SJME_F_T(inFrame), fieldId, &value)))
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Read/write promotion. */
	/* Note that the class referred to by the field is used directly as */
	/* static fields can be "inherited" by subclasses. So we want to use */
	/* the class this field truly exists in. */
	if (sjme_error_is(error = sjme_nvm_instance_fieldAccessStack(
		SJME_F_T(inFrame),
		&commit, fieldId, SJME_AS_JOBJECT(sjme_atomic_g(sjme_jclass,
			&fieldId->member.inClass)), &value, isPut)))
		return sjme_error_vmError(inFrame, error);
	
	/* Push result to the stack. */
	if (!isPut)
	{
		/* Place onto the stack. */
		value.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &commit, &value)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(Throw)
SJME_NVM_BYTECODE_SLOW(Throw)
{
	sjme_jvalueTyped toss;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in object to toss. */
	memset(&toss, 0, sizeof(toss));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &toss)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (toss.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Set thrown exception. */
	if (!sjme_atomic_cs(sjme_jobject, &SJME_F_T(inFrame)->tossed,
		NULL, toss.v.l))
		return sjme_error_vmError(inFrame, SJME_ERROR_DOUBLE_TOSS);

	/* Set the toss level very high as we are not running any implicit */
	/* constructors! */
	sjme_atomic_s(sjme_jint, &SJME_F_T(inFrame)->tossedLevel, INT32_MAX);

	/* Count up since it is now also in tossed. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(toss.v.l)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XALoad)
SJME_NVM_BYTECODE_SLOW(XALoad)
{
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_jvalueTyped pushValue;
	sjme_jarray array;
	sjme_jint index;
	sjme_basicTypeId arrayType;
	sjme_jclass componentType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in index and array. */
	memset(&commit, 0, sizeof(commit));
	memset(&indexValue, 0, sizeof(indexValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be null. */
	array = SJME_AS_JARRAY(arrayValue.v.l);
	if (array == NULL)
	{
		/* Emit exception. */
		if (sjme_error_is(error = sjme_nvm_task_frameEmit(inFrame,
			SJME_NVM_COMMON_EXCEPTION_NULL_POINTER, NULL,
			"NULL")))
			return sjme_error_vmError(inFrame, error);
		
		/* Leave early. */
		goto skip_tossed;
	}

	/* Make sure the array is actually valid. */
	arrayType = sjme_nvm_byteCode_xArrayType[id - 46];
	componentType = sjme_atomic_g(sjme_jclass, 
		&sjme_atomic_g(sjme_jclass,
			&array->object.isClass)->componentType);
	if (array == NULL || componentType == NULL ||
		!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!sjme_atomic_g(sjme_jclass,
			&array->object.isClass)->info->isArray ||
		componentType == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Boolean and byte reads are considered the same. */
	if ((arrayType == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE &&
		(componentType->arrayTypeId != SJME_BASIC_TYPE_ID_BOOLEAN &&
		componentType->arrayTypeId != SJME_BASIC_TYPE_ID_BYTE)) ||
		(arrayType != SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE &&
		componentType->arrayTypeId != arrayType))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);
	
	/* Check bounds. */
	index = indexValue.v.i;
	if (index < 0 || index >= array->e.length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS);

	/* Load value to be pushed. */
	memset(&pushValue, 0, sizeof(pushValue));
	if (sjme_error_is(error = sjme_nvm_vmField_cisGetS(
		&array->e, index, SJME_VLG_JVALUE_TYPED_P(&pushValue))))
		return sjme_error_vmError(inFrame, error);

	/* Push. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &pushValue)))
		return sjme_error_vmError(inFrame, error);

skip_tossed:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XAStore)
SJME_NVM_BYTECODE_SLOW(XAStore)
{
	sjme_jvalueTyped popValue;
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_jarray array;
	sjme_jint index;
	sjme_basicTypeId arrayType;
	sjme_javaTypeId promoteType;
	sjme_jclass componentType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Determine the type to read from the stack and to store to the array. */
	arrayType = sjme_nvm_byteCode_xArrayType[id - 79];
	promoteType = sjme_nvm_typePromote[arrayType];

	/* Read in value, index, and array. */
	memset(&popValue, 0, sizeof(popValue));
	memset(&indexValue, 0, sizeof(indexValue));
	memset(&arrayValue, 0, sizeof(arrayValue));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		promoteType, &commit, &popValue)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be null. */
	array = SJME_AS_JARRAY(arrayValue.v.l);
	if (array == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Make sure the array is actually valid. */
	componentType = sjme_atomic_g(sjme_jclass, 
		&SJME_AO_C(array)->componentType);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!SJME_AO_C(array)->info->isArray ||
		componentType == NULL || componentType->arrayTypeId != arrayType ||
		popValue.t != componentType->typeId)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check bounds. */
	index = indexValue.v.i;
	if (index < 0 || index >= array->e.length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS);

	/* Store value into the array. */
	if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
		&array->e, index, &commit,
		SJME_VLS_JVALUE_TYPED_P(&popValue))))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
		
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()
