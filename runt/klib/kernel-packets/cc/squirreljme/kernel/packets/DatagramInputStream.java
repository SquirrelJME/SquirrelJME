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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This wraps an input stream and provides input for datagrams sent from a
 * remote stream.
 *
 * @since 2018/01/17
 */
public final class DatagramInputStream
	implements DatagramIn
{
	/** The input data source. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the datagram input.
	 *
	 * @param __in The stream to read datagrams from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	public DatagramInputStream(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = ((__in instanceof DataInputStream) ?
			(DataInputStream)__in : new DataInputStream(__in));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public final Packet read(int[] __key)
		throws ArrayIndexOutOfBoundsException, DatagramIOException,
			NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		if (__key.length < 1)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
}

