// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

import java.io.Closeable;
import java.util.NoSuchElementException;

/**
 * This represents a mailbox which is used as the basis for inter-midlet
 * communication which communicates with non-kernel tasks.
 *
 * @since 2017/12/10
 */
public interface SystemMailBoxConnection
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2017/12/10
	 */
	@Override
	public abstract void close()
		throws SystemMailBoxException;
	
	/**
	 * Receives a single datagram from the input mailbox.
	 *
	 * @param __fd The mailbox to receive data from.
	 * @param __chan An array with a length of least zero, used as output to
	 * specify the channel the data was sent on.
	 * @param __b The output array where data is to be written.
	 * @param __o The starting offset to the output.
	 * @param __l The maximum number of bytes to read.
	 * @param __wait If {@code true} then the operation will block until a
	 * datagram is read or the thread is interrupted.
	 * @return The number of bytes read, or a negative value if the end of
	 * the stream was reached. A value of zero means that a datagram with no
	 * actual data was sent by the remote side.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the length of the array; or the channel array has
	 * a zero length.
	 * @throws ArrayStoreException If there is not enough data in the output
	 * array to store the datagram data, {@code __chan} will contain the
	 * required storage length.
	 * @throws InterruptedException If an interrupt occured waiting for data.
	 * @throws NoSuchElementException If not waiting and there are no datagrams
	 * available.
	 * @throws NullPointerException On null arguments.
	 * @throws SystemMailBoxException If receiving could not happen.
	 * @since 2016/10/13
	 */
	public abstract int receive(int[] __chan, byte[] __b,
		int __o, int __l, boolean __wait)
		throws ArrayIndexOutOfBoundsException, ArrayStoreException,
			InterruptedException, NoSuchElementException, NullPointerException,
			SystemMailBoxException;
	
	/**
	 * Returns the remote ID of the connected mailbox.
	 *
	 * @return The byte array representing the remote ID, the format is in the
	 * form of {@code <vendor>;<name>;<version>}.
	 * @throws SystemMailBoxException If the ID could not be obtained.
	 * @since 2016/10/13
	 */
	public abstract String remoteId()
		throws SystemMailBoxException;
	
	/**
	 * Sends a single datagram to the destination mailbox.
	 *
	 * @param __fd The mailbox descriptor to send data over.
	 * @param __chan The channel to send over.
	 * @param __b The data to send.
	 * @param __o The offset to the start of the data.
	 * @param __l The number of bytes to send.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the length of the array.
	 * @throws IllegalArgumentException If the descriptor is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws SystemMailBoxException If the remote end was closed.
	 * @since 2016/10/13
	 */
	public abstract void send(int __chan, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException, SystemMailBoxException;
}

