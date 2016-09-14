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

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITCodeWriter;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This writes methods of a class..
 *
 * @since 2016/09/14
 */
public class BasicMethodWriter
	implements JITMethodWriter
{
	/** The owning namespace. */
	protected final BasicNamespaceWriter namespace;
	
	/** Where native machine code is to be placed. */
	protected final ExtendedDataOutputStream output;
	
	/**
	 * Initializes the method writer.
	 *
	 * @parma __ns The writer for namespaces.
	 * @param __o The class output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicMethodWriter(BasicNamespaceWriter __ns, ExtendedDataOutputStream __o)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.output = __o;
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void endMember()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void noCode()
	{
		throw new Error("TODO");
	}
}

