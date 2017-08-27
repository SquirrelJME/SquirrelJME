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
public class JITProcessor
{
	/** The configuration for the JIT. */
	protected final JITConfig config;
	
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
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Returns the binary where compiled methods and data sections are placed.
	 *
	 * @return The target for data and code.
	 * @since 2017/08/24
	 */
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
	public final void jitProgram(LinkingPoint __e, HighLevelProgram __hlp)
		throws JITException, NullPointerException
	{
		// Check
		if (__e == null || __hlp == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Opens the input stream as a ZIP file then processes it.
	 *
	 * @param __is The stream to read ZIP file contents from.
	 * @throws IOException If it is not a ZIP or if the stream could not be
	 * read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(InputStream __is)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a ZIP
		process(new ZipStreamReader(__is));
	}
	
	/**
	 * Processes the given ZIP file
	 *
	 * @param __zip The ZIP file to read from.
	 * @throws IOException If the ZIP could not be read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(ZipStreamReader __zip)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Go through the ZIP contents and process the entries
		for (;;)
			try (ZipStreamEntry e = __zip.nextEntry())
			{
				// No more input
				if (e == null)
					break;
				
				// Compiling a class?
				String name = e.name();
				if (name.endsWith(".class"))
					new ClassDecompiler(this, e).run();
				
				// Appending a resource
				else
					throw new todo.TODO();
			}
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the symbol storage for the procesor.
	 *
	 * @return The symbol storage.
	 * @since 2017/08/24
	 */
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
	public final VerificationChecks verifier()
	{
		return this.verifier;
	}
}

