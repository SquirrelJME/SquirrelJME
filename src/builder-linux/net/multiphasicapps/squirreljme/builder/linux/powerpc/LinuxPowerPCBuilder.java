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

import net.multiphasicapps.squirreljme.builder.linux.LinuxBuilder;

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
		super(false,
			"powerpc-32+g1,big~soft.linux.sysv",
				"Linux PowerPC (32-bit/G1/Softfloat/Little/SysV)",
			"powerpc-32+g3,big~hard.linux.sysv",
				"Linux PowerPC (32-bit/G3/Hardfloat/Little/SysV)",
			"powerpc-64+g5,little~hard.linux.poweropen",
				"Linux PowerPC (64-bit/G5/Hardfloat/Little/PowerOpen)",
			"powerpc-64+g5,big~hard.linux.poweropen",
				"Linux PowerPC (64-bit/G5/Hardfloat/Big/PowerOpen)");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	protected void dependentELF(BuildConfig __conf, ELFOutput __eo)
		throws IOException, NullPointerException
	{
		// Check
		if (__conf == null || __eo == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public TargetEmulator emulate(TargetEmulatorArguments __args)
		throws IllegalArgumentException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public void outputConfig(JITOutputConfig __conf, BuildConfig __bc)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Rewrite calls
		__conf.addStaticCallRewrite(new JITClassNameRewrite(
			ClassNameSymbol.of(
				"net/multiphasicapps/squirreljme/unsafe/SquirrelJME"),
			ClassNameSymbol.of(
				"net/multiphasicapps/squirreljme/os/linux/powerpc/" +
				"SquirrelJME")));
		
		// Which ABI to use?
		GenericABI abi;
		JITTriplet triplet = __bc.triplet();
		String osvar;
		switch ((osvar = triplet.operatingSystemVariant()))
		{
				// SysV
			case "sysv":
				throw new Error("TODO");
				
				// EABI
			case "eabi":
				throw new Error("TODO");
				
				// PowerOpen
			case "poweropen":
				throw new Error("TODO");
			
				// {@squirreljme.error BT01 Do not know how to build for the
				// given operating system variant. (The operating system
				// variant)}
			default:
				throw new JITException(String.format("BU01 %s", osvar));
		}
		
		// Use the given ABI
		__conf.<GenericABI>registerObject(GenericABI.class, abi);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Any Linux PowerPC system
		JITTriplet triplet = __conf.triplet();
		return triplet.architecture().equals("powerpc") &&
			triplet.operatingSystem().equals("linux");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public String targetPackageGroup(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Linux something
		return "linux-powerpc-" + __conf.triplet().operatingSystemVariant();
	}
}

