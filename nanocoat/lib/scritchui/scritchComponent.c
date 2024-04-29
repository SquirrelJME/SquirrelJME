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
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_scritchui_basePaintListener(
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
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_paintListenerFunc listener;
	
	if (inState == NULL || inComponent == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not something we can paint? */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_ARGUMENT);
	
	/* No actual paint listener? */
	listener = paint->listeners[SJME_SCRITCHUI_LISTENER_USER].paint;
	if (listener == NULL)
	{
		error = SJME_ERROR_NO_LISTENER;
		goto fail_noListener;
	}
	
	/* Forward to callback. */
	sjme_atomic_sjme_jint_set(&paint->inPaint, 1);
	error = listener(inState, inComponent,
		pf,
		bw, bh,
		buf, bufOff, bufLen,
		pal, numPal,
		sx, sy, sw, sh, special);
	
	/* No longer painting. */
	sjme_atomic_sjme_jint_set(&paint->inPaint, 0);
	
	/* Success or failure! */
fail_noListener:
	paint->lastError = error;
	return error;
}

static sjme_errorCode sjme_scritchui_core_baseSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositiveNonZero sjme_jint newWidth,
	sjme_attrInPositiveNonZero sjme_jint newHeight)
{
	sjme_errorCode error;
	sjme_scritchui_sizeListenerFunc forward;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Call user handler */
	forward = inComponent->listeners[SJME_SCRITCHUI_LISTENER_USER]
		.size;
	if (forward != NULL)
		if (sjme_error_is(error = forward(inState, inComponent,
			newWidth, newHeight)))
			return sjme_error_default(error);
	
	/* Schedule repaint, ignore any errors. */
	inState->api->componentRepaint(inState, inComponent,
		0, 0, INT32_MAX, INT32_MAX);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/**
 * Belayed repainting.
 * 
 * @param anything The input component. 
 * @return On any error.
 * @since 2024/04/26
 */
static sjme_thread_result sjme_scritchui_core_componentRepaintBelay(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_rect rect;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Recover component and state. */
	inComponent = (sjme_scritchui_uiComponent)anything;
	inState = inComponent->common.state;
	
	/* Only certain types are paintable. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return SJME_THREAD_RESULT(sjme_error_default(error));
	
	/* Call paint now. */
	rect = paint->belayRect;
	error = inState->impl->componentRepaint(inState, inComponent,
		rect.x, rect.y, rect.width, rect.height);
	return SJME_THREAD_RESULT(error);
}

sjme_errorCode sjme_scritchui_core_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Not implemented? */
	if (inState->impl->componentRepaint == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Only certain types are paintable. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Rather than failing, just normalize. */
	if (x < 0)
		x = 0;
	if (y < 0)
		y = 0;
	if (width <= 0)
		width = INT32_MAX;
	if (height <= 0)
		height = INT32_MAX;
	
	/* If we are in a paint, we need to delay painting by a single frame */
	/* otherwise the native UI might get stuck not repainting or end up */
	/* in an infinite loop. */
	if (sjme_atomic_sjme_jint_get(&paint->inPaint) != 0)
	{
		/* Store paint properties. */
		paint->belayRect.x = x;
		paint->belayRect.y = y;
		paint->belayRect.width = width;
		paint->belayRect.height = height;
		
		/* Schedule for later, if it errors fall through to paint. */
		if (!sjme_error_is(inState->api->loopExecuteLater(inState,
			sjme_scritchui_core_componentRepaintBelay,
			inComponent)))
			return SJME_ERROR_NONE;
	}
	
	/* Forward. */
	return inState->impl->componentRepaint(inState, inComponent,
		x, y, width, height);
}

sjme_errorCode sjme_scritchui_core_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inState->impl->componentRevalidate == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Forward call. */
	return inState->impl->componentRevalidate(inState, inComponent);
}

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_scritchui inState,
	sjme_scritchui_uiComponent inComponent,
	sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_frontEnd oldFrontEnd;
	sjme_scritchui_paintListenerFunc oldUserListener, oldCoreListener;
	sjme_scritchui_uiPaintableListeners* coreListener;
	sjme_scritchui_uiPaintableListeners* userListener;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be painted on. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Set new listener. */
	userListener = &paint->listeners[SJME_SCRITCHUI_LISTENER_USER];
	oldUserListener = userListener->paint;
	userListener->paint = inListener;
	
	/* Set core callback for common handling. */
	coreListener = &paint->listeners[SJME_SCRITCHUI_LISTENER_CORE];
	oldCoreListener = coreListener->paint;
	if (inListener != NULL)
		coreListener->paint = sjme_scritchui_basePaintListener;
	else
		coreListener->paint = NULL;
	
	/* Copy old front end in the event of an error. */
	memmove(&oldFrontEnd, &paint->frontEnd,
		sizeof(sjme_frontEnd));
	
	/* Replace with new front end data before the call. */
	if (copyFrontEnd != NULL && inListener != NULL)
		memmove(&paint->frontEnd, copyFrontEnd,
			sizeof(sjme_frontEnd));
	
	/* Inform component of updated listener. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (inState->impl->componentSetPaintListener == NULL ||
		sjme_error_is(error = inState->impl->componentSetPaintListener(
			inState, inComponent, sjme_scritchui_basePaintListener,
			copyFrontEnd)))
	{
		/* Error, copy old value back. */
		userListener->paint = oldUserListener;
		coreListener->paint = oldCoreListener;
		memmove(&paint->frontEnd, &oldFrontEnd,
			sizeof(sjme_frontEnd));
		
		return sjme_error_default(error);
	}
	
	/* If there is a repaint handler, then run it but ignore any errors. */
	if (inState->apiInThread->componentRepaint != NULL)
		inState->apiInThread->componentRepaint(inState, inComponent,
			0, 0, INT32_MAX, INT32_MAX);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_sizeListenerFunc inListener,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_intern_getPaintable(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiPaintable* outPaintable)
{
	sjme_scritchui_uiPaintable paint;
	
	if (inState == NULL || inComponent == NULL || outPaintable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be painted on. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_PANEL:
			paint = &((sjme_scritchui_uiPanel)inComponent)->paint;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outPaintable = paint;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_initComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_uiContainer container;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Post-initialize? */
	if (postCreate)
	{
		/* Install core size listener. */
		if (inState->impl->componentSetSizeListener != NULL)
			if (sjme_error_is(error =
				inState->impl->componentSetSizeListener(inState, inComponent,
				sjme_scritchui_core_baseSizeListener, NULL)))
				return sjme_error_default(error);
		
		/* Common paintable base initialization. */
		paint = NULL;
		if (!sjme_error_is(error = inState->intern->getPaintable(
			inState, inComponent, &paint)) &&
			paint != NULL)
		{
		}
		
		/* Common container base initialization. */
		container = NULL;
		if (!sjme_error_is(error = inState->intern->getContainer(
			inState, inComponent, &container)) &&
			container != NULL)
		{
		}
	}
	
	/* Pre-initialize? */
	else
	{
		/* Type must be valid. */
		if (uiType <= SJME_SCRITCHUI_TYPE_RESERVED ||
			uiType >= SJME_NUM_SCRITCHUI_UI_TYPES)
			return SJME_ERROR_INVALID_ARGUMENT;
		
		/* Set base properties. */
		inComponent->common.state = inState;
		inComponent->common.type = uiType;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
