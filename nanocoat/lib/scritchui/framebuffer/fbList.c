/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_fb_list_draw(
	sjme_attrInNotNull sjme_scritchui wrappedState,
	sjme_attrInNotNull sjme_scritchui_uiComponent wrappedComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInValue sjme_jint special)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiChoice choice;
	
	if (wrappedState == NULL || wrappedComponent == NULL || g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get owning state and component. */
	inState = wrappedComponent->common.frontEnd.data;
	inComponent = wrappedComponent->common.frontEnd.wrapper;
	
	/* Recover choice. */
	choice = NULL;
	if (sjme_error_is(error = inState->intern->getChoice(inState,
		inComponent, &choice)) || choice == NULL)
		return sjme_error_default(error);
	
	g->api->setAlphaColor(g, 0xFF000000);
	g->api->drawHoriz(g, 2, 3, 4);
	g->api->drawPixel(g, 4, 4);
	
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fb_list_input(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiList inList,
	sjme_attrInValue const sjme_scritchui_impl_initParamList* init)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiPanel wrappedPanel;
	
	if (inState == NULL || inList == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Setup wrapped panel to draw all of our list items on. */
	wrappedPanel = NULL;
	if (sjme_error_is(error = wrappedState->api->panelNew(
		wrappedState, &wrappedPanel)) ||
		wrappedPanel == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inList, wrappedPanel)))
		return sjme_error_default(error);
	
	/* Enable focus on the list. */
	if (sjme_error_is(error = wrappedState->api->panelEnableFocus(
		wrappedState, wrappedPanel,
		SJME_JNI_TRUE, SJME_JNI_FALSE)))
		return sjme_error_default(error);
	
	/* Set renderer for list. */
	if (sjme_error_is(error =
		wrappedState->api->componentSetPaintListener(
		wrappedState, wrappedPanel,
		sjme_scritchui_fb_list_draw, NULL)))
		return sjme_error_default(error);
	
	/* Set listener for events. */
	if (sjme_error_is(error =
		wrappedState->api->componentSetInputListener(
		wrappedState, wrappedPanel,
		sjme_scritchui_fb_list_input, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
