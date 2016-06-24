// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This is used to track the the progress of a suite that is currently
 * being installed.
 *
 * @since 2016/06/24
 */
public class SuiteManagementTracker
{
	/**
	 * Prevents implicit instantiation of trackers.
	 *
	 * @since 2016/06/24
	 */
	protected SuiteManagementTracker()
	{
	}
	
	/**
	 * Returns the suite that this tracker is assigned to or {@code null} if it
	 * is not install yet.
	 *
	 * @return The suite this tracker is assigned to or {@code null} if it
	 * has not yet been installed.
	 * @since 2016/06/24
	 */
	public Suite getSuite()
	{
		throw new Error("TODO");
	}
}

