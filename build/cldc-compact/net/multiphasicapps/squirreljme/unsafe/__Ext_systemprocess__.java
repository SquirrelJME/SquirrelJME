// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * Internal system process handling.
 *
 * @since 2017/08/11
 */
final class __Ext_systemprocess__
{
	/**
	 * Not used.
	 *
	 * @since 2017/08/11
	 */
	private __Ext_systemprocess__()
	{
	}
	
	/**
	 * This returns the number of CPU threads which are available for usage.
	 *
	 * @return The total number of available CPU threads.
	 * @since 2017/08/29
	 */
	public static int cpuThreads()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * The build system always has the most permission to do anything.
	 *
	 * @return Always {@code true}.
	 * @since 2017/08/11
	 */
	public static boolean isLauncher()
	{
		return true;
	}
}

