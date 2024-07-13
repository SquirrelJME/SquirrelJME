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

sjme_errorCode sjme_scritchpen_corePrim_rawScanFillInt(
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

sjme_errorCode sjme_scritchpen_corePrim_rawScanFillShort(
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

sjme_errorCode sjme_scritchpen_corePrim_rawScanFillByte(
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

sjme_errorCode sjme_scritchpen_corePrim_rawScanGetNoDest(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	if (g == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inDataLen < 0 || inNumPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Just set everything to the first default value. */
	memset(outData, 0, inDataLen);
	
	/* Do nothing, there is no reading function. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_blendRGBInto(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) const sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels)
{
	sjme_jint i;
	sjme_juint d, s, a, na, rb, ag;
	sjme_juint srcMask, destMask;
	
	if (g == NULL || dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (numPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Source and dest mask, if alpha is applicable. */
	destMask = (destAlpha ? 0 : UINT32_C(0xFF000000));
	srcMask = (srcAlpha ? 0 : UINT32_C(0xFF000000));
	
	/* Blend each pixel individually. */
	/* R(dest) = (R(src) * A(src)) + (R(dest) * (1 - A(src))) */
	/* G(dest) = (G(src) * A(src)) + (G(dest) * (1 - A(src))) */
	/* B(dest) = (B(src) * A(src)) + (B(dest) * (1 - A(src))) */
	/* A(dest) = A(src) + A(dest) - (A(src) * A(dest)) */
	for (i = 0; i < numPixels; i++)
	{
#define AM UINT32_C(0xFF000000)
#define RBM UINT32_C(0x00FF00FF)
#define GM UINT32_C(0x0000FF00)
#define AGM UINT32_C(0xFF00FF00)
#define ONE UINT32_C(0xFF)
		s = src[i] | srcMask;
		d = dest[i] | destMask;
		
		a = (s & AM) >> 24U;
		na = 255U - a;
		rb = ((na * (d & RBM)) + (a * (s & RBM))) >> 8U;
		ag = (na * ((d & AGM) >> 8U)) + (a * (ONE | ((s & GM) >> 8U)));
		
		dest[i] = ((rb & RBM) | (ag & AGM)) | destMask;
#undef AM
#undef RBM
#undef GM
#undef AGM
#undef ONE
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_rawScanBytes(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBytes,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit)
{
	sjme_jint result;
	
	if (g == NULL || outBytes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixels < 0 || (outLimit != NULL && inBytes < 0))
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
	
	/* Calculate smaller value? */
	if (outLimit != NULL)
	{
		/* Use the smaller of the two. */
		if (result < inBytes)
			*outLimit = inBytes;
		else
			*outLimit = result;
	}
	
	/* Success! */
	*outBytes = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_rawScanToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(outRgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(inRawLen) const void* inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen)
{
	sjme_errorCode error;
	sjme_juint destAlphaMask;
	sjme_jint limit, i, t;
	sjme_jint* si;
	sjme_jshort* ss;
	sjme_jbyte* sb;
	sjme_jint* d;
	sjme_scritchui_pencilColor color;
	
	if (g == NULL || outRgb == NULL || inRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRgbOff < 0 || outRgbLen < 0 || (outRgbOff + outRgbLen) < 0 ||
		inRawOff < 0 || inRawLen < 0 || (inRawOff + inRawLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Destination alpha mask. */
	destAlphaMask = (g->hasAlpha ? 0 : 0xFF000000);
	
	/* Determine the type to use for scan reading. */
	si = NULL;
	ss = NULL;
	sb = NULL;
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			si = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen / 4;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			ss = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen / 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			sb = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			sjme_todo("Impl?");
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Writing where? */
	d = &outRgb[outRgbOff];
	
	/* If the output RGB is smaller than the raw input, limit to it. */
	if (outRgbLen < limit)
		limit = outRgbLen;
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Already in the most native format? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 && si != NULL)
		memmove(d, si, limit * 4);
	
	/* Integer mapping. */
	else if (si != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(si++);
			
			error |= sjme_scritchpen_corePrim_mapColorFromRaw(g,
				t, &color);
			
			*(d++) = color.argb | destAlphaMask;
		}
	}
	
	/* Short mapping. */
	else if (ss != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(ss++) & 0xFFFF;
			
			error |= sjme_scritchpen_corePrim_mapColorFromRaw(g,
				t, &color);
			
			*(d++) = color.argb | destAlphaMask;
		}
	}
	
	/* Byte mapping. */
	else if (sb != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(sb++) & 0xFF;
			
			error |= sjme_scritchpen_corePrim_mapColorFromRaw(g,
				t, &color);
			
			*(d++) = color.argb | destAlphaMask;
		}
	}
	
	/* Unknown. */
	else
	{
		sjme_todo("Impl?");
	}
	
	/* Failed or success? */
	if (sjme_error_is(error))
		return sjme_error_default(error);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanFill(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(inNumPixels) sjme_jint* outRgb,
	sjme_attrInPositiveNonZero sjme_jint outRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jint inValue)
{
	sjme_jint i;
	sjme_jint* p;
	
	if (g == NULL || outRgb == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRgbOff < 0 || inNumPixels < 0 ||
		(outRgbOff + inNumPixels) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Fill in, which is a rather simple operation. */
	p = &outRgb[outRgbOff];
	for (i = 0; i < inNumPixels; i++)
		*(p++) = inValue;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_jint* destRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_errorCode error;
	sjme_jint ex, rgbBytes, rawScanBytes;
	void* rawScan;
	
	if (g == NULL || destRgb == NULL)
		return SJME_ERROR_NONE;
	
	/* We cannot access a region outside the image bounds. */
	ex = x + inNumPixels;
	rgbBytes = inNumPixels * sizeof(*destRgb);
	if (x < 0 || y < 0 || inNumPixels < 0 ||
		ex < 0 || ex > g->width || rgbBytes < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* How much data is to be read? */
	rawScanBytes = (inNumPixels * g->bitsPerPixel) / 8;
	if (rawScanBytes < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate. */
	rawScan = sjme_alloca(rawScanBytes);
	if (rawScan == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Clear. */
	memset(rawScan, 0, rawScanBytes);
	
	/* Load in from image directly. */
	if (sjme_error_is(error = g->prim.rawScanGet(g,
		x, y, rawScan, rawScanBytes, inNumPixels)))
		return sjme_error_default(error);
	
	/* Map to RGB. */
	return g->util->rawScanToRgb(g,
		destRgb, 0, inNumPixels,
		rawScan, 0, rawScanBytes);
}

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanPut(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const sjme_jint* srcRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue)
{
	sjme_errorCode error;
	sjme_jint ex, rawScanBytes, rgbBytes;
	void* rawScan;
	sjme_jint* destRgb;
	
	if (g == NULL || srcRgb == NULL)
		return SJME_ERROR_NONE;
	
	/* We cannot access a region outside the image bounds. */
	ex = x + inNumPixels;
	rgbBytes = inNumPixels * sizeof(*destRgb);
	if (x < 0 || y < 0 || inNumPixels < 0 ||
		ex < 0 || ex > g->width || rgbBytes < 0 ||
		(mulAlpha && (mulAlphaValue < 0 || mulAlphaValue > 255)))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* How much data is to be written? */
	rawScanBytes = (inNumPixels * g->bitsPerPixel) / 8;
	if (rawScanBytes < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Do we need to alpha blend? */
	if (mulAlpha || g->state.applyAlpha)
	{
		/* Allocate dest RGB data. */
		destRgb = sjme_alloca(rgbBytes);
		if (destRgb == NULL)
			return SJME_ERROR_OUT_OF_MEMORY;
		
		/* Clear. */
		memset(destRgb, 0, rgbBytes);
		
		/* Load in RGB data from image, which might be lossy. */
		if (sjme_error_is(error = g->util->rgbScanGet(g,
			x, y, destRgb, inNumPixels)))
			return sjme_error_default(error);
		
		/* Perform blending. */
		if (sjme_error_is(error = g->util->blendRGBInto(g,
			g->hasAlpha, SJME_JNI_TRUE,
			mulAlpha, mulAlphaValue,
			destRgb, srcRgb, inNumPixels)))
			return sjme_error_default(error);
		
		/* The destination becomes the new source. */
		srcRgb = destRgb;
	}
	
	/* Allocate raw scan data. */
	rawScan = sjme_alloca(rawScanBytes);
	if (rawScan == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Clear. */
	memset(rawScan, 0, rawScanBytes);
	
	/* Map from RGB to raw pixels. */
	if (sjme_error_is(error = g->util->rgbToRawScan(g,
		rawScan, 0, rawScanBytes,
		srcRgb, 0, inNumPixels)))
		return sjme_error_default(error);
	
	/* Write direct image data. */
	return g->prim.rawScanPutPure(g,
		x, y,
		rawScan, rawScanBytes);
}

sjme_errorCode sjme_scritchpen_coreUtil_rgbToRawScan(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) const sjme_jint* inRgb,
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
	byteLimit = -1;
	if (sjme_error_is(error = g->util->rawScanBytes(g,
		inRgbLen, outRawLen, 
		&inRgbLenRaw, &byteLimit)) ||
		inRgbLenRaw < 0 || byteLimit < 0)
		return sjme_error_default(error);
	
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
