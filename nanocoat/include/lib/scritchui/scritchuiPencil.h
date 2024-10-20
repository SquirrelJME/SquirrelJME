/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil Drawing.
 * 
 * @since 2024/05/01
 */

#ifndef SQUIRRELJME_SCRITCHUIPENCIL_H
#define SQUIRRELJME_SCRITCHUIPENCIL_H

#include "lib/scritchui/scritchui.h"
#include "sjme/charSeq.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The flags indicating the anchor point when rendering.
 * 
 * @since 2024/06/27
 */
typedef enum sjme_scritchui_pencilAnchor
{
	/** Horizontal center. */
	SJME_SCRITCHUI_ANCHOR_HCENTER = 1,
	
	/** Vertical center. */
	SJME_SCRITCHUI_ANCHOR_VCENTER = 2,
	
	/** Left. */
	SJME_SCRITCHUI_ANCHOR_LEFT = 4,
	
	/** Right. */
	SJME_SCRITCHUI_ANCHOR_RIGHT = 8,
	
	/** Top. */
	SJME_SCRITCHUI_ANCHOR_TOP = 16,
	
	/** Bottom. */
	SJME_SCRITCHUI_ANCHOR_BOTTOM = 32,
	
	/** Baseline. */
	SJME_SCRITCHUI_ANCHOR_BASELINE = 64,
} sjme_scritchui_pencilAnchor;

/** Mask for anchor points which are valid for text rendering. */
#define SJME_SCRITCHUI_ANCHOR_TEXT_MASK \
	((SJME_SCRITCHUI_ANCHOR_HCENTER | SJME_SCRITCHUI_ANCHOR_LEFT | \
	SJME_SCRITCHUI_ANCHOR_RIGHT | SJME_SCRITCHUI_ANCHOR_TOP | \
	SJME_SCRITCHUI_ANCHOR_BOTTOM | SJME_SCRITCHUI_ANCHOR_BASELINE))

/**
 * Translations which may be performed on images.
 * 
 * @since 2024/07/09
 */
typedef enum sjme_scritchui_pencilTranslate
{
	/** None. */
	SJME_SCRITCHUI_TRANS_NONE = 0,
	
	/** Mirror and rotate 180 degrees. */
	SJME_SCRITCHUI_TRANS_MIRROR_ROT180 = 1,
	
	/** Mirror. */
	SJME_SCRITCHUI_TRANS_MIRROR = 2,
	
	/** Rotate 180 degrees. */
	SJME_SCRITCHUI_TRANS_ROT180 = 3,
	
	/** Mirror and rotate 270 degrees. */ 
	SJME_SCRITCHUI_TRANS_MIRROR_ROT270 = 4,
	
	/** Rotate 90 degrees. */
	SJME_SCRITCHUI_TRANS_ROT90 = 5,
	
	/** Rotate 270 degrees. */
	SJME_SCRITCHUI_TRANS_ROT270 = 6,
	
	/** Mirror and rotate 90 degrees. */
	SJME_SCRITCHUI_TRANS_MIRROR_ROT90 = 7,
	
	/** The number of translations available. */
	SJME_SCRITCHUI_NUM_TRANS = 8,
} sjme_scritchui_pencilTranslate;

/**
 * The blending mode for a pencil.
 * 
 * @since 2024/05/06
 */
typedef enum sjme_scritchui_pencilBlendingMode
{
	/** Blend with source and multiply. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER,
	
	/** Use only the source alpha color. */
	SJME_SCRITCHUI_PENCIL_BLEND_SRC,
	
	/** The number of blending modes. */
	SJME_NUM_SCRITCHUI_PENCIL_BLENDS
} sjme_scritchui_pencilBlendingMode;

/**
 * Stroke style for lines.
 * 
 * @since 2024/05/06
 */
typedef enum sjme_scritchui_pencilStrokeMode
{
	/** Solid line. */
	SJME_SCRITCHUI_PENCIL_STROKE_SOLID,
	
	/** Dotted line. */
	SJME_SCRITCHUI_PENCIL_STROKE_DOTTED,
	
	/** The number of stroke modes. */
	SJME_NUM_SCRITCHUI_PENCIL_STROKES
} sjme_scritchui_pencilStrokeMode;

/**
 * Pencil drawing sub-translation matrix.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilMatrixSub
{
	/** Step for source X coordinate. */
	sjme_fixed wx;
	
	/** Step for source Y coordinate. */
	sjme_fixed zy;
} sjme_scritchui_pencilMatrixSub;

/**
 * Pencil drawing matrix, for any translations, rotations, and mirroring.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilMatrix
{
	/** Translation for input X coordinates. */
	sjme_scritchui_pencilMatrixSub x;
	
	/** Translation for input Y coordinates. */
	sjme_scritchui_pencilMatrixSub y;
	
	/** Target width after transformations. */
	sjme_jint tw;
	
	/** Target width after transformations. */
	sjme_jint th;
} sjme_scritchui_pencilMatrix;

/**
 * Represents the color of a pixel.
 * 
 * @since 2024/07/09
 */
typedef struct sjme_scritchui_pencilColor
{
	/** The raw pencil color, which is placed in the buffer. */
	sjme_jint v;
	
	/** The RGBA color. */
	sjme_jint argb;
	
	/** Red. */
	sjme_jubyte r;
	
	/** Green. */
	sjme_jubyte g;
	
	/** Blue. */
	sjme_jubyte b;
	
	/** Alpha. */
	sjme_jubyte a;
	
	/** Indexed color. */
	sjme_jchar i;
} sjme_scritchui_pencilColor;

/**
 * Applies an anchor point.
 * 
 * @param anchor The anchor point.
 * @param x The input X coordinate.
 * @param y The output Y coordinate.
 * @param w The width of the drawable.
 * @param h The height of the drawable.
 * @param baseline The baseline position of the font, if this is one. If
 * set to a negative value this will be considered invalid to use.
 * @param outX The resultant X coordinate.
 * @param outY The resultant Y coordinate.
 * @return Any error, if applicable.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyAnchorFunc)(
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint baseline,
	sjme_attrOutNotNull sjme_jint* outX,
	sjme_attrOutNotNull sjme_jint* outY);

/**
 * Applies rotation and scaling.
 * 
 * @param outMatrix The resultant matrix.
 * @param inTrans The translation to use.
 * @param wSrc The source width.
 * @param hSrc The source height.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @return Any resultant error, if any.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyRotateScaleFunc)(
	sjme_attrOutNotNull sjme_scritchui_pencilMatrix* outMatrix,
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest);

/**
 * Applies translation to the given coordinates.
 * 
 * @param g The graphics to apply from.
 * @param x The X coordinate to translate.
 * @param y The Y coordinate to translate.
 * @return Any resultant error, if any.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilApplyTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y);

/**
 * This blends RGB data from a source buffer into the target buffer.
 * 
 * @param g The graphics to operate under.
 * @param destAlpha Does the destination utilize an alpha channel?
 * @param srcAlpha Does the source utilize an alpha channel?
 * @param mulAlpha Should alpha values be multiplied?
 * @param mulAlphaValue The alpha value to multiply with.
 * @param dest The destination buffer which is written over.
 * @param src The source buffer.
 * @param numPixels The number of pixels to blend.
 * @return Any resultant error, if any.
 * @since 2024/07/11
 */
typedef sjme_errorCode (*sjme_scritchui_pencilBlendRGBIntoFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean destAlpha,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) const sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels);

/**
 * This copies one region of the image to another region.
 *
 * Copying to a display device is not permitted because it may impact how
 * double buffering is implemented, as such it is not supported.
 *
 * Pixels are copied directly and no alpha compositing is performed.
 *
 * If the source and destination overlap then it must be as if they did not
 * overlap at all, this means that the destination will be an exact copy of
 * the source.
 *
 * @param g The hardware graphics to draw with.
 * @param sx The source X position, will be translated.
 * @param sy The source Y position, will be translated.
 * @param w The width to copy.
 * @param h The height to copy.
 * @param dx The destination X position, will be translated.
 * @param dy The destination Y position, will be translated.
 * @param anchor The anchor point of the destination.
 * @return An error if the call is not valid or the native graphics
 * does not support this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilCopyAreaFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws the given character.
 *
 * @param g The hardware graphics to draw with.
 * @param c The codepoint to draw.
 * @param x The X position.
 * @param y The Y position.
 * @param anchor The anchor point.
 * @param outCw The output codepoint width, this is optional.
 * @return An error if the graphics is not valid, does not support
 * the given operation, or if the anchor point is not valid.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw);

/**
 * Draws the given characters.
 *
 * @param g The hardware graphics to draw with.
 * @param s The characters to draw.
 * @param o The offset into the buffer.
 * @param l The number of characters to draw.
 * @param x The X position.
 * @param y The Y position.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, does not support
 * the given operation, if the anchor point is not valid, or if the
 * offset and/or length are out of bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a horizontal line in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The starting X coordinate.
 * @param y The starting Y coordinate.
 * @param w The width of the line.
 * @return An error on null arguments.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawHorizFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w);

/**
 * Draws a line in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x1 The starting X coordinate.
 * @param y1 The starting Y coordinate.
 * @param x2 The ending X coordinate.
 * @param y2 The ending Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

/**
 * Draws a single pixel in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The starting X coordinate.
 * @param y The starting Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawPixelFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);
	
/**
 * Draws the outline of the given rectangle using the current color and
 * stroke style. The rectangle will cover an area that
 * is @code \[width + 1, height + 1\] @endcode .
 *
 * Nothing is drawn if the width and/or height are zero.
 *
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error if the graphics is not valid or does not support
 * the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Draws the given substring.
 *
 * @param g The hardware graphics to draw with.
 * @param s The string to draw.
 * @param o The offset into the string.
 * @param l The offset into the length.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, this operation is
 * not supported, or on null arguments, or if the offset and/or length are
 * negative or exceed the string bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawSubstringFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq* s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a triangle using the current color.
 *
 * @param g The graphics to use for drawing.
 * @param x1 First X coordinate.
 * @param y1 First Y coordinate.
 * @param x2 Second X coordinate.
 * @param y2 Second Y coordinate.
 * @param x3 Third X coordinate.
 * @param y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

/**
 * Draws a region of 32-bit RGB data into the target.
 *
 * @param g The hardware graphics to draw with.
 * @param data The source buffer.
 * @param off The offset into the buffer.
 * @param dataLen The total length of the data buffer.
 * @param scanLen The scanline length.
 * @param alpha Drawing with the alpha channel?
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param trans Sprite translation and/or rotation,
 * see @c javax.microedition.lcdui.game.Sprite.
 * @param xDest The destination X position, is translated.
 * @param yDest The destination Y position, is translated.
 * @param anchor The anchor point.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @param origImgWidth Original image width.
 * @param origImgHeight Original image height.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawXRGB32RegionFunc)(
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

/**
 * Performs rectangular fill in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Draws a filled triangle using the current color, the lines which make
 * up the triangle are included in the filled area.
 *
 * @param g The graphics to use for drawing.
 * @param x1 First X coordinate.
 * @param y1 First Y coordinate.
 * @param x2 Second X coordinate.
 * @param y2 Second Y coordinate.
 * @param x3 Third X coordinate.
 * @param y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

/**
 * Locks the pencil for drawing.
 * 
 * @param g The pencil to lock.
 * @return Any resultant error, if any.
 * @since 2024/07/08
 */
typedef sjme_errorCode (*sjme_scritchui_pencilLockFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Releases the pencil drawing lock.
 * 
 * @param g The pencil to unlock.
 * @return Any resultant error, if any.
 * @since 2024/07/08
 */
typedef sjme_errorCode (*sjme_scritchui_pencilLockReleaseFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Maps a color to or from a raw color.
 * 
 * @param g The pencil to operate within.
 * @param fromRaw If @c SJME_JNI_TRUE the input color is considered to be
 * a raw pixel.
 * @param inRgbOrRaw The input value to map.
 * @param outColor The resultant full color set.
 * @return Any resultant error.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilMapColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor);

/**
 * Maps a the number of bytes needed to represent the specified number of
 * pixels in the raw buffer.
 * 
 * @param g The pencil to operate with.
 * @param inPixel The number of pixels to map.
 * @param inBytes The number of bytes to use for limit calculation.
 * @param outBytes The number of bytes used to represent the raw pixel data.
 * @param outLimit Optional output value for the smaller of @c outBytes
 * and @c inBytes .
 * @return Any resultant error.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanBytesFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrInPositiveNonZero sjme_jint inBytes,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes,
	sjme_attrOutNullable sjme_attrOutPositiveNonZero sjme_jint* outLimit);

/**
 * Fills a buffer with the given value. 
 * 
 * @param g The graphics owning this.
 * @param outRaw The output raw scan buffer.
 * @param outRawOff Offset into the raw scan buffer.
 * @param outRawLen Length of the raw scan buffer.
 * @param rawPixel The raw pixel to fill with.
 * @param inNumPixels The number of pixels to fill.
 * @return Any resultant error code.
 * @since 2024/07/10
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanFillFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) sjme_pointer outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Reads raw data from a single scanline at the given position. 
 * 
 * @param g The graphics to read from.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param outData The resultant pixel data.
 * @param inDataLen Length of the data buffer.
 * @param inNumPixels The number of pixels to read.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanGetFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Writes raw data to a single scanline at the given position, no alpha
 * blending is performed. 
 * 
 * @param g The graphics to write to.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param srcRaw The raw pixel data to write.
 * @param srcRawLen Length of the data buffer.
 * @param srcNumPixels The number of source pixels.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanPutPureFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen,
	sjme_attrInPositiveNonZero sjme_jint srcNumPixels);

/**
 * Maps a raw scanline from raw RGB data.
 * 
 * @param g The graphics to operate within.
 * @param outRgb The output RGB data.
 * @param outRgbOff The offset into the RGB buffer.
 * @param outRgbLen The length of the RGB buffer.
 * @param inRaw The input raw scan buffer.
 * @param inRawOff Offset into the raw scan buffer.
 * @param inRawLen Length of the raw scan buffer.
 * @return Any resultant error, if any.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRawScanToRgbFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(outRgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(inRawLen) sjme_cpointer inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen);

/**
 * Fills a scan with the given RGB value. 
 * 
 * @param g The graphics owning this.
 * @param outRgb The output RGB scan buffer.
 * @param outRgbOff Offset into the RGB scan buffer.
 * @param outRawLen Length of the RGB scan buffer.
 * @param inNumPixels The number of pixels to fill.
 * @param inValue The raw pixel to fill with.
 * @return Any resultant error code.
 * @since 2024/07/10
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanFillFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(inNumPixels) sjme_jint* outRgb,
	sjme_attrInPositiveNonZero sjme_jint outRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jint inValue);

/**
 * Reads raw data from a single scanline at the given position. 
 * 
 * @param g The graphics to read from.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param destRgb The RGB data that has come from the image.
 * @param inNumPixels The number of pixels to read.
 * @return Any resultant error code.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanGetFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_jint* destRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels);

/**
 * Writes raw data to a single scanline at the given position. 
 * 
 * @param g The graphics to write to.
 * @param x The X coordinate to access.
 * @param y The Y coordinate to access.
 * @param srcRgb Source RGB data to write.
 * @param inNumPixels The number of pixels to read.
 * @param srcAlpha Does the source data have an alpha channel?
 * @param mulAlpha Should alpha values in the input buffer be multiplied by
 * the current alpha value? That is the buffer has significant alpha values.
 * @param mulAlphaValue The value to multiply with.
 * @return Any resultant error code.
 * @since 2024/07/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbScanPutFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) const sjme_jint* srcRgb,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInValue sjme_jboolean mulAlpha,
	sjme_attrInRange(0, 255) sjme_jint mulAlphaValue);

/**
 * Maps a raw scanline from raw RGB data.
 * 
 * @param g The graphics to operate within.
 * @param outRaw The output raw scan buffer.
 * @param outRawOff Offset into the raw scan buffer.
 * @param outRawLen Length of the raw scan buffer.
 * @param inRgb The input RGB data.
 * @param inRgbOff The offset into the RGB buffer.
 * @param inRgbLen The length of the RGB buffer.
 * @return Any resultant error, if any.
 * @since 2024/07/09
 */
typedef sjme_errorCode (*sjme_scritchui_pencilRgbToRawScanFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) sjme_pointer outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) const sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositive sjme_jint inRgbLen);

/**
 * Sets the alpha color for graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param argb The color to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetAlphaColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb);

/**
 * Sets the blending mode to use.
 * 
 * @param g The hardware graphics to draw with.
 * @param mode The blending mode to use.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetBlendingModeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode);

/**
 * Sets the clipping rectangle position.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetClipFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Sets that the graphics should now use the default font.
 * 
 * @param g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Sets the default for the pencil.
 * 
 * @param g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/08/13
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Sets to use the specified font.
 *
 * @param g The graphics used.
 * @param name The font name.
 * @param style The style of the font.
 * @param pixelSize The pixel size of the font.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font);

/**
 * Copies the parameters from the other pencil to this one.
 *
 * @param g The graphics used.
 * @param from The pencil to copy the parameters from.
 * @return An error if the graphics is not valid.
 * @since 2024/07/26
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetParametersFromFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from);

/**
 * Sets the stroke style for the hardware graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param style The stroke type to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetStrokeStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style);

/**
 * Translates drawing operations.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X translation.
 * @param y The Y translation.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_PENCIL(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencil, what, Func) lWhat

/**
 * ScritchUI Pencil API functions.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilFunctions
{
	/** @c CopyArea . */
	SJME_SCRITCHUI_QUICK_PENCIL(CopyArea, copyArea);
	
	/** @c DrawChar . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawChar, drawChar);
	
	/** @c DrawChars . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawChars, drawChars);
	
	/** @c DrawHoriz . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHoriz);
	
	/** @c DrawLine . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLine);
	
	/** @c DrawRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRect, drawRect);
	
	/** @c DrawPixel . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixel);
	
	/** @c DrawSubstring . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawSubstring, drawSubstring);
	
	/** @c DrawTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawTriangle, drawTriangle);
	
	/** @c DrawXRGB32Region . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawXRGB32Region, drawXRGB32Region);
	
	/** @c FillRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillRect, fillRect);
	
	/** @c FillTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillTriangle, fillTriangle);
	
	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c SetAlphaColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetAlphaColor, setAlphaColor);
	
	/** @c SetBlendingMode . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetBlendingMode, setBlendingMode);
	
	/** @c SetClip . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetClip, setClip);
	
	/** @c SetDefaultFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetDefaultFont, setDefaultFont);
	
	/** @c SetDefaults . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetDefaults, setDefaults);
	
	/** @c SetFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetFont, setFont);
	
	/** @c SetParametersFrom . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetParametersFrom, setParametersFrom);
	
	/** @c SetStrokeStyle . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetStrokeStyle, setStrokeStyle);
	
	/** @c Translate . */
	SJME_SCRITCHUI_QUICK_PENCIL(Translate, translate);
} sjme_scritchui_pencilFunctions;

/**
 * Lowest level drawing primitives, note that none of these
 * accept transformations however they may accept clipping.
 * 
 * @since 2024/05/17
 */
typedef struct sjme_scritchui_pencilPrimFunctions
{
	/** @c DrawHoriz . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHoriz);
	
	/** @c DrawLine . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLine);
	
	/** @c DrawPixel . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixel);
	
	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c RawScanFill. */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanFill, rawScanFill);
	
	/** @c RawScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanGet, rawScanGet);
	
	/** @c RawScanPut without any alpha blending . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanPutPure, rawScanPutPure);
} sjme_scritchui_pencilPrimFunctions;

struct sjme_scritchui_pencilLockFunctions
{
	/** @c Lock . */
	SJME_SCRITCHUI_QUICK_PENCIL(Lock, lock);
	
	/** @c LockRelease . */
	SJME_SCRITCHUI_QUICK_PENCIL(LockRelease, lockRelease);
};

struct sjme_scritchui_pencilUtilFunctions
{
	/** @c ApplyAnchor . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyAnchor, applyAnchor);
	
	/** @c ApplyRotateScale . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyRotateScale, applyRotateScale);
	
	/** @c ApplyTranslate . */
	SJME_SCRITCHUI_QUICK_PENCIL(ApplyTranslate, applyTranslate);
	
	/** @c BlendRGBInto . */
	SJME_SCRITCHUI_QUICK_PENCIL(BlendRGBInto, blendRGBInto);
	
	/** @c RawScanBytes . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanBytes, rawScanBytes);
	
	/** @c RawScanToRgb . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanToRgb, rawScanToRgb);
	
	/** @c RgbScanFill . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanFill, rgbScanFill);
	
	/** @c RgbScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanGet, rgbScanGet);
	
	/** @c RgbScanPut . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbScanPut, rgbScanPut);
	
	/** @c RgbToRawScan . */
	SJME_SCRITCHUI_QUICK_PENCIL(RgbToRawScan, rgbToRawScan);
};

/**
 * ScritchUI Pencil implementation functions, note that none of these
 * accept transformations however they may accept clipping.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilImplFunctions
{
	/** @c CopyArea . */
	SJME_SCRITCHUI_QUICK_PENCIL(CopyArea, copyArea);
	
	/** @c DrawHoriz , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHorizSrc);
	
	/** @c DrawHoriz , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawHoriz, drawHorizSrcOver);
	
	/** @c DrawLine , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLineSrc);
	
	/** @c DrawLine , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine, drawLineSrcOver);
	
	/** @c DrawPixel , direct source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixelSrc);
	
	/** @c DrawPixel , Over source. */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawPixel, drawPixelSrcOver);
	
	/** @c MapColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(MapColor, mapColor);
	
	/** @c RawScanGet . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanGet, rawScanGet);
	
	/** @c RawScanPutPure , to place without blending . */
	SJME_SCRITCHUI_QUICK_PENCIL(RawScanPutPure, rawScanPutPure);
	
	/** @c SetAlphaColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetAlphaColor, setAlphaColor);
	
	/** @c SetBlendingMode . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetBlendingMode, setBlendingMode);
	
	/** @c SetClip . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetClip, setClip);
	
	/** @c SetStrokeStyle . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetStrokeStyle, setStrokeStyle);
} sjme_scritchui_pencilImplFunctions;

#undef SJME_SCRITCHUI_QUICK_PENCIL

/**
 * Function used for bit-line drawing operations.
 * 
 * @param g The pencil to draw onto.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilBitLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);
	
/** The bit-line functions which are available. */
extern const sjme_scritchui_pencilBitLineFunc
	sjme_scritchui_pencilBitLines[256];

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inState The state this is under.
 * @param outPencil The resultant pencil.
 * @param outWeakPencil The output weak reference to the pencil.
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
sjme_errorCode sjme_scritchpen_initBuffer(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param inOutPencil The input and output pencil.
 * @param inState The input ScritchUI state.
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param inLockFuncs The locking functions to use for buffer access.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
sjme_errorCode sjme_scritchpen_initBufferStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inOutPencil,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Static pencil function initialization.
 * 
 * @param inPencil The pencil to be initialized.
 * @param inState The input ScritchUI state.
 * @param inFunctions The functions to set.
 * @param inLockFuncs Functions for native locking.
 * @param inLockFrontEndCopy Front end copy data for locks.
 * @param pf The pixel format used.
 * @param tx Forced X translate.
 * @param ty Forced Y translate.
 * @param sw The surface width.
 * @param sh The surface height.
 * @param bw The buffer width, the scanline length.
 * @param defaultFont The default font to use.
 * @param copyFrontEnd Optional front end data to copy.
 * @return Any error code if applicable.
 * @since 2024/05/04
 */
sjme_errorCode sjme_scritchpen_initStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint tx,
	sjme_attrInValue sjme_jint ty,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd);

/**
 * Represents a single point.
 * 
 * @since 2024/05/17
 */
typedef struct sjme_scritchui_pencilPoint
{
	/** The X coordinate. */
	sjme_jint x;
	
	/** The Y coordinate. */
	sjme_jint y;
} sjme_scritchui_pencilPoint;

/**
 * Pencil drawing state, such as colors or otherwise.
 * 
 * @since 2024/05/04
 */
typedef struct sjme_scritchui_pencilState
{
	/** The current color used. */
	sjme_scritchui_pencilColor color;
	
	/** The style for strokes. */
	sjme_scritchui_pencilStrokeMode stroke;
	
	/** Blending mode for lines. */
	sjme_scritchui_pencilBlendingMode blending;
	
	/** The font used for text. */
	sjme_scritchui_pencilFont font;
	
	/** Transformation coordinates. */
	sjme_scritchui_pencilPoint translate;
	
	/** The real transformation coordinates, after adjustment. */
	sjme_scritchui_pencilPoint translateReal;
	
	/** The clipping region. */
	sjme_scritchui_rect clip;
	
	/** Clip coordinates. */
	sjme_scritchui_line clipLine;
	
	/** Is blending applicable? */
	sjme_jboolean applyAlpha;
} sjme_scritchui_pencilState;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIPENCIL_H */
