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

import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.generic.mips.MIPSABI;
import net.multiphasicapps.squirreljme.jit.generic.mips.MIPSOutputFactory;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This is the build instance for Linux MIPS systems.
 *
 * @since 2016/09/02
 */
public class LinuxMIPSBuildInstance
	extends LinuxBuildInstance
{
	/**
	 * Initializes the build instance.
	 *
	 * @param __conf The build configuration.
	 * @since 2016/09/02
	 */
	public LinuxMIPSBuildInstance(BuildConfig __conf)
	{
		super(__conf, "mips");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	protected void configureLinuxEmulator(EmulatorConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	protected GenericABI getLinuxABI()
	{
		// Which ABI to use?
		GenericABI abi;
		JITTriplet triplet = this.triplet;
		String osvar;
		switch ((osvar = triplet.operatingSystemVariant()))
		{
				// EABI
			case "eabi":
				return MIPSABI.eabi(triplet);
			
				// {@squirreljme.error BU09 Do not know how to build for the
				// given operating system variant. (The operating system
				// variant)}
			default:
				throw new TargetNotSupportedException(
					String.format("BU09 %s", osvar));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	protected void modifyOutputConfig(JITOutputConfig __conf)
	{
		// Add base Linux changes first
		super.modifyOutputConfig(__conf);
		
		// Add the JIT factory
		__conf.<JITOutputFactory>registerObject(JITOutputFactory.class,
			new MIPSOutputFactory());
	}
}

