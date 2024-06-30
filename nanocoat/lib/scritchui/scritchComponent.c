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

static sjme_errorCode sjme_scritchui_baseInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_basePaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint* info;
	sjme_scritchui_paintListenerFunc callback;
	
	if (inState == NULL || inComponent == NULL || g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not something we can paint? */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_ARGUMENT);
	
	/* Base info. */		
	info = &SJME_SCRITCHUI_LISTENER_USER(paint, paint);
	
	/* No actual paint listener? */
	callback = info->callback;
	if (callback == NULL)
	{
		error = SJME_ERROR_NO_LISTENER;
		goto fail_noListener;
	}
	
	/* Forward to callback. */
	sjme_atomic_sjme_jint_set(&paint->inPaint, 1);
	error = callback(inState, inComponent,
		g, sw, sh, special);
	
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
	sjme_scritchui_listener_size* info;
	sjme_scritchui_sizeListenerFunc callback;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Base info. */		
	info = &SJME_SCRITCHUI_LISTENER_USER(inComponent, size);
	
	/* Call user handler, if there is one */
	callback = info->callback;
	if (callback != NULL)
		if (sjme_error_is(error = callback(inState, inComponent,
			newWidth, newHeight)))
			return sjme_error_default(error);
	
	/* Schedule repaint, ignore any errors. */
	inState->api->componentRepaint(inState, inComponent,
		0, 0, INT32_MAX, INT32_MAX);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_baseVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean fromVisible,
	sjme_attrInValue sjme_jboolean toVisible)
{
	sjme_scritchui_listener_visible* infoUser;
	sjme_jboolean wasVisible;
	sjme_jboolean wasUserVisible;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Has core visibility changed? We do not want to spam visibility */
	/* changes to our components if there is no point to it. */
	wasVisible = inComponent->isVisible;
	if (toVisible != wasVisible)
	{
		/* Update visibility state. */
		inComponent->isVisible = toVisible;
	}
	
	/* There may be a delay before a listener is set for a user listener */
	/* so wait until that occurs. */
	wasUserVisible = inComponent->isUserVisible;
	if (toVisible != wasUserVisible)
	{
		/* Send to callback accordingly, if one is set. */
		infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, visible);
		if (infoUser->callback != NULL)
		{
			/* Update state. */
			inComponent->isUserVisible = toVisible;
			
			return infoUser->callback(inState, inComponent,
				wasVisible, toVisible);
		}
	}
	
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

static sjme_errorCode sjme_scritchui_core_componentSetSimpleUserListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoUser,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void))
{
	if (inState == NULL || inComponent == NULL || infoUser == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The core listener is always set, so we can just set this here */
	/* and any future size calls will use this callback. */
	infoUser->callback = inListener;
	if (inListener != NULL && copyFrontEnd != NULL)
		memmove(&infoUser->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Clear old front end data for the user if the listener was cleared. */
	if (inListener == NULL)
		memset(&infoUser->frontEnd, 0, sizeof(infoUser->frontEnd));
	
	/* Success! */
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_scritchui_core_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input))
{
	sjme_errorCode error;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can only be set for panels. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_PANEL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set core listener events which is forwarded to for handling. */
	if (inState->impl->componentSetInputListener != NULL)
		if (sjme_error_is(error = inState->impl->
			componentSetInputListener(inState, inComponent,
			(inListener != NULL ? sjme_scritchui_baseInputListener :
				NULL), NULL)))
			return sjme_error_default(error);

	/* Set user listener. */
	return sjme_scritchui_core_componentSetSimpleUserListener(
		inState,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, input),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_scritchui inState,
	sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_scritchui_listener_paint undo;
	sjme_scritchui_listener_paint* infoCore;
	sjme_scritchui_listener_paint* infoUser;
	sjme_scritchui_paintListenerFunc coreCallback;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not supported? */
	if (inState->impl->componentSetPaintListener == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Only certain types can be painted on. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Get listener information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(paint, paint);
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(paint, paint);
	
	/* Copy data for undo. */
	memmove(&undo, infoUser, sizeof(undo));
	
	/* Set new listener data. */
	infoUser->callback = inListener;
	if (copyFrontEnd != NULL)
		memmove(&infoUser->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Which core callback is being used? */
	coreCallback = (inListener != NULL ? sjme_scritchui_basePaintListener :
		NULL);
	
	/* Is this callback changing? We need to set a new one! */
	if (infoCore->callback != coreCallback)
		if (sjme_error_is(error =
			inState->impl->componentSetPaintListener(inState, inComponent,
				coreCallback, NULL)))
			goto fail_coreSet;
	
	/* If there is a repaint handler, then run it but ignore any errors. */
	if (inState->apiInThread->componentRepaint != NULL)
		inState->apiInThread->componentRepaint(inState, inComponent,
			0, 0, INT32_MAX, INT32_MAX);
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_coreSet:
	/* Undo change. */
	memmove(infoUser, &undo, sizeof(undo));
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	if (inState == NULL || inComponent == NULL ||
		(outWidth == NULL && outHeight == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not supported? */
	if (inState->impl->componentSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return inState->impl->componentSize(inState, inComponent,
		outWidth, outHeight);
}

sjme_errorCode sjme_scritchui_core_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_scritchui_core_componentSetSimpleUserListener(
		inState,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, size),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_componentSetVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_scritchui_core_componentSetSimpleUserListener(
		inState,
		inComponent,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, visible),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
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
		/* Install size listener to emit repaints on resize. */
		if (inState->impl->componentSetSizeListener != NULL)
			if (sjme_error_is(error =
				inState->impl->componentSetSizeListener(inState,
				inComponent,
				sjme_scritchui_core_baseSizeListener,
				NULL)))
				return sjme_error_default(error);
		
		/* Set base visibility listener. */
		if (inState->impl->componentSetVisibleListener != NULL)
		{
			if (sjme_error_is(error =
				inState->impl->componentSetVisibleListener(inState,
				inComponent,
				sjme_scritchui_core_baseVisibleListener,
				NULL)))
				return sjme_error_default(error);
		}
		
		/* If there is no native support for listeners, still set it */
		/* as we will handle visibility ourselves manually. */
		else
			SJME_SCRITCHUI_LISTENER_CORE(inComponent, visible)
				.callback = sjme_scritchui_core_baseVisibleListener;
		
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

sjme_errorCode sjme_scritchui_core_intern_updateVisibleComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_scritchui_listener_visible* infoUser;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There always is a core interface! */
	infoUser = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, visible);
	if (infoUser->callback != NULL)
		return infoUser->callback(inState, inComponent,
			inComponent->isVisible, isVisible);
	
	/* There was no callback, so just success. */
	return SJME_ERROR_NONE;
}
