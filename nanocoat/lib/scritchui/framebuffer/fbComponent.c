/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"

static sjme_errorCode sjme_scritchui_fb_listenerInput(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_scritchui topState;
	sjme_scritchui_uiComponent topComponent;
	sjme_scritchui_listener_input* infoCore;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inComponent->common.frontEnd.data;
	topComponent = inComponent->common.frontEnd.wrapper;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topComponent, input);

	/* Forward call. */
	return infoCore->callback(topState, topComponent,
		inEvent);
}

static sjme_errorCode sjme_scritchui_fb_listenerPaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special)
{
	sjme_errorCode error;
	sjme_scritchui topState;
	sjme_scritchui_uiComponent topComponent;
	sjme_scritchui_uiPaintable topPaint;
	sjme_scritchui_listener_paint* infoCore;
	
	if (inState == NULL || inComponent == NULL || g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inComponent->common.frontEnd.data;
	topComponent = inComponent->common.frontEnd.wrapper;
	
	/* Get the top component's paint. */
	topPaint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(
		topState, topComponent, &topPaint)) ||
		topPaint == NULL)
		return sjme_error_default(error);

	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topPaint, paint);

	/* Forward call. */
	return infoCore->callback(topState, topComponent,
		g, sw, sh, special);
}

static sjme_errorCode sjme_scritchui_fb_listenerSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositiveNonZero sjme_jint newWidth,
	sjme_attrInPositiveNonZero sjme_jint newHeight)
{
	sjme_scritchui topState;
	sjme_scritchui_uiComponent topComponent;
	sjme_scritchui_listener_size* infoCore;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inComponent->common.frontEnd.data;
	topComponent = inComponent->common.frontEnd.wrapper;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topComponent, size);

	/* Forward call. */
	return infoCore->callback(topState, topComponent,
		newWidth, newHeight);
}

static sjme_errorCode sjme_scritchui_fb_listenerVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean fromVisible,
	sjme_attrInValue sjme_jboolean toVisible)
{
	sjme_scritchui topState;
	sjme_scritchui_uiComponent topComponent;
	sjme_scritchui_listener_visible* infoCore;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inComponent->common.frontEnd.data;
	topComponent = inComponent->common.frontEnd.wrapper;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topComponent, visible);

	/* Forward call. */
	return infoCore->callback(topState, topComponent,
		fromVisible, toVisible);
}

sjme_errorCode sjme_scritchui_fb_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent = inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward. */
	return wrappedState->api->componentFocusGrab(wrappedState, 
		wrappedComponent);
}

sjme_errorCode sjme_scritchui_fb_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL || outHasFocus == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent = inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward. */
	return wrappedState->api->componentFocusHas(wrappedState, 
		wrappedComponent, outHasFocus);
}

sjme_errorCode sjme_scritchui_fb_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward repaint. */
	return wrappedState->apiInThread->componentRepaint(wrappedState,
		wrappedComponent, x, y, width, height);
}

sjme_errorCode sjme_scritchui_fb_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent = inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward call. */
	return wrappedState->apiInThread->componentRevalidate(wrappedState,
		wrappedComponent);
}

sjme_errorCode sjme_scritchui_fb_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input))
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	sjme_frontEnd wrappedFrontEnd;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, inComponent,
		(sjme_scritchui_listener_void*)
			&SJME_SCRITCHUI_LISTENER_CORE(inComponent, input),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd, &wrappedFrontEnd)))
		return sjme_error_default(error);
		
	/* Have wrapped handler call our wrapped listener. */
	return wrappedState->apiInThread->componentSetInputListener(
		wrappedState,
		wrappedComponent,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_listenerInput), 
			&wrappedFrontEnd);
}

sjme_errorCode sjme_scritchui_fb_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	sjme_frontEnd wrappedFrontEnd;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
		
	/* Get the component's paint. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, inComponent,
		(sjme_scritchui_listener_void*)
			&SJME_SCRITCHUI_LISTENER_CORE(paint, paint),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd, &wrappedFrontEnd)))
		return sjme_error_default(error);
		
	/* Have wrapped handler call our wrapped listener. */
	return wrappedState->apiInThread->componentSetPaintListener(
		wrappedState,
		wrappedComponent,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_listenerPaint),
		&wrappedFrontEnd);
}

sjme_errorCode sjme_scritchui_fb_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size))
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	sjme_frontEnd wrappedFrontEnd;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, inComponent,
		(sjme_scritchui_listener_void*)
			&SJME_SCRITCHUI_LISTENER_CORE(inComponent, size),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd, &wrappedFrontEnd)))
		return sjme_error_default(error);
		
	/* Have wrapped handler call our wrapped listener. */
	return wrappedState->apiInThread->componentSetSizeListener(
		wrappedState,
		wrappedComponent,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_listenerSize), 
			&wrappedFrontEnd);
}

sjme_errorCode sjme_scritchui_fb_componentSetVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible))
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	sjme_frontEnd wrappedFrontEnd;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, inComponent,
		(sjme_scritchui_listener_void*)
			&SJME_SCRITCHUI_LISTENER_CORE(inComponent, visible),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd, &wrappedFrontEnd)))
		return sjme_error_default(error);
		
	/* Have wrapped handler call our wrapped listener. */
	return wrappedState->apiInThread->componentSetVisibleListener(
		wrappedState,
		wrappedComponent,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_listenerVisible), 
			&wrappedFrontEnd);
}

sjme_errorCode sjme_scritchui_fb_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiComponent wrappedComponent;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent =
		inComponent->common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward call. */
	return wrappedState->apiInThread->componentSize(wrappedState,
		wrappedComponent, outWidth, outHeight);
}
