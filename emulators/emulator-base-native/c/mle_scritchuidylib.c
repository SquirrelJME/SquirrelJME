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

#define DESC_SCRITCH_PAINT_LISTENER DESC_CLASS( \
	"cc/squirreljme/jvm/mle/scritchui/callbacks/ScritchPaintListener")

#define FORWARD_DESC___componentSetPaintListener "(" \
	DESC_LONG DESC_LONG DESC_SCRITCH_PAINT_LISTENER ")" DESC_VOID
#define FORWARD_DESC___linkInit "(" \
	DESC_STRING DESC_STRING ")" DESC_LONG
#define FORWARD_DESC___panelEnableFocus "(" \
	DESC_LONG DESC_LONG DESC_BOOLEAN ")" DESC_VOID
#define FORWARD_DESC___panelNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___screens "(" \
	DESC_LONG DESC_ARRAY(DESC_LONG) ")" DESC_INTEGER
#define FORWARD_DESC___windowManagerType "(" \
	DESC_LONG ")" DESC_INTEGER

static sjme_errorCode mle_scritchUiPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent component,
	sjme_attrInNotNull sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNotNull const void* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufLen,
	sjme_attrInNullable const sjme_jint* pal,
	sjme_attrInPositive sjme_jint numPal,
	sjme_attrInPositive sjme_jint sx,
	sjme_attrInPositive sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special)
{
	sjme_todo("Implement paint");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetPaintListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_frontEnd newFrontEnd;
	
	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Setup new front-end to refer to this component. */
	memset(&newFrontEnd, 0, sizeof(newFrontEnd));
	newFrontEnd.data = env;
	newFrontEnd.wrapper = (*env)->NewGlobalRef(env, javaListener);
	
	/* Forward. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->componentSetPaintListener == NULL ||
		sjme_error_is(error = state->api->componentSetPaintListener(
			state, component,
			mle_scritchUiPaintListener, &newFrontEnd)))
	{
		sjme_jni_throwVMException(env, error);
		return;
	}
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __linkInit)
	(JNIEnv* env, jclass classy, jstring libPath, jstring name)
{
#define BUF_SIZE 128
	sjme_errorCode error;
	sjme_dylib lib;
	char buf[BUF_SIZE];
	sjme_scritchui_dylibApiFunc getFuncs;
	const char* libPathChars;
	jboolean libPathCharsCopy;
	const char* nameChars;
	jboolean nameCharsCopy;
	const sjme_scritchui_apiFunctions* apiFuncs;
	const sjme_scritchui_implFunctions* implFuncs;
	sjme_alloc_pool* pool;
	sjme_scritchui state;
	
	/* Debug. */
	sjme_message("Initializing pool...");
	
	/* We need a pool for allocations. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		4 * 1048576)) || pool == NULL)
		goto fail_poolInit;
	
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
		goto fail_dyLibOpen;
	
	/* Debug. */
	sjme_message("Attempting lookup of '%s'...", buf);
	
	/* Find function that returns the ScritchUI API interface. */
	getFuncs = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(lib, buf,
		&getFuncs)) || getFuncs == NULL)
		goto fail_dyLibLookup;
	
	/* Release path. */
	(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
	libPathChars = NULL;
	
	/* Debug. */
	sjme_message("Obtaining ScritchUI API Interface...");

	/* Obtain ScritchUI API functions. */
	apiFuncs = NULL;
	implFuncs = NULL;
	if (sjme_error_is(error = getFuncs(&apiFuncs,
	   &implFuncs)) || apiFuncs == NULL || implFuncs == NULL)
	   goto fail_getFuncs;
	
	/* Initialize ScritchUI. */
	state = NULL;
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (apiFuncs->apiInit == NULL ||
		sjme_error_is(apiFuncs->apiInit(pool, apiFuncs,
		implFuncs, &state)) || state == NULL)
		goto fail_apiInit;
	
	/* Call it to get from it. */
	return (jlong)state;
#undef BUF_SIZE

fail_apiInit:
fail_getFuncs:
fail_dyLibLookup:
fail_dyLibOpen:
	if (libPathChars != NULL)
	{
		(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
		libPathChars = NULL;
	}

fail_poolInit:
	/* Delete pool. */
	if (pool != NULL)
		free(pool);
	
	/* Fail. */
	sjme_jni_throwVMException(env, sjme_error_default(error));
	return 0;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__panelEnableFocus)(JNIEnv* env, jclass classy, jlong stateP,
	jlong panelP, jboolean enableFocus)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiPanel panel;
	
	if (stateP == 0 || panelP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}
	
	/* Restore. */
	state = (sjme_scritchui)stateP;
	panel = (sjme_scritchui_uiPanel)panelP;
	
	/* Forward call. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->panelEnableFocus == NULL ||
		sjme_error_is(error = state->api->panelEnableFocus(state,
			panel, (sjme_jboolean)enableFocus)))
		goto fail_panelFocus;

	/* Success! */
	return;
	
fail_panelFocus:
fail_nullArgs:
	sjme_jni_throwVMException(env, sjme_error_default(error));
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __panelNew)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiPanel panel;
	
	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Create new panel. */
	panel = NULL;
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->panelNew == NULL ||
		sjme_error_is(error = state->api->panelNew(state,
			&panel)) || panel == NULL)
		goto fail_newPanel;
	
	/* Return the state pointer. */
	return (jlong)panel;

fail_newPanel:
fail_nullArgs:
	
	/* Fail. */
	sjme_jni_throwVMException(env, sjme_error_default(error));
	return 0L;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __screens)
	(JNIEnv* env, jclass classy, jlong stateP, jlongArray screenPs)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_jint numScreenPs, maxScreenPs, i;
	sjme_scritchui_uiScreen* screens;
	jlong tempJ;
	
	if (stateP == 0 || screenPs == NULL)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* How many screens are being used? */
	maxScreenPs = (*env)->GetArrayLength(env, screenPs);
	numScreenPs = maxScreenPs;
	
	/* Allocate where screens will go before mapping. */
	screens = sjme_alloca(sizeof(*screens) * numScreenPs);
	if (screens == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_alloca;
	}
	
	/* Clear. */
	memset(screens, 0, sizeof(*screens) * numScreenPs);
	
	/* Debug. */
	sjme_message("Before Screen Call");
	
	/* Request screen information. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->screens == NULL ||
		sjme_error_is(error = state->api->screens(state,
			screens, &numScreenPs)))
	{
		sjme_jni_throwVMException(env, error);
		goto fail_screens;
	}

	/* Debug. */
	sjme_message("After Screen Call");
	
	/* Smaller amount? */
	if (numScreenPs > maxScreenPs)
		numScreenPs = maxScreenPs; 
	
	/* Copy pointers over. */
	for (i = 0; i < numScreenPs; i++)
	{
		tempJ = (jlong)screens[i];
		(*env)->SetLongArrayRegion(env, screenPs, i, 1, &tempJ);
	}
	
	/* Return actual screen count. */
	return numScreenPs;

fail_screens:
fail_alloca:
fail_nullArgs:
	sjme_jni_throwVMException(env, error);
	return -1;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowManagerType)(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_scritchui state;
	
	if (stateP == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* We can directly access this. */
	return state->wmType;
}

static const JNINativeMethod mleNativeScritchDylibMethods[] =
{
	FORWARD_list(NativeScritchDylib, __componentSetPaintListener),
	FORWARD_list(NativeScritchDylib, __linkInit),
	FORWARD_list(NativeScritchDylib, __panelEnableFocus),
	FORWARD_list(NativeScritchDylib, __panelNew),
	FORWARD_list(NativeScritchDylib, __screens),
	FORWARD_list(NativeScritchDylib, __windowManagerType),
};

FORWARD_init(mleNativeScritchDylibInit, mleNativeScritchDylibMethods)
