// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a virtualized process within the virtual machine which contains
 * the task context information along with hardware threads that a process
 * owns.
 *
 * @since 2020/04/28
 */
public final class VirtualProcess
{
	/** The main thread. */
	protected final HardwareThread main;
	
	/** The task ID of this process, this identifies the context. */
	protected final int taskId;
	
	/**
	 * Initializes the virtual process.
	 *
	 * @param __main The main thread for the process.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/28
	 */
	private VirtualProcess(HardwareThread __main)
		throws NullPointerException
	{
		if (__main == null)
			throw new NullPointerException("NARG");
		
		this.main = __main;
		this.taskId = __main.taskId;
	}
	
	/**
	 * Starts this process.
	 *
	 * @since 2020/04/28
	 */
	public final void start()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Waits for this process to terminate.
	 *
	 * @return The exit code of this process.
	 * @throws InterruptedException If this process was interrupted.
	 * @since 2020/04/28
	 */
	public final int waitForExit()
		throws InterruptedException
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Spawns a process.
	 *
	 * @return The resulting process that was spawned.
	 * @since 2020/04/28
	 */
	public static VirtualProcess spawn()
	{
		// Create hardware thread for the main thread, the hardware thread ID
		// will become the task ID
		HardwareThread main = HardwareThread.createThread(
			true, 0);
		VirtualProcess rv = new VirtualProcess(main);
		
		if (true)
		{
			Assembly.breakpoint();
			throw Debugging.todo();
		}
		
		return rv;
	}
}
