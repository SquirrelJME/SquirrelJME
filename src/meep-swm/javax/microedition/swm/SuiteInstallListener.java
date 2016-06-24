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
 * This is a listener which is called when an application is currently being
 * installed, has finished installation, or failed to install.
 *
 * @since 2016/06/24
 */
public interface SuiteInstallListener
{
	/**
	 * This is called when an installation has finished for any reason, from
	 * success to failure.
	 *
	 * Before this is called, {@link #updateStatus(SuiteManagementTracker,
	 * SuiteInstallStage, int)} must be called with
	 * {@link SuiteInstallStage#DONE}.
	 *
	 * @param __ec The result of the installation attempt.
	 * @param __track The tracker which was created when an installation
	 * was started to be performed.
	 * @since 2016/06/24
	 */
	public abstract void installationDone(InstallErrorCodes __ec,
		SuiteManagementTracker __track);
	
	/**
	 * This is called when the current status of the installation of a suite
	 * has been updated. This may be used by a listener to provide a progress
	 * bar for example.
	 *
	 * @param __track The tracker which was created when an installation
	 * was started to be performed.
	 * @param __stage The current stage of the installation.
	 * @param __pct The amount of progress which has passed in this stage, this
	 * should be a value between zero and one hundred.
	 * @since 2016/06/24
	 */
	public abstract void updateStatus(SuiteManagementTracker __track,
		SuiteInstallStage __stage, int __pct);
}

