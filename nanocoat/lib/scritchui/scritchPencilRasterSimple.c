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
	sjme_jint numBytes, ex;
	void* outRaw;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Debug. */
	sjme_message("primHoriz(%p, %d, %d, %d) in [(%d, %d), (%d, %d)]",
		g, x, y, w,
		g->state.clipLine.s.x, g->state.clipLine.s.y,
		g->state.clipLine.e.x, g->state.clipLine.e.y);
	
	/* Off top or bottom? */
	clipLine = &g->state.clipLine;
	if (y < 0 || y < clipLine->s.y ||
		y >= g->height || y >= clipLine->e.y)
		return SJME_ERROR_NONE;
	
	/* Normalize horizontal coordinates. */
	ex = x + w;
	
	if (x < 0)
		x = 0;
	if (ex >= g->width)
		ex = g->width;
	if (ex >= clipLine->e.x)
		ex = clipLine->e.x;
	
	w = ex - x;
	
	/* Not visible? */
	if (w <= 0 || ex < 0 || x >= g->width || x >= clipLine->e.x)
		return SJME_ERROR_NONE;
	
	/* Setup raw pixel buffer. */
	numBytes = g->bytesPerPixel * w;
	outRaw = sjme_alloca(numBytes);
	if (outRaw == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Map it. */
	memset(outRaw, 0, numBytes);
	if (sjme_error_is(error = g->prim.rawScanFill(g,
		outRaw, 0, numBytes,
		g->state.color.v, w)))
		return sjme_error_default(error);
	
	/* Render line. */
	return g->prim.rawScanPut(g, x, y,
		outRaw, numBytes, w,
		SJME_JNI_FALSE);
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
	
	return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchpen_corePrim_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
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
