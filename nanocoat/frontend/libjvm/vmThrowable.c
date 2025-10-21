/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

void JNICALL JVM_FillInStackTrace(JNIEnv* env, jobject throwable)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_GetStackTraceDepth(JNIEnv* env, jobject throwable)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_GetStackTraceElement(JNIEnv* env,
	jobject throwable,
	jint index)
{
	sjme_todo("Impl?");
}
