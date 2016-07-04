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
 * This is used to store the initial configuration which would be used to
 * start a given simulator.
 *
 * @since 2016/07/04
 */
public class SimulationStartConfig
{
	/**
	 * Creates a new starting configuration.
	 *
	 * @param __grp The owning simulation group.
	 * @param __arch The architecture to simulate.
	 * @param __archvar The variant of the architecture.
	 * @param __archend The endianess of the architecture.
	 * @param __os The operating system to target.
	 * @param __osvar The variant of the operating system to target.
	 * @param __prog The path to the program to run.
	 * @param __args The arguments to the program.
	 * @return The newly created simulation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	SimulationStartConfig(SimulationGroup __grp, String __arch,
		JITCPUVariant __archvar, JITCPUEndian __archend, String __os,
		String __osvar, Path __prog, String... __args)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
}

