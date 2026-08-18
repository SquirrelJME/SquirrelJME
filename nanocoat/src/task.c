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

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The size to grow the Jar package list by. */
#define SJME_NVM_TASK_JAR_GROW 32

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8

static sjme_errorCode sjme_nvm_task_taskScheduleMove(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInRange(0, SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		sjme_nvm_threadScheduleMode modeFrom,
	sjme_attrInRange(0, SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		sjme_nvm_threadScheduleMode modeTo,
	sjme_attrInValue sjme_jboolean inject)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	sjme_nvm_threadSubSchedule* fromSub;
	sjme_nvm_threadSubSchedule* toSub;
	sjme_list_sjme_nvm_thread* fromOrder;
	sjme_list_sjme_nvm_thread* toOrder;
	sjme_nvm_threadScheduleMode wasMode;
	sjme_jint i, n, freeSlot;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (modeFrom < 0 || modeTo < 0 ||
		modeFrom >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE ||
		modeTo >= SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* No change in schedule state? */
	wasMode = sjme_atomic_sjme_jint_get(&inThread->scheduleMode);
	if (wasMode == modeTo)
		return SJME_ERROR_NONE;

	/* We will be operating on this schedule. */
	schedule = inState->schedule;

	/* Is there an order to actually remove from? */
	fromSub = &schedule->mode[modeFrom];
	fromOrder = fromSub->order;
	if (modeFrom == SJME_NVM_THREAD_UNDEFINED_SCHEDULE ||
		modeFrom == SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		fromOrder = NULL;

	/* Remove from the old schedule first. */
	if (fromOrder != NULL && fromOrder->length > 0)
		for (i = 0, n = fromOrder->length; i < n; i++)
			if (fromOrder->elements[i] == inThread)
			{
				/* Set the schedule to undefined as it is not in one. */
				sjme_atomic_sjme_jint_set(&inThread->scheduleMode,
					SJME_NVM_THREAD_UNDEFINED_SCHEDULE);
				fromOrder->elements[i] = NULL;

				/* Move everything down so there are no gaps. */
				memmove(&fromOrder->elements[i],
					&fromOrder->elements[i + 1],
					sizeof(sjme_nvm_thread) * (n - i - 1));
				fromOrder->elements[n - 1] = NULL;

				/* Since we moved down, try this slot again. */
				i--;
			}

	/* Which schedule is this going into? */
	toSub = &schedule->mode[modeTo];
	toOrder = toSub->order;
	if (modeTo == SJME_NVM_THREAD_UNDEFINED_SCHEDULE ||
		modeTo == SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		goto skip_noPlace;

	/* Place into free slot, growing the list if needed. */
	if (sjme_error_is(error = sjme_list_injectGrow(inState->allocPool,
		SJME_NVM_THREAD_GROW, &toOrder, &inThread, sjme_nvm_thread, 0)) ||
		toOrder == NULL)
		return sjme_error_default(error);

	/* If the list changed, update it. */
	if (toSub->order != toOrder)
		toSub->order = toOrder;

	/* Mark the thread as being scheduled in the given target. */
	sjme_atomic_sjme_jint_set(&inThread->scheduleMode, modeTo);

	/* Success! */
skip_noPlace:
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_bracketJarPackage(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jbracketJarPackage* outBracket)
{
	sjme_errorCode error;
	sjme_nvm_task_globals* globals;
	sjme_list_sjme_jbracketJarPackage* brackets;
	sjme_jbracketJarPackage result;
	sjme_jint i, n;
	
	if (contextThread == NULL || inLibrary == NULL || outBracket == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* We need the task globals. */
	globals = &SJME_T_K(contextThread)->globals;

	/* Grab the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&globals->lock)))
		return sjme_error_default(error);

	/* Check pre-existing brackets. */
	result = NULL;
	brackets = globals->jarBrackets;
	if (brackets != NULL)
		for (i = 0, n = brackets->length; i < n; i++)
			if (inLibrary == brackets->elements[i]->library)
			{
				result = brackets->elements[i];
				break;
			}

	/* Need to create a new bracket for this library. */
	if (result == NULL)
	{
		/* Initialize bracket. */
		if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(
			contextThread, SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE,
			SJME_AS_JOBJECTP(&result))) || result == NULL)
			goto fail_newBracket;

		/* Set details. */
		result->library = inLibrary;

		/* Count up library. */
		if (sjme_error_is(error = sjme_alloc_weakRef(inLibrary, NULL)))
			goto fail_countUp;
		
		/* Cache it into the list. */
		if (sjme_error_is(error = sjme_list_injectGrow(
			SJME_T_S(contextThread)->allocPool, SJME_NVM_TASK_JAR_GROW,
			&brackets, result,
			sjme_jbracketJarPackage, 0)) || brackets == NULL)
			goto fail_growList;
		
		/* Store new list. */
		globals->jarBrackets = brackets;
	}
	
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&globals->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*outBracket = result;
	return SJME_ERROR_NONE;

fail_newBracket:
fail_countUp:
fail_growList:
	sjme_thread_spinLockRelease(&globals->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_commonClass(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonId,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInValue sjme_jboolean doInit)
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

		case SJME_NVM_TASK_COMMON_CLASS_JAR_PACKAGE:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;";
			break;
		
		case SJME_NVM_TASK_COMMON_CLASS_OBJECT:
			commonName = "Ljava/lang/Object;";
			break;

		case SJME_NVM_TASK_COMMON_CLASS_PIPE:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/PipeBracket;";
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

		case SJME_NVM_TASK_COMMON_CLASS_THREAD:
			commonName = "Ljava/lang/Thread;";
			break;
		
		case SJME_NVM_TASK_COMMON_CLASS_THROWABLE:
			commonName = "Ljava/lang/Throwable;";
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
		commonName, doInit)) || result == NULL)
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
		&result, SJME_JNI_TRUE)) || result == NULL)
		return NULL;

	/* Success! */
	return result;
}

sjme_errorCode sjme_nvm_task_stackTraceStep(
	sjme_attrInNotNull sjme_nvm_task_stackTraceState* traceState,
	sjme_attrInNotNull sjme_jclass atClass,
	sjme_attrInNotNull sjme_nvm_class_codeInfo atCode,
	sjme_attrInValue sjme_jint atPc,
	sjme_attrInValue sjme_byteCode atIv)
{
	if (traceState == NULL || atClass == NULL || atCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The compact SquirrelJME format is in the following form: */
 	/*  | IN java.lang.Class (Class.java) */
  	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
	
	/* Did the class change? */
	/* | IN java.lang.Class (Class.java) */
	traceState->nowClass = atClass;
	if (traceState->nowClass != traceState->lastClass)
		sjme_messageB(" | IN %s (%s)",
			sjme_charSeq_tempUtf(traceState->nowClass->binaryName),
				"<UNKNOWN>");

	/* Print method trace. */
	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
	traceState->nowCode = atCode;
	traceState->nowMethod = (traceState->nowCode != NULL ?
		atCode->inMethod : NULL);
	traceState->pc = atPc;
	traceState->instructionId = (atIv != 0 ? atIv :
		(traceState->nowCode != NULL && traceState->pc >= 0 &&
			traceState->pc < traceState->nowCode->rawCodeLen ?
			traceState->nowCode->rawCode[traceState->pc] & 0xFF : -1));
	if (traceState->nowCode == NULL || traceState->nowMethod == NULL)
		sjme_messageB(" | PURE VIRTUAL");
	else
		sjme_messageB(" | .%s:%s @%xh (:%d #%s@%d)",
			sjme_charSeq_tempUtf(traceState->nowMethod->name->seq),
			sjme_charSeq_tempUtf(traceState->nowMethod->type->seq),
			traceState->pc,
			-1,
			sjme_nvm_byteCode_names[traceState->instructionId & 0xFF],
			traceState->pc);

	/* Set for next run. */
	traceState->lastClass = traceState->nowClass;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_stackTraceThread(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_frame frame;
	sjme_nvm_task_stackTraceState traceState;
	sjme_jint i;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Empty? Do nothing. */
	if (inThread->numFrames == 0)
		return SJME_ERROR_NONE;
	
	/* Start from the top of the stack. */
	memset(&traceState, 0, sizeof(traceState));
	for (i = inThread->numFrames - 1; i >= 0; i--)
	{
		/* Which frame is this? */
		frame = inThread->frames->elements[i];

		/* Step trace. */
		if (sjme_error_is(error = sjme_nvm_task_stackTraceStep(
			&traceState, frame->inClass, frame->inCode,
			frame->lastPc, frame->lastIv)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_stackTraceThrowable(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jthrowable inThrowable)
{
	sjme_errorCode error;
	sjme_jarray pointArray;
	sjme_jbracketTrace point;
	sjme_nvm_task_stackTraceState traceState;
	sjme_jint i;
	
	if (inThrowable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be Throwable. */
	if (!sjme_nvm_vmClass_isAssignableFrom(contextThread,
		sjme_nvm_task_commonClassR(contextThread,
			SJME_NVM_TASK_COMMON_CLASS_THROWABLE),
			inThrowable->object.isClass))
		return SJME_ERROR_CLASS_CAST;

	/* Must be an array type. */
	pointArray = (sjme_jarray)sjme_atomic_sjme_intPointer_get(
		&inThrowable->object.special);
	if (!sjme_nvm_isAR(pointArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;

	/* Go through and extract points per each. */
	memset(&traceState, 0, sizeof(traceState));
	for (i = 0; i < pointArray->length; i++)
	{
		/* Must be a trace point. */
		point = (sjme_jbracketTrace)pointArray->e.l[i];
		if (!sjme_nvm_isAR(point,
			SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE))
			return SJME_ERROR_CLASS_CAST;

		/* Step trace. */
		if (sjme_error_is(error = sjme_nvm_task_stackTraceStep(
			&traceState, point->capture.inClass, point->capture.inCode,
			point->capture.lastPc, point->capture.lastIv)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_taskEnterMain(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNullable sjme_nvm_thread* outThread)
{
	sjme_errorCode error;
	sjme_nvm inState;
	sjme_cchar adjustMain[SJME_NVM_CLASS_NAME_LIMIT];
	sjme_nvm_thread mainThread;
	sjme_jint i, n;
	const sjme_nvm_task_taskNewConfig* initConfigCopy;
	sjme_list_sjme_jstring* argStrings;
	
	if (inTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Lock task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inTask->object.common.lock)))
		return sjme_error_default(error);

	/* Main thread already set? */
	if (inTask->globals.mainThread != NULL)
		goto fail_mainExists;

	/* Quicker to reference this way. */
	inState = inTask->inState;

	/* Recover the initial config. */
	initConfigCopy = inTask->initConfig;
	
	/* Setup main thread, all threads start in java.lang.__Start__! */
	mainThread = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadNew(inTask,
		&mainThread, "main")) || mainThread == NULL)
		goto fail_taskNewThread;

	/* The main thread gets flagged as the main thread. */
	inTask->globals.mainThread = mainThread;
	mainThread->isMain = SJME_JNI_TRUE;

	/* Adjust the main class name, turn periods into slashes. */
	memset(adjustMain, 0, sizeof(adjustMain));
	snprintf(adjustMain, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"%s", initConfigCopy->mainClass);
	for (i = 0, n = sjme_util_sizeToInt(strlen(adjustMain)); i < n; i++)
		if (adjustMain[i] == '.')
			adjustMain[i] = '/';

	/* Setup string for main class. */
	inTask->globals.mainClassName = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
		mainThread, &inTask->globals.mainClassName, SJME_JNI_TRUE,
		adjustMain)) ||
		inTask->globals.mainClassName == NULL)
		goto fail_mainClassString;

	/* Count up main class string. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(inTask->globals.mainClassName))))
		goto fail_countMainClassString;

	/* Setup strings for main arguments. */
	argStrings = NULL;
	if (initConfigCopy->mainArgs != NULL &&
		initConfigCopy->mainArgs->length > 0)
	{
		/* Setup string list. */
		n = initConfigCopy->mainArgs->length;
		if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
			n, &argStrings, sjme_jstring, 0)) || argStrings == NULL)
			goto fail_mainArgsStrings;

		/* Setup strings for each argument. */
		for (i = 0; i < n; i++)
		{
			/* Create string. */
			if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
				mainThread, &argStrings->elements[i], SJME_JNI_TRUE,
				initConfigCopy->mainArgs->elements[i])) ||
				argStrings->elements[i] == NULL)
				goto fail_mainArgsString;
			
			/* Count up string. */
			if (sjme_error_is(error = sjme_nvm_instance_countUp(
				SJME_AS_JOBJECT(argStrings->elements[i]))))
				goto fail_countMainArgString;
		}
	}
		
	/* Set argument strings. */
	inTask->globals.mainArgs = argStrings;
	
	/* The main thread of any task is always implicitly started. */
	if (sjme_error_is(error = sjme_nvm_task_threadStart(mainThread)))
		goto fail_startMain;
	
	/* Unlock task. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->object.common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	if (outThread != NULL)
		*outThread = mainThread;
	return SJME_ERROR_NONE;

fail_startMain:
fail_countMainArgString:
fail_mainArgsString:
fail_mainArgsStrings:
	if (argStrings != NULL)
	{
		sjme_alloc_free(argStrings);
		argStrings = NULL;
	}
fail_countMainClassString:
fail_mainClassString:
fail_taskNewThread:
fail_mainExists:
	/* Release lock before failing. */
	sjme_thread_spinLockRelease(&inTask->object.common.lock, NULL);

	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* initConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_task* tasks;
	sjme_list_sjme_nvm_thread* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_vmClass_loader classLoader;
	sjme_nvm_taskStrings strings;
	const sjme_nvm_task_taskNewConfig* initConfigCopy;

	if (inState == NULL || initConfig == NULL || outTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (initConfig->mainClass == NULL || initConfig->classPath == NULL ||
		initConfig->classPath->length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Start Main: %s", initConfig->mainClass);

	if (initConfig->mainArgs != NULL)
		for (i = 0; i < initConfig->mainArgs->length; i++)
			sjme_message("Start Arg[%d]: %s",
				i, initConfig->mainArgs->elements[i]);

	if (initConfig->sysProps != NULL)
		for (i = 0; i < initConfig->sysProps->length; i++)
			sjme_message("Start SysProp[%d]: %s",
				i, initConfig->sysProps->elements[i]);
#endif
	
	/* Lock state for task access. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inState->common.lock)))
		return sjme_error_default(error);

	/* Make a copy of the initialization config. */
	initConfigCopy = NULL;
	if (sjme_error_is(error = sjme_alloc_copy(inState->allocPool,
		sizeof(*initConfigCopy), (sjme_pointer*)&initConfigCopy,
		(sjme_pointer)initConfig)) ||
		initConfigCopy == NULL)
		goto fail_copyInitConfig;
	
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
		initConfigCopy->classPath)) || classLoader == NULL)
		goto fail_initClassLoader;
	
	/* Refer to owning state and set identifier. */
	result->inState = inState;
	result->classLoader = classLoader;
	result->id = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextTaskId, 1);
	result->strings = strings;
	result->initConfig = initConfigCopy;

	/* Initialize identity hashcode generator. */
	if (sjme_error_is(error = sjme_random_init(&result->idHash,
		INT32_C(0x43757465), INT32_C(0x53716B21))))
		goto fail_initIdHash;

	/* Use the default field accessor for this task by default. */
	result->globals.accessor = sjme_nvm_instance_fieldAccessor;

	/* Do not perform optimizations? */
	result->globals.noOptimize = initConfigCopy->noOptimize;
	
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
	
	/* Add to the running task count. */
	sjme_atomic_sjme_jint_getAdd(&inState->numRunningTasks, 1);

	/* Not belaying main start? Then start the main thread. */
	if ((initConfigCopy->belay & SJME_NVM_BOOT_BELAY_MAIN) == 0)
		if (sjme_error_is(error = sjme_nvm_task_taskEnterMain(result, NULL)))
			goto fail_enterMain;
	
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
fail_initIdHash:
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
fail_copyInitConfig:
	if (initConfigCopy != NULL)
		sjme_alloc_free((sjme_pointer)initConfigCopy);
	
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inState->common.lock, NULL));
	
	return sjme_error_default(error);

	/* Post state lock, when accessing state is no longer needed. */
fail_enterMain:
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
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Ignore if already deleted. */
	if (sjme_atomic_sjme_jint_get(&inThread->scheduleMode) ==
		SJME_NVM_THREAD_NUM_SCHEDULE_MODE)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Remove from both schedule states. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_SCHEDULED,
		SJME_NVM_THREAD_UNDEFINED_SCHEDULE,
		SJME_JNI_FALSE)))
		goto fail_remove;
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_UNSCHEDULED,
		SJME_NVM_THREAD_UNDEFINED_SCHEDULE,
		SJME_JNI_FALSE)))
		goto fail_remove;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_remove:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleIn(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Ignore if already scheduled. */
	if (sjme_atomic_sjme_jint_get(&inThread->scheduleMode) ==
			SJME_NVM_THREAD_SCHEDULED)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Move to scheduled. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_UNSCHEDULED,
		SJME_NVM_THREAD_SCHEDULED,
		SJME_JNI_TRUE)))
		goto fail_move;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_move:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleNext(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_nvm_thread* runThread,
	sjme_attrOutNotNull sjme_jboolean* isTerminated)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	sjme_list_sjme_nvm_thread* order;
	sjme_nvm_thread nextThread, checkThread;
	sjme_jboolean terminated, isRunning;
	sjme_jint i, n, mode;
	sjme_list_sjme_nvm_task* tasks;
	sjme_nvm_task checkTask;
	
	if (inState == NULL || runThread == NULL || isTerminated == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Default state. */
	nextThread = NULL;
	terminated = sjme_atomic_sjme_jint_get(&inState->terminating);

	/* Terminating already? Or no-effect when multithreaded. */
	if (terminated || inState->threadModel == SJME_NVM_MLE_THREAD_MULTI)
	{
		*runThread = NULL;
		*isTerminated = terminated;
		return SJME_ERROR_NONE;
	}
	
	/* If no tasks are left alive, stop VM execution. */
	if (sjme_atomic_sjme_jint_get(&inState->numRunningTasks) <= 0)
	{
		*runThread = NULL;
		*isTerminated = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}

	/* This is used to determine what to get. */
	schedule = inState->schedule;
	
	/* Lock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&schedule->lock)))
		return sjme_error_default(error);

	/* Find next running, then waiting, thread that is scheduled. */
	for (mode = SJME_NVM_THREAD_SCHEDULED;
		mode > SJME_NVM_THREAD_UNDEFINED_SCHEDULE &&
		mode < SJME_NVM_THREAD_NUM_SCHEDULE_MODE; mode++)
	{
		/* Scan through the running or waiting set. */
		order = schedule->mode[mode].order;
		if (order != NULL && order->length > 0)
			for (i = 0, n = order->length; i < n; i++)
			{
				/* There must be a thread here to check. */
				checkThread = order->elements[i];
				if (checkThread == NULL)
					continue;

				/* Can this thread even be run (not sleeping)? */
				isRunning = SJME_JNI_FALSE;
				if (sjme_error_is(error = sjme_nvm_task_taskScheduleYes(
					inState, checkThread, &isRunning)))
					goto fail_checkRunning;

				/* Thread is not actually running. */
				if (!isRunning)
				{
					/* If this thread is scheduled, move it into */
					/* unscheduled. */
					if (mode == SJME_NVM_THREAD_SCHEDULED)
						if (sjme_error_is(error =
							sjme_nvm_task_taskScheduleMove(inState,
							checkThread,
							SJME_NVM_THREAD_SCHEDULED,
							SJME_NVM_THREAD_UNSCHEDULED,
							SJME_JNI_FALSE)))
							goto fail_move;

					/* Try another thread. */
					continue;
				}

				/* Accept this thread! */
				nextThread = checkThread;
				break;
			}
	}

	/* If we selected a thread, then move it to be scheduled. */
	if (nextThread != NULL)
		if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
			nextThread,
			SJME_NVM_THREAD_UNSCHEDULED,
			SJME_NVM_THREAD_SCHEDULED,
			SJME_JNI_TRUE)))
			goto fail_move;
	
	/* Release the schedule lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*runThread = nextThread;
	*isTerminated = terminated;
	return SJME_ERROR_NONE;

fail_unlockState:
fail_lockState:
fail_move:
fail_checkRunning:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleOut(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_threadSchedule* schedule;
	
	if (inState == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* No effect in multi-threading. */
	if (SJME_T_S(inThread)->threadModel == SJME_NVM_MLE_THREAD_MULTI)
		return SJME_ERROR_NONE;

	/* Ignore if already unscheduled. */
	if (sjme_atomic_sjme_jint_get(&inThread->scheduleMode) ==
		SJME_NVM_THREAD_UNSCHEDULED)
		return SJME_ERROR_NONE;

	/* Lock schedule. */
	schedule = inState->schedule;
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&schedule->lock)))
		return sjme_error_default(error);
	
	/* Move to unscheduled. */
	if (sjme_error_is(error = sjme_nvm_task_taskScheduleMove(inState,
		inThread, SJME_NVM_THREAD_SCHEDULED,
		SJME_NVM_THREAD_UNSCHEDULED,
		SJME_JNI_TRUE)))
		goto fail_move;

	/* Unlock schedule. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&schedule->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
fail_move:
	sjme_thread_spinLockRelease(&schedule->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskScheduleYes(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jboolean* isRunning)
{
	sjme_nvm_thread_startType start;
	sjme_nvm_task inTask;
	sjme_jint left;
	
	if (inState == NULL || inThread == NULL || isRunning == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If this is a callback thread, only consider if it has at least */
	/* one actively running frame. */
	start = sjme_atomic_sjme_jint_get(&inThread->start);
	if (start == SJME_NVM_THREAD_START_CALLBACK)
	{
		if (inThread->numFrames > 0)
			goto skip_yes;
	}

	/* If the thread is running, continue. */
	else if (start == SJME_NVM_THREAD_START_STANDARD)
		goto skip_yes;

	/* Thread is entering the finishing state, so it is stopping. */
	else if (start == SJME_NVM_THREAD_START_FINISHING)
	{
		/* Mark as finished. */
		sjme_atomic_sjme_jint_compareSet(&inThread->start,
			SJME_NVM_THREAD_START_FINISHING,
			SJME_NVM_THREAD_START_FINISHED);

		/* This thread is awaiting termination. */
		inTask = SJME_T_K(inThread);
		sjme_atomic_sjme_jint_getAdd(
			&inTask->numThreads[SJME_NVM_THREAD_COUNT_AWAIT_CLEANUP],
			1);

		/* Reduce total thread count. */
		sjme_atomic_sjme_jint_getAdd(
			&inTask->numThreads[SJME_NVM_THREAD_COUNT_ALL], -1);

		/* Non-daemon or daemon thread? */
		if (inThread->flags.isDaemon)
			sjme_atomic_sjme_jint_getAdd(
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_DAEMON], -1);
		else
		{
			/* How many threads are left? */
			left = sjme_atomic_sjme_jint_getAdd(
				&inTask->numThreads[SJME_NVM_THREAD_COUNT_NORMAL], -1) - 1;

			/* If there are no threads left, then start termination. */
			if (left <= 0)
				sjme_atomic_sjme_jint_compareSet(&inTask->terminate,
					SJME_NVM_TERMINATE_NOT,
					SJME_NVM_TERMINATE_CLEANUP);
		}

		/* Cleanup must be called! */
		goto skip_yes;
	}

skip_not:
	*isRunning = SJME_JNI_FALSE;
	return SJME_ERROR_NONE;
	
skip_yes:
	*isRunning = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}
