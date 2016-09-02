// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.os.linux;

import net.multiphasicapps.squirreljme.emulator.Emulator;

/**
 * This is the base class for all Linux based emulators.
 *
 * @since 2016/08/21
 */
public abstract class LinuxEmulator
	extends Emulator
{
	/** The Linux configuration. */
	protected final LinuxEmulatorConfig linuxconfig;
	
	/**
	 * Initializes the Linux emulator using the specified configuration.
	 *
	 * @param __conf The configuration to use.
	 * @since 2016/08/21
	 */
	public LinuxEmulator(LinuxEmulatorConfig __conf)
	{
		super(__conf);
		
		// Set
		this.linuxconfig = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public LinuxEmulatorConfig config()
	{
		return this.linuxconfig;
	}
}

