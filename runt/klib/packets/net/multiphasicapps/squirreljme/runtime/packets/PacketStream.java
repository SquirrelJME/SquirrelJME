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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCall;

/**
 * This represents a packet stream which is used to read and write events from
 * the local end to the remote end.
 *
 * This class spawns a new thread to manage asynchronous events.
 *
 * @since 2018/01/01
 */
public final class PacketStream
{
	/**
	 * This lock is used to prevent threads from writing intertwined data
	 * when they send packets, which will completely cause communication to
	 * fail.
	 */
	protected final Object lock =
		new Object();
	
	/** The output stream . */
	protected final DataOutputStream out;
	
	/** The farm for packets. */
	protected final PacketFarm farm =
		new PacketFarm();
	
	/**
	 * Initializes the packet stream.
	 *
	 * @param __in The stream to read input events from.
	 * @param __out The stream to write events to.
	 * @param __handler The handler which is called on all input events.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public PacketStream(InputStream __in, OutputStream __out,
		PacketStreamHandler __handler)
		throws NullPointerException
	{
		if (__in == null || __out == null || __handler == null)
			throw new NullPointerException("NARG");
		
		// This class on the outside just sends responses to the remote end
		// while another thread handles input events
		this.out = new DataOutputStream(__out);
		
		// Create thread which reads the input side and allows for handling
		Thread thread = new Thread(new __Reader__(new DataInputStream(__in),
			__handler), this.toString());
		
		// Make sure it is a daemon thread so that it terminates when every
		// other thread is terminated
		SystemCall.setDaemonThread(thread);
		thread.start();
	}
	
	/**
	 * Returns the packet farm.
	 *
	 * @return The packet farm.
	 * @since 2018/01/01
	 */
	public final PacketFarm farm()
	{
		return this.farm;
	}
	
	/**
	 * Sends the specified packet to the remote end.
	 *
	 * @param __p The packet to send to the remote end.
	 * @return The resulting packet, if the type of one that does not generate
	 * a response then this will be {@code null}. The return value of a
	 * response should be used with try-with-resources.
	 * @throws NullPointerException On null arguments.
	 * @throws RemoteThrowable If the remote handler threw an exception.
	 * @since 2018/01/01
	 */
	public final Packet send(Packet __p)
		throws NullPointerException, RemoteThrowable
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This class reads incoming packets and handles events.
	 *
	 * @since 2018/01/01
	 */
	private final class __Reader__
		implements Runnable
	{
		/** The stream to read packets from. */
		protected final DataInputStream in;
		
		/** The handler for input events. */
		protected final PacketStreamHandler handler;
		
		/**
		 * Initializes the reader.
		 *
		 * @param __in The stream to read packets from.
		 * @param __handler The handler for incoming packets.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/01
		 */
		private __Reader__(DataInputStream __in, PacketStreamHandler __handler)
			throws NullPointerException
		{
			if (__in == null || __handler == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
			this.handler = __handler;
		}
		
		/**
		 * {@inheritDoc}
		 * @sine 2018/01/01
		 */
		@Override
		public void run()
		{
			throw new todo.TODO();
		}
	}
}

