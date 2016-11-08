// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;

/**
 * This interface manages which suites are available to the kernel. A suite
 * is basically a MIDlet or LIBlet which may be used by user programs.
 *
 * @since 2016/11/02
 */
public interface KernelSuiteManager
{
	/**
	 * Returns an array containing the identifiers of every suite which is
	 * installed.
	 *
	 * @return An array containing the identifiers of installed suites.
	 * @since 2016/11/08
	 */
	public abstract int[] installedSuitesIDs();
	
	/**
	 * Loads the given resource from the specified suite.
	 *
	 * @param __id The suite identifier.
	 * @param __name The resource to load, if the resource name is the name of
	 * a class file then {@code null} may be returned depending on the
	 * implementation.
	 * @return The input stream for the given resource or {@code null} if it
	 * is unknown.
	 * @throws InvalidSuiteException If the suite identifier is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	public abstract InputStream loadSuiteResource(int __id, String __name)
		throws InvalidSuiteException, NullPointerException;
}

