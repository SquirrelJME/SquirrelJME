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
#include "lib/scritchui/core/coreSerial.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchpen_core_close(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Release the front-end. */
	if (g->common.frontEnd.base.bindType != SJME_FRONTEND_BINDLESS)
		return sjme_frontEnd_release(g,
			SJME_AS_FE_BINDABLEP(&g->common.frontEnd));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_lock(
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
			return sjme_error_notImplemented(0);
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Grab the spin lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&state->spinLock)))
			return sjme_error_default(error);
		
		/* Obtain the buffer if we need to. */
		if (sjme_atomic_ga(sjme_jint, &state->count, 1) == 0)
			if (sjme_error_is(error = g->lock->lock(g)))
				return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_lockRelease(
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
			return sjme_error_notImplemented(0);
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Forward if release is needed. */
		if (sjme_atomic_ga(sjme_jint, &state->count, -1) <= 1)
		{
			sjme_atomic_s(sjme_jint, &state->count, 0);
			if (sjme_error_is(error = g->lock->lockRelease(g)))
				return sjme_error_default(error);
		}
		
		/* Release the spin lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&state->spinLock, NULL)))
			return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

/** Core pencil functions. */
static const sjme_scritchui_pencilFunctions sjme_scritchpen_core_functions =
{
	sjme_sm(.close, sjme_scritchpen_core_close),
	sjme_sm(.copyArea, sjme_scritchpen_core_copyArea),
	sjme_sm(.drawChar, sjme_scritchpen_core_drawChar),
	sjme_sm(.drawChars, sjme_scritchpen_core_drawChars),
	sjme_sm(.drawHoriz, sjme_scritchpen_core_drawHoriz),
	sjme_sm(.drawLine, sjme_scritchpen_core_drawLine),
	sjme_sm(.drawPixel, sjme_scritchpen_core_drawPixel),
	sjme_sm(.drawRect, sjme_scritchpen_core_drawRect),
	sjme_sm(.drawSubstring, sjme_scritchpen_core_drawSubstring),
	sjme_sm(.drawTriangle, sjme_scritchpen_core_drawTriangle),
	sjme_sm(.drawXRGB32Region, sjme_scritchpen_core_drawXRGB32Region),
	sjme_sm(.fillRect, sjme_scritchpen_core_fillRect),
	sjme_sm(.fillTriangle, sjme_scritchpen_core_fillTriangle),
	sjme_sm(.mapColor, sjme_scritchpen_core_mapColor),
	sjme_sm(.setAlphaColor, sjme_scritchpen_core_setAlphaColor),
	sjme_sm(.setBlendingMode, sjme_scritchpen_core_setBlendingMode),
	sjme_sm(.setClip, sjme_scritchpen_core_setClip),
	sjme_sm(.setDefaultFont, sjme_scritchpen_core_setDefaultFont),
	sjme_sm(.setDefaults, sjme_scritchpen_core_setDefaults),
	sjme_sm(.setFont, sjme_scritchpen_core_setFont),
	sjme_sm(.setParametersFrom, sjme_scritchpen_core_setParametersFrom),
	sjme_sm(.setStrokeStyle, sjme_scritchpen_core_setStrokeStyle),
	sjme_sm(.translate, sjme_scritchpen_core_translate),
};

/** Core pencil functions, serialized to the event thread. */
static const sjme_scritchui_pencilFunctions
	sjme_scritchpen_coreSerial_functions =
{
	sjme_sm(.close, sjme_scritchpen_core_close),
	sjme_sm(.copyArea, sjme_scritchpen_coreSerial_copyArea),
	sjme_sm(.drawChar, sjme_scritchpen_coreSerial_drawChar),
	sjme_sm(.drawChars, sjme_scritchpen_coreSerial_drawChars),
	sjme_sm(.drawHoriz, sjme_scritchpen_coreSerial_drawHoriz),
	sjme_sm(.drawLine, sjme_scritchpen_coreSerial_drawLine),
	sjme_sm(.drawPixel, sjme_scritchpen_coreSerial_drawPixel),
	sjme_sm(.drawRect, sjme_scritchpen_coreSerial_drawRect),
	sjme_sm(.drawSubstring, sjme_scritchpen_coreSerial_drawSubstring),
	sjme_sm(.drawTriangle, sjme_scritchpen_coreSerial_drawTriangle),
	sjme_sm(.drawXRGB32Region, sjme_scritchpen_coreSerial_drawXRGB32Region),
	sjme_sm(.fillRect, sjme_scritchpen_coreSerial_fillRect),
	sjme_sm(.fillTriangle, sjme_scritchpen_coreSerial_fillTriangle),
	sjme_sm(.mapColor, sjme_scritchpen_coreSerial_mapColor),
	sjme_sm(.setAlphaColor, sjme_scritchpen_coreSerial_setAlphaColor),
	sjme_sm(.setBlendingMode, sjme_scritchpen_coreSerial_setBlendingMode),
	sjme_sm(.setClip, sjme_scritchpen_coreSerial_setClip),
	sjme_sm(.setDefaultFont, sjme_scritchpen_coreSerial_setDefaultFont),
	sjme_sm(.setDefaults, sjme_scritchpen_coreSerial_setDefaults),
	sjme_sm(.setFont, sjme_scritchpen_coreSerial_setFont),
	sjme_sm(.setParametersFrom, sjme_scritchpen_coreSerial_setParametersFrom),
	sjme_sm(.setStrokeStyle, sjme_scritchpen_coreSerial_setStrokeStyle),
	sjme_sm(.translate, sjme_scritchpen_coreSerial_translate),
};

/** Utility functions. */
static const sjme_scritchui_pencilUtilFunctions
	sjme_scritchpen_coreUtil_functions =
{
	sjme_sm(.blendRGBInto, sjme_scritchpen_coreUtil_blendRGBInto),
	sjme_sm(.applyAnchor, sjme_scritchpen_coreUtil_applyAnchor),
	sjme_sm(.applyRotateScale, sjme_scritchpen_coreUtil_applyRotateScale),
	sjme_sm(.applyTranslate, sjme_scritchpen_coreUtil_applyTranslate),
	sjme_sm(.pfScanGet, sjme_scritchpen_coreUtil_pfScanGet),
	sjme_sm(.pfScanPut, sjme_scritchpen_coreUtil_pfScanPut),
	sjme_sm(.pfScanBits, sjme_scritchpen_coreUtil_pfScanBits),
	sjme_sm(.pfScanBytes, sjme_scritchpen_coreUtil_pfScanBytes),
	sjme_sm(.pfScanToPf, sjme_scritchpen_coreUtil_pfScanToPf),
	sjme_sm(.pfScanToRgb, sjme_scritchpen_coreUtil_pfScanToRgb),
	sjme_sm(.rgbScanFill, sjme_scritchpen_coreUtil_rgbScanFill),
	sjme_sm(.rgbScanGet, sjme_scritchpen_coreUtil_rgbScanGet),
	sjme_sm(.rgbScanPut, sjme_scritchpen_coreUtil_rgbScanPut),
	sjme_sm(.rgbScanToPf, sjme_scritchpen_coreUtil_rgbScanToPf),
	sjme_sm(.rgbScanToRaw, sjme_scritchpen_coreUtil_rgbScanToRaw),
	sjme_sm(.rawScanToRgb, sjme_scritchpen_coreUtil_rawScanToRgb),
};

sjme_errorCode sjme_scritchpen_initStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEndBindable* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui_pencilBase result;
	
	if (inPencil == NULL || inFunctions == NULL || defaultFont == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL) ||
		inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sw <= 0 || sh <= 0 || bw <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Raw scan putting is required at a minimum. */
	if (inFunctions->rawScanPutPure == NULL)
		return sjme_error_notImplemented(0);
	
	/* Locking functions which are required. */
	if (inLockFuncs != NULL)
		if (inLockFuncs->lock == NULL || inLockFuncs->lockRelease == NULL)
			return sjme_error_notImplemented(0);
		
	/* Setup base result. */
	memset(&result, 0, sizeof(result));
	result.common.type = SJME_SCRITCHUI_TYPE_PENCIL;
	result.common.state = inState;
	if (inFunctions->asyncSafe)
		result.api = &sjme_scritchpen_core_functions;
	else
		result.api = &sjme_scritchpen_coreSerial_functions;
	result.apiInThread = &sjme_scritchpen_core_functions;
	result.util = &sjme_scritchpen_coreUtil_functions;
	result.impl = inFunctions;
	result.lock = inLockFuncs;
	result.defaultFont = defaultFont;
	result.pixelFormat = pf;
	result.width = sw;
	result.height = sh;
	result.scanLenPixels = bw;
	result.forceTranslate.x = tx;
	result.forceTranslate.y = ty;
	
	/* Determine bits and bytes per pixel. */
	result.bitsPerPixel = -1;
	if (sjme_error_is(error = result.util->pfScanBits(&result, pf,
		1, -1,
		&result.bitsPerPixel, NULL)) ||
		result.bitsPerPixel <= 0)
		goto fail_determineBpp;
	result.bytesPerPixel = -1;
	if (sjme_error_is(error = result.util->pfScanBytes(&result, pf,
		1, -1,
		&result.bytesPerPixel, NULL)) ||
		result.bytesPerPixel <= 0)
		goto fail_determineBpp;
	
	/* Determine raw scan line length. */
	/* Note that the scanline length needs to be ceil() to a full byte. */
	result.scanLenBits = (sjme_jint)sjme_util_alignTo(
		result.scanLenPixels * result.bitsPerPixel, 8);
	result.scanLenBytes = result.scanLenBits / 8;

	/* Overflowed? */
	if (result.scanLenBits <= 0 || result.scanLenBytes <= 0)
		goto fail_determineBpp;
	
	/* Copy lock front end source? */
	if (inLockFuncs != NULL && inLockFrontEndCopy != NULL)
		sjme_frontEnd_copy(&result.lockState.source, inLockFrontEndCopy);
	
	/* Is there an alpha channel? */
	/* Note that alpha can only be supported if we can read the underlying */
	/* pixel data. */
	result.hasAlpha = (sjme_scritchpen_hasAlpha(pf) &&
		(result.impl->rawScanGet != NULL));
	
	/* Copy in front end? */
	if (copyFrontEnd != NULL)
		sjme_frontEnd_copy(&result.frontEnd, copyFrontEnd);
	
	/* Raw scan put, must be implemented always. */
	result.prim.rawScanPutPure = result.impl->rawScanPutPure;
	
	/* These are always handled by us unless supported by hardware. */
	result.prim.drawHoriz = sjme_scritchpen_corePrim_drawHoriz;
	result.prim.drawLine = sjme_scritchpen_corePrim_drawLine;
	result.prim.drawPixel = sjme_scritchpen_corePrim_drawPixel;
	
	/* Raw scan get. */
	if (result.impl->rawScanGet != NULL)
		result.prim.rawScanGet = result.impl->rawScanGet;
	else
		result.prim.rawScanGet = sjme_scritchpen_corePrim_rawScanGetNoDest;
	
	/* Color mapping. */
	if (result.impl->mapColor != NULL)
		result.prim.mapColor = result.impl->mapColor;
	else
		result.prim.mapColor = sjme_scritchpen_corePrim_mapColor;
	
	/* Basic filling of raw value. */
	if (result.bytesPerPixel == 4)
		result.prim.rawScanFill = sjme_scritchpen_corePrim_rawScanFillInt;
	else if (result.bytesPerPixel == 2)
		result.prim.rawScanFill = sjme_scritchpen_corePrim_rawScanFillShort;
	else
		result.prim.rawScanFill = sjme_scritchpen_corePrim_rawScanFillByte;
	
	/* Set defaults. */
	result.apiInThread->setDefaults(&result);
	
	/* Success! Copy back. */
	memmove(inPencil, &result, sizeof(result));
	return SJME_ERROR_NONE;

fail_determineBpp:
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy)
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
		return sjme_error_notImplemented(0);
	
	/* Get default font. */
	defaultFont = NULL;
	if (sjme_error_is(error = inState->api->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		return sjme_error_default(error);
	
	/* Forward to basic operations. */
	result = NULL;
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_scritchpen_initBuffer(
		inState, &result, &resultWeak,
		pf, bw, bh,
		inLockFuncs, inLockFrontEndCopy,
		0, 0,
		sx, sy, sw, sh,
		defaultFont, pencilFrontEndCopy)) ||
		result == NULL || resultWeak == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outPencil = result;
	if (outWeakPencil != NULL)
		*outWeakPencil = resultWeak;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_setDefaults(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Reset translation. */
	g->apiInThread->translate(g, -g->state.translate.x, -g->state.translate.y);
	
	/* Set initial state, ignore any errors. */
	g->apiInThread->setClip(g, 0, 0, g->width, g->height);
	g->apiInThread->setAlphaColor(g, 0xFF000000);
	g->apiInThread->setStrokeStyle(g,
		SJME_SCRITCHUI_PENCIL_STROKE_SOLID);
	g->apiInThread->setBlendingMode(g,
		SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	g->apiInThread->setDefaultFont(g);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
