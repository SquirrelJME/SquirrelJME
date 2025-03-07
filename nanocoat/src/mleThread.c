/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
		"III"),
	SJME_NVM_MLE_DEFINE(createVMThread,
		SJME_MD(SJME_MD_VM_THREAD, SJME_MD_THREAD),
		"LL"),
	SJME_NVM_MLE_DEFINE(currentExitCode,
		SJME_MD(SJME_MD_I, ),
		"I"),
	SJME_NVM_MLE_DEFINE(currentJavaThread,
		SJME_MD(SJME_MD_THREAD, ),
		"L"),
	SJME_NVM_MLE_DEFINE(currentVMThread,
		SJME_MD(SJME_MD_VM_THREAD, ),
		"L"),
	SJME_NVM_MLE_DEFINE(equals,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD SJME_MD_VM_THREAD),
		"ILL"),
	SJME_NVM_MLE_DEFINE(javaThreadClearInterrupt,
		SJME_MD(SJME_MD_Z, SJME_MD_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(javaThreadRunnable,
		SJME_MD(SJME_MD_RUNNABLE, SJME_MD_THREAD),
		"LL"),
	SJME_NVM_MLE_DEFINE(javaThreadSetDaemon,
		SJME_MD(SJME_MD_V, SJME_MD_THREAD),
		"VL"),
	SJME_NVM_MLE_DEFINE(model,
		SJME_MD(SJME_MD_I, ),
		"I"),
	SJME_NVM_MLE_DEFINE(runProcessMain,
		SJME_MD(SJME_MD_V, ),
		"V"),
	SJME_NVM_MLE_DEFINE(setCurrentExitCode,
		SJME_MD(SJME_MD_V, SJME_MD_I),
		"VI"),
	SJME_NVM_MLE_DEFINE(setTrace,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_A(SJME_MD_TRACE_POINT)),
		"VLL"),
	SJME_NVM_MLE_DEFINE(sleep,
		SJME_MD(SJME_MD_V, SJME_MD_I SJME_MD_I),
		"VII"),
	SJME_NVM_MLE_DEFINE(toJavaThread,
		SJME_MD(SJME_MD_THREAD, SJME_MD_VM_THREAD),
		"LL"),
	SJME_NVM_MLE_DEFINE(toVMThread,
		SJME_MD(SJME_MD_VM_THREAD, SJME_MD_THREAD),
		"LL"),
	SJME_NVM_MLE_DEFINE(vmThreadId,
		SJME_MD(SJME_MD_I, SJME_MD_VM_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(vmThreadInterrupt,
		SJME_MD(SJME_MD_V, SJME_MD_VM_THREAD),
		"VL"),
	SJME_NVM_MLE_DEFINE(vmThreadIsAlive,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(vmThreadIsMain,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(vmThreadIsStarted,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(vmThreadSetPriority,
		SJME_MD(SJME_MD_V, SJME_MD_VM_THREAD SJME_MD_I),
		"VLI"),
	SJME_NVM_MLE_DEFINE(vmThreadStart,
		SJME_MD(SJME_MD_Z, SJME_MD_VM_THREAD),
		"IL"),
	SJME_NVM_MLE_DEFINE(vmThreadTask,
		SJME_MD(SJME_MD_TASK, SJME_MD_VM_THREAD),
		"LL"),
	SJME_NVM_MLE_DEFINE(waitForUpdate,
		SJME_MD(SJME_MD_Z, SJME_MD_I),
		"II"),
	
	SJME_NVM_MLE_STOP()
};
