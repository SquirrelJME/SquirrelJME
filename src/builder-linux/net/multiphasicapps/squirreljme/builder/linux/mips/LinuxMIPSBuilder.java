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
import net.multiphasicapps.squirreljme.builder.BuildInstance;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuilder;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;

/**
 * This is used to build SquirrelJME for MIPS based Linux systems.
 *
 * @since 2016/07/30
 */
public class LinuxMIPSBuilder
	extends LinuxBuilder
{
	/**
	 * Initializes the Linux MIPS target builder.
	 *
	 * @since 2016/07/30
	 */
	public LinuxMIPSBuilder()
	{
		super(
			"mips-32+i,little~hard32.linux.eabi",
				"Generic Little Endian 32-bit Linux MIPS (Hardware float)",
			"mips-32+i,little~soft.linux.eabi",
				"Generic Little Endian 32-bit Linux MIPS (Software float)",
			"mips-32+i,big~hard32.linux.eabi",
				"Generic Big Endian 32-bit Linux MIPS (Hardware float)",
			"mips-32+i,big~soft.linux.eabi",
				"Generic Big Endian 32-bit Linux MIPS (Software float)",
			"mips-32+mips32,little~hard32.linux.gcwzero",
				"GCW Zero");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public BuildInstance createBuildInstance(BuildConfig __conf) 
		throws TargetNotSupportedException
	{
		return new LinuxMIPSBuildInstance(__conf);
	}
}

