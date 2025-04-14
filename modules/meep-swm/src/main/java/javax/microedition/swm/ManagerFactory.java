// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This class provides static methods for obtaining the application suite and
 * task manager.
 *
 * @since 2016/06/24
 */
@Api
public class ManagerFactory
{
	/** Lock for initialization. */
	private static final Object _LOCK =
		new Object();
	
	/** The suite manager. */
	private static volatile SuiteManager _SUITE_MANAGER;
	
	/** The task manager. */
	private static volatile TaskManager _TASK_MANAGER;
	
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
	@Api
	public static SuiteManager getSuiteManager()
		throws SecurityException
	{
		// Lazily initialize
		synchronized (ManagerFactory._LOCK)
		{
			SuiteManager rv = ManagerFactory._SUITE_MANAGER;
			if (rv == null)
				ManagerFactory._SUITE_MANAGER =
					(rv = new __SystemSuiteManager__());
			return rv;
		}
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
	@Api
	public static TaskManager getTaskManager()
		throws SecurityException
	{
		// Lazily initialize so that the class is easier to bring up rather
		// than at class initialization time
		synchronized (ManagerFactory._LOCK)
		{
			TaskManager rv = ManagerFactory._TASK_MANAGER;
			if (rv == null)
				ManagerFactory._TASK_MANAGER =
					(rv = new __SystemTaskManager__());
			return rv;
		}
	}
}

