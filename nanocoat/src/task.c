/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/task.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8
	
sjme_errorCode sjme_nvm_task_start(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_startConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_task* tasks;
	sjme_list_sjme_nvm_thread* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_thread mainThread;

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
	if (sjme_error_is(error = sjme_nvm_alloc(inState->reservedPool,
		sizeof(*result), SJME_NVM_STRUCT_TASK,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Refer to owning state and set identifier. */
	result->inState = inState;
	result->id = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextTaskId, 1);
	
	/* All new tasks are considered alive. */
	result->status = SJME_NVM_TASK_STATUS_ALIVE;
	
	/* Setup thread storage. */
	if (sjme_error_is(error = sjme_list_alloc(inState->reservedPool,
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
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV)
{
	if (inThread == NULL || outFrame == NULL || inMethod == NULL ||
		(argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_threadEnterA(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_lpcstr inClass,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV)
{
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_threadEnterC(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalue* argV)
{
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_threadNew(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNotNull sjme_nvm_thread* outThread,
	sjme_attrInNotNull sjme_lpcstr threadName)
{
	sjme_errorCode error;
	sjme_nvm_thread result;
	sjme_nvm_frame firstFrame;
	sjme_alloc_pool* pool;
	sjme_nvm inState;
	sjme_jint freeSlot, i, n;
	
	if (inTask == NULL || outThread == NULL || threadName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate thread structure. */
	result = NULL;
	inState = inTask->inState;
	pool = inState->reservedPool;
	if (sjme_error_is(error = sjme_nvm_alloc(pool, sizeof(*result),
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
	result->inState = inState;
	result->threadId = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextThreadId, 1);
	
	/* All new threads are considered initially sleeping. */
	result->status = SJME_NVM_THREAD_STATUS_SLEEPING;
	
	/* All threads have an initial frame within java.lang.__Start__. */
	firstFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnterA(
		result, &firstFrame,
		"java/lang/__Start__", "__main", "()V",
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
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
