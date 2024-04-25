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

#define FORWARD_DESC___componentRepaint "(" \
	DESC_LONG DESC_LONG DESC_INT DESC_INT DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC___componentRevalidate "(" \
	DESC_LONG DESC_LONG ")" DESC_VOID
#define FORWARD_DESC___componentSetPaintListener "(" \
	DESC_LONG DESC_LONG DESC_SCRITCH_PAINT_LISTENER ")" DESC_VOID
#define FORWARD_DESC___containerAdd "(" \
	DESC_LONG DESC_LONG DESC_LONG ")" DESC_VOID
#define FORWARD_DESC___containerSetBounds "(" \
	DESC_LONG DESC_LONG DESC_LONG \
	DESC_INTEGER DESC_INTEGER DESC_INTEGER DESC_INTEGER ")" DESC_VOID
#define FORWARD_DESC___linkInit "(" \
	DESC_STRING DESC_STRING ")" DESC_LONG
#define FORWARD_DESC___loopExecute "(" \
	DESC_LONG DESC_CLASS("java/lang/Runnable") ")" DESC_VOID
#define FORWARD_DESC___loopExecuteWait "(" \
	DESC_LONG DESC_CLASS("java/lang/Runnable") ")" DESC_VOID
#define FORWARD_DESC___loopIsInThread "(" \
	DESC_LONG ")" DESC_BOOLEAN
#define FORWARD_DESC___panelEnableFocus "(" \
	DESC_LONG DESC_LONG DESC_BOOLEAN ")" DESC_VOID
#define FORWARD_DESC___panelNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___screens "(" \
	DESC_LONG DESC_ARRAY(DESC_LONG) ")" DESC_INTEGER
#define FORWARD_DESC___windowContentMinimumSize "(" \
	DESC_LONG DESC_LONG DESC_INTEGER DESC_INTEGER ")" DESC_VOID
#define FORWARD_DESC___windowManagerType "(" \
	DESC_LONG ")" DESC_INTEGER
#define FORWARD_DESC___windowNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___windowSetVisible "(" \
	DESC_LONG DESC_LONG DESC_BOOLEAN ")" DESC_VOID

/** Loop execution data. */
typedef struct mle_loopExecuteData
{
	/** The Java VM used. */
	JavaVM* vm;
	
	/** The @c Runnable to call. */
	jobject runnable;
} mle_loopExecuteData;

static sjme_errorCode mle_scritchUiPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
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
	jint error;
	JavaVM* vm;
	JNIEnv* env;
	
	if (inState == NULL || inComponent == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Restore VM. */
	vm = (JavaVM*)inState->common.frontEnd.data;
		
	/* Relocate env. */
	env = NULL;
	error = (*vm)->GetEnv(vm, &env, JNI_VERSION_1_1);
	if (env == NULL)
		sjme_die("Could not relocate env: %d??", error);
	
	sjme_todo("Implement paint");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_thread_result mle_loopExecuteMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	mle_loopExecuteData* data;
	JavaVM* vm;
	JNIEnv* env;
	jint error;
	jobject runnable;
	jclass classy;
	jmethodID runId;
	
	/* Recover data. */
	data = (mle_loopExecuteData*)anything;
	if (data == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Copy data locally. */
	vm = data->vm;
	runnable = data->runnable;
	
	/* Free it. */
	free(data);
	data = NULL;
	
	/* Relocate env. */
	env = NULL;
	error = (*vm)->GetEnv(vm, &env, JNI_VERSION_1_1);
	if (env == NULL)
		sjme_die("Could not relocate env: %d??", error);
	
	/* Debug. */
	sjme_message("Lookup Runnable...");
	
	/* Locate Runnable Class. */
	classy = (*env)->FindClass(env, "java/lang/Runnable");
	if (classy == NULL)
		sjme_die("Did not find Runnable??");

	/* Debug. */
	sjme_message("Lookup Runnable:run()...");
	
	/* Locate run() method. */
	runId = (*env)->GetMethodID(env, classy, "run", "()V");
	if (runId == NULL)
		sjme_die("Did not find Runnable:run()??");

	/* Debug. */
	sjme_message("Execute Runnable!");
	
	/* Call it. */
	(*env)->CallVoidMethod(env, runnable, runId);

	/* Debug. */
	sjme_message("Cleanup Reference...");
	
	/* Remove reference when the call is done. */
	(*env)->DeleteGlobalRef(env, runnable);

	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_thread_result mle_bindEventThread(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui state;
	JavaVM* vm;
	JNIEnv* env;
	JNIEnv* checkEnv;
	JavaVMAttachArgs attachArgs;
	jint error;
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	if (state == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Restore VM. */
	vm = (JavaVM*)state->common.frontEnd.data;
	
	/* If this thread is already attached, only attach once. */
	checkEnv = NULL;
	error = (*vm)->GetEnv(vm, &checkEnv, JNI_VERSION_1_1);
	if (error == JNI_OK)
		return SJME_THREAD_RESULT(SJME_ERROR_NONE);
	
	/* Setup arguments. */
	memset(&attachArgs, 0, sizeof(attachArgs));
	attachArgs.version = JNI_VERSION_1_1;
	attachArgs.name = "ScritchUIEventLoop";
	
	/* Debug. */
	sjme_message("Attaching ScritchUI thread to current VM...");

	/* Attach event loop to the JVM. */
	env = NULL;
	error = (*vm)->AttachCurrentThreadAsDaemon(vm, &env, &attachArgs);
	if (env == NULL)
		sjme_die("Could not attach thread: %d??", error);
	
	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentRepaint)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	
	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Forward call. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->componentRepaint == NULL ||
		sjme_error_is(error = state->api->componentRepaint(
			state, component, x, y, w, h)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentRevalidate)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	
	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Forward call. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->componentRevalidate == NULL ||
		sjme_error_is(error = state->api->componentRevalidate(
			state, component)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetPaintListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_frontEnd newFrontEnd;
	JavaVM* vm;
	
	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Setup new front-end to refer to this component. */
	memset(&newFrontEnd, 0, sizeof(newFrontEnd));
	vm = NULL;
	(*env)->GetJavaVM(env, &vm);
	newFrontEnd.data = vm;
	newFrontEnd.wrapper = (*env)->NewGlobalRef(env, javaListener);
	
	/* Forward. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->api->componentSetPaintListener == NULL ||
		sjme_error_is(error = state->api->componentSetPaintListener(
			state, component,
			mle_scritchUiPaintListener, &newFrontEnd)))
	{
		sjme_jni_throwMLECallError(env, error);
		return;
	}
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__containerAdd)(JNIEnv* env, jclass classy, jlong stateP,
	jlong containerP, jlong componentP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent container;
	sjme_scritchui_uiComponent component;
	
	if (stateP == 0 || containerP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Restore. */
	state = (sjme_scritchui)stateP;
	container = (sjme_scritchui_uiComponent)containerP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Not implemented? */
	if (state->api->containerAdd == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NOT_IMPLEMENTED);
		return;
	}
	
	/* Forward call. */
	if (sjme_error_is(error = state->api->containerAdd(state,
		container, component)))
	{
		sjme_jni_throwMLECallError(env, error);
		return;
	}
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__containerSetBounds)(JNIEnv* env, jclass classy, jlong stateP,
	jlong containerP, jlong componentP, jint x, jint y, jint w, jint h)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent container;
	sjme_scritchui_uiComponent component;
	
	if (stateP == 0 || containerP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Restore. */
	state = (sjme_scritchui)stateP;
	container = (sjme_scritchui_uiComponent)containerP;
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Not implemented? */
	if (state->api->containerSetBounds == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NOT_IMPLEMENTED);
		return;
	}
	
	/* Forward call. */
	if (sjme_error_is(error = state->api->containerSetBounds(
		state, container, component,
		x, y, w, h)))
	{
		sjme_jni_throwMLECallError(env, error);
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
	sjme_scritchui_dylibApiFunc apiInitFunc;
	const char* libPathChars;
	jboolean libPathCharsCopy;
	const char* nameChars;
	jboolean nameCharsCopy;
	sjme_alloc_pool* pool;
	sjme_scritchui state;
	sjme_frontEnd frontEnd;
	JavaVM* vm;
	
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
	apiInitFunc = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(lib, buf,
		&apiInitFunc)) || apiInitFunc == NULL)
		goto fail_dyLibLookup;
	
	/* Release path. */
	(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
	libPathChars = NULL;
	
	/* Debug. */
	sjme_message("Initializing ScritchUI Interface...");
	
	/* Setup front end. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	vm = NULL;
	(*env)->GetJavaVM(env, &vm);
	frontEnd.data = vm;

	/* Initialize ScritchUI. */
	state = NULL;
	if (sjme_error_is(error = apiInitFunc(pool,
		mle_bindEventThread, &frontEnd, &state)) ||
		state == NULL)
		goto fail_apiInit;
	
	/* Call it to get from it. */
	return (jlong)state;
#undef BUF_SIZE

fail_apiInit:
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
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __loopExecute)
	(JNIEnv* env, jclass classy, jlong stateP, jobject runnable)
{
	sjme_errorCode error;
	sjme_scritchui state;
	mle_loopExecuteData* data;
	
	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* Allocate data for call. */
	data = malloc(sizeof(*data));
	if (data == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_OUT_OF_MEMORY);
		return;
	}
	
	/* Fill in data. */
	data->vm = NULL;
	(*env)->GetJavaVM(env, &data->vm);
	data->runnable = (*env)->NewGlobalRef(env, runnable);
	
	/* Perform call. */
	if (sjme_error_is(error = state->api->loopExecute(state,
		mle_loopExecuteMain, data)))
	{
		free(data);
		sjme_jni_throwMLECallError(env, error);
	}
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __loopExecuteWait)
	(JNIEnv* env, jclass classy, jlong stateP, jobject runnable)
{
	sjme_errorCode error;
	sjme_scritchui state;
	mle_loopExecuteData* data;
	
	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* Allocate data for call. */
	data = malloc(sizeof(*data));
	if (data == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_OUT_OF_MEMORY);
		return;
	}
	
	/* Fill in data. */
	data->vm = NULL;
	(*env)->GetJavaVM(env, &data->vm);
	data->runnable = (*env)->NewGlobalRef(env, runnable);
	
	/* Perform call. */
	if (sjme_error_is(error = state->api->loopExecuteWait(state,
		mle_loopExecuteMain, data)))
	{
		free(data);
		sjme_jni_throwMLECallError(env, error);
	}
}

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__loopIsInThread) (JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_jboolean inThread;
	
	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Query API. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	inThread = SJME_JNI_FALSE;
	if (state->api->loopIsInThread == NULL ||
		sjme_error_is(error = state->api->loopIsInThread(state,
		&inThread)))
	{
		sjme_jni_throwMLECallError(env, error);
		return JNI_FALSE;
	}
	
	/* Is this in thread? */
	return inThread;
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
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
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
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
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
		sjme_jni_throwMLECallError(env, error);
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
	sjme_jni_throwMLECallError(env, error);
	return -1;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowContentMinimumSize)(JNIEnv* env, jclass classy,
	jlong stateP, jlong windowP, jint width, jint height)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;
	
	if (stateP == 0 || windowP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Restore. */
	state = (sjme_scritchui)stateP;
	window = (sjme_scritchui_uiWindow)windowP;
	
	/* Forward call. */
	if (sjme_error_is(error = state->api->windowContentMinimumSize(
		state, window, width, height)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowManagerType)(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_scritchui state;
	
	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* We can directly access this. */
	return state->wmType;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowNew)(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;

	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	
	/* Forward call. */
	window = NULL;
	if (sjme_error_is(error = state->api->windowNew(state,
		&window)) || window == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}
	
	/* Success! */
	return (jlong)window;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowSetVisible)(JNIEnv* env, jclass classy, jlong stateP,
	jlong windowP, jboolean visible)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;
	
	if (stateP == 0 || windowP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	window = (sjme_scritchui_uiWindow)windowP;
	
	/* Not implemented? */
	if (state->api->windowSetVisible == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NOT_IMPLEMENTED);
		return;
	}
	
	/* Forward call. */
	if (sjme_error_is(error = state->api->windowSetVisible(
		state, window, visible)))
		sjme_jni_throwMLECallError(env, error);
}

static const JNINativeMethod mleNativeScritchDylibMethods[] =
{
	FORWARD_list(NativeScritchDylib, __componentRepaint),
	FORWARD_list(NativeScritchDylib, __componentRevalidate),
	FORWARD_list(NativeScritchDylib, __componentSetPaintListener),
	FORWARD_list(NativeScritchDylib, __containerAdd),
	FORWARD_list(NativeScritchDylib, __containerSetBounds),
	FORWARD_list(NativeScritchDylib, __linkInit),
	FORWARD_list(NativeScritchDylib, __loopExecute),
	FORWARD_list(NativeScritchDylib, __loopExecuteWait),
	FORWARD_list(NativeScritchDylib, __loopIsInThread),
	FORWARD_list(NativeScritchDylib, __panelEnableFocus),
	FORWARD_list(NativeScritchDylib, __panelNew),
	FORWARD_list(NativeScritchDylib, __screens),
	FORWARD_list(NativeScritchDylib, __windowContentMinimumSize),
	FORWARD_list(NativeScritchDylib, __windowManagerType),
	FORWARD_list(NativeScritchDylib, __windowNew),
	FORWARD_list(NativeScritchDylib, __windowSetVisible),
};

FORWARD_init(mleNativeScritchDylibInit, mleNativeScritchDylibMethods)
