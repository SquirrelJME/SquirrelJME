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
#include "sjme/debug.h"

/** Serial variables. */
#define SJME_SCRITCHUI_SERIAL_VARS(what) \
	sjme_errorCode error; \
	sjme_jboolean direct; \
	volatile sjme_scritchui_serialData data; \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)* volatile serial

/** Pre-check call to make. */
#define SJME_SCRITCHUI_SERIAL_PRE_CHECK \
	do { if (inState == NULL) \
    { \
		return SJME_ERROR_NULL_ARGUMENTS; \
	} } while(0)

/** Check for being in the loop. */
#define SJME_SCRITCHUI_SERIAL_LOOP_CHECK(what) \
	do { \
		error = SJME_ERROR_NOT_IMPLEMENTED; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api->loopIsInThread == NULL || \
            inState->api->what == NULL || \
            inState->apiInThread->what == NULL || \
			sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

/** Setup pre-serialization. */
#define SJME_SCRITCHUI_SERIAL_SETUP(key, what) \
	do { memset(&data, 0, sizeof(data)); \
	data.type = (key); \
	data.error = SJME_ERROR_UNKNOWN; \
	data.state = inState; \
	serial = &data.data.what; } while (0)

/* Invoke serial call and wait for result. */
#define SJME_SCRITCHUI_INVOKE_WAIT \
	do { sjme_thread_barrier(); \
		if (sjme_error_is(error = inState->api->loopExecuteWait(inState, \
		sjme_scritchui_serialDispatch, &data))) \
		return sjme_error_default(error); \
	return data.error; } while (0)

/** Declares dispatch type. */
#define SJME_DISPATCH_DECL(what) \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)* volatile what

/** Performs dispatch call. */
#define SJME_DISPATCH_CALL(what, args) \
	do { what = &data->data.what; \
	if (state->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT(SJME_ERROR_NOT_IMPLEMENTED); \
	data->error = state->apiInThread->what args; } while (0)

static sjme_thread_result sjme_scritchui_serialDispatch(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	volatile sjme_scritchui_serialData* data;
	SJME_DISPATCH_DECL(componentRevalidate);
	SJME_DISPATCH_DECL(componentSetPaintListener);
	SJME_DISPATCH_DECL(containerAdd);
	SJME_DISPATCH_DECL(panelNew);
	SJME_DISPATCH_DECL(panelEnableFocus);
	SJME_DISPATCH_DECL(screenSetListener);
	SJME_DISPATCH_DECL(screens);
	SJME_DISPATCH_DECL(windowContentMinimumSize);
	SJME_DISPATCH_DECL(windowNew);
	sjme_scritchui state;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
		
	/* Emit barrier so we can access this. */
	data = (sjme_scritchui_serialData*)anything;
	sjme_thread_barrier();
	
	/* Restore info. */
	state = data->state;
	
	/* Depends on the type... */
	switch (data->type)
	{
		case SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE:
			SJME_DISPATCH_CALL(componentRevalidate,
				(state,
				componentRevalidate->inComponent));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER:
			SJME_DISPATCH_CALL(componentSetPaintListener,
				(state,
				componentSetPaintListener->inComponent,
				componentSetPaintListener->inListener,
				componentSetPaintListener->copyFrontEnd));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD:
			SJME_DISPATCH_CALL(containerAdd,
				(state,
				containerAdd->inContainer,
				containerAdd->inComponent));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS:
			SJME_DISPATCH_CALL(panelEnableFocus,
				(state,
				panelEnableFocus->inPanel,
				panelEnableFocus->enableFocus));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW:
			SJME_DISPATCH_CALL(panelNew,
				(state,
				panelNew->outPanel));
			break;
	
		case SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER:
			SJME_DISPATCH_CALL(screenSetListener,
				(state,
				screenSetListener->callback));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_SCREENS:
			SJME_DISPATCH_CALL(screens,
				(state,
				screens->outScreens,
				screens->inOutNumScreens));
			break;
		
		case SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE:
			SJME_DISPATCH_CALL(windowContentMinimumSize,
				(state,
				windowContentMinimumSize->inWindow,
				windowContentMinimumSize->width,
				windowContentMinimumSize->height));
			break;
	
		case SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW:
			SJME_DISPATCH_CALL(windowNew,
				(state,
				windowNew->outWindow));
			break;
			
		default:
			return SJME_THREAD_RESULT(SJME_ERROR_NOT_IMPLEMENTED);
	}
	
	/* Map result. */
	return SJME_THREAD_RESULT(data->error);
}

sjme_errorCode sjme_scritchui_coreSerial_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_VARS(componentRevalidate);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(componentRevalidate);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->componentRevalidate(inState,
			inComponent);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
		componentRevalidate);
	serial->inComponent = inComponent;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	SJME_SCRITCHUI_SERIAL_VARS(componentSetPaintListener);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(componentSetPaintListener);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->componentSetPaintListener(inState,
			inComponent, inListener, copyFrontEnd);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
		componentSetPaintListener);
	serial->inComponent = inComponent;
	serial->inListener = inListener;
	serial->copyFrontEnd = copyFrontEnd;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_VARS(containerAdd);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(containerAdd);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->containerAdd(inState,
			inContainer, inComponent);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		containerAdd);
	serial->inContainer = inContainer;
	serial->inComponent = inComponent;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus)
{
	SJME_SCRITCHUI_SERIAL_VARS(panelEnableFocus);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(panelEnableFocus);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->panelEnableFocus(inState,
			inPanel, enableFocus);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		panelEnableFocus);
	serial->inPanel = inPanel;
	serial->enableFocus = enableFocus;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	SJME_SCRITCHUI_SERIAL_VARS(panelNew);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(panelNew);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->panelNew(inState,
			outPanel);

	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
		panelNew);
	serial->outPanel = outPanel;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback)
{
	SJME_SCRITCHUI_SERIAL_VARS(screenSetListener);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(screenSetListener);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->screenSetListener(inState,
			callback);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		screenSetListener);
	serial->callback = callback;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	SJME_SCRITCHUI_SERIAL_VARS(screens);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(screens);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->screens(inState,
			outScreens, inOutNumScreens);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
		screens);
	serial->outScreens = outScreens;
	serial->inOutNumScreens = inOutNumScreens;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_VARS(windowContentMinimumSize);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(windowContentMinimumSize);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->windowContentMinimumSize(inState,
			inWindow, width, height);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
		windowContentMinimumSize);
	serial->inWindow = inWindow;
	serial->width = width;
	serial->height = height;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	SJME_SCRITCHUI_SERIAL_VARS(windowNew);
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(windowNew);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->windowNew(inState,
			outWindow);
	
	/* Serialize and Store. */
	SJME_SCRITCHUI_SERIAL_SETUP(
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
		windowNew);
	serial->outWindow = outWindow;
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
