/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

sjme_errorCode sjme_scritchui_core_intern_getView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_scritchui_uiView* outView)
{
	sjme_scritchui_uiView view;
	
	if (inState == NULL || inComponent == NULL || outView == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only certain types can be a view. */
	switch (inComponent->common.type)
	{
		case SJME_SCRITCHUI_TYPE_SCROLL_PANEL:
			view = &((sjme_scritchui_uiScrollPanel)inComponent)->view;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	*outView = view;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_viewSuggest(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_dim* suggestDim)
{
	sjme_errorCode error;
	sjme_scritchui_uiComponent parent;
	sjme_scritchui_uiView view;
	sjme_scritchui_listener_sizeSuggest* infoUser;
	
	if (inState == NULL || inComponent == NULL || suggestDim == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get the parent of the component. */
	parent = NULL;
	if (sjme_error_is(error = inState->api->componentGetParent(inState,
		inComponent, &parent)))
		return sjme_error_default(error);
	
	/* If there is no parent, then just ignore. */
	if (parent == NULL)
		return SJME_ERROR_NONE;
		
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		parent, &view)) || view == NULL)
	{
		/* It is not really an error here, just means it is not one. */
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			return SJME_ERROR_NONE;
			
		/* Some other error? Not good! */
		return sjme_error_default(error);
	}

	/* Call suggestion function. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(view, sizeSuggest);
	if (infoUser->callback != NULL)
		return infoUser->callback(inState, parent,
			inComponent, suggestDim);
	
	/* There was nothing to suggest to. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect)
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	
	if (inState == NULL || inComponent == NULL || outViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea)
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	
	if (inState == NULL || inComponent == NULL || inViewArea == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->viewSetArea == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	/* Forward call. */
	if (sjme_error_is(error = inState->impl->viewSetArea(inState,
		inComponent, inViewArea)))
		return sjme_error_default(error);
	
	/* Revalidate after setting the area. */
	return inState->apiInThread->componentRevalidate(inState, inComponent);
}

sjme_errorCode sjme_scritchui_core_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_rect* inViewRect)
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	
	if (inState == NULL || inComponent == NULL || inViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_viewSetSizeSuggestListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest))
{
	sjme_errorCode error;
	sjme_scritchui_uiContainer container;
	sjme_scritchui_uiView view;
	sjme_scritchui_uiComponent firstSub;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	/* Use simple listener set. */
	if (sjme_error_is(error = inState->intern->setSimpleListener(
		inState,
		(sjme_scritchui_listener_void*)&SJME_SCRITCHUI_LISTENER_USER(
			view, sizeSuggest),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd)))
		return sjme_error_default(error);
	
	/* If this is a container, we should have a size be suggested. */
	container = NULL;
	if (sjme_error_is(error = inState->intern->getContainer(inState,
		inComponent, &container) || container == NULL))
	{
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			return SJME_ERROR_NONE;
		
		return sjme_error_default(error);
	}
	
	/* If there is a component here, just suggest on the first one. */
	if (container->components != NULL && container->components->length >= 1)
	{
		firstSub = container->components->elements[0];
		if (firstSub->bounds.d.width > 0 && firstSub->bounds.d.height)
			if (sjme_error_is(error = inState->intern->viewSuggest(inState,
				firstSub, &firstSub->bounds.d)))
				return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_viewSetViewListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(view))
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
