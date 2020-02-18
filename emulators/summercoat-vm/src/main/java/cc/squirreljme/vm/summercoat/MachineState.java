// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This contains the machine state.
 *
 * @since 2019/06/19
 */
public final class MachineState
{
	/** The virtual machine memory. */
	protected final WritableMemory memory;
	
	/** The profiler snapshot to write to. */
	protected final ProfilerSnapshot profiler;
	
	/** Was the supervisor okay? */
	private volatile boolean _supervisorokay;
	
	/**
	 * Initializes the machine state.
	 *
	 * @param __mem The memory state.
	 * @param __pf The profiler, this is optional.
	 * @throws NullPointerException If no memory was specified.
	 * @since 2019/12/28
	 */
	public MachineState(WritableMemory __mem, ProfilerSnapshot __pf)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
		this.profiler = __pf;
	}
	
	/**
	 * Flags that the supervisor booted okay.
	 *
	 * @since 2019/06/19
	 */
	public final void flagSupervisorOkay()
	{
		synchronized (this)
		{
			this._supervisorokay = true;
		}
	}
	
	/**
	 * Has the supervisor initialized correctly?
	 *
	 * @return If the supervisor initialized it was okay.
	 * @since 2019/06/19
	 */
	public final boolean isSupervisorOkay()
	{
		synchronized (this)
		{
			return this._supervisorokay;
		}
	}
}

