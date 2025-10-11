/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"

static sjme_errorCode sjme_nvm_mleFunc_waitForUpdateCheck(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_intPointer condition,
	sjme_attrOutNotNull sjme_jvalueTyped* stackPush)
{
	sjme_errorCode error;
	
	if (inFrame == NULL || stackPush == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Was this actually interrupted? */
	if (sjme_error_is(error = sjme_nvm_task_threadInterruptCheck(
		SJME_F_T(inFrame), SJME_JNI_TRUE)))
	{
		if (error != SJME_ERROR_INTERRUPTED)
			return sjme_error_default(error);

		/* Return as interrupted! */
		stackPush->t = SJME_BASIC_TYPE_ID_INTEGER;
		stackPush->v.i = 1;
		
		/* Stop waiting! */
		return SJME_ERROR_NONE;
	}

	/* Did the frame count actually change? */
	if (condition != sjme_atomic_g(sjme_jint,
		&SJME_F_K(inFrame)->numThreads[SJME_NVM_THREAD_COUNT_ALL]))
	{
		/* Not interrupted. */
		stackPush->t = SJME_BASIC_TYPE_ID_INTEGER;
		stackPush->v.i = 0;
		
		/* Stop waiting! */
		return SJME_ERROR_NONE;
	}

	/* Still waiting for it to complete. */
	return SJME_ERROR_NOT_MATCHED;
}

SJME_NVM_MLE_FUNCTION_DECL(aliveThreadCount)
{
	sjme_jboolean includeMain, includeDaemon;
	sjme_jint count;
	sjme_nvm_task inTask;

	/* Including main and/or daemon threads? */
	includeMain = (argV[0].v.i != 0);
	includeDaemon = (argV[1].v.i != 0);

	/* Working with the frame's task. */
	inTask = SJME_F_K(inFrame);

	/* Get the base thread count. */
	count = sjme_atomic_g(sjme_jint, &inTask->numThreads[
		(includeDaemon ? SJME_NVM_THREAD_COUNT_ALL :
			SJME_NVM_THREAD_COUNT_NORMAL)]);

	/* If not including the main thread, reduce by the count which should */
	/* always be one. */
	if (!includeMain)
		count -= sjme_atomic_g(sjme_jint, 
			&inTask->numThreads[SJME_NVM_THREAD_COUNT_MAIN]);

	/* Return the count. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = count;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(createVMThread)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(currentExitCode)
{
	/* This is just a read of the value. */
	argR->t = SJME_BASIC_TYPE_ID_INTEGER;
	argR->v.i = sjme_atomic_g(sjme_jint, &SJME_F_K(inFrame)->exitCode);
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(currentJavaThread)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(currentVMThread)
{
	sjme_errorCode error;
	
	/* Native threads are VM threads. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)SJME_F_T(inFrame);

	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(equals)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(javaThreadClearInterrupt)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(javaThreadRunnable)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(javaThreadSetDaemon)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(model)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(runProcessMain)
{
	sjme_errorCode error;
	sjme_nvm_task task;
	sjme_jclass mainClass;
	sjme_jarray mainArgs;
	sjme_jmethodID mainMethod;
	sjme_nvm_frame ignoreFrame;
	sjme_jvalueTyped mainArgV[1];
	sjme_jint i, n;

	/* Recover task. */
	task = sjme_atomic_g(sjme_nvm_task, &inFrame->inTask);
	if (task == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Locate the main class. */
	mainClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(task->classLoader,
		&mainClass, SJME_F_T(inFrame),
		sjme_atomic_g(sjme_charSeq, 
			&task->globals.mainClassName->seq), SJME_JNI_TRUE)) ||
		mainClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Locate the main method. */
	mainMethod = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameTypeU(
		mainClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_STATIC, SJME_JNI_TRUE,
		"main", "([Ljava/lang/String;)V", &mainMethod)) ||
		mainMethod == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Allocate main arguments. */
	mainArgs = NULL;
	n = (task->globals.mainArgs == NULL ? 0 : task->globals.mainArgs->length);
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&mainArgs),
		sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
			SJME_NVM_TASK_COMMON_CLASS_STRING), n)))
		return sjme_error_vmError(inFrame, error);

	/* Fill in actual arguments. */
	for (i = 0; i < n; i++)
		mainArgs->e.l[i] =
			SJME_AS_JOBJECT(task->globals.mainArgs->elements[i]);
	
	/* Setup arguments. */
	memset(mainArgV, 0, sizeof(mainArgV));
	mainArgV[0].t = SJME_JAVA_TYPE_ID_OBJECT;
	mainArgV[0].v.l = SJME_AS_JOBJECT(mainArgs);
	
	/* Enter the frame. */
	ignoreFrame = NULL;
	return sjme_nvm_task_threadEnter(SJME_F_T(inFrame),
		&ignoreFrame, mainMethod, SJME_NVM_CLASS_MEMBER_STATIC,
		1, mainArgV);
}

SJME_NVM_MLE_FUNCTION_DECL(setCurrentExitCode)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(setTrace)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(sleep)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(toJavaThread)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(toVMThread)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadId)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadInterrupt)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadIsAlive)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadIsMain)
{
	sjme_nvm_thread thread;
	
	/* Must be a VMThread. */
	thread = (sjme_nvm_thread)argV[0].v.l;
	if (thread == NULL ||
		!sjme_nvm_isAR(thread, SJME_NVM_STRUCT_THREAD_INSTANCE))
		return SJME_ERROR_MLE_CALL;

	/* Is a simple flag get. */
	argR->t = SJME_JAVA_TYPE_ID_INTEGER;
	argR->v.i = !!thread->isMain;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadIsStarted)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadSetPriority)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadStart)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(vmThreadTask)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(waitForUpdate)
{
	sjme_errorCode error;
	sjme_nvm inState;
	sjme_jboolean interrupted, forget;
	sjme_jint ms;

	/* How long to rest for? */
	ms = argV[0].v.i;
	if (ms < 0)
		return SJME_ERROR_MLE_CALL;

	/* Forget that we executed this method? */
	forget = SJME_JNI_FALSE;
	
	/* Wait for an update on multithreaded systems. */
	inState = SJME_F_S(inFrame);
	if (inState->threadModel == SJME_NVM_MLE_THREAD_MULTI)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Single threaded thread operation. */
	else
	{
		/* Check interrupt pre-sleep. */
		if (sjme_error_is(error = sjme_nvm_task_threadInterruptCheck(
			SJME_F_T(inFrame), SJME_JNI_TRUE)))
		{
			/* Some other error? */
			if (error != SJME_ERROR_INTERRUPTED)
				return sjme_error_default(error);

			/* This was actually interrupted. */
			argR->v.i = 1;
			return SJME_ERROR_NONE;
		}
		
		/* Schedule out this thread. */
		if (sjme_error_is(error =
			sjme_nvm_task_taskScheduleOut(inState, SJME_F_T(inFrame), ms)))
			return sjme_error_default(error);

		/* Because we just scheduled out this, this call cannot complete. */
		forget = SJME_JNI_TRUE;
	}

	/* Not finishing this call? We need to wait for the count to change */
	if (forget)
	{
		/* Wait for an update to occur. */
		if (sjme_error_is(error = sjme_nvm_task_frameWaitFor(inFrame,
			sjme_nvm_mleFunc_waitForUpdateCheck, ms,
			sjme_atomic_g(sjme_jint,
				&SJME_F_K(inFrame)->numThreads[SJME_NVM_THREAD_COUNT_ALL]))))
			return sjme_error_default(error);

		/* We must not push the value to the stack, it is done in the */
		/* condition. */
		return SJME_ERROR_CANCEL_MLE_CALL;
	}
	
	/* Not interrupted, and finished. */
	argR->v.i = 0;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(ThreadShelf) =
{
	SJME_NVM_MLE_DEFINE(aliveThreadCount,
		SJME_MD(SJME_MD_I, SJME_MD_Z SJME_MD_Z),
		"I", "II"),
	SJME_NVM_MLE_DEFINE(createVMThread,
		SJME_MD(SJME_MD_VM_THREAD, SJME_MD_THREAD),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(currentExitCode,
		SJME_MD(SJME_MD_I, ),
		"I", ),
	SJME_NVM_MLE_DEFINE(currentJavaThread,
		SJME_MD(SJME_MD_THREAD, ),
		"L", ),
	SJME_NVM_MLE_DEFINE(currentVMThread,
		SJME_MD(SJME_MD_VM_THREAD, ),
		"L", ),
	SJME_NVM_MLE_DEFINE(equals,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD SJME_MD_VM_THREAD),
		"I", "LL"),
	SJME_NVM_MLE_DEFINE(javaThreadClearInterrupt,
		SJME_MD(SJME_MD_Z, SJME_MD_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(javaThreadRunnable,
		SJME_MD(SJME_MD_RUNNABLE, SJME_MD_THREAD),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(javaThreadSetDaemon,
		SJME_MD(SJME_MD_V, SJME_MD_THREAD),
		"V", "L"),
	SJME_NVM_MLE_DEFINE(model,
		SJME_MD(SJME_MD_I, ),
		"I", ),
	SJME_NVM_MLE_DEFINE(runProcessMain,
		SJME_MD(SJME_MD_V, ),
		"V", ),
	SJME_NVM_MLE_DEFINE(setCurrentExitCode,
		SJME_MD(SJME_MD_V, SJME_MD_I),
		"V", "I"),
	SJME_NVM_MLE_DEFINE(setTrace,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_A(SJME_MD_TRACE)),
		"V", "LL"),
	SJME_NVM_MLE_DEFINE(sleep,
		SJME_MD(SJME_MD_V, SJME_MD_I SJME_MD_I),
		"V", "II"),
	SJME_NVM_MLE_DEFINE(toJavaThread,
		SJME_MD(SJME_MD_THREAD, SJME_MD_VM_THREAD),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(toVMThread,
		SJME_MD(SJME_MD_VM_THREAD, SJME_MD_THREAD),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadId,
		SJME_MD(SJME_MD_I, SJME_MD_VM_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadInterrupt,
		SJME_MD(SJME_MD_V, SJME_MD_VM_THREAD),
		"V", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadIsAlive,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadIsMain,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadIsStarted,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadSetPriority,
		SJME_MD(SJME_MD_V, SJME_MD_VM_THREAD SJME_MD_I),
		"V", "LI"),
	SJME_NVM_MLE_DEFINE(vmThreadStart,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(vmThreadTask,
		SJME_MD(SJME_MD_TASK, SJME_MD_VM_THREAD),
		"L", "L"),
	SJME_NVM_MLE_DEFINE(waitForUpdate,
		SJME_MD(SJME_MD_Z, SJME_MD_I),
		"I", "I"),
	
	SJME_NVM_MLE_STOP()
};
