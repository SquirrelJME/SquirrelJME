/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_scritchui_baseInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_scritchui_listener_input* infoUser;
	sjme_scritchinput_event clone;
	
	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get callback information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, input);
	
	/* Clone event data for normalization. */
	memmove(&clone, inEvent, sizeof(clone));
	
	/* Store last mouse position for any motion. */
	if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION)
	{
		/* Remember position of last motion. */
		inComponent->state.mouseX = clone.data.mouseMotion.x;
		inComponent->state.mouseY = clone.data.mouseMotion.y;
		
		/* Do we need to restore a button mask? Some GUIs do not have this */
		/* information during motion events. */
		if (clone.data.mouseMotion.buttonMask == 0)
			clone.data.mouseMotion.buttonMask =
				inComponent->state.mouseButtons;
	}
	
	/* Do we need to set the position for mouse button clicks? */
	else if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED ||
		clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED)
	{
		/* Either set or clear the mouse button bit. */
		sjme_jint bit = (1 << clone.data.mouseButton.button); 
		if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED)
			inComponent->state.mouseButtons |= bit;
		else
			inComponent->state.mouseButtons &= ~bit;
		
		/* Restore X or save it? */
		if (clone.data.mouseButton.x == 0)
			clone.data.mouseButton.x = inComponent->state.mouseX;
		else
			inComponent->state.mouseX = clone.data.mouseButton.x;
			
		/* Restore Y or save it? */
		if (clone.data.mouseButton.y == 0)
			clone.data.mouseButton.y = inComponent->state.mouseY;
		else
			inComponent->state.mouseY = clone.data.mouseButton.y;
	}
	
	/* Forward to callback! */
	if (infoUser->callback != NULL)
		return infoUser->callback(inState, inComponent, &clone);
	return SJME_ERROR_NONE;
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
	sjme_scritchui_listener_paint* infoUser;
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
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(paint, paint);
	
	/* No actual paint listener? */
	callback = infoUser->callback;
	if (callback == NULL)
	{
		error = SJME_ERROR_NO_LISTENER;
		goto fail_noListener;
	}
	
	/* Forward to callback. */
	sjme_atomic_sjme_jint_set(&paint->inPaint, 1);
	error = callback(inState, inComponent, g, sw, sh, special);

#if defined(SJME_CONFIG_DEBUG)
	/* Error? */
	if (sjme_error_is(error))
		sjme_message("Paint failed: %d", error);
#endif
	
	/* No longer painting. */
	sjme_atomic_sjme_jint_set(&paint->inPaint, 0);
	
	/* Success or failure! */
fail_noListener:
	paint->lastError = error;
	return error;
}

static sjme_errorCode sjme_scritchui_core_baseActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_errorCode error;
	sjme_scritchui_listener_activate* info;
	sjme_scritchui_activateListenerFunc callback;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Base info. */		
	info = &SJME_SCRITCHUI_LISTENER_USER(inComponent, activate);
	
	/* Call user handler, if there is one */
	callback = info->callback;
	if (callback != NULL)
		if (sjme_error_is(error = callback(inState, inComponent)))
			return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	wasVisible = inComponent->state.isVisible;
	if (toVisible != wasVisible)
	{
		/* Update visibility state. */
		inComponent->state.isVisible = toVisible;
	}
	
	/* There may be a delay before a listener is set for a user listener */
	/* so wait until that occurs. */
	wasUserVisible = inComponent->state.isUserVisible;
	if (toVisible != wasUserVisible)
	{
		/* Send to callback accordingly, if one is set. */
		infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, visible);
		if (infoUser->callback != NULL)
		{
			/* Update state. */
			inComponent->state.isUserVisible = toVisible;
			
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
		rect.s.x, rect.s.y, rect.d.width, rect.d.height);
	return SJME_THREAD_RESULT(error);
}

sjme_errorCode sjme_scritchui_core_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->componentFocusGrab == NULL)
		return sjme_error_notImplemented(0);
	
	/* Direct forward. */
	return inState->impl->componentFocusGrab(inState, inComponent);
}

sjme_errorCode sjme_scritchui_core_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	if (inState == NULL || inComponent == NULL || outHasFocus == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->componentFocusHas == NULL)
		return sjme_error_notImplemented(0);
	
	/* Direct forward. */
	return inState->impl->componentFocusHas(inState, inComponent, outHasFocus);
}

sjme_errorCode sjme_scritchui_core_componentGetParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent)
{
	if (inState == NULL || inComponent == NULL || outParent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This is a simple read. */
	*outParent = inComponent->parent;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_componentPosition(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY)
{
	if (inState == NULL || inComponent == NULL ||
		(outX == NULL && outY == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is native position information, use it. */
	if (inState->impl->componentPosition != NULL)
		return inState->impl->componentPosition(inState, inComponent,
			outX, outY);
	
	/* Otherwise, fallback to last known bounds. */
	if (outX != NULL)
		*outX = inComponent->bounds.s.x;
	if (outY != NULL)
		*outY = inComponent->bounds.s.y;
	
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
		return sjme_error_notImplemented(0);
		
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
		paint->belayRect.s.x = x;
		paint->belayRect.s.y = y;
		paint->belayRect.d.width = width;
		paint->belayRect.d.height = height;
		
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
	sjme_errorCode error;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inState->impl->componentRevalidate == NULL)
		return sjme_error_notImplemented(0);
	
	/* Forward call. */
	if (sjme_error_is(error = inState->impl->componentRevalidate(inState,
		inComponent)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_componentSetActivateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(activate))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, activate),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_componentSetInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(input))
{
	sjme_errorCode error;
	sjme_scritchui_inputListenerFunc coreListener;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can only be set for panels. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_PANEL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Using core input listener? */
	coreListener = NULL;
	if (inListener != NULL)
		coreListener = sjme_scritchui_baseInputListener;
	
	/* Set core listener events which is forwarded to for handling. */
	if (inState->impl->componentSetInputListener != NULL)
	{
		if (sjme_error_is(error = inState->impl->
			componentSetInputListener(inState, inComponent,
			coreListener, NULL)))
			return sjme_error_default(error);
	}
	
	/* Set one regardless, there might be another way to handle. */
	else
	{
		if (sjme_error_is(error = inState->intern->setSimpleListener(
			inState,
			(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_CORE(
				inComponent, input),
			(sjme_scritchui_voidListenerFunc)coreListener,
			copyFrontEnd)))
			return sjme_error_default(error);
	}

	/* Set user listener. */
	return inState->intern->setSimpleListener(
		inState,
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
		return sjme_error_notImplemented(0);
	
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
	if (sjme_error_is(error = inState->intern->setSimpleListener(
		inState, (sjme_scritchui_listener_void*)infoUser,
		(sjme_scritchui_voidListenerFunc)inListener, copyFrontEnd)))
		return sjme_error_default(error);
	
	/* Which core callback is being used? */
	coreCallback = (inListener != NULL ? sjme_scritchui_basePaintListener :
		NULL);
	
	/* Is this callback changing? We need to set a new one! */
	if (infoCore->callback != coreCallback)
	{
		if (sjme_error_is(error =
			inState->impl->componentSetPaintListener(inState, inComponent,
				coreCallback, NULL)))
			goto fail_coreSet;
		
		/* Set new listener data, if it was not changed. */
		if (infoCore->callback != coreCallback)
			if (sjme_error_is(error = inState->intern->setSimpleListener(
				inState, (sjme_scritchui_listener_void*)infoCore,
				(sjme_scritchui_voidListenerFunc)inListener,
				copyFrontEnd)))
				return sjme_error_default(error);
	}
	
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

sjme_errorCode sjme_scritchui_core_componentSetSizeListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(size))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, size),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
}

sjme_errorCode sjme_scritchui_core_componentSetValueUpdateListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(valueUpdate))
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_core_componentSetVisibleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(visible))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			inComponent, visible),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd);
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

sjme_errorCode sjme_scritchui_core_intern_initCommon(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInValue sjme_jboolean postCreate,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType)
{
	sjme_errorCode error;
	sjme_alloc_link* link;
	
	if (inState == NULL || inCommon == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Post-initialize? */
	if (postCreate)
	{
		/* Currently nothing. */
	}
	
	/* Pre-initialize? */
	else
	{
		/* Must be directly linked. */
		link = NULL;
		if (sjme_error_is(error = sjme_alloc_getLink(inCommon,
			&link)) || link == NULL)
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_LINK);
		
		/* Must be weak referenced. */
		if (link->weak == NULL)
			return SJME_ERROR_NOT_WEAK_REFERENCE;
		
		/* Type must be valid. */
		if (uiType <= SJME_SCRITCHUI_TYPE_RESERVED ||
			uiType >= SJME_NUM_SCRITCHUI_UI_TYPES)
			return SJME_ERROR_INVALID_ARGUMENT;
		
		/* Set base properties. */
		inCommon->state = inState;
		inCommon->type = uiType;
	}
	
	/* Success! */
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
		
	/* Common initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(
		inState, &inComponent->common, postCreate, uiType)))
		return sjme_error_default(error);
	
	/* Post-initialize? */
	if (postCreate)
	{
		/* Install activate listener for activation events. */
		if (inState->impl->componentSetActivateListener != NULL)
		{
			if (sjme_error_is(error =
				inState->impl->componentSetActivateListener(inState,
				inComponent,
				sjme_scritchui_core_baseActivateListener,
				NULL)))
				return sjme_error_default(error);
		}
		else
		{
			/* Still set the activation listener because there might be */
			/* implicit soft activates. */
			SJME_SCRITCHUI_LISTENER_CORE(inComponent, activate)
				.callback = sjme_scritchui_core_baseActivateListener;
		}
		
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
		else
		{
			/* If there is no native support for listeners, still set it */
			/* as we will handle visibility ourselves manually. */
			SJME_SCRITCHUI_LISTENER_CORE(inComponent, visible)
				.callback = sjme_scritchui_core_baseVisibleListener;
		}
		
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
		/* Make up a string based ID for the component. */
		snprintf(inComponent->strId,
			SJME_SCRITCHUI_UI_COMPONENT_ID_STRLEN - 1,
			"sjme%p", inComponent);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_setSimpleListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoAny,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void))
{
	if (inState == NULL || infoAny == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set new callback and copy any front-end data as needed. */
	infoAny->callback = inListener;
	if (inListener != NULL && copyFrontEnd != NULL)
		memmove(&infoAny->frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Clear old front end data for the listener if it was cleared. */
	if (inListener == NULL)
		memset(&infoAny->frontEnd, 0, sizeof(infoAny->frontEnd));
	
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
			inComponent->state.isVisible, isVisible);
	
	/* There was no callback, so just success. */
	return SJME_ERROR_NONE;
}
