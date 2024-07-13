// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import java.util.Arrays;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Base class for rendering operations.
 *
 * @since 2024/07/12
 */
public abstract class BaseOperation
	extends TestConsumer<String>
{
	/** Buffer width. */
	public static final int WIDTH =
		8;
	
	/** Buffer height. */
	public static final int HEIGHT =
		8;
	
	/** Buffer area. */
	public static final int AREA =
		BaseOperation.WIDTH * BaseOperation.HEIGHT;
	
	/**
	 * Tests with the given pencil graphics.
	 *
	 * @param __g The pencil graphics used.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @throws Throwable On any exception.
	 * @since 2024/07/12
	 */
	protected abstract void test(PencilBracket __g, int __w, int __h)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/12
	 */
	@Override
	public void test(String __pf)
		throws Throwable
	{
		// Initialize raw buffer with initial white color
		int[] raw = new int[BaseOperation.AREA];
		Arrays.fill(raw, 0xFFFFFFFF);
		
		// Setup pencil graphics for drawing
		int pixelFormat = BaseOperation.__nameToFormat(__pf);
		PencilBracket g = NativeScritchInterface.nativeInterface()
			.hardwareGraphics(
				pixelFormat,
				BaseOperation.WIDTH, BaseOperation.HEIGHT,
				raw, null,
				0, 0,
				BaseOperation.WIDTH, BaseOperation.HEIGHT);
		
		// Run inner test
		this.test(g, BaseOperation.WIDTH, BaseOperation.HEIGHT);
		
		// Secondaries with rows, for simpler operation
		int[] row = new int[BaseOperation.WIDTH];
		String[] rowS = new String[BaseOperation.WIDTH];
		for (int y = 0; y < BaseOperation.HEIGHT; y++)
		{
			// Copy to buffer
			System.arraycopy(raw, BaseOperation.WIDTH * y,
				row, 0, BaseOperation.WIDTH);
			
			// Determine names
			for (int x = 0; x < BaseOperation.WIDTH; x++)
				rowS[x] = BaseOperation.__string(pixelFormat, row[x]);
			
			// Emit pixel data on it
			this.secondary(String.format("y%02d", y), rowS);
		}
	}
	
	/**
	 * Returns the pixel format to use for the given name.
	 *
	 * @param __pf The pixel format to use.
	 * @return The pixel format code.
	 * @since 2024/07/12
	 */
	private static int __nameToFormat(String __pf)
	{
		switch (__pf)
		{
			case "INT_ARGB8888":
				return UIPixelFormat.INT_ARGB8888;
				
			case "INT_RGB888":
				return UIPixelFormat.INT_RGB888;
				
			default:
				throw new RuntimeException("Unknown PF: " + __pf);
		}
	}
	
	/**
	 * Maps the color to the given name
	 *
	 * @param __pixelFormat The pixel format used.
	 * @param __c The color to convert.
	 * @return The resultant string.
	 * @since 2024/07/12
	 */
	private static String __string(int __pixelFormat, int __c)
	{
		if (__pixelFormat == UIPixelFormat.INT_RGB888)
			__c |= 0xFF000000;
		
		// Base for background and foreground color
		if (__c == 0xFFFFFFFF)
			return "bg";
		else if (__c == 0xFF000000)
			return "fg";
		
		// Otherwise use an HTML color code
		int a = (__c >>> 24) & 0xFF;
		int r = (__c >>> 16) & 0xFF;
		int g = (__c >>> 8) & 0xFF;
		int b = (__c) & 0xFF;
		
		if (a == 0xFF)
			return String.format("#%02x%02x%02x", r, g, b);
		return String.format("#%02X%02x%02x%02x", a, r, g, b);
	}
}
