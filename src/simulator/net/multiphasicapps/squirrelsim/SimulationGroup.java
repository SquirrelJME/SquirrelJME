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

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is a simulation group which contains multiple simulations which are
 * shared under the same group. Simulations that are within the same group
 * may communicate with each other.
 *
 * @since 2016/06/14
 */
public class SimulationGroup
{
	/** The set of simulations which are available for running. */
	protected final List<Simulation> simulations;
	
	/**
	 * Initializes the simulation group.
	 *
	 * @param __confs The configurations which are used to initialize the
	 * simulation with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public SimulationGroup(SimulatorConfiguration... __confs)
		throws NullPointerException
	{
		// Check
		if (__confs == null)
			throw new NullPointerException("NARG");
		
		// Load all simulations
		int n = __confs.length;
		Simulation[] sims = new Simulation[n];
		for (int i = 0; i < n; i++)
			sims[i] = new Simulation(this, __confs[i]);
		
		// Lock in
		this.simulations = UnmodifiableList.<Simulation>of(
			Arrays.<Simulation>asList(sims));
		
		throw new Error("TODO");
	}
}

