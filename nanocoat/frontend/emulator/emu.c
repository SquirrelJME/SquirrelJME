/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/nvm/nvm.h"
#include "sjme/debug.h"
#include "frontend/emulator/jniHelper.h"

sjme_attrUnused JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	// Debug
	sjme_message("Initial load of NanoCoat JNI library...");
	
	// Support Java 7!
	return JNI_VERSION_1_6;
}

/**
 * Binds methods accordingly.
 *
 * @param env Java environment.
 * @param classy The class context this is called from.
 * @since 2023/12/05
 */
sjme_attrUnused JNIEXPORT jint JNICALL
	Java_cc_squirreljme_vm_nanocoat__1_1Native_1_1__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	return 0;
}
