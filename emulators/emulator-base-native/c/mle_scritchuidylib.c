/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

#include "squirreljme.h"
#include "lib/scritchui/scritchui.h"
#include "sjme/dylib.h"
#include "sjme/debug.h"
#include "sjme/alloc.h"

/** The class being implemented. */
#define IMPL_CLASS "cc/squirreljme/emulator/scritchui/dylib/" \
	"NativeScritchDylib"
#define FORWARD_CLASS IMPL_CLASS

#define FORWARD_DESC___apiInit "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___link "(" \
	DESC_STRING DESC_STRING ")" DESC_LONG
	
JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __apiInit)
	(JNIEnv* env, jclass classy, jlong structP)
{
	sjme_errorCode error;
	const sjme_scritchui_apiFunctions* apiFuncs;
	sjme_alloc_pool* pool;
	sjme_scritchui state;
	
	if (structP == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}
	
	/* Debug. */
	sjme_message("Initializing pool...");
	
	/* We need a pool for allocations. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		4 * 1048576)) || pool == NULL)
		goto fail_poolAlloc;

	/* Restore structure. */
	apiFuncs = (const sjme_scritchui_apiFunctions*)structP;

	/* Debug. */
	sjme_message("Init into %p (%p)...", apiFuncs,
		apiFuncs->apiInit);

	/* Initialize state. */
	state = NULL;
	if (sjme_error_is(error = apiFuncs->apiInit(pool,
		&state)) || state == NULL)
		goto fail_apiInit;
	
	/* Return the state pointer. */
	return (jlong)state;
	
fail_apiInit:
	if (pool != NULL)
		free(pool);
	
fail_poolAlloc:

	/* Fail. */
	sjme_jni_throwVMException(env, sjme_error_default(error));
	return 0L;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __link)
	(JNIEnv* env, jclass classy, jstring libPath, jstring name)
{
#define BUF_SIZE 128
	sjme_errorCode error;
	sjme_dylib lib;
	char buf[BUF_SIZE];
	sjme_scritchui_dylibApiFunc apiFunc;
	const char* libPathChars;
	jboolean libPathCharsCopy;
	const char* nameChars;
	jboolean nameCharsCopy;
	
	/* Resolve path. */
	libPathCharsCopy = JNI_FALSE;
	libPathChars = (*env)->GetStringUTFChars(env, libPath, &libPathCharsCopy);
	
	/* Resolve name. */
	nameCharsCopy = JNI_FALSE;
	nameChars = (*env)->GetStringUTFChars(env, name, &nameCharsCopy);
	
	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 2,
		"sjme_scritchui_dylibApi%s", nameChars);
	buf[BUF_SIZE - 1] = 0;
	
	/* Release name. */
	(*env)->ReleaseStringUTFChars(env, name, nameChars);
	
	/* Debug. */
	sjme_message("Attempting load of '%s'...", libPathChars);
	
	/* Load native library. */
	lib = NULL;
	if (sjme_error_is(error = sjme_dylib_open(libPathChars,
		&lib)) || lib == NULL)
	{
		sjme_message("Did not find lib '%s': %d",
			libPathChars, error);
			
		(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
		return 0;
	}
	
	/* Debug. */
	sjme_message("Attempting lookup of '%s'...", buf);
	
	/* Find function that returns the ScritchUI API interface. */
	apiFunc = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(lib, buf,
		&apiFunc)) || apiFunc == NULL)
	{
		sjme_message("Did not find symbol '%s' in '%s': %d",
			buf, libPathChars, error);
			
		(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
		return 0;
	}
	
	/* Release path. */
	(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
	
	/* Debug. */
	sjme_message("Obtaining ScritchUI API Interface...");
	
	/* Call it to get from it. */
	return (jlong)apiFunc();
#undef BUF_SIZE
}

static const JNINativeMethod mleNativeScritchDylibMethods[] =
{
	FORWARD_list(NativeScritchDylib, __apiInit),
	FORWARD_list(NativeScritchDylib, __link),
};

FORWARD_init(mleNativeScritchDylibInit, mleNativeScritchDylibMethods)
