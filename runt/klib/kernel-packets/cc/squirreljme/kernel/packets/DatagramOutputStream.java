// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This wraps an output stream and allows datagrams to be written to the
 * remote side.
 *
 * @since 2018/01/17
 */
public final class DatagramOutputStream
	implements DatagramOut
{
	/** The output data sink. */
	protected final DataOutputStream out;
	
	/**
	 * Initializes the datagram output.
	 *
	 * @param __out The stream to write datagrams to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	public DatagramOutputStream(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = ((__out instanceof DataOutputStream) ?
			(DataOutputStream)__out : new DataOutputStream(__out));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public final void write(int __key, Packet __p)
		throws DatagramIOException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

