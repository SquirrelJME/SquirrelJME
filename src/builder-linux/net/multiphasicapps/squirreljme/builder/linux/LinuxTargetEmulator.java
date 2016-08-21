// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux;

import java.io.IOException;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxEmulator;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxEmulatorConfig;

/**
 * This is the base class for all Linux based emulators.
 *
 * @since 2016/08/21
 */
public abstract class LinuxTargetEmulator
	extends TargetEmulator
{
	/**
	 * Sets up the emulator target.
	 *
	 * @param __args The arguments to the emulator.
	 * @since 2016/08/21
	 */
	public LinuxTargetEmulator(TargetEmulatorArguments __args)
	{
		super(__args);
	}
	
	/**
	 * This creates an architecture dependent Linux emulator.
	 *
	 * @return The Linux emulator.
	 * @since 2016/08/21
	 */
	protected abstract LinuxEmulator createLinuxEmulator();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final Emulator emulator()
		throws IOException
	{
		// Create architecture dependent emulator
		LinuxEmulator rv = createLinuxEmulator();
		LinuxEmulatorConfig conf = rv.config();
		
		if (true)
			throw new Error("TODO");
		
		// Return it
		return rv;
		
		/*
		// Get triplet
		TargetEmulatorArguments arguments = this.arguments;
		JITTriplet triplet = arguments.triplet();
		
		// Create Linux hypervisor
		LinuxMIPSHypoVisor hv = new LinuxMIPSHypoVisor();
		
		// "init" is the process to start (and becomes PID 1)
		hv.setInit(arguments.loadExecutable("squirreljme"),
			arguments.fullArguments("squirreljme"));
		
		// Setup emulator
		MIPSEmulator rv = new MIPSEmulator(hv, triplet.bits(),
			triplet.endianess(), triplet.architectureVariant());
		
		// Return it
		return rv;*/
	}
}

