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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
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
	implements Closeable
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
	
	/** Responses to packets. */
	private final Map<Integer, Packet> _responses =
		new HashMap<>();
	
	/** The next key to use for a packet. */
	private volatile int _nextkey =
		1;
	
	/** Has the stream finished? */
	private volatile boolean _done;
	
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
		try
		{
			SystemCall.setDaemonThread(thread);
		}
		catch (IllegalThreadStateException e)
		{
			// This will happen when the client tries to initialize the packet
			// interface because the caller might not be setup yet, ignore it
			// in this case
		}
		
		// Start it
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/04
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized (this.lock)
		{
			this.out.close();
		}
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
		
		return this.__send(0, false, __p);
	}
	
	/**
	 * Sends the specified packet to the remote end.
	 *
	 * @param __key The key to use, if {@code 0} then one is generated.
	 * @param __forceresponse Force a response type to be used?
	 * @param __p The packet to send to the remote end.
	 * @return The resulting packet, if the type of one that does not generate
	 * a response then this will be {@code null}. The return value of a
	 * response should be used with try-with-resources.
	 * @throws NullPointerException On null arguments.
	 * @throws RemoteThrowable If the remote handler threw an exception.
	 * @since 2018/01/01
	 */
	final Packet __send(int __key, boolean __forceresponse, Packet __p)
		throws NullPointerException, RemoteThrowable
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		DataOutputStream out = this.out;
		
		// Lock to prevent multiple threads from sending packets at the same
		// time
		synchronized (this.lock)
		{
			// {@squirreljme.error AT05 The stream has been disconnected.}
			if (this._done)
				throw new PacketStreamDisconnected("AT05");
			
			try
			{
				// Generate key to use for the send
				if (__key == 0)
					__key = this._nextkey++;
			
				// Write key, this is used to find responses when they are
				// generated
				out.writeInt(__key);
				
				// Write the type
				int type = (__forceresponse ? 0 : __p.type());
				out.writeShort(type);
				
				// Write the packet data
				__p.__writeToOutput(out);
				
				// Flush to force data through
				out.flush();
				
				System.err.printf("DEBUG -- Sent k=%d t=%d%n",
					__key, type);
				
				// No response expected, can stop here
				if (type <= 0)
					return null;
			}
			
			// {@squirreljme.error AT06 Could not write to the remote end.}
			catch (IOException e)
			{
				throw new PacketStreamDisconnected("AT06", e);
			}
		}
		
		// The key is used as the key in the response map
		Integer ki = __key;
		
		// If this point was reached then a response is expected from the
		// remote end, so wait for one to happen
		Map<Integer, Packet> responses = this._responses;
		Packet rv;
		synchronized (responses)
		{
			for (;;)
			{
				// If a packet was stored in the map return it
				rv = responses.remove(ki);
				if (rv != null)
					break;
				
				// Wait for a signal to be generated, but wait at most one
				// second for responses to appear before trying to grab
				// it again
				try
				{
					responses.wait(1_000L);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		
		// An exception was thrown on the remote end, to report that response
		if (rv.type() == Packet._RESPONSE_FAIL)
		{
			throw new todo.TODO();
		}
		
		return rv;
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
			PacketStreamHandler handler = this.handler;
			PacketFarm farm = PacketStream.this.farm;
			Map<Integer, Packet> responses = PacketStream.this._responses;
			
			try (DataInputStream in = this.in)
			{
				// Always try reading packets
				for (;;)
				{
					// Read packet key, but also detect EOF
					int pkey;
					try
					{
						pkey = in.readInt();
					}
					
					// Report that the remote end has closed
					catch (EOFException e)
					{
						handler.end();
						return;
					}
					
					// Read packet details
					int ptype = in.readShort(),
						plen = in.readInt();
					
					// Load in packet details
					Packet rv = null;
					
					// Read in packet data
					Packet p = farm.create(ptype, plen);
					p.__readFromInput(in, plen);
					
					System.err.printf("DEBUG -- Recv k=%d t=%d l=%d%n",
						pkey, ptype, plen);
					
					// Handle responses
					int type = p.type();
					if (type == Packet._RESPONSE_OKAY ||
						type == Packet._RESPONSE_FAIL)
					{
						System.err.printf("DEBUG -- Got response for %d%n",
							pkey);
						
						synchronized (responses)
						{
							responses.put(pkey, p);
							responses.notifyAll();
							continue;
						}
					}
					
					// Allow all exceptions to be caught so that
					// responses can be generated for them
					try
					{
						// Handle response
						rv = handler.handle(p);
						
						// A packet was returned when none was expected
						if (ptype <= 0)
							rv = null;
						
						// A response was expected, but none was given
						// Force one to exist
						else if (rv == null)
							rv = farm.create(0, 0);
					}
					
					// Send failure response
					catch (Throwable t)
					{
						// Only generate exception response if one was
						// expected
						if (ptype > 0)
						{
							throw new todo.TODO();
						}
					}
					
					// Close the source packet, it is not needed anymore
					p.close();
					
					// Send response packet to the remote end
					if (rv != null)
						PacketStream.this.__send(pkey, true, rv);
				}
			}
			
			// {@squirreljme.error AT03 IOException in the input packet stream
			// handler.}
			catch (IOException e)
			{
				throw new RuntimeException("AT03", e);
			}
		}
	}
}

