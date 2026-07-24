/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

void JNICALL JVM_InitializeCompiler(JNIEnv* env, jclass compCls)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_IsSilentCompiler(JNIEnv* env, jclass compCls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jboolean JNICALL JVM_CompileClass(JNIEnv* env, jclass compCls, jclass cls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jboolean JNICALL JVM_CompileClasses(JNIEnv* env, jclass cls, jstring jname)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobject JNICALL JVM_CompilerCommand(JNIEnv* env, jclass compCls, jobject arg)
{
	sjme_todo("Impl?");
	return NULL;
}

void JNICALL JVM_EnableCompiler(JNIEnv* env, jclass compCls)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_DisableCompiler(JNIEnv* env, jclass compCls)
{
	sjme_todo("Impl?");
}
