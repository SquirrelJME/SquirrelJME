// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import net.multiphasicapps.squirreljme.emulator.Emulator;

/**
 * This class is used to setup the target for emulation.
 *
 * @since 2016/07/30
 */
public abstract class TargetEmulator
{
	/** Emulator arguments. */
	protected final TargetEmulatorArguments arguments;
	
	/**
	 * Initializes the base target emulator.
	 *
	 * @param __args The arguments to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public TargetEmulator(TargetEmulatorArguments __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.arguments = __args;
	}
	
	/**
	 * This sets up and returns the emulator capable of emulating SquirrelJME
	 * for a given target.
	 *
	 * @return The target emulator, which is ready to be run.
	 * @since 2016/07/30
	 */
	public abstract Emulator emulator()
		throws IOException;
}

