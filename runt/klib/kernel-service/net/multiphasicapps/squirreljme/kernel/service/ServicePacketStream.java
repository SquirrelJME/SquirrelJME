// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.service;

import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;

/**
 * This is a wrapped packet stream which .
 *
 * @since 2018/01/05
 */
public final class ServicePacketStream
{
	/**
	 * Initializes the service packet stream.
	 *
	 * @param __ps The stream to the server.
	 * @param __dx The index of the service to send under.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public ServicePacketStream(PacketStream __ps, int __dx)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

