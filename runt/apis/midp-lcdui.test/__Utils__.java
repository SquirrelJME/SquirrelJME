// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.InvalidTestException;

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
	 * @throws InvalidTestException If the display could not be obtained.
	 * @since 2019/03/04
	 */
	public static final Display getDisplay()
		throws InvalidTestException
	{
		try
		{
			return Display.getDisplays(0)[0];
		}
		
		// No display possible?
		catch (Throwable e)
		{
			throw new InvalidTestException(e);
		}
	}
}

