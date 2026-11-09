/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/vmOther.h"
#include "frontend/libjvm/internals.h"

void* SJME_JNI_LIB_FUNCTION JNICALL JVM_GetManagement(
	jint version)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_SupportsCX8(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

int sjme_jni_EnvTodoImpl(int ignored, ...)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

int sjme_jni_JvmTodoImpl(int ignored, ...)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}
