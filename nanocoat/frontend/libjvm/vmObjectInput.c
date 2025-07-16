/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
#endif

#include <jni.h>
#include <jvm.h>

#include "sjme/debug.h"

jobject JNICALL JVM_AllocateNewObject(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jclass initClass)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_AllocateNewArray(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jint length)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_LatestUserDefinedLoader(JNIEnv* env)
{
	sjme_todo("Impl?");
}

/*
 * This function has been deprecated and should not be considered
 * part of the specified JVM interface.
 */
jclass JNICALL JVM_LoadClass0(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jstring currClassName)
{
	sjme_todo("Impl?");
}
