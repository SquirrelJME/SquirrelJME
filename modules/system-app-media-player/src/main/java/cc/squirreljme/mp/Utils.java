// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Player;
import org.freedesktop.tango.TangoIconLoader;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities.
 *
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public class Utils
{
	/**
	 * Gets an icon from the Tango theme.
	 *
	 * @param __name The name of the icon to get.
	 * @return The resultant icon data.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public static Image tangoIcon(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set icon for the toolbar item
		if (!__name.equals("-"))
			try (InputStream in = TangoIconLoader.loadIcon(16, __name))
			{
				if (in != null)
					return Image.createImage(in);
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
		
		// Blank nothingness
		return Image.createImage(16, 16);
	}
	
	/**
	 * Formats time to be human-readable.
	 *
	 * @param __micros THe microseconds.
	 * @return The human-readable time.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static String formatTime(long __micros)
	{
		if (__micros < 0)
			return "Unknown";
		
		StringBuilder sb = new StringBuilder();
		
		// Add microseconds
		long mod = __micros % 1_000_000;
		long div = __micros / 1_000_000;
		sb.append(String.format("%06d\u00B5s", mod));
		
		// Add seconds
		if (div > 0)
		{
			mod = div % 60;
			div = div / 60;
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02ds", mod));
		}
		
		// Add minutes
		if (div > 0)
		{
			mod = div % 60;
			div = div / 60;
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02dm", mod));
		}
		
		// Add hours
		if (div > 0)
		{
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02dh", div));
		}
		
		return sb.toString();
	}
	
	/**
	 * Formats the player state ID.
	 *
	 * @param __id The ID.
	 * @return The formatted state.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static String formatState(int __id)
	{
		switch (__id)
		{
			case Player.CLOSED:
				return "Closed (0)";
				
			case Player.UNREALIZED:
				return "Unrealized (100)";
			
			case Player.REALIZED:
				return "Realized (200)";
				
			case Player.PREFETCHED:
				return "Prefetched (300)";
				
			case Player.STARTED:
				return "Started (400)";
				
				// Undefined State ID
			default:
				return String.format("Undefined (%d)", __id);
		}
	}
}
