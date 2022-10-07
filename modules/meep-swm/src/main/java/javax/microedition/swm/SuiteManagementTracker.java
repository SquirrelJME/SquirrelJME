// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This is used to track the the progress of a suite that is currently
 * being installed.
 *
 * @since 2016/06/24
 */
public abstract class SuiteManagementTracker
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
	public abstract Suite getSuite();
}

