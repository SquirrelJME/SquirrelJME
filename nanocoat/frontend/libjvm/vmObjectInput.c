/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

jobject JNICALL JVM_AllocateNewObject(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jclass initClass)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_AllocateNewArray(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jint length)
{
	sjme_todo("Impl?");
	return NULL;
}

jobject JNICALL JVM_LatestUserDefinedLoader(JNIEnv* env)
{
	sjme_todo("Impl?");
	return NULL;
}

jclass JNICALL JVM_LoadClass0(JNIEnv* env,
	jobject obj,
	jclass currClass,
	jstring currClassName)
{
	sjme_todo("Impl?");
	return NULL;
}
