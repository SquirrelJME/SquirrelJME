/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include <string.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchui_core_lock(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_scritchui_pencilLockState* state;
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try locking. */
	if (g->lock != NULL)
	{
		/* Not implemented? */
		if (g->lock->lock == NULL || g->lock->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Forward if locking is needed. */
		if (sjme_atomic_sjme_jint_getAdd(&state->count, 1) == 0)
			if (sjme_error_is(error = g->lock->lock(g)))
				return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_lockRelease(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_scritchui_pencilLockState* state;
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try locking. */
	if (g->lock != NULL)
	{
		/* Not implemented? */
		if (g->lock->lock == NULL || g->lock->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Already released? Do nothing... */
		if (sjme_atomic_sjme_jint_get(&state->count) == 0)
			return SJME_ERROR_NONE;
		
		/* Forward if release is needed. */
		if (sjme_atomic_sjme_jint_getAdd(&state->count, -1) == 1)
			if (sjme_error_is(error = g->lock->lockRelease(g)))
				return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

/** Core pencil functions. */
static const sjme_scritchui_pencilFunctions sjme_scritchui_core_pencil =
{
	.blendRGBInto = sjme_scritchui_core_pencilBlendRGBInto,
	.copyArea = sjme_scritchui_core_pencilCopyArea,
	.drawChar = sjme_scritchui_core_pencilDrawChar,
	.drawChars = sjme_scritchui_core_pencilDrawChars,
	.drawHoriz = sjme_scritchui_core_pencilDrawHoriz,
	.drawLine = sjme_scritchui_core_pencilDrawLine,
	.drawPixel = sjme_scritchui_core_pencilDrawPixel,
	.drawRect = sjme_scritchui_core_pencilDrawRect,
	.drawSubstring = sjme_scritchui_core_pencilDrawSubstring,
	.drawTriangle = sjme_scritchui_core_pencilDrawTriangle,
	.drawXRGB32Region = sjme_scritchui_core_pencilDrawXRGB32Region,
	.fillRect = sjme_scritchui_core_pencilFillRect,
	.fillTriangle = sjme_scritchui_core_pencilFillTriangle,
	.mapColor = sjme_scritchui_core_pencilMapColor,
	.mapRawScanBytes = sjme_scritchui_core_pencilMapRawScanBytes,
	.mapRawScanFromRGB = sjme_scritchui_core_pencilMapRawScanFromRGB,
	.mapRGBFromRawScan = sjme_scritchui_core_pencilMapRGBFromRawScan,
	.setAlphaColor = sjme_scritchui_core_pencilSetAlphaColor,
	.setBlendingMode = sjme_scritchui_core_pencilSetBlendingMode,
	.setClip = sjme_scritchui_core_pencilSetClip,
	.setDefaultFont = sjme_scritchui_core_pencilSetDefaultFont,
	.setFont = sjme_scritchui_core_pencilSetFont,
	.setStrokeStyle = sjme_scritchui_core_pencilSetStrokeStyle,
	.translate = sjme_scritchui_core_pencilTranslate,
};

sjme_errorCode sjme_scritchui_pencilInitStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_scritchui_pencilBase result;
	
	if (inPencil == NULL || inFunctions == NULL || defaultFont == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sw <= 0 || sh <= 0 || bw <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Raw scan putting is required at a minimum. */
	if (inFunctions->rawScanPutPure == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Locking functions which are required. */
	if (inLockFuncs != NULL)
		if (inLockFuncs->lock == NULL || inLockFuncs->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Setup base result. */
	memset(&result, 0, sizeof(result));
	result.api = &sjme_scritchui_core_pencil;
	result.impl = inFunctions;
	result.lock = inLockFuncs;
	result.defaultFont = defaultFont;
	result.pixelFormat = pf;
	result.width = sw;
	result.height = sh;
	result.scanLen = bw;
	
	/* Determine bits per pixel. */
	result.bitsPerPixel = -1;
	switch (pf)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			result.bitsPerPixel = 32;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			result.bitsPerPixel = 16;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			result.bitsPerPixel = 8;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			result.bitsPerPixel = 4;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			result.bitsPerPixel = 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			result.bitsPerPixel = 1;
			break;
	}
	
	/* Determine raw scan line length. */
	result.scanLenBytes = (result.scanLen * result.bitsPerPixel) / 8;
	
	/* Copy lock front end source? */
	if (inLockFuncs != NULL && inLockFrontEndCopy != NULL)
		memmove(&result.lockState.source, inLockFrontEndCopy,
			sizeof(result.lockState.source));
	
	/* Is there an alpha channel? */
	/* Note that alpha can only be supported if we can read the underlying */
	/* pixel data. */
	result.hasAlpha = (pf == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555 ||
		pf == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ? SJME_JNI_TRUE :
		SJME_JNI_FALSE) && (result.impl->rawScanGet != NULL);
	
	/* Copy in front end? */
	if (copyFrontEnd != NULL)
		memmove(&result.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Raw scan put, must be implemented always. */
	result.prim.rawScanPutPure = result.impl->rawScanPutPure;
	
	/* We do handle alpha blending in our own wrapper, however. */
	if (result.impl->rawScanGet != NULL)
		result.prim.rawScanPut = sjme_scritchui_corePrim_rawScanPut;
	else
		result.prim.rawScanPut = sjme_scritchui_corePrim_rawScanPutSkipBlend;
	
	/* These are always handled by us. */
	result.prim.drawHoriz = sjme_scritchui_corePrim_drawHoriz;
	result.prim.drawLine = sjme_scritchui_corePrim_drawLine;
	result.prim.drawPixel = sjme_scritchui_corePrim_drawPixel;
	
	/* Raw scan get. */
	if (result.impl->rawScanGet != NULL)
		result.prim.rawScanGet = result.impl->rawScanGet;
	else
		result.prim.rawScanGet = sjme_scritchui_corePrim_rawScanGet;
	
	/* Color mapping. */
	if (result.impl->mapColor != NULL)
		result.prim.mapColor = result.impl->mapColor;
	else
		result.prim.mapColor = sjme_scritchui_corePrim_mapColor;
	
	/* Determine bytes per pixel. */
	result.api->mapRawScanBytes(&result, 1,
		&result.bytesPerPixel);
	
	/* Basic filling of raw value. */
	if (result.bytesPerPixel == 4)
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillInt;
	else if (result.bytesPerPixel == 2)
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillShort;
	else
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillByte;
	
	/* Set initial state, ignore any errors. */
	result.api->setAlphaColor(&result, 0xFF000000);
	result.api->setStrokeStyle(&result,
		SJME_SCRITCHUI_PENCIL_STROKE_SOLID);
	result.api->setBlendingMode(&result,
		SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	result.api->setDefaultFont(&result);
	
	/* Success! Copy back. */
	memmove(inPencil, &result, sizeof(result));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEnd* pencilFrontEndCopy)
{
	sjme_errorCode error;
	sjme_scritchui_pencil result;
	sjme_alloc_weak resultWeak;
	sjme_scritchui_pencilFont defaultFont;
	
	if (inState == NULL || outPencil == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bw <= 0 || bh <= 0 || sx < 0 || sy < 0 || sw <= 0 || sh <= 0 ||
		sw > bw || sh > bh || (sx + sw) < 0 || (sx + sw) > bw ||
		(sy + sh) < 0 || (sy + sh) > bh)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* If natively supported, see if it can create a graphics context. */
	if (inState->impl->hardwareGraphics != NULL)
	{
		/* If this does not fail, use native graphics. */
		if (!sjme_error_is(inState->impl->hardwareGraphics(
			inState, outPencil, outWeakPencil, pf, bw, bh,
			inLockFuncs, inLockFrontEndCopy,
			sx, sy, sw, sh, pencilFrontEndCopy)))
			return SJME_ERROR_NONE;
	}
	
	/* At this point locking functions are required. */
	if (inLockFuncs == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Get default font. */
	defaultFont = NULL;
	if (sjme_error_is(error = inState->api->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		return sjme_error_default(error);
	
	/* Forward to basic operations. */
	result = NULL;
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_scritchui_pencilInitBuffer(
		inState->pool, &result, &resultWeak,
		pf, bw, bh,
		inLockFuncs, inLockFrontEndCopy, sx, sy, sw, sh,
		defaultFont, pencilFrontEndCopy)) ||
		result == NULL || resultWeak == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outPencil = result;
	if (outWeakPencil != NULL)
		*outWeakPencil = resultWeak;
	return SJME_ERROR_NONE;
}
