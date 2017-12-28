// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class acts as the base for classes which need to call the kernel to
 * perform a task.
 *
 * @since 2017/12/10
 */
public abstract class SystemCaller
{
	/**
	 * Lists programs which are available on the system.
	 *
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return Programs which match the specified mask and exist.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/11
	 */
	public abstract SystemProgram[] listPrograms(int __typemask)
		throws SecurityException;
	
	/**
	 * Lists tasks which are currently running within the system.
	 *
	 * @param __incsys If {@code true} then system tasks are included.
	 * @return Tasks which are currently running within the system.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/27
	 */
	public abstract SystemTask[] listTasks(boolean __incsys)
		throws SecurityException;
	
	/**
	 * Locates the specified program by the given index.
	 *
	 * @param __dx The index of the program to get.
	 * @return The program for the given index or {@code null} if not found.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/27
	 */
	public final SystemProgram programByIndex(int __dx)
		throws SecurityException
	{
		for (SystemProgram p : this.listPrograms(~0))
			if (p.index() == __dx)
				return p;
		
		// Not found
		return null;
	}
}

