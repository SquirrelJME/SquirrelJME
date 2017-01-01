// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * This interface manages which suites are available to the kernel. A suite
 * is basically a MIDlet or LIBlet which may be used by user programs.
 *
 * @since 2016/11/02
 */
public interface KernelSuiteManager
{
	/**
	 * Returns the read-only list of system suites that are currently
	 * installed in the kernel.
	 *
	 * @return The list of installed system suites.
	 * @since 2016/12/16
	 */
	public abstract SystemInstalledSuites systemSuites();
	
	/**
	 * Returns the synchronized and thread safe mapping of user installed
	 * suites where each suite is associated with its suite identifier.
	 *
	 * @return The synchronized and thread safe mapping of suites which are
	 * installed the user.
	 * @since 2016/12/16
	 */
	public abstract UserInstalledSuites userSuites();
}

