/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

/* //// MLE /// */
#define mleGroupId NativeScritchDylibEx
#define mleShelfClass \
	"cc/squirreljme/emulator/scritchui/dylib/NativeScritchDylib"
#include "squirreljmeMle.h"
/* //////////// */

#include "squirreljme.h"
#include "lib/scritchui/scritchui.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/dylib.h"

#define SJME_JNI_MEMORY (64 * 1048576)

/** The class being implemented. */
#define IMPL_CLASS "cc/squirreljme/emulator/scritchui/dylib/" \
	"NativeScritchDylib"
#define FORWARD_CLASS IMPL_CLASS
#define FORWARD_CLASS_NAME NativeScritchDylib

#define DESC_ScritchActivateListener_activate DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_COMPONENT )
#define DESC_ScritchCloseListener_closed DESC_METHOD(DESC_BOOLEAN,  \
	DESC_SCRITCHUI_WINDOW )
#define DESC_ScritchInputListener_inputEvent DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_COMPONENT DESC_INT DESC_LONG DESC_INT DESC_INT DESC_INT \
	DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT DESC_INT \
	DESC_INT)
#define DESC_ScritchPaintListener_paint DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_COMPONENT /* __component */ \
	DESC_SCRITCHUI_PENCIL /* __g */ \
	DESC_INTEGER /* __sw */ \
	DESC_INTEGER /* __sh */ \
	DESC_INTEGER /* __special */ )
#define DESC_ScritchMenuItemActivateListener_menuItemActivate DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_WINDOW DESC_SCRITCHUI_MENUKIND )
#define DESC_ScritchSizeSuggestListener_sizeSuggest DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_VIEW /* __view */ \
	DESC_SCRITCHUI_COMPONENT /* __sub */ \
	DESC_INT DESC_INT /* __w, __h */ )
#define DESC_ScritchVisibleListener_visibilityChanged DESC_METHOD(DESC_VOID,  \
	DESC_SCRITCHUI_COMPONENT /* __component */ \
	DESC_BOOLEAN /* __from */ \
	DESC_BOOLEAN /* __to */ )

#define FORWARD_DESC___builtinFonts DESC_METHOD(DESC_ARRAY(DESC_PENCILFONT),  \
	DESC_LONG )

#define FORWARD_DESC___choiceGetSelectedIndex DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___choiceInsert DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG DESC_INT )
#define FORWARD_DESC___choiceLength DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___choiceRemove DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT )
#define FORWARD_DESC___choiceRemoveAll DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___choiceSetEnabled DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT DESC_BOOLEAN )
#define FORWARD_DESC___choiceSetImage DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT DESC_ARRAY(DESC_INT) DESC_INT \
	DESC_INT DESC_INT DESC_INT )
#define FORWARD_DESC___choiceSetSelected DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT DESC_BOOLEAN )
#define FORWARD_DESC___choiceSetString DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT DESC_STRING )

#define FORWARD_DESC___componentGetParent DESC_METHOD(DESC_SCRITCHUI_COMPONENT,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___componentHeight DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___componentRepaint DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_INT DESC_INT DESC_INT DESC_INT )
#define FORWARD_DESC___componentRevalidate DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___componentSetActivateListener DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_ACTIVATE_LISTENER )
#define FORWARD_DESC___componentSetInputListener DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_INPUT_LISTENER )
#define FORWARD_DESC___componentSetPaintListener DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_PAINT_LISTENER )
#define FORWARD_DESC___componentSetVisibleListener DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_VISIBLE_LISTENER )
#define FORWARD_DESC___componentWidth FORWARD_DESC___componentHeight

#define FORWARD_DESC___containerAdd DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_LONG )
#define FORWARD_DESC___containerGetFrame \
	DESC_METHOD(DESC_VOID, DESC_LONG DESC_LONG \
	DESC_ARRAY(DESC_INT) DESC_ARRAY(DESC_INT) DESC_ARRAY(DESC_INT))
#define FORWARD_DESC___containerRemoveAll DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG )
#define FORWARD_DESC___containerSetBounds DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_LONG \
	DESC_INTEGER DESC_INTEGER DESC_INTEGER DESC_INTEGER )

#define FORWARD_DESC___hardwareGraphics DESC_METHOD(DESC_PENCIL,  \
	DESC_LONG DESC_INT DESC_INT DESC_INT DESC_OBJECT \
	DESC_ARRAY(DESC_INT) DESC_INT DESC_INT DESC_INT DESC_INT )

#define FORWARD_DESC___linkInit DESC_METHOD(DESC_LONG,  \
	DESC_STRING DESC_STRING )

#define FORWARD_DESC___lafElementColor DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG DESC_INT )
#define FORWARD_DESC___lafPlatformFlags DESC_METHOD(DESC_INT,  \
	DESC_LONG )

#define FORWARD_DESC___labelSetString DESC_METHOD(DESC_VOID,  \
	DESC_LONG DESC_LONG DESC_STRING )

#define FORWARD_DESC___listNew DESC_METHOD(DESC_LONG,  \
	DESC_LONG DESC_INT )

#define FORWARD_DESC___loopExecute "(" \
	DESC_LONG DESC_CLASS("java/lang/Runnable") ")" DESC_VOID
#define FORWARD_DESC___loopExecuteLater FORWARD_DESC___loopExecute
#define FORWARD_DESC___loopExecuteWait FORWARD_DESC___loopExecute
#define FORWARD_DESC___loopIsInThread DESC_METHOD(DESC_BOOLEAN,  \
	DESC_LONG )
#define FORWARD_DESC___loopIterate DESC_METHOD(DESC_BOOLEAN,  \
	DESC_LONG )

#define FORWARD_DESC___menuBarNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___menuInsert "(" \
	DESC_LONG DESC_LONG DESC_INT DESC_LONG ")" DESC_VOID
#define FORWARD_DESC___menuItemNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___menuNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___menuRemoveAll "(" \
	DESC_LONG DESC_LONG ")" DESC_VOID

#define FORWARD_DESC___objectDelete "(" \
	DESC_LONG DESC_LONG ")" DESC_VOID

#define FORWARD_DESC___panelEnableFocus "(" \
	DESC_LONG DESC_LONG DESC_BOOLEAN DESC_BOOLEAN ")" DESC_VOID
#define FORWARD_DESC___panelNew "(" \
	DESC_LONG ")" DESC_LONG

#define FORWARD_DESC___screenId "(" \
	DESC_LONG DESC_LONG ")" DESC_INTEGER
#define FORWARD_DESC___screenGetBounds \
	DESC_METHOD(DESC_VOID, DESC_LONG DESC_LONG DESC_LONG \
	DESC_ARRAY(DESC_INT) DESC_ARRAY(DESC_INT))
#define FORWARD_DESC___screens "(" \
	DESC_LONG DESC_ARRAY(DESC_LONG) ")" DESC_INTEGER

#define FORWARD_DESC___scrollPanelNew "(" \
	DESC_LONG ")" DESC_LONG

#define FORWARD_DESC___viewSetArea "(" \
	DESC_LONG DESC_LONG DESC_INT DESC_INT ")" DESC_VOID
#define FORWARD_DESC___viewSetSizeSuggestListener "(" \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_SIZE_SUGGEST_LISTENER ")" DESC_VOID
#define FORWARD_DESC___viewSetViewListener "(" \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_VIEW_LISTENER ")" DESC_VOID

#define FORWARD_DESC___weakDelete "(" \
	DESC_LONG ")" DESC_VOID

#define FORWARD_DESC___windowContentMinimumSize "(" \
	DESC_LONG DESC_LONG DESC_INTEGER DESC_INTEGER ")" DESC_VOID
#define FORWARD_DESC___windowIsVisible \
	DESC_METHOD(DESC_BOOLEAN, DESC_LONG DESC_LONG)
#define FORWARD_DESC___windowManagerType "(" \
	DESC_LONG ")" DESC_INTEGER
#define FORWARD_DESC___windowNew "(" \
	DESC_LONG ")" DESC_LONG
#define FORWARD_DESC___windowSetCloseListener "(" \
	DESC_LONG DESC_LONG DESC_SCRITCHUI_CLOSE_LISTENER ")" DESC_VOID
#define FORWARD_DESC___windowSetMenuBar "(" \
	DESC_LONG DESC_LONG DESC_LONG ")" DESC_VOID
#define FORWARD_DESC___windowSetMenuItemActivateListener "(" \
	DESC_LONG DESC_LONG \
	DESC_SCRITCHUI_MENU_ITEM_ACTIVATE_LISTENER ")" DESC_VOID
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

/** Callback data. */
typedef struct mle_callbackData
{
	/** The Java object used. */
	jobject onWhat;

	/** The Java callback. */
	jobject javaCallback;

	/** The java callback method ID. */
	jmethodID javaCallbackId;
} mle_callbackData;

static sjme_errorCode mle_scritchUiStoreCallback(JNIEnv* env,
	sjme_frontEndBindable* outFrontEnd,
	jobject javaListener)
{
	sjme_errorCode error;

	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env,
		outFrontEnd, javaListener)))
	{
		sjme_jni_throwMLECallError(env, error);
		return sjme_error_default(error);
	}
}

static void mle_scritchUiRecoverCallback(JNIEnv* env,
	sjme_scritchui_uiComponent inComponent,
	sjme_frontEndBindable* frontEndP,
	sjme_lpcstr methodName,
	sjme_lpcstr methodType,
	mle_callbackData* callbackData)
{
	jclass listenerClass;

	/* Get the object that represents the component. */
	callbackData->onWhat = (jobject)inComponent->common.frontEnd.base.wrapper;

	/* Recover the callback we want to call. */
	callbackData->javaCallback = frontEndP->base.wrapper;

	/* Get class of the listener. */
	listenerClass = (*env)->GetObjectClass(env, callbackData->javaCallback);
	if (listenerClass == NULL)
		sjme_die("Listener has no class?");

	/* Get method to call. */
	callbackData->javaCallbackId = (*env)->GetMethodID(env, listenerClass,
		methodName, methodType);
	if (callbackData->javaCallbackId == NULL)
		sjme_die("Missing method %s %s?", methodName, methodType);
}

static void mle_scritchUiRecoverEnv(
	sjme_scritchui inState,
	JNIEnv** outEnv)
{
	JavaVM* vm;
	JNIEnv* env;
	jint error;

	/* Restore VM. */
	vm = (JavaVM*)inState->common.frontEnd.base.data;

	/* Relocate env. */
	env = NULL;
	error = (*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_1);
	if (env == NULL)
		sjme_die("Could not relocate env: %d??", error);

	/* Success! */
	*outEnv = env;
}

#define mle_simpleListenerSet(env, state, component, javaListener, \
	setListenerFunc, wrapListener) \
	do { \
		sjme_errorCode error; \
		sjme_frontEndBindable newFrontEnd; \
		\
		/* Setup new front-end to refer to this component. */ \
		if (sjme_error_is(error = mle_scritchUiStoreCallback(env, \
			&newFrontEnd, javaListener))) \
		{ \
			sjme_message("FAIL A"); \
			sjme_jni_throwMLECallError(env, error); \
			return; \
		} \
		\
		/* Forward. */ \
		if (sjme_error_is(error = setListenerFunc( \
			state, component, wrapListener, \
			&newFrontEnd))) \
		{ \
			sjme_message("FAIL B"); \
			sjme_jni_throwMLECallError(env, error); \
			return; \
		} \
	} while (0)

static sjme_errorCode mle_scritchUiListenerClose(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	JNIEnv* env;
	sjme_scritchui_listener_close* infoUser;
	mle_callbackData callbackData;
	jboolean skippy;

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inWindow, close);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, &inWindow->component,
		&infoUser->frontEnd,
		"closed",
		DESC_ScritchCloseListener_closed,
		&callbackData);

	/* Forward call. */
	skippy = (*env)->CallBooleanMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		/* Component. */
		callbackData.onWhat);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	if (skippy)
		return SJME_ERROR_CANCEL_WINDOW_CLOSE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerActivate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	JNIEnv* env;
	sjme_scritchui_listener_activate* infoUser;
	mle_callbackData callbackData;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, activate);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, inComponent,
		&infoUser->frontEnd,
		"activate",
		DESC_ScritchActivateListener_activate,
		&callbackData);

	/* Forward call. */
	(*env)->CallVoidMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		callbackData.onWhat);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerInput(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	JNIEnv* env;
	sjme_scritchui_listener_input* infoUser;
	mle_callbackData callbackData;
	const sjme_scritchinput_eventDataUnknown* unknown;

	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("Input Event");
#endif

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, input);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, inComponent,
		&infoUser->frontEnd,
		"inputEvent",
		DESC_ScritchInputListener_inputEvent,
		&callbackData);

	/* Forward call. */
	unknown = &inEvent->data.unknown;
	(*env)->CallVoidMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		callbackData.onWhat,
		inEvent->type,
		sjme_jni_jlong(inEvent->time),
		unknown->a,
		unknown->b,
		unknown->c,
		unknown->d,
		unknown->e,
		unknown->f,
		unknown->g,
		unknown->h,
		unknown->i,
		unknown->j,
		unknown->k,
		unknown->l);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerMenuItemActivate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind activatedItem)
{
	JNIEnv* env;
	sjme_scritchui_listener_menuItemActivate* infoUser;
	mle_callbackData callbackData;
	jboolean skippy;

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inWindow, menuItemActivate);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, &inWindow->component,
		&infoUser->frontEnd,
		"menuItemActivate",
		DESC_ScritchMenuItemActivateListener_menuItemActivate,
		&callbackData);

	/* Forward call. */
	skippy = (*env)->CallBooleanMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		(jobject)inWindow->component.common.frontEnd.base.wrapper,
		(jobject)activatedItem->common.frontEnd.base.wrapper);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	if (skippy)
		return SJME_ERROR_CANCEL_WINDOW_CLOSE;
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerPaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special)
{
	JNIEnv* env;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint* infoUser;
	jclass pencilClass;
	jmethodID pencilNew;
	jobject pencilObject;
	mle_callbackData callbackData;

	if (inState == NULL || inComponent == NULL || g == NULL)
		sjme_die("Null arguments to paint");

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get paint information. */
	paint = NULL;
	if (sjme_error_is(inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		sjme_die("Not paintable?");

	/* Get listener from paint. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(paint, paint);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, inComponent,
		&infoUser->frontEnd,
		"paint",
		DESC_ScritchPaintListener_paint,
		&callbackData);

	/* Setup pencil object. */
	pencilClass = (*env)->FindClass(env,
		DESC_DYLIB_PENCIL_UI);
	if (pencilClass == NULL)
		sjme_die("No DylibPencilUiObject?");

	pencilNew = (*env)->GetMethodID(env, pencilClass, "<init>", "(J)V");
	if (pencilNew == NULL)
		sjme_die("No default constructor for DylibPencilUiObject?");

	pencilObject = (*env)->NewObject(env, pencilClass, pencilNew,
		(jlong)g);
	if (pencilObject == NULL)
		sjme_die("Could not allocate DylibPencilObject.");

	/* Forward call. */
	(*env)->CallVoidMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		/* Component. */
		callbackData.onWhat,

		/* Pencil state and drawer. */
		pencilObject,

		/* Surface. */
		sw, sh,

		/* Special. */
		special);

	/* We no longer need the graphics reference. */
	(*env)->DeleteLocalRef(env, pencilObject);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerSizeSuggest(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inView,
	sjme_attrInNullable sjme_scritchui_uiComponent subComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* subDim)
{
	JNIEnv* env;
	sjme_errorCode error;
	sjme_scritchui_listener_sizeSuggest* infoUser;
	mle_callbackData callbackData;
	sjme_scritchui_uiView view;
	jobject subObject;

	if (inState == NULL || inView == NULL || subDim == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inView, &view)) || view == NULL)
		return sjme_error_default(error);

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(view, sizeSuggest);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, inView,
		&infoUser->frontEnd,
		"sizeSuggest",
		DESC_ScritchSizeSuggestListener_sizeSuggest,
		&callbackData);

	/* Do we have a subcomponent? */
	subObject = NULL;
	if (subComponent != NULL)
		subObject = (jobject)subComponent->common.frontEnd.base.wrapper;

	/* Forward call. */
	(*env)->CallVoidMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		(jobject)inView->common.frontEnd.base.wrapper,
		(subComponent == NULL ? NULL :
			(jobject)subComponent->common.frontEnd.base.wrapper),
		subDim->width,
		subDim->height);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mle_scritchUiListenerView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NATIVE_WIDGET_FAILURE;
}

static sjme_errorCode mle_scritchUiListenerVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean fromVisible,
	sjme_attrInValue sjme_jboolean toVisible)
{
	JNIEnv* env;
	sjme_scritchui_listener_visible* infoUser;
	mle_callbackData callbackData;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Relocate env. */
	mle_scritchUiRecoverEnv(inState, &env);

	/* Get listener from window. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, visible);

	/* Recover callback information. */
	mle_scritchUiRecoverCallback(env, inComponent,
		&infoUser->frontEnd,
		"visibilityChanged",
		DESC_ScritchVisibleListener_visibilityChanged,
		&callbackData);

	/* Forward call. */
	(*env)->CallVoidMethod(env,
		callbackData.javaCallback, callbackData.javaCallbackId,

		callbackData.onWhat,
		fromVisible, toVisible);

	/* Failed? */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_UNKNOWN;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_thread_result sjme_attrThreadCall mle_loopExecuteMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	mle_loopExecuteData* data;
	JavaVM* vm;
	JNIEnv* env;
	jint error;
	jobject runnable;
	jclass classy;
	jmethodID runId;
	jboolean tossed;

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
	error = (*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_1);
	if (env == NULL)
		sjme_die("Could not relocate env: %d??", error);

	/* Locate Runnable Class. */
	classy = (*env)->FindClass(env, "java/lang/Runnable");
	if (classy == NULL)
		sjme_die("Did not find Runnable??");

	/* Locate run() method. */
	runId = (*env)->GetMethodID(env, classy, "run", "()V");
	if (runId == NULL)
		sjme_die("Did not find Runnable:run()??");

	/* Call it. */
	(*env)->CallVoidMethod(env, runnable, runId);

	/* Check for any exception. */
	tossed = (*env)->ExceptionCheck(env);
	if (tossed)
	{
		/* Notice. */
		sjme_message("Exception thrown from Java loopExecute().");

		/* Describe it. */
		(*env)->ExceptionDescribe(env);
	}

	/* Remove reference when the call is done. */
	(*env)->DeleteGlobalRef(env, runnable);

	/* Success or failed? */
	if (tossed)
		return SJME_THREAD_RESULT(SJME_ERROR_JNI_EXCEPTION);
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_thread_result sjme_attrThreadCall mle_bindEventThread(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui state;
	JavaVM* vm;
	JNIEnv* env;
	JNIEnv* checkEnv;
	JavaVMAttachArgs attachArgs;
	jint error;

	/* Debug. */
	sjme_message("mle_bindEventThread called... %p",
		anything);

	/* Restore state. */
	state = (sjme_scritchui)anything;
	if (state == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Restore VM. */
	vm = (JavaVM*)state->common.frontEnd.base.data;

	/* If this thread is already attached, only attach once. */
	checkEnv = NULL;
	error = (*vm)->GetEnv(vm, (void**)&checkEnv, JNI_VERSION_1_1);
	if (error == JNI_OK)
	{
		sjme_message("ScritchUI already attached.");

		return SJME_THREAD_RESULT(SJME_ERROR_NONE);
	}

	/* Setup arguments. */
	memset(&attachArgs, 0, sizeof(attachArgs));
	attachArgs.version = JNI_VERSION_1_1;
	attachArgs.name = "ScritchUIEventLoop";

	/* Debug. */
	sjme_message("Attaching ScritchUI thread to current VM...");

	/* Attach event loop to the JVM. */
	env = NULL;
	error = (*vm)->AttachCurrentThreadAsDaemon(vm, (void**)&env, &attachArgs);
	if (env == NULL)
		sjme_die("Could not attach thread: %d??", error);

	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_errorCode mlePencilLock(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_errorCode error;
	JNIEnv* env;
	sjme_scritchui_pencilLockState* state;
	jarray buf;
	jboolean isCopy;
	sjme_pointer bufElem;
	sjme_jint typeSize;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* We need to operate on this state. */
	state = &g->lockState;

	/* Recover environment. */
	env = NULL;
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(&env,
		&state->source)))
		return sjme_error_default(error);

	/* Get buffer to access. */
	buf = state->source.base.wrapper;
	if (buf == NULL)
		return SJME_ERROR_RESOURCE_NOT_FOUND;

	/* Obtain buffer. */
	bufElem = NULL;
	isCopy = JNI_FALSE;
	typeSize = -1;
	if (sjme_error_is(error = sjme_jni_arrayGetElements(env,
		buf, &bufElem, &isCopy, &typeSize)) ||
		bufElem == NULL || typeSize < 0)
		return sjme_error_default(error);

	/* Store state. */
	state->base = bufElem;
	state->baseLimitBytes = (*env)->GetArrayLength(env, buf) * typeSize;
	state->isCopy = isCopy;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mlePencilLockRelease(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_errorCode error;
	JNIEnv* env;
	sjme_scritchui_pencilLockState* state;
	jarray buf;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* We need to operate on this state. */
	state = &g->lockState;

	/* Recover environment. */
	env = NULL;
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(&env,
		&state->source)))
		return sjme_error_default(error);

	/* Get buffer to access. */
	buf = state->source.base.wrapper;

	/* Release buffer. */
	if (sjme_error_is(error = sjme_jni_arrayReleaseElements(env,
		buf, state->base)))
		return sjme_error_default(error);

	/* Buffer no longer valid. */
	state->base = NULL;
	state->baseLimitBytes = 0;
	state->isCopy = SJME_JNI_FALSE;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode mlePencilExternalAsset(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_scritchui_externalAssetType assetType,
	sjme_attrInNotNull sjme_lpcstr inAsset,
	sjme_attrOutNullable sjme_stream_input* outStream)
{
	if (inState == NULL || inAsset == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (assetType <= SJME_SCRITCHUI_ASSET_TYPE_UNDEFINED ||
		assetType >= SJME_SCRITCHUI_NUM_ASSET_TYPES)
		return SJME_ERROR_RESOURCE_NOT_FOUND;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_scritchui_pencilLockFunctions mlePencilLockFuncs =
{
	.lock = mlePencilLock,
	.lockRelease = mlePencilLockRelease,
};

static const sjme_scritchui_externalFunctions mleExternals =
{
	sjme_sm(.externalAsset, mlePencilExternalAsset),
	sjme_sm(.externalLoopExecute, NULL),
	sjme_sm(.externalLoopExecuteLater, NULL),
	sjme_sm(.externalLoopExecuteWait, NULL),
};

JNIEXPORT jobjectArray JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__builtinFonts)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_scritchui_pencilFont font;
	sjme_errorCode error;
	sjme_scritchui state;
	JavaVM* vm;
	jobject instance;
	jclass instanceClass;
	jmethodID instanceNew;

	if (stateP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Find wrapper class. */
	instanceClass = (*env)->FindClass(env, DESC_DYLIB_PENCILFONT);
	if (instanceClass == NULL)
	{
		sjme_die("No DylibPencilFontObject?");
		sjme_jni_throwMLECallError(env, SJME_ERROR_UNKNOWN);
		return NULL;
	}

	/* Locate font. */
	font = NULL;
	if (sjme_error_is(error = state->api->fontBuiltin(state,
		&font)) || font == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return NULL;
	}

	/* Need to attach an object to it? */
	instance = font->common.frontEnd.base.wrapper;
	if (instance == NULL)
	{
		/* Find constructor. */
		instanceNew = (*env)->GetMethodID(env, instanceClass,
			"<init>", "(J)V");
		if (instanceNew == NULL)
		{
			sjme_die("Could not find new for pencil font wrapper.");
			sjme_jni_throwMLECallError(env, SJME_ERROR_UNKNOWN);
			return NULL;
		}

		/* Setup instance. */
		instance = (*env)->NewObject(env, instanceClass, instanceNew,
			font);
		if (instance == NULL)
		{
			sjme_jni_throwMLECallError(env, SJME_ERROR_JNI_EXCEPTION);
			return NULL;
		}
	}

	/* Wrap array. */
	return (*env)->NewObjectArray(env, 1, instanceClass, instance);
}

JNIEXPORT int JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceGetSelectedIndex)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;
	sjme_jint result;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Forward. */
	result = -1;
	if (sjme_error_is(error = state->api->choiceGetSelectedIndex(
		state, choice, &result)))
		sjme_jni_throwMLECallError(env, error);

	return result;
}

JNIEXPORT int JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceInsert)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint atIndex)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;
	sjme_jint result;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Forward. */
	result = atIndex;
	if (sjme_error_is(error = state->api->choiceItemInsert(
		state, choice, &result)))
		sjme_jni_throwMLECallError(env, error);

	return result;
}

JNIEXPORT int JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceLength)(JNIEnv* env, jclass classy, jlong stateP, jlong choiceP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;
	sjme_jint result;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Forward. */
	result = -1;
	if (sjme_error_is(error = state->api->choiceLength(
		state, choice, &result)) ||
		result < 0)
		sjme_jni_throwMLECallError(env, error);
	return result;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceRemove)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint atIndex)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemRemove(
		state, choice, atIndex)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceRemoveAll)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemRemoveAll(
		state, choice)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceSetEnabled)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint atIndex, jboolean enabled)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemSetEnabled(
		state, choice, atIndex, enabled)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceSetImage)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint  atIndex, jintArray data, jint off, jint scanLen,
	jint width, jint height)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;
	sjme_pointer dataBuf;
	jboolean isCopy;
	sjme_jint dataLen;

	/* Restore. */

	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get array data. */
	dataBuf = NULL;
	isCopy = JNI_FALSE;
	dataLen = 0;
	if (data != NULL)
	{
		dataLen = (*env)->GetArrayLength(env, data);
		sjme_jni_arrayGetElements(env, data, &dataBuf, &isCopy,
			NULL);
	}

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemSetImage(
		state, choice, atIndex,
		dataBuf, 0, dataLen,
		scanLen, width, height)))
		sjme_jni_throwMLECallError(env, error);

	if (dataBuf != NULL)
		sjme_jni_arrayReleaseElements(env, data, dataBuf);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceSetSelected)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint atIndex, jboolean selected)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemSetSelected(
		state, choice, atIndex, selected)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__choiceSetString)(JNIEnv* env, jclass classy, jlong stateP,
	jlong choiceP, jint atIndex, jstring string)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent choice;
	sjme_lpcstr chars;
	jboolean isCopy;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	choice = (sjme_scritchui_uiComponent)choiceP;

	/* Check. */
	if (state == NULL || choice == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Obtain characters. */
	chars = NULL;
	isCopy = JNI_FALSE;
	if (string != NULL)
		chars = (*env)->GetStringUTFChars(env, string, &isCopy);

	/* Forward. */
	if (sjme_error_is(error = state->api->choiceItemSetString(
		state, choice, atIndex, chars)))
		sjme_jni_throwMLECallError(env, error);

	/* Cleanup. */
	if (chars != NULL)
		(*env)->ReleaseStringUTFChars(env, string, chars);
}

JNIEXPORT jobject JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentGetParent)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP)
{
	sjme_todo("Impl?");
	sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
	return NULL;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentHeight)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_jint result;

	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;

	if (state->api->componentSize == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return 0;
	}

	/* Forward. */
	result = 0;
	if (sjme_error_is(error = state->api->componentSize(state,
		component, NULL, &result)))
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	return result;
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
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
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
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
	if (state->api->componentRevalidate == NULL ||
		sjme_error_is(error = state->api->componentRevalidate(
			state, component)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetActivateListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == NULL || component == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->componentSetActivateListener,
		mle_scritchUiListenerActivate);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetInputListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == NULL || component == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->componentSetInputListener,
		mle_scritchUiListenerInput);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetPaintListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == NULL || component == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->componentSetPaintListener,
		mle_scritchUiListenerPaint);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentSetVisibleListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == NULL || component == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->componentSetVisibleListener,
		mle_scritchUiListenerVisible);
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__componentWidth)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_jint result;

	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;

	if (state->api->componentSize == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return 0;
	}

	/* Forward. */
	result = 0;
	if (sjme_error_is(error = state->api->componentSize(state,
		component, &result, NULL)))
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	return result;
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
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
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
	__containerGetFrame)(JNIEnv* env, jclass classy,
	jlong stateP, jlong containerP,
	jintArray contentSize, jintArray frameBound, jintArray contentBound)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent container;
	sjme_scritchui_dim mapContentSize;
	sjme_scritchui_rect mapFrameBound;
	sjme_scritchui_rect mapContentBound;
	sjme_jint temp[4];
	sjme_jint lnContentSize, lnFrameBound, lnContentBound;

	if (stateP == 0 || containerP == 0 ||
		(contentSize == NULL && frameBound == NULL && contentBound == NULL))
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Get array lengths. */
	lnContentSize = (contentSize == NULL ? -1 :
		(*env)->GetArrayLength(env, contentSize));
	lnFrameBound = (frameBound == NULL ? -1 :
		(*env)->GetArrayLength(env, frameBound));
	lnContentBound = (contentBound == NULL ? -1 :
		(*env)->GetArrayLength(env, contentBound));
	if ((lnContentSize >= 0 && lnContentSize < 2) ||
		(lnFrameBound >= 0 && lnFrameBound < 4) ||
		(lnContentBound >= 0 && lnContentBound < 4))
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_INDEX_OUT_OF_BOUNDS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	container = (sjme_scritchui_uiComponent)containerP;

	/* Not implemented? */
	if (state->api->containerGetFrame == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	memset(&mapContentSize, 0, sizeof(mapContentSize));
	memset(&mapFrameBound, 0, sizeof(mapFrameBound));
	memset(&mapContentBound, 0, sizeof(mapContentBound));
	if (sjme_error_is(error = state->api->containerGetFrame(state, container,
		&mapContentSize,
		&mapFrameBound, &mapContentBound)))
	{
		sjme_jni_throwMLECallError(env, sjme_error_default(error));
		return;
	}

	/* Write content size. */
	if (lnContentSize >= 2)
		sjme_jni_setIntArray(env, contentSize, 0, 2,
			(jint)mapContentSize.width, (jint)mapContentSize.height);

	/* Write frame bound. */
	if (lnFrameBound >= 4)
		sjme_jni_setIntArray(env, frameBound, 0, 4,
			(jint)mapFrameBound.s.x, (jint)mapFrameBound.s.y,
			(jint)mapFrameBound.d.width, (jint)mapFrameBound.d.height);

	/* Write content bound. */
	if (lnContentBound >= 4)
		sjme_jni_setIntArray(env, contentBound, 0, 4,
			(jint)mapContentBound.s.x, (jint)mapContentBound.s.y,
			(jint)mapContentBound.d.width, (jint)mapContentBound.d.height);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__containerRemoveAll)(JNIEnv* env, jclass classy, jlong stateP,
	jlong containerP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent container;

	if (stateP == 0 || containerP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	container = (sjme_scritchui_uiComponent)containerP;

	/* Not implemented? */
	if (state->api->containerRemoveAll == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = state->api->containerRemoveAll(state,
		container)))
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
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
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

#define MLE_DESC___fontByFace DESC_METHOD(DESC_LONG, \
	DESC_LONG DESC_INT DESC_ARRAY(DESC_INT) DESC_ARRAY(DESC_INT))
MLE_FUNC_PROTO(jlong, __fontByFace,
	jlong jStateP, jint inFace, jintArray inParamsF, jintArray outParamsF)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_pencilFontParam inParamsS, outParamsS;
	sjme_scritchui_pencilFont found;

	inState = (sjme_scritchui)jStateP;
	if (inState == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	if (inState->api == NULL || inState->api->fontByFace == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_ILLEGAL_STATE);
		return 0;
	}

	/* Setup in parameters. */
	memset(&inParamsS, 0, sizeof(inParamsS));
	if (inParamsF != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env, inState,
			&inParamsS, inParamsF)))
		{
			sjme_jni_throwMLECallError(env, error);
			return 0;
		}

	/* Locate font by face. */
	found = NULL;
	memset(&outParamsS, 0, sizeof(outParamsS));
	if (sjme_error_is(error = inState->api->fontByFace(inState,
		&found, &outParamsS, inFace, (inParamsF != NULL ? &inParamsS : NULL))))
	{
		sjme_jni_throwMLECallError(env, error);
		return 0;
	}

	/* Map output parameters. */
	if (outParamsF != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamToFlat(env, inState,
			outParamsF, &outParamsS)))
		{
			sjme_jni_throwMLECallError(env, error);
			return 0;
		}

	/* Return the found font. */
	return (jlong)found;
}

#define MLE_DESC___fontDerive DESC_METHOD(DESC_LONG,  \
	DESC_LONG DESC_LONG DESC_ARRAY(DESC_INT) DESC_ARRAY(DESC_INT))
MLE_FUNC_PROTO(jlong, __fontDerive, jlong jStateP, jlong jFontP,
	jintArray jDeriveFPI, jintArray jNewFPI)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_pencilFont inFont;
	sjme_scritchui_pencilFont newFont;
	sjme_scritchui_pencilFontParam deriveParams, newParams;

	/* Check. */
	inState = (sjme_scritchui)jStateP;
	inFont = (sjme_scritchui_pencilFont)jFontP;
	if (inState == NULL || inFont == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return (jlong)0;
	}

	/* Make sure this exists. */
	if (inState->api == NULL || inState->api->fontDerive == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_ILLEGAL_STATE);
		return (jlong)0;
	}

	/* Map old parameters? */
	memset(&deriveParams, 0, sizeof(deriveParams));
	if (jDeriveFPI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamFromFlat(env, inState,
			&deriveParams, jDeriveFPI)))
		{
			sjme_jni_throwMLECallError(env, error);
			return (jlong)0;
		}

	/* Perform derivation. */
	newFont = NULL;
	memset(&newParams, 0, sizeof(newParams));
	if (sjme_error_is(error = inState->api->fontDerive(inState,
		&newFont, &newParams,
		inFont, (jDeriveFPI != NULL ? &deriveParams : NULL))) ||
		newFont == NULL)
	{
		sjme_jni_throwMLECallError(env, error);
		return (jlong)0;
	}

	/* Map new parameters? */
	if (jNewFPI != NULL)
		if (sjme_error_is(error = sjme_jni_fontParamToFlat(env, inState,
			jNewFPI, &newParams)))
		{
			sjme_jni_throwMLECallError(env, error);
			return (jlong)0;
		}

	/* Return resultant font. */
	return (jlong)newFont;
}

JNIEXPORT jobject JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__hardwareGraphics)(JNIEnv* env, jclass classy, jlong stateP,
	jint pf, jint bw, jint bh, jobject buf,
	jintArray pal, jint sx, jint sy, jint sw, jint sh)
{
	sjme_errorCode error;
	sjme_frontEndBindable frontSource;
	sjme_scritchui_pencil result;
	sjme_alloc_weak resultWeak;
	sjme_scritchui state;
	jclass pencilClass;
	jmethodID pencilNewId;
	jobject pencilObject;

	if (stateP == 0 || buf == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return NULL;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Fill in source. */
	memset(&frontSource, 0, sizeof(frontSource));
	sjme_jni_fillFrontEnd(env, &frontSource, buf);

	/* Setup pencil. */
	result = NULL;
	resultWeak = NULL;
	if (sjme_error_is(error = state->api->hardwareGraphics(
		state,
		&result, &resultWeak, pf, bw, bh,
		&mlePencilLockFuncs, &frontSource,
		sx, sy, sw, sh, NULL)) ||
		result == NULL || resultWeak == NULL)
		goto fail;

	/* Find pencil class. */
	pencilClass = (*env)->FindClass(env, DESC_DYLIB_PENCIL_BASIC);
	if (pencilClass == NULL)
		goto fail;

	/* Find constructor. */
	pencilNewId = (*env)->GetMethodID(env, pencilClass,
		"<init>", "(JJ)V");
	if (pencilNewId == NULL)
		goto fail;

	/* Construct. */
	pencilObject = (*env)->NewObject(env, pencilClass, pencilNewId,
		(jlong)result, (jlong)resultWeak);
	if (pencilObject == NULL)
		goto fail;

	/* Set front end. */
	sjme_jni_fillFrontEnd(env, &result->frontEnd, pencilObject);

	/* Setup weak object binding. */
	if (sjme_error_is(error = sjme_jni_pushWeakLink(env,
		pencilObject, resultWeak)))
		goto fail;

	/* Success! */
	return pencilObject;

fail:
	if (result != NULL)
		sjme_todo("Impl?");

	sjme_jni_throwMLECallError(env, error);
	return NULL;

}

JNIEXPORT int JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__lafElementColor)(JNIEnv* env, jclass classy, jlong stateP,
	jlong contextP, jint type)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent context;
	sjme_jint rgb;

	/* Recover state. */
	state = (sjme_scritchui)stateP;
	if (state == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Context is optional. */
	context = (sjme_scritchui_uiComponent)contextP;

	/* Read in color. */
	rgb = 0;
	if (sjme_error_is(error = state->api->lafElementColor(state,
		context, &rgb, type)))
		sjme_jni_throwMLECallError(env, error);
	return rgb;
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__lafPlatformFlags)(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;

	/* Recover state. */
	state = (sjme_scritchui)stateP;
	if (state == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Ask the native API. */
	return state->platformFlags;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__labelSetString)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jstring string)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_lpcstr chars;
	jboolean isCopy;

	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;

	/* Not implemented? */
	if (state->api->labelSetString == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Obtain characters. */
	isCopy = JNI_FALSE;
	if (string != NULL)
		chars = (*env)->GetStringUTFChars(env, string, &isCopy);
	else
		chars = NULL;

	/* Forward call. */
	error = state->api->labelSetString(
		state, (sjme_scritchui_uiCommon)component, chars);

	/* Cleanup. */
	if (string != NULL)
		(*env)->ReleaseStringUTFChars(env, string, chars);

	/* Fail? */
	if (sjme_error_is(error))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __linkInit)
	(JNIEnv* env, jclass classy, jstring libPath, jstring name)
{
#define BUF_SIZE 128
	char buf[BUF_SIZE];
	sjme_errorCode error;
	sjme_dylib lib;
	sjme_scritchui_dylibApiFunc apiInitFunc;
	const char* libPathChars;
	jboolean libPathCharsCopy;
	const char* nameChars;
	jboolean nameCharsCopy;
	sjme_alloc_pool pool;
	sjme_scritchui state;
	sjme_frontEndBindable frontEnd;
	sjme_debug_handlerFunctions** dylibDebugHandlers;

	/* Use these debug handlers. */
	sjme_debug_handlers = &sjme_jni_debugHandlers;

	/* Debug. */
	sjme_message("Initializing pool...");

	/* We need a pool for allocations. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		SJME_JNI_MEMORY)) || pool == NULL)
		goto fail_poolInit;

	/* Resolve path. */
	libPathCharsCopy = JNI_FALSE;
	libPathChars = (*env)->GetStringUTFChars(env, libPath, &libPathCharsCopy);

	/* Resolve name. */
	nameCharsCopy = JNI_FALSE;
	nameChars = (*env)->GetStringUTFChars(env, name, &nameCharsCopy);

	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 2, "%s%s",
		SJME_TOKEN_STRING_PP(SJME_SCRITCHUI_DYLIB_SYMBOL_PREFIX),
		nameChars);
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

	/* Copy debug handlers since it may be in a different symbol domain. */
	dylibDebugHandlers = NULL;
	if (!sjme_error_is(sjme_dylib_lookup(lib,
		"sjme_debug_handlers",
		(void**)&dylibDebugHandlers)))
		*dylibDebugHandlers = &sjme_jni_debugHandlers;

	/* Debug. */
	sjme_message("Attempting lookup of '%s'...", buf);

	/* Find function that returns the ScritchUI API interface. */
	apiInitFunc = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(lib, buf,
		(sjme_pointer*)&apiInitFunc)) || apiInitFunc == NULL)
		goto fail_dyLibLookup;

	/* Release path. */
	(*env)->ReleaseStringUTFChars(env, libPath, libPathChars);
	libPathChars = NULL;

	/* Debug. */
	sjme_message("Initializing ScritchUI Interface...");

	/* Setup front end. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env,
		&frontEnd, NULL)))
		goto fail_initFrontEnd;

	/* Initialize ScritchUI. */
	state = NULL;
	if (sjme_error_is(error = apiInitFunc(pool, &state,
		mle_bindEventThread,
		&mleExternals,
		&frontEnd)) || state == NULL)
		goto fail_apiInit;

	/* Call it to get from it. */
	return (jlong)state;
#undef BUF_SIZE

fail_apiInit:
fail_initFrontEnd:
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
		free((void*)pool);

	/* Fail. */
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __listNew)
	(JNIEnv* env, jclass classy, jlong stateP, jint type)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiList list;

	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	if (state->api->listNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_newList;
	}

	/* Create new list. */
	list = NULL;
	if (sjme_error_is(error = state->api->listNew(state,
		&list, type)) || list == NULL)
		goto fail_newList;

	/* Return the state pointer. */
	return (jlong)list;

fail_newList:
fail_nullArgs:

	/* Fail. */
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0L;
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

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__loopExecuteLater)(JNIEnv* env, jclass classy, jlong stateP,
	jobject runnable)
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
	if (sjme_error_is(error = state->api->loopExecuteLater(
		state, mle_loopExecuteMain, data)))
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
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
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

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__loopIterate)(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_jboolean terminated;

	state = (sjme_scritchui)stateP;
	if (state == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}

	/* Query API. */
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
	terminated = SJME_JNI_FALSE;
	if (state->api->loopIterate == NULL ||
		sjme_error_is(error = state->api->loopIterate(state,
			SJME_JNI_FALSE, &terminated)))
	{
		sjme_jni_throwMLECallError(env, error);
		return JNI_FALSE;
	}

	/* Did this terminate? */
	return terminated;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __menuBarNew)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiMenuBar menuBar;

	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Not implemented? */
	if (state->api->menuBarNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_new;
	}

	/* Create new menu bar. */
	menuBar = NULL;
	if (sjme_error_is(error = state->api->menuBarNew(state,
			&menuBar)) || menuBar == NULL)
		goto fail_new;

	/* Return the state pointer. */
	return (jlong)menuBar;

fail_new:
fail_nullArgs:
	/* Fail. */
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0L;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __menuInsert)
	(JNIEnv* env, jclass classy, jlong stateP, jlong intoP, int at,
	jlong itemP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiMenuKind into;
	sjme_scritchui_uiMenuKind item;

	if (stateP == 0 || intoP == 0 || itemP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	into = (sjme_scritchui_uiMenuKind)intoP;
	item = (sjme_scritchui_uiMenuKind)itemP;

	/* Not implemented? */
	if (state->api->menuInsert == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = state->api->menuInsert(state,
		into, at, item)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __menuItemNew)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiMenuItem menuItem;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Not implemented? */
	if (state->api->menuItemNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_new;
	}

	/* Create new menu item. */
	menuItem = NULL;
	if (sjme_error_is(error = state->api->menuItemNew(state,
			&menuItem)) || menuItem == NULL)
		goto fail_new;

	/* Return the state pointer. */
	return (jlong)menuItem;

fail_new:
fail_nullArgs:
	/* Fail. */
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0L;
}

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __menuNew)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiMenu menu;

	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;

	/* Not implemented? */
	if (state->api->menuNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_new;
	}

	/* Create new menu. */
	menu = NULL;
	if (sjme_error_is(error = state->api->menuNew(state,
			&menu)) || menu == NULL)
		goto fail_new;

	/* Return the state pointer. */
	return (jlong)menu;

fail_new:
fail_nullArgs:
	/* Fail. */
	sjme_jni_throwMLECallError(env, sjme_error_default(error));
	return 0L;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __menuRemoveAll)
	(JNIEnv* env, jclass classy, jlong stateP, jlong fromMenuP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiMenuKind fromMenu;

	if (stateP == 0 || fromMenuP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	fromMenu = (sjme_scritchui_uiMenuKind)fromMenuP;

	/* Not implemented? */
	if (state->api->menuRemoveAll == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = state->api->menuRemoveAll(state,
		fromMenu)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __objectDelete)
	(JNIEnv* env, jclass classy, jlong stateP, jlong objectP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiCommon object;

	if (stateP == 0 || objectP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	object = (sjme_scritchui_uiCommon)objectP;

	/* Call delete handler. */
	if (sjme_error_is(error = state->api->objectDelete(state,
		&object)) || object != NULL)
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__panelEnableFocus)(JNIEnv* env, jclass classy, jlong stateP,
	jlong panelP, jboolean enableFocus, jboolean defaultFocus)
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
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
	if (state->api->panelEnableFocus == NULL ||
		sjme_error_is(error = state->api->panelEnableFocus(state,
			panel, (sjme_jboolean)enableFocus,
			(sjme_jboolean)defaultFocus)))
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

	/* Restore state. */
	state = (sjme_scritchui)stateP;
	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Not implemented? */
	if (state->api->panelNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_newPanel;
	}

	/* Create new panel. */
	panel = NULL;
	if (sjme_error_is(error = state->api->panelNew(state,
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

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __screenId)
	(JNIEnv* env, jclass classy, jlong stateP, jlong screenP)
{
	sjme_scritchui state;
	sjme_scritchui_uiScreen screen;

	if (stateP == 0 || screenP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	screen = (sjme_scritchui_uiScreen)screenP;

	/* Return the screen Id. */
	return screen->id;
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __screenGetBounds)
	(JNIEnv* env, jclass classy, jlong stateP, jlong screenP, jlong forP,
		jintArray screenB, jintArray mmB)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiScreen screen;
	sjme_scritchui_uiComponent forComponent;
	sjme_jint lnS, lnMM;
	sjme_scritchui_rect rS, rMM;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	screen = (sjme_scritchui_uiScreen)screenP;
	forComponent = (sjme_scritchui_uiComponent)forP;
	if (state == 0 || screen == 0 || (screenB == NULL && mmB == NULL))
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Must fit all coordinates. */
	lnS = (screenB == NULL ? -1 : (*env)->GetArrayLength(env, screenB));
	lnMM = (mmB == NULL ? -1 : (*env)->GetArrayLength(env, mmB));
	if ((lnS >= 0 && lnS < 4) || (lnMM >= 0 && lnMM < 4))
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_INDEX_OUT_OF_BOUNDS);
		return;
	}

	/* Get the screen bounds. */
	memset(&rS, 0, sizeof(rS));
	memset(&rMM, 0, sizeof(rMM));
	if (sjme_error_is(error = state->api->screenGetBounds(state, screen,
		forComponent, &rS, &rMM)))
	{
		sjme_jni_throwMLECallError(env, sjme_error_default(error));
		return;
	}

	/* Output bounds. */
	if (lnS >= 4)
		sjme_jni_setIntArray(env, screenB, 0, 4,
			(jint)rS.s.x, (jint)rS.s.y,
			(jint)rS.d.width, (jint)rS.d.height);

	if (lnMM >= 4)
		sjme_jni_setIntArray(env, mmB, 0, 4,
			(jint)rMM.s.x, (jint)rMM.s.y,
			(jint)rMM.d.width, (jint)rMM.d.height);
}

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __screens)
	(JNIEnv* env, jclass classy, jlong stateP, jlongArray screenPs)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_jint numScreenPs, maxScreenPs, i;
	sjme_scritchui_uiScreen* screens;
	jlong tempJ;

	inState = (sjme_scritchui)stateP;
	if (inState == 0 || screenPs == NULL)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

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
	error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
	if (inState->api->screens == NULL ||
		sjme_error_is(error = inState->api->screens(inState,
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

JNIEXPORT jlong JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __scrollPanelNew)
	(JNIEnv* env, jclass classy, jlong stateP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiScrollPanel panel;

	/* Restore state. */
	state = (sjme_scritchui)stateP;
	if (stateP == 0)
	{
		error = SJME_ERROR_NULL_ARGUMENTS;
		goto fail_nullArgs;
	}

	/* Not implemented? */
	if (state->api->scrollPanelNew == NULL)
	{
		error = SJME_ERROR_NATIVE_WIDGET_FAILURE;
		goto fail_newPanel;
	}

	/* Create new panel. */
	panel = NULL;
	if (sjme_error_is(error = state->api->scrollPanelNew(state,
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

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__viewSetArea)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jint width, jint height)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;
	sjme_scritchui_dim dim;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (stateP == 0 || componentP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Not implemented? */
	if (state->api->viewSetArea == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward. */
	memset(&dim, 0, sizeof(dim));
	dim.width = width;
	dim.height = height;
	if (sjme_error_is(error = state->api->viewSetArea(state,
		component, &dim)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__viewSetSizeSuggestListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == 0 || component == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->viewSetSizeSuggestListener,
		mle_scritchUiListenerSizeSuggest);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__viewSetViewListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong componentP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)componentP;
	if (state == 0 || component == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, component, javaListener,
		state->api->viewSetViewListener,
		mle_scritchUiListenerView);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib, __weakDelete)
	(JNIEnv* env, jclass classy, jlong weakP)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;

	if (weakP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Call deletion handler. */
	weak = (sjme_alloc_weak)weakP;
	if (sjme_error_is(error = sjme_alloc_weakDelete(&weak)))
		sjme_jni_throwMLECallError(env, error);
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

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowIsVisible)(JNIEnv* env, jclass classy, jlong stateP,
	jlong windowP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;

	if (stateP == 0 || windowP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return JNI_FALSE;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	window = (sjme_scritchui_uiWindow)windowP;

	/* Is this known to be visible? */
	return window->component.state.isVisible ||
		window->component.state.isUserVisible;
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
	__windowSetCloseListener)(JNIEnv* env, jclass classy, jlong stateP,
	jlong windowP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiComponent component;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	component = (sjme_scritchui_uiComponent)windowP;
	if (state == NULL || component == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state, SJME_SUI_CAST_WINDOW(component),
		javaListener,
		state->api->windowSetCloseListener,
		mle_scritchUiListenerClose);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowSetMenuBar)(JNIEnv* env, jclass classy, jlong stateP,
	jlong windowP, jlong menuBarP)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;
	sjme_scritchui_uiMenuBar menuBar;

	if (stateP == 0 || windowP == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	/* Restore. */
	state = (sjme_scritchui)stateP;
	window = (sjme_scritchui_uiWindow)windowP;
	menuBar = (sjme_scritchui_uiMenuBar)menuBarP;

	/* Not implemented? */
	if (state->api->windowSetMenuBar == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = state->api->windowSetMenuBar(
		state, window, menuBar)))
		sjme_jni_throwMLECallError(env, error);
}

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(NativeScritchDylib,
	__windowSetMenuItemActivateListener)(JNIEnv* env, jclass classy,
	jlong stateP, jlong windowP, jobject javaListener)
{
	sjme_scritchui state;
	sjme_scritchui_uiWindow window;

	/* Restore. */
	state = (sjme_scritchui)stateP;
	window = (sjme_scritchui_uiWindow)windowP;
	if (state == NULL || window == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}

	mle_simpleListenerSet(env, state,
		window, javaListener,
		state->api->windowSetMenuItemActivateListener,
		mle_scritchUiListenerMenuItemActivate);
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
		sjme_jni_throwMLECallError(env, SJME_ERROR_NATIVE_WIDGET_FAILURE);
		return;
	}

	/* Forward call. */
	if (sjme_error_is(error = state->api->windowSetVisible(
		state, window, visible)))
		sjme_jni_throwMLECallError(env, error);
}

static const JNINativeMethod mleNativeScritchDylibMethods[] =
{
	FORWARD_list(NativeScritchDylib, __builtinFonts),
	FORWARD_list(NativeScritchDylib, __choiceGetSelectedIndex),
	FORWARD_list(NativeScritchDylib, __choiceInsert),
	FORWARD_list(NativeScritchDylib, __choiceLength),
	FORWARD_list(NativeScritchDylib, __choiceRemove),
	FORWARD_list(NativeScritchDylib, __choiceRemoveAll),
	FORWARD_list(NativeScritchDylib, __choiceSetEnabled),
	FORWARD_list(NativeScritchDylib, __choiceSetImage),
	FORWARD_list(NativeScritchDylib, __choiceSetSelected),
	FORWARD_list(NativeScritchDylib, __choiceSetString),
	FORWARD_list(NativeScritchDylib, __componentGetParent),
	FORWARD_list(NativeScritchDylib, __componentHeight),
	FORWARD_list(NativeScritchDylib, __componentRepaint),
	FORWARD_list(NativeScritchDylib, __componentRevalidate),
	FORWARD_list(NativeScritchDylib, __componentSetActivateListener),
	FORWARD_list(NativeScritchDylib, __componentSetInputListener),
	FORWARD_list(NativeScritchDylib, __componentSetPaintListener),
	FORWARD_list(NativeScritchDylib, __componentSetVisibleListener),
	FORWARD_list(NativeScritchDylib, __componentWidth),
	FORWARD_list(NativeScritchDylib, __containerAdd),
	FORWARD_list(NativeScritchDylib, __containerGetFrame),
	FORWARD_list(NativeScritchDylib, __containerRemoveAll),
	FORWARD_list(NativeScritchDylib, __containerSetBounds),
	FORWARD_list(NativeScritchDylib, __hardwareGraphics),
	FORWARD_list(NativeScritchDylib, __labelSetString),
	FORWARD_list(NativeScritchDylib, __lafElementColor),
	FORWARD_list(NativeScritchDylib, __lafPlatformFlags),
	FORWARD_list(NativeScritchDylib, __linkInit),
	FORWARD_list(NativeScritchDylib, __listNew),
	FORWARD_list(NativeScritchDylib, __loopExecute),
	FORWARD_list(NativeScritchDylib, __loopExecuteLater),
	FORWARD_list(NativeScritchDylib, __loopExecuteWait),
	FORWARD_list(NativeScritchDylib, __loopIsInThread),
	FORWARD_list(NativeScritchDylib, __loopIterate),
	FORWARD_list(NativeScritchDylib, __menuBarNew),
	FORWARD_list(NativeScritchDylib, __menuInsert),
	FORWARD_list(NativeScritchDylib, __menuItemNew),
	FORWARD_list(NativeScritchDylib, __menuNew),
	FORWARD_list(NativeScritchDylib, __menuRemoveAll),
	FORWARD_list(NativeScritchDylib, __objectDelete),
	FORWARD_list(NativeScritchDylib, __panelEnableFocus),
	FORWARD_list(NativeScritchDylib, __panelNew),
	FORWARD_list(NativeScritchDylib, __screenGetBounds),
	FORWARD_list(NativeScritchDylib, __screenId),
	FORWARD_list(NativeScritchDylib, __screens),
	FORWARD_list(NativeScritchDylib, __scrollPanelNew),
	FORWARD_list(NativeScritchDylib, __viewSetArea),
	FORWARD_list(NativeScritchDylib, __viewSetSizeSuggestListener),
	FORWARD_list(NativeScritchDylib, __viewSetViewListener),
	FORWARD_list(NativeScritchDylib, __weakDelete),
	FORWARD_list(NativeScritchDylib, __windowContentMinimumSize),
	FORWARD_list(NativeScritchDylib, __windowIsVisible),
	FORWARD_list(NativeScritchDylib, __windowManagerType),
	FORWARD_list(NativeScritchDylib, __windowNew),
	FORWARD_list(NativeScritchDylib, __windowSetCloseListener),
	FORWARD_list(NativeScritchDylib, __windowSetMenuBar),
	FORWARD_list(NativeScritchDylib, __windowSetMenuItemActivateListener),
	FORWARD_list(NativeScritchDylib, __windowSetVisible),
};

FORWARD_init(mleNativeScritchDylibInit, mleNativeScritchDylibMethods)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(__fontByFace),
	MLE_LIST_ITEM(__fontDerive),
MLE_LIST_END()
