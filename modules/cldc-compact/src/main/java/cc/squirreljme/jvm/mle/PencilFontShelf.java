// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.PencilFontFace;
import cc.squirreljme.jvm.mle.constants.PencilFontStyle;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Used to obtain details on fonts and how to draw them.
 *
 * @since 2024/05/14
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
public final class PencilFontShelf
{
	/**
	 * Not used.
	 *
	 * @since 2024/05/14
	 */
	private PencilFontShelf()
	{
	}
	
	/**
	 * Looks up the given font by name, face, style, and pixel size.
	 *
	 * @param __name The name of the font.
	 * @param __face The face of the font.
	 * @param __style The style of font.
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant found font or {@code null} if no such font
	 * exists.
	 * @throws MLECallError On null arguments or values are out of range or
	 * not valid.
	 * @since 2024/05/17
	 */
	@Nullable
	@SquirrelJMEVendorApi
	public static native PencilFontBracket lookup(
		@NotNull String __name,
		@MagicConstant(flagsFromClass = PencilFontFace.class) int __face,
		@MagicConstant(flagsFromClass = PencilFontStyle.class) int __style,
		@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize)
		throws MLECallError;
	
	/**
	 * Looks up an internal SquirrelJME font that acts as a fallback for
	 * when a system font is not available.
	 *
	 * @param __face The face of the font.
	 * @param __style The style of font.
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant found font or {@code null} if no such font
	 * exists.
	 * @throws MLECallError On null arguments or values are out of range or
	 * not valid.
	 * @since 2024/05/17
	 */
	@Nullable
	@SquirrelJMEVendorApi
	public static native PencilFontBracket lookupFallback(
		@MagicConstant(flagsFromClass = PencilFontFace.class) int __face,
		@MagicConstant(flagsFromClass = PencilFontStyle.class) int __style,
		@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize)
		throws MLECallError;
	
	/**
	 * Returns the direction of the given character in the font.
	 *
	 * @param __font The font to check.
	 * @param __c The character.
	 * @return The direction of the character, will be {@code -1} or {@code 1}.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	@Range(from = -1, to = 1)
	public static native int metricCharDirection(
		@NotNull PencilFontBracket __font,
		int __c)
		throws MLECallError;
	
	/**
	 * Checks whether the character in the given font is valid, as in it has
	 * a render-able glyph.
	 *
	 * @param __font The font to check within.
	 * @param __c The character to check.
	 * @return If the character in the font has a glyph and is valid.
	 * @throws MLECallError On null arguments or if the font is not valid.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	public static native boolean metricCharValid(
		@NotNull PencilFontBracket __font,
		int __c)
		throws MLECallError;
	
	/**
	 * Returns the {@link PencilFontFace} of a font. 
	 *
	 * @param __font The font to request from.
	 * @return The font face, any flag from {@link PencilFontFace}.
	 * @throws MLECallError On null arguments or the font is not valid.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(flagsFromClass = PencilFontFace.class)
	public static native int metricFontFace(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	/**
	 * Returns the style of the font.
	 *
	 * @param __font The style of the font to request.
	 * @return The font style, will be flags from {@link PencilFontStyle}.
	 * @throws MLECallError On null arguments or the font is not valid.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(flagsFromClass = PencilFontStyle.class)
	public static native int metricFontStyle(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	/**
	 * Returns the ascent of the font.
	 *
	 * @param __font The font to check.
	 * @param __max Should the max be obtained.
	 * @return The ascent of the font in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native int metricPixelAscent(
		@NotNull PencilFontBracket __font,
		boolean __max)
		throws MLECallError;
	
	/**
	 * Returns the baseline of the font.
	 *
	 * @param __font The font to check.
	 * @return The baseline of the font in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native int metricPixelBaseline(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	/**
	 * Returns the descent of the font.
	 *
	 * @param __font The font to check.
	 * @param __max Should the max be obtained.
	 * @return The descent of the font in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native int metricPixelDescent(
		@NotNull PencilFontBracket __font,
		boolean __max)
		throws MLECallError;
	
	/**
	 * Returns the leading of the font.
	 *
	 * @param __font The font to obtain from.
	 * @return The leading amount in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native int metricPixelLeading(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	/**
	 * Returns the height of the given character.
	 *
	 * @param __font The font to obtain from.
	 * @param __char The character.
	 * @return The height of the font in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static native int pixelCharHeight(
		@NotNull PencilFontBracket __font,
		int __char)
		throws MLECallError;
	
	/**
	 * Returns the width of the given character.
	 *
	 * @param __font The font to obtain from.
	 * @param __char The character.
	 * @return The width of the font in pixels.
	 * @throws MLECallError If the font is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static native int pixelCharWidth(
		@NotNull PencilFontBracket __font,
		int __char)
		throws MLECallError;
	
	/**
	 * Renders the font glyph to a bitmap represented in a byte array. Each
	 * byte within the array represents 8 pixels.
	 *
	 * @param __font The font to render to the bitmap.
	 * @param __char The character to render.
	 * @param __buf The resultant buffer.
	 * @param __bufOff The offset into the buffer.
	 * @param __scanLen The scanline length of the buffer.
	 * @param __sx The surface X.
	 * @param __sy The surface Y.
	 * @param __sw The surface width.
	 * @param __sh The surface height.
	 * @throws MLECallError On null arguments, the font is not valid, or
	 * if the positions and/or offsets are negative or out of bounds.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native void renderBitmap(
		@NotNull PencilFontBracket __font,
		int __char,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bufOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sx,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh)
		throws MLECallError;
	
	/**
	 * Renders the given character to the resultant pencil.
	 *
	 * @param __font The font to render from.
	 * @param __char The character to render.
	 * @param __pencil The pencil to draw into.
	 * @param __x The target X position.
	 * @param __y The target Y position.
	 * @param __nextXY Optional output which contains the next X and Y
	 * coordinates accordingly for continual drawing.
	 * @throws MLECallError On null arguments, if the font is not valid, or if
	 * the pencil is not valid.
	 * @since 2024/05/14
	 */
	@SquirrelJMEVendorApi
	public static native void renderChar(
		@NotNull PencilFontBracket __font,
		int __char,
		@NotNull PencilBracket __pencil,
		int __x, int __y,
		@Nullable int[] __nextXY)
		throws MLECallError;
}
