// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.io;

import cc.squirreljme.test.TestRunner;

/**
 * Test Runner.
 *
 * @since 2018/03/05
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Test arguments.
	 * @since 2018/03/05
	 */
	public static void main(String... __args)
	{
		new TestRunner(__args,
			TestBase64Decoder.class).run();
	}
}

