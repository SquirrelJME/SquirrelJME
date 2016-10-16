// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.base.launcher;

/**
 * This is the interface which is used to launch projects and execute their
 * code via the main entry point.
 *
 * @since 2016/09/18
 */
public interface BootLauncher
{
	/**
	 * Launches the specified class with the given arguments.
	 *
	 * @param __ra The accessor for resources.
	 * @param __ce A storage for any caught exceptions.
	 * @param __main The main class to enter into.
	 * @param __args The arguments to the main class.
	 * @return The main thread, it is not started.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	public abstract Thread launch(ResourceAccessor __ra, CaughtException __ce,
		String __main, String... __args)
		throws NullPointerException;
}

