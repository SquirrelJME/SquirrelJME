// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.clsyscall;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
	/** Lock to prevent multiple threads from sending mismatched packets. */
	protected final Object lock =
		new Object();
	
	/** The output stream. */
	protected final DataOutputStream out;
	
	/** Thread which actually handles communication. */
	protected final Thread thread;
	
	/** The exception which was thrown on error. */
	private volatile IOException _fail;
	
	/** The next key to use for a response. */
	private volatile int _key =
		1;
	
	/**
	 * Initializes the packet stream.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @param __h Handler for packet stream events.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public PacketStream(InputStream __in, OutputStream __out,
		PacketStreamHandler __h)
		throws NullPointerException
	{
		if (__in == null || __out == null || __h == null)
			throw new NullPointerException("NARG");
		
		// Input events are handled by another thread so only the output
		// is visible to this class
		this.out = new DataOutputStream(__out);
		
		// Setup thread which handles actual communication, only the input
		// side is read and events are pushed accordingly
		Thread thread = new Thread(
			new __EventReader__(new DataInputStream(__in), __h, this),
			"SquirrelJME-PacketStream-" + System.identityHashCode(this));
		this.thread = thread;
		
		// Start the process thread
		thread.start();
		
		// Send the hello response to the other side to indicate that the
		// link has been established
		this.send(PacketTypes.HELLO, new byte[0]);
	}
	
	/**
	 * Sends the specified packet to the remote server.
	 *
	 * @param __t The type of packet to send, if the value is negative then
	 * no response will be returned.
	 * @param __b The bytes to send.
	 * @return The response from the server, {@code null} is returned if there
	 * is no response.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public final byte[] send(int __t, byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return this.send(__t, __b, 0, __b.length);
	}
	
	/**
	 * Sends the specified packet to the remote server.
	 *
	 * @param __t The type of packet to send, if the value is negative then
	 * no response will be returned.
	 * @param __b The bytes to send.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The response from the server, {@code null} is returned if there
	 * is no response.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public final byte[] send(int __t, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		return this.__send(__t, 0, __b, __o, __l);
	}
	
	/**
	 * Sends the specified packet to the remote server.
	 *
	 * @param __t The type of packet to send, if the value is negative then
	 * no response will be returned.
	 * @param __key The key to send the response as, if this is {@code 0} then
	 * the next key will be determined automatically.
	 * @param __b The bytes to send.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The response from the server, {@code null} is returned if there
	 * is no response.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	final byte[] __send(int __t, int __key, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
			
		DataOutputStream out = this.out;
		
		// Lock since multiple threads cannot send packets otherwise they
		// will end up being mashed together
		synchronized (this.lock)
		{
			// {@squirreljme.error AR02 Cannot send more packets because
			// a previous operation failed to write to the remote end.}
			IOException fail = this._fail;
			if (fail != null)
				throw new RuntimeException("AR02", fail);
			
			// Sending could fail, perhaps the remote end disconnected for
			// some reason?
			try
			{
				// Send the packet using a unique key because this will wait
				// for a remote response using the given key
				// But response packets will use a pre-existing key
				if (__key == 0)
					__key = this._key++;
			
				// Send key to server
				out.writeInt(__key);
				
				// Send the type to the server
				out.writeShort(__t);
				
				// Write array data
				out.writeInt(__l);
				out.write(__b, __o, __l);
				
				// Flush so the other side sees it
				out.flush();
				
				// Any types which are negative never get a responmse
				if (__t < 0)
					return null;
				
				throw new todo.TODO();
			}
			
			// Failed read/write, record so that the stream becomes always
			// invalid
			catch (IOException e)
			{
				this._fail = e;
				
				// {@squirreljme.error AR03 The connection with the client
				// has been terminated and data cannot be sent.}
				throw new RuntimeException("AR03", e);
			}
		}
	}
	
	/**
	 * This reads events from the input stream constantly.
	 *
	 * @since 2018/01/01
	 */
	private static final class __EventReader__
		implements Runnable
	{
		/** The input bytes to read from. */
		protected final DataInputStream in;
		
		/** The handler for requests from the remote end. */
		protected final PacketStreamHandler handler;
		
		/** The packet stream. */
		protected final PacketStream stream;
		
		/**
		 * Initializes the event runner.
		 *
		 * @param __in The input stream to read events from.
		 * @param __h The stream handler.
		 * @param __ps The packet stream to send responses to.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/01
		 */
		private __EventReader__(DataInputStream __in, PacketStreamHandler __h,
			PacketStream __ps)
			throws NullPointerException
		{
			if (__in == null || __h == null || __ps == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
			this.handler = __h;
			this.stream = __ps;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/01
		 */
		@Override
		public void run()
		{
			DataInputStream in = this.in;
			PacketStreamHandler handler = this.handler;
			PacketStream stream = this.stream;
			
			try
			{
				for (;;)
				{
					// Read packet key which is used to get a response back
					// in a linear stream
					int pkey;
					try
					{
						pkey = in.readInt();
					}
					
					// Process closed the pipe so no more events are possible
					catch (EOFException e)
					{
						throw new todo.TODO();
					}
					
					System.err.printf("DEBUG -- Read key %d%n", pkey);
					
					// Read packet details
					int ptype = in.readShort(),
						plen = in.readInt();
					byte[] pdata = new byte[plen];
					in.readFully(pdata);
					
					System.err.printf("DEBUG -- Read (key=%d) %d -> sz=%d%n",
						pkey, ptype, plen);
					
					// Do not send responses to the handler because otherwise
					// there will be an infinite loop
					if (ptype == PacketTypes.RESPONSE_OKAY)
					{
						if (true)
							throw new todo.TODO();
						continue;
					}
					
					// Also handle failed responses
					else if (ptype == PacketTypes.RESPONSE_FAIL)
					{
						if (true)
							throw new todo.TODO();
						continue;
					}
					
					// Send to handler
					try
					{
						byte[] rv = handler.handle(ptype, pdata, 0, plen);
						
						// Send okay response to the given key
						if (ptype >= 0)
						{
							// Always make sure an array exists
							if (rv == null)
								rv = new byte[0];
							
							stream.__send(PacketTypes.RESPONSE_OKAY, pkey,
								rv, 0, rv.length);
						}
					}
					
					// It is possible that 
					catch (Throwable t)
					{
						// Send fail response to the given key
						if (ptype >= 0)
						{
							// Write the exception message to the client
							// {@squirreljme.error AR04 Thrown exception from
							// handler has no message.}
							byte[] rv = Objects.toString(t.getMessage(),
								"AR04").getBytes("utf-8");
						
							stream.__send(PacketTypes.RESPONSE_FAIL, pkey,
								rv, 0, rv.length);
						}
					}
				}
			}
			
			// {@squirreljme.error AR01 The packet stream encountered an
			// IOException.}
			catch (IOException e)
			{
				throw new RuntimeException("AR01", e);
			}
		}
	}
}

