// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.TargetBuilder;
import net.multiphasicapps.squirreljme.emulator.EmulatorGroup;
import net.multiphasicapps.squirreljme.emulator.EmulatorSystem;
import net.multiphasicapps.squirreljme.emulator.interpreter.InterpreterCPU;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;
import net.multiphasicapps.squirreljme.exe.interpreter.
	InterpreterExecutableOutput;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This is the builder which can target the interpreter.
 *
 * @since 2016/07/22
 */
public class InterpreterTargetBuilder
	extends TargetBuilder
{
	/**
	 * Initializes the interpreter target builder.
	 *
	 * @since 2016/07/22
	 */
	public InterpreterTargetBuilder()
	{
		super(false,
			"mips-32+i,big.squirreljme.interpreter",
			"SquirrelJME Test Interpreter");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/25
	 */
	@Override
	public EmulatorGroup emulate(BuildConfig __conf, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__conf == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Setup emulator
		EmulatorGroup eg = new EmulatorGroup(null, createLastRun());
		
		// Create new system for the interpreter
		EmulatorSystem sys = eg.createSystem();
		
		// Add CPU component
		InterpreterCPU cpu = sys.<InterpreterCPU>addComponent(
			InterpreterCPU.class, "cpu", "clockrate", "8000000");
		
		// Use it
		return eg;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/23
	 */
	@Override
	public void linkBinary(ZipStreamWriter __zsw, String[] __names,
		InputStream[] __blobs, BuildConfig __conf, String[] __vmcp)
		throws JITException, IOException, NullPointerException
	{
		// Check
		if (__zsw == null || __names == null || __blobs == null ||
			__conf == null || __vmcp == null)
			throw new NullPointerException("NARG");
		
		// Create binary
		try (OutputStream os = __zsw.nextEntry("squirreljme.int",
			ZipCompressionType.DEFAULT_COMPRESSION))
		{
			// Create executable output
			InterpreterExecutableOutput ieo =
				new InterpreterExecutableOutput();
			
			// Add standard properties
			addStandardSystemProperties(__conf, ieo);
			
			// Add VM paths
			addVirtualMachineClassPath(__conf, ieo, __vmcp);
			
			// Link the binary together
			ieo.linkBinary(os, __names, __blobs);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void outputConfig(JITOutputConfig __conf, BuildConfig __bc)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Interpreted MIPS
		JITTriplet triplet = __conf.triplet();
		return triplet.architecture().equals("mips") &&
			triplet.operatingSystem().equals("squirreljme") &&
			triplet.operatingSystemVariant().equals("interpreter");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/23
	 */
	@Override
	public String targetPackageGroup(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Always just interpreter
		return "interpreter";
	}
}

