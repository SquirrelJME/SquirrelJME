/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>

#include "squirreljme.h"

// The class to forward to
#define TASK_CLASSNAME "cc/squirreljme/emulator/EmulatedTaskShelf"

#define TASK_START_DESC "([Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;II)Lcc/squirreljme/jvm/mle/brackets/TaskBracket;"
#define TASK_STATUS_DESC "(Lcc/squirreljme/jvm/mle/brackets/TaskBracket;)I"

JNIEXPORT jobject JNICALL Impl_mle_TaskShelf_start(
	JNIEnv* env, jclass classy, jobjectArray classPath, jstring mainClass,
	jobjectArray args, jobjectArray sysPropPairs, jint stdOut, jint stdErr)
{
	return forwardCallStaticObject(env, TASK_CLASSNAME,
		"start", TASK_START_DESC,
		classPath, mainClass, args, sysPropPairs, stdOut, stdErr);
}

JNIEXPORT jint JNICALL Impl_mle_TaskShelf_status(
	JNIEnv* env, jclass classy, jobject task)
{
	return forwardCallStaticInteger(env, TASK_CLASSNAME,
		"status", TASK_STATUS_DESC,
		task);
}

static const JNINativeMethod mleTaskMethods[] =
{
	{"start", TASK_START_DESC, (void*)Impl_mle_TaskShelf_start},
	{"status", TASK_STATUS_DESC, (void*)Impl_mle_TaskShelf_status},
};

jint JNICALL mleTaskInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/TaskShelf"),
		mleTaskMethods, sizeof(mleTaskMethods) /
			sizeof(JNINativeMethod));
}
