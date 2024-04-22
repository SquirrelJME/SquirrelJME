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

/** Invoke serial call and wait for result. */
#define SJME_SCRITCHUI_INVOKE_WAIT \
	do { sjme_thread_barrier(); \
		if (sjme_error_is(error = inState->api->loopExecuteWait(inState, \
		sjme_scritchui_serialDispatch, &data))) \
		return sjme_error_default(error); \
	return data.error; } while (0)

/** Common shared chunk of forwarding code, to reduce duplicates. */
#define SJME_SCRITCHUI_SERIAL_CHUNK(what, whatType, directInvokeArgs) \
	SJME_SCRITCHUI_SERIAL_VARS(what); \
	 \
	SJME_SCRITCHUI_SERIAL_PRE_CHECK; \
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(what); \
	 \
	/* Direct call? */ \
	if (direct) \
		return inState->apiInThread->what directInvokeArgs; \
	 \
	/* Serialize and Store. */ \
	SJME_SCRITCHUI_SERIAL_SETUP( \
		whatType, \
		what)

/** Pass a serial value. */
#define SJME_SCRITCHUI_SERIAL_PASS(what) \
	serial->what = what

/** Declares dispatch type. */
#define SJME_SCRITCHUI_DISPATCH_DECL(what) \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)* volatile what

/** Performs dispatch call. */
#define SJME_SCRITCHUI_DISPATCH_CALL(what, args) \
	do { what = &data->data.what; \
	if (state->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT(SJME_ERROR_NOT_IMPLEMENTED); \
	data->error = state->apiInThread->what args; } while (0)

/** Simplified case call. */
#define SJME_SCRITCHUI_DISPATCH_CASE(what, whatType, args) \
	case whatType: \
		SJME_SCRITCHUI_DISPATCH_CALL(what, args); \
		break

static sjme_thread_result sjme_scritchui_serialDispatch(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	volatile sjme_scritchui_serialData* data;
	SJME_SCRITCHUI_DISPATCH_DECL(componentRevalidate);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSetPaintListener);
	SJME_SCRITCHUI_DISPATCH_DECL(containerAdd);
	SJME_SCRITCHUI_DISPATCH_DECL(containerSetBounds);
	SJME_SCRITCHUI_DISPATCH_DECL(panelNew);
	SJME_SCRITCHUI_DISPATCH_DECL(panelEnableFocus);
	SJME_SCRITCHUI_DISPATCH_DECL(screenSetListener);
	SJME_SCRITCHUI_DISPATCH_DECL(screens);
	SJME_SCRITCHUI_DISPATCH_DECL(windowContentMinimumSize);
	SJME_SCRITCHUI_DISPATCH_DECL(windowNew);
	SJME_SCRITCHUI_DISPATCH_DECL(windowSetVisible);
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
		SJME_SCRITCHUI_DISPATCH_CASE(componentRevalidate,
			SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
			(state,
			componentRevalidate->inComponent));
		
		SJME_SCRITCHUI_DISPATCH_CASE(componentSetPaintListener,
			SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
			(state,
			componentSetPaintListener->inComponent,
			componentSetPaintListener->inListener,
			componentSetPaintListener->copyFrontEnd));
			
		SJME_SCRITCHUI_DISPATCH_CASE(containerAdd,
			SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
			(state,
			containerAdd->inContainer,
			containerAdd->inComponent));
			
		SJME_SCRITCHUI_DISPATCH_CASE(containerSetBounds,
			SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
			(state,
			containerSetBounds->inContainer,
			containerSetBounds->inComponent,
			containerSetBounds->x,
			containerSetBounds->y,
			containerSetBounds->width,
			containerSetBounds->height));
	
		SJME_SCRITCHUI_DISPATCH_CASE(panelEnableFocus,
			SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
			(state,
			panelEnableFocus->inPanel,
			panelEnableFocus->enableFocus));
	
		SJME_SCRITCHUI_DISPATCH_CASE(panelNew,
			SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
			(state,
			panelNew->outPanel));
	
		SJME_SCRITCHUI_DISPATCH_CASE(screenSetListener,
			SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
			(state,
			screenSetListener->callback));
	
		SJME_SCRITCHUI_DISPATCH_CASE(screens,
			SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
			(state,
			screens->outScreens,
			screens->inOutNumScreens));
		
		SJME_SCRITCHUI_DISPATCH_CASE(windowContentMinimumSize,
			SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
			(state,
			windowContentMinimumSize->inWindow,
			windowContentMinimumSize->width,
			windowContentMinimumSize->height));
				
		SJME_SCRITCHUI_DISPATCH_CASE(windowNew,
			SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
			(state,
			windowNew->outWindow));
			
		SJME_SCRITCHUI_DISPATCH_CASE(windowSetVisible,
			SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
			(state,
			windowSetVisible->inWindow,
			windowSetVisible->isVisible));
			
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
	SJME_SCRITCHUI_SERIAL_CHUNK(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
		(inState, inComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentSetPaintListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
		(inState, inComponent, inListener, copyFrontEnd));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(inListener);
	SJME_SCRITCHUI_SERIAL_PASS(copyFrontEnd);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerAdd,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		(inState, inContainer, inComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
		(inState, inContainer, inComponent, x, y, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(x);
	SJME_SCRITCHUI_SERIAL_PASS(y);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		(inState, inPanel, enableFocus));
		
	SJME_SCRITCHUI_SERIAL_PASS(inPanel);
	SJME_SCRITCHUI_SERIAL_PASS(enableFocus);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(panelNew,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
		(inState, outPanel));
		
	SJME_SCRITCHUI_SERIAL_PASS(outPanel);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(screenSetListener,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		(inState, callback));
		
	SJME_SCRITCHUI_SERIAL_PASS(callback);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(screens,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
		(inState, outScreens, inOutNumScreens));
		
	SJME_SCRITCHUI_SERIAL_PASS(outScreens);
	SJME_SCRITCHUI_SERIAL_PASS(inOutNumScreens);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
		(inState, inWindow, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowNew,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
		(inState, outWindow));
		
	SJME_SCRITCHUI_SERIAL_PASS(outWindow);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
		(inState, inWindow, isVisible));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(isVisible);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

