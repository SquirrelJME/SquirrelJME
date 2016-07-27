// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This is a generic writer for namespaces which writes standard blobs which
 * are used by all architectures.
 *
 * @since 2016/07/27
 */
public final class GenericNamespaceWriter
	implements JITNamespaceWriter
{
	/** The owner of this writer. */
	protected final GenericOutput owner;
	
	/** The namespace name. */
	protected final String namespace;
	
	/**
	 * Initializes the generic namespace writer.
	 *
	 * @param __go The owning output (used to obtain code generators).
	 * @param __ns The Namespace to be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericNamespaceWriter(GenericOutput __go, String __ns)
		throws NullPointerException
	{
		// Check
		if (__go == null || __ns == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __go;
		this.namespace = __ns;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
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
	 * @since 2016/07/27
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
	 * @since 2016/07/27
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
}

