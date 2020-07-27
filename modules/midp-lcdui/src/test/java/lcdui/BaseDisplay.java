// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Base class for display tests.
 *
 * @since 2020/07/26
 */
public abstract class BaseDisplay
	extends TestRunnable
{
	/**
	 * Tests with the given display.
	 * 
	 * @param __display The display to test on.
	 * @throws Throwable On any exception.
	 * @since 2020/07/26
	 */
	public abstract void test(Display __display)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public final void test()
		throws Throwable
	{
		// Forward test
		this.test(Display.getDisplays(0)[0]);
	}
}
