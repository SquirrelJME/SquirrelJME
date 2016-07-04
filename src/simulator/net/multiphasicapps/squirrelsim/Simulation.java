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

import java.nio.file.Path;
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
	/**
	 * This initializes the base simulation.
	 *
	 * @param __grp The owning simulation group.
	 * @param __prov The provider of the simulation.
	 * @param __archvar The architecture variant.
	 * @param __archend The endian of the CPU.
	 * @param __prog The program to run.
	 * @param __args The program arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public Simulation(SimulationGroup __grp, SimulationProvider __prov,
		JITCPUVariant __archvar, JITCPUEndian __archend, Path __prog,
		String... __args)
		throws NullPointerException
	{
		// Check
		if (__grp == null || __prov == null || __archvar == null ||
			__archend == null || __prog == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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

