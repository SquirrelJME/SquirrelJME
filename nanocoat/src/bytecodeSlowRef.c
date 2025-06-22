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

static sjme_errorCode sjme_nvm_byteCode_slowInvoke(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInNotNull sjme_jmethodID methodId)
{
	sjme_errorCode error;
	sjme_nvm_frame newFrame;
	sjme_jint argC;
	sjme_jvalueTyped* argV;
	sjme_jvalueTyped* argVParam;
	sjme_jvalueTyped mleArgR;
	sjme_jboolean isStatic;
	sjme_nvm_class_methodInfo target;

	if (inFrame == NULL || methodId == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToM(
		inFrame, methodId)))
		return sjme_error_vmError(inFrame,
			sjme_error_defaultOr(error, SJME_ERROR_CLASS_CHANGED));

	/* Get the non-virtual target info. */
	target = methodId->info[callType];

	/* Static-ness is wrong? */
	isStatic = target->flags.member.isStatic;
	if (isStatic && instanceType != SJME_NVM_CLASS_MEMBER_STATIC &&
		callType != SJME_NVM_CALL_NON_VIRTUAL)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);
	
	/* Allocate pushed arguments. */
	argC = target->argC + (!isStatic ? 1 : 0);
	argV = sjme_alloca(sizeof(*argV) * (argC + 2));
	if (argV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Pull in stack arguments for the call. */
	argVParam = (!isStatic ? &argV[1] : argV);
	if (target->argC != 0)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(
			inFrame, target->argC, target->argT, argVParam)))
			return sjme_error_vmError(inFrame, error);

	/* Pop instance. */
	if (!isStatic)
	{
		/* Pop. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(
			inFrame, SJME_JAVA_TYPE_ID_OBJECT, &argV[0])))
			return sjme_error_vmError(inFrame, error);

		/* Cannot be null. */
		if (argV[0].v.l == NULL)
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_REFERENCE_POP);
		
		/* Must be the same or a compatible class as the call site. */
		if (!sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame),
			methodId->member.inClass,
			SJME_O_C(argV[0].v.l)))
			return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);
	}

	/* If native, perform an MLE call. */
	if (target->flags.native && isStatic)
	{
		/* Perform the native call. */
		memset(&mleArgR, 0, sizeof(mleArgR));
		mleArgR.t = SJME_JAVA_TYPE_ID_VOID;
		if (sjme_error_is(error = sjme_mle_mleCall(inFrame,
			target->inClass->name->seq,
			target->name->seq,
			target->type->seq,
			&mleArgR,
			argC, argV)))
		{
			/* MLECallError is a valid response. */
			if (error == SJME_ERROR_MLE_CALL)
				return SJME_ERROR_MLE_CALL;
			
#if defined(SJME_CONFIG_DEBUG)
			/* Unknown/Unimplemented method. */
			else if (error == SJME_ERROR_UNKNOWN_MLE_SHELF ||
				error == SJME_ERROR_UNKNOWN_MLE_FUNCTION)
			{
				sjme_message("Missing MLE: %s.%s %s",
					sjme_charSeq_tempUtf(target->inClass->name->seq),
					sjme_charSeq_tempUtf(target->name->seq),
					sjme_charSeq_tempUtf(target->type->seq));
				
				return sjme_error_vmError(inFrame, error);
			}
#endif

			/* Anything else is considered a failure. */
			return sjme_error_vmError(inFrame, error);
		}

		/* Wrong type? */
		if (mleArgR.t != target->argR)
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Is there a return value being pushed to the stack? */
		if (mleArgR.t != SJME_JAVA_TYPE_ID_VOID)
		{
			/* Count up if an object. */
			if (mleArgR.t == SJME_JAVA_TYPE_ID_OBJECT &&
				mleArgR.v.l != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(
					mleArgR.v.l, NULL)))
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
			SJME_F_T(inFrame),
			&newFrame,
			methodId,
			callType,
			argC, argV)) || newFrame == NULL)
			return sjme_error_vmError(inFrame, error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_BYTECODE_SLOW(ArrayLength)
{
	sjme_jarray array;
	sjme_jvalueTyped value, result;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Pop single object value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (value.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Must be an array type. */
	array = SJME_AS_JARRAY(value.v.l);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Push length onto the stack. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	result.v.i = array->length;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CheckCast)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jvalueTyped value;
	sjme_jvalueTyped instance;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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

	/* Pop object from the stack. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPeek(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* Not a match? */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b) */
	if (value.v.l != NULL &&
		!(SJME_O_C(value.v.l) == desireClass ||
		sjme_nvm_vmClass_isAssignableFrom(SJME_F_T(inFrame),
			desireClass, SJME_O_C(value.v.l))))
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InstanceAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalue* direct;
	sjme_jvalueTyped result;
	sjme_jvalueTyped instance;
	sjme_nvm_jfieldAccessFunc accessor;
	sjme_jboolean isPut;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_fieldIDByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not an instance field? */
	if (fieldId->flags.member.isStatic)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);

	/* Read in value to put. */
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (fieldId->flags.member.final)
		{
			/* Cannot write static final fields. */
			if (fieldId->flags.member.isStatic)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (fieldId->member.inClass != inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in an instance initializer. */
			if (!inFrame->flags.isInstanceInit)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		memset(&result, 0, sizeof(result));
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Obtain accessor for this field. */
	if (fieldId->accessor != NULL)
		accessor = fieldId->accessor;
	else
		accessor = SJME_F_K(inFrame)->globals.accessor;
	if (accessor == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_FIELD_NOT_DIRECT);

	/* Read instance to act on. */
	memset(&instance, 0, sizeof(instance));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &instance)))
		return sjme_error_vmError(inFrame, error);
	
	/* Direct access. */
	direct = accessor(instance.v.l, fieldId);
	if (direct == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_FIELD_NOT_DIRECT);

	/* Copy data over. */
	if (isPut)
		memmove(direct, &result.v, sjme_nvm_typeMul[fieldId->javaType]);
	else
		memmove(&result.v, direct, sjme_nvm_typeMul[fieldId->javaType]);
	
	/* Push result to the stack. */
	if (!isPut)
	{
		result.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &result)))
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InstanceOf)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jvalueTyped check, result;
	sjme_jclass desireClass;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Read in object to check. */
	memset(&check, 0, sizeof(check));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &check)))
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

	/* Is this the given class? */
	memset(&result, 0, sizeof(result));
	if (check.v.l == NULL)
		result.v.i = SJME_JNI_FALSE;
	else
		result.v.i = sjme_nvm_vmClass_isAssignableFrom(SJME_F_T(inFrame),
			desireClass, SJME_O_C(check.v.l));

	/* Push result to the stack. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InvokeInterface)
{
	sjme_jmethodID methodId;
	sjme_jint poolIndex, depth;
	sjme_nvm_class_poolEntryMember* methodRef;
	sjme_jvalueTyped depthRef;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
		&depthRef, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* It must be an object type. */
	if (depthRef.t != SJME_JAVA_TYPE_ID_OBJECT)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* It cannot be null. */
	if (depthRef.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);
	
	/* Lookup interface method. */
	methodId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByInterface(
		SJME_F_T(inFrame), SJME_JNI_TRUE, &methodId,
		depthRef.v.l, methodRef)) ||
		methodId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Perform the invocation. */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_NVM_CALL_VIRTUAL,
		methodId)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

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
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
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

	/* The target method needs to be found dynamically. */
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
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
		&rawOnThis, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* The instance object to call onto, cannot be null. */
	onThis = rawOnThis.v.l;
	if (onThis == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* These modify the action to be performed */
	inSameClass = (currentClass == refClass);
	inSuper = sjme_nvm_vmClass_isSuperClass(currentClass,
		refClass);
	isInit = refMethod->bits.isInstanceInit;
	isPrivate = SJME_M_AF(refMethod).private;
	isPackagePrivate = (!SJME_M_AF(refMethod).private &&
		!SJME_M_AF(refMethod).protected &&
		!SJME_M_AF(refMethod).public);
	
	/* Call superclass method instead? */
	if ((!isPrivate && !isPackagePrivate) && inSuper && !isInit)
	{
		if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
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
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_NVM_CALL_VIRTUAL,
		refMethod)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InvokeStatic)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jmethodID target;
	sjme_jclass refClass;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
	
	/* Lookup target method. */
	target = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		refClass, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq, &target)) ||
		target == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Perform the invocation. */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_NVM_CALL_NON_VIRTUAL,
		target)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InvokeVirtual)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jmethodID target;
	sjme_jclass refClass;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
	
	/* Lookup target method. */
	target = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		refClass, SJME_F_T(inFrame), SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq, &target)) ||
		target == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Perform the invocation. */
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvoke(inFrame,
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_NVM_CALL_VIRTUAL, target)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(Monitor)
{
	sjme_jboolean isExit;
	sjme_jvalueTyped instance;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Entry or exit? */
	isExit = (id == 195);

	/* Get the object we are accessing. */
	memset(&instance, 0, sizeof(instance));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &instance)))
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
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(New)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	sjme_jint poolIndex;
	sjme_jvalueTyped result;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	
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

	/* Allocate new instance of the given object. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(
		SJME_F_T(inFrame), -1, SJME_NVM_STRUCT_OBJECT_INSTANCE,
		&result.v.l, desireClass)) || result.v.l == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Push allocate class to the stack. */
	result.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NewArray)
{
	sjme_jvalueTyped length, array;
	sjme_basicTypeId arrayType;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &length)))
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
		SJME_F_T(inFrame), &array.v.l, arrayType, length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&array)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(NewArrayA)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass componentType;
	sjme_jvalueTyped length;
	sjme_jvalueTyped array;
	SJME_NVM_BYTECODE_SLOW_ENTRY;
	
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
	
	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &length)))
		return sjme_error_vmError(inFrame, error);

	/* Length is not valid. */
	if (length.v.i < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Create new array. */
	memset(&array, 0, sizeof(array));
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), &array.v.l, componentType, length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&array)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(StaticAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalue* direct;
	sjme_jvalueTyped result;
	sjme_nvm_jfieldAccessFunc accessor;
	sjme_jboolean isPut;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

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
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_fieldIDByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not a static field? */
	if (!fieldId->flags.member.isStatic)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);

	/* Obtain accessor for this field. */
	if (fieldId->accessor != NULL)
		accessor = fieldId->accessor;
	else
		accessor = SJME_F_K(inFrame)->globals.accessor;
	if (accessor == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_FIELD_NOT_DIRECT);
	
	/* Direct access. */
	direct = accessor(SJME_AS_JOBJECT(desireClass), fieldId);
	if (direct == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_FIELD_NOT_DIRECT);

	/* Read in value to put. */
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (fieldId->flags.member.final)
		{
			/* Cannot write instance final fields. */
			if (!fieldId->flags.member.isStatic)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (fieldId->member.inClass != inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in a static initializer. */
			if (!inFrame->flags.isStaticInit)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		memset(&result, 0, sizeof(result));
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Copy data over. */
	if (isPut)
		memmove(direct, &result.v, sjme_nvm_typeMul[fieldId->javaType]);
	else
		memmove(&result.v, direct, sjme_nvm_typeMul[fieldId->javaType]);
	
	/* Push result to the stack. */
	if (!isPut)
	{
		result.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &result)))
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XALoad)
{
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_jvalueTyped pushValue;
	sjme_jarray array;
	sjme_jint index;
	sjme_basicTypeId arrayType;
	sjme_jclass componentType;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Read in index and array. */
	memset(&indexValue, 0, sizeof(indexValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be null. */
	array = SJME_AS_JARRAY(arrayValue.v.l);
	if (array == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Make sure the array is actually valid. */
	arrayType = sjme_nvm_byteCode_xArrayType[id - 46];
	componentType = sjme_atomic_sjme_jclass_get(
		&array->object.isClass->componentType);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!array->object.isClass->info->isArray ||
		componentType == NULL || componentType->arrayTypeId != arrayType)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check bounds. */
	index = indexValue.v.i;
	if (index < 0 || index >= array->length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS);

	/* Load value to push. */
	memset(&pushValue, 0, sizeof(pushValue));
	pushValue.t = componentType->typeId;
	switch (componentType->arrayTypeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
			
		case SJME_BASIC_TYPE_ID_BYTE:
			pushValue.v.i =
				((sjme_jint)array->e.b[index]) & INT32_C(0xFF);
			if ((pushValue.v.i & INT32_C(0x80)) != 0)
				pushValue.v.i |= INT32_C(0xFFFFFF00);
			break;
			
		case SJME_BASIC_TYPE_ID_SHORT:
			pushValue.v.i =
				((sjme_jint)array->e.s[index]) & INT32_C(0xFFFF);
			if ((pushValue.v.i & INT32_C(0x8000)) != 0)
				pushValue.v.i |= INT32_C(0xFFFF0000);
			break;
			
		case SJME_BASIC_TYPE_ID_CHARACTER:
			pushValue.v.i =
				((sjme_jint)array->e.c[index]) & INT32_C(0xFFFF);
			break;
			
		case SJME_JAVA_TYPE_ID_INTEGER:
			pushValue.v.i = array->e.i[index];
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			pushValue.v.j = array->e.j[index];
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			pushValue.v.f = array->e.f[index];
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			pushValue.v.d = array->e.d[index];
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			pushValue.v.l = array->e.l[index];

			/* Count up if not null as it is now on the stack. */
			if (pushValue.v.l != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(
					pushValue.v.l, NULL)))
					return sjme_error_vmError(inFrame, error);
			break;

		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_STACK_INVALID_WRITE);
	}

	/* Push. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &pushValue)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

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
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* Determine the type to read from the stack and to store to the array. */
	arrayType = sjme_nvm_byteCode_xArrayType[id - 79];
	promoteType = sjme_nvm_typePromote[arrayType];

	/* Read in value, index, and array. */
	memset(&popValue, 0, sizeof(popValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		promoteType, &popValue)))
		return sjme_error_vmError(inFrame, error);
	memset(&indexValue, 0, sizeof(indexValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be null. */
	array = SJME_AS_JARRAY(arrayValue.v.l);
	if (array == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Make sure the array is actually valid. */
	componentType = sjme_atomic_sjme_jclass_get(
		&SJME_AO_C(array)->componentType);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!SJME_AO_C(array)->info->isArray ||
		componentType == NULL || componentType->arrayTypeId != arrayType ||
		popValue.t != componentType->typeId)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check bounds. */
	index = indexValue.v.i;
	if (index < 0 || index >= array->length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS);

	/* Store value into the array. */
	switch (componentType->arrayTypeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
			
		case SJME_BASIC_TYPE_ID_BYTE:
			array->e.b[index] = (sjme_jbyte)popValue.v.i;
			break;
			
		case SJME_BASIC_TYPE_ID_SHORT:
			array->e.s[index] = (sjme_jshort)popValue.v.i;
			break;
			
		case SJME_BASIC_TYPE_ID_CHARACTER:
			array->e.c[index] = (sjme_jchar)popValue.v.i;
			break;
			
		case SJME_JAVA_TYPE_ID_INTEGER:
			array->e.i[index] = popValue.v.i;
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			array->e.j[index] = popValue.v.j;
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			array->e.f[index] = popValue.v.f;
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			array->e.d[index] = popValue.v.d;
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			/* Count down if there is an old value. */
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				&array->e.l[index], popValue.v.l)))
				return sjme_error_vmError(inFrame, error);

			/* Set new value. */
			array->e.l[index] = popValue.v.l;
			break;

		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_STACK_INVALID_READ);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
