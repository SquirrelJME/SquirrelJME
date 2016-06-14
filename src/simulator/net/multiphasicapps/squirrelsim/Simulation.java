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
public class Simulation
{
	/** The group which owns this simulation. */
	protected final SimulationGroup group;

	/**
	 * This initializes a simulation which exists as part of a simulation
	 * group and is initialized with the given configuration.
	 *
	 * @param __grp The group owning this simulation.
	 * @param __conf The configuration to initialize the simulation with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public Simulation(SimulationGroup __grp, SimulatorConfiguration __conf)
		throws NullPointerException
	{
		// Check
		if (__grp == null || __conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __grp;
		
		throw new Error("TODO");
	}
}

