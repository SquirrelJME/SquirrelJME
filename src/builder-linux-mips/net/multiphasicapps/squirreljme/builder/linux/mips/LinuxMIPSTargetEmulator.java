// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux.mips;

import java.io.IOException;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;
import net.multiphasicapps.squirreljme.chv.linux.mips.LinuxMIPSHypoVisor;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.mips.MIPSEmulator;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * This is used to emulate a target MIPS Linux system.
 *
 * @since 2016/07/30
 */
public class LinuxMIPSTargetEmulator
	extends TargetEmulator
{
	/**
	 * Sets up the emualtor target.
	 *
	 * @param __args The arguments to the emulator.
	 * @since 2016/07/30
	 */
	public LinuxMIPSTargetEmulator(TargetEmulatorArguments __args)
	{
		super(__args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public Emulator emulator()
		throws IOException
	{
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
		return rv;
	}
}

