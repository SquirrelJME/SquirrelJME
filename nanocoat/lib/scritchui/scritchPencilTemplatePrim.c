/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/** Primitive function name. */
#define pencilPrim_NAME(func) \
	SJME_TOKEN_PASTE5_PP(sjme_scritchui_pencil_, func, _, \
		pencilPixelType, pencilPixelBits)

/** Name for horizontal draw. */
#define pencilPrimDrawHoriz_NAME \
	pencilPrim_NAME(DrawHoriz)

/** Name for draw line. */
#define pencilPrimDrawLine_NAME \
	pencilPrim_NAME(DrawLine)

/** Name for draw pixel. */
#define pencilPrimDrawPixel_NAME \
	pencilPrim_NAME(DrawPixel)

#include "scritchPencilTemplateCommon.c"

static sjme_inline sjme_errorCode pencilPrimDrawHoriz_NAME(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_jint sw, sh, v;
	sjme_jint ex;
	sjme_jint i;
	pencilPixelType* s;
	
	if (g == NULL)
		return SJME_ERROR_NONE;
	
	/* Always draw at least one pixel. */
	if (w <= 0)
		w = 1;
	
	/* Out of bounds vertically? */
	sh = g->height;
	if (y < 0 || y >= sh)
		return SJME_ERROR_NONE;
	
	/* Normalize end coordinate. */
	ex = x + w;
	
	/* Map to within bounds. */
	sw = g->width;
	if (x < 0)
		x = 0;
	if (ex > sw)
		ex = sw;
	
	/* Nothing to draw? */
	w = (ex - x);
	if (w <= 0)
		return SJME_ERROR_NONE;
	
	/* Draw pixels in. */
	v = g->state.color.v;
	for (i = 0; i < w; i++)
		;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_inline sjme_errorCode pencilPrimDrawLine_NAME(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	if (g == NULL)
		return SJME_ERROR_NONE;
		
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_inline sjme_errorCode pencilPrimDrawPixel_NAME(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	sjme_jint sw, sh, sl, p;
	
	if (g == NULL)
		return SJME_ERROR_NONE;
	
	/* Completely out of bounds? */
	sw = g->width;
	sh = g->height;
	sl = g->scanLen;
	if (x < 0 || y < 0 || x >= sw || y >= sh)
		return SJME_ERROR_NONE;
	
	/* Place single pixel. */
	p = g->state.color.v;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_scritchui_pencilPrimFunctions pencilPrim_NAME(_) =
{
	.drawHoriz = pencilPrimDrawHoriz_NAME,
	.drawLine = pencilPrimDrawLine_NAME,
	.drawPixel = pencilPrimDrawPixel_NAME,
};

/* Un-define for next run. */
#undef SJME_PENCIL_NAME
#undef SJME_PENCIL_PIXEL_FORMAT
#undef pencilPixelType
#undef pencilPixelBits
#undef pencilPrim_NAME
#undef pencilPrimDrawHoriz_NAME
#undef pencilPrimDrawLine_NAME
#undef pencilPrimDrawPixel_NAME
#undef pencilMove
#undef pencilIncr
#undef pencilPut
#undef pencilPixelMask
