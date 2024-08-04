/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/util.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchpen_corePrim_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	sjme_scritchui_line* clipLine;
	sjme_jint ex;
	sjme_jint* rgbScan;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (w < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine end coordinate. */
	ex = x + w;
	
	/* Out of bounds? */
	clipLine = &g->state.clipLine;
	if (x >= g->width || x >= clipLine->e.x ||
		y < clipLine->s.y || y >= clipLine->e.y)
		return SJME_ERROR_NONE;
	
	/* Clip into bounds. */
	if (x < clipLine->s.x)
		x = clipLine->s.x;
	if (x < 0)
		x = 0;
	
	/* Clip end into bounds. */
	if (ex > clipLine->e.x)
		ex = clipLine->e.x;
	if (ex > g->width)
		ex = g->width;
	
	/* Outside the clip? */
	w = ex - x;
	if (w <= 0 || y < 0 || y > g->height ||
		y < clipLine->s.y || y > clipLine->e.y)
		return SJME_ERROR_NONE;
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawHorizSrc != NULL)
		return g->impl->drawHorizSrc(g, x, y, w);
	else if (g->state.applyAlpha && g->impl->drawHorizSrcOver != NULL)
		return g->impl->drawHorizSrcOver(g, x, y, w);
	
	/* Allocate buffer. */
	rgbScan = sjme_alloca(sizeof(*rgbScan) * w);
	if (rgbScan == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Fill RGB buffer. */
	memset(rgbScan, 0, sizeof(*rgbScan) * w);
	if (sjme_error_is(error = g->util->rgbScanFill(g,
		rgbScan, 0, w,
		g->state.color.argb)))
		return sjme_error_default(error);
	
	/* Put onto image, we do not multiply alpha as the alpha in source */
	/* is already the correct alpha value and this is not an image. */
	return g->util->rgbScanPut(g, x, y,
		rgbScan, w,
		g->state.applyAlpha,
		SJME_JNI_FALSE, 0);
}

sjme_errorCode sjme_scritchpen_corePrim_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawLineSrc != NULL)
		return g->impl->drawLineSrc(g, x1, y1, x2, y2);
	else if (g->state.applyAlpha && g->impl->drawLineSrcOver != NULL)
		return g->impl->drawLineSrcOver(g, x1, y1, x2, y2);
	
	return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchpen_corePrim_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawPixelSrc != NULL)
		return g->impl->drawPixelSrc(g, x, y);
	else if (g->state.applyAlpha && g->impl->drawPixelSrcOver != NULL)
		return g->impl->drawPixelSrcOver(g, x, y);
	
	/* Use horizontal line drawing. */
	return g->prim.drawHoriz(g, x, y, 1);
}

sjme_errorCode sjme_scritchpen_core_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawHoriz(g, x, y, w)))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x1, &y1);
	sjme_scritchpen_coreUtil_applyTranslate(g, &x2, &y2);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawLine(g, x1, y1, x2, y2)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawPixel(g, x, y)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}
