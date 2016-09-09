// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux.powerpc;

import java.io.IOException;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.powerpc.PowerPCABI;

/**
 * This is the build instance for Linux PowerPC systems.
 *
 * @since 2016/09/02
 */
public class LinuxPowerPCBuildInstance
	extends LinuxBuildInstance
{
	/**
	 * Initializes the build instance.
	 *
	 * @param __conf The build configuration.
	 * @since 2016/09/02
	 */
	public LinuxPowerPCBuildInstance(BuildConfig __conf)
	{
		super(__conf, "powerpc");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	protected void configureLinuxEmulator(EmulatorConfig __conf)
		throws IOException, NullPointerException
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
		// Get target details
		JITTriplet triplet = this.triplet;
		int bits = triplet.bits();
		NativeFloatType ft = triplet.floatingPoint();
		
		// Which ABI to use?
		GenericABI abi;
		String osvar;
		NativeABI nabi;
		switch ((osvar = triplet.operatingSystemVariant()))
		{
				// SysV
			case "sysv":
				nabi = PowerPCABI.sysV(bits, ft);
				break;
				
				// OpenPOWER
			case "openpower":
				nabi = PowerPCABI.openPower(bits, ft);
				break;
				
				// EABI
			case "eabi":
				nabi = PowerPCABI.eabi(bits, ft);
				break;
			
				// {@squirreljme.error BU0a Do not know how to build for the
				// given operating system variant. (The operating system
				// variant)}
			default:
				throw new TargetNotSupportedException(
					String.format("BU0a %s", osvar));
		}
		
		// Wrap
		return new GenericABI(nabi);
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
		throw new Error("TODO");
	}
}

