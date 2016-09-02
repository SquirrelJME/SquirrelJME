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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuilder;
import net.multiphasicapps.squirreljme.builder.TargetBuilder;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;
import net.multiphasicapps.squirreljme.exe.elf.ELFOutput;
import net.multiphasicapps.squirreljme.exe.elf.ELFProgram;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericABI;
import net.multiphasicapps.squirreljme.jit.JITClassNameRewrite;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.mips.MIPSABI;
import net.multiphasicapps.squirreljme.jit.mips.MIPSRegister;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

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
		super(false,
			"mips-32+i,little~hard.linux.eabi",
				"Generic Linux MIPS (Hardfloat, Little, EABI)",
			"mips-32+i,little~soft.linux.eabi",
				"Generic Linux MIPS (Softfloat, Little, EABI)",
			"mips-32+i,big~hard.linux.eabi",
				"Generic Linux MIPS (Hardfloat, Big, EABI)",
			"mips-32+i,big~soft.linux.eabi",
				"Generic Linux MIPS (Softfloat, Big, EABI)",
			"mips-32+mips32,little~hard.linux.gcwzero",
				"GCW Zero");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/15
	 */
	@Override
	protected void dependentELF(BuildConfig __conf, ELFOutput __eo)
		throws IOException, NullPointerException
	{
		// Check
		if (__conf == null || __eo == null)
			throw new NullPointerException("NARG");
		
		// Just load the ELF anywhere really
		__eo.setBaseAddress(0x400000);
		
		// Get triplet to determine later things
		JITTriplet triplet = __conf.triplet();
		int bits = triplet.bits();
		
		// Align programs to the MIPS page size
		boolean inital = true;
		for (ELFProgram p : __eo.programs())
		{
			p.setAlignment((inital ? 8 : 4));
			inital = false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public TargetEmulator emulate(TargetEmulatorArguments __args)
		throws IllegalArgumentException, NullPointerException
	{
		return new LinuxMIPSTargetEmulator(__args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
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
				"net/multiphasicapps/squirreljme/os/linux/mips/SquirrelJME")));
		
		// Which ABI to use?
		GenericABI abi;
		JITTriplet triplet = __bc.triplet();
		String osvar;
		switch ((osvar = triplet.operatingSystemVariant()))
		{
				// EABI
			case "eabi":
				abi = MIPSABI.eabi(triplet);
				break;
			
				// {@squirreljme.error BU01 Do not know how to build for the
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
	 * @since 2016/07/30
	 */
	@Override
	public boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Any Linux MIPS system
		JITTriplet triplet = __conf.triplet();
		return triplet.architecture().equals("mips") &&
			triplet.operatingSystem().equals("linux");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public String targetPackageGroup(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Linux something
		return "linux-mips-" + __conf.triplet().operatingSystemVariant();
	}
}

