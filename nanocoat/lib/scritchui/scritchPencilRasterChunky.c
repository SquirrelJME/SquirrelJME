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

sjme_errorCode sjme_attrDeprecated sjme_scritchpen_core_copyArea(
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
	sjme_scritchui_matrix m;
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

sjme_errorCode sjme_scritchpen_core_drawRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
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
	sjme_scritchui_matrix m;
	sjme_fixed wx, zy, wxBase, zyMajor;
	sjme_jint dx, dy, iwx, izy, at, srcBytes, mulAlphaVal, byteSize;
	sjme_pointer srcData;
	sjme_jboolean srcAlpha, mulAlpha;
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

	byteSize = -1;
	if (sjme_error_is(error = g->util->pfScanBytes(g, pf,
		1, -1, &byteSize, NULL)) ||
		byteSize < 0)
		return sjme_error_default(error);

	/* Translate base coordinates. */
	if (sjme_error_is(error = g->util->applyTranslate(g,
		&xDest, &yDest)))
		return sjme_error_default(error);

	/* We are now doing the transforming and drawing ourselves. */
	memset(&m, 0, sizeof(m));

	/* Special value indicating INDEXED1, but with VERTICAL disposition */
	if (pf == SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1_VERTICAL)
	{
		/* We can basically rotate 270 degrees clockwise to be able to treat */
		/* it as INDEXED1 data, we can then apply the received transform */
		if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyRotateScale(
			&m, SJME_SCRITCHUI_TRANS_ROT270, wSrc, hSrc, wDest, hDest)))
			return sjme_error_default(error);

		/* Then we just change the format into normal INDEXED1 */
		pf = SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1;
	}

	/* Calculate transformation matrix. */
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
		return SJME_ERROR_NONE;

	/* A scanline here is just the width multiplied by the byteSize */
	srcBytes = m.tw * byteSize;

	/* Setup input and output RGB buffers. */
	srcData = sjme_alloca(srcBytes);
	if (srcData == NULL)
	{
		error = sjme_error_outOfMemory(NULL, srcBytes);
		goto fail_alloca;
	}

	/* Clear buffers. */
	memset(srcData, 0, srcBytes);

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		goto fail_lock;

	/* Do we draw with alpha? */
	srcAlpha = alpha;
	mulAlpha = g->hasAlpha || alpha;

	/* The value to use to multiply the source. */
	mulAlphaVal = g->state.color.a;

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


			if (sjme_error_is(error = g->util->pfScanToPf(g, pf, (void*)srcData,
					dx*byteSize, srcBytes, pf, (void*)data, at*byteSize,
					dataLen*byteSize, 1)))
					goto fail_scanBytes;
		}

		/* Render data to buffer, it only has alpha if the source also does */
		if (sjme_error_is(error = g->util->pfScanPut(g, pf, xDest, yDest + dy,
			srcData, m.tw, mulAlpha, mulAlphaVal)))
			goto fail_anyInLock;
	}

	/* Cleanup. */
	sjme_alloca_free(srcData);

	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_unlock;

	/* Success! */
	return SJME_ERROR_NONE;

fail_anyInLock:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);

fail_applyMask:
fail_unlock:
fail_lock:
fail_alloca:
fail_scanBytes:
	if (srcData != NULL)
		sjme_alloca_free(srcData);

	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_getRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	sjme_jint* srcRGB;
	sjme_jint* dataScanline;
	sjme_jint blendBytes, mulAlphaVal, byteSize, y;
	sjme_gfx_pixelFormat blendPf;
	sjme_jboolean mulAlpha, pfAlpha;

	if (g == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || dataLen < 0 || scanLen < 0 ||
		(off + dataLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* The scanline length needs to be at least the image width. */
	if (scanLen < 0 || wSrc > scanLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* The source needs to be in the image bounds. */
	if (xSrc < 0 || ySrc < 0 || wSrc < 0 || hSrc < 0 ||
		(xSrc + wSrc) < 0 || (ySrc + hSrc) < 0 ||
		(xSrc + wSrc) > g->width || (ySrc + hSrc) > g->height)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Reading nothing? */
	if (wSrc <= 0 || hSrc <= 0)
		return SJME_ERROR_NONE;
	
	/* This is a special BYTE_1_GRAY_VERTICAL format not yet supported. */
	/* Has to be rotated 270 degrees clockwise. */
	if (pf == SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1_VERTICAL)
		return sjme_error_notImplemented(0);
	
	/* TODO: Create static sjme_scritchui_pencil and then use */
	/* TODO: @code g->apiInThread->transferRegion() @endcode . */

	/* May be dynamically allocated for blending. */
	srcRGB = NULL;
	dataScanline = NULL;

	/* Do we draw with alpha? */
	mulAlpha = g->hasAlpha || alpha;
	pfAlpha = sjme_scritchpen_hasAlpha(pf);
	blendPf = ((alpha && mulAlpha) ? SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 :
		SJME_GFX_PIXEL_FORMAT_INT_RGB888);

	/* The value to use to multiply the source. */
	mulAlphaVal = g->state.color.a;

	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	/* How large is a scan? */
	byteSize = -1;
	if (sjme_error_is(error = g->util->pfScanBytes(g, pf,
		1, -1, &byteSize, NULL)) ||
		byteSize < 0)
		goto fail_oob;

	/* If alpha is specified, we need to blend pixels from the screen into */
	/* the received 'data' array. The temporary blendPf is always (A)RGB. */
	/* Since that allows usage of blendRGBInto(). */
	if (alpha && pfAlpha)
	{
		blendBytes = -1;
		if (sjme_error_is(error = g->util->pfScanBytes(g, blendPf,
			wSrc, -1, &blendBytes, NULL)) ||
			blendBytes < 0)
			goto fail_oob;

		srcRGB = sjme_alloca(blendBytes);
		dataScanline = sjme_alloca(blendBytes);
		if (srcRGB == NULL || dataScanline == NULL)
			goto fail_any;

		/* Need to clear both. */
		memset(srcRGB, 0, blendBytes);
		memset(dataScanline, 0, blendBytes);
	}

	error = SJME_ERROR_NONE;
	for (y = 0; y < hSrc; y++)
	{
		if (alpha && pfAlpha)
		{
			/* Get screen data as RGB */
			if (sjme_error_is(error = g->util->rgbScanGet(g,
				xSrc, ySrc + y, srcRGB, wSrc)))
				goto fail_scanGet;

			/* Convert data's elements into proper RGB for blending as well */
			if (sjme_error_is(error = g->util->pfScanToPf(g,
				blendPf, dataScanline, 0, blendBytes,
				pf, (void*)data, (y * wSrc * byteSize),
				-1, wSrc)))
				goto fail_srcBlendMap;

			/* Perform the actual blending operation, in full RGB. */
			if (sjme_error_is(error = g->util->blendRGBInto(g,
				g->hasAlpha, alpha,
				mulAlpha, mulAlphaVal,
				dataScanline, srcRGB, wSrc)))
				goto fail_blendInto;

			/* Now set blended pixels back into 'data' with correct format */
			if (sjme_error_is(error = g->util->pfScanToPf(g, pf,
				(void*)data, (y * wSrc * byteSize), -1,
				blendPf, dataScanline, 0, blendBytes,
				wSrc)))
				goto fail_destMapBlend;
		}
		
		/* No alpha blending, so we can speed it up with a direct pfScanGet */
		else
		{
			if (sjme_error_is(error = g->util->pfScanGet(g, pf, xSrc, ySrc + y,
				(void*)((char*)data + y * wSrc * byteSize),
				wSrc * byteSize,
				wSrc)))
				goto fail_scanGet;
		}
	}

	if (srcRGB != NULL)
		sjme_alloca_free(srcRGB);
	if (dataScanline != NULL)
		sjme_alloca_free(dataScanline);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;

	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);

	/* Success? */
	return error;

fail_oob:
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("getRegion(%p, %d, %d, %d, %d, %d, %d) != [%d, %d]",
		g, pf, xSrc, ySrc, wSrc, mulAlpha, mulAlphaVal,
		g->width, g->height);
#endif
fail_any:
fail_scanGet:
fail_srcBlendMap:
fail_blendInto:
fail_destMapBlend:
	if (srcRGB != NULL)
		sjme_alloca_free(srcRGB);
	if (dataScanline != NULL)
		sjme_alloca_free(dataScanline);

	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);

	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_transferRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil srcPencil,
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
	sjme_attrInValue sjme_scritchui_transferRegionMode mode)
{
	sjme_errorCode error;
	sjme_jint origImgWidth, origImgHeight;
	sjme_jint* rgb;
	sjme_jint rgbPixels, rgbBytes, direction, zS, zD, y, yE;
	sjme_gfx_pixelFormat rgbPf;
	sjme_scritchui_pencilBlendingMode oldBlend;
	
	if (g == NULL || srcPencil == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These pencils must come from the same ScritchUI instance as these */
	/* would be in the same event and locking loop! */
	if (g->common.state != srcPencil->common.state)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* If memmove() like transfer, needs to be the same size. */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
	{
		/* Determine source bits per pixel. */
		zS = -1;
		if (sjme_error_is(error = g->util->pfScanBits(srcPencil,
			srcPencil->pixelFormat,
			1, -1, &zS, NULL)) || zS <= 0)
			return sjme_error_default(error);
		
		/* Determine dest bits per pixel. */
		zD = -1;
		if (sjme_error_is(error = g->util->pfScanBits(g,
			g->pixelFormat,
			1, -1, &zD, NULL)) || zS <= 0)
			return sjme_error_default(error);
		
		/* These must be the same values. */
		if (zS != zD)
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* If we are doing a memmove() like transfer, since drawRegion() is not */
	/* being used, we need to pre-anchor the destination. We also need to */
	/* make sure the target destination is actually valid. */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
		if (sjme_error_is(error = g->util->applyAnchor(anchor,
			xDest, yDest, wDest, hDest,
			0, &xDest, &yDest)))
			return sjme_error_default(error);
	
	/* The source rectangle must always be in bounds. */
	/* Note that we can take the original source pencil graphics surface */
	/* to make sure we do not read out of bounds. */
	origImgWidth = srcPencil->width;
	origImgHeight = srcPencil->height;
	if (xSrc < 0 || ySrc < 0 || wSrc <= 0 || hSrc <= 0 ||
		(xSrc + wSrc) > origImgWidth || (ySrc + hSrc) > origImgHeight ||
		(xSrc + wSrc) > srcPencil->width ||
		(ySrc + hSrc) > srcPencil->height ||
		(xSrc + wSrc) < 0 || (ySrc + hSrc) < 0 ||
		origImgWidth < 0 || origImgHeight < 0 ||
		wDest < 0 || hDest < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Additional checks if memmove()-like. */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
	{
		/* memmove() copy requires the destination be fully in bounds. */
		if (xDest < 0 || yDest < 0 ||
			(xDest + wDest) < 0 || (yDest + hDest) < 0 ||
			(xDest + wDest) > g->width ||
			(yDest + hDest) > g->height)
			return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
		/* Source and destination dimensions must match. */
		if (wSrc != wDest || hSrc != hDest)
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Drawing nothing? */
	if (wSrc <= 0 || hSrc <= 0 || wDest <= 0 || hDest <= 0)
		return SJME_ERROR_NONE;
	
	/* Drop all translations. */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
		trans = SJME_SCRITCHUI_TRANS_NONE;
	
	/* Does the source graphics have alpha? */
	rgbPf = (sjme_scritchpen_hasAlpha(srcPencil->pixelFormat) ?
		SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 :
		SJME_GFX_PIXEL_FORMAT_INT_RGB888);
	
	/* memmove() like transfer copies only scan per scan */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
		rgbPixels = wSrc;
	else
		rgbPixels = wSrc * hSrc;
	
	/* Allocate RGB/PF buffer. */
	rgb = NULL;
	rgbBytes = rgbPixels * (sizeof(*rgb));
	rgb = sjme_alloca(rgbBytes);
	if (rgb == NULL)
	{
		error = sjme_error_outOfMemory(NULL, rgbBytes);
		goto fail_alloca;
	}

	/* Clear. */
	memset(rgb, 0, rgbBytes);
	
	/* Lock destination */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		goto fail_destLock;
	
	/* Lock source, if different. */
	if (g != srcPencil)
		if (sjme_error_is(error = sjme_scritchpen_core_lock(srcPencil)))
			goto fail_srcLock;
	
	/* memmove() like transfer. */
	if ((mode & SJME_SCRITCHUI_TRANSFER_MEMMOVE) != 0)
	{
		/* Determine the virtual scan based pixel positions. */
		zS = (srcPencil->width * ySrc) + xSrc;
		zD = (g->width * yDest) + xDest;
		
		/* Which direction is used? */
		direction = (zD < zS ? 1 : -1);
		
		/* Copy scan by scan. */
		error = SJME_ERROR_NONE;
		y = (direction > 0 ? 0 : hSrc);
		yE = (direction > 0 ? (hSrc + 1) : -1);
		for (; y != yE; y += direction)
		{
			error |= srcPencil->impl->rawScanGet(
				srcPencil, xSrc, ySrc + y,
				rgb, rgbBytes, wSrc);
			error |= g->impl->rawScanPutPure(
				g, xDest, yDest + y,
				rgb, rgbBytes, wDest);
		}
		
		/* Any errors occurred? */
		if (sjme_error_is(error))
			goto fail_memmove;
	}
	
	/* Load entire chunk and blend RGB. */
	else
	{
		/* Read in RGB data. */
		if (sjme_error_is(error = g->apiInThread->getRegion(
			g, rgbPf,
			rgb, 0, rgbBytes, wSrc,
			alpha,
			xSrc, ySrc, wSrc, hSrc,
			anchor)))
			goto fail_readRgb;
	
		/* Remember the old blending mode so that SRC is forced if */
		/* requested. Disregard error, in the event the blending mode is */
		/* unsupported as it will be unchanged. */
		oldBlend = g->state.blending;
		if ((mode & SJME_SCRITCHUI_TRANSFER_SRC_FORCE) != 0)
			g->apiInThread->setBlendingMode(g,
				SJME_SCRITCHUI_PENCIL_BLEND_SRC);
		
		/* Write RGB data. */
		if (sjme_error_is(error = g->apiInThread->drawRegion(
			g, rgbPf,
			rgb, 0, rgbBytes, wSrc, 
			alpha, 
			0, 0, wSrc, hSrc,
			trans, 
			xDest, yDest, 
			anchor,
			wDest, hDest, wSrc, hSrc)))
		{
			/* If SRC was forced, return the old blending mode. */
			if ((mode & SJME_SCRITCHUI_TRANSFER_SRC_FORCE) != 0)
				g->apiInThread->setBlendingMode(g, oldBlend);
		
			/* Fail. */
			goto fail_writeRgb;
		}
	
		/* If SRC was forced, return the old blending mode. */
		if ((mode & SJME_SCRITCHUI_TRANSFER_SRC_FORCE) != 0)
			g->apiInThread->setBlendingMode(g, oldBlend);
	}
	
	/* Release source lock. */
	if (g != srcPencil)
		if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(
			srcPencil)))
			goto fail_srcUnlock;
	
	/* Release destination lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_destUnlock;
	
	/* Cleanup. */
	sjme_alloca_free(rgb);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_memmove:
fail_readRgb:
fail_writeRgb:
	/* Source is locked at this point. */
	if (g != srcPencil)
		if (sjme_error_is(sjme_scritchpen_core_lockRelease(srcPencil)))
			return sjme_error_default(error);
	
fail_srcLock:
fail_srcUnlock:
	/* Destination is locked at this point. */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
fail_destLock:
fail_destUnlock:
fail_alloca:
	if (rgb != NULL)
		sjme_alloca_free(rgb);
	return sjme_error_default(error);
}
