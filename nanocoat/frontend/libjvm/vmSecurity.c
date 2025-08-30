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

jobject JNICALL JVM_DoPrivileged(JNIEnv* env,
	jclass cls,
	jobject action,
	jobject context,
	jboolean wrapException)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_GetInheritedAccessControlContext(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_GetStackAccessControlContext(JNIEnv* env, jclass cls)
{
	sjme_todo("Impl?");
}
