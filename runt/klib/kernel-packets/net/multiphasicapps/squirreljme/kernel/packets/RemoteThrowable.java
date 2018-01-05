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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

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
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/01/05
	 */
	public RemoteThrowable()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/01/05
	 */
	public RemoteThrowable(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/01/05
	 */
	public RemoteThrowable(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/01/05
	 */
	public RemoteThrowable(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * This decodes a throwable which was stored in a packet.
	 *
	 * @param __r The reader to decode from.
	 * @return The decoded throwable from the remote end.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public static final RemoteThrowable decode(PacketReader __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public void printStackTrace()
	{
		this.printStackTrace(System.err);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public void printStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Encodes the given throwable to the packet.
	 *
	 * @param __t The throwable to encode.
	 * @param __w The target writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public static final void encode(Throwable __t, PacketWriter __w)
		throws NullPointerException
	{
		if (__t == null || __w == null)
			throw new NullPointerException("NARG");
		
		// Record suppressed exceptions
		Throwable[] suppressed = __t.getSuppressed();
		int n = Math.min(suppressed.length, Byte.MAX_VALUE);
		__w.writeByte(n);
		for (int i = 0; i < n; i++)
			RemoteThrowable.encode(suppressed[i], __w);
		
		// Is there a cause?
		Throwable cause = __t.getCause();
		boolean hascause;
		__w.writeBoolean((hascause = (cause != null)));
		if (hascause)
			RemoteThrowable.encode(cause, __w);
		
		// Class type
		__w.writeString(__t.getClass().getName());
		
		// Message, if there is any
		// {@squirreljme.error AT0g Throwable has no message.}
		__w.writeString(Objects.toString(__t.getLocalizedMessage(), "AT0g"));
		
		// Write the stack trace, this is the one which is printed in the
		// printStackTrace call
		String trace = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos, true, "utf-8"))
		{
			// Write and flush so it actually exists
			__t.printStackTrace(ps);
			ps.flush();
			
			// Encode string from written bytes
			trace = new String(baos.toByteArray(), "utf-8");
		}
		
		// Ignore these
		catch (IOException|OutOfMemoryError e)
		{
			trace = "";
		}
		
		// Write it out
		__w.writeString(trace);
	}
}

