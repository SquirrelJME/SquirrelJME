// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux.powerpc;

import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.BuildInstance;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuilder;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;

/**
 * This is the builder for PowerPC based Linux systems.
 *
 * @since 2016/09/02
 */
public class LinuxPowerPCBuilder
	extends LinuxBuilder
{
	/**
	 * Initializes the Linux PowerPC target builder.
	 *
	 * @since 2016/09/02
	 */
	public LinuxPowerPCBuilder()
	{
		super(
			"powerpc-32+g1,big~soft.linux.sysv",
				"Generic Big Endian 32-bit Linux PowerPC (G1, Software Float)",
			"powerpc-32+g3,big~hard64.linux.sysv",
				"Generic Big Endian 32-bit Linux PowerPC (G3, Hardware Float)",
			"powerpc-64+g5,little~hard64.linux.poweropen",
				"Generic Little Endian 64-bit Linux PowerPC (Hardware Float)",
			"powerpc-64+g5,big~hard64.linux.poweropen",
				"Generic Big Endian 64-bit Linux PowerPC (Hardware Float)");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public BuildInstance createBuildInstance(BuildConfig __conf) 
		throws TargetNotSupportedException
	{
		return new LinuxPowerPCBuildInstance(__conf);
	}
}

