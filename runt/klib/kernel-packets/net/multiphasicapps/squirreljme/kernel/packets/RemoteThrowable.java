// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.packets;

/**
 * This class is thrown by the {@link PacketStream#send(Packet)} method if the
 * remote end threw an exception while handling a packet.
 *
 * This will wrap as much detail as needed from the exception which was caught
 * on the remote end.
 *
 * Responseless packets do not respond with this exception.
 *
 * @since 2018/01/01
 */
public final class RemoteThrowable
	extends RuntimeException
{
}

