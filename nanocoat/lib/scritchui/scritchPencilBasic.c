/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "sjme/alloc.h"

static sjme_errorCode sjme_scritchui_basicRawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_errorCode error;
	sjme_jint pixelBytes, limit;
	void* targetP;
	
	if (g == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (x < 0 || y < 0 || x >= g->width || y >= g->height ||
		inDataLen < 0 || inNumPixels < 0 ||
		(x + inNumPixels) < 0 || (x + inNumPixels) > g->width)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Buffer not locked? */
	if (g->lockState.base == NULL)
		return SJME_ERROR_BUFFER_NOT_LOCKED;
	
	/* Determine the number of pixels to be drawn. */
	pixelBytes = -1;
	limit = -1;
	if (sjme_error_is(error = g->util->rawScanBytes(g,
		inNumPixels, inDataLen,
		&pixelBytes, &limit)) ||
		pixelBytes < 0 || limit < 0)
		return sjme_error_default(error);
	
	/* Nothing to do? */
	if (limit == 0)
		return SJME_ERROR_NONE;
	
	/* Direct memory copy over. */
	targetP = SJME_POINTER_OFFSET(g->lockState.base,
		(y * g->scanLenBytes) + ((x * g->bitsPerPixel) / 8));
	memmove(outData, targetP, limit);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_basicRawScanPutPure(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const void* srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen)
{
	sjme_errorCode error;
	sjme_jint pixelBytes, limit;
	sjme_jint targetI;
	void* targetP;
	
	if (g == NULL || srcRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Buffer not locked? */
	if (g->lockState.base == NULL)
		return SJME_ERROR_BUFFER_NOT_LOCKED;
	
	/* Outside of image bounds? */
	targetI = (y * g->scanLenBytes) + ((x * g->bitsPerPixel) / 8);
	if (x < 0 || y < 0 || x >= g->width || y >= g->height ||
		targetI < 0 || targetI > g->lockState.baseLimitBytes ||
		(targetI + srcRawLen) < 0 ||
		(targetI + srcRawLen) > g->lockState.baseLimitBytes)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Copy over the buffer directly. */
	targetP = SJME_POINTER_OFFSET(g->lockState.base, targetI);
	memmove(targetP, srcRaw, srcRawLen);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_basicRawScanGet_sjme_jbyte4(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_basicRawScanPutPure_sjme_jbyte4(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const void* srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen)
{
	if (g == NULL || srcRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_basicRawScanGet_sjme_jbyte2(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_basicRawScanPutPure_sjme_jbyte2(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const void* srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen)
{
	if (g == NULL || srcRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_basicRawScanGet_sjme_jbyte1(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_basicRawScanPutPure_sjme_jbyte1(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const void* srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen)
{
	if (g == NULL || srcRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

/* Integer. */
#define pencilPixelType sjme_jint
#define pencilPixelBits 32
#define pencilPixelMask 0xFFFFFFFF
#define pencilRawScanCopy

#include "scritchPencilTemplate.c"

/* Short. */
#define pencilPixelType sjme_jshort
#define pencilPixelBits 16
#define pencilPixelMask 0xFFFF
#define pencilRawScanCopy

#include "scritchPencilTemplate.c"

/* Byte. */
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 8
#define pencilPixelMask 0xFF
#define pencilRawScanCopy

#include "scritchPencilTemplate.c"

/* Packed 16 colors (4-bit). (packed @c uint8_t ) */
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 4
#define pencilPixelMask 0xF

#include "scritchPencilTemplate.c"

/* Packed 4 Colors (2-bit). (packed @c uint8_t ) */
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 2
#define pencilPixelMask 0x3

#include "scritchPencilTemplate.c"

/* Packed 2 colors (1-bit). (packed @c uint8_t ) */
#define pencilPixelType sjme_jbyte
#define pencilPixelBits 1
#define pencilPixelMask 0x1

#include "scritchPencilTemplate.c"

sjme_errorCode sjme_scritchpen_initBuffer(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
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
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui_pencil result;
	sjme_alloc_weak resultWeak;
	
	if (inPool == NULL || outPencil == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate pencil. */
	result = NULL;
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), NULL, NULL, &result, &resultWeak)) ||
		result == NULL || resultWeak == NULL)
		return sjme_error_default(error);
	
	/* Initialize it. */
	if (sjme_error_is(error = sjme_scritchpen_initBufferStatic(
		result, pf, bw, bh, inLockFuncs, inLockFrontEndCopy,
		sx, sy, sw, sh, defaultFont, copyFrontEnd)))
	{
		/* Free before failing. */
		sjme_alloc_free(result);
		
		/* Then fail. */
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outPencil = result;
	if (outWeakPencil != NULL)
		*outWeakPencil = resultWeak;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_initBufferStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inOutPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	const sjme_scritchui_pencilImplFunctions* chosen;
	
	if (inOutPencil == NULL || inLockFuncs == NULL || defaultFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bw <= 0 || bh <= 0 || sx < 0 || sy < 0 || sw <= 0 || sh <= 0 ||
		sx >= sw || sy >= sh || sx >= bw || sy >= bh ||
		sw > bw || sh > bh || (sx + sw) > bw || (sy + sh) > bh ||
		(sx + sw) < 0 || (sy + sh) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Which drawing operations to use? */
	switch (pf)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			chosen = &sjme_scritchui_basic__sjme_jint32;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			chosen = &sjme_scritchui_basic__sjme_jshort16;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			chosen = &sjme_scritchui_basic__sjme_jbyte8;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			chosen = &sjme_scritchui_basic__sjme_jbyte4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			chosen = &sjme_scritchui_basic__sjme_jbyte2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			chosen = &sjme_scritchui_basic__sjme_jbyte1;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Forward. */
	return sjme_scritchpen_initStatic(inOutPencil,
		chosen, inLockFuncs, inLockFrontEndCopy, pf,
		sw, sh, bw, defaultFont, copyFrontEnd);
}
