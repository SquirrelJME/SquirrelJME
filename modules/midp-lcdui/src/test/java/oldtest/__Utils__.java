// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package oldtest;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.UntestableException;

/**
 * Utilities for the LCDUI tests.
 *
 * @since 2019/03/04
 */
final class __Utils__
{
	/**
	 * Attempts to obtain the display, otherwise the test cannot be ran.
	 *
	 * @return The display to use.
	 * @throws UntestableException If the display could not be obtained.
	 * @since 2019/03/04
	 */
	public static Display getDisplay()
		throws UntestableException
	{
		try
		{
			Display rv = Display.getDisplays(0)[0];
			
			rv.getCapabilities();
			
			return rv;
		}
		
		// No display possible?
		catch (Throwable e)
		{
			throw new UntestableException(e);
		}
	}
}

