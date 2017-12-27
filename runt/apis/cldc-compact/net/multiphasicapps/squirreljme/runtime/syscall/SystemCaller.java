// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.syscall;

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
	 * @since 2017/12/11
	 */
	public abstract SystemProgram[] listPrograms(int __typemask);
	
	/**
	 * Lists tasks which are currently running within the system.
	 *
	 * @param __incsys If {@code true} then system tasks are included.
	 * @return Tasks which are currently running within the system.
	 * @since 2017/12/27
	 */
	public abstract SystemTask[] listTasks(boolean __incsys);
}

