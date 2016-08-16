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
		try (OutputStream bin = __zsw.nextEntry("squirreljme",
			ZipCompressionType.DEFAULT_COMPRESSION))
		{
			// Setup target ELF
			ELFOutput eo = new ELFOutput();
			
			// Prime it (even though it is not needed for ELF)
			eo.primeOutput(bin);
			
			// Set properties
			super.addStandardSystemProperties(__conf, eo);
			
			// Add the used classpath
			super.addVirtualMachineClassPath(__conf, eo, __vmcp);
			
			// Add namespaces
			int n = __names.length;
			for (int i = 0; i < n; i++)
				eo.insertNamespace(__names[i], __blobs[i]);
			
			// Generate
			eo.generate(bin);
		}
	}
}

