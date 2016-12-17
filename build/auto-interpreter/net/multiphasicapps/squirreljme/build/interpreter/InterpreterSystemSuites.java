// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import net.multiphasicapps.squirreljme.kernel.SystemInstalledSuites;

/**
 * This class manages and determines which suites are used and auto-installed
 * in the system class path.
 *
 * @since 2016/12/17
 */
public class InterpreterSystemSuites
	extends SystemInstalledSuites
{
	/**
	 * Initializes the system suite manager.
	 *
	 * @param __akm The owning kernel manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	public InterpreterSystemSuites(AbstractKernelManager __akm)
		throws NullPointerException
	{
		// Check
		if (__akm == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

