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

import java.util.NoSuchElementException;

/**
 * This class provides access to the mailbox system.
 *
 * @since 2017/11/10
 */
public abstract class MailboxFunctions
{
	/**
	 * Accepts an incoming mailbox request to create a mailbox connection.
	 *
	 * @param __id The listening mailbox to accept.
	 * @return The mailbox descriptor for the server end of the mailbox.
	 * @throws IllegalArgumentException If the mailbox is not valid.
	 * @throws InterruptedException If the thread was interrupted accepting
	 * a connection.
	 * @throws MailboxException If the mailbox is closed.
	 * @since 2016/10/13
	 */
	public final int accept(int __ld)
		throws IllegalArgumentException, InterruptedException,
			MailboxException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Closes the given mailbox client.
	 *
	 * If the mailbox is already closed or is going to be closed this has
	 * no effect.
	 *
	 * @param __fd The descriptor to close.
	 * @throws MailboxException If the mailbox could not be closed.
	 * @since 2016/10/13
	 */
	public final void close(int __fd)
		throws MailboxException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Connects to a mailbox that is listening. 
	 *
	 * @param __mb The UTF-8 bytes for the midlet to connect to, if
	 * {@code null} then any midlet is connected to.
	 * @param __mo The offset in the midlet name, ignored if {@code __mb} is
	 * {@code null}.
	 * @param __ml The length of the midlet UTF-8 bytes, ignored if
	 * {@code __mb} is {@code null}.
	 * @param __sb The UTF-8 bytes of the server to connect to.
	 * @param __so The offset in the server name.
	 * @param __sl The number of UTF-8 bytes used in the server name.
	 * @param __v The encoded version of the server to connect to, the server
	 * must have a version that is at least this value.
	 * @param __am If {@code true} then authorized mode to use.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IllegalArgumentException If the remote midlet is malformed or
	 * the server name and/or version are malformed.
	 * @throws NullPointerException If no server was specified or the offset
	 * and length are not of the specified values and the midlet is not
	 * specified.
	 * @throws MailboxException If the server does not exist or if the
	 * remote destination is closed.
	 * @since 2016/10/13
	 */
	public final int connect(byte[] __mb, int __mo, int __ml,
		byte[] __sb, int __so, int __sl, int __v, boolean __am)
		throws ArrayIndexOutOfBoundsException,
			IllegalArgumentException, NullPointerException,
			MailboxException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets up a listening mailbox that waits for incoming mailbox connections.
	 *
	 * @param __b The name of the server, UTF-8 encoded.
	 * @param __o The offset.
	 * @param __l The length of the name.
	 * @param __v The integer encoded version number.
	 * @param __am If {@code true} then authorization mode is used.
	 * @return The descriptor of the mailbox destination.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IllegalArgumentException If the version number is not correct.
	 * @throws NullPointerException On null arguments.
	 * @throws MailboxException If the mailbox was closed.
	 * @since 2016/10/13
	 */
	public final int listen(byte[] __b, int __o, int __l, int __v,
		boolean __am)
		throws ArrayIndexOutOfBoundsException,
			IllegalArgumentException, NullPointerException,
			MailboxException
	{
		throw new todo.TODO();
	}
	
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
	 * @throws IllegalArgumentException If the mailbox descriptor is not
	 * valid.
	 * @throws InterruptedException If an interrupt occured waiting for data.
	 * @throws NoSuchElementException If not waiting and there are no datagrams
	 * available.
	 * @throws NullPointerException On null arguments.
	 * @throws MailboxException If receiving could not happen.
	 * @since 2016/10/13
	 */
	public final int receive(int __fd, int[] __chan, byte[] __b,
		int __o, int __l, boolean __wait)
		throws ArrayIndexOutOfBoundsException, ArrayStoreException,
			IllegalArgumentException, InterruptedException,
			NoSuchElementException, NullPointerException, MailboxException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the remote ID of the connected mailbox.
	 *
	 * @param __fd The remote file descriptor.
	 * @return The byte array representing the remote ID, the format is in the
	 * form of {@code <vendor>;<name>;<version>}.
	 * @throws IllegalArgumentException If the mailbox descriptor is not valid.
	 * @throws MailboxException If the ID could not be obtained.
	 * @since 2016/10/13
	 */
	public final byte[] remoteId(int __fd)
		throws IllegalArgumentException, MailboxException
	{
		throw new todo.TODO();
	}
	
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
	 * @throws MailboxException If the remote end was closed.
	 * @since 2016/10/13
	 */
	public final void send(int __fd, int __chan, byte[] __b, int __o,
		int __l)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException, MailboxException
	{
		throw new todo.TODO();
	}
}

