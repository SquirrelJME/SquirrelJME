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
import net.multiphasicapps.squirreljme.jit.generic.
	GenericAllocatorFactory;
import net.multiphasicapps.squirreljme.jit.JITClassNameRewrite;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.mips.MIPSAllocatorFactory;
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
			"mips-32+i,little~hard.linux.generic",
				"Generic Linux MIPS (Little Endian)",
			"mips-32+i,big~hard.linux.generic",
				"Generic Linux MIPS (Big Endian)",
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
		
		// Nothing needs to be done
		__conf.addStaticCallRewrite(new JITClassNameRewrite(
			ClassNameSymbol.of(
				"net/multiphasicapps/squirreljme/unsafe/SquirrelJME"),
			ClassNameSymbol.of(
				"net/multiphasicapps/squirreljme/os/linux/mips/SquirrelJME")));
		
		// Fill in list of GPRs
		JITTriplet t = __bc.triplet();
		boolean usefloat = t.floatingPoint().isAnyHardware();
		List<MIPSRegister> gprs = new ArrayList<>();
		for (int i = 1; i < 32; i++)
		{
			// SquirrelJME does not have to call any library and uses system
			// calls in wrapper methods, as such every register is available
			// for usage
			if (i >= 1 && i <= 25)
				gprs.add(MIPSRegister.of(t, i, false));
			
			// Any floating point register is valid
			if (usefloat)
				gprs.add(MIPSRegister.of(t, i, true));
		}
		
		// Use a register allocator that is Linux friendly
		__conf.<GenericAllocatorFactory>registerObject(
			GenericAllocatorFactory.class,
			new MIPSAllocatorFactory(t, MIPSRegister.of(t, 29, false), true,
			gprs.<MIPSRegister>toArray(new MIPSRegister[gprs.size()])));
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

