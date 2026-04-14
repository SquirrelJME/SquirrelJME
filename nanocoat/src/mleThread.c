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

SJME_NVM_MLE_FUNCTION_DECL(aliveThreadCount)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(createVMThread)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

SJME_NVM_MLE_FUNCTION_DECL(currentExitCode)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	task = inFrame->inTask;
	if (task == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Locate the main class. */
	mainClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(task->classLoader,
		&mainClass, SJME_F_T(inFrame),
		sjme_atomic_sjme_charSeq_get(
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
		&ignoreFrame, mainMethod, SJME_NVM_CALL_VIRTUAL,
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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
