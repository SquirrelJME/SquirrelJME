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
	sjme_jint i;
	sjme_intPointer typeOff[SJME_NVM_STACK_FINAL_ID + 1];
	sjme_pointer storeBase;
	sjme_javaTypeId sizeAlias;
	
	if (inState == NULL || inThread == NULL || inFrame == NULL ||
		targetInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get source and target framing information. */
	store = &inThread->stack;
	stack = &inFrame->stack;

	/* Make sure it is cleared beforehand. */
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
		/* Fill in the object check stack. */
		if (i == SJME_NVM_STACK_OBJECT_CHECK_ID)
		{
			perType = &code->perType[SJME_JAVA_TYPE_ID_OBJECT];
			sizeAlias = SJME_JAVA_TYPE_ID_INTEGER;
		}

		/* Normal type set. */
		else
		{
			perType = &code->perType[i];
			sizeAlias = i;
		}

		/* Obtain this stack. */
		typeStack = &stack->stack[i];

		/* Determine totals for per types. */
		typeStack->front = perType->locals;
		typeStack->top = typeStack->front;
		typeStack->length = typeStack->front + perType->stack;

		/* The offset for the next type is the total storage for this type. */
		typeOff[i + 1] = sjme_util_alignTo(
			sjme_util_alignTo(typeOff[i],
				sjme_nvm_typeMul[sizeAlias]) +
			(sjme_nvm_typeMul[sizeAlias] * typeStack->length),
			sizeof(sjme_pointer));
	}

	/* Is there enough memory to even allocate this big of a stack? */
	if (store->storageTop +
		typeOff[SJME_NVM_STACK_FINAL_ID] > store->storageLen)
		return sjme_error_vmError(inThread, SJME_ERROR_STACK_OVERFLOW);

	/* Grab a chunk of the stack. */
	storeBase = SJME_POINTER_OFFSET(store->storage, store->storageTop);
	stack->storageClaim = typeOff[SJME_NVM_STACK_FINAL_ID];
	store->storageTop += typeOff[SJME_NVM_STACK_FINAL_ID];

	/* Clear any data which used to be here. */
	memset(storeBase, 0, typeOff[SJME_NVM_STACK_FINAL_ID]);

	/* Setup pointers. */
	stack->order = SJME_POINTER_OFFSET(storeBase, 0);
	for (i = 0; i < SJME_NVM_STACK_FINAL_ID; i++)
		stack->stack[i].base.base = SJME_POINTER_OFFSET(storeBase, typeOff[i]);

	/* Success! */
	return SJME_ERROR_NONE;
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
	
	if (inThread == NULL || outFrame == NULL || inMethod == NULL ||
		(argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (callType < 0 || callType >= SJME_NVM_NUM_METHOD_CALL_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Cannot a new frame if terminating. */
	if (sjme_atomic_sjme_jint_get(&SJME_T_K(inThread)->terminate) !=
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
	isStatic = targetInfo->flags.member.isStatic;
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
		inThread->inState, inThread, result, targetInfo)))
		return sjme_error_vmError(inThread, error);

	/* Set frame details, needed for local set. */
	result->inClass = inMethod->member.inClass;
	result->id = sjme_atomic_sjme_jint_getAdd(
		&SJME_T_K(inThread)->nextFrameId, 1) + 1;
	result->index = inThread->numFrames;
	result->inMethod = inMethod;
	result->inState = SJME_F_S(inThread);
	result->inThread = inThread;
	result->inTask = SJME_T_K(inThread);
	result->inCode = targetInfo->code;
	result->pool = targetInfo->code->inMethod->inClass->pool;

	/* If static, refer to the class, otherwise refer to the instance. */
	if (inMethod->flags.member.isStatic)
		result->instance = SJME_AS_JOBJECT(result->inClass);
	else if (argV != NULL)
		result->instance = argV[0].v.l;

	/* Used for final field setting. */
	result->flags.isStaticInit = targetInfo->bits.isStaticInit;
	result->flags.isInstanceInit = targetInfo->bits.isInstanceInit;

	/* Link to parent. */
	if (inThread->numFrames == 0)
		result->parent = NULL;
	else
		result->parent = inThread->frames->elements[inThread->numFrames - 1];
	
	/* Setup initial locals, which are copied in from arguments. */
	if (argV != NULL)
		for (i = 0, dx = 0, n = argC; i < n; i++, dx++)
		{
			/* Set local value. */
			if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
				result, dx, &argV[i])))
				return sjme_error_vmError(inThread, error);
			
			/* Move wide values up twice. */
			if (argV[i].t == SJME_JAVA_TYPE_ID_LONG ||
				argV[i].t == SJME_JAVA_TYPE_ID_DOUBLE)
				dx++;
		}
	
	/* Set frame as active. */
	inThread->numFrames++;

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
	inTask = inThread->inTask;
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
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
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
		if (sjme_error_is(error = sjme_nvm_alloc(inThread->inState,
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

sjme_errorCode sjme_nvm_task_threadLeave(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_frame topFrame;
	sjme_jint topIndex;
	sjme_nvm_frameBase blank;
	sjme_jobject uncaught;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot pop a frame when there is nothing. */
	topIndex = inThread->numFrames - 1;
	if (topIndex <= -1)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Cannot pop if anything is still committed. */
	topFrame = inThread->frames->elements[topIndex];
	if (topFrame->commit != NULL)
		return sjme_error_vmError(inThread, SJME_ERROR_ACTIVE_GC_COMMIT);

	/* Clear the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackClear(topFrame)))
		return sjme_error_vmError(inThread, error);

	/* Clear locals as well. */
	if (sjme_error_is(error = sjme_nvm_task_frameLocalClear(topFrame)))
		return sjme_error_vmError(inThread, error);

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
		sjme_atomic_sjme_jint_compareSet(&inThread->start,
			SJME_NVM_THREAD_START_STANDARD,
			SJME_NVM_THREAD_START_FINISHING);

		/* Force schedule the thread, so cleanup is called. */
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleIn(
			SJME_T_S(inThread), inThread)))
			return sjme_error_vmError(inThread, error);
		
		/* There is still an uncaught exception? */
		uncaught = sjme_atomic_sjme_jobject_get(&inThread->tossed);
		if (uncaught != NULL)
		{
			/* Print it out. */
			if (sjme_error_is(error = sjme_nvm_task_stackTraceThrowable(
				inThread, (sjme_jthrowable)uncaught)))
				sjme_message("Uncaught throwable, print error %d",
					error);
			
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
	sjme_attrInNotNull sjme_lpcstr threadName)
{
	sjme_errorCode error;
	sjme_nvm_thread result;
	sjme_nvm_frame firstFrame;
	sjme_nvm inState;
	sjme_jint freeSlot, i, n;
	sjme_pointer storage;
	
	if (inTask == NULL || outThread == NULL || threadName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot start a new thread if terminating. */
	if (sjme_atomic_sjme_jint_get(&inTask->terminate) !=
		SJME_NVM_TERMINATE_NOT)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Allocate stack storage. */
	storage = NULL;
	inState = inTask->inState;
	if (sjme_error_is(error = sjme_alloc(inState->allocPool,
		SJME_NVM_THREAD_STACK_SIZE, &storage)) || storage == NULL)
		goto fail_allocStorage;
	
	/* Allocate thread structure. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState, sizeof(*result),
		SJME_NVM_STRUCT_THREAD_INSTANCE, SJME_AS_NVM_COMMONP(&result))))
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
	result->inState = inState;
	result->inTask = inTask;
	result->threadId = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextThreadId, 1);
	result->object.identityHash =
		sjme_nvm_instance_calcIdentityHash(inTask, result);
	result->stack.storage = storage;
	result->stack.storageLen = SJME_NVM_THREAD_STACK_SIZE;
	
	/* All new threads are considered initially sleeping. */
	result->status = SJME_NVM_THREAD_STATUS_SLEEPING;
	
	/* Soft load the thread class. */
	if (sjme_error_is(error = sjme_nvm_task_commonClass(result,
		SJME_NVM_TASK_COMMON_CLASS_THREAD, &result->object.isClass,
		SJME_JNI_FALSE)) ||
		result->object.isClass == NULL)
		goto fail_loadThreadClass;
	
	/* All threads have an initial frame within java.lang.__Start__. */
	firstFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnterA(
		result, &firstFrame,
		"java/lang/__Start__",
		SJME_NVM_CLASS_MEMBER_STATIC,
		"__main", "()V",
		0, NULL)))
		goto fail_enterFrame;
	
	/* Store thread for future referencing. */
	inTask->threads->elements[freeSlot] = result;
	
	/* We want to count up the thread, so it does not get GCed! */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(result))))
		return sjme_error_vmError(inTask, error);

	/* Increase task thread count, for both all and normal. Normal gets */
	/* an add because a thread gets daemon being set later. */
	sjme_atomic_sjme_jint_getAdd(
		&inTask->numThreads[SJME_NVM_THREAD_COUNT_ALL], 1);
	sjme_atomic_sjme_jint_getAdd(
		&inTask->numThreads[SJME_NVM_THREAD_COUNT_NORMAL], 1);
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->object.common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outThread = result;
	return SJME_ERROR_NONE;
	
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
	if (!sjme_atomic_sjme_jint_compareSet(&inThread->start,
		SJME_NVM_THREAD_START_NEVER,
		SJME_NVM_THREAD_START_STANDARD))
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Set to be in the run state. */
	inThread->status = SJME_NVM_THREAD_STATUS_RUNNING;

	/* Schedule the thread for execution. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleIn(inThread->inState,
		inThread)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadStringValueOfCS(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNotNull sjme_charSeq inSeq)
{
#define SJME_INTERN_GROW 32
	sjme_errorCode error;
	sjme_nvm_taskStrings strings;
	sjme_list_sjme_jstring* interns;
	sjme_jstring* blankIntern;
	sjme_jstring result;
	sjme_jint hash, length, i, n;
	sjme_charSeq strSeq;
	
	if (inThread == NULL || outString == NULL || inSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Calculate the hash/length of the string. */
	hash = 0;
	if (sjme_error_is(error = sjme_charSeq_hash(inSeq, &hash)))
		return sjme_error_default(error);

	length = 0;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &length)))
		return sjme_error_default(error);

	/* If interned, we need to lock on all the strings. */
	strings = inThread->inTask->strings;
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
			SJME_NVM_TASK_COMMON_CLASS_STRING))) ||
		result == NULL)
		goto fail_allocStringInstance;

	/* Set string properties. */
	result->object.identityHash = hash;
	result->intern.hashCode = hash;
	result->intern.length = length;
	
	/* Duplicate the sequence. */
	strSeq = NULL;
	if (sjme_error_is(error = sjme_charSeq_dup(
		SJME_F_S(inThread)->allocPool, &strSeq, inSeq)) || strSeq == NULL)
		goto fail_dupSeq;

	/* Set sequence. */
	if (!sjme_atomic_sjme_charSeq_compareSet(&result->seq,
		NULL, strSeq))
		goto fail_collided;
	
	/* Final intern setup. */
	if (isIntern)
	{
		/* Need to grow the intern list? */
		if (blankIntern == NULL)
		{
			/* Reallocate list. */
			n = (interns == NULL ? 0 : interns->length);
			if (sjme_error_is(error = sjme_list_replace(
				inThread->inState->allocPool,
				n + SJME_INTERN_GROW,
				&strings->interns,
				sjme_jstring, 0)) || strings->interns == NULL)
				goto fail_replaceList;

			/* Place at end. */
			interns = strings->interns;
			blankIntern = &interns->elements[n];
		}
		
		/* Set slot here. */
		*blankIntern = result;

		/* Count it up since it is in the intern list. */
		if (sjme_error_is(error = sjme_nvm_instance_countUp(
			SJME_AS_JOBJECT(result))))
			goto fail_countInIntern;
		
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
		sjme_atomic_sjme_charSeq_set(&result->seq, NULL);
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
		outString, SJME_JNI_TRUE, inPool->seq);
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
		outString, isIntern, &tempSeq);
}
