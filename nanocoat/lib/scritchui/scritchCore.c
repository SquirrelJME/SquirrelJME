/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "lib/scritchui/framebuffer/fb.h"

/** Window manager information. */
static const sjme_scritchui_wmInfo sjme_scritchUI_coreWmInfo =
{
	.defaultTitle = "SquirrelJME",
	.xwsClass = "squirreljme",
};

/** Core dispatch functions for serial calls. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_serialFunctions =
{
	/* As normal. */
	.apiFlags = NULL,
	.componentRepaint = sjme_scritchui_coreSerial_componentRepaint,
	.componentRevalidate = sjme_scritchui_coreSerial_componentRevalidate,
	.componentSetInputListener =
		sjme_scritchui_coreSerial_componentSetInputListener,
	.componentSetPaintListener =
		sjme_scritchui_coreSerial_componentSetPaintListener,
	.componentSetSizeListener =
		sjme_scritchui_coreSerial_componentSetSizeListener,
	.componentSetVisibleListener =
		sjme_scritchui_coreSerial_componentSetVisibleListener,
	.componentSize = sjme_scritchui_coreSerial_componentSize,
	.containerAdd = sjme_scritchui_coreSerial_containerAdd,
	.containerRemove = sjme_scritchui_coreSerial_containerRemove,
	.containerRemoveAll = sjme_scritchui_coreSerial_containerRemoveAll,
	.containerSetBounds = sjme_scritchui_coreSerial_containerSetBounds,
	.fontBuiltin = sjme_scritchui_coreSerial_fontBuiltin,
	.fontDerive = sjme_scritchui_coreSerial_fontDerive,
	.hardwareGraphics = sjme_scritchui_coreSerial_hardwareGraphics,
	
	/* Loops are unchanged. */
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteLater = sjme_scritchui_core_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	
	/* As normal. */
	.panelEnableFocus = sjme_scritchui_coreSerial_panelEnableFocus,
	.panelNew = sjme_scritchui_coreSerial_panelNew,
	.screenSetListener = sjme_scritchui_coreSerial_screenSetListener,
	.screens = sjme_scritchui_coreSerial_screens,
	.windowContentMinimumSize =
		sjme_scritchui_coreSerial_windowContentMinimumSize,
	.windowNew = sjme_scritchui_coreSerial_windowNew,
	.windowSetCloseListener = sjme_scritchui_coreSerial_windowSetCloseListener,
	.windowSetVisible = sjme_scritchui_coreSerial_windowSetVisible,
};

/** Core Function set for ScritchUI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_coreFunctions =
{
	.apiFlags = NULL,
	.componentRepaint = sjme_scritchui_core_componentRepaint,
	.componentRevalidate = sjme_scritchui_core_componentRevalidate,
	.componentSetInputListener = sjme_scritchui_core_componentSetInputListener,
	.componentSetPaintListener = sjme_scritchui_core_componentSetPaintListener,
	.componentSetSizeListener = sjme_scritchui_core_componentSetSizeListener,
	.componentSetVisibleListener =
		sjme_scritchui_core_componentSetVisibleListener,
	.componentSize = sjme_scritchui_core_componentSize,
	.containerAdd = sjme_scritchui_core_containerAdd,
	.containerRemove = sjme_scritchui_core_containerRemove,
	.containerRemoveAll = sjme_scritchui_core_containerRemoveAll,
	.containerSetBounds = sjme_scritchui_core_containerSetBounds,
	.fontBuiltin = sjme_scritchui_core_fontBuiltin,
	.fontDerive = sjme_scritchui_core_fontDerive,
	.hardwareGraphics = sjme_scritchpen_core_hardwareGraphics,
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteLater = sjme_scritchui_core_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	.panelEnableFocus = sjme_scritchui_core_panelEnableFocus,
	.panelNew = sjme_scritchui_core_panelNew,
	.screenSetListener = sjme_scritchui_core_screenSetListener,
	.screens = sjme_scritchui_core_screens,
	.windowContentMinimumSize = sjme_scritchui_core_windowContentMinimumSize,
	.windowNew = sjme_scritchui_core_windowNew,
	.windowSetCloseListener = sjme_scritchui_core_windowSetCloseListener,
	.windowSetVisible = sjme_scritchui_core_windowSetVisible,
};

/** Internal functions for ScritchUI implementations. */
static const sjme_scritchui_internFunctions sjme_scritchUI_coreIntern =
{
	.fontBuiltin = sjme_scritchui_core_intern_fontBuiltin,
	.getContainer = sjme_scritchui_core_intern_getContainer,
	.getPaintable = sjme_scritchui_core_intern_getPaintable,
	.initComponent = sjme_scritchui_core_intern_initComponent,
	.mapScreen = sjme_scritchui_core_intern_mapScreen,
	.updateVisibleContainer =
		sjme_scritchui_core_intern_updateVisibleContainer,
	.updateVisibleComponent =
		sjme_scritchui_core_intern_updateVisibleComponent,
	.updateVisibleWindow = sjme_scritchui_core_intern_updateVisibleWindow,
};

static sjme_thread_result SJME_THREAD_CONVENTION sjme_scritchui_core_fbBelay(
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
		sjme_thread_barrier();
		sjme_thread_yield();
		sjme_thread_barrier();
		
		/* Read it in. */
		topState = sjme_atomic_sjme_pointer_get(
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
	sjme_atomic_sjme_jint_set(&topState->loopThreadReady, 1);
	
	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_core_apiInitActual(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInNullable sjme_scritchui wrappedState)
{
	sjme_errorCode error;
	sjme_scritchui state;
	
	if (inPool == NULL || inImplFunc == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate state. */
	state = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*state),
		&state)) || state == NULL)
		goto fail_alloc;
	
	/* Seed state. */
	state->pool = inPool;
	state->api = &sjme_scritchUI_serialFunctions;
	state->apiInThread = &sjme_scritchUI_coreFunctions;
	state->intern = &sjme_scritchUI_coreIntern;
	state->impl = inImplFunc;
	state->wmInfo = &sjme_scritchUI_coreWmInfo;
	state->nanoTime = sjme_nal_default_nanoTime;
	
	/* By default, everything is panel only. */
	state->isPanelOnly = SJME_JNI_TRUE;
	
	/* Loop initialization. */
	if (wrappedState != NULL)
		state->loopThread = wrappedState->loopThread;
	else
		state->loopThread = SJME_THREAD_NULL;
	state->loopThreadInit = loopExecute;
	
	/* Use provided front end if a core interface. */
	if (initFrontEnd != NULL)
		memmove(&state->common.frontEnd, initFrontEnd,
			sizeof(*initFrontEnd));
	
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
		state->wrappedState = wrappedState;
		sjme_atomic_sjme_pointer_set(&wrappedState->topState,
			state);
		
		/* Barrier here for wrapped init. */
		sjme_thread_barrier();
		sjme_thread_yield();
		sjme_thread_barrier();
	}
	
	/* Debug. */
	sjme_message("Waiting for thread ready (%p)...",
		state);
	
	/* Wait for the ready signal, but only if required. */
	if (state->loopThread == SJME_THREAD_NULL)
		sjme_atomic_sjme_jint_set(&state->loopThreadReady, 1);
	else
	{
		while (0 == sjme_atomic_sjme_jint_get(&state->loopThreadReady))
		{
			sjme_thread_barrier();
			sjme_thread_yield();
			sjme_thread_barrier();
		}
	}
	
	/* Return resultant state. */
	*outState = state;
	return SJME_ERROR_NONE;

fail_apiInit:
fail_alloc:
	if (state != NULL)
	{
		sjme_alloc_free(state);
		state = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
	sjme_errorCode error;
	sjme_jboolean isFbWrapper; 
	sjme_jboolean needFbWrapper;
	sjme_scritchui state;
	sjme_scritchui wrappedState;
	
	if (inPool == NULL || inImplFunc == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Do we need the framebuffer wrapper? */
	isFbWrapper = (inImplFunc == &sjme_scritchui_fbFunctions);
	needFbWrapper = !isFbWrapper;
	
	/* Normal top-level initialization. */
	if (!needFbWrapper)
	{
		return sjme_scritchui_core_apiInitActual(inPool, outState,
			inImplFunc, loopExecute, initFrontEnd, NULL);
	}
	
	/* Initialize API we are going to wrap. */
	wrappedState = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInitActual(inPool,
		&wrappedState,
		inImplFunc, NULL,
		NULL, NULL)) ||
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
	if (sjme_error_is(error = sjme_scritchui_core_apiInitActual(inPool,
		&state,
		&sjme_scritchui_fbFunctions,
		loopExecute, initFrontEnd, wrappedState)) ||
		state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}
