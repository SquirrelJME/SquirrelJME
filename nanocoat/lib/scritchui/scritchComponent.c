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

sjme_errorCode sjme_scritchui_core_componentSetPaintListener(
	sjme_scritchui inState,
	sjme_scritchui_uiComponent inComponent,
	sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui_uiPaintable paint;
	sjme_frontEnd oldFrontEnd;
	sjme_scritchui_paintListenerFunc oldListener;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be painted on. */
	paint = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paint)) || paint == NULL)
		return sjme_error_default(error);
	
	/* Set new listener. */
	oldListener = paint->listener;
	paint->listener = inListener;
	
	/* Copy old front end in the event of an error. */
	memmove(&oldFrontEnd, &paint->frontEnd, sizeof(sjme_frontEnd));
	
	/* Replace with new front end data before the call. */
	if (copyFrontEnd != NULL)
		memmove(&paint->frontEnd, copyFrontEnd,
			sizeof(sjme_frontEnd));
	
	/* Inform component of updated listener. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (inState->impl->componentSetPaintListener == NULL ||
		sjme_error_is(error = inState->impl->componentSetPaintListener(
			inState, inComponent, inListener, paint, copyFrontEnd)))
	{
		/* Error, copy old value back. */
		paint->listener = oldListener;
		memmove(&paint->frontEnd, &oldFrontEnd,
			sizeof(sjme_frontEnd));
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_scritchui_uiContainer container;
	sjme_errorCode error;
	
	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot add a window to a container despite windows being components. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Only certain types are containers. */
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inComponent, &container)) || container == NULL)
		return sjme_error_default(error);
	
	/* Cannot add a component to multiple containers, they must be removed */
	/* manually for consistency purposes. */
	if (inComponent->parent != NULL)
		return SJME_ERROR_ALREADY_IN_CONTAINER;
	
	/* Forward call. */	
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (inState->impl->containerAdd == NULL ||
		sjme_error_is(error = inState->impl->containerAdd(inState,
		inContainer, container,
		inComponent)))
		return sjme_error_default(error);
	
	/* Update parent. */
	inComponent->parent = inContainer;
		
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_getContainer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiContainer* outContainer)
{
	sjme_scritchui_uiContainer container;
	
	if (inState == NULL || inComponent == NULL || outContainer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be a container. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_PANEL:
			container = &((sjme_scritchui_uiPanel)inComponent)->container;
			break;
		
		case SJME_SCRITCHUI_TYPE_WINDOW:
			container = &((sjme_scritchui_uiWindow)inComponent)->container;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outContainer = container;
	return SJME_ERROR_NONE;
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
