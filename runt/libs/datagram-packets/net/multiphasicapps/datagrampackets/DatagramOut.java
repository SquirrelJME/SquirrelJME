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
 * This interface represents a destination for output datagrams.
 *
 * @since 2018/01/17
 */
public interface DatagramOut
	extends Closeable
{
	/**
	 * {@inheritDoc}
	 * @throws DatagramIOException If the output could not be closed.
	 * @since 2018/01/17
	 */
	@Override
	public abstract void close()
		throws DatagramIOException;
	
	/**
	 * Writes the specified packet to the output source which will be written
	 * to the other side.
	 *
	 * Datagrams may be sent out of order and they must not be duplicated.
	 *
	 * @param __key The key which is associated with the packet, this is used
	 * to handle responses from the remote stream.
	 * @param __p The packet to write.
	 * @throws DatagramIOException If the packet could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	public abstract void write(int __key, Packet __p)
		throws DatagramIOException, NullPointerException;
}

