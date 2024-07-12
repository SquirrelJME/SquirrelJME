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

sjme_errorCode sjme_scritchui_corePrim_rawScanFillInt(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jint* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 4)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_corePrim_rawScanFillShort(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jshort* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 2)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_corePrim_rawScanFillByte(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jbyte* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 1)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_corePrim_rawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint inX,
	sjme_attrInPositive sjme_jint inY,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	if (g == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing, there is no reading function. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_pencilBlendRGBInto(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels)
{
	sjme_jint i;
	sjme_juint pac, sa, na, srb, sgg, dcc, xrb, xgg;
	sjme_juint srcMask, destMask;
	
	if (g == NULL || dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (numPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Source and dest mask, if alpha is applicable. */
	destMask = (destAlpha ? 0 : 0xFF000000);
	srcMask = (srcAlpha ? 0 : 0xFF000000);
	
	/* Blend each pixel individually. */
	/* R(dest) = (R(src) * A(src)) + (R(dest) * (1 - A(src))) */
	/* G(dest) = (G(src) * A(src)) + (G(dest) * (1 - A(src))) */
	/* B(dest) = (B(src) * A(src)) + (B(dest) * (1 - A(src))) */
	/* A(dest) = A(src) + A(dest) - (A(src) * A(dest)) */
	for (i = 0; i < numPixels; i++)
	{
		/* From my existing Java code... */
		/* int pac = __b[src]; */
		/* int sa = pac >>> 24; */
		/* int na = (sa ^ 0xFF); */
		/* int srb = ((pac & 0xFF00FF) * sa); */
		/* int sgg = (((pac >>> 8) & 0xFF) * sa); */
		/* int dcc = data[dp]; */
		/* int xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8; */
		/* int xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) *  ... */
		/* 	257) >>> 16; */
		/*  */
		/* data[dp] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8)); */
		pac = src[i] | srcMask;
		dcc = dest[i] | destMask;
		
		sa = (pac >> 24);
		na = (sa ^ 0xFF);
		srb = ((pac & 0xFF00FF) * sa);
		sgg = (((pac >> 8) & 0xFF) * sa);
		xrb = (srb + ((dcc & 0xFF00FF) * na)) >> 8;
		xgg = (((sgg + (((dcc >> 8) & 0xFF) * na)) + 1) * 257) >> 16;
		
		dest[i] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8)) | destMask;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_pencilCopyArea(
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
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_pencilDrawXRGB32Region(
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
	sjme_jint dx, dy, iwx, izy, at, q;
	sjme_jint* flatRgb;
	sjme_jint* srcRgb;
	void* rawScan;
	void* srcRaw;
	sjme_jint rawScanBytes, flatRgbBytes;
	sjme_jboolean destAlpha;
	
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
	
	/* Apply alpha? */
	destAlpha = g->hasAlpha;
	alpha = (alpha && g->impl->rawScanGet != NULL &&
		g->state.blending == SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	
	/* Transform. */
	sjme_scritchui_core_transform(g, &xDest, &yDest);
	
	/* If there is native drawing, use that as it will likely be faster */
	/* or more efficient for the API. */
	if (g->impl->drawXRGB32Region != NULL)
	{
		/* Anchor to target coordinates. */
		if (sjme_error_is(error = sjme_scritchui_core_anchor(anchor,
			xDest, yDest, wSrc, hSrc, 0,
			&xDest, &yDest)))
			return sjme_error_default(error);
		
		/* Lock. */
		if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
			return sjme_error_default(error);
			
		/* Forward, note that this is pre-anchored. */
		if (sjme_error_is(error = g->impl->drawXRGB32Region(
			g, data, off, dataLen, scanLen, alpha, xSrc, ySrc,
			wSrc, hSrc, trans, xDest, yDest, 0, wDest, hDest,
			origImgWidth, origImgHeight)))
			goto fail_any;
		
		/* Release lock. */
		if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
			return sjme_error_default(error);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* We are now doing the transforming and drawing ourselves. */
	/* Calculate transformation matrix. */
	memset(&m, 0, sizeof(m));
	if (sjme_error_is(error = sjme_scritchui_core_translateRotateScale(
		&m, trans, wSrc, hSrc, wDest, hDest)))
		return sjme_error_default(error);
	
	/* Anchor to target coordinates after scaling, because we need */
	/* to know what our target scale is. */
	if (sjme_error_is(error = sjme_scritchui_core_anchor(anchor,
		xDest, yDest, m.tw, m.th, 0,
		&xDest, &yDest)))
		return sjme_error_default(error);
	
	/* Determine how large our raw scan buffer actually is. */
	rawScanBytes = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		m.tw, &rawScanBytes)) || rawScanBytes < 0)
		return sjme_error_default(error);
	
	/* RGB buffer is this many bytes. */
	flatRgbBytes = m.tw * sizeof(*flatRgb);
	
	/* Setup input and output RGB buffers. */
	rawScan = sjme_alloca(rawScanBytes);
	flatRgb = sjme_alloca(flatRgbBytes);
	srcRgb = (alpha ? sjme_alloca(flatRgbBytes) : NULL);
	srcRaw = (alpha ? sjme_alloca(rawScanBytes) : NULL);
	if (rawScan == NULL || flatRgb == NULL ||
		(alpha && (srcRgb == NULL || srcRaw == NULL)))
		return sjme_error_defaultOr(error,
			SJME_ERROR_OUT_OF_MEMORY);
	
	/* Clear buffers. */
	memset(rawScan, 0, rawScanBytes);
	memset(flatRgb, 0, flatRgbBytes);
	
	if (alpha)
	{
		memset(srcRaw, 0, rawScanBytes);
		memset(srcRgb, 0, flatRgbBytes);
	}
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* Figure out the position of our base pointer. */
	/* Matrix multiplication? Squeak? */
	wxBase = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.x.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.x.zy);
	wx = wxBase;
	zyMajor = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.y.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.y.zy);
	zy = zyMajor;
	
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
			flatRgb[dx] = data[at];
		}
		
		/* Perform alpha blending? */
		if (alpha)
		{
			/* Get raw scanline data. */
			if (sjme_error_is(error = g->prim.rawScanGet(g,
				xDest, yDest + dy,
				srcRaw, rawScanBytes, m.tw)))
				goto fail_any;
			
			/* Map from Native to RGB. */
			if (sjme_error_is(error = g->api->mapRGBFromRawScan(
				g, srcRgb, 0, m.tw,
				srcRaw, 0, rawScanBytes)))
				goto fail_any;
			
			/* Blend? */
			if (sjme_error_is(error = g->api->blendRGBInto(
				g, destAlpha, SJME_JNI_TRUE,
				flatRgb, srcRgb, m.tw)))
				goto fail_any;
		}
		
		/* Map RGB to Native. */
		if (sjme_error_is(error = g->api->mapRawScanFromRGB(
			g, rawScan, 0, rawScanBytes,
			flatRgb, 0, m.tw)))
			goto fail_any;
		
		/* Render RGB line at destination. */
		if (sjme_error_is(error = g->prim.rawScanPut(g,
			xDest, yDest + dy,
			rawScan, rawScanBytes, m.tw)))
			goto fail_any;
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
		
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_core_pencilMapRawScanBytes(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes)
{
	sjme_jint result;
	
	if (g == NULL || outBytes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Depends on the pixel format. */
	result = -1;
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			result = inPixels * 4;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			result = inPixels * 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			result = inPixels;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			result = (inPixels >> 1) + (inPixels & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			result = (inPixels >> 2) + ((inPixels >> 1) & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			result = (inPixels >> 3) + ((inPixels >> 2) & 1);
			break;
	}
	
	/* Make sure what was calculated did not overflow. */
	if (result < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Success! */
	*outBytes = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_pencilMapRawScanFromRGB(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositive sjme_jint inRgbLen)
{
	sjme_errorCode error;
	sjme_jint byteLimit, inRgbOffRaw, inRgbLenRaw;
	
	if (g == NULL || outRaw == NULL || inRgb == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	inRgbOffRaw = inRgbOff * 4;
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		inRgbOff < 0 || inRgbLen < 0 || (inRgbOff + inRgbLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Double check RGB count. */
	inRgbLenRaw = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		inRgbLen, &inRgbLenRaw)) || inRgbLenRaw < 0)
		return sjme_error_default(error);
	
	/* Use the smaller of the two. */
	if (inRgbLenRaw < outRawLen)
		byteLimit = inRgbLenRaw;
	else
		byteLimit = outRawLen;
	
	/* Optimal format for direct copy? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888)
	{
		/* Copy over efficiently. */
		memmove(SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Simple byte swap. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRX8888)
	{
		return sjme_swap_uint_memmove(
			SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
	}
	
	/* Shift up by 8. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_XBGR8888)
	{
		return sjme_swap_shu8_uint_memmove(
			SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_core_pencilMapRGBFromRawScan(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(rgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(rawLen) void* inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen)
{
	sjme_errorCode error;
	sjme_jint byteLimit, outRgbOffRaw, outRgbLenRaw;
	
	if (g == NULL || outRgb == NULL || inRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	outRgbOffRaw = outRgbOff * 4;
	if (outRgbOff < 0 || outRgbLen < 0 || (outRgbOff + outRgbLen) < 0 ||
		inRawOff < 0 || inRawLen < 0 || (inRawOff + inRawLen) < 0 ||
		outRgbOffRaw < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Double check RGB count. */
	outRgbLenRaw = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		outRgbLen, &outRgbLenRaw)) || outRgbLenRaw < 0)
		return sjme_error_default(error);
		
	/* Use the smaller of the two. */
	if (outRgbLenRaw < inRawLen)
		byteLimit = outRgbLenRaw;
	else
		byteLimit = inRawLen;
	
	/* Optimal format for direct copy? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888)
	{
		/* Copy over efficiently. */
		memmove(SJME_POINTER_OFFSET(outRgb, outRgbOffRaw),
			SJME_POINTER_OFFSET(inRaw, inRawOff),
			byteLimit);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Simple byte swap. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRX8888)
	{
		return sjme_swap_uint_memmove(
			SJME_POINTER_OFFSET(outRgb, outRgbOffRaw),
			SJME_POINTER_OFFSET(inRaw, inRawOff),
			byteLimit);
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
