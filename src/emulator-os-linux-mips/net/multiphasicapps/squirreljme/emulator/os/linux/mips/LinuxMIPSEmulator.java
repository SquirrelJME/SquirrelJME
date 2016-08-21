// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.os.linux.mips;

import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxEmulator;

/**
 * This provides an emulator for MIPS based Linux executables.
 *
 * @since 2016/08/21
 */
public class LinuxMIPSEmulator
	extends LinuxEmulator
{
	/** The Linux MIPS configuration to use. */
	protected final LinuxMIPSEmulatorConfig linuxmipsconfig;
	
	/**
	 * Initializes the Linux MIPS emulator.
	 *
	 * @param __conf The configuration to use.
	 * @since 2016/08/21
	 */
	public LinuxMIPSEmulator(LinuxMIPSEmulatorConfig __conf)
	{
		super(__conf);
		
		// Set
		this.linuxmipsconfig = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public LinuxMIPSEmulatorConfig config()
	{
		return this.linuxmipsconfig;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}

