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

jboolean JNICALL JVM_SupportsCX8(void)
{
	sjme_todo("Impl?");
}

void* JNICALL JVM_GetManagement(jint version)
{
	sjme_todo("Impl?");
}

int sjme_jni_EnvTodoImpl(int ignored, ...)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

int sjme_jni_JvmTodoImpl(int ignored, ...)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
