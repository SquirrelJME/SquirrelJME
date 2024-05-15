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
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
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
		char __c)
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
		char __char)
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
		char __char)
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
		char __char,
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
		char __char,
		@NotNull PencilBracket __pencil,
		int __x, int __y,
		@Nullable int[] __nextXY)
		throws MLECallError;
}
