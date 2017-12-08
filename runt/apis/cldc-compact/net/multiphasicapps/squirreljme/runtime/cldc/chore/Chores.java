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
		throw new todo.TODO();
	}
}

