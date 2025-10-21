/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

/**
 * Returns the interface version of the JNI library.
 * 
 * @return The JNI library interface version.
 * @since 2025/10/20
 */
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
