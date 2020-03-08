// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package display;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.tac.InvalidTestException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Not Described
 *
 * @since 2020/02/29
 */
public class TestDisplayInitialize
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/29
	 */
	@Override
	public void test()
	{
		Display[] displays = Display.getDisplays(0);
		if (displays.length == 0)
			throw new InvalidTestException("No displays to test.");
		
		Display display = displays[0];
		display.setCurrent(new __BlankCanvas__());
	}
	
	/**
	 * Blank canvas, needed because the display appears when a displayable is
	 * added.
	 *
	 * @since 2020/02/29
	 */
	private static final class __BlankCanvas__
		extends Canvas
	{
		/**
		 * Initialization.
		 *
		 * @since 2020/02/29
		 */
		__BlankCanvas__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/02/29
		 */
		@Override
		protected void paint(Graphics __g)
		{
			// Does nothing
		}
	}
}
