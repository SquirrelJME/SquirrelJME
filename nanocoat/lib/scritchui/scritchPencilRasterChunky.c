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

static void sjme_scritchpen_core_clipLeftTop(
	sjme_jint dim,
	sjme_jint clipAt,
	sjme_jint* zSrc,
	sjme_jint* zDest,
	sjme_jint* m)
{
	sjme_jint cut, min;
	
	if ((*zDest) < clipAt || (*zDest) < 0)
	{
		/* Get right-most coordinate. */
		min = (clipAt < 0 ? 0 : clipAt);
		
		/* Cut by this many pixels. */
		cut = min - (*zDest);
		if (cut < 0)
			cut = -cut;
		
		/* Shift and cut off any excess. */
		(*zSrc) += cut;
		(*zDest) += cut;
		(*m) -= cut;
	}
}


static void sjme_scritchpen_core_clipRightBottom(
	sjme_jint dim,
	sjme_jint clipAt,
	sjme_jint* zSrc,
	sjme_jint* zDest,
	sjme_jint* m)
{
	sjme_jint cut, max;
	sjme_jint shift;
	
	/* Where does this coordinate end? */
	shift = (*zDest) + (*m);
	
	/* Past the edge? */
	if (shift > clipAt || shift > dim)
	{
		/* Get the left most coordinate. */
		max = (clipAt < dim ? clipAt : dim);
		
		/* Cut by this many pixels. */
		cut = shift - max;
		if (cut < 0)
			cut = -cut;
		
		/* Shift and cut off any excess. */
		(*m) -= cut;
	}
}

sjme_errorCode sjme_scritchpen_core_copyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	return sjme_error_notImplemented(0);
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencilMatrix m;
	sjme_fixed wx, zy, wxBase, zyMajor;
	sjme_jint dx, dy, iwx, izy, at;
	sjme_jint* srcRgb;
	sjme_jint srcRgbBytes, srcAlphaMask;
	sjme_jboolean srcAlpha, mulAlpha;
	sjme_jint mulAlphaVal;
	sjme_scritchui_line* clipLine;
	
	if (g == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The source rectangle must always be in bounds. */
	if (xSrc < 0 || ySrc < 0 || wSrc <= 0 || hSrc <= 0 ||
		(xSrc + wSrc) > origImgWidth || (ySrc + hSrc) > origImgHeight ||
		(xSrc + wSrc) < 0 || (ySrc + hSrc) < 0 ||
		origImgWidth < 0 || origImgHeight < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (off < 0 || dataLen < 0 || (off + dataLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Drawing nothing? */
	if (wDest <= 0 || hDest <= 0)
		return SJME_ERROR_NONE;
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &xDest, &yDest);
	
	/* We are now doing the transforming and drawing ourselves. */
	/* Calculate transformation matrix. */
	memset(&m, 0, sizeof(m));
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyRotateScale(
		&m, trans, wSrc, hSrc, wDest, hDest)))
		return sjme_error_default(error);
	
	/* Anchor to target coordinates after scaling, because we need */
	/* to know what our target scale is. */
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyAnchor(
		anchor,
		xDest, yDest, m.tw, m.th, 0,
		&xDest, &yDest)))
		return sjme_error_default(error);
	
	/* Get clipping information. */
	clipLine = &g->state.clipLine;
	
	/* Clip left X and top Y. */
	sjme_scritchpen_core_clipLeftTop(g->width, clipLine->s.x,
		&xSrc, &xDest, &m.tw);
	sjme_scritchpen_core_clipLeftTop(g->height, clipLine->s.y,
		&ySrc, &yDest, &m.th);
	
	/* Clip right X and bottom Y. */
	sjme_scritchpen_core_clipRightBottom(g->width, clipLine->e.x,
		&xSrc, &xDest, &m.tw);
	sjme_scritchpen_core_clipRightBottom(g->height, clipLine->e.y,
		&ySrc, &yDest, &m.th);
	
	/* Not actually drawing anything? */
	if (m.tw <= 0 || m.th <= 0)
		goto skip_noDraw;

	/* RGB buffer is this many bytes. */
	srcRgbBytes = m.tw * sizeof(*srcRgb);
	
	/* Setup input and output RGB buffers. */
	srcRgb = sjme_alloca(srcRgbBytes);
	if (srcRgb == NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_OUT_OF_MEMORY);
	
	/* Clear buffers. */
	memset(srcRgb, 0, srcRgbBytes);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Do we draw with alpha? */
	srcAlpha = alpha;
	mulAlpha = g->hasAlpha || alpha;
	
	/* The value to use to multiply the source. */
	mulAlphaVal = g->state.color.a;
	
	/* Does the source have a valid alpha value? */
	srcAlphaMask = (alpha ? 0 : 0xFF000000);
	
	/* Figure out the position of our base pointer. */
	/* Matrix multiplication? Squeak? */
	wxBase = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.x.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.x.zy);
	zyMajor = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.y.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.y.zy);
	
	/* Scan copy, rotate, and stretch by destination scans. */
	for (dy = 0; dy < m.th; dy++, wxBase += m.y.wx, zyMajor += m.y.zy)
	{
		/* Reset wx to base for start of scan. */
		wx = wxBase;
		zy = zyMajor;
		
		/* Scan in RGB line. */
		for (dx = 0; dx < m.tw; dx++, wx += m.x.wx, zy += m.x.zy)
		{
			/* Get pixel from source buffer. */
			iwx = sjme_fixed_int(sjme_fixed_round(wx)) % wSrc;
			izy = sjme_fixed_int(sjme_fixed_round(zy)) % hSrc;
			
			/* Keep in bounds. */
			if (iwx < 0)
				iwx += (wSrc - 1);
			if (izy < 0)
				izy += (hSrc - 1);
			
			/* Copy pixel from source? */
			at = off + ((izy * scanLen) + iwx);
			srcRgb[dx] = data[at] | srcAlphaMask;
		}
		
		/* Render RGB to buffer, it only has alpha if the source data */
		/* has alpha data. If it does, we want to multiply it. */
		if (sjme_error_is(error = g->util->rgbScanPut(g,
			xDest, yDest + dy,
			srcRgb, m.tw,
			srcAlpha, mulAlpha, mulAlphaVal)))
			goto fail_any;
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
		
skip_noDraw:
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}
