// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.packets;

/**
 * This class represents a single packet which may be sent through the
 * interface, it contains a type and data associated with it.
 *
 * The {@link #close()} method is thread safe when used with a
 * {@link PacketFarm}.
 *
 * @since 2018/01/01
 */
public final class Packet
	implements Closeable
{
	/**
	 * Closes this packet and frees the byte array it uses so that it can be
	 * re-used by the packet farm.
	 *
	 * @since 2018/01/01
	 */
	@Override
	public void close()
	{
		throw new todo.TODO();
	}
}

