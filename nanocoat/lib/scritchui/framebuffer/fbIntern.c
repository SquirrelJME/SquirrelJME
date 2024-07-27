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

static sjme_errorCode sjme_scritchui_fb_selLock(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just directly set the buffer. */
	g->lockState.base = g->lockState.source.wrapper;
	g->lockState.baseLimitBytes = (sjme_jint)(
		(sjme_intPointer)g->lockState.source.data);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fb_selLockRelease(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing. */
	return SJME_ERROR_NONE;
}

/** Selection buffer functions. */
static const sjme_scritchui_pencilLockFunctions sjme_scritchui_fb_selBufFuncs =
{
	.lock = sjme_scritchui_fb_selLock,
	.lockRelease = sjme_scritchui_fb_selLockRelease,
};

static sjme_errorCode sjme_scritchui_fb_eventInput(
	sjme_attrInNotNull sjme_scritchui wrappedState,
	sjme_attrInNotNull sjme_scritchui_uiComponent wrappedComponent,
	sjme_attrInNotNull const sjme_scritchinput_event* inEvent)
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_fb_widgetState* wState;
	sjme_jint pX, pY, selId;
	sjme_jboolean hasFocus;
	
	if (wrappedState == NULL || wrappedComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	inState = wrappedComponent->common.frontEnd.data;
	inComponent = wrappedComponent->common.frontEnd.wrapper;
	
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the widget state for this lightweight component. */
	/* If there is no state yet, then we never rendered this widget. */
	wState = inComponent->common.handle[SJME_SUI_FB_H_WSTATE];
	if (wState == NULL)
		return SJME_ERROR_NONE;
	
	/* Pointer event? */
	if (inEvent->type == SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED ||
		inEvent->type == SJME_SCRITCHINPUT_TYPE_TOUCH_FINGER_PRESSED ||
		inEvent->type == SJME_SCRITCHINPUT_TYPE_STYLUS_PEN_PRESSED)
	{
		/* Check if we have focus. */
		hasFocus = SJME_JNI_FALSE;
		if (sjme_error_is(error = inState->api->componentFocusHas(
			inState, inComponent, &hasFocus)))
			return sjme_error_default(error);
		
		/* If we do not have focus, grab it and do nothing else. */
		if (!hasFocus)
			return inState->api->componentFocusGrab(
				inState, inComponent);
		
		/* If there is no selection buffer, we cannot handle press events */
		/* as we have no idea what we even clicked on. */
		if (wState->selBuf == NULL)
			return SJME_ERROR_NONE;
		
		/* Recover pointer coordinates. */
		pX = inEvent->data.mouseButton.x;
		pY = inEvent->data.mouseButton.y;
		
		/* Outside the buffer edge? */
		if (pX < 0 || pY < 0 ||
			pX >= wState->selBufWidth || pY >= wState->selBufHeight)
			return SJME_ERROR_NONE;
		
		/* Read the selection id here. */
		selId = wState->selBuf[(pY * wState->selBufWidth) + pX] & 0xFFFFFF;
		
		/* Was there something here that was clicked? */
		if (selId != 0 && wState->lightClickListener != NULL)
			return wState->lightClickListener(inState, inComponent,
				selId, pX, pY);
		return SJME_ERROR_NONE;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_intern_lightweightInit(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_fb_widgetState** outWState,
	sjme_attrInValue sjme_jboolean isInteractive,
	sjme_attrInNotNull sjme_scritchui_paintListenerFunc paintListener)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiPanel wrappedPanel;
	sjme_scritchui_fb_widgetState* wState;
	
	if (inState == NULL || inComponent == NULL || paintListener == NULL ||
		outWState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Widget state for interactions. */
	wState = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*wState), &wState)) || wState == NULL)
		return sjme_error_default(error);
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Store in state. */
	inComponent->common.handle[SJME_SUI_FB_H_WSTATE] = wState;
	
	/* Setup wrapped panel to draw our widget on. */
	wrappedPanel = NULL;
	if (sjme_error_is(error = wrappedState->api->panelNew(
		wrappedState, &wrappedPanel)) ||
		wrappedPanel == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inComponent, wrappedPanel)))
		return sjme_error_default(error);
	
	/* If this is interactive, we need to handle inputs and otherwise. */
	if (isInteractive)
	{
		/* Enable focus on the widget. */
		if (sjme_error_is(error = wrappedState->api->panelEnableFocus(
			wrappedState, wrappedPanel,
			SJME_JNI_TRUE, SJME_JNI_FALSE)))
			return sjme_error_default(error);
			
		/* Set listener for events. */
		if (sjme_error_is(error =
			wrappedState->api->componentSetInputListener(
			wrappedState, wrappedPanel,
			sjme_scritchui_fb_eventInput, NULL)))
			return sjme_error_default(error);
	}
	
	/* Set renderer for widget. */
	if (sjme_error_is(error =
		wrappedState->api->componentSetPaintListener(
		wrappedState, wrappedPanel,
		paintListener, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outWState = wState;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_intern_render(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData)
{
	sjme_errorCode error;
	sjme_jint dlIndex;
	sjme_scritchui_fb_widgetState* wState;
	const sjme_scritchui_fb_displayList* dlAt;
	sjme_scritchui_pencil sg;
	sjme_frontEnd sgFrontEnd;
	sjme_jint bsx, bsy, bex, bey, bw, bh;
	sjme_jint cW, cH, cT;
	sjme_jint* tempSelBuf;
	sjme_charSeq seq;
	sjme_jint seqLen;
	sjme_jboolean doSel;
	sjme_jint i;
	sjme_jint lafColors[SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR];
	sjme_scritchui_lafElementColorType colorType;
	
	if (inState == NULL || g == NULL || dlFull == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (shaderData != NULL && shaders == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dlCount < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Recover widget state, if this has one for selection buffering. */
	cW = 1;
	cH = 1;
	wState = NULL;
	if (inComponent != NULL)
	{
		/* Recover the widget state for this lightweight component. */
		wState = inComponent->common.handle[SJME_SUI_FB_H_WSTATE];
	
		/* Read in component size. */
		if (sjme_error_is(error = inState->api->componentSize(
			inState, inComponent, &cW, &cH)))
			goto fail_componentSize;
	}
	
	/* Total component area. */
	cT = cW * cH;
	
	/* Does the selection buffer need initializing? */
	sg = NULL;
	if (wState != NULL)
	{
		/* Need to allocate new buffer? */
		if (wState->selBuf == NULL || wState->selBufLen < cT)
		{
			/* Delete old buffer. */
			if (wState->selBuf != NULL)
				if (sjme_error_is(error = sjme_alloc_free(
					wState->selBuf)))
					goto fail_freeSelBuf;
			
			/* Clear everything. */
			wState->selBuf = NULL;
			wState->selBufLen = 0;
			wState->selBufWidth = 0;
			wState->selBufHeight = 0;
			
			/* Attempt allocation of new buffer. */
			tempSelBuf = NULL;
			if (sjme_error_is(error = sjme_alloc(inState->pool,
				cT * sizeof(sjme_jint), &tempSelBuf)) || tempSelBuf == NULL)
				goto fail_allocSelBuf;
			
			/* Store for usage. */
			wState->selBuf = tempSelBuf;
			wState->selBufLen = cT;
			wState->selBufWidth = cW;
			wState->selBufHeight = cH;
		}
		
		/* Setup front end to point to the buffer. */
		memset(&sgFrontEnd, 0, sizeof(sgFrontEnd));
		sgFrontEnd.data = (sjme_pointer)
			((sjme_intPointer)cT * sizeof(sjme_jint));
		sgFrontEnd.wrapper = wState->selBuf;
		
		/* Wrap a pencil over the selection buffer. */
		if (sjme_error_is(inState->api->hardwareGraphics(inState,
			&sg, NULL,
			SJME_GFX_PIXEL_FORMAT_INT_RGB888,
			cW, cH,
			&sjme_scritchui_fb_selBufFuncs,
			&sgFrontEnd,
			0, 0, cW, cH,
			NULL)) || sg == NULL)
			goto fail_initSelPen;
	}
	
	/* Obtain all the look and feel colors. */
	memset(lafColors, 0, sizeof(lafColors));
	for (i = 0; i < SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR; i++)
		if (sjme_error_is(error = inState->api->lafElementColor(
			inState, inComponent,
			&lafColors[i], i)))
			goto fail_lafColor;
	
	/* Perform all drawing operations. */
	for (dlIndex = 0; dlIndex < dlCount; dlIndex++)
	{
		/* Get the item to draw. */
		dlAt = &dlFull[dlIndex];
		
		/* Normalize clip coordinates. */
		bsx = dlAt->bound.s.x;
		bsy = dlAt->bound.s.y;
		bw = dlAt->bound.d.width;
		bh = dlAt->bound.d.height;
		bex = bsx + bw;
		bey = bsy + bh;
		
		/* Remove old translation. */
		g->api->translate(g, -g->state.translate.x, -g->state.translate.y);
		
		/* Set clip bound for the item. */
		g->api->setClip(g, bsx, bsy, bw, bh);
		
		/* Translate to base coordinates. */
		g->api->translate(g, bsx, bsy);
		
		/* Set font to use? */
		if (dlAt->type == SJME_SCRITCHUI_FB_DL_TYPE_TEXT)
			g->api->setFont(g, dlAt->data.text.font);
		
		/* Determine base color. */
		colorType = 0;
		if (dlAt->color >= 0 &&
			dlAt->color < SJME_SCRITCHUI_NUM_LAF_ELEMENT_COLOR)
			colorType = dlAt->color;
		
		/* Adjust to highlight color? */
		if (dlAt->mod & SJME_SCRITCHUI_FB_DL_TYPE_MOD_SELECTED)
		{
			if (colorType == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BACKGROUND)
				colorType =
					SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BACKGROUND;
			else if (colorType == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_FOREGROUND)
				colorType =
					SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_FOREGROUND;
			else if (colorType == SJME_SCRITCHUI_LAF_ELEMENT_COLOR_BORDER)
				colorType =
					SJME_SCRITCHUI_LAF_ELEMENT_COLOR_HIGHLIGHTED_BORDER;
		}
		
		/* Set color. */
		g->api->setAlphaColor(g, lafColors[colorType]);
		
		/* Must handle drawing of the selection buffer */
		doSel = (dlAt->selection != 0);
		if (sg != NULL && doSel)
		{
			/* Copy all the translation and otherwise here. */
			if (sjme_error_is(error = sg->api->setParametersFrom(sg,
				g)))
				goto fail_sgCopyParam;
			
			/* The color is the selection index. */
			if (sjme_error_is(error = sg->api->setAlphaColor(sg,
				0xFF000000 | dlAt->selection)))
				goto fail_sgSelColor;
		}
		
		/* Which type is being drawn? */
		switch (dlAt->type)
		{
				/* Normal box. */
			case SJME_SCRITCHUI_FB_DL_TYPE_BOX:
				if (dlAt->mod & SJME_SCRITCHUI_FB_DL_TYPE_MOD_SELECTED)
					g->api->fillRect(g, 0, 0, bw - 1, bh - 1);
				else
					g->api->drawRect(g, 0, 0, bw - 1, bh - 1);
				
				/* Selection buffer, always filled here! */
				if (sg != NULL && doSel)
					sg->api->fillRect(sg, 0, 0, bw - 1, bh - 1);
				break;
				
				/* Text item. */
			case SJME_SCRITCHUI_FB_DL_TYPE_TEXT:
				if (dlAt->data.text.string != NULL)
				{
					/* Load in string. */
					memset(&seq, 0, sizeof(seq));
					if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
						&seq, dlAt->data.text.string)))
						goto fail_charSeqLoad;
					
					/* Determine how long the string is. */
					seqLen = 0;
					if (sjme_error_is(error = sjme_charSeq_length(
						&seq, &seqLen)))
						goto fail_charSeqLen;
						
					/* Draw string. */
					g->api->drawSubstring(g, &seq, 0, seqLen,
						0, 0, 0);
					
					/* Selection buffer. */
					if (sg != NULL && doSel)
						sg->api->drawSubstring(sg, &seq, 0, seqLen,
							0, 0, 0);
				}
				break;
		}
	}
	
	/* Free pen before leaving. */
	if (sg != NULL)
		sjme_alloc_free(sg);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_charSeqLen:
fail_charSeqLoad:
fail_sgSelColor:
fail_sgCopyParam:
fail_lafColor:
fail_initSelPen:
	/* Free pen before leaving. */
	if (sg != NULL)
		sjme_alloc_free(sg);
fail_allocSelBuf:
fail_freeSelBuf:
fail_componentSize:
	/* Debug. */
	sjme_message("FB Render Failed: %d", error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_fb_intern_renderInScroll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData)
{
	/* For now just ignore this and draw directly on. */
	return inState->implIntern->render(inState, inComponent, g,
		dlFull, dlCount, shaders, shaderData);
}
