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
	
	/* Obtain view. */
	view = NULL;
	if (sjme_error_is(error = inState->intern->getView(inState,
		inComponent, &view)) || view == NULL)
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
