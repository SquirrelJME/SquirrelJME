// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.interpreter;

import java.util.Map;
import net.multiphasicapps.squirreljme.emulator.EmulatorComponent;
import net.multiphasicapps.squirreljme.emulator.EmulatorSystem;

/**
 * This emulates the CPU that the interpreter uses.
 *
 * @since 2016/07/26
 */
public class InterpreterCPU
	extends EmulatorComponent
{
	/** The current clock rate. */
	protected final long clockrate;
	
	/** Picoseconds per clock. */
	protected final long picosperclock;
	
	/** The next clock time index. */
	private volatile long _nextclock;
	
	/**
	 * Initializes the CPU.
	 *
	 * @param __es The owning system.
	 * @param __id The CPU identifier.
	 * @param __args The arguments to the CPU.
	 * @since 2016/07/26
	 */
	public InterpreterCPU(EmulatorSystem __es, String __id, String... __args)
	{
		super(__es, __id, __args);
		
		// Get the clock rate
		Map<String, String> arguments = this.arguments;
		String clockrate = arguments.get("clockrate");
		
		// {@squirreljme.error BK02 No clock rate specified.}
		if (clockrate == null)
			throw new IllegalArgumentException("BK02");
		
		// Set
		long cr = Long.decode(clockrate);
		this.clockrate = cr;
		
		// {@squirreljme.error BK03 Zero or negative clock rate.
		// (The clock rate)}
		if (cr <= 0)
			throw new IllegalArgumentException(String.format("BK03 %d", cr));
		
		// {@squirreljme.error BK04 The lock rate exceeds 1THz. (The clock
		// rate)}
		long ppc = 1_000_000_000_000L / cr;
		if (ppc <= 0)
			throw new IllegalArgumentException(String.format("BK04 %d", cr));
		this.picosperclock = ppc;
		
		// Lock
		synchronized (this.lock)
		{
			// Clock a cycle after the base time
			this._nextclock = this.group().currentTimeIndex() + ppc;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	protected long nextEventTime()
	{
		// Lock
		synchronized (this.lock)
		{
			// Return the next clock pulse
			return this._nextclock;
		}
	}
}

