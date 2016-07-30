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

import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;

/**
 * This is used to emulate a target MIPS Linux system.
 *
 * @since 2016/07/30
 */
public class LinuxMIPSEmulator
	extends TargetEmulator
{
	/**
	 * Sets up the emualtor target.
	 *
	 * @param __args The arguments to the emulator.
	 * @since 2016/07/30
	 */
	public LinuxMIPSEmulator(TargetEmulatorArguments __args)
	{
		super(__args);
	}
}

