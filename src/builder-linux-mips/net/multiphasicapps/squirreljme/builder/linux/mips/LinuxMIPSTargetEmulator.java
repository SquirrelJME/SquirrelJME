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
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.linux.mips.LinuxMIPSHyperVisor;

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
		// Create the emulator
		Emulator rv = new Emulator(new LinuxMIPSHyperVisor(
			));
		
		// Start the initial process
		throw new Error("TODO");
	}
}

