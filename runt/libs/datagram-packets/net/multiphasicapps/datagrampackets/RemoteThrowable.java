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

import java.io.PrintStream;

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
public interface RemoteThrowable
{
	/**
	 * Returns the details of the thrown exception.
	 *
	 * @return The exception details.
	 * @since 2018/01/07
	 */
	public abstract RemoteThrowableDetail detail();
	
	/**
	 * Prints the local stack trace.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/07
	 */
	public abstract void printLocalStackTrace(PrintStream __ps)
		throws NullPointerException;
}

