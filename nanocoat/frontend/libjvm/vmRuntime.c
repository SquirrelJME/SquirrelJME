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

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_ActiveProcessorCount(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_Exit(
	jint code)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void* SJME_JNI_LIB_FUNCTION JNICALL JVM_FindLibraryEntry(
	void* handle, 
	const char* name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_FreeMemory(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_GC(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_Halt(
	jint code)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsSupportedJNIVersion(
	jint version)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void* SJME_JNI_LIB_FUNCTION JNICALL JVM_LoadLibrary(
	const char* name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_MaxMemory(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_MaxObjectInspectionAge(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_TotalMemory(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_TraceInstructions(
	jboolean on)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_TraceMethodCalls(
	jboolean on)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_UnloadLibrary(
	void* handle)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
