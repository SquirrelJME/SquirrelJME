// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that strings are formatted properly.
 *
 * @since 2022/06/23
 */
public class TestFormatter
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/06/23
	 */
	@SuppressWarnings("RedundantStringFormatCall")
	@Override
	public void test()
		throws Throwable
	{
		// Newline should be the same as the line ending
		super.secondary("newline",
			System.getProperty("line.separator").equals(String.format("%n")));
		
		// Should be percent symbol
		super.secondary("percent", String.format("%%"));
	}
}
