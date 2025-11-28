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
	sjme_fixed sa, sr, sg, sb, da, dr, dg, db, iA, ca, cr, cg, cb, tff;
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
	tff = sjme_fixed_hi(255);
	for (i = 0; i < numPixels; i++)
	{
		/* Extract as integers first. */
		da = ((dest[i] | destMask) >> 24) & 0xFF;
		dr = ((dest[i]) >> 16) & 0xFF;
		dg = ((dest[i]) >> 8) & 0xFF;
		db = dest[i] & 0xFF;
		
		sa = ((src[i] | srcMask) >> 24) & 0xFF;
		sr = ((src[i]) >> 16) & 0xFF;
		sg = ((src[i]) >> 8) & 0xFF;
		sb = src[i] & 0xFF;
		
		/* Inverse alpha. */
		iA = sjme_fixed_fraction(255 - sa, 255);
		
		/* Extract components. */
		da = sjme_fixed_fraction(da, 255);
		dr = sjme_fixed_fraction(dr, 255);
		dg = sjme_fixed_fraction(dg, 255);
		db = sjme_fixed_fraction(db, 255);
		
		sa = sjme_fixed_fraction(sa, 255);
		sr = sjme_fixed_fraction(sr, 255);
		sg = sjme_fixed_fraction(sg, 255);
		sb = sjme_fixed_fraction(sb, 255);
		
		/* A(dest) = A(src) + A(dest) - (A(src) * A(dest)) */
		ca = sa + da - sjme_fixed_mul(sa, da);
		
		/* R(dest) = (R(src) * A(src)) + (R(dest) * (1 - A(src))) */
		cr = sjme_fixed_mul(sr, sa) + sjme_fixed_mul(dr, iA);
		
		/* G(dest) = (G(src) * A(src)) + (G(dest) * (1 - A(src))) */
		cg = sjme_fixed_mul(sg, sa) + sjme_fixed_mul(dg, iA);
		
		/* B(dest) = (B(src) * A(src)) + (B(dest) * (1 - A(src))) */
		cb = sjme_fixed_mul(sb, sa) + sjme_fixed_mul(db, iA);
		
		/* Return the original factor. */
		ca = sjme_fixed_mul(ca, tff);
		cr = sjme_fixed_mul(cr, tff);
		cg = sjme_fixed_mul(cg, tff);
		cb = sjme_fixed_mul(cb, tff);
		
		/* Recompose. */
		ca = sjme_fixed_int(ca) & 0xFF;
		cr = sjme_fixed_int(cr) & 0xFF;
		cg = sjme_fixed_int(cg) & 0xFF;
		cb = sjme_fixed_int(cb) & 0xFF;
		dest[i] = (ca << 24) | (cr << 16) | (cg << 8) | cb | destMask;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_pfScanPut(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer src,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue)
{
	sjme_errorCode error;
	sjme_jint ex, rsBytes, pfBytes, blendBytes;
	sjme_pointer rsScan;
	sjme_jboolean srcAlpha;
	sjme_jint* blendDest;
	sjme_jint* blendSrc;
	
	if (g == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Get right edge. */
	ex = x + inNumPixels;
	if (x < 0 || y < 0 || inNumPixels < 0 ||
		ex < 0 || ex > g->width ||
		(mulAlpha && (mulAlphaValue < 0 || mulAlphaValue > 255)))
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;

	/* May be dynamically allocated. */
	rsScan = NULL;
	blendDest = NULL;
	blendSrc = NULL;

	/* Does the source have an alpha channel? */
	srcAlpha = sjme_scritchpen_hasAlpha(pf);
	
	/* We cannot access a region outside the image bounds. */
	pfBytes = -1;
	if (sjme_error_is(error = g->util->pfScanBytes(g, pf,
		inNumPixels, -1, &pfBytes, NULL)) ||
		pfBytes < 0)
	{
#if defined(SJME_CONFIG_DEBUG)
		sjme_message("pfScanPut(%p, %d, %d, %d, %p, %d, %d, %d) != [%d, %d]",
			g, pf, x, y, src, inNumPixels, mulAlpha, mulAlphaValue,
			g->width, g->height);
#endif
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;
	}
	
	/* How much data is to be written? */
	rsBytes = -1;
	if (sjme_error_is(error = g->util->pfScanBytes(g, g->pixelFormat,
		inNumPixels, -1, &rsBytes, NULL)) ||
		rsBytes < 0)
	{
#if defined(SJME_CONFIG_DEBUG)
		sjme_message("pfScanPut(%p, %d, %d, %d, %p, %d, %d, %d) != [%d, %d]",
			g, pf, x, y, inNumPixels, mulAlpha, mulAlphaValue,
			g->width, g->height);
#endif
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;
	}
	
	/* Allocate raw scan data. */
	rsScan = sjme_alloca(rsBytes);
	if (rsScan == NULL)
		return sjme_error_outOfMemory(NULL, rsBytes);
	
	/* Clear. */
	memset(rsScan, 0, rsBytes);
	
	/* Do we need to alpha blend? */
	if (srcAlpha || mulAlpha)
	{
		/* Because we need to do alpha channel blending, we need to read in */
		/* the RGB data from the scan as actual RGB data. */
		/* First we need the actual bytes to store the RGB data. */
		blendBytes = -1;
		if (sjme_error_is(error = g->util->pfScanBytes(g,
			SJME_GFX_PIXEL_FORMAT_INT_ARGB8888,
			inNumPixels, -1, &blendBytes, NULL)) ||
			blendBytes < 0)
			return SJME_ERROR_SCAN_OUT_OF_BOUNDS;

		/* Allocate both source and destination RGB buffers, for blending. */
		/* This is needed because we need a temporary buffer and also the */
		/* source and destination pixel formats might not even match. */
		blendDest = sjme_alloca(blendBytes);
		blendSrc = sjme_alloca(blendBytes);
		if (blendDest == NULL || blendSrc == NULL)
			goto fail_allocBlends;

		/* Need to clear both. */
		memset(blendDest, 0, pfBytes);
		memset(blendSrc, 0, pfBytes);

		/* Load in the destination data as RGB, this needs to be blended */
		/* onto. */
		if (sjme_error_is(error = g->util->rgbScanGet(g,
			x, y, blendDest, inNumPixels)))
			goto fail_scanGet;

		/* Also need to do the same for the source scan, it has to be in */
		/* RGB format regardless. */
		if (sjme_error_is(error = g->util->pfScanToPf(g,
			SJME_GFX_PIXEL_FORMAT_INT_ARGB8888,
			blendSrc, blendBytes, -1,
			pf,
			(void*)src, pfBytes, -1,
			inNumPixels)))
			goto fail_srcBlendMap;

		/* Perform the actual blending operation, in full RGB. */
		if (sjme_error_is(error = g->util->blendRGBInto(g,
			g->hasAlpha, srcAlpha,
			mulAlpha, mulAlphaValue,
			blendDest, blendSrc, inNumPixels)))
			goto fail_blendInto;

		/* Map from RGB to the format that the raw scan is using. */
		if (sjme_error_is(error = g->util->pfScanToPf(g,
			g->pixelFormat,
			rsScan, rsBytes, -1,
			SJME_GFX_PIXEL_FORMAT_INT_ARGB8888,
			blendDest, blendBytes, -1,
			inNumPixels)))
			goto fail_destMapBlend;
	}

	/* Map from PF to raw pixels. */
	else
	{
		/* Map from the given PF to raw pixels. */
		if (sjme_error_is(error = g->util->pfScanToPf(g,
			g->pixelFormat,
			rsScan, rsBytes, -1,
			pf,
			src, pfBytes, -1,
			inNumPixels)))
			goto fail_destMapBlend;
	}
	
	/* Write direct image data. */
	if (sjme_error_is(error = g->prim.rawScanPutPure(g, x, y,
		rsScan, rsBytes, inNumPixels)))
		goto fail_putPure;

	/* Cleanup, as always. */
	if (blendDest != NULL)
		sjme_alloca_free(blendDest);
	if (blendSrc != NULL)
		sjme_alloca_free(blendSrc);
	if (rsScan != NULL)
		sjme_alloca_free(rsScan);

	/* Success! */
	return SJME_ERROR_NONE;

fail_srcBlendMap:
fail_destMapBlend:
fail_blendInto:
fail_scanGet:
fail_allocBlends:
fail_putPure:
fail_mapToRaw:
	if (blendDest != NULL)
		sjme_alloca_free(blendDest);
	if (blendSrc != NULL)
		sjme_alloca_free(blendSrc);
	if (rsScan != NULL)
		sjme_alloca_free(rsScan);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_coreUtil_pfScanBytes(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBytes,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit)
{
	sjme_jint result;
	
	if (g == NULL || outBytes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (inPixels < 0 || (outLimit != NULL && inBytes < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Depends on the pixel format. */
	result = -1;
	switch (pf)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGR888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGBX8888:
			result = inPixels * 4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888:
		case SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888:
			result = inPixels * 3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536A:
			result = inPixels * 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256A:
			result = inPixels;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4A:
			result = (inPixels >> 1) + (inPixels & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2A:
			result = (inPixels >> 2) + ((inPixels >> 1) & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A:
			result = (inPixels >> 3) + ((inPixels >> 2) & 1);
			break;

		default:
			return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
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

sjme_errorCode sjme_scritchpen_coreUtil_pfScanToPf(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat destPf,
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInPositive sjme_jint destRawOff,
	sjme_attrInNegativeOnePositive sjme_jint destRawLen,
	sjme_attrInValue sjme_gfx_pixelFormat srcPf,
	sjme_attrInNotNull sjme_pointer src,
	sjme_attrInPositive sjme_jint srcRawOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRawLen,
	sjme_attrInPositive sjme_jint inNumPixels)
{
	sjme_errorCode error;
	sjme_jint rgbBytes;
	sjme_jint* rgbScan;
	
	if (g == NULL || dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (destPf < 0 || destPf >= SJME_NUM_GFX_PIXEL_FORMATS ||
		srcPf < 0 || srcPf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (destRawOff < 0 || srcRawOff < 0 ||
		destRawLen < -1 || srcRawLen < -1)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	if (inNumPixels < 0)
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;

	/* Calculate actual lengths from input values? */
	/* Destination. */
	if (destRawLen < 0)
		if (sjme_error_is(error = sjme_scritchpen_coreUtil_pfScanBytes(
			g, destPf, inNumPixels, -1,
			&destRawLen, NULL)) || destRawLen < 0)
			return sjme_error_default(error);

	/* Source. */
	if (srcRawLen < 0)
		if (sjme_error_is(error = sjme_scritchpen_coreUtil_pfScanBytes(
			g, destPf, inNumPixels, -1,
			&srcRawLen, NULL)) || srcRawLen < 0)
			return sjme_error_default(error);

	/* Double check bounds. */
	if ((destRawOff + destRawLen) < 0 || (srcRawOff + srcRawLen) < 0)
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;

	/* If the pixel format is the same, we can short circuit and just copy */
	/* the data directly. */
	if (destPf == srcPf)
	{
		/* Direct move over. */
		memmove(SJME_POINTER_OFFSET(dest, destRawOff),
			SJME_POINTER_OFFSET(src, srcRawOff),
			(destRawLen < srcRawLen ? destRawLen : srcRawLen));
		
		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Setup buffer to map the raw scan to RGB, which will then be mapped */
	/* back to the pixel format. */
	sjme_todo("Impl?");

	/* Run conversion from the source to RGB. */
	/* sjme_scritchpen_coreUtil_rawScanToRgb */
	sjme_todo("Impl?");

	/* Run conversion from the RGB to the destination. */
	/* sjme_scritchpen_coreUtil_rgbToRawScan */
	sjme_todo("Impl?");

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_rawScanToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(outRgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(inRawLen) sjme_cpointer inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen)
{
	sjme_errorCode error;
	sjme_juint destAlphaMask;
	sjme_jint limit, i, t, mulShift, j;
	const sjme_jint* si;
	const sjme_jshort* ss;
	const sjme_jbyte* sb;
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
	mulShift = 1;
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGR888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGBX8888:
			si = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen / 4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888:
		case SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888:
			sb = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen / 3;
			mulShift = 3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			ss = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen / 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256A:
			sb = SJME_POINTER_OFFSET(inRaw, inRawOff);
			limit = inRawLen;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A:
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
	
	/* Almost there. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888 && si != NULL)
	{
		/* Copy and put in the mask. */
		for (i = 0; i < limit; i++)
			*(d++) = *(si++) | destAlphaMask;
	}
	
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
	
	/* Triple byte? */
	else if (sb != NULL && mulShift == 3)
	{
		for (i = 0; i < limit; i++)
		{
			t = ((sjme_juint)(((*(sb++)) & 0xFF))) << 16;
			t |= ((sjme_juint)(((*(sb++)) & 0xFF))) << 8;
			t |= ((sjme_juint)(((*(sb++)) & 0xFF)));
			
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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We cannot access a region outside the image bounds. */
	ex = x + inNumPixels;
	rgbBytes = inNumPixels * sizeof(*destRgb);
	if (x < 0 || y < 0 || inNumPixels < 0 ||
		ex < 0 || ex > g->width || rgbBytes < 0)
	{
#if defined(SJME_CONFIG_DEBUG)
		sjme_message("rgbScanGet(%p, %d, %d, %p, %d) != [%d, %d]",
			g, x, y, destRgb, inNumPixels,
			g->width, g->height);
#endif
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;
	}
	
	/* How much data is to be read? */
	rawScanBytes = (inNumPixels * g->bitsPerPixel) / 8;
	if (rawScanBytes < 0)
	{
#if defined(SJME_CONFIG_DEBUG)
		sjme_message("rgbScanGet(%p, %d, %d, %p, %d) != [%d, %d]",
			g, x, y, destRgb, inNumPixels,
			g->width, g->height);
#endif
		return SJME_ERROR_SCAN_OUT_OF_BOUNDS;
	}
	
	/* Allocate. */
	rawScan = sjme_alloca(rawScanBytes);
	if (rawScan == NULL)
		return sjme_error_outOfMemory(NULL, rawScanBytes);
	
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
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue)
{
	if (g == NULL || srcRgb == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward to generic format scan put. */
	return g->util->pfScanPut(g,
		(srcAlpha ? SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 :
			SJME_GFX_PIXEL_FORMAT_INT_RGB888),
		x, y,
		srcRgb, inNumPixels,
		mulAlpha, mulAlphaValue);
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
	sjme_jint limit, i, t, mulShift, j;
	sjme_jint* di;
	sjme_jshort* ds;
	sjme_jbyte* db;
	const sjme_jint* s;
	sjme_scritchui_pencilColor color;
	
	if (g == NULL || inRgb == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inRgbOff < 0 || inRgbLen < 0 || (inRgbOff + inRgbLen) < 0 ||
		outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine the type to use for scan reading. */
	di = NULL;
	ds = NULL;
	db = NULL;
	mulShift = 1;
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGR888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGBX8888:
			di = SJME_POINTER_OFFSET(outRaw, outRawOff);
			limit = outRawLen / 4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888:
		case SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888:
			db = SJME_POINTER_OFFSET(outRaw, outRawOff);
			limit = outRawLen / 3;
			mulShift = 3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			ds = SJME_POINTER_OFFSET(outRaw, outRawOff);
			limit = outRawLen / 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			db = SJME_POINTER_OFFSET(outRaw, outRawOff);
			limit = outRawLen;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			sjme_todo("Impl?");
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Reading what? */
	s = &inRgb[inRgbOff];
	
	/* If the output RGB is smaller than the raw input, limit to it. */
	if (inRgbLen < limit)
		limit = inRgbLen;
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Already in the most native format? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 && di != NULL)
		memmove(di, s, limit * 4);
	
	/* Almost there. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888 && di != NULL)
	{
		/* Copy and put in the mask. */
		for (i = 0; i < limit; i++)
			*(di++) = *(s++);
	}
	
	/* Integer mapping. */
	else if (di != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(s++);
			
			error |= sjme_scritchpen_corePrim_mapColorFromRGB(g,
				t, &color);
			
			*(di++) = color.v;
		}
	}
	
	/* Short mapping. */
	else if (ds != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(s++);
			
			error |= sjme_scritchpen_corePrim_mapColorFromRGB(g,
				t, &color);
			
			*(ds++) = color.v & 0xFFFF;
		}
	}
	
	/* Triple byte? */
	else if (db != NULL && mulShift == 3)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(s++);
			
			error |= sjme_scritchpen_corePrim_mapColorFromRGB(g,
				t, &color);
			
			*(db++) = (color.v >> 16) & 0xFF;
			*(db++) = (color.v >> 8) & 0xFF;
			*(db++) = (color.v) & 0xFF;
		}
	}
	
	/* Byte mapping. */
	else if (db != NULL)
	{
		for (i = 0; i < limit; i++)
		{
			t = *(s++);
			
			error |= sjme_scritchpen_corePrim_mapColorFromRGB(g,
				t, &color);
			
			*(db++) = color.v & 0xFF;
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
