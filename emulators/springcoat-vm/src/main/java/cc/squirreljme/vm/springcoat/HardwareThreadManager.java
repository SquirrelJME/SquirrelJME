// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.jvm.HardwareThreadControl;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallException;
import cc.squirreljme.jvm.SystemCallIndex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages hardware threads which run within the virtual machine.
 *
 * @since 2020/05/02
 */
public class HardwareThreadManager
{
	/** The profiler used. */
	protected final ProfilerSnapshot profiler;
	
	/** Threads which are available. */
	private final Map<Integer, SpringThread> _threads =
		new HashMap<>();
	
	/**
	 * Intiializes the hardware thread manager.
	 *
	 * @param __profiler The profiler to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/02
	 */
	public HardwareThreadManager(ProfilerSnapshot __profiler)
		throws NullPointerException
	{
		if (__profiler == null)
			throw new NullPointerException("NARG");
		
		this.profiler = __profiler;
	}
	
	/**
	 * Creates a new thread within the virtual machine.
	 *
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public final SpringThread createThread()
		throws NullPointerException
	{
		// Get unique thread ID
		int id = AtomicTicker.next();
		
		// Create new thread using this ID
		String name = "hwThread-" + Long.toString(id & 0xFFFFFFFL, 16);
		SpringThread rv = new SpringThread(id, name,
			this.profiler.measureThread(name));
		
		// Store thread
		synchronized (this)
		{
			this._threads.put(id, rv);
		}
		
		// Return the newly created thread
		return rv;
	}
	
	/**
	 * Handles system call from a given thread.
	 *
	 * @param __thread The thread this is acting under.
	 * @param __ctrl The control being used.
	 * @param __a A.
	 * @param __b B.
	 * @param __c C.
	 * @param __d D.
	 * @param __e E.
	 * @param __f F.
	 * @param __g G.
	 * @return The result of the system call.
	 * @throws NullPointerException On null arguments.
	 * @throws SystemCallException If there is an issue with any of these
	 * calls.
	 * @see HardwareThreadControl
	 * @since 2020/05/02
	 */
	public long sysCall(SpringThreadWorker __thread, int __ctrl,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g)
		throws NullPointerException, SystemCallException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		switch (__ctrl)
		{
				// Create hardware thread
			case HardwareThreadControl.CONTROL_CREATE_THREAD:
				return this.createThread().id;
				
				// Set thread task ID
			case HardwareThreadControl.CONTROL_THREAD_SET_TASKID:
				
			
				// Unsupported hardware thread call
			default:
				throw new SystemCallException(SystemCallIndex.HW_THREAD,
					(SystemCallError.UNSUPPORTED_SYSTEM_CALL << 8) | __ctrl);
		}
	}
}
