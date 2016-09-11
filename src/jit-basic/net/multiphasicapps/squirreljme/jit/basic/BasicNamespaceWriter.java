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

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This is the namespace writer which is used ot write.
 *
 * @since 2016/09/11
 */
public class BasicNamespaceWriter
	implements JITNamespaceWriter
{
	/** The configuration. */
	protected final JITConfig config;
	
	/** The namespace name. */
	protected final String name;
	
	/**
	 * Initializes the namespace writer.
	 *
	 * @param __conf The configuration used.
	 * @param __name The name of the namespace.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public BasicNamespaceWriter(JITConfig __conf, String __name)
		throws NullPointerException
	{
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
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
	 * @since 2016/09/11
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
	 * @since 2016/09/11
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
}

