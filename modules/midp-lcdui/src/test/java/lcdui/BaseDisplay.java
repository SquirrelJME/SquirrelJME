// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.UntestableException;

/**
 * Base class for display tests.
 *
 * @since 2020/07/26
 */
public abstract class BaseDisplay
	extends TestConsumer<String>
{
	/**
	 * Tests with the given display.
	 * 
	 * @param __display The display to test on.
	 * @param __param The parameter to use, this is optional.
	 * @throws Throwable On any exception.
	 * @since 2020/07/26
	 */
	public abstract void test(Display __display, String __param)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public final void test(String __param)
		throws Throwable
	{
		// Extract parameters
		int at = __param.indexOf('@');
		int displayId = Integer.parseInt(__param.substring(0, at).trim());
		String subParam = __param.substring(at + 1).trim();
		
		// Determine the display to test on
		Display[] displays = Display.getDisplays(0);
		if (displayId < 0 || displayId >= displays.length)
			throw new UntestableException("No display " + displayId);
		Display display = displays[displayId];	
		
		// Forward to native handler for test, for each display
		this.test(display, subParam);
	}
}
