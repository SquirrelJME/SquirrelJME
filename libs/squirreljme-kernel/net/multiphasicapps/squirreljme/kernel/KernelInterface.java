// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This interface is implemented by the CPU and operating system support and
 * is used by the kernel to manage native processes and such.
 *
 * @since 2016/10/31
 */
public interface KernelInterface
{
	/**
	 * If SquirrelJME is running in a host environment which is cooperatively
	 * tasked then this will yield SquirrelJME to let other processes run.
	 *
	 * Otherwise this method does nothing and returns.
	 *
	 * @since 2016/10/31
	 */
	public abstract void cooperativeHostYield();
	
	/**
	 * This returns whether or not the interrupted flag for the kernel was
	 * set.
	 *
	 * The interrupted state must be cleared.
	 *
	 * @return {@code true} if the kernel was interrupted.
	 * @since 2016/10/31
	 */
	public abstract boolean isKernelInterrupted();
	
	/**
	 * Returns the number of loops that must run before a check is made to
	 * see if the kernel is interrupted or if the kernel should yield on the
	 * host.
	 *
	 * @return The number of loop cycles to yield or check for interruption.
	 * @since 2016/10/21
	 */
	public abstract int runCycleCount();
	
	/**
	 * This returns the interface which is used to manage suites which are
	 * installed in SquirrelJME.
	 *
	 * @return The suite interface.
	 * @since 2016/11/01
	 */
	public abstract KernelSuiteInterface suiteInterface();
	
	/**
	 * Returns the threading execution model which determines how threads are
	 * managed by SquirrelJME.
	 *
	 * @return The threading execution model.
	 * @since 2016/10/31
	 */
	public abstract ThreadingExecutionModel threadingExecutionModel();
}

