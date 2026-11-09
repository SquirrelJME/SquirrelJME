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

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_DoPrivileged(
	JNIEnv* env,
	jclass classy,
	jobject action,
	jobject context,
	jboolean wrapException)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_GetInheritedAccessControlContext(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_GetStackAccessControlContext(
	JNIEnv* env,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}
