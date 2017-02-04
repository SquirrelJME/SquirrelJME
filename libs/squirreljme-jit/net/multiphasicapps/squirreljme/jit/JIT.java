// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.classformat.ClassDecoder;
import net.multiphasicapps.squirreljme.classformat.ClassDescriptionStream;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;

/**
 * This is the JIT which reads an input class and translates the byte code
 * into bare objects with machine code using the given translation engine.
 *
 * @since 2017/01/30
 */
public class JIT
{
	/** The translation engine provider. */
	protected final TranslationEngineProvider engineprovider;
	
	/** The class decoder. */
	protected final ClassDecoder decoder;
	
	/** The output executrable. */
	protected final JITExecutableBuilder output =
		new JITExecutableBuilder();
	
	/**
	 * Initialzes the JIT to decode the given class.
	 *
	 * @param __is The class data to decode.
	 * @param __tep The translation engine for machine code.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/30
	 */
	public JIT(InputStream __is, TranslationEngineProvider __tep)
		throws NullPointerException
	{
		// Check
		if (__is == null || __tep == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.engineprovider = __tep;
		
		// Initialize the decoder
		this.decoder = new ClassDecoder(__is, new __JITClassStream__(this,
			this.output));
	}
	
	/**
	 * Performs JIT compilation.
	 *
	 * @return The resulting executable class which could be cached or directly
	 * executed when loaded in memory.
	 * @throws JITException If JIT compilation fails.
	 * @since 2017/01/30
	 */
	public ExecutableClass compile()
		throws JITException
	{
		// Wrap most exceptions
		try
		{
			// Decode
			this.decoder.decode();
			
			// Setup output executable class
			return this.output.build();
		}
		
		// {@squirreljme.error ED01 Failed to perform JIT compilation.}
		catch (IOException|OutOfMemoryError e)
		{
			throw new JITException("ED01", e);
		}
	}
}

