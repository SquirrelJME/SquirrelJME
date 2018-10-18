// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import java.util.HashSet;
import java.util.Set;

/**
 * Main entry class for the TAC runner system.
 *
 * @since 2018/10/17
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments, used to filter tests.
	 * @since 2018/10/17
	 */
	public static void main(String... __args)
	{
		if (__args == null)
			__args = new String[0];
		
		// Load the database
		Database db = Database.build();
		
		// Run each test
		boolean hasfailed = false;
		for (SingleUnit su : db)
			hasfailed |= su.run();
		
		// Exit with failure if there are bad tests
		if (hasfailed)
			System.exit(1);
	}
}

