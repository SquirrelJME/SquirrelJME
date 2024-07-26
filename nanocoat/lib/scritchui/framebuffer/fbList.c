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
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/framebuffer/fbIntern.h"

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
	sjme_scritchui_uiChoiceItem choiceItem; 
	sjme_scritchui_fb_displayList* dlFull;
	sjme_scritchui_fb_displayList* dlAt;
	sjme_scritchui_pencilFont noFont, useFont;
	sjme_jint dlCount, i, n, fontHeight, x, y, cW, cH;
	
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
		
	/* Get font to use if there is no change in it. */
	noFont = NULL;
	if (sjme_error_is(error = inState->api->fontBuiltin(inState,
		&noFont)) || noFont == NULL)
		return sjme_error_default(error);
	
	/* Determine display list size. */
	n = choice->numItems;
	dlCount = 1 + (n * 2);
	
	/* Allocate display list. */
	dlFull = sjme_alloca(sizeof(*dlFull) * dlCount);
	if (dlFull == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(dlFull, 0, sizeof(*dlFull) * dlCount);
	
	/* Get component actual size. */
	cW = 0;
	cH = 0;
	if (sjme_error_is(error = inState->api->componentSize(inState,
		inComponent, &cW, &cH)))
		return sjme_error_default(error);
	
	/* Draw each list entry. */
	dlAt = &dlFull[0];
	for (i = 0, x = 0, y = 0; i < n; i++)
	{
		/* Item used. */
		choiceItem = choice->items->elements[i];
		
		/* Use which font? */
		if (choiceItem->font != NULL)
			useFont = choiceItem->font;
		else
			useFont = noFont;
		
		/* How tall is this font? Used to determine bounds. */
		fontHeight = 0;
		if (sjme_error_is(error = useFont->api->metricPixelSize(
			useFont, &fontHeight)))
			return sjme_error_default(error);
		
		/* Box this is drawn on top of. */
		dlAt->type = SJME_SCRITCHUI_FB_DL_TYPE_BOX;
		dlAt->bound.s.x = x;
		dlAt->bound.s.y = y;
		dlAt->bound.d.width = cW;
		dlAt->bound.d.height = fontHeight;
		
		/* Selection box for index. */
		dlAt->selection = i + 1;
		
		/* Is this selected? */
		if (choiceItem->isSelected)
			dlAt->mod |= SJME_SCRITCHUI_FB_DL_TYPE_MOD_SELECTED;
		
		/* Next display list. */
		dlAt++;
		
		/* This is the text content. */
		dlAt->type = SJME_SCRITCHUI_FB_DL_TYPE_TEXT;
		dlAt->bound.s.x = x;
		dlAt->bound.s.y = y;
		dlAt->bound.d.width = cW;
		dlAt->bound.d.height = fontHeight;
		dlAt->data.text.font = useFont;
		dlAt->data.text.string = choiceItem->string;
		
		/* Is this selected? */
		if (choiceItem->isSelected)
			dlAt->mod |= SJME_SCRITCHUI_FB_DL_TYPE_MOD_SELECTED;
		
		/* Is this disabled? */
		if (!choiceItem->isEnabled)
			dlAt->mod |= SJME_SCRITCHUI_FB_DL_TYPE_MOD_DISABLED;
		
		/* Next display list. */
		dlAt++;
		
		/* Move Y up to next line. */
		y += fontHeight;
	}
	
	/* Render display list in the component window. */
	return inState->implIntern->renderInScroll(inState,
		inComponent, g, dlFull, dlCount, NULL, NULL);
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
	sjme_scritchui_fb_widgetState* wState;
	
	if (inState == NULL || inList == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Widget state for interactions. */
	wState = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*wState), &wState)) || wState == NULL)
		return sjme_error_default(error);
	
	/* Store in state. */
	inList->component.common.handle[SJME_SUI_FB_H_WSTATE] = wState;
	
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
