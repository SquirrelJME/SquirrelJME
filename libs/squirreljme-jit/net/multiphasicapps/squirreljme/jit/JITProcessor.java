// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.bin.LinkingPoint;
import net.multiphasicapps.squirreljme.jit.bin.TemporaryBinary;
import net.multiphasicapps.squirreljme.jit.bin.TemporaryFragmentBuilder;
import net.multiphasicapps.squirreljme.jit.bin.ResourceLinkingPoint;
import net.multiphasicapps.squirreljme.jit.hil.HighLevelProgram;
import net.multiphasicapps.squirreljme.jit.java.ClassDecompiler;
import net.multiphasicapps.squirreljme.jit.symbols.Symbols;
import net.multiphasicapps.squirreljme.jit.verifier.VerificationChecks;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class is used to process classes and resources for compilation for the
 * JIT.
 *
 * @since 2017/08/24
 */
@Deprecated
public class JITProcessor
{
	/** The configuration for the JIT. */
	protected final JITConfig config;
	
	/** The progresss notifier for the JIT. */
	protected final JITProgressNotifier notifier;
	
	/** The binary where the output is placed into. */
	protected final TemporaryBinary binary =
		new TemporaryBinary();
	
	/** Class symbols. */
	protected final Symbols symbols =
		new Symbols();
	
	/** Verification checks that may be performed. */
	protected final VerificationChecks verifier =
		new VerificationChecks();
	
	/**
	 * The configuration used for the JIT.
	 *
	 * @param __conf The JIT configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public JITProcessor(JITConfig __conf)
		throws NullPointerException
	{
		this(__conf, new NullProgressNotifier());
	}
	
	/**
	 * The configuration used for the JIT.
	 *
	 * @param __conf The JIT configuration.
	 * @param __notifier Notifier for events which are happening within the
	 * processor.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public JITProcessor(JITConfig __conf, JITProgressNotifier __notifier)
	{
		// Check
		if (__conf == null || __notifier == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.notifier = __notifier;
	}
	
	/**
	 * Returns the binary where compiled methods and data sections are placed.
	 *
	 * @return The target for data and code.
	 * @since 2017/08/24
	 */
	@Deprecated
	public final TemporaryBinary binary()
	{
		return this.binary;
	}
	
	/**
	 * Returns the JIT configuration this processor is using.
	 *
	 * @return The configuration to use during compilation.
	 * @since 2017/08/24
	 */
	public final JITConfig config()
	{
		return this.config;
	}
	
	/**
	 * JITs the specified high level program. 
	 *
	 * @param __e The program entry point.
	 * @param __hlp The high level program to JIT.
	 * @throws JITException The program to JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/27
	 */
	@Deprecated
	public final void jitProgram(LinkingPoint __e, HighLevelProgram __hlp)
		throws JITException, NullPointerException
	{
		// Check
		if (__e == null || __hlp == null)
			throw new NullPointerException("NARG");
		
		// Timed execution
		JITProgressNotifier notifier = this.notifier;
		long start = System.nanoTime();
		try
		{
			// Specify start
			notifier.jitStartHighLevelProgram(__e);
			
			throw new todo.TODO();
		}
		
		// Report time
		finally
		{
			notifier.jitEndHighLevelProgram(__e, System.nanoTime() - start);
		}
	}
	
	/**
	 * Opens the input stream as a ZIP file then processes it.
	 *
	 * @param __n The name of the input ZIP file.
	 * @param __is The stream to read ZIP file contents from.
	 * @throws IOException If it is not a ZIP or if the stream could not be
	 * read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(String __n, InputStream __is)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a ZIP
		process(__n, new ZipStreamReader(__is));
	}
	
	/**
	 * Processes the given ZIP file
	 *
	 * @param __n The name of the input ZIP file.
	 * @param __zip The ZIP file to read from.
	 * @throws IOException If the ZIP could not be read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(String __n, ZipStreamReader __zip)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__n == null || __zip == null)
			throw new NullPointerException("NARG");
		
		// Log processing
		JITProgressNotifier notifier = this.notifier;
		notifier.beginJar(__n);
		
		// Go through the ZIP contents and process the entries
		int numrc = 0, numcl = 0;
		for (;;)
			try (ZipStreamEntry e = __zip.nextEntry())
			{
				// No more input
				if (e == null)
					break;
				
				// Compiling a class?
				String name = e.name();
				if (name.endsWith(".class"))
				{
					notifier.processClass(__n, name, ++numcl);
					new ClassDecompiler(this, e).run();
				}
				
				// Appending a resource
				else
				{
					notifier.processResource(__n, name, ++numrc);
					__processResource(__n, name, e);
				}
			}
		
		// Report results
		notifier.endJar(__n, numrc, numcl);
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the symbol storage for the procesor.
	 *
	 * @return The symbol storage.
	 * @since 2017/08/24
	 */
	@Deprecated
	public final Symbols symbols()
	{
		return this.symbols;
	}
	
	/**
	 * Returns the verifier for this processor.
	 *
	 * @return The verifier.
	 * @since 2017/08/24
	 */
	@Deprecated
	public final VerificationChecks verifier()
	{
		return this.verifier;
	}
	
	/**
	 * Processes the specified resource.
	 *
	 * @param __n The owning JAR.
	 * @param __rc The name of the resource being processed.
	 * @param __in The stream containing the resource data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	private final void __processResource(String __n, String __rc,
		InputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null || __rc == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Copy data into a fragment
		TemporaryFragmentBuilder tfb = new TemporaryFragmentBuilder();
		byte[] buf = new byte[512];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				break;
			
			tfb.append(buf, 0, rc);
		}
		
		// Link into the program
		this.binary.link(new ResourceLinkingPoint(__n, __rc), tfb.build());
	}
}

