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

import java.util.ArrayList;
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
	private final List<Simulation> _simulations =
		new ArrayList<>(2);
	
	/**
	 * Initializes the simulation group.
	 *
	 * @since 2016/06/14
	 */
	public SimulationGroup()
	{
	}
	
	/**
	 * Goes through all simulations and runs a single cycle.
	 *
	 * @return {@code true} if there are simulations still running.
	 * @since 2016/06/15
	 */
	public final boolean runCycle()
	{
		// Infinite loop mostly
		List<Simulation> sims = this._simulations;
		synchronized (sims)
		{
			// Stop if no simulations remain
			int n = sims.size();
			if (n <= 0)
				return false;
			
			// Go through all simulations
			for (int i = 0; i < n; i++)
				if (!sims.get(i).runCycle())
				{
					sims.remove(i--);
					n -= 1;
				}
			
			// Keep going
			return true;
		}
	}
}

