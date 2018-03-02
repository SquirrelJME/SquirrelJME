// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

import java.io.Closeable;

/**
 * This interface represents a source for incoming datagrams.
 *
 * @since 2018/01/17
 */
public interface DatagramIn
	extends Closeable
{
	/**
	 * {@inheritDoc}
	 * @throws DatagramIOException If the input could not be closed.
	 * @since 2018/01/17
	 */
	@Override
	public abstract void close()
		throws DatagramIOException;
	
	/**
	 * Reads an incoming packet and key from the datagram source.
	 *
	 * Incoming datagrams are permitted to be out of order but they must not
	 * be duplicated.
	 *
	 * @param __key The key which read read from the datagram, only the first
	 * element is written to and this must have a size of at least 1.
	 * @return The read packet.
	 * @throws ArrayIndexOutOfBoundsException If the array is empty.
	 * @throws DatagramIOException If the datagram could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	public abstract Packet read(int[] __key)
		throws ArrayIndexOutOfBoundsException, DatagramIOException,
			NullPointerException;
}

