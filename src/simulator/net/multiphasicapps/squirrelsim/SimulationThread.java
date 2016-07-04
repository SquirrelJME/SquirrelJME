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

/**
 * This represents the base class for a simulated thread which exists within
 * a simulation. A thread has its own state for CPU registers and such.
 *
 * @since 2016/07/04
 */
public abstract class SimulationThread
{
	/** The owning simulation. */
	protected final Simulation simulation;
	
	/**
	 * Initializes the base thread information.
	 *
	 * @param __sim The owning simulation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public SimulationThread(Simulation __sim)
		throws NullPointerException
	{
		// Check
		if (__sim == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.simulation = __sim;
	}
	
	/**
	 * Runs a single cycle within this thread.
	 *
	 * @return {@code true} if the thread is still alive.
	 * @since 2016/07/04
	 */
	public final boolean runCycle()
	{
		throw new Error("TODO");
	}
}

