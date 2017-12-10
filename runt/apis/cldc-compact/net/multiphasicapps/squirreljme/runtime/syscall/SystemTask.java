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
 * This interface is used to represent generic access to a task which exists
 * and may be running within the kernel.
 *
 * Since the kernel always uses tasks for permission checking, the instance
 * of this class should pass that information to the kernel as needed.
 *
 * Instances of this class will be used as keys so it must implement
 * {@link #equals(Object)} and {@link #hashCode()} to where even two
 * instances of this class which point to the same program refer to that
 * instance.
 *
 * @since 2017/12/10
 */
public interface SystemTask
{
	/**
	 * Is this a system task?
	 *
	 * @return {@code true} if this is a system task.
	 * @since 2017/12/10
	 */
	public abstract boolean isSystem();

	/**
	 * Returns the main entry point of this task.
	 *
	 * @return The task main entry point.
	 * @since 2017/12/10
	 */
	public abstract String mainClass();
	
	/**
	 * Returns the value of the given metric for this task.
	 *
	 * @param __m The metric to get.
	 * @return The value for this metric.
	 * @since 2017/12/10
	 */
	public abstract long metric(int __m);
	
	/**
	 * Returns the priority of the task, the lower the value the more
	 * priority it has.
	 *
	 * @return The task priority.
	 * @since 2017/12/20
	 */
	public abstract int priority();
	
	/**
	 * Returns the program which this task is currently running under.
	 *
	 * @return The system program for this task.
	 * @since 2017/12/10
	 */
	public abstract SystemProgram program();
	
	/**
	 * Attempts to restart the specified task, if permitted to.
	 *
	 * This should perform the same operation as destroying and starting it
	 * but re-using the ID of the task.
	 *
	 * @since 2017/12/10
	 */
	public abstract void restart();
	
	/**
	 * Returns the current status of the task.
	 *
	 * @return The task status.
	 * @since 2017/12/10
	 */
	public abstract int status();
}

