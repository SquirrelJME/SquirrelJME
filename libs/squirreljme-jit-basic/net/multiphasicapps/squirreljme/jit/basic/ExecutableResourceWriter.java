// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This class is used to write resources into the output executable.
 *
 * @since 2016/10/03
 */
public class ExecutableResourceWriter
	implements JITResourceWriter
{
	/** The owning writer. */
	protected final ExecutableNamespaceWriter writer;
	
	/** The name of the resource being written. */
	protected final String name;
	
	/**
	 * Initializes the writer for executable resources.
	 *
	 * @param __nsw The owning writer.
	 * @param __name The name of the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/03
	 */
	public ExecutableResourceWriter(ExecutableNamespaceWriter __nsw,
		String __name)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.writer = __nsw;
		this.name = __name;
		
		// 
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/03
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/03
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, JITException, NullPointerException
	{
		throw new Error("TODO");
	}
}

