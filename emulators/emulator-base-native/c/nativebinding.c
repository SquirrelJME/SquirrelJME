/* ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include "jni.h"
#include "cc_squirreljme_emulator_NativeBinding.h"
#include "squirreljme.h"
#include "sjme/dylib.h"

/** Perform MLE initialization call. */
#define MLE_INIT_FUNC_CALL(mleGroupId) \
	rv |= SJME_TOKEN_PASTE_PP(SJME_TOKEN_PASTE_PP(mle, mleGroupId), \
		Init)(env, classy)

static sjme_jboolean sjme_jni_abortHandler(sjme_errorCode error)
{
	jsize resultLen;
	JavaVM* vm;
	JNIEnv* env;

	/* Debug. */
	sjme_message("JNI Aborting...");

	/* Recover JVM. */
	resultLen = 0;
	vm = NULL;
	if (JNI_OK != JNI_GetCreatedJavaVMs(&vm, 1, &resultLen) ||
		resultLen == 0)
		return SJME_JNI_FALSE;

	/* Recover env, attach if not attached. */
	if (JNI_OK != (*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_1))
		return SJME_JNI_FALSE;

	/* Print stack trace, would use FatalError, however that prints to */
	/* stdout for some reason. */
	sjme_jni_throwVMException(env, sjme_error_default(error));
	(*env)->ExceptionDescribe(env);

	/* Call abort! */
	abort();

	/* Continue aborting. */
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_jni_messageHandler(sjme_lpcstr fullMessage,
	sjme_lpcstr partMessage)
{
	fprintf(stderr, "%s\n", fullMessage);
	fflush(stderr);

	return SJME_JNI_TRUE;
}

sjme_debug_handlerFunctions sjme_jni_debugHandlers =
{
	.abort = sjme_jni_abortHandler,
	.exit = NULL,
	.message = sjme_jni_messageHandler,
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	// Used to indicate that something might be happened
	fprintf(stderr, "JNI Sub-Level: Loading Library...\n");
#endif

	// Support Java 7!
	return JNI_VERSION_1_6;
}

JNIEXPORT jint JNICALL sjme_attrUnused
	Java_cc_squirreljme_emulator_NativeBinding__1_1bindMethods
	(JNIEnv* env, jclass classy)
{
	jint rv = 0;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* It is happening! */
	fprintf(stderr, "JNI Sub-Level: Binding Methods...\n");
#endif

	/* Use these debug handlers. */
	sjme_debug_handlers = &sjme_jni_debugHandlers;

	/* Initialize all functions. */
	rv |= mleAudioStreamInit(env, classy);
	rv |= mleBucketInit(env, classy);
	rv |= mleDebugInit(env, classy);
	rv |= mleJarInit(env, classy);
	rv |= mleMathInit(env, classy);
	rv |= mleMathAccelInit(env, classy);
	rv |= mleMidiInit(env, classy);
	rv |= mleNativeArchiveInit(env, classy);
	rv |= mleObjectInit(env, classy);
	rv |= mleReflectionInit(env, classy);
	rv |= mleTaskInit(env, classy);

	/* ScritchUI. */
	rv |= mleDylibBaseObjectInit(env, classy);
	rv |= mleNativeScritchCallbackInit(env, classy);
	rv |= mleNativeScritchDylibInit(env, classy);

	/* MLE */
	MLE_INIT_FUNC_CALL(NativeScritchDylibEx);
	MLE_INIT_FUNC_CALL(NativeScritchInterface);
	MLE_INIT_FUNC_CALL(PencilFontShelf);
	MLE_INIT_FUNC_CALL(PencilShelf);
	MLE_INIT_FUNC_CALL(RuntimeShelf);
	MLE_INIT_FUNC_CALL(TerminalShelf);
	MLE_INIT_FUNC_CALL(ThreadShelf);
	MLE_INIT_FUNC_CALL(TypeShelf);

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* It happened! */
	fprintf(stderr, "JNI Sub-Level: Methods are now bound!\n");
#endif

	return rv;
}
