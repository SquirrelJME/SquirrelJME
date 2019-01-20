// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import javax.microedition.swm.Suite;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;
import javax.microedition.swm.TaskStatus;

/**
 * This contains a single test unit which may be run accordingly.
 *
 * @since 2018/10/17
 */
public final class SingleUnit
{
	/** The full name for this suite. */
	protected final String fullname;
	
	/** The suite for this unit. */
	protected final Suite suite;
	
	/** The midlet for this unit. */
	protected final String midlet;
	
	/**
	 * Initializes the unit.
	 *
	 * @param __s The suite of the unit.
	 * @param __m The unit midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/29
	 */
	public SingleUnit(Suite __s, String __m)
		throws NullPointerException
	{
		if (__s == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.suite = __s;
		this.midlet = __m;
		
		// The MIDlet name can be quite long, so see if there is an internal
		// name for the project used by SquirrelJME
		String sqname = __s.getAttributeValue(
			"X-SquirrelJME-InternalProjectName");
		if (sqname == null)
			sqname = __s.getName();
		else if (sqname.endsWith(".test"))
			sqname = sqname.substring(0, sqname.length() - 5);
		
		// Setup full name
		this.fullname = SingleUnit.__crimpName(sqname) + "." + __m;
	}
	
	/**
	 * The full name for this test.
	 *
	 * @return The full name for this test.
	 * @since 2018/10/29
	 */
	public final String fullName()
	{
		return this.fullname;
	}
	
	/**
	 * Runs the test.
	 *
	 * @return Whether the test was a success or not.
	 * @since 2018/10/17
	 */
	public final boolean run()
	{
		// Start task
		Task task = ManagerFactory.getTaskManager().startTask(
			this.suite, this.midlet);
		
		// Run the task task until it terminates
		for (long mswait = 10;; mswait += Math.min(10, 250))
		{
			// Check status
			TaskStatus status = task.getStatus();
			
			// Depends on the status
			switch (status)
			{
					// Success
				case EXITED_REGULAR:
					return true;
					
					// Failure
				case EXITED_FATAL:
				case EXITED_TERMINATED:
				case START_FAILED:
					return false;
					
					// Unhandled
				default:
					break;
			}
			
			// Wait for the task to do things before checking again
			try
			{
				Thread.sleep(mswait);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	/**
	 * Crimps the name so it fits and is easier to type and such.
	 *
	 * @param __in The input name.
	 * @return The crimped name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/29
	 */
	private static final String __crimpName(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Strip and lowercase characters
		StringBuilder rv = new StringBuilder();
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			char c = __in.charAt(i);
			
			// Lowercase capitals
			if (c >= 'A' && c <= 'Z')
				c = Character.toLowerCase(c);
			
			// Underscores to hyphens
			else if (c == '_')
				c = '-';
			
			// Ignore anything outside of this range
			else if (!((c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') ||
				c == '.' || c == '-'))
				continue;
			
			rv.append(c);
		}
		
		return rv.toString();
	}
}

