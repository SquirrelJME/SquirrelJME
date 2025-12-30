// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;
import org.freedesktop.tango.TangoIconLoader;

/**
 * Utilities.
 *
 * @since 2025/12/30
 */
public class Utils
{
	/**
	 * Gets an icon from the Tango theme.
	 *
	 * @param __name The name of the icon to get.
	 * @return The resultant icon data.
	 * @since 2025/12/30
	 */
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
}
