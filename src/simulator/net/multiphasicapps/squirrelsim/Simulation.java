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
 * This contains a simulation which provides a simulated execution environment
 * for running simple programs and for testing and porting of SquirrelJME for
 * multiple systems.
 *
 * @since 2016/06/14
 */
public abstract class Simulation
{
	/** The group which owns this simulation. */
	protected final SimulationGroup group;
	
	/**
	 * This initializes a simulation which exists as part of a simulation
	 * group and is initialized with the given configuration.
	 *
	 * @param __grp The group owning this simulation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public Simulation(SimulationGroup __grp)
		throws NullPointerException
	{
		// Check
		if (__grp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __grp;
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
		throw new Error("TODO");
	}
}

