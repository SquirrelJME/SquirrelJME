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

static sjme_errorCode sjme_scritchui_baseInputListenerMouse(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_scritchui_listener_input* infoUser;
	sjme_scritchui_uiMouseState* currentMouse;
	sjme_scritchui_uiMouseState* logicalMouse;
	sjme_scritchinput_event emit;
	sjme_jint buttonChange, shift;
	sjme_jboolean pressed;
	
	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We are adjusting with multiple mouse states. */
	currentMouse = &inComponent->state.mouse[0];
	logicalMouse = &inComponent->state.mouse[1];
	
	/* Get callback information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, input);
	
	/* Always copy modifiers over. */
	logicalMouse->mouseModifiers = currentMouse->mouseModifiers;
	
	/* Has there been a change to buttons? */
	if (currentMouse->mouseButtons != logicalMouse->mouseButtons)
	{
		/* Store new position, if valid. */
		if (currentMouse->mouseX != 0)
			logicalMouse->mouseX = currentMouse->mouseX;
		if (currentMouse->mouseY != 0)
			logicalMouse->mouseY = currentMouse->mouseY;
		
		/* Determine the buttons that changed. */
		buttonChange = (currentMouse->mouseButtons ^
			logicalMouse->mouseButtons);
		
		/* If anything changed, go through each button. */
		for (shift = 0; buttonChange != 0 && shift <= 31; shift++)
		{
			/* Button did not change? */
			int mask = (1 << shift);
			if ((buttonChange & mask) == 0)
				continue;
			
			/* We are changing this, so flip to new state. */
			logicalMouse->mouseButtons ^= mask;
				
			/* Is this being pressed or released? */
			pressed = ((logicalMouse->mouseButtons & mask) != 0);
				
			/* Setup event. */
			memset(&emit, 0, sizeof(emit));
			if (pressed)
				emit.type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED;
			else
				emit.type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED;
			emit.time = inEvent->time;
			emit.data.mouseButton.button = shift + 1;
			emit.data.mouseButton.buttonMask = logicalMouse->mouseButtons;
			emit.data.mouseButton.modifiers = logicalMouse->mouseModifiers;
			emit.data.mouseButton.x = logicalMouse->mouseX;
			emit.data.mouseButton.y = logicalMouse->mouseY;

#if 0
			/* Debug. */
			sjme_message("Mouse Button: %s %d %08x (%d, %d) [sh=%d, bc=%08x]",
				(pressed ? "pressed" : "released"),
				emit.data.mouseButton.buttonMask,
				emit.data.mouseButton.button,
				emit.data.mouseButton.x,
				emit.data.mouseButton.y,
				shift, buttonChange);
#endif
			
			/* Emit a button event. */
			if (infoUser->callback != NULL)
				return infoUser->callback(inState, inComponent, &emit);
		}
	}
	
	/* Has there been a change of position? */
	if (currentMouse->mouseX != logicalMouse->mouseX ||
		currentMouse->mouseY != logicalMouse->mouseY)
	{
		/* Store new position. */
		logicalMouse->mouseX = currentMouse->mouseX;
		logicalMouse->mouseY = currentMouse->mouseY;
		
		/* Setup event. */
		memset(&emit, 0, sizeof(emit));
		emit.type = SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION;
		emit.time = inEvent->time;
		emit.data.mouseMotion.buttonMask = logicalMouse->mouseButtons;
		emit.data.mouseMotion.modifiers = logicalMouse->mouseModifiers;
		emit.data.mouseMotion.x = logicalMouse->mouseX;
		emit.data.mouseMotion.y = logicalMouse->mouseY;
		
		/* Debug. */
#if 0
		sjme_message("Mouse Motion: %08x (%d, %d)",
			emit.data.mouseMotion.buttonMask,
			emit.data.mouseMotion.x,
			emit.data.mouseMotion.y);
#endif
		
		/* Emit a motion event. */
		if (infoUser->callback != NULL)
			return infoUser->callback(inState, inComponent, &emit);
	}
	
	/* Nothing changed, we want to reduce duplication. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_baseInputListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_scritchui_listener_input* infoUser;
	sjme_scritchui_uiMouseState* currentMouse;
	sjme_scritchui_uiMouseState* logicalMouse;
	sjme_scritchinput_event clone;
	sjme_jint bit;
	sjme_jboolean isPressEvent;
	
	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get callback information. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(inComponent, input);
	
	/* Clone event data for normalization. */
	memmove(&clone, inEvent, sizeof(clone));
	
	/* We are only adjusting the current mouse state. */
	currentMouse = &inComponent->state.mouse[0];
	logicalMouse = &inComponent->state.mouse[1];
	
	/* Mouse events. */
	if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION ||
		clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED ||
		clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED)
	{
		/* Is this a press/release event? */
		isPressEvent = SJME_JNI_FALSE;
		if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED ||
			clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED)
			isPressEvent = SJME_JNI_TRUE;
		
		/* Pull from logical unless position is set as some */
		/* GUIs do not report position when buttons are pressed. This is */
		/* the case where mouse buttons are technically keyboard keys. */
		/* Motion always copies, however. */
		if (isPressEvent && clone.data.mouseButton.x == 0)
			currentMouse->mouseX = logicalMouse->mouseX;
		else
			currentMouse->mouseX = clone.data.mouseButton.x;
			
		if (isPressEvent && clone.data.mouseButton.y == 0)
			currentMouse->mouseY = logicalMouse->mouseY;
		else
			currentMouse->mouseY = clone.data.mouseButton.y;
			
		/* Set modifiers, if unsupported for this event type then pull */
		/* from the last logical modifier state. */
		if (clone.data.mouseMotion.modifiers ==
			SJME_SCRITCHINPUT_MODIFIER_UNSUPPORTED)
			currentMouse->mouseModifiers = logicalMouse->mouseModifiers;
		else
			currentMouse->mouseModifiers = clone.data.mouseMotion.modifiers;
		
		/* If no buttons are down, pull from logical. Otherwise, use the */
		/* mask from the event. Some GUIs will not pass buttons during */
		/* motion events, but will for normal press/release. */
		if (clone.data.mouseMotion.buttonMask == 0)
			currentMouse->mouseButtons = logicalMouse->mouseButtons;
		else
			currentMouse->mouseButtons = clone.data.mouseMotion.buttonMask;
			
		/* Either set or clear the mouse button bit, if known. */
		if (isPressEvent && clone.data.mouseButton.button != 0)
		{
			bit = (1 << (clone.data.mouseButton.button - 1)); 
			if (clone.type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED)
				currentMouse->mouseButtons |= bit;
			else
				currentMouse->mouseButtons &= ~bit;
		}
		
		/* Forward to handler to further clean up. */
		return sjme_scritchui_baseInputListenerMouse(inState, inComponent,
			&clone);
	}
	
	/* Normalize key. */
	else if (clone.type == SJME_SCRITCHINPUT_TYPE_KEY_PRESSED ||
		clone.type == SJME_SCRITCHINPUT_TYPE_KEY_RELEASED)
	{
		/* If any lowercase keys were passed, make them capitalized. */
		bit = clone.data.key.code;
		if (bit >= 'a' && bit <= 'z')
			clone.data.key.code = 'A' + (bit - 'a');
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
static sjme_thread_result sjme_attrThreadCall
	sjme_scritchui_core_componentRepaintBelay(
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
	sjme_errorCode error;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->componentFocusGrab == NULL)
		return sjme_error_notImplemented(0);
	
	/* For Windows, keyboard input happens on the window itself and not */
	/* the component, so we need to refer back. */
	if (sjme_error_is(error = inState->intern->bindFocus(inState,
		inComponent, inComponent, SJME_JNI_TRUE)))
		return sjme_error_default(error);
	
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
	sjme_scritchui_uiContainer container;
	sjme_scritchui_uiComponent* subComponents;
	sjme_jint i, n;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Not implemented? */
	if (inState->impl->componentRepaint == NULL)
		return sjme_error_notImplemented(0);
		
	/* Only certain types are paintable, ignore if requested. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
	{
		/* An actual error? */
		if (error != SJME_ERROR_INVALID_ARGUMENT)
			return sjme_error_default(error);

#if 1
		/* If this is a container, repaint all children. */
		container = NULL;
		if (sjme_error_is(error = inState->intern->getContainer(inState,
			inComponent, &container)) || container == NULL)
			return sjme_error_default(error);
		
		/* Do not bother? */
		if (container->components == NULL ||
			container->components->length == 0)
			return SJME_ERROR_NONE;
		
		/* Allocate storage for components. */
		n = container->components->length;
		subComponents = sjme_alloca(sizeof(*subComponents) * n);
		if (subComponents == NULL)
			return sjme_error_outOfMemory(NULL, n);
		
		/* Get all components. */
		memmove(subComponents, container->components->elements,
			sizeof(*subComponents) * n);
		
		/* Go through each and request repainting. */
		for (i = 0; i < n; i++)
			if (subComponents[i] != NULL)
				if (sjme_error_is(error = inState->apiInThread
					->componentRepaint(inState, subComponents[i],
					x, y, width, height)))
				{
					if (error != SJME_ERROR_INVALID_ARGUMENT)
						return sjme_error_default(error);
				}
#endif
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
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
