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

import java.util.List;

/**
 * This interface is used to manage querying, installation, and removal of
 * Suites.
 *
 * @see ManagerFactory
 * @since 2016/06/24
 */
public interface SuiteManager
{
	/**
	 * Adds a listener which is used when the state of a suite has been changed
	 * such as when it has been installed or removed.
	 *
	 * @param __sl The listener to be added.
	 * @since 2016/06/24
	 */
	public abstract void addSuiteListener(SuiteListener __sl);
	
	/**
	 * Attempts to return an installed suite created by a specific vendor and
	 * which uses the given name.
	 *
	 * @param __vendor The vendor which created the given suite.
	 * @param __name The name of the suite.
	 * @return An instance of the given suite or {@code null} if it was not
	 * found.
	 * @since 2016/06/24
	 */
	public abstract Suite getSuite(String __vendor, String __name);
	
	/**
	 * Obtains an installer that would be used to install the given JAR file
	 * which is represented as raw byte data.
	 *
	 * The {@code {@link SWMPermission}("client", "installation")} or
	 * {@code {@link SWMPermission}("crossClient", "installation")} is checked
	 * before an installation is attempted.
	 *
	 * @param __b The raw JAR data.
	 * @param __o The offset to the start of the data.
	 * @param __l The length of the data.
	 * @param __ignuplock If {@code true} then the components of the suite
	 * will be updated without regards to synchronization, this is not
	 * recommended and {@code false} should always be used.
	 * @return An installer which represents this given installation.
	 * @throws IllegalArgumentException If the input buffer is null, empty, or
	 * the offset and length are negative or exceed the array bounds.
	 * @throws SecurityException If the current application is not permitted
	 * to install new suites.
	 * @since 2016/06/24
	 */
	public abstract SuiteInstaller getSuiteInstaller(byte[] __b, int __o,
		int __l, boolean __ignuplock)
		throws IllegalArgumentException, SecurityException;
	
	/**
	 * This attempts to install a suite which is specified at the given URL. It
	 * is implementation dependent on how the URL is handled, it may be a
	 * platform specific resource or a network resource.
	 *
	 * The {@code {@link SWMPermission}("client", "installation")} or
	 * {@code {@link SWMPermission}("crossClient", "installation")} is checked
	 * before an installation is attempted.
	 *
	 * @param __url The URL which refers to a suite to be installed.
	 * @param __ignuplock If {@code true} then the components of the suite
	 * will be updated without regards to synchronization, this is not
	 * recommended and {@code false} should always be used.
	 * @throws IllegalArgumentException If the URL is not valid or it could
	 * not be obtained.
	 * @throws SecurityException If the current application is not permitted
	 * to install new suites.
	 * @since 2016/06/24
	 */
	public abstract SuiteInstaller getSuiteInstaller(String __url,
		boolean __ignuplock)
		throws IllegalArgumentException, SecurityException;
	
	/**
	 * Returns a list of the suites tha are currently installed on the system.
	 *
	 * @param __t The type of suites to obtain, only
	 * {@link SuiteType#APPLICATION} and {@link SuiteType#LIBRARY} are valid.
	 * @return A list containing the suites which are currently installed,
	 * if there are no suites then this may be empty.
	 * @throws IllegalArgumentException If the requested type is neither
	 * an application or a library.
	 * @since 2016/06/24
	 */
	public abstract List<Suite> getSuites(SuiteType __t)
		throws IllegalArgumentException;
	
	/**
	 * Synchronously removes the given installed suite.
	 *
	 * Suites must be stopped before they can be used, a removal of a suite
	 * that is currently active cannot be performed.
	 *
	 * In situations where any resources of the suite are currently being used
	 * by an application, the removed suite must still make its resources
	 * accessible until all of them are released.
	 *
	 * If a suite was removed without issues then {@link Suite#isInstalled()}
	 * will return {@code false}.
	 *
	 * @param __s The suite to be removed.
	 * @param __ignuplock If {@code true} then the suite is removed regardless
	 * if it has the {@link SuiteStateFlag#REMOVE_DENIED} flag.
	 * @throws IllegalArgumentException If the suite has already been removed,
	 * or if the application is currently running and the application.
	 * @throws SuiteLockedException If the suite is locked and
	 * {@code __ignuplock} has been set to {@code false}. It should also be
	 * noted that it is possible for this to be thrown regardless of the
	 * parameter.
	 * @since 2016/06/24
	 */
	public abstract void removeSuite(Suite __s, boolean __ignuplock)
		throws IllegalArgumentException, SuiteLockedException;
	
	/**
	 * Removes a previously added suite listener so that it no longer is
	 * given any status updated when the state of a suite has changed.
	 *
	 * @param __sl The listener to remove.
	 * @since 2016/06/24
	 */
	public abstract void removeSuiteListener(SuiteListener __sl);
}

