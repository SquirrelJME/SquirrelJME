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
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

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
	sjme_scritchui_rect rect;
	sjme_scritchui_dim realSuggest;
	
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

	/* Get the view rectangle. */
	memset(&rect, 0, sizeof(rect));
	if (sjme_error_is(error = inState->apiInThread->viewGetView(
		inState, parent, &rect)))
		return sjme_error_default(error);
	
	/* If the view rectangle is larger than the suggestion, increase it. */
	memmove(&realSuggest, suggestDim, sizeof(realSuggest));
	if (rect.d.width > realSuggest.width)
		realSuggest.width = rect.d.width;
	if (rect.d.height > realSuggest.height)
		realSuggest.height = rect.d.height;
	
	/* Call suggestion function. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(view, sizeSuggest);
	if (infoUser->callback != NULL)
		return infoUser->callback(inState, parent,
			inComponent, &realSuggest);
	
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
	sjme_scritchui_rect viewRect;
	
	if (inState == NULL || inComponent == NULL || outViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->viewGetView == NULL)
		return sjme_error_notImplemented(0);
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	/* Forward call. */
	memset(&viewRect, 0, sizeof(viewRect));
	if (sjme_error_is(error = inState->impl->viewGetView(inState,
		inComponent, &viewRect)))
		return sjme_error_default(error);
	
	/* Remember the current view area. */
	memmove(&view->view, &viewRect, sizeof(viewRect));
	
	/* Success! */
	memmove(outViewRect, &viewRect, sizeof(*outViewRect));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea)
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	sjme_scritchui_dim fullArea, oldArea, oldPageSize;
	sjme_scritchui_rect viewRect;
	sjme_scritchui_dim* pageSize;
	
	if (inState == NULL || inComponent == NULL || inViewArea == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inState->impl->viewSetArea == NULL)
		return sjme_error_notImplemented(0);
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	/* Get the current view rectangle. */
	memset(&viewRect, 0, sizeof(viewRect));
	if (sjme_error_is(error = inState->apiInThread->viewGetView(
		inState, inComponent, &viewRect)))
		return sjme_error_default(error);
	
	/* If the viewing area is larger than the suggested area, grow. */
	memmove(&fullArea, inViewArea, sizeof(fullArea));
	if (viewRect.d.width > fullArea.width)
		fullArea.width = viewRect.d.width;
	if (viewRect.d.height > fullArea.height)
		fullArea.height = viewRect.d.height;
	
	/* Get previous view area to check if it changed. */
	memmove(&oldArea, &view->area, sizeof(oldArea));
	memmove(&oldPageSize, pageSize, sizeof(oldPageSize));
	
	/* Cache the new view area. */
	memmove(&view->area, &fullArea, sizeof(view->area));
	
	/* Update the page size. */
	pageSize = &view->pageSize;
	pageSize->width = viewRect.d.width;
	pageSize->height = viewRect.d.height;
	
	/* If the viewport has not changed, do nothing. */
	if (memcmp(&oldArea, &fullArea, sizeof(oldArea)) == 0 &&
		memcmp(&oldPageSize, pageSize, sizeof(oldPageSize)) == 0)
		return SJME_ERROR_NONE;
	
	/* Forward call. */
	if (sjme_error_is(error = inState->impl->viewSetArea(inState,
		inComponent, &fullArea, pageSize)))
		return sjme_error_default(error);
	
	/* Revalidate and repaint after setting the area. */
	if (sjme_error_is(error = inState->apiInThread->componentRevalidate(
		inState, inComponent)))
		return sjme_error_default(error);
	return inState->apiInThread->componentRepaint(inState, inComponent,
		0, 0, INT32_MAX, INT32_MAX);
}

sjme_errorCode sjme_scritchui_core_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos)
{
	sjme_errorCode error;
	sjme_scritchui_uiView view;
	sjme_scritchui_rect rect;
	sjme_scritchui_rect oldRect;
	const sjme_scritchui_dim* area;
	sjme_jint sx, sy, ex, ey;
	
	if (inState == NULL || inComponent == NULL || inViewPos == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inState->impl->viewSetView == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	/* Get the view rectangle. */
	memset(&rect, 0, sizeof(rect));
	if (sjme_error_is(error = inState->apiInThread->viewGetView(
		inState, inComponent, &rect)))
		return sjme_error_default(error);
	
	/* Get the current viewing area. */
	area = &view->area;
	
	/* Calculate all coordinates. */
	sx = inViewPos->x;
	sy = inViewPos->y;
	ex = sx + rect.d.width;
	ey = sy + rect.d.height;
	
	/* Clip into bounds, make sure the entire page fits as well. */
	if (ex > area->width)
	{
		sx = area->width - rect.d.width;
		ex = area->width;
	}
	if (ey > area->height)
	{
		sy = area->height - rect.d.height;
		ey = area->height;
	}
	if (sx < 0)
		sx = 0;
	if (sy < 0)
		sy = 0;
	
	/* Remember old view rect before we change it. */
	memmove(&oldRect, &view->view, sizeof(oldRect));
	
	/* Set new viewing positions. */
	view->view.s.x = sx;
	view->view.s.y = sy;
	view->view.d.width = ex - sx;
	view->view.d.height = ey - sy;
	
	/* If the viewport has not changed, do nothing. */
	if (memcmp(&oldRect, &view->view, sizeof(oldRect)) == 0)
		return SJME_ERROR_NONE;
	
	/* Forward set of new viewing area. */
	if (sjme_error_is(error = inState->impl->viewSetView(inState, inComponent,
		&view->view.s)))
		return sjme_error_default(error);
		
	/* Revalidate and repaint after setting this. */
	if (sjme_error_is(error = inState->apiInThread->componentRevalidate(
		inState, inComponent)))
		return sjme_error_default(error);
	return inState->apiInThread->componentRepaint(inState, inComponent,
		0, 0, INT32_MAX, INT32_MAX);
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
	return sjme_error_notImplemented(0);
}
