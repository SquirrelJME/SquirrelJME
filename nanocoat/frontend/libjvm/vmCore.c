/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <jvm.h>

#include "sjme/debug.h"

jint JNICALL JVM_GetInterfaceVersion(void)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_GetVersionInfo(JNIEnv* env,
	jvm_version_info* info,
	size_t info_size)
{
	sjme_todo("Impl?");
}
