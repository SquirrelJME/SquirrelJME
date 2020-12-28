// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a scanner which can read all of the application groups that are
 * available.
 *
 * @since 2020/12/28
 */
public final class SuiteScanner
{
	/**
	 * Not used.
	 * 
	 * @since 2020/12/28
	 */
	private SuiteScanner()
	{
	}
	
	/**
	 * Scans all of the available suites and returns imformation that is needed
	 * for them to properly launch.
	 * 
	 * @return The state of scanned suites.
	 * @since 2020/12/28
	 */
	public static SuiteState scanSuites()
	{
		throw Debugging.todo();
	}
}
