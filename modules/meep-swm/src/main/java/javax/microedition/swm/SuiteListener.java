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

/**
 * This interface is called when a suite has been installed, is being
 * installed, or has been removed.
 *
 * @since 2016/06/24
 */
public interface SuiteListener
{
	/**
	 * This is called when the installation state of a suite has been changed.
	 *
	 * If installation of a Suite has failed for any reason then
	 * {@link SuiteState#INSTALLATION_FAILED} must be called.
	 *
	 * @param __track This is either the tracker which was provided when an
	 * application was set for installation or an unspecified tracker in the
	 * event of a removal. In the event of removal it is likely that the
	 * tracker would not be the same one returned by an installation due to
	 * restarts of the virtual machine.
	 * @param __state The new state of the suite.
	 * @since 2016/06/24
	 */
	public abstract void notifySuiteStateChanged(
		SuiteManagementTracker __track, SuiteState __state);
}

