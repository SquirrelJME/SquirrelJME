// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.list;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests that selection commands work properly.
 *
 * @since 2020/11/03
 */
public class TestSelectionCommands
	extends BaseList
{
	/** Number of list items to test. */
	public static final int NUM_ITEMS =
		3;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/03
	 */
	@Override
	protected void test(Display __display, List __list, int __type,
		String __typeName)
		throws Throwable
	{
		// This is only valid on implicit tests
		if (__type != Choice.IMPLICIT)
			throw new UntestableException("Implicit only."); 
		
		throw Debugging.todo();
	}
}
