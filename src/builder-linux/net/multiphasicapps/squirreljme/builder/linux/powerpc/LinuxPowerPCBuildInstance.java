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

import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.generic.powerpc.PowerPCABI;

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
				// SysV
			case "sysv":
				return PowerPCABI.sysV(triplet);
				
				// OpenPOWER
			case "openpower":
				return PowerPCABI.openPower(triplet);
				
				// EABI
			case "eabi":
				return PowerPCABI.eabi(triplet);
			
				// {@squirreljme.error BU0a Do not know how to build for the
				// given operating system variant. (The operating system
				// variant)}
			default:
				throw new TargetNotSupportedException(
					String.format("BU0a %s", osvar));
		}
	}
}

