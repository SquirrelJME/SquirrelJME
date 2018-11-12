// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestRunnable;

import javax.microedition.lcdui.Display;

/**
 * Tests that the display works.
 *
 * @since 2018/11/12
 */
public class TestDisplay
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/12
	 */
	@Override
	public void test()
	{
		Display[] displays = Display.getDisplays(0);
		
		throw new todo.TODO();
	}
}

