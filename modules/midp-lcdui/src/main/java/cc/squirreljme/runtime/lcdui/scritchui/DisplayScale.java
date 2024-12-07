// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.midlet.MIDlet;

/**
 * Interface for display scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public abstract class DisplayScale
{
	/**
	 * Does this display scale require a buffer?
	 *
	 * @return If a buffer is required for scaling.
	 * @since 2024/05/12
	 */
	@SquirrelJMEVendorApi
	public abstract boolean requiresBuffer();
	
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __x The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int screenX(int __x);
	
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __y The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int screenY(int __y);
	
	/**
	 * Returns the current texture height.
	 *
	 * @return The current texture height.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	public abstract int textureH();
	
	/**
	 * Returns the max height of the scaled target texture.
	 *
	 * @return The target texture height.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	public abstract int textureMaxH();
	
	/**
	 * Returns the max width of the scaled target texture.
	 *
	 * @return The target texture width.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	public abstract int textureMaxW();
	
	/**
	 * Returns the current texture width.
	 *
	 * @return The current texture width.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	public abstract int textureW();
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __x The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int textureX(int __x);
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __y The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int textureY(int __y);
	
	/**
	 * Returns the display scale that currently should be used.
	 *
	 * @param __scritch The Scritch API used.
	 * @param __screen The screen to draw on.
	 * @param __window The window for the display.
	 * @return The resultant scale.
	 * @since 2024/03/21
	 */
	public static DisplayScale currentScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen, ScritchWindowBracket __window)
		throws NullPointerException
	{
		DisplayScale rv;
		
		// Try to figure out what a MIDlet desires as far as size is concerned
		MIDlet midlet = ActiveMidlet.optional();
		if (midlet != null)
		{
			// SquirrelJME Specific
			rv = DisplayScale.__midlet(midlet,
				"X-SquirrelJME-Resolution", false, 'x');
			if (rv != null)
				return rv;
			
			// MEXA API
			rv = DisplayScale.__midlet(midlet,
				"MIDxlet-ScreenSize", false, ',');
			if (rv != null)
				return rv;
			
			// Vodafone API
			rv = DisplayScale.__midlet(midlet,
				"MIDxlet-Application-Resolution", false,
				',');
			if (rv != null)
				return rv;
			
			// Mode Vodafone?
			rv = DisplayScale.__midlet(midlet,
				"MIDxlet-Application-Range", true,
				',');
			if (rv != null)
				return rv;
			
			// Nokia
			rv = DisplayScale.__midlet(midlet,
				"Nokia-MIDlet-Original-Display-Size", false,
				',');
			if (rv != null)
				return rv;
			
			// Nokia (alternative)
			rv = DisplayScale.__midlet(midlet,
				"Nokia-MIDlet-Target-Display-Size", false,
				',');
			if (rv != null)
				return rv;
			
			// SEMC?
			rv = DisplayScale.__midlet(midlet,
				"SEMC-Screen-Size", false,
				',');
			if (rv != null)
				return rv;
		}
		
		// DoJa with a defined screen size
		String doJaSize = System.getProperty(
			"cc.squirreljme.imode.adf.DrawArea");
		Debugging.debugNote("Scale: %s", doJaSize);
		if (doJaSize != null)
		{
			// Parse it
			int x = doJaSize.indexOf('x');
			if (x >= 1)
				try
				{
					int width = Math.max(96, Integer.parseInt(
						doJaSize.substring(0, x), 10));
					int height = Math.max(72, Integer.parseInt(
						doJaSize.substring(x + 1), 10));
					
					return new DisplayFixedFlatScale(width, height);
				}
				catch (NumberFormatException ignored)
				{
				}
		}
		
		// Use default otherwise
		return new DisplayFixedFlatScale(240, 320);
	}
	
	/**
	 * Parses a key from a MIDlet manifest for screen sizes.
	 *
	 * @param __midlet The MIDlet to parse from.
	 * @param __key The key to check.
	 * @param __swap If {@code true} then height is first.
	 * @param __delim The delimiter.
	 * @return The display scale or {@code null} if there is none.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/12/07
	 */
	private static DisplayScale __midlet(MIDlet __midlet, String __key,
		boolean __swap, char __delim)
		throws NullPointerException
	{
		if (__midlet == null)
			throw new NullPointerException("NARG");
		
		String value = __midlet.getAppProperty(__key);
		if (value == null)
			return null;
		
		return new DisplayFixedFlatScale(
			DisplayScale.__parse(value, __delim, __swap),
			DisplayScale.__parse(value, __delim, !__swap));
	}
	
	/**
	 * Parses the given dimension.
	 *
	 * @param __s The input string.
	 * @param __delim The delimiter to use.
	 * @param __height Reading the height?
	 * @return The resultant value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	private static int __parse(String __s, char __delim, boolean __height)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Try parsing values
		int at = __s.indexOf(__delim);
		if (at >= 0)
			try
			{
				// Read in value
				int v;
				if (!__height)
					v = Integer.parseInt(
						__s.substring(0, at), 10);
				else
					v = Integer.parseInt(
						__s.substring(at + 1), 10);
				
				// Only consider if it makes sense
				if (v >= 32 && v <= 1024)
					return v;
			}
			catch (NumberFormatException __ignored)
			{
			}
		
		// Fallback
		return (__height ? 320 : 240);
	}
}
