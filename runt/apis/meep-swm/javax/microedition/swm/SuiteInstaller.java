// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.swm.JarStreamSupplier;
import java.util.Set;
import net.multiphasicapps.collections.IdentityLinkedHashSet;

/**
 * This class is created when a suite is to be installed.
 *
 * @since 2016/06/24
 */
public final class SuiteInstaller
{
	/** The supplier for the JAR data. */
	final JarStreamSupplier _supplier;
	
	/** Listeners for suites. */
	final Set<SuiteInstallListener> _listeners =
		new IdentityLinkedHashSet<>();
	
	/**
	 * Internal use only.
	 *
	 * @param __sup The supplier for JAR files.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	SuiteInstaller(JarStreamSupplier __sup)
		throws NullPointerException
	{
		if (__sup == null)
			throw new NullPointerException("NARG");
		
		this._supplier = __sup;
	}
	
	/**
	 * Adds a suite installation listener which can be given status updates
	 * when a suite's installation status has changed.
	 *
	 * @param __sl The listener to add.
	 * @since 2016/06/24
	 */
	public final void addInstallationListener(SuiteInstallListener __sl)
	{
		// Ignore
		if (__sl == null)
			return;
		
		this._listeners.add(__sl);
	}
	
	/**
	 * Cancels the current installation.
	 *
	 * @since 2016/06/24
	 */
	public final void cancel()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes the given installation listener so that it no longer is notified
	 * of installation updates.
	 *
	 * @param __sl The listener to remove.
	 * @since 2016/06/24
	 */
	public final void removeInstallationListener(SuiteInstallListener __sl)
	{
		// Ignore
		if (__sl == null)
			return;
		
		this._listeners.remove(__sl);
	}
	
	/**
	 * Starts installation of the given suite.
	 *
	 * If this is called twice then the next installation attempt is
	 * enqueued regardless if the previous installation has succeeded or
	 * failed.
	 *
	 * If there is not enough permission to install the given suite then
	 * {@link InstallErrorCodes.UNAUTHORIZED_INSTALL} is set.
	 *
	 * @return The tracker for the given suite.
	 * @since 2016/06/24
	 */
	public final SuiteManagementTracker start()
	{
		return new __SuiteTracker__(this);
	}
}

