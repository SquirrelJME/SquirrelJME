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
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.basic.BasicOutputFactory;
import net.multiphasicapps.squirreljme.jit.base.JITConfig;
import net.multiphasicapps.squirreljme.jit.base.JITConfigBuilder;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;
import net.multiphasicapps.squirreljme.nativecode.mips.MIPSABI;
import net.multiphasicapps.squirreljme.nativecode.mips.MIPSWriterFactory;

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
	protected void modifyOutputConfig(JITConfigBuilder __conf)
	{
		// Add base Linux changes first
		super.modifyOutputConfig(__conf);
		
		// Add the native code generator to use
		__conf.setProperty(BasicOutputFactory.NATIVE_CODE_WRITER_PROPERTY,
			MIPSWriterFactory.class.getName());
		
		// Add the native code generator to use
		__conf.setProperty(BasicOutputFactory.NATIVE_ABI_PROPERTY,
			__conf.triplet().operatingSystemVariant());
		__conf.setProperty(BasicOutputFactory.NATIVE_ABI_FACTORY_PROPERTY,
			MIPSABI.class.getName());
	}
}

