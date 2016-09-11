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
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This writes resources to the output namespace.
 *
 * @since 2016/09/11
 */
public class BasicResourceWriter
	implements JITResourceWriter
{
	/** The output stream. */
	protected final ExtendedDataOutputStream output;
	
	/**
	 * Initializes the resource writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	BasicResourceWriter(BasicNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __nsw._output;
		
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, JITException, NullPointerException
	{
		throw new Error("TODO");
	}
}

