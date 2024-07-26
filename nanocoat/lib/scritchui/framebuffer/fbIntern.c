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
