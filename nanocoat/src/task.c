/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

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

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8

static sjme_errorCode sjme_nvm_loop_subSchedule(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInRange(0, SJME_NVM_THREAD_SCHEDULED)
		sjme_nvm_threadScheduleMode inMode)
{
#define SJME_NVM_SUB_GROW 8
	sjme_errorCode error;
	sjme_nvm_threadSubSchedule* sub;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inMode < 0 || inMode >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Get sub-schedule. */
	sub = &inState->schedule->mode[inMode];
	
	/* Lock thread. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inThread->object.common.lock)))
		return sjme_error_default(error);

	/* Ignore if already scheduled in this group. */
	if (inThread->schedule == inMode)
		goto skip_alreadyScheduled;

	/* Lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&sub->lock)))
		goto fail_lockSub;

	/* Need to grow the schedule list? */
	if (sub->order == NULL || (sub->count + 1) >= sub->order->length)
		if (sjme_error_is(error = sjme_list_replace(inState->allocPool,
			sub->count + SJME_NVM_SUB_GROW, &sub->order,
			sjme_nvm_thread, 0)))
			goto fail_growList;

	/* Place it at the end of the schedule queue. */
	sub->order->elements[sub->count] = inThread;
	sub->count++;

	/* Set new thread scheduling mode. */
	inThread->schedule = inMode;
	
	/* Release schedule lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&sub->lock, NULL)))
		goto fail_releaseSub;
	
	/* Release thread lock. */
skip_alreadyScheduled:
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inThread->object.common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_growList:
	sjme_thread_spinLockRelease(&sub->lock, NULL);
	
fail_releaseSub:
fail_lockSub:
	sjme_thread_spinLockRelease(&inThread->object.common.lock, NULL);

	return sjme_error_default(error);
#undef SJME_NVM_SUB_GROW
}

sjme_errorCode sjme_nvm_task_commonClass(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_TASK_NUM_COMMON_CLASS)
		sjme_nvm_task_commonClassId commonId,
	sjme_attrOutNotNull sjme_jclass* outClass)
{
	sjme_errorCode error;
	sjme_lpcstr commonName;
	sjme_jclass result;
	
	if (contextThread == NULL)
		return SJME_ERROR_NONE;

	if (commonId <= SJME_NVM_TASK_COMMON_CLASS_NULL ||
		commonId >= SJME_NVM_TASK_NUM_COMMON_CLASS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Already cached? */
	result = sjme_atomic_sjme_jclass_get(
		&contextThread->inTask->globals.commonClasses[commonId]);
	if (result != NULL)
	{
		*outClass = result;
		return SJME_ERROR_NONE;
	}
	
	/* What is the name of the common class? */
	commonName = NULL;
	switch (commonId)
	{
		case SJME_NVM_TASK_COMMON_CLASS_CLASS:
			commonName = "Ljava/lang/Class;";
			break;
		
		case SJME_NVM_TASK_COMMON_CLASS_OBJECT:
			commonName = "Ljava/lang/Object;";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BOOLEAN:
			commonName = "Z";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_BYTE:
			commonName = "B";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_CHARACTER:
			commonName = "C";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_DOUBLE:
			commonName = "D";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_FLOAT:
			commonName = "F";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_INTEGER:
			commonName = "I";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_LONG:
			commonName = "J";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_SHORT:
			commonName = "S";
			break;
	
		case SJME_NVM_TASK_COMMON_CLASS_PRIMITIVE_VOID:
			commonName = "F";
			break;

		case SJME_NVM_TASK_COMMON_CLASS_REFERENCE_PHANTOM:
			commonName = "Ljava/lang/ref/PhantomReference;";
			break;

		case SJME_NVM_TASK_COMMON_CLASS_REFERENCE_SOFT:
			commonName = "Ljava/lang/ref/SoftReference;";
			break;

		case SJME_NVM_TASK_COMMON_CLASS_REFERENCE_WEAK:
			commonName = "Ljava/lang/ref/WeakReference;";
			break;
		
		case SJME_NVM_TASK_COMMON_CLASS_STRING:
			commonName = "Ljava/lang/String;";
			break;

		case SJME_NVM_TASK_COMMON_CLASS_TRACE_POINT:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;";
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Load the common class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		SJME_F_CL(contextThread), &result, contextThread,
		commonName, SJME_JNI_TRUE)) || result == NULL)
		return sjme_error_vmError(contextThread, error);

	/* Cache for later. */
	sjme_atomic_sjme_jclass_compareSet(
		&contextThread->inTask->globals.commonClasses[commonId],
		NULL, result);

	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;
}

sjme_jclass sjme_nvm_task_commonClassR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_TASK_NUM_COMMON_CLASS)
		sjme_nvm_task_commonClassId commonId)
{
	sjme_jclass result;
	
	if (contextThread == NULL)
		return NULL;

	if (commonId <= SJME_NVM_TASK_COMMON_CLASS_NULL ||
		commonId >= SJME_NVM_TASK_NUM_COMMON_CLASS)
		return NULL;

	/* Load the class. */
	result = NULL;
	if (sjme_error_is(sjme_nvm_task_commonClass(contextThread, commonId,
		&result)) || result == NULL)
		return NULL;

	/* Success! */
	return result;
}

sjme_errorCode sjme_nvm_task_stackTrace(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_nvm_frame frame;
	sjme_jint i, instructionId, pc;
	sjme_jclass lastClass, nowClass;
	sjme_nvm_class_codeInfo nowCode;
	sjme_nvm_class_methodInfo nowMethod;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Empty? Do nothing. */
	if (inThread->numFrames == 0)
		return SJME_ERROR_NONE;

	/* The compact SquirrelJME format is in the following form: */
 	/*  | IN java.lang.Class (Class.java) */
  	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */

	/* Start from the top of the stack. */
	lastClass = NULL;
	for (i = inThread->numFrames - 1; i >=0; i--)
	{
		/* Which frame is this? */
		frame = inThread->frames->elements[i];

		/* Did the class change? */
		/* | IN java.lang.Class (Class.java) */
		nowClass = frame->inClass;
		if (nowClass != lastClass)
			sjme_messageB(" | IN %s (%s)",
				sjme_charSeq_tempUtf(nowClass->binaryName), "<UNKNOWN>");

		/* Print method trace. */
		/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
		nowCode = frame->inCode;
		nowMethod = (nowCode != NULL ? frame->inCode->inMethod : NULL);
		pc = frame->lastPc;
		instructionId = (frame->lastIv != 0 ? frame->lastIv :
			(nowCode != NULL && pc >= 0 &&
				pc < nowCode->rawCodeLen ?
				nowCode->rawCode[pc] & 0xFF : -1));
		if (nowCode == NULL || nowMethod == NULL)
			sjme_messageB(" | PURE VIRTUAL");
		else
			sjme_messageB(" | .%s:%s @%xh (:%d #%s@%d)",
				sjme_charSeq_tempUtf(nowMethod->name->seq),
				sjme_charSeq_tempUtf(nowMethod->type->seq),
				pc,
				-1,
				sjme_nvm_byteCode_names[instructionId & 0xFF],
				pc);

		/* Set for next run. */
		lastClass = nowClass;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_cchar adjustMain[SJME_NVM_CLASS_NAME_LIMIT];
	sjme_errorCode error;
	sjme_list_sjme_nvm_task* tasks;
	sjme_list_sjme_nvm_thread* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_thread mainThread;
	sjme_nvm_vmClass_loader classLoader;
	sjme_nvm_taskStrings strings;
	sjme_list_sjme_jstring* argStrings;

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

	/* Allocate strings. */
	strings = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK_STRINGS,
		SJME_AS_NVM_COMMONP(&strings))) || strings == NULL)
		goto fail_allocStrings;
	
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
	result->strings = strings;

	/* Use the default field accessor for this task by default. */
	result->globals.accessor = sjme_nvm_instance_fieldAccessor;
	
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
		&result->object.common.lock)))
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

	/* The main thread gets flagged as the main thread. */
	mainThread->isMain = SJME_JNI_TRUE;

	/* Adjust the main class name, turn periods into slashes. */
	memset(adjustMain, 0, sizeof(adjustMain));
	snprintf(adjustMain, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"%s", startConfig->mainClass);
	for (i = 0, n = strlen(adjustMain); i < n; i++)
		if (adjustMain[i] == '.')
			adjustMain[i] = '/';

	/* Setup string for main class. */
	result->globals.mainClassName = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
		mainThread, &result->globals.mainClassName, SJME_JNI_TRUE,
		adjustMain)) ||
		result->globals.mainClassName == NULL)
		goto fail_mainClassString;

	/* Setup strings for main arguments. */
	argStrings = NULL;
	if (startConfig->mainArgs != NULL && startConfig->mainArgs->length > 0)
	{
		/* Setup string list. */
		n = startConfig->mainArgs->length;
		if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
			n, &argStrings, sjme_jstring, 0)) || argStrings == NULL)
			goto fail_mainArgsStrings;

		/* Setup strings for each argument. */
		for (i = 0; i < n; i++)
			if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
				mainThread, &argStrings->elements[i], SJME_JNI_TRUE,
				startConfig->mainArgs->elements[i])) ||
				argStrings->elements[i] == NULL)
				goto fail_mainArgsString;
	}
	
	/* Set argument strings. */
	result->globals.mainArgs = argStrings;
	
	/* The main thread of any task is always implicitly started. */
	if (sjme_error_is(error = sjme_nvm_task_threadStart(mainThread)))
		goto fail_startMain;
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&result->object.common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outTask = result;
	return SJME_ERROR_NONE;
	
	/* In-state locks. */
fail_preLockBeforeRelease:
fail_allocThreads:
fail_initClassLoader:
fail_allocStrings:
	if (strings != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(strings));
		strings = NULL;
	}
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
fail_mainArgsString:
fail_mainArgsStrings:
	if (argStrings != NULL)
	{
		sjme_alloc_free(argStrings);
		argStrings = NULL;
	}
fail_mainClassString:
fail_startMain:
fail_taskNewThread:
fail_stateLockRelease:
	/* Unlock task before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&result->object.common.lock, NULL));
	
fail_other:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleDelete(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_taskScheduleIn(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;
	
	/* Forward. */
	return sjme_nvm_loop_subSchedule(inState, inThread,
		SJME_NVM_THREAD_SCHEDULED);
}
