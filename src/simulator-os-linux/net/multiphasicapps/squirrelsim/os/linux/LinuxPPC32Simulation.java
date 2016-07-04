// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim.os.linux;

import net.multiphasicapps.squirrelsim.Simulation;
import net.multiphasicapps.squirrelsim.SimulationGroup;
import net.multiphasicapps.squirrelsim.SimulationStartConfig;
import net.multiphasicapps.squirrelsim.SimulationStartException;

/**
 * This simulates a 32-bit PowerPC Linux system.
 *
 * @since 2016/07/04
 */
public class LinuxPPC32Simulation
	extends LinuxSimulation
{
	/**
	 * Initializes the Linux simulation.
	 *
	 * @param __sc The starting configuration.
	 * @throws SimulationStartException If the simulation failed to start.
	 * @since 2016/07/04
	 */
	public LinuxPPC32Simulation(SimulationStartConfig __sc)
		throws SimulationStartException
	{
		super(__sc);
	}
}

