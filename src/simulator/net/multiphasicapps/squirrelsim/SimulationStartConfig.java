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
public final class SimulationStartConfig
{
	/** The owning simulation group. */
	protected final SimulationGroup group;
	
	/** The architecture to simulate. */
	protected final String arch;
	
	/** The variant of the architecture to simulate. */
	protected final JITCPUVariant archvar;
	
	/** The endianess of the architecture to simulate. */
	protected final JITCPUEndian archend;
	
	/** The operating system to simulate. */
	protected final String os;
	
	/** The variant of the operating system to simulate. */
	protected final String osvar;
	
	/** The program to run. */
	protected final Path program;
	
	/** The arguments to the program. */
	private final String[] _args;
	
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
		// Check
		if (__grp == null || __arch == null || __archvar == null ||
			__archend == null || __os == null || __osvar == null ||
			__prog == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __grp;
		this.arch = __arch;
		this.archvar = __archvar;
		this.archend = __archend;
		this.os = __os;
		this.osvar = __os;
		this.program = __prog;
		this._args = (__args == null ? new String[0] : __args.clone());
	}
}

