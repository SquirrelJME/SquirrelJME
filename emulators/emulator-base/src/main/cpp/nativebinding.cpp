/* ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include "jni.h"
#include "cc_squirreljme_emulator_NativeBinding.h"
#include "squirreljme.h"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env;

	// Used to indicate that something might be happened
	fprintf(stderr, "JNI Sub-Level: Loading Library...\n");

	// Support Java 7!
	return JNI_VERSION_1_6;
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_emulator_NativeBinding__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	jint rv = 0;
	
	// It is happening!
	fprintf(stderr, "JNI Sub-Level: Binding Methods...\n");

	rv |= mleDebugInit(env, classy);
	rv |= mleFormInit(env, classy);
	rv |= mleJarInit(env, classy);
	rv |= mleMathInit(env, classy);
	rv |= mleMidiInit(env, classy);
	rv |= mleNativeArchiveInit(env, classy);
	rv |= mleObjectInit(env, classy);
	rv |= mlePencilInit(env, classy);
	rv |= mleReflectionInit(env, classy);
	rv |= mleRuntimeInit(env, classy);
	rv |= mleTaskInit(env, classy);
	rv |= mleTerminalInit(env, classy);
	rv |= mleTypeInit(env, classy);
	rv |= mleThreadInit(env, classy);
	
	// It happened!
	fprintf(stderr, "JNI Sub-Level: Methods are now bound!\n");

	return rv;
}
