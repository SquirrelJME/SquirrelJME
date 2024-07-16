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
#include "lib/scritchui/core/core.h"

static sjme_errorCode sjme_scritchui_fb_inputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_scritchui topState;
	sjme_scritchui_uiComponent topComponent;
	sjme_errorCode error;
	
	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get owning state and component. */
	topState = inComponent->common.frontEnd.data;
	topComponent = inComponent->common.frontEnd.wrapper;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fb_paintListener(
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
	wrappedComponent = inComponent->common.handle;
	
	/* Forward repaint. */
	return wrappedState->api->componentRepaint(wrappedState,
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
	wrappedComponent = inComponent->common.handle;
	
	/* Forward repaint. */
	return wrappedState->api->componentRevalidate(wrappedState,
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
	sjme_frontEnd frontEnd;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedComponent = inComponent->common.handle;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NONE;
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
	wrappedComponent = inComponent->common.handle;
		
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
	return wrappedState->api->componentSetPaintListener(wrappedState,
		wrappedComponent,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_paintListener), 
			&wrappedFrontEnd);
}
