/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

static void sjme_scritchpen_core_clipLeftTop(
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
	sjme_jint scanlineBytes, copiedBytes;
	sjme_pointer copiedArea, areaP;
	sjme_jint y, zy;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* The source rectangle must always be in bounds. */
	if (sx < 0 || sy < 0 || sx + w < 0 || sy + h < 0 ||
		sx + w > g->width || sy + h > g->height)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* The destination area on the other hand, can extend beyond the context */
	/* bounds, we just don't copy whatever's outside. */
	if (dx < 0)
	{
		w += dx;
		dx = 0;
	}
	
	if (dy < 0)
	{
		h += dy;
		dy = 0;
	}

	/* Clip to within bounds. */
	if (dx + w > g->width)
		w = g->width - dx;
	if (dy + h > g->height)
		h = g->height - dy;

	/* Drawing nothing? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;

	/* Translate base coordinates. */
	if (sjme_error_is(error = g->util->applyTranslate(g, &sx, &sy)))
		return sjme_error_default(error);
	if (sjme_error_is(error = g->util->applyTranslate(g, &dx, &dy)))
		return sjme_error_default(error);

	/* Apply anchor to the destination area. */
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyAnchor(anchor,
		dx, dy, w, h, 0, &dx, &dy)))
		return sjme_error_default(error);

	/* Make sure the scanline does not overflow. */
	scanlineBytes = -1;
	if (sjme_error_is(error = g->util->pfScanBytes(g, g->pixelFormat,
		w, -1, &scanlineBytes, NULL)) ||
		scanlineBytes < 0)
		goto fail_scanBytes;

	/* And additionally ensure the entire temporary buffer does not overflow */
	/* as well. */
	copiedBytes = scanlineBytes * h;
	if (copiedBytes < 0)
	{
		error = SJME_ERROR_SCAN_OUT_OF_BOUNDS;
		goto fail_scanOverflow;
	}

	/* Allocate temporary buffer as the copy must be an "atomic" operation. */
	copiedArea = sjme_alloca(copiedBytes);
	if (copiedArea == NULL)
	{
		error = sjme_error_outOfMemory(NULL, copiedBytes);
		goto fail_alloca;
	}

	/* Clear. */
	memset(copiedArea, 0, copiedBytes);
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	/* A neat property here is that we don't need to worry about format */
	/* conversion, as the data is being copied between regions of the same */
	/* image. We can get away with just figuring out the native image */
	/* format, and then go straight to copying from there. */
	error = SJME_ERROR_NONE;
	for (y = 0, zy = sy, areaP = copiedArea; y < h;
		y++, zy++, areaP = SJME_POINTER_OFFSET(areaP, scanlineBytes))
		error |= g->prim.rawScanGet(g, sx, zy,
			areaP, scanlineBytes, w);
	
	/* Now start moving to the destination in order to copy into it. */
	
	/* Copy the source region into the destination. Note that copyArea is */
	/* only used for mutable images, which only contain opaque pixels as per */
	/* MIDP 2. Thus, no alpha handling is needed. */
	/* This is also the case for MIDP 3, no alpha blending is performed. */
	for (y = 0, zy = dy, areaP = copiedArea; y < h;
		y++, zy++, areaP = SJME_POINTER_OFFSET(areaP, scanlineBytes))
		error |= g->prim.rawScanPutPure(g, dx, zy,
			areaP, scanlineBytes, w);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_readWrite;

	/* Cleanup. */
	sjme_alloca_free(copiedArea);

	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_readWrite:
	/* Unlock before fail */
	sjme_scritchpen_core_lockRelease(g);
	
fail_alloca:
	if (copiedArea != NULL)
		sjme_alloca_free(copiedArea);
	
fail_scanBytes:
fail_scanOverflow:
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

	/* The scanline length needs to be at least the image width. */
	if (scanLen < 0 || origImgWidth > scanLen || wSrc > scanLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (off < 0 || dataLen < 0 || (off + dataLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Drawing nothing? */
	if (wDest <= 0 || hDest <= 0)
		return SJME_ERROR_NONE;
	
	/* Translate base coordinates. */
	if (sjme_error_is(error = g->util->applyTranslate(g,
		&xDest, &yDest)))
		return sjme_error_default(error);

	/* We are now doing the transforming and drawing ourselves. */
	/* Calculate transformation matrix. */
	memset(&m, 0, sizeof(m));
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyRotateScale(
		&m, trans, wSrc, hSrc, wDest, hDest)))
		return sjme_error_default(error);
	
	/* Now we need to adjust the source and destination areas to account for */
	/* the transformed image, otherwise we'll read from an incorrect area, */
	/* and draw to another wrong area. */
	if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyCoordinateAdj(
		trans, &xSrc, &ySrc, &wSrc, &hSrc, scanLen,
		(dataLen / scanLen))))
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
	sjme_scritchpen_core_clipLeftTop(clipLine->s.x, &xSrc, &xDest, &m.tw);
	sjme_scritchpen_core_clipLeftTop(clipLine->s.y, &ySrc, &yDest, &m.th);
	
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
	{
		error = sjme_error_outOfMemory(NULL, srcRgbBytes);
		goto fail_alloca;
	}
	
	/* Clear buffers. */
	memset(srcRgb, 0, srcRgbBytes);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		goto fail_lock;
	
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
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.y.wx);
	zyMajor = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.x.zy) +
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
			iwx = sjme_fixed_int(wx) % origImgWidth;
			iwx = ((iwx % origImgWidth) + origImgWidth) % origImgWidth;
			
			izy = sjme_fixed_int(zy) % origImgHeight;
			izy = ((izy % origImgHeight) + origImgHeight) % origImgHeight;
			
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
			goto fail_anyInLock;
	}

	/* Cleanup. */
	sjme_alloca_free(srcRgb);
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_unlock;
		
skip_noDraw:
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_anyInLock:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);

fail_unlock:
fail_lock:
fail_alloca:
	if (srcRgb != NULL)
		sjme_alloca_free(srcRgb);
	return sjme_error_default(error);
}
