// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITCPUVariant;

/**
 * This contains a simulation which provides a simulated execution environment
 * for running simple programs and for testing and porting of SquirrelJME for
 * multiple systems.
 *
 * @since 2016/06/14
 */
public abstract class Simulation
{
	/** The emulation configuration. */
	protected final SimulationStartConfig config;

	/** The owning simulation group. */
	protected final SimulationGroup group;
	
	/** The executable program bytes. */
	protected final byte[] executablebytes;
	
	/** Threads which are active in the simulated process. */
	private final List<SimulationThread> _threads =
		new ArrayList<>();
	
	/**
	 * This initializes the base simulation.
	 *
	 * @param __sc The configuration used to start the simulation.
	 * @throws NullPointerException On null arguments.
	 * @throws SimulationStartException If the simulation failed to start.
	 * @since 2016/06/14
	 */
	public Simulation(SimulationStartConfig __sc)
		throws NullPointerException, SimulationStartException
	{
		// Check
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __sc;
		this.group = __sc.group();
		
		// Load the executable's bytes which are needed before it may be ran
		try
		{
			this.executablebytes = __sc.executableBytes();
		}
		
		// {@squirreljme.error BV05 Could not load the bytes which make up
		// the program.}
		catch (IOException e)
		{
			throw new SimulationStartException("BV05", e);
		}
	}
	
	/**
	 * Runs a simulation cycle.
	 *
	 * @return If {@code true} the simulation still exists, otherwise if
	 * {@code false} then the simulation has ended.
	 * @since 2016/06/15
	 */
	public final boolean runCycle()
	{
		// Lock
		int count = 0;
		List<SimulationThread> threads = this._threads;
		synchronized (threads)
		{
			// Run all threads
			int n = threads.size();
			for (int i = 0; i < n; i++)
			{
				// Get
				SimulationThread thr = threads.get(i);
				
				// Run cycle, clear if it ended
				if (thr != null)
				{
					if (!thr.runCycle())
						threads.set(i, null);
					else
						count++;
				}
			}
			
			// Stop if no threads ran
			return (count != 0);
		}
	}
}

