// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadParameter;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This shelf is responsible for accelerated graphics drawing.
 *
 * @see PencilBracket
 * @since 2020/09/25
 */
@Exported
public final class PencilShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/09/25
	 */
	@Exported
	private PencilShelf()
	{
	}
	
	/**
	 * Returns the capabilities of the native possibly hardware accelerated
	 * pencil graphics drawing for the given pixel format.
	 * 
	 * @param __pf The {@link UIPixelFormat} being used for drawing.
	 * @throws MLECallError If the pixel format is not valid.
	 * @return The capabilities, will be the bit-field of
	 * {@link PencilCapabilities}. If there is not capability for this format
	 * then {@code 0} will be returned.
	 * @since 2020/09/25
	 */
	@Exported
	public static native int capabilities(int __pf)
		throws MLECallError;
	
	/**
	 * Draws a line in hardware.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x1 The starting X coordinate.
	 * @param __y1 The starting Y coordinate.
	 * @param __x2 The ending X coordinate.
	 * @param __y2 The ending Y coordinate.
	 * @throws MLECallError On null arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareDrawLine(PencilBracket __g,
		int __x1, int __y1, int __x2, int __y2)
		throws MLECallError;
	
	/**
	 * Draws a region of 32-bit RGB data into the target.
	 * 
	 * @param __data The source buffer.
	 * @param __off The offset into the buffer.
	 * @param __scanLen The scanline length.
	 * @param __alpha Drawing with the alpha channel?
	 * @param __xSrc The source X position.
	 * @param __ySrc The source Y position.
	 * @param __wSrc The width of the source region.
	 * @param __hSrc The height of the source region.
	 * @param __trans Sprite translation and/or rotation, see
	 * {@code javax.microedition.lcdui.game.Sprite}.
	 * @param __xDest The destination X position, is translated.
	 * @param __yDest The destination Y position, is translated.
	 * @param __anch The anchor point.
	 * @param __wDest The destination width.
	 * @param __hDest The destination height.
	 * @param __origImgWidth Original image width.
	 * @param __origImgHeight Original image height.
	 * @throws MLECallError On null arguments.
	 * @since 2022/01/26
	 */
	@Exported
	public static native void hardwareDrawXRGB32Region(
		PencilBracket __hardware, int[] __data, int __off, int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc, int __wSrc, int __hSrc,
		int __trans, int __xDest, int __yDest, int __anch, int __wDest,
		int __hDest, int __origImgWidth, int __origImgHeight)
		throws MLECallError;
	
	/**
	 * Performs rectangular fill in hardware.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareFillRect(PencilBracket __g,
		int __x, int __y, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Creates a hardware reference bracket to the native hardware graphics.
	 * 
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @throws MLECallError If the requested graphics are not valid.
	 * @since 2020/09/25
	 */
	@Exported
	public static native PencilBracket hardwareGraphics(int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh)
		throws MLECallError;
	
	/**
	 * Sets the alpha color for graphics.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __argb The color to set.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareSetAlphaColor(PencilBracket __g,
		int __argb)
		throws MLECallError;
	
	/**
	 * Sets the blending mode to use.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __mode The blending mode to use.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareSetBlendingMode(PencilBracket __g,
		int __mode)
		throws MLECallError;
	
	/**
	 * Sets the clipping rectangle position.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareSetClip(PencilBracket __g,
		int __x, int __y, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Sets the stroke style for the hardware graphics.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __style The stroke type to set.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareSetStrokeStyle(PencilBracket __g,
		int __style)
		throws MLECallError;
	
	/**
	 * Translates drawing operations.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X translation.
	 * @param __y The Y translation.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@Exported
	public static native void hardwareTranslate(PencilBracket __g, int __x,
		int __y)
		throws MLECallError;
	
	/**
	 * Performs native image loading and returns a semi-modified RGB buffer
	 * where the first values according to {@link NativeImageLoadParameter}
	 * represent information about the image.
	 * 
	 * @param __type The {@link NativeImageLoadType} to load.
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The raw RGB for the image with starting parameters.
	 * @throws MLECallError If the image could not be loaded.
	 * @since 2021/12/05
	 */
	@Exported
	public static native int[] nativeImageLoadRGBA(int __type,
		byte[] __b, int __o, int __l)
		throws MLECallError;
	
	/**
	 * Returns a bit field of {@link NativeImageLoadType} which indicates which
	 * types of images are capable of being natively loaded on a platform
	 * which requiring byte code to be executed for it.
	 * 
	 * @return The bit field of {@link NativeImageLoadType} that can be
	 * natively loaded.
	 * @since 2021/12/05
	 */
	@Exported
	public static native int nativeImageLoadTypes();
}
