// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang;

import java.io.Flushable;
import java.io.IOException;
import java.io.PrintStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;

/**
 * This class acts as the base for outputing class based code which uses
 * an unspecified language.
 *
 * @since 2016/07/17
 */
public abstract class LangClassWriter
	implements Flushable, JITClassWriter
{
	/** The target stream to write to. */
	protected final PrintStream output;
	
	/** The basic name. */
	protected final String name;
	
	/** The name of the class to write. */
	protected final ClassNameSymbol classname;
	
	/**
	 * Initializes the base writer support for classes.
	 *
	 * @param __n The basic name of class being written.
	 * @param __cn The symbol name of the class being written.
	 * @param __ps The output file stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public LangClassWriter(String __n, ClassNameSymbol __cn, PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__n == null || __cn == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.classname = __cn;
		this.output = __ps;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void close()
	{
		// Close the output
		this.output.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// Flush the output
		this.output.flush();
	}
}

