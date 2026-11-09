/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_CompileClass(
	JNIEnv* env, 
	jclass compileClass, 
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_CompileClasses(
	JNIEnv* env, 
	jclass classy, 
	jstring name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_CompilerCommand(
	JNIEnv* env,
	jclass compileClass,
	jobject arg)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_DisableCompiler(
	JNIEnv* env, 
	jclass compileClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_EnableCompiler(
	JNIEnv* env, 
	jclass compileClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_InitializeCompiler(
	JNIEnv* env, 
	jclass compileClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsSilentCompiler(
	JNIEnv* env, 
	jclass compileClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}
