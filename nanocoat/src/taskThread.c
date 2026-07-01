/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/stdGone.h"

#if defined(SJME_CONFIG_HAS_LOW_MEMORY)
	/** The size of the thread stack. */
	#define SJME_NVM_THREAD_STACK_SIZE 8192
#else
	/** The size of the thread stack. */
	#define SJME_NVM_THREAD_STACK_SIZE 65536
#endif

static sjme_errorCode sjme_nvm_task_stackReframe(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_class_methodInfo targetInfo)
{
	sjme_errorCode error;
	sjme_nvm_class_codeInfo code;
	sjme_frame_threadStacks* store;
	sjme_frame_frameStacks* stack;
	sjme_nvm_class_codePerType* perType;
	sjme_frame_frameStack* typeStack;
	sjme_jint i, setSize;
	sjme_intPointer typeOff[SJME_NVM_STACK_FINAL_ID + 1];
	sjme_pointer storageBase;
	sjme_jint storageClaim;
	
	if (inState == NULL || inThread == NULL || inFrame == NULL ||
		targetInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get source and target framing information. */
	store = &inThread->stack;
	stack = &inFrame->stack;

	/* Make sure it is cleared beforehand as everything needs to be */
	/* reinitialized correctly. */
	memset(stack, 0, sizeof(*stack));

	/* The ordering information can be taken directly from the code info. */
	/* Allocate extra space for object check values. */
	code = targetInfo->code;
	stack->orderFront = code->perType[SJME_NVM_CODE_INFO_ALL_TYPES].locals;
	stack->orderTop = stack->orderFront;
	stack->orderLength = stack->orderFront +
		code->perType[SJME_NVM_CODE_INFO_ALL_TYPES].stack;

	/* Determine initial offset to store ordering information. */
	memset(typeOff, 0, sizeof(typeOff));
	typeOff[0] = sjme_util_alignTo(
		sizeof(*stack->order) * stack->orderLength,
		sizeof(sjme_pointer));

	/* Determine the totals for each type. */
	for (i = 0; i < SJME_NVM_STACK_FINAL_ID; i++)
	{
		/* What is the per-type info? */
		perType = &code->perType[i];

		/* Obtain this stack. */
		typeStack = &stack->stack[i];

		/* Determine totals for per types. */
		typeStack->front = perType->locals;
		typeStack->top = typeStack->front;
		typeStack->length = typeStack->front + perType->stack;
		
		/* Determine the size of this set. */
		setSize = -1;
		if (sjme_error_is(error = sjme_nvm_vmField_sizeValueSet(&setSize,
			i, typeStack->length)) || setSize < 0)
			return sjme_error_default(error);

		/* The offset for the next type is the total storage for this type. */
		typeOff[i + 1] = sjme_util_alignTo(typeOff[i],
				sjme_nvm_typeMul[i]) +
			sjme_util_alignTo(setSize, sizeof(sjme_pointer));
	}

	/* Is there enough memory to even allocate this big of a stack? */
	if (store->storageTop +
		typeOff[SJME_NVM_STACK_FINAL_ID] > store->storageLen)
		return sjme_error_vmError(inThread, SJME_ERROR_STACK_OVERFLOW);

	/* Grab a chunk of the stack. */
	storageBase = SJME_POINTER_OFFSET(store->storage, store->storageTop);
	storageClaim = typeOff[SJME_NVM_STACK_FINAL_ID];
	store->storageTop += storageClaim;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_emitB("STACK RF %p: %p[%d] (rel %d + %d = %d)",
		stack, storageBase, storageClaim,
		(sjme_jint)((sjme_intPointer)storageBase -
			(sjme_intPointer)store->storage),
		storageClaim, store->storageTop);
#endif

	/* Clear any data which used to be here, since it could be garbage. */
	stack->storageBase = storageBase;
	stack->storageClaim = storageClaim;
	memset(storageBase, 0, storageClaim);

	/* Setup pointers. */
	stack->order = SJME_POINTER_OFFSET(storageBase, 0);
	for (i = 0; i < SJME_NVM_STACK_FINAL_ID; i++)
	{
		/* Determine base pointer. */
		stack->stack[i].set = SJME_POINTER_OFFSET(storageBase, typeOff[i]);
		
		/* Set the set details. */
		stack->stack[i].set->type = i;
		stack->stack[i].set->length = stack->stack[i].length;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadEmit(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	...)
{
	sjme_errorCode error;
	va_list args;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start arguments. */
	va_start(args, message);
	
	/* Forward. */
	error = sjme_nvm_task_threadEmitV(inThread, commonClass, cause,
		message, args);
	
	/* End. */
	va_end(args);
	
	/* Return the result of the forward. */
	return error;
}

sjme_errorCode sjme_nvm_task_threadEmitV(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonClass,
	sjme_attrInNullable sjme_jthrowable cause,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	sjme_attrInValue va_list args)
{
#define BUF_SIZE 256
	sjme_errorCode error;
	sjme_jclass tossClass, throwableClass;
	sjme_jthrowable toss, oldTossed;
	sjme_jfieldID initCauseID, causeID;
	sjme_nvm_value* accessor;
	sjme_jvalueTyped argV[1];
	sjme_cchar buf[BUF_SIZE];
	va_list copy;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Locate the class to toss. */
	tossClass = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(inThread,
		commonClass, &tossClass, SJME_JNI_TRUE)) || tossClass == NULL)
		return sjme_error_default(error);

	/* Allocate new instance of object. */
	toss = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(inThread,
		-1, SJME_NVM_STRUCT_OBJECT_INSTANCE,
		SJME_AS_JOBJECTP(&toss), tossClass)) || toss == NULL)
		return sjme_error_default(error);

	/* Set as tossed! */
	oldTossed = NULL;
	if (!sjme_atomic_cs(sjme_jobject, &inThread->tossed,
		NULL, SJME_AS_JOBJECT(toss)))
	{
		/* Get the old tossed to print it out. */
		oldTossed = SJME_AS_JTHROWABLE(
			sjme_atomic_g(sjme_jobject, &inThread->tossed));

		/* Print it out, assuming it is not our cause. */
		if (oldTossed != cause)
		{
			sjme_message("DROPPING DOUBLE TOSSED EXCEPTION:");
			if (sjme_error_is(error = sjme_nvm_task_stackTraceThrowable(
				inThread, SJME_AS_JTHROWABLE(oldTossed))))
				return sjme_error_default(error);
		}

		/* Set new value. */
		sjme_atomic_s(sjme_jobject, &inThread->tossed,
			SJME_AS_JOBJECT(toss));

		/* Count down the old toss since it is no longer stored, if it */
		/* is not our cause. */
		if (oldTossed != cause)
		{
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				SJME_AS_JOBJECT(oldTossed))))
				return sjme_error_default(error);
		}
	}

	/* Set level so we can run the constructor. */
	sjme_atomic_s(sjme_jint, &inThread->tossedLevel, inThread->numFrames);

	/* Build message string. */
	va_copy(copy, args);

	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, BUF_SIZE - 1, message, copy);
	buf[BUF_SIZE - 1] = '\0';
	
	va_end(copy);

	/* Setup string argument. */
	memset(&argV, 0, sizeof(argV));
	argV[0].t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
		inThread, SJME_AS_JSTRINGP(&argV[0].v.l), SJME_JNI_FALSE, buf)))
		return sjme_error_default(error);

	/* Initialize exception. */
	if (sjme_error_is(error = sjme_nvm_instance_defaultInit(inThread,
		NULL, SJME_AS_JOBJECT(toss),
		"(Ljava/lang/String;)V", 1, argV)))
		return sjme_error_default(error);

	/* Count up once as it is now stored in tossed. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(toss))))
		return sjme_error_default(error);

	/* Initialize cause, if there is one to set. */
	if (cause != NULL)
	{
		/* Locate the throwable class. */
		throwableClass = NULL;
		if (sjme_error_is(error = sjme_nvm_task_commonClass(inThread,
			SJME_NVM_COMMON_THROWABLE, &throwableClass,
			SJME_JNI_TRUE)))
			return sjme_error_default(error);

		/* Lookup initCause to flag it on. */
		initCauseID = NULL;
		if (sjme_error_is(error = sjme_nvm_vmField_idByNameTypeU(
			throwableClass, inThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE,
			"_initCause", "Z",
			&initCauseID)) || initCauseID == NULL)
			return sjme_error_default(error);

		/* Lookup cause, to set it. */
		causeID = NULL;
		if (sjme_error_is(error = sjme_nvm_vmField_idByNameTypeU(
			throwableClass, inThread, SJME_NVM_CLASS_MEMBER_INSTANCE,
			SJME_JNI_TRUE,
			"_cause", "Ljava/lang/Throwable;",
			&causeID)) || causeID == NULL)
			return sjme_error_default(error);

		/* Flag cause as initialized. */
		accessor = sjme_nvm_instance_fieldAccessor(
			SJME_AS_JOBJECT(toss), initCauseID);
		accessor->v.z = SJME_JNI_TRUE;

		/* Set actual cause now, and its check value directly. */
		accessor = sjme_nvm_instance_fieldAccessor(
			SJME_AS_JOBJECT(toss), causeID);
		if (sjme_error_is(error = sjme_nvm_vmField_cisSet(
			accessor, causeID->info->basicType,
			NULL, SJME_VLS_JOBJECT(toss))))
			return sjme_error_vmError(inThread, error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
#undef BUF_SIZE
}

sjme_errorCode sjme_nvm_task_threadEnter(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jmethodID inMethod,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_nvm_class_methodInfo targetInfo;
	sjme_jint i, n, dx;
	sjme_nvm_frame result;
	sjme_jboolean isStatic;
	sjme_jvalueTyped* argVParam;
	sjme_nvm_frame_gcCommit commit;
#if defined(SJME_CONFIG_DEBUG)
#define ARG_BUF_SIZE 128
	sjme_jint argBufLen;
	sjme_cchar argBuf[ARG_BUF_SIZE];
#endif
	
	if (inThread == NULL || outFrame == NULL || inMethod == NULL ||
		(argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (callType < 0 || callType >= SJME_NVM_NUM_METHOD_CALL_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Cannot a new frame if terminating. */
	if (sjme_atomic_g(sjme_jint, &SJME_T_K(inThread)->terminate) !=
		SJME_NVM_TERMINATE_NOT)
		return SJME_ERROR_INVALID_THREAD_STATE;
	
	/* Recover target info. */
	targetInfo = inMethod->info[callType];
	if (targetInfo == NULL)
		return sjme_error_vmError(inThread, SJME_ERROR_UNBOUND_METHOD);

	/* No code loaded? */
	if (targetInfo->code == NULL)
		return sjme_error_vmError(inThread, SJME_ERROR_PURE_VIRTUAL_CALL);

	/* Is the target static? */
	isStatic = SJME_NVM_ACC_IS(targetInfo->flags, STATIC);
	if (isStatic && callType != SJME_NVM_CALL_NON_VIRTUAL)
		return sjme_error_vmError(inThread,
			SJME_ERROR_CLASS_CHANGED);
	
	/* Argument count mismatch? */
	if (argC != targetInfo->argC + (!isStatic ? 1 : 0))
		return sjme_error_vmError(inThread,
			SJME_ERROR_ARGUMENT_COUNT_MISMATCH);

	/* Argument type mismatch? */
	if (argV != NULL)
	{
		argVParam = (!isStatic ? &argV[1] : argV);
		for (i = 0, n = targetInfo->argC; i < n; i++)
			if (argVParam[i].t != targetInfo->argT[i])
				return sjme_error_vmError(inThread,
					SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
	}

	/* If non-static, first must be a valid object. */
	if (!isStatic)
		if (argC == 0 || argV[0].t != SJME_JAVA_TYPE_ID_OBJECT ||
			argV[0].v.l == NULL)
			return sjme_error_vmError(inThread,
				SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
	
	/* Grab a frame from the thread's frame pool. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadFrameNext(
		inThread, &result)) || result == NULL)
		return sjme_error_vmError(inThread, error);

	/* Perform stack and thread re-framing. */
	if (sjme_error_is(error = sjme_nvm_task_stackReframe(
		sjme_atomic_g(sjme_nvm, &inThread->inState),
		inThread, result, targetInfo)))
		return sjme_error_vmError(inThread, error);

	/* Set frame details, needed for local set. */
	result->inClass = sjme_weakUp(sjme_atomic_g(sjme_jclass,
		&inMethod->member.inClass));
	result->id = sjme_atomic_ga(sjme_jint, 
		&SJME_T_K(inThread)->nextFrameId, 1) + 1;
	result->index = inThread->numFrames;
	result->inMethod = sjme_weakUp(inMethod);
	sjme_atomic_s(sjme_nvm, &result->inState, SJME_F_S(inThread));
	sjme_atomic_s(sjme_nvm_thread, &result->inThread, inThread);
	sjme_atomic_s(sjme_nvm_task, &result->inTask, SJME_T_K(inThread));
	result->inCode = sjme_weakUp(targetInfo->code);
	result->pool = sjme_atomic_g(sjme_nvm_class_info,
		&sjme_atomic_g(sjme_nvm_class_methodInfo,
			&targetInfo->code->inMethod)->inClass)->pool;

	/* If static, refer to the class, otherwise refer to the instance. */
	if (SJME_NVM_ACC_IS(inMethod->flags, STATIC))
		result->instance = SJME_AS_JOBJECT(result->inClass);
	else if (argV != NULL)
		result->instance = argV[0].v.l;
	else
		return SJME_ERROR_ILLEGAL_STATE;

	/* The instance is always counted up, as this frame now refers to it. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(result->instance)))
		return sjme_error_default(error);

	/* Used for final field setting. */
	result->flags |= (targetInfo->bits & SJME_NVM_CLASS_INIT_ANY);

	/* Link to parent. */
	if (inThread->numFrames == 0)
		sjme_atomic_s(sjme_nvm_frame, &result->parent, NULL);
	else
		sjme_atomic_s(sjme_nvm_frame, &result->parent,
			inThread->frames->elements[inThread->numFrames - 1]);

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Setup initial locals, which are copied in from arguments. */
	if (argV != NULL)
		for (i = 0, dx = 0, n = argC; i < n; i++, dx++)
		{
			/* Set local value. */
			if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
				result, &commit, dx, &argV[i])))
				return sjme_error_vmError(inThread, error);
			
			/* Move wide values up twice. */
			if (argV[i].t == SJME_JAVA_TYPE_ID_LONG ||
				argV[i].t == SJME_JAVA_TYPE_ID_DOUBLE)
				dx++;
		}
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(result, &commit)))
		return sjme_error_vmError(result, error);
	
	/* Set frame as active. */
	inThread->numFrames++;

#if defined(SJME_CONFIG_DEBUG_ENTRY)
	/* Print out arguments. */
	memset(argBuf, 0, sizeof(argBuf));
	for (i = 0; i < argC && argV != NULL; i++)
	{
		/* Out of room? */
		argBufLen = strlen(argBuf);
		if (argBufLen >= ARG_BUF_SIZE - 1)
			break;
		
		/* Comma? */
		if (i > 0)
			snprintf(&argBuf[argBufLen],
				(ARG_BUF_SIZE - argBufLen) - 1,
				", ");
		
		/* Out of room? */
		argBufLen = strlen(argBuf);
		if (argBufLen >= ARG_BUF_SIZE - 1)
			break;

		/* A value. */
		if (argV[i].t == SJME_JAVA_TYPE_ID_INTEGER)
			snprintf(&argBuf[argBufLen],
				(ARG_BUF_SIZE - argBufLen) - 1,
				"%d", argV[i].v.i);
		else if (argV[i].t == SJME_JAVA_TYPE_ID_LONG)
			snprintf(&argBuf[argBufLen],
				(ARG_BUF_SIZE - argBufLen) - 1,
				"%"PRId64, argV[i].v.j.full);
		else if (argV[i].t == SJME_JAVA_TYPE_ID_OBJECT)
			snprintf(&argBuf[argBufLen],
				(ARG_BUF_SIZE - argBufLen) - 1,
				"%p", (void*)argV[i].v.l);
		else
			snprintf(&argBuf[argBufLen],
				(ARG_BUF_SIZE - argBufLen) - 1,
				"?");
	}
	
	/* Emit. */
	sjme_messageB("ENTER (+%d) %s.%s %s: (%s)",
		inThread->numFrames,
		sjme_charSeq_tempUtf(sjme_atomic_g(sjme_nvm_class_info,
			&targetInfo->inClass)->name->seq),
		sjme_charSeq_tempUtf(targetInfo->name->seq),
		sjme_charSeq_tempUtf(targetInfo->type->seq),
		argBuf);
#undef ARG_BUF_SIZE
#endif

	/* Success! */
	*outFrame = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadEnterA(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_lpcstr inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_nvm_task inTask;
	sjme_jclass foundClass;
	sjme_charSeqStatic classSeq, nameSeq, typeSeq;
	
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* There must be a task. */
	inTask = sjme_atomic_g(sjme_nvm_task, &inThread->inTask);
	if (inTask == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Wrap in sequences. */
	memset(&classSeq, 0, sizeof(classSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&classSeq,
		inClass, 0, -1)))
		return sjme_error_default(error);
	memset(&nameSeq, 0, sizeof(classSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&nameSeq,
		inName, 0, -1)))
		return sjme_error_default(error);
	memset(&typeSeq, 0, sizeof(classSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&typeSeq,
		inType, 0, -1)))
		return sjme_error_default(error);

	/* Need to find the class first. */
	foundClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inTask->classLoader, &foundClass,
		inThread, &classSeq, SJME_JNI_TRUE)))
		return sjme_error_vmError(inThread, error);
	
	/* Forward to other call. */
	return sjme_nvm_task_threadEnterC(
		inThread, outFrame, foundClass, instanceType,
		&nameSeq, &typeSeq, argC, argV);
}

sjme_errorCode sjme_nvm_task_threadEnterC(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_charSeq inName,
	sjme_attrInNotNull sjme_charSeq inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_jmethodID id;
	
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Locate method to execute, since we are calling it, it is required. */
	id = NULL;
	if (sjme_error_is(error = sjme_nvm_vmMethod_idByNameType(
		inClass, inThread, instanceType, SJME_JNI_TRUE, inName,
		inType, &id)) || id == NULL)
		return sjme_error_vmError(inThread, error);
	
	/* Forward to implementation. */
	return sjme_nvm_task_threadEnter(inThread, outFrame,
		id, SJME_NVM_CALL_NON_VIRTUAL, argC, argV);
}

sjme_errorCode sjme_nvm_task_threadFrameNext(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame)
{
#define SJME_NVM_FRAME_GROW_SIZE 8
	sjme_errorCode error;
	sjme_nvm_frame result;
	sjme_nvm_frameBase blank;
	
	if (inThread == NULL || outFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to allocate more frames? */
	if (inThread->frames == NULL ||
		inThread->numFrames >= inThread->frames->length)
		if (sjme_error_default(error = sjme_list_replace(
			SJME_F_S(inThread)->allocPool,
			inThread->numFrames + SJME_NVM_FRAME_GROW_SIZE,
			&inThread->frames, sjme_nvm_frame, 0)))
			return sjme_error_default(error);
	
	/* "Pop" and init/clear frame. */
	result = inThread->frames->elements[inThread->numFrames];
	if (result != NULL)
	{
		/* Initialize a blank which always keeps the common info. */
		memset(&blank, 0, sizeof(blank));
		memmove(&blank.common, &result->common, sizeof(blank.common));
		
		/* Use this resultant blank, keeping the common areas. */
		memmove(result, &blank, sizeof(*result));
	}
	else
	{
		/* Allocate new blank frame. */
		if (sjme_error_is(error = sjme_nvm_alloc(
			sjme_atomic_g(sjme_nvm, &inThread->inState),
			sizeof(*result), SJME_NVM_STRUCT_FRAME,
			SJME_AS_NVM_COMMONP(&result))) || result == NULL)
			return sjme_error_default(error);

		/* Store in this slot, forever. */
		inThread->frames->elements[inThread->numFrames] = result;
	}

	/* Success! */
	*outFrame = result;
	return SJME_ERROR_NONE;
#undef SJME_NVM_FRAME_GROW_SIZE
}

sjme_errorCode sjme_nvm_task_threadInterrupt(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_nvm inState;
	sjme_errorCode error;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Set interrupt flag. */
	sjme_atomic_s(sjme_jint, &inThread->interrupted, 1);

	/* Schedule the thread, if not multithreaded. */
	inState = SJME_T_S(inThread);
	if (inState->threadModel != SJME_NVM_MLE_THREAD_MULTI)
	{
		/* Schedule in the thread. */
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleIn(inState,
			inThread)))
			return sjme_error_default(error);
	}

	/* Otherwise, attempt waking it, if possible. */
	else
	{
		/* Attempt thread wake. */
		if (sjme_error_is(error = sjme_thread_wake(
			sjme_atomic_g(sjme_thread, &inThread->nativeThread))))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadInterruptCheck(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInValue sjme_jboolean clear)
{
	sjme_jboolean set;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear or just get the value? */
	if (clear)
		set = sjme_atomic_cs(sjme_jint, &inThread->interrupted, 1, 0);
	else
		set = (sjme_atomic_g(sjme_jint, &inThread->interrupted) != 0);

	/* If this was set, then return interrupted, otherwise not. */
	if (set)
		return SJME_ERROR_INTERRUPTED;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadLeave(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_frame topFrame;
	sjme_jint topIndex;
	sjme_nvm_frameBase blank;
	sjme_nvm_frame_gcCommit commit;
	sjme_jobject uncaught;
	const sjme_nvm_stateHooks* hooks;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot pop a frame when there is nothing. */
	topIndex = inThread->numFrames - 1;
	if (topIndex <= -1)
		return SJME_ERROR_INVALID_THREAD_STATE;
	
#if defined(SJME_CONFIG_DEBUG_ENTRY)
	/* DEBUG. */
	sjme_emitB("LEAVE (-%d): ",
		inThread->numFrames);
#endif

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Clear the stack. */
	topFrame = inThread->frames->elements[topIndex];
	if (sjme_error_is(error = sjme_nvm_task_frameStackClear(topFrame,
		&commit)))
		return sjme_error_vmError(inThread, error);

	/* Clear locals as well. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalClear(topFrame,
		&commit)))
		return sjme_error_vmError(inThread, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(topFrame, &commit)))
		return sjme_error_vmError(topFrame, error);

	/* Clear reference to current class. */
	if (sjme_error_is(error = sjme_nvm_instance_countDown(
		SJME_AS_JOBJECT(topFrame->inClass))))
		return sjme_error_vmError(topFrame, error);

	/* Clear reference to bytecode. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(topFrame->inCode))))
		return sjme_error_vmError(topFrame, error);

	/* Clear reference to used method. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(topFrame->inMethod))))
		return sjme_error_vmError(topFrame, error);

	/* Count down reference to object/class instance. */
	if (sjme_error_is(error = sjme_nvm_instance_countDown(topFrame->instance)))
		return sjme_error_vmError(topFrame, error);

	/* Make the top-most frame  not exist. */
	inThread->numFrames = topIndex;

	/* Reduce the storage claim to free it up. */
	inThread->stack.storageTop -= topFrame->stack.storageClaim;
	
	/* Clear the frame to a blank state. */
	memset(&blank, 0, sizeof(blank));
	memmove(&blank.common, &topFrame->common, sizeof(blank.common));
	
	/* Use this resultant blank, keeping the common areas. */
	memmove(topFrame, &blank, sizeof(*topFrame));
	
	/* If this is the last frame, the thread will be terminating unless */
	/* it is considered a callback thread. */
	if (topIndex == 0)
	{
		/* Set as finishing. */
		sjme_atomic_cs(sjme_nvm_thread_startType, &inThread->start,
			SJME_NVM_THREAD_START_STANDARD,
			SJME_NVM_THREAD_START_FINISHING);

		/* Force schedule the thread, so cleanup is called. */
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleIn(
			SJME_T_S(inThread), inThread)))
			return sjme_error_vmError(inThread, error);
		
		/* There is still an uncaught exception? */
		uncaught = sjme_atomic_g(sjme_jobject, &inThread->tossed);
		if (uncaught != NULL)
		{
			/* Print it out. */
			if (sjme_error_is(error = sjme_nvm_task_stackTraceThrowable(
				inThread, (sjme_jthrowable)uncaught)))
				sjme_message("Uncaught throwable, print error %d",
					error);
			
			/* Is there a special handler for this? */
			hooks = SJME_T_S(inThread)->hooks;
			if (hooks != NULL && hooks->uncaught != NULL)
				if (sjme_error_is(error = hooks->uncaught(inThread,
					(sjme_jthrowable)uncaught)))
					return sjme_error_vmError(inThread, error);

			/* Clear it. */
			sjme_atomic_cs(sjme_jobject, &inThread->tossed,
				uncaught, NULL);
			sjme_atomic_s(sjme_jint, &inThread->tossedLevel, -1);

			/* Count it down. */
			if (sjme_error_is(error = sjme_nvm_instance_countDown(uncaught)))
				return sjme_error_vmError(inThread, error);
			
			/* Fail. */
			return sjme_error_vmError(inThread,
				SJME_ERROR_UNCAUGHT_EXCEPTION);
		}
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadNew(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNotNull sjme_nvm_thread* outThread,
	sjme_attrInNotNull sjme_lpcstr threadName,
	sjme_attrInValue sjme_jboolean isMain)
{
	sjme_errorCode error;
	sjme_nvm_thread result;
	sjme_nvm_frame firstFrame;
	sjme_nvm inState;
	sjme_jint freeSlot, i, n;
	sjme_pointer storage;
	sjme_jclass threadType;
	
	if (inTask == NULL || outThread == NULL || threadName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot start a new thread if terminating. */
	if (sjme_atomic_g(sjme_jint, &inTask->terminate) !=
		SJME_NVM_TERMINATE_NOT)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Allocate stack storage. */
	storage = NULL;
	inState = SJME_T_S(inTask);
	if (sjme_error_is(error = sjme_alloc(inState->allocPool,
		SJME_NVM_THREAD_STACK_SIZE, &storage)) || storage == NULL)
		goto fail_allocStorage;
	
	/* Allocate thread structure. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState, sizeof(*result),
		SJME_NVM_STRUCT_BRACKET_VM_THREAD_INSTANCE,
		SJME_AS_NVM_COMMONP(&result))))
		goto fail_allocResult;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inTask->object.common.lock)))
		goto fail_lock;
	
	/* Find free slot in the thread list. */
	freeSlot = -1;
	for (i = 0, n = inTask->threads->length; i < n; i++)
		if (inTask->threads->elements[i] == NULL)
		{
			freeSlot = i;
			break;
		}
	
	/* Need to grow the list? */
	if (freeSlot < 0)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Fill out basic details. */
	sjme_atomic_s(sjme_nvm, &result->inState, inState);
	sjme_atomic_s(sjme_nvm_task, &result->inTask, inTask);
	result->threadId = 1 + sjme_atomic_ga(sjme_jint, 
		&inState->nextThreadId, 1);
	result->object.identityHash =
		sjme_nvm_instance_calcIdentityHash(inTask, result);
	result->stack.storage = storage;
	result->stack.storageLen = SJME_NVM_THREAD_STACK_SIZE;
	
	/* All new threads are considered initially sleeping. */
	sjme_atomic_s(sjme_nvm_thread_statusType, &result->status,
		SJME_NVM_THREAD_STATUS_SLEEPING);
	
	/* Soft load the VM thread bracket class. */
	threadType = NULL;
	if (sjme_error_is(error = sjme_nvm_task_commonClass(result,
		SJME_NVM_COMMON_VM_THREAD,
		&threadType,
		SJME_JNI_FALSE)) || threadType == NULL)
		goto fail_loadThreadClass;
	sjme_atomic_s(sjme_jclass, &result->object.isClass,
		sjme_weakUp(threadType));
	
	/* All threads have an initial frame within java.lang.__Start__. */
	firstFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnterA(
		result, &firstFrame,
		"java/lang/__Start__",
		SJME_NVM_CLASS_MEMBER_STATIC,
		"__main", "()V",
		0, NULL)))
		goto fail_enterFrame;
	
	/* Count up. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(result))))
		goto fail_countUp;
	
	/* Store thread for future referencing. */
	inTask->threads->elements[freeSlot] = result;

	/* Increase task thread count, for both all and normal. Normal gets */
	/* an add because a thread gets daemon being set later. */
	sjme_atomic_ga(sjme_jint, 
		&inTask->numThreads[SJME_NVM_THREAD_COUNT_ALL], 1);
	sjme_atomic_ga(sjme_jint, 
		&inTask->numThreads[SJME_NVM_THREAD_COUNT_NORMAL], 1);
	
	/* The main thread gets flagged as the main thread. */
	if (isMain)
	{
		/* Set the main thread, if not set. */
		if (sjme_atomic_cs(sjme_nvm_thread, 
			&inTask->globals.mainThread, NULL, result))
		{
			/* Record that this is the actual main thread. */
			result->isMain = SJME_JNI_TRUE;

			/* Make sure the count is just one. */
			sjme_atomic_ga(sjme_jint, 
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_MAIN], 1);
		}
	}
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->object.common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outThread = result;
	return SJME_ERROR_NONE;
	
fail_countUp:
fail_enterFrame:
	if (firstFrame != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(firstFrame));
	
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inTask->object.common.lock, NULL));
fail_lock:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
fail_allocStorage:
	sjme_alloc_free(storage);

fail_loadThreadClass:
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_threadStart(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There must be frames. */
	if (inThread->numFrames <= 0)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Threads can only be started once! */
	if (!sjme_atomic_cs(sjme_nvm_thread_startType, &inThread->start,
		SJME_NVM_THREAD_START_NEVER,
		SJME_NVM_THREAD_START_STANDARD))
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Set to be in the run state. */
	sjme_atomic_s(sjme_nvm_thread_statusType, &inThread->status,
		SJME_NVM_THREAD_STATUS_RUNNING);

	/* Schedule the thread for execution. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleIn(
		SJME_T_S(inThread), inThread)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadStringValueOfCS(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNullable sjme_nvm_stringPool_string refString,
	sjme_attrInNotNull sjme_charSeq inSeq)
{
#define SJME_INTERN_GROW 32
	sjme_errorCode error;
	sjme_nvm_taskStrings strings;
	sjme_list(sjme_jstring)* interns;
	sjme_jstring* blankIntern;
	sjme_jstring result;
	sjme_jint hash, length, i, n;
	sjme_charSeq strSeq;
	
	if (inThread == NULL || outString == NULL || inSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Passed reference string, but it is the wrong one? */
	if (refString != NULL && refString->seq != inSeq)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Calculate the hash/length of the string. */
	hash = 0;
	if (sjme_error_is(error = sjme_charSeq_hash(inSeq, &hash)))
		return sjme_error_default(error);

	length = 0;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &length)))
		return sjme_error_default(error);

	/* If interned, we need to lock on all the strings. */
	strings = SJME_T_K(inThread)->strings;
	interns = strings->interns;
	blankIntern = NULL;
	if (isIntern)
	{
		/* Lock on the interned strings. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&strings->common.lock)))
			return sjme_error_default(error);

		/* See if there are any potential string matches. */
		if (interns != NULL)
			for (i = 0, n = interns->length; i < n; i++)
			{
				/* Ignore blank strings. */
				result = interns->elements[i];
				if (result == NULL)
				{
					if (blankIntern == NULL)
						blankIntern = &interns->elements[i];
					continue;
				}

				/* Different hash/length? Ignore. */
				if (hash != result->intern.hashCode ||
					length != result->intern.length)
					continue;
				
				/* Release. */
				if (sjme_error_is(error = sjme_thread_spinLockRelease(
					&strings->common.lock, NULL)))
					return sjme_error_default(error);

				/* Already exists, so return it! */
				*outString = result;
				return SJME_ERROR_NONE;
			}
	}

	/* Setup string object. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(inThread,
		sizeof(*result), SJME_NVM_STRUCT_STRING_INSTANCE,
		SJME_AS_JOBJECTP(&result),
		sjme_nvm_task_commonClassR(inThread,
			SJME_NVM_COMMON_STRING))) ||
		result == NULL)
		goto fail_allocStringInstance;

	/* Set string properties. */
	result->object.identityHash = hash;
	result->intern.hashCode = hash;
	result->intern.length = length;

	/* Comes from a string pool string? */
	if (refString != NULL)
	{
		/* Set sequence from pool string. */
		sjme_atomic_cs(sjme_nvm_stringPool_string, &result->poolString,
			NULL, sjme_weakUp(refString));
		sjme_atomic_cs(sjme_charSeq, &result->seq,
			NULL, refString->seq);
	}

	/* Otherwise, comes from a sequence directly. */
	else
	{
		/* Duplicate the sequence. */
		strSeq = NULL;
		if (sjme_error_is(error = sjme_charSeq_dup(
			SJME_F_S(inThread)->allocPool, &strSeq, inSeq)) || strSeq == NULL)
			goto fail_dupSeq;

		/* Set. */
		if (!sjme_atomic_cs(sjme_charSeq, &result->seq, NULL, strSeq))
			goto fail_collided;
	}
	
	/* Final intern setup. */
	if (isIntern)
	{
		/* Need to grow the intern list? */
		if (blankIntern == NULL)
		{
			/* Reallocate list. */
			n = (interns == NULL ? 0 : interns->length);
			if (sjme_error_is(error = sjme_list_replace(
				SJME_T_S(inThread)->allocPool,
				n + SJME_INTERN_GROW,
				&strings->interns,
				sjme_jstring, 0)) || strings->interns == NULL)
				goto fail_replaceList;

			/* Place at end. */
			interns = strings->interns;
			blankIntern = &interns->elements[n];
		}
		
		/* Set slot here. */
		*blankIntern = sjme_weakUpR(sjme_jstring, result);
		
		/* Release. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&strings->common.lock, NULL)))
			return sjme_error_default(error);
	}

	/* Success! */
	*outString = result;
	return SJME_ERROR_NONE;
	
fail_countInIntern:
	sjme_thread_spinLockRelease(&strings->common.lock, NULL);

	return sjme_error_default(error);

fail_collided:
fail_dupSeq:
	if (!isIntern && result != NULL)
		sjme_atomic_s(sjme_charSeq, &result->seq, NULL);
	if (strSeq != NULL)
		sjme_alloc_free(strSeq);
fail_replaceList:
fail_countPoolString:
fail_allocStringInstance:
	/* Do not destroy loaded intern strings. */
	if (!isIntern && result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));

	return sjme_error_default(error);
#undef SJME_INTERN_GROW
}

sjme_errorCode sjme_nvm_task_threadStringValueOfP(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInNotNull sjme_nvm_stringPool_string inPool)
{
	if (inThread == NULL || outString == NULL || inPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward implementation. */
	return sjme_nvm_task_threadStringValueOfCS(inThread,
		outString, SJME_JNI_TRUE, inPool, inPool->seq);
}

sjme_errorCode sjme_nvm_task_threadStringValueOfUtf(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNotNull sjme_lpcstr inUtf)
{
	sjme_errorCode error;
	sjme_charSeqStatic tempSeq;
	
	if (inThread == NULL || outString == NULL || inUtf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Setup static sequence. */
	memset(&tempSeq, 0, sizeof(tempSeq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&tempSeq,
		inUtf, 0, -1)))
		return sjme_error_default(error);

	/* Forward implementation. */
	return sjme_nvm_task_threadStringValueOfCS(inThread,
		outString, isIntern, NULL, &tempSeq);
}
