// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.chore;

import net.multiphasicapps.squirreljme.runtime.cldc.program.Program;

/**
 * This is used to provide native access to system chores.
 *
 * @since 2017/12/07
 */
public abstract class Chores
{
	/** Shared lock to use to prevent concurrency issues. */
	protected final Object lock =
		new Object();
	
	/**
	 * Returns the current chore.
	 *
	 * @return The current chore.
	 * @since 2017/12/08
	 */
	public abstract Chore current();
	
	/**
	 * Internally lists the chores which exist within the system.
	 *
	 * @return The list of chores.
	 * @since 2017/12/08
	 */
	protected abstract Chore[] internalList(boolean __sys);
	
	/**
	 * Returns the current chore group.
	 *
	 * @return The current chore group.
	 * @since 2017/12/08
	 */
	public final ChoreGroup currentGroup()
	{
		return this.current().group();
	}
	
	/**
	 * Launches the specified chore.
	 *
	 * Note that even if the chore fails to start it will still be created
	 * except that it will be in the start failed state.
	 *
	 * @param __cp The class path consisting of programs to use for execution.
	 * @param __cl The midlet class to enter.
	 * @return The chore which represents the given launched program.
	 * @throws SecurityException If this chore is not permitted to launch the
	 * specified program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public final Chore launch(Program[] __cp, String __cl)
		throws SecurityException, NullPointerException
	{
		if (__cp == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0g The current chore is not permitted to
		// launch new chores.}
		if (0 == (this.currentGroup().basicPermissions() &
			(ChoreGroup.BASIC_PERMISSION_CLIENT_MANAGE_TASKS |
			ChoreGroup.BASIC_PERMISSION_CROSSCLIENT_MANAGE_TASKS)))
			throw new SecurityException("ZZ0g");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the list of chores which are currently running.
	 *
	 * @param __sys If {@code true} then system chores are included in the
	 * list.
	 * @return An array containing the available chores.
	 * @throws SecurityException If obtaining the list of chores is not
	 * permitted.
	 * @since 2017/12/08
	 */
	public final Chore[] list(boolean __sys)
		throws SecurityException
	{
		// {@squirreljme.error ZZ0f The current chore is not permitted to
		// list the available chores.}
		if (0 == (this.currentGroup().basicPermissions() &
			(ChoreGroup.BASIC_PERMISSION_CLIENT_MANAGE_TASKS |
			ChoreGroup.BASIC_PERMISSION_CROSSCLIENT_MANAGE_TASKS)))
			throw new SecurityException("ZZ0f");
		
		return this.internalList(__sys);
	}
}

