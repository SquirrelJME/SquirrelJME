/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

jlong JNICALL JVM_CurrentTimeMillis(JNIEnv* env, jclass ignored)
{
	jlong r;
	sjme_todo("Impl?");
	return r;
}

jlong JNICALL JVM_NanoTime(JNIEnv* env, jclass ignored)
{
	jlong r;
	sjme_todo("Impl?");
	return r;
}

void JNICALL JVM_ArrayCopy(JNIEnv* env,
	jclass ignored,
	jobject src,
	jint src_pos,
	jobject dst,
	jint dst_pos,
	jint length)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_InitProperties(JNIEnv* env, jobject p)
{
	sjme_todo("Impl?");
	return NULL;
}
