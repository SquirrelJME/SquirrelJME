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
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

/**
 * Tests the selection of items and otherwise.
 *
 * @since 2020/11/03
 */
public class TestSelections
	extends BaseList
{
	/**
	 * {@inheritDoc}
	 * @since 2020/11/03
	 */
	@Override
	protected void test(Display __display, List __list, int __type,
		String __typeName)
		throws Throwable
	{
		throw Debugging.todo();
	}
}
