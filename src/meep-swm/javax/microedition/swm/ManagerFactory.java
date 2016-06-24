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
 * This class provides static methods for obtaining the application suite and
 * task manager.
 *
 * @since 2016/06/24
 */
public class ManagerFactory
{
	/**
	 * Returns an instance of the suite manager that the application may use
	 * to manage suites on the system.
	 *
	 * @return The manager which is used to manage suites installed on the
	 * system.
	 * @throws SecurityException If the {@code {@link SWMPermission}("client",
	 * "manageSuite")} or {@code {@link SWMPermission}("crossClient",
	 * "manageSuite")} permission is not permitted.
	 * @since 2016/06/24
	 */
	public static SuiteManager getSuiteManager()
		throws SecurityException
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns an instance of the task manager which is used to start,
	 * stop, and enumerate currently running tasks.
	 *
	 * @return The manager which is used to manage tasks which are currently
	 * running.
	 * @throws SecurityException If the {@code {@link SWMPermission}("client",
	 * "manageTask")} or {@code {@link SWMPermission}("crossClient",
	 * "manageTask")} permission is not permitted.
	 * @since 2016/06/24
	 */
	public static TaskManager getTaskManager()
		throws SecurityException
	{
		throw new Error("TODO");
	}
}

