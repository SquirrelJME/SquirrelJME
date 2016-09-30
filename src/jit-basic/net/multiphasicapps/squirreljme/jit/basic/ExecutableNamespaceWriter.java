// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This is a namespace writer which writes into an executable which may
 * potentially be rewritten in the future.
 *
 * @since 2016/09/30
 */
public class ExecutableNamespaceWriter
	implements JITNamespaceWriter
{
	/** The owning output. */
	protected final BasicOutput basicoutput;
	
	/** The target executable to write into. */
	protected final ExecutableOutput executable;
	
	/** The name of this namespace. */
	protected final String name;
	
	/** The executable stream, may be {@code null} if not used. */
	protected final OutputStream executablestream;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the executable namespace writer.
	 *
	 * @param __bo The owning basic output.
	 * @param __exe The target executable to write into.
	 * @param __ns The name of this namespace.
	 * @param __os The stream to write to if this is a non-shared namespace.
	 * @throws NullPointerException On null arguments, except for {@code __os}.
	 * @since 2016/09/30
	 */
	public ExecutableNamespaceWriter(BasicOutput __bo, ExecutableOutput __exe,
		String __ns, OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__bo == null || __exe == null || __ns == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.basicoutput = __bo;
		this.executable = __exe;
		this.name = __ns;
		this.executablestream = __os;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public JITResourceWriter beginResource(String __name)
		throws JITException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void close()
	{
		// Only close once
		if (this._closed)
			return;
		this._closed = true;
		
		// Write to the executable, if desired
		try
		{
			OutputStream os = this.executablestream;
			if (os != null)
				this.executable.writeOutput(os);
		}
		
		// {@squirreljme.error BV0k Failed to write the executable output.}
		catch (IOException e)
		{
			throw new JITException("BV0k");
		}
	}
}

