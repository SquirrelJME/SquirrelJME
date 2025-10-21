/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sjme/debug.h>

#include "frontend/libjvm/sjCast.h"

sjme_errorCode sjme_jni_joToSo(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrOutNotNull sjme_jobject* outSo,
	sjme_attrInNotNull jobject inJo)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_jni_soToJo(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject* outJo,
	sjme_attrOutNotNull sjme_jobject inSo)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
