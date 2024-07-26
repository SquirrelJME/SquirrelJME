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

sjme_errorCode sjme_scritchui_fb_intern_render(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_scritchui_fb_displayList* dlFull,
	sjme_attrInPositive sjme_jint dlCount,
	sjme_attrInNullable const sjme_scritchui_fb_displayShaders* shaders,
	sjme_attrInNullable sjme_pointer shaderData)
{
	sjme_errorCode error;
	sjme_jint dlIndex;
	const sjme_scritchui_fb_displayList* dlAt;
	sjme_jint bsx, bsy, bex, bey, bw, bh;
	sjme_charSeq seq;
	sjme_jint seqLen;
	
	if (inState == NULL || g == NULL || dlFull == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (shaderData != NULL && shaders == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (dlCount < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
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
		
		/* Set clip bound for the item. */
		g->api->setClip(g, bsx, bsy, bw, bh);
		
		/* Translate to base coordinates, remove old translation. */
		g->api->translate(g, -g->state.translate.x, -g->state.translate.y);
		g->api->translate(g, bsx, bsy);
		
		/* Which type is being drawn? */
		switch (dlAt->type)
		{
				/* Normal box. */
			case SJME_SCRITCHUI_FB_DL_TYPE_BOX:
				g->api->drawRect(g, 0, 0, bw - 1, bh - 1);
				break;
				
				/* Text item. */
			case SJME_SCRITCHUI_FB_DL_TYPE_TEXT:
				if (dlAt->data.text.string != NULL)
				{
					/* Load in string. */
					memset(&seq, 0, sizeof(seq));
					if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
						&seq, dlAt->data.text.string)))
						return sjme_error_default(error);
					
					/* Set font to use. */
					g->api->setFont(g, dlAt->data.text.font);
					
					/* Determine how long the string is. */
					seqLen = 0;
					if (sjme_error_is(error = sjme_charSeq_length(
						&seq, &seqLen)))
						return sjme_error_default(error);
						
					/* Draw string. */
					g->api->drawSubstring(g, &seq, 0, seqLen,
						0, 0, 0);
				}
				break;
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	return inState->implIntern->render(inState, g, dlFull, dlCount, shaders,
		shaderData);
}
