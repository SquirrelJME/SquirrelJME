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
	
	@SquirrelJMEVendorApi
	public static native int metricPixelAscent(
		@NotNull PencilFontBracket __font,
		boolean __max)
		throws MLECallError;
	
	@SquirrelJMEVendorApi
	public static native int metricPixelBaseline(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	@SquirrelJMEVendorApi
	public static native int metricPixelDescent(
		@NotNull PencilFontBracket __font,
		boolean __max)
		throws MLECallError;
	
	@SquirrelJMEVendorApi
	public static native int metricPixelLeading(
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static native int pixelCharHeight(
		@NotNull PencilFontBracket __font,
		char __char)
		throws MLECallError;
	
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	public static native int pixelCharWidth(
		@NotNull PencilFontBracket __font,
		char __char)
		throws MLECallError;
	
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
	
	@SquirrelJMEVendorApi
	public static native void renderChar(
		@NotNull PencilFontBracket __font,
		char __char,
		@NotNull PencilBracket __pencil,
		int __x, int __y,
		@Nullable int[] __nextXY)
		throws MLECallError;
}
