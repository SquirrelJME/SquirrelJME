/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8

sjme_errorCode sjme_nvm_task_frameLocalSetL(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue)
{
	sjme_jboolean isWide;
	
	if (inFrame == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inValue->type < 0 || inValue->type >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Check for complete out of bounds. */
	isWide = (inValue->type == SJME_JAVA_TYPE_ID_LONG ||
		inValue->type == SJME_JAVA_TYPE_ID_DOUBLE);
	if (localIndex < 0 ||
		((localIndex + (isWide ? 1 : 0)) >= inFrame->inCode->maxLocals))
		return SJME_ERROR_LOCAL_INDEX_INVALID;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_framePool(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositiveNonZero sjme_jint poolIndex,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inType,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inTypeB,
	...)
{
	sjme_list_sjme_nvm_class_poolEntry* pool;
	sjme_nvm_class_poolEntry* result;
	sjme_jint argType;
	va_list arg;
	
	if (inFrame == NULL || outEntry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is the index valid? */
	pool = inFrame->pool->pool;
	if (poolIndex <= 0 || poolIndex >= pool->length)
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;

	/* Get entry here, check for base validity. */
	result = &pool->elements[poolIndex];
	if (result->type == inType)
		goto skip_success;

	/* Check second validity, if not zero. */
	if (inTypeB == 0)
		goto fail_notMatched;
	else if (result->type == inTypeB)
		goto skip_success;

	/* Check continual multi-type checks, until zero */
	for (va_start(arg, inTypeB);;)
	{
		/* Read in. */
		argType = va_arg(arg, int);

		/* Not matched? */
		if (argType == 0)
		{
			va_end(arg);
			goto fail_notMatched;
		}

		/* Matched? */
		if (result->type == argType)
		{
			va_end(arg);
			break;
		}
	}
	
skip_success:
	*outEntry = result;
	return SJME_ERROR_NONE;

fail_notMatched:
	return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
}

sjme_errorCode sjme_nvm_task_frameStackPushStringP(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_stringPool_string inString)
{
	if (inFrame == NULL || inString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_frameTreadSetT(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue)
{
	if (inFrame == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inValue->type < 0 || inValue->type >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (typeIndex < 0 || inFrame->treads[inValue->type] == NULL ||
		typeIndex >= inFrame->treads[inValue->type]->max)
		return SJME_ERROR_TREAD_INDEX_INVALID;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_task* tasks;
	sjme_list_sjme_nvm_thread* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_thread mainThread;
	sjme_nvm_vmClass_loader classLoader;

	if (inState == NULL || startConfig == NULL || outTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (startConfig->mainClass == NULL || startConfig->classPath == NULL ||
		startConfig->classPath->length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Start Main: %s", startConfig->mainClass);

	if (startConfig->mainArgs != NULL)
		for (i = 0; i < startConfig->mainArgs->length; i++)
			sjme_message("Start Arg[%d]: %s",
				i, startConfig->mainArgs->elements[i]);

	if (startConfig->sysProps != NULL)
		for (i = 0; i < startConfig->sysProps->length; i++)
			sjme_message("Start SysProp[%d]: %s",
				i, startConfig->sysProps->elements[i]);
#endif
	
	/* Lock state for task access. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inState->common.lock)))
		return sjme_error_default(error);
	
	/* Find a free slot to claim for a new task. */
	freeSlot = -1;
	tasks = inState->tasks;
	if (tasks != NULL)
		for (i = 0, n = tasks->length; i < n; i++)
			if (tasks->elements[0] == NULL)
			{
				freeSlot = -1;
				break;
			}
	
	/* Need to allocate or grow the task list? */
	if (freeSlot < 0)
	{
		/* Either allocate or grow. */
		if (tasks == NULL)
		{
			/* Allocate. */
			if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
				SJME_NVM_TASK_GROW, &tasks, sjme_nvm_task, 0)) ||
				tasks == NULL)
				goto fail_allocTasks;
			
			/* Free slot is always at the start. */
			freeSlot = 0;
		}
		else
		{
			/* Copy everything over. */
			if (sjme_error_is(error = sjme_list_copy(inState->allocPool,
				tasks->length + SJME_NVM_TASK_GROW, tasks, &tasks,
				sjme_nvm_task, 0)))
				goto fail_allocTasks;
			
			/* Free slot is always at the end. */
			freeSlot = tasks->length;
			
			/* Destroy old list. */
			if (sjme_error_is(error = sjme_alloc_free(inState->tasks)))
				goto fail_allocTasks;
			inState->tasks = NULL;
		}
		
		/* Store for later usage. */
		inState->tasks = tasks;
	}
	
	/* Allocate new task to start. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Initialize a new class loader for the current classpath. */
	classLoader = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderNew(
		inState, &classLoader,
		startConfig->classPath)) || classLoader == NULL)
		goto fail_initClassLoader;
	
	/* Refer to owning state and set identifier. */
	result->inState = inState;
	result->classLoader = classLoader;
	result->id = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextTaskId, 1);
	
	/* All new tasks are considered alive. */
	result->status = SJME_NVM_TASK_STATUS_ALIVE;
	
	/* Setup thread storage. */
	if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
		SJME_NVM_THREAD_GROW, &threads, sjme_nvm_thread, 0)) ||
		threads == NULL)
		goto fail_allocThreads;
	result->threads = threads;
	
	/* Task is considered valid now, so store it in. */
	tasks->elements[freeSlot] = result;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&result->common.lock)))
		goto fail_preLockBeforeRelease;
	
	/* Unlock state, we no longer need to keep the state locked since we */
	/* are now in the task list and others will really only care if we */
	/* are even alive or not. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inState->common.lock, NULL)))
		goto fail_stateLockRelease;
	
	/* Setup main thread, all threads start in java.lang.__Start__! */
	mainThread = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadNew(result,
		&mainThread, "main")) || mainThread == NULL)
		goto fail_taskNewThread;
	
	/* The main thread of any task is always implicitly started. */
	if (sjme_error_is(error = sjme_nvm_task_threadStart(mainThread)))
		goto fail_startMain;
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&result->common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outTask = result;
	return SJME_ERROR_NONE;
	
	/* In-state locks. */
fail_preLockBeforeRelease:
fail_allocThreads:
fail_initClassLoader:
fail_allocResult:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
fail_allocTasks:
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inState->common.lock, NULL));
	
	return sjme_error_default(error);

	/* Post state lock, when accessing state is no longer needed. */
fail_startMain:
fail_taskNewThread:
fail_stateLockRelease:
	/* Unlock task before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&result->common.lock, NULL));
	
fail_other:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
	
	return sjme_error_default(error);
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
	
	if (inThread == NULL || outFrame == NULL || inMethod == NULL ||
		(argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (callType < 0 || callType >= SJME_NVM_NUM_METHOD_CALL_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover target info. */
	targetInfo = inMethod->info[callType];
	if (targetInfo == NULL)
		return SJME_ERROR_UNBOUND_METHOD;
	
	/* Argument count mismatch? */
	if (argC != targetInfo->argC)
		return SJME_ERROR_ARGUMENT_COUNT_MISMATCH;

	/* Argument type mismatch? */
	for (i = 0, n = argC; i < n; i++)
		if (argV[i].type != targetInfo->argT[i])
			return SJME_ERROR_ARGUMENT_TYPE_MISMATCH;
	
	/* Grab a frame from the thread's frame pool. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadFrameNext(
		inThread, &result)) || result == NULL)
		return sjme_error_default(error);

	/* Setup initial locals, which are copied in from arguments. */
	for (i = 0, dx = 0, n = argC; i < n;
		i++, (dx += (argV[i].type == SJME_JAVA_TYPE_ID_LONG ||
			argV[i].type == SJME_JAVA_TYPE_ID_DOUBLE)) ? 2 : 1)
		if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
			result, dx, &argV[i])))
			return sjme_error_default(error);

	/* Set frame details. */
#if 0
	result->inClass = targetInfo->inClass;
#endif
	result->inCode = targetInfo->code;
	result->pool = targetInfo->code->inMethod->inClass->pool;
	
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
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_nvm_task inTask;
	sjme_jclass foundClass;
	
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* There must be a task. */
	inTask = inThread->inTask;
	if (inTask == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Need to find the class first. */
	foundClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inTask->classLoader, &foundClass,
		inThread, inClass, SJME_JNI_TRUE)))
		return sjme_error_default(error);
	
	/* Forward to other call. */
	return sjme_nvm_task_threadEnterC(
		inThread, outFrame, foundClass, instanceType,
		inName, inType, argC, argV);
}

sjme_errorCode sjme_nvm_task_threadEnterC(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
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

	/* Locate method to execute. */
	id = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		inClass, inThread, instanceType, inName,
		inType, &id)) || id == NULL)
		return sjme_error_default(error);
	
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
	
	if (inThread == NULL || outFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to allocate more frames? */
	if (inThread->frames == NULL ||
		inThread->numFrames >= inThread->frames->length)
		if (sjme_error_default(error = sjme_list_replace(
			inThread->inTask->inState->allocPool,
			inThread->numFrames + SJME_NVM_FRAME_GROW_SIZE,
			&inThread->frames, sjme_nvm_frame, 0)))
			return sjme_error_default(error);
	
	/* "Pop" and init/clear frame. */
	result = inThread->frames->elements[inThread->numFrames];
	if (result != NULL)
		memset(result, 0, sizeof(*result));
	else
	{
		/* Allocate new blank frame. */
		if (sjme_error_is(error = sjme_nvm_alloc(inThread->state,
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
	
	if (inTask == NULL || outThread == NULL || threadName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate thread structure. */
	result = NULL;
	inState = inTask->inState;
	if (sjme_error_is(error = sjme_nvm_alloc(inState, sizeof(*result),
		SJME_NVM_STRUCT_THREAD, SJME_AS_NVM_COMMONP(&result))))
		goto fail_allocResult;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inTask->common.lock)))
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
	result->state = inState;
	result->schedule = SJME_NVM_THREAD_NUM_SCHEDULE_MODE;
	result->inTask = inTask;
	result->threadId = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextThreadId, 1);
	
	/* All new threads are considered initially sleeping. */
	result->status = SJME_NVM_THREAD_STATUS_SLEEPING;
	
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
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outThread = result;
	return SJME_ERROR_NONE;
	
fail_enterFrame:
	if (firstFrame != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(firstFrame));
	
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inTask->common.lock, NULL));
fail_lock:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_threadStart(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Threads can only be started once! */
	if (inThread->start != SJME_NVM_THREAD_START_NEVER)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* There must be frames. */
	if (inThread->numFrames <= 0)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Set thread as started and in the run state. */
	inThread->start = SJME_NVM_THREAD_START_STANDARD;
	inThread->status = SJME_NVM_THREAD_STATUS_RUNNING;

	/* Schedule the thread for execution. */
	if (sjme_error_is(error = sjme_nvm_loop_schedule(inThread->state,
		inThread)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}
