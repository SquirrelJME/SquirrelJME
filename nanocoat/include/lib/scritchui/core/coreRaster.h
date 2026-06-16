/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Prototypes for core raster functions.
 * 
 * @since 2024/07/12
 */

#ifndef SJME_C_CORERASTER_H
#define SJME_C_CORERASTER_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_CORERASTER_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#pragma region(corePrimitives)
	
sjme_errorCode sjme_scritchpen_corePrim_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_corePrim_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w);

sjme_errorCode sjme_scritchpen_corePrim_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

sjme_errorCode sjme_scritchpen_corePrim_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

sjme_errorCode sjme_scritchpen_corePrim_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_corePrim_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_corePrim_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints,
	sjme_attrInValue sjme_jboolean safePoints);

sjme_errorCode sjme_scritchpen_corePrim_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);
	
sjme_errorCode sjme_scritchpen_corePrim_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);
	
sjme_errorCode sjme_scritchpen_corePrim_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);
	
sjme_errorCode sjme_scritchpen_corePrim_mapColorPfToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRGB(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRaw(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);
	
sjme_errorCode sjme_scritchpen_corePrim_mapColorRgbToPf(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint argb,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

sjme_errorCode sjme_scritchpen_corePrim_rawScanFillInt(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_corePrim_rawScanFillShort(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_corePrim_rawScanFillByte(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_corePrim_rawScanGetNoDest(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

#pragma endregion(corePrimitives)
	
/*--------------------------------------------------------------------------*/

#pragma region(coreUtils)
	
sjme_errorCode sjme_scritchpen_coreUtil_applyAnchor(
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint baseline,
	sjme_attrOutNotNull sjme_jint* outX,
	sjme_attrOutNotNull sjme_jint* outY);

sjme_errorCode sjme_scritchpen_coreUtil_applyCoordinateAdj(
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y,
	sjme_attrInOutNotNull sjme_jint* w,
	sjme_attrInOutNotNull sjme_jint* h,
	sjme_attrInPositive sjme_jint dataWidth,
	sjme_attrInPositive sjme_jint dataHeight);

sjme_errorCode sjme_scritchpen_coreUtil_applyRotateScale(
	sjme_attrInOutNotNull sjme_scritchui_matrix* adjMatrix,
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest);
	
sjme_errorCode sjme_scritchpen_coreUtil_applyTranslate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y);

sjme_errorCode sjme_scritchpen_coreUtil_blendRGBInto(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) const sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels);

sjme_errorCode sjme_scritchpen_coreUtil_pfScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inDataLen) sjme_pointer dest,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_coreUtil_pfScanPut(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer src,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue);
	
sjme_errorCode sjme_scritchpen_coreUtil_pfScanBits(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBits,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBits,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit);

sjme_errorCode sjme_scritchpen_coreUtil_pfScanBytes(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBytes,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit);
	
sjme_errorCode sjme_scritchpen_coreUtil_pfScanToPf(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat destPf,
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInPositive sjme_jint destRawOff,
	sjme_attrInNegativeOnePositive sjme_jint destRawLen,
	sjme_attrInValue sjme_gfx_pixelFormat srcPf,
	sjme_attrInNotNull sjme_cpointer src,
	sjme_attrInPositive sjme_jint srcRawOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRawLen,
	sjme_attrInPositive sjme_jint inNumPixels);

sjme_errorCode sjme_scritchpen_coreUtil_pfScanToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* destRgb,
	sjme_attrInPositive sjme_jint destRgbOff,
	sjme_attrInNegativeOnePositive sjme_jint destRgbLen,
	sjme_attrInValue sjme_gfx_pixelFormat srcPf,
	sjme_attrInNotNull sjme_pointer src,
	sjme_attrInPositive sjme_jint srcRawOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRawLen,
	sjme_attrInPositive sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_coreUtil_rgbScanFill(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(inNumPixels) sjme_jint* outRgb,
	sjme_attrInPositiveNonZero sjme_jint outRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jint inValue);

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_jint* destRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanPut(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const sjme_jint* srcRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue);

sjme_errorCode sjme_scritchpen_coreUtil_rgbScanToPf(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat destPf,
	sjme_attrInNotNull sjme_pointer dest,
	sjme_attrInPositive sjme_jint destRawOff,
	sjme_attrInNegativeOnePositive sjme_jint destRawLen,
	sjme_attrInNotNull const sjme_jint* srcRgb,
	sjme_attrInPositive sjme_jint srcRgbOff,
	sjme_attrInNegativeOnePositive sjme_jint srcRgbLen,
	sjme_attrInPositive sjme_jint inNumPixels);
	
sjme_errorCode sjme_scritchpen_coreUtil_rgbScanToRaw(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) const sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositive sjme_jint inRgbLen);
	
sjme_errorCode sjme_scritchpen_coreUtil_rawScanToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(outRgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(inRawLen) sjme_cpointer inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen);

#pragma endregion(coreUtils)
	
/*--------------------------------------------------------------------------*/
	
#pragma region(coreImplementations)
	
sjme_errorCode sjme_scritchpen_core_lock(
	sjme_attrInNotNull sjme_scritchui_pencil g);

sjme_errorCode sjme_scritchpen_core_lockRelease(
	sjme_attrInNotNull sjme_scritchui_pencil g);

sjme_errorCode sjme_scritchpen_core_close(
	sjme_attrInNotNull sjme_scritchui_pencil g);
	
sjme_errorCode sjme_attrDeprecated sjme_scritchpen_core_copyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_core_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_core_drawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw);

sjme_errorCode sjme_scritchpen_core_drawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_core_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w);

sjme_errorCode sjme_scritchpen_core_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

sjme_errorCode sjme_scritchpen_core_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

sjme_errorCode sjme_scritchpen_core_drawPolyline(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);

sjme_errorCode sjme_scritchpen_core_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

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
	sjme_attrInPositive sjme_jint origImgHeight);

sjme_errorCode sjme_scritchpen_core_drawRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);
	
sjme_errorCode sjme_scritchpen_core_drawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_core_drawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

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
	sjme_attrInPositive sjme_jint origImgHeight);

sjme_errorCode sjme_scritchpen_core_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle);

sjme_errorCode sjme_scritchpen_core_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints);

sjme_errorCode sjme_scritchpen_core_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_core_fillRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight);

sjme_errorCode sjme_scritchpen_core_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

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
	sjme_attrInValue sjme_jint anchor);

sjme_errorCode sjme_scritchpen_core_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor);

sjme_errorCode sjme_scritchpen_core_setAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb);

sjme_errorCode sjme_scritchpen_core_setBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode);

sjme_errorCode sjme_scritchpen_core_setClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

sjme_errorCode sjme_scritchpen_core_setDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g);
	
sjme_errorCode sjme_scritchpen_core_setDefaults(
	sjme_attrInNotNull sjme_scritchui_pencil g);
	
sjme_errorCode sjme_scritchpen_core_setStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style);
	
sjme_errorCode sjme_scritchpen_core_setFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* params);

sjme_errorCode sjme_scritchpen_core_setParametersFrom(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from);
	
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
	sjme_attrInValue sjme_scritchui_transferRegionMode mode);

sjme_errorCode sjme_scritchpen_core_translate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);
	
#pragma endregion(coreImplementations)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_CORERASTER_H
}
		#undef SJME_CXX_SQUIRRELJME_CORERASTER_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_CORERASTER_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_CORERASTER_H */
