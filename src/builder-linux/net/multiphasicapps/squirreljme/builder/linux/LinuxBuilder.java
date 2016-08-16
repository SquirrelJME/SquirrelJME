// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.TargetBuilder;
import net.multiphasicapps.squirreljme.exe.elf.ELFOutput;
import net.multiphasicapps.squirreljme.exe.elf.ELFProgram;
import net.multiphasicapps.squirreljme.exe.elf.ELFProgramFlag;
import net.multiphasicapps.squirreljme.exe.elf.ELFType;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This is the base buidler for all Linux based systems.
 *
 * @since 2016/08/15
 */
public abstract class LinuxBuilder
	extends TargetBuilder
{
	/**
	 * Initializes the base builder.
	 *
	 * @param __cj Does this support the JIT?
	 * @param __sug Suggested targets.
	 * @since 2016/08/15
	 */
	public LinuxBuilder(boolean __cj, String... __sug)
	{
		super(__cj, __sug);
	}
	
	/**
	 * Initializes more ELF details in an architecture dependent manner.
	 *
	 * @param __conf The build configuration.
	 * @param __eo The output to use.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	protected abstract void dependentELF(BuildConfig __conf, ELFOutput __eo)
		throws IOException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
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
		
		// Setup ELF output
		JITTriplet triplet = __conf.triplet();
		try (OutputStream bin = TargetBuilder.hexDump(
			__zsw.nextEntry("squirreljme",
			ZipCompressionType.DEFAULT_COMPRESSION)))
		{
			// Setup target ELF
			ELFOutput eo = new ELFOutput();
			
			// Prime it (even though it is not needed for ELF)
			eo.primeOutput(bin);
			
			// Set some basic common details
			eo.setEndianess(triplet.endianess());
			eo.setWordSize(triplet.bits());
			eo.setOSABI(0x03);
			eo.setType(ELFType.EXECUTABLE);
			
			// Set properties
			super.addStandardSystemProperties(__conf, eo);
			
			// Add the used classpath
			super.addVirtualMachineClassPath(__conf, eo, __vmcp);
			
			// Add namespaces
			int n = __names.length;
			for (int i = 0; i < n; i++)
			{
				ELFProgram p = eo.insertNamespace(__names[i], __blobs[i]);
				
				// Make executable
				p.setFlags(ELFProgramFlag.EXECUTE, ELFProgramFlag.READ);
			}
			
			// Set system dependent stuff
			dependentELF(__conf, eo);
			
			// Generate
			eo.generate(bin);
		}
	}
}

