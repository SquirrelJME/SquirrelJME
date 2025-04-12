// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.launch.IModeProperty;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.midlet.MIDlet;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for display scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public abstract class DisplayScale
{
	/** Display scale system property. */
	@SquirrelJMEVendorApi
	public static final String SCALE_PROPERTY =
		"cc.squirreljme.scale";
	
	/** Display scale environment. */
	@SquirrelJMEVendorApi
	public static final String SCALE_ENV =
		"SQUIRRELJME_SCALE";
	
	/** Display frame system property. */
	@SquirrelJMEVendorApi
	public static final String FRAME_PROPERTY =
		"cc.squirreljme.frame";
	
	/** Display frame environment. */
	@SquirrelJMEVendorApi
	public static final String FRAME_ENV =
		"SQUIRRELJME_FRAME";
	
	/** The default scaling. */
	@SquirrelJMEVendorApi
	public static final byte SCALE_DEFAULT =
		2;
	
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
	 * Returns the application scale, how big the application should be.
	 *
	 * @param __scritch The Scritch API used.
	 * @param __screen The screen to draw on.
	 * @param __window The window for the display.
	 * @return The resultant application scale.
	 * @since 2024/03/21
	 */
	@SquirrelJMEVendorApi
	public static DisplayScale applicationScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen, ScritchWindowBracket __window)
		throws NullPointerException
	{
		DisplayScale rv;
		
		// Overridden by the user?
		String override = System.getProperty(DisplayScale.FRAME_PROPERTY);
		if (override == null)
			override = RuntimeShelf.systemEnv(DisplayScale.FRAME_ENV);
		if (override != null && !override.isEmpty())
		{
			rv = DisplayScale.__xyOverride(override);
			if (rv != null)
				return rv;
		}
		
		// Try to figure out what a MIDlet desires as far as size is concerned
		MIDlet midlet = ActiveMidlet.optional();
		if (midlet != null)
		{
			rv = DisplayScale.__midlet(midlet);
			if (rv != null)
				return rv;
		}
		
		// DoJa with a defined screen size
		String doJaSize = System.getProperty(
			"cc.squirreljme.imode.adf.DrawArea");
		if (doJaSize != null)
		{
			rv = DisplayScale.__dojaDrawArea(doJaSize);
			if (rv != null)
				return rv;
		}
		
		// DoJa specific phone model
		String dojaTargetDevice = System.getProperty(
			IModeProperty.ADF_PROPERTY_PREFIX + "." + 
				IModeProperty._TARGET_DEVICE);
		if (dojaTargetDevice != null && dojaTargetDevice.isEmpty())
		{
			rv = DisplayScale.__dojaTargetDevice(
				dojaTargetDevice.toLowerCase());
			if (rv != null)
				return rv;
		}
		
		// DoJa profile
		String dojaProfile = System.getProperty(
			IModeProperty.DOJA_PROFILE_PROPERTY);
		if (dojaProfile != null && !dojaProfile.isEmpty())
		{
			rv = DisplayScale.__dojaProfile(
				dojaProfile.trim().toLowerCase());
			if (rv != null)
				return rv;
		}
		
		// Use default otherwise
		return new DisplayFixedFlatScale(240, 320);
	}
	
	/**
	 * Returns the display scale that currently should be used.
	 *
	 * @param __scritch The Scritch API used.
	 * @param __screen The screen to draw on.
	 * @param __window The window for the display.
	 * @return The resultant scale.
	 * @since 2024/03/21
	 */
	@SquirrelJMEVendorApi
	public static DisplayScale currentScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen, ScritchWindowBracket __window)
		throws NullPointerException
	{
		// Get the scale the application uses
		DisplayScale appScale = DisplayScale.applicationScale(__scritch,
			__screen, __window);
		
		// Overridden by the user?
		String override = System.getProperty(DisplayScale.SCALE_PROPERTY);
		if (override == null)
			override = RuntimeShelf.systemEnv(DisplayScale.SCALE_ENV);
		
		// Actually overridden?
		int useW = 0;
		int useH = 0;
		int useScale = DisplayScale.SCALE_DEFAULT;
		if (override != null && !override.isEmpty())
			try
			{
				// WxH?
				int s = override.indexOf('x');
				if (s >= 0)
				{
					useW = Integer.parseInt(
						override.substring(0, s), 10);
					useH = Integer.parseInt(
						override.substring(s + 1), 10);
				}
				
				// Normal scale
				else
					useScale = Integer.parseInt(override, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Project scaled coordinates
		int[] coord = new int[]{0, 0,
			(useW > 0 ? useW : appScale.textureW() * Math.max(1, useScale)),
			(useH > 0 ? useH : appScale.textureH() * Math.max(1, useScale))};
		
		// Did it actually change?
		if (coord[2] != appScale.textureW() ||
			coord[3] != appScale.textureH())
			return new DisplayFloatScale(appScale,
				coord[2], coord[3]);
		return appScale;
	}
	
	/**
	 * Parses the DoJa draw area.
	 *
	 * @param __v The input value.
	 * @return The resultant scale.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/10
	 */
	private static DisplayFixedFlatScale __dojaDrawArea(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Parse it
		int x = __v.indexOf('x');
		if (x >= 1)
			try
			{
				int width = Math.max(96, Integer.parseInt(
					__v.substring(0, x), 10));
				int height = Math.max(72, Integer.parseInt(
					__v.substring(x + 1), 10));
				
				return new DisplayFixedFlatScale(width, height);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		return null;
	}
	
	/**
	 * Returns a resolution based on the target device.
	 *
	 * @param __devices The DoJa device list.
	 * @return The display scale for the given device.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/10
	 */
	private static DisplayScale __dojaTargetDevice(String __devices)
		throws NullPointerException
	{
		if (__devices == null)
			throw new NullPointerException("NARG");
		
		// Split by commas, as that is the expected format
		for (String device : StringUtils.basicSplit(',', __devices))
		{
			// Ignore blank devices
			device = device.trim().toLowerCase();
			if (device.isEmpty())
				continue;
			
			// Depends on the device type
			switch (device)
			{
				case "so503i":
				case "so503is":
					return new DisplayFixedFlatScale(120, 120);
					
				case "f503i":
				case "f503is":
				case "n2001":
				case "n2002":
				case "n503i":
				case "n503is":
				case "p2002":
				case "p503i":
				case "p503is":
					return new DisplayFixedFlatScale(120, 130);
					
				case "so504i":
					return new DisplayFixedFlatScale(128, 128);
					
				case "d503i":
				case "d503is":
					return new DisplayFixedFlatScale(132, 126);
					
				case "d2101v":
					return new DisplayFixedFlatScale(132, 130);
					
				case "f504i":
				case "f504is":
					return new DisplayFixedFlatScale(132, 136);
					
				case "d504i":
				case "p504i":
				case "p504is":
					return new DisplayFixedFlatScale(132, 144);
					
				case "n504i":
				case "n504is":
					return new DisplayFixedFlatScale(160, 180);
					
				case "nm850ig":
				case "t2101v":
					return new DisplayFixedFlatScale(176, 144);
					
				case "n600i":
					return new DisplayFixedFlatScale(176, 180);
					
				case "f2051":
				case "f2102v":
				case "p2101v":
					return new DisplayFixedFlatScale(176, 182);
					
				case "l600i":
				case "l601i":
				case "n2051":
				case "n2102v":
				case "n2701":
				case "p2102v":
					return new DisplayFixedFlatScale(176, 198);
					
				case "sh2101v":
					return new DisplayFixedFlatScale(240, 160);
					
				case "sh505i":
				case "sh505is":
				case "sh506ic":
				case "sh900i":
					return new DisplayFixedFlatScale(240, 252);
					
				case "p505i":
				case "p505is":
				case "p506ic":
				case "p506icii":
					return new DisplayFixedFlatScale(240, 266);
					
				case "m702ig":
				case "m702is":
					return new DisplayFixedFlatScale(240, 267);
					
				case "f505i":
				case "f505igps":
				case "f506i":
					return new DisplayFixedFlatScale(240, 268);
					
				case "d505i":
				case "d505is":
				case "d506i":
				case "d900i":
				case "n506i":
				case "n506is":
				case "n506isii":
					return new DisplayFixedFlatScale(240, 270);
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Returns the device size based on the DoJa profile.
	 *
	 * @param __profile The profile to use.
	 * @return The resultant screen size based on the profile.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/10
	 */
	private static DisplayScale __dojaProfile(String __profile)
		throws NullPointerException
	{
		if (__profile == null)
			throw new NullPointerException("NARG");
		
		if (__profile.equalsIgnoreCase("DoJa-1.0"))
			return new DisplayFixedFlatScale(240, 160);
		
		// Use this otherwise
		return new DisplayFixedFlatScale(240, 240);
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
	 * Parses MIDlet based versions.
	 *
	 * @param __midlet The midlet used.
	 * @return The resultant scale.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/10
	 */
	private static DisplayScale __midlet(MIDlet __midlet)
		throws NullPointerException
	{
		if (__midlet == null)
			throw new NullPointerException("NARG");
		
		DisplayScale rv;
		
		// SquirrelJME Specific
		rv = DisplayScale.__midlet(__midlet,
			"X-SquirrelJME-Resolution", false, 'x');
		if (rv != null)
			return rv;
		
		// MEXA API
		rv = DisplayScale.__midlet(__midlet,
			"MIDxlet-ScreenSize", false, ',');
		if (rv != null)
			return rv;
		
		// Vodafone API
		rv = DisplayScale.__midlet(__midlet,
			"MIDxlet-Application-Resolution", false,
			',');
		if (rv != null)
			return rv;
		
		// Mode Vodafone?
		rv = DisplayScale.__midlet(__midlet,
			"MIDxlet-Application-Range", true,
			',');
		if (rv != null)
			return rv;
		
		// Nokia
		rv = DisplayScale.__midlet(__midlet,
			"Nokia-MIDlet-Original-Display-Size", false,
			',');
		if (rv != null)
			return rv;
		
		// Nokia (alternative)
		rv = DisplayScale.__midlet(__midlet,
			"Nokia-MIDlet-Target-Display-Size", false,
			',');
		if (rv != null)
			return rv;
		
		// SEMC?
		rv = DisplayScale.__midlet(__midlet,
			"SEMC-Screen-Size", false,
			',');
		if (rv != null)
			return rv;
		
		return null;
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
	
	/**
	 * Parses an WxH override.
	 *
	 * @param __v The input value.
	 * @return The resultant scale.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/10
	 */
	private static DisplayFixedFlatScale __xyOverride(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		int useW = -1;
		int useH = -1;
		
		// Parse values
		int s = __v.indexOf('x');
		if (s >= 0)
			try
			{
				useW = Integer.parseInt(
					__v.substring(0, s), 10);
				useH = Integer.parseInt(
					__v.substring(s + 1), 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Is the override valid?
		if (useW > 0 && useH > 0)
			return new DisplayFixedFlatScale(useW, useH);
		return null;
	}
}
