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
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITCodeWriter;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This writes methods of a class..
 *
 * @since 2016/09/14
 */
@Deprecated
public class BasicMethodWriter
	implements JITMethodWriter
{
	/** The owning namespace. */
	protected final BasicNamespaceWriter namespace;
	
	/** Where native machine code is to be placed. */
	protected final ExtendedDataOutputStream output;
	
	/** The method data. */
	final __Method__ _method;
	
	/** The current code writer. */
	private volatile BasicCodeWriter _code;
	
	/**
	 * Initializes the method writer.
	 *
	 * @param __ns The writer for namespaces.
	 * @param __o The class output stream.
	 * @param __m Method information in the table.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicMethodWriter(BasicNamespaceWriter __ns, ExtendedDataOutputStream __o,
		__Method__ __m)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __o == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.output = __o;
		this._method = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public JITCodeWriter code()
		throws JITException
	{
		// Create table entry
		__Method__ method = this._method;
		__Code__ entry = new __Code__(method);
		
		// Align output and store start position
		ExtendedDataOutputStream output = this.output;
		try
		{
			this.output.align(4);
			entry._startpos = output.size();
		}
		
		// {@squirreljme.error BV0b Could not aligh the output before starting
		// to write code.}
		catch (IOException e)
		{
			throw new JITException("BV0b", e);
		}
		
		// Add to namespace
		BasicNamespaceWriter namespace = this.namespace;
		method._codedx = namespace.__addCode(entry);
		
		// Create wrapping code
		BasicCodeWriter code = new BasicCodeWriter(namespace, output, entry);
		this._code = code;
		return code;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void endMember()
		throws JITException
	{
		// If there is being output, close it
		BasicCodeWriter code = this._code;
		if (code != null)
		{
			code.close();
			this._code = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void noCode()
	{
		// Do nothing
	}
}

