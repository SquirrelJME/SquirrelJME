/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/core/coreGeneric.h"

/** Window manager information. */
static const sjme_scritchui_wmInfo sjme_scritchUI_coreWmInfo =
{
	sjme_sm(.defaultTitle, "SquirrelJME"),
	sjme_sm(.xwsClass, "squirreljme"),
};

/** Core dispatch functions for serial calls. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_serialFunctions =
{
	/* As normal. */
	sjme_sm(.choiceGetSelectedIndex, 
		sjme_scritchui_coreSerial_choiceGetSelectedIndex),
	sjme_sm(.choiceItemGet, sjme_scritchui_coreSerial_choiceItemGet),
	sjme_sm(.choiceItemInsert, sjme_scritchui_coreSerial_choiceItemInsert),
	sjme_sm(.choiceItemRemove, sjme_scritchui_coreSerial_choiceItemRemove),
	sjme_sm(.choiceItemRemoveAll, 
		sjme_scritchui_coreSerial_choiceItemRemoveAll),
	sjme_sm(.choiceItemSetEnabled, 
		sjme_scritchui_coreSerial_choiceItemSetEnabled),
	sjme_sm(.choiceItemSetImage, sjme_scritchui_coreSerial_choiceItemSetImage),
	sjme_sm(.choiceItemSetSelected, 
		sjme_scritchui_coreSerial_choiceItemSetSelected),
	sjme_sm(.choiceItemSetString, 
		sjme_scritchui_coreSerial_choiceItemSetString),
	sjme_sm(.choiceLength, sjme_scritchui_coreSerial_choiceLength),
	sjme_sm(.componentFocusGrab, sjme_scritchui_coreSerial_componentFocusGrab),
	sjme_sm(.componentFocusHas, sjme_scritchui_coreSerial_componentFocusHas),
	sjme_sm(.componentGetParent, sjme_scritchui_coreSerial_componentGetParent),
	sjme_sm(.componentPosition, sjme_scritchui_coreSerial_componentPosition),
	sjme_sm(.componentRepaint, 
		sjme_scritchui_coreSerial_componentRepaint),
	sjme_sm(.componentRevalidate, 
		sjme_scritchui_coreSerial_componentRevalidate),
	sjme_sm(.componentSetActivateListener,
		sjme_scritchui_coreSerial_componentSetActivateListener),
	sjme_sm(.componentSetInputListener,
		sjme_scritchui_coreSerial_componentSetInputListener),
	sjme_sm(.componentSetPaintListener,
		sjme_scritchui_coreSerial_componentSetPaintListener),
	sjme_sm(.componentSetSizeListener,
		sjme_scritchui_coreSerial_componentSetSizeListener),
	sjme_sm(.componentSetValueUpdateListener, 
		sjme_scritchui_coreSerial_componentSetValueUpdateListener),
	sjme_sm(.componentSetVisibleListener,
		sjme_scritchui_coreSerial_componentSetVisibleListener),
	sjme_sm(.componentSize, sjme_scritchui_coreSerial_componentSize),
	sjme_sm(.containerAdd, sjme_scritchui_coreSerial_containerAdd),
	sjme_sm(.containerGetFrame,
		sjme_scritchui_coreSerial_containerGetFrame),
	sjme_sm(.containerRemove, sjme_scritchui_coreSerial_containerRemove),
	sjme_sm(.containerRemoveAll, sjme_scritchui_coreSerial_containerRemoveAll),
	sjme_sm(.containerSetBounds, sjme_scritchui_coreSerial_containerSetBounds),
	sjme_sm(.fontBuiltin, sjme_scritchui_coreSerial_fontBuiltin),
	sjme_sm(.fontByFace, sjme_scritchui_coreSerial_fontByFace),
	sjme_sm(.fontCount, sjme_scritchui_coreSerial_fontCount),
	sjme_sm(.fontDerive, sjme_scritchui_coreSerial_fontDerive),
	sjme_sm(.fontList, sjme_scritchui_coreSerial_fontList),
	sjme_sm(.hardwareGraphics, sjme_scritchui_coreSerial_hardwareGraphics),
	sjme_sm(.labelSetString, sjme_scritchui_coreSerial_labelSetString),
	sjme_sm(.lafDpiProject, sjme_scritchui_coreSerial_lafDpiProject),
	sjme_sm(.lafElementColor, sjme_scritchui_coreSerial_lafElementColor),
	sjme_sm(.lafMetric, sjme_scritchui_coreSerial_lafMetric),
	sjme_sm(.listNew, sjme_scritchui_coreSerial_listNew),

	/* Loops are unchanged. */
	sjme_sm(.loopExecute, sjme_scritchui_core_loopExecute),
	sjme_sm(.loopExecuteLater, sjme_scritchui_core_loopExecuteLater),
	sjme_sm(.loopExecuteWait, sjme_scritchui_core_loopExecuteWait),
	sjme_sm(.loopIsInThread, sjme_scritchui_core_loopIsInThread),
	sjme_sm(.loopIterate, sjme_scritchui_core_loopIterate),

	/* As normal. */
	sjme_sm(.menuBarNew, sjme_scritchui_coreSerial_menuBarNew),
	sjme_sm(.menuInsert, sjme_scritchui_coreSerial_menuInsert),
	sjme_sm(.menuItemNew, sjme_scritchui_coreSerial_menuItemNew),
	sjme_sm(.menuNew, sjme_scritchui_coreSerial_menuNew),
	sjme_sm(.menuRemove, sjme_scritchui_coreSerial_menuRemove),
	sjme_sm(.menuRemoveAll, sjme_scritchui_coreSerial_menuRemoveAll),
	sjme_sm(.objectDelete, sjme_scritchui_coreSerial_objectDelete),
	sjme_sm(.panelEnableFocus, sjme_scritchui_coreSerial_panelEnableFocus),
	sjme_sm(.panelNew, sjme_scritchui_coreSerial_panelNew),
	sjme_sm(.pseudoGraphics, sjme_scritchui_coreSerial_pseudoGraphics),
	sjme_sm(.screenGetBounds, sjme_scritchui_coreSerial_screenGetBounds),
	sjme_sm(.screenSetListener, sjme_scritchui_coreSerial_screenSetListener),
	sjme_sm(.screens, sjme_scritchui_coreSerial_screens),
	sjme_sm(.scrollPanelNew, sjme_scritchui_coreSerial_scrollPanelNew),
	sjme_sm(.viewGetView, sjme_scritchui_coreSerial_viewGetView),
	sjme_sm(.viewSetArea, sjme_scritchui_coreSerial_viewSetArea),
	sjme_sm(.viewSetView, sjme_scritchui_coreSerial_viewSetView),
	sjme_sm(.viewSetSizeSuggestListener,
		sjme_scritchui_coreSerial_viewSetSizeSuggestListener),
	sjme_sm(.viewSetViewListener, 
		sjme_scritchui_coreSerial_viewSetViewListener),
	sjme_sm(.windowContentMinimumSize,
		sjme_scritchui_coreSerial_windowContentMinimumSize),
	sjme_sm(.windowNew, sjme_scritchui_coreSerial_windowNew),
	sjme_sm(.windowSetCloseListener, 
		sjme_scritchui_coreSerial_windowSetCloseListener),
	sjme_sm(.windowSetMenuBar, sjme_scritchui_coreSerial_windowSetMenuBar),
	sjme_sm(.windowSetMenuItemActivateListener,
		sjme_scritchui_coreSerial_windowSetMenuItemActivateListener),
	sjme_sm(.windowSetVisible, sjme_scritchui_coreSerial_windowSetVisible),
};

/** Core Function set for ScritchUI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_coreFunctions = 
{
	sjme_sm(.choiceGetSelectedIndex,
		sjme_scritchui_core_choiceGetSelectedIndex),
	sjme_sm(.choiceItemGet, sjme_scritchui_core_choiceItemGet),
	sjme_sm(.choiceItemInsert, sjme_scritchui_core_choiceItemInsert),
	sjme_sm(.choiceItemRemove, sjme_scritchui_core_choiceItemRemove),
	sjme_sm(.choiceItemRemoveAll, sjme_scritchui_core_choiceItemRemoveAll),
	sjme_sm(.choiceItemSetEnabled, sjme_scritchui_core_choiceItemSetEnabled),
	sjme_sm(.choiceItemSetImage, sjme_scritchui_core_choiceItemSetImage),
	sjme_sm(.choiceItemSetSelected, sjme_scritchui_core_choiceItemSetSelected),
	sjme_sm(.choiceItemSetString, sjme_scritchui_core_choiceItemSetString),
	sjme_sm(.choiceLength, sjme_scritchui_core_choiceLength),
	sjme_sm(.componentFocusGrab, sjme_scritchui_core_componentFocusGrab),
	sjme_sm(.componentFocusHas, sjme_scritchui_core_componentFocusHas),
	sjme_sm(.componentGetParent, sjme_scritchui_core_componentGetParent),
	sjme_sm(.componentPosition, sjme_scritchui_core_componentPosition),
	sjme_sm(.componentRepaint, sjme_scritchui_core_componentRepaint),
	sjme_sm(.componentRevalidate, sjme_scritchui_core_componentRevalidate),
	sjme_sm(.componentSetActivateListener,
		sjme_scritchui_core_componentSetActivateListener),
	sjme_sm(.componentSetInputListener,
		sjme_scritchui_core_componentSetInputListener),
	sjme_sm(.componentSetPaintListener,
		sjme_scritchui_core_componentSetPaintListener),
	sjme_sm(.componentSetSizeListener,
		sjme_scritchui_core_componentSetSizeListener),
	sjme_sm(.componentSetValueUpdateListener,
		sjme_scritchui_core_componentSetValueUpdateListener),
	sjme_sm(.componentSetVisibleListener,
		sjme_scritchui_core_componentSetVisibleListener),
	sjme_sm(.componentSize, sjme_scritchui_core_componentSize),
	sjme_sm(.containerAdd, sjme_scritchui_core_containerAdd),
	sjme_sm(.containerGetFrame, sjme_scritchui_core_containerGetFrame),
	sjme_sm(.containerRemove, sjme_scritchui_core_containerRemove),
	sjme_sm(.containerRemoveAll, sjme_scritchui_core_containerRemoveAll),
	sjme_sm(.containerSetBounds, sjme_scritchui_core_containerSetBounds),
	sjme_sm(.fontBuiltin, sjme_scritchui_core_fontBuiltin),
	sjme_sm(.fontByFace, sjme_scritchui_core_fontByFace),
	sjme_sm(.fontCount, sjme_scritchui_core_fontCount),
	sjme_sm(.fontDerive, sjme_scritchui_core_fontDerive),
	sjme_sm(.fontList, sjme_scritchui_core_fontList),
	sjme_sm(.hardwareGraphics, sjme_scritchpen_core_hardwareGraphics),
	sjme_sm(.labelSetString, sjme_scritchui_core_labelSetString),
	sjme_sm(.lafDpiProject, sjme_scritchui_core_lafDpiProject),
	sjme_sm(.lafElementColor, sjme_scritchui_core_lafElementColor),
	sjme_sm(.lafMetric, sjme_scritchui_core_lafMetric),
	sjme_sm(.listNew, sjme_scritchui_core_listNew),
	sjme_sm(.loopExecute, sjme_scritchui_core_loopExecute),
	sjme_sm(.loopExecuteLater, sjme_scritchui_core_loopExecuteLater),
	sjme_sm(.loopExecuteWait, sjme_scritchui_core_loopExecuteWait),
	sjme_sm(.loopIsInThread, sjme_scritchui_core_loopIsInThread),
	sjme_sm(.loopIterate, sjme_scritchui_core_loopIterate),
	sjme_sm(.menuBarNew, sjme_scritchui_core_menuBarNew),
	sjme_sm(.menuInsert, sjme_scritchui_core_menuInsert),
	sjme_sm(.menuItemNew, sjme_scritchui_core_menuItemNew),
	sjme_sm(.menuNew, sjme_scritchui_core_menuNew),
	sjme_sm(.menuRemove, sjme_scritchui_core_menuRemove),
	sjme_sm(.menuRemoveAll, sjme_scritchui_core_menuRemoveAll),
	sjme_sm(.objectDelete, sjme_scritchui_core_objectDelete),
	sjme_sm(.panelEnableFocus, sjme_scritchui_core_panelEnableFocus),
	sjme_sm(.panelNew, sjme_scritchui_core_panelNew),
	sjme_sm(.pseudoGraphics, sjme_scritchui_core_pseudoGraphics),
	sjme_sm(.screenGetBounds, sjme_scritchui_core_screenGetBounds),
	sjme_sm(.screenSetListener, sjme_scritchui_core_screenSetListener),
	sjme_sm(.screens, sjme_scritchui_core_screens),
	sjme_sm(.scrollPanelNew, sjme_scritchui_core_scrollPanelNew),
	sjme_sm(.viewGetView, sjme_scritchui_core_viewGetView),
	sjme_sm(.viewSetArea, sjme_scritchui_core_viewSetArea),
	sjme_sm(.viewSetView, sjme_scritchui_core_viewSetView),
	sjme_sm(.viewSetSizeSuggestListener,
		sjme_scritchui_core_viewSetSizeSuggestListener),
	sjme_sm(.viewSetViewListener, sjme_scritchui_core_viewSetViewListener),
	sjme_sm(.windowContentMinimumSize,
		sjme_scritchui_core_windowContentMinimumSize),
	sjme_sm(.windowNew, sjme_scritchui_core_windowNew),
	sjme_sm(.windowSetCloseListener,
		sjme_scritchui_core_windowSetCloseListener),
	sjme_sm(.windowSetMenuBar, sjme_scritchui_core_windowSetMenuBar),
	sjme_sm(.windowSetMenuItemActivateListener,
		sjme_scritchui_core_windowSetMenuItemActivateListener),
	sjme_sm(.windowSetVisible, sjme_scritchui_core_windowSetVisible),
};

/** Internal functions for ScritchUI implementations. */
static const sjme_scritchui_internFunctions sjme_scritchUI_coreIntern =
{
	sjme_sm(.bindFocus, sjme_scritchui_core_intern_bindFocus),
	sjme_sm(.containerMaxSize, sjme_scritchui_core_intern_containerMaxSize),
	sjme_sm(.fontBuiltin, sjme_scritchui_core_intern_fontBuiltin),
	sjme_sm(.fontIterate, sjme_scritchui_core_intern_fontIterate),
	sjme_sm(.fontParamFromFlat, sjme_scritchui_core_intern_fontParamFromFlat),
	sjme_sm(.fontParamToFlat, sjme_scritchui_core_intern_fontParamToFlat),
	sjme_sm(.fontRegister, sjme_scritchui_core_intern_fontRegister),
	sjme_sm(.fontScanAll, sjme_scritchui_core_intern_fontScanAll),
	sjme_sm(.fontScanResource, sjme_scritchui_core_intern_fontScanResource),
	sjme_sm(.getChoice, sjme_scritchui_core_intern_getChoice),
	sjme_sm(.getContainer, sjme_scritchui_core_intern_getContainer),
	sjme_sm(.getLabeled, sjme_scritchui_core_intern_getLabeled),
	sjme_sm(.getMenuHasChildren, 
		sjme_scritchui_core_intern_getMenuHasChildren),
	sjme_sm(.getMenuHasParent, sjme_scritchui_core_intern_getMenuHasParent),
	sjme_sm(.getPaintable, sjme_scritchui_core_intern_getPaintable),
	sjme_sm(.getView, sjme_scritchui_core_intern_getView),
	sjme_sm(.initCommon, sjme_scritchui_core_intern_initCommon),
	sjme_sm(.initComponent, sjme_scritchui_core_intern_initComponent),
	sjme_sm(.mapScreen, sjme_scritchui_core_intern_mapScreen),
	sjme_sm(.menuItemActivate, sjme_scritchui_intern_menuItemActivate),
	sjme_sm(.menuItemActivateById, sjme_scritchui_intern_menuItemActivateById),
	sjme_sm(.objectNew, sjme_scritchui_core_intern_objectNew),
	sjme_sm(.setSimpleListener, sjme_scritchui_core_intern_setSimpleListener),
	sjme_sm(.updateVisibleContainer,
		sjme_scritchui_core_intern_updateVisibleContainer),
	sjme_sm(.updateVisibleComponent,
		sjme_scritchui_core_intern_updateVisibleComponent),
	sjme_sm(.updateVisibleWindow,
		sjme_scritchui_core_intern_updateVisibleWindow),
	sjme_sm(.viewSuggest, sjme_scritchui_core_intern_viewSuggest),
};

static sjme_thread_result sjme_attrThreadCall sjme_scritchui_core_fbBelay(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui topState;
	sjme_scritchui wrappedState;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Recover wrapped state, which is calling this one. */
	wrappedState = (sjme_scritchui)anything;
	
	/* Debug. */
	sjme_message("Waiting for top state to become mapped...");

	/* Recover wrapped state. */
	topState = NULL;
	while (topState == NULL)
	{
		/* Barrier for other thread to run. */
		sjme_atomic_barrier();
		sjme_thread_yield();
		sjme_atomic_barrier();
		
		/* Read it in. */
		topState = sjme_atomic_g(sjme_pointer, 
			&wrappedState->topState);
	}
	
	/* Debug. */
	sjme_message("Calling top initializer, if applicable...");
	
	/* Call the intended wrapper in this event thread? */
	if (topState->loopThreadInit != NULL)
		topState->loopThreadInit(topState);
	
	/* Debug. */
	sjme_message("Marking framebuffer as ready!");
	
	/* Mark as ready so initialization continues and gets finished. */
	sjme_atomic_s(sjme_jint, &topState->loopThreadReady, 1);
	
	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_thread_result sjme_attrThreadCall
	sjme_scritchui_core_grabExternalThreadId(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui state;

	state = anything;
	if (state == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Fill in thread as it is missing. */
	return SJME_THREAD_RESULT(sjme_thread_current(
		&state->loopThread));
}

static sjme_errorCode sjme_scritchui_core_apiInitActual(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd,
	sjme_attrInNullable sjme_scritchui wrappedState,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals)
{
	sjme_errorCode error;
	sjme_scritchui state;
	sjme_thread currentThread;
	
	if (allocPool == NULL || inImplFunc == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return sjme_error_notImplemented(0);
	
	/* Allocate state. */
	state = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(allocPool, sizeof(*state),
		NULL, (void**)&state, NULL)) || state == NULL)
		goto fail_alloc;
	
	/* Seed state. */
	state->pool = allocPool;
	state->api = &sjme_scritchUI_serialFunctions;
	state->apiInThread = &sjme_scritchUI_coreFunctions;
	state->intern = &sjme_scritchUI_coreIntern;
	state->impl = inImplFunc;
	state->wmInfo = &sjme_scritchUI_coreWmInfo;
	state->nanoTime = sjme_nal_default.nanoTime;
	state->externals = externals;

	/* Use provided front end if a core interface. */
	if (initFrontEnd != NULL)
		sjme_frontEnd_copy(&state->common.frontEnd, initFrontEnd);
	
	/* Common initialize. */
	if (sjme_error_is(error = state->intern->initCommon(state,
		SJME_SUI_CAST_COMMON(state), SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_ROOT_STATE)))
		goto fail_commonInit;
	
	/* By default, everything is panel only. */
	state->platformFlags |= SJME_SCRITCHUI_LAF_PLATFORM_PANEL_ONLY;
	
	/* Loop initialization. */
	if (wrappedState != NULL)
		state->loopThread = wrappedState->loopThread;
	else
		state->loopThread = SJME_THREAD_NULL;
	state->loopThreadInit = loopExecute;

	/* If no loop thread was defined, and we have an overridden loop execute */
	/* then we need to grab the actual thread ID. */
	while (state->loopThread == SJME_THREAD_NULL && externals != NULL &&
		externals->externalLoopExecuteLater != NULL)
		if (sjme_error_is(error = externals->externalLoopExecuteLater(
			state, sjme_scritchui_core_grabExternalThreadId,
			state)))
			goto fail_grabExternalThreadId;

	/* Set wrapped state. */
	if (wrappedState != NULL)
		state->wrappedState = wrappedState;
	
	/* Perform API specific initialization. */
	if (sjme_error_is(error = state->impl->apiInit(state)))
		goto fail_apiInit;
		
	/* Link back states. */
	if (wrappedState != NULL)
	{
		/* Debug. */
		sjme_message("Linking in wrapped state (%p -> %p)...",
			state, wrappedState);
		
		/* Link together. */
		sjme_atomic_s(sjme_pointer, &wrappedState->topState,
			state);
		
		/* Barrier here for wrapped init. */
		sjme_atomic_barrier();
		sjme_thread_yield();
		sjme_atomic_barrier();
	}

	/* Debug. */
	sjme_message("Waiting for thread ready (s=%p t=%p)...",
		state, state->loopThread);

	/* Get current thread to potentially detect a case where the main thread */
	/* is the event thread, in which case if we loop we will deadlock */
	/* ourselves. */
	currentThread = SJME_THREAD_NULL;
	sjme_thread_current(&currentThread);

	/* Wait for the ready signal, but only if required. */
	if (state->loopThread == SJME_THREAD_NULL ||
		(currentThread != SJME_THREAD_NULL &&
			state->loopThread == currentThread))
		sjme_atomic_s(sjme_jint, &state->loopThreadReady, 1);
	else
	{
		while (0 == sjme_atomic_g(sjme_jint, &state->loopThreadReady))
		{
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
		}
	}

	/* Debug. */
	sjme_message("UI thread marked ready (s=%p t=%p)!",
		state, state->loopThread);
	
	/* Return resultant state. */
	*outState = state;
	return SJME_ERROR_NONE;

fail_grabExternalThreadId:
fail_apiInit:
fail_commonInit:
fail_alloc:
	if (state != NULL)
	{
		sjme_alloc_free(state);
		state = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_apiInit(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
	sjme_errorCode error;
	sjme_jboolean isFbWrapper; 
	sjme_jboolean needFbWrapper;
	sjme_scritchui state;
	sjme_scritchui wrappedState;
	
	if (allocPool == NULL || inImplFunc == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return sjme_error_notImplemented(0);
	
	/* Do we need the framebuffer wrapper? */
	isFbWrapper = (inImplFunc == &sjme_scritchui_fbFunctions);
	needFbWrapper = !isFbWrapper;
	
	/* Normal top-level initialization. */
	if (!needFbWrapper)
	{
		return sjme_scritchui_core_apiInitActual(allocPool, outState,
			inImplFunc, loopExecute, initFrontEnd, NULL, externals);
	}
	
	/* Initialize API we are going to wrap. */
	wrappedState = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInitActual(allocPool,
		&wrappedState,
		inImplFunc, NULL,
		NULL, NULL, externals)) ||
		wrappedState == NULL)
		return sjme_error_default(error);
	
	/* Debug. */
	sjme_message("Pre-call init loop belay...");
	
	/* We need to initialize our wrapper in the event thread. */
	if (sjme_error_is(error = wrappedState->api->loopExecuteLater(
		wrappedState, sjme_scritchui_core_fbBelay,
		wrappedState)))
		return sjme_error_default(error);
	
	/* Debug. */
	sjme_message("Now performing framebuffer init...");
	
	/* Wrap this with the framebuffer call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInitActual(allocPool,
		&state,
		&sjme_scritchui_fbFunctions,
		loopExecute, initFrontEnd, wrappedState, externals)) ||
		state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_objectDelete(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject)
{
	if (inState == NULL || inOutObject == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Already free? */
	if (*inOutObject == NULL)
		return SJME_ERROR_NONE;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_pointer sjme_scritchui_checkCast(sjme_scritchui_uiType inType,
	sjme_pointer inPtr)
{
	sjme_scritchui_uiCommon common;
	
	if (inPtr == NULL)
		return NULL;
	
	/* Check type. */
	common = inPtr;
	if (common->type != inType)
	{
		sjme_debug_abort(SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
		return NULL;
	}
	
	/* Return passed value. */
	return inPtr;
}

sjme_pointer sjme_scritchui_checkCast_component(sjme_pointer inPtr)
{
	sjme_scritchui_uiCommon common;
	
	if (inPtr == NULL)
		return NULL;
	
	/* Check type. */
	common = inPtr;
	if (common->type < SJME_SCRITCHUI_TYPE_FONT ||
		common->type >= SJME_SCRITCHUI_NUM_UI_TYPES)
	{
		sjme_debug_abort(SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
		return NULL;
	}
	
	/* Return passed value. */
	return inPtr;
}

sjme_pointer sjme_scritchui_checkCast_container(sjme_pointer inPtr)
{
	sjme_scritchui_uiCommon common;

	if (inPtr == NULL)
		return NULL;

	/* Check type. */
	common = inPtr;
	if (common->type != SJME_SCRITCHUI_TYPE_PANEL &&
		common->type != SJME_SCRITCHUI_TYPE_SCROLL_PANEL &&
		common->type != SJME_SCRITCHUI_TYPE_WINDOW)
	{
		sjme_debug_abort(SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
		return NULL;
	}

	/* Return passed value. */
	return inPtr;
}

sjme_pointer sjme_scritchui_checkCast_menuKind(sjme_pointer inPtr)
{
	sjme_scritchui_uiCommon common;
	
	if (inPtr == NULL)
		return NULL;
	
	/* Check type. */
	common = inPtr;
	if (common->type != SJME_SCRITCHUI_TYPE_MENU &&
		common->type != SJME_SCRITCHUI_TYPE_MENU_BAR &&
		common->type != SJME_SCRITCHUI_TYPE_MENU_ITEM)
	{
		sjme_debug_abort(SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
		return NULL;
	}
	
	/* Return passed value. */
	return inPtr;
}

sjme_errorCode sjme_scritchui_isA(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES) sjme_scritchui_uiType inType,
	sjme_attrOutNotNull sjme_jboolean* outResult)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_scritchui_uiCommon common;
	
	if (outResult == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inType <= SJME_SCRITCHUI_TYPE_RESERVED ||
		inType >= SJME_SCRITCHUI_NUM_UI_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Null input is always nothing. */
	if (inWhat == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* All ScritchUI objects are weakly referenced. */
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakRefGet(inWhat, &weak)) || weak == NULL)
	{
		*outResult = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}

	/* Must be the type and the magic must be valid! */
	/* Aliases of object types match objects as well. */
	common = inWhat;
	if (common->magic != SJME_SCRITCHUI_OBJECT_MAGIC)
		*outResult = SJME_JNI_FALSE;
	else if (common->type == inType)
		*outResult = SJME_JNI_TRUE;
	else
		*outResult = SJME_JNI_FALSE;
		
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_scritchui_isAR(
	sjme_attrInNullable sjme_pointer inWhat,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES) 
		sjme_scritchui_uiType inType)
{
	sjme_jboolean result;
	
	/* Forward call. */
	result = SJME_JNI_FALSE;
	if (sjme_error_is(sjme_scritchui_isA(inWhat, inType, &result)))
		return SJME_JNI_FALSE;

	/* Was this the type? */
	return result;
}
