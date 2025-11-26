/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

void JNICALL JVM_Exit(jint code)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_Halt(jint code)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_GC(void)
{
	sjme_todo("Impl?");
}

jlong JNICALL JVM_MaxObjectInspectionAge(void)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_TraceInstructions(jboolean on)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_TraceMethodCalls(jboolean on)
{
	sjme_todo("Impl?");
}

jlong JNICALL JVM_TotalMemory(void)
{
	sjme_todo("Impl?");
}

jlong JNICALL JVM_FreeMemory(void)
{
	sjme_todo("Impl?");
}

jlong JNICALL JVM_MaxMemory(void)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_ActiveProcessorCount(void)
{
	sjme_todo("Impl?");
}

void* JNICALL JVM_LoadLibrary(const char* name)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_UnloadLibrary(void* handle)
{
	sjme_todo("Impl?");
}

void* JNICALL JVM_FindLibraryEntry(void* handle, const char* name)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_IsSupportedJNIVersion(jint version)
{
	sjme_todo("Impl?");
}
