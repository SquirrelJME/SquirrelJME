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

/**
 * This class handles reading of datagrams from the remote source and fills
 * in responses as required.
 *
 * @since 2018/01/17
 */
final class __PacketStreamReader__
	implements Runnable
{
	/** The input source for datagrams. */
	protected final DatagramIn in;
	
	/** Where to send responses to as needed. */
	protected final DatagramOut out;
	
	/** The handler for input events. */
	protected final PacketStreamHandler eventhandler;
	
	/** The counter for packet streams. */
	protected final PacketStreamCounter counter;
	
	/** The local name. */
	protected final String localname;
	
	/** The remote name. */
	private final String[] _remotename;
	
	/** The response handler where responses go. */
	private final __ResponseHandler__ _rhandler;
	
	/**
	 * Initializes the packet stream reader.
	 *
	 * @param __in The input source for read datagrams.
	 * @param __out Where to write responses to as needed.
	 * @param __rhandler The bridge for handling responses.
	 * @param __handler The handler for input events.
	 * @param __counter The counter for packet streams.
	 * @param __ln The local name.
	 * @param __rn The remote name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	__PacketStreamReader__(DatagramIn __in, DatagramOut __out,
		__ResponseHandler__ __rh, PacketStreamHandler __handler,
		PacketStreamCounter __counter, String __ln, String[] __rn)
		throws NullPointerException
	{
		if (__in == null || __out == null || __rh == null ||
			__handler == null || __counter == null || __ln == null ||
			__rn == null)
			throw new NullPointerException("");
		
		this.in = __in;
		this.out = __out;
		this.eventhandler = __handler;
		this.counter = __counter;
		this.localname = __ln;
		this._rhandler = __rh;
		this._remotename = __rn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public void run()
	{
		DatagramOut out = this.out;
		PacketStreamCounter counter = this.counter;
		__ResponseHandler__ rhandler = this._rhandler;
		PacketStreamHandler eventhandler = this.eventhandler;
		
		// Close on the input, because if an exception occurs and the loop
		// terminates the other side will be closed
		int[] pkeya = new int[1];
		try (DatagramIn in = this.in)
		{
			// Receive name from the remote end
			int[] key = new int[1];
			String remotename;
			try (Packet p = in.read(key))
			{
				// {@squirreljme.error AT0l Could not read the remote packet
				// stream name.}
				if (key[0] != PacketStream._SPECIAL_NAME_KEY)
					throw new DatagramIOException("AT0l");
			
				remotename = p.readString(0);
				
				// Store remote name so the local side knows it
				String[] srname = this._remotename;
				synchronized (srname)
				{
					srname[0] = remotename;
				}
			}
			
			// Read loop now that the initial stream header has been read
			for (;;)
			{
				// Read in datagram
				Packet p = in.read(pkeya);
				int pkey = pkeya[0];
				
				// Count it for statistics
				counter.__countRead(p.length());
				
				// This is a response to a packet, so store it
				int ptype = p.type();
				if (ptype == Packet._RESPONSE_OKAY ||
					ptype == Packet._RESPONSE_FAIL)
				{
					rhandler.__give(pkey, p);
					continue;
				}
				
				// Remote side threw an exception, so just print it to the
				// console to let someone know
				else if (ptype == Packet._RESPONSE_EXCEPTION)
				{
					((Throwable)__ThrowableUtil__.__decode(
						p.createReader(), this.localname, remotename)).
						printStackTrace(System.err);
					continue;
				}
				
				// Since this is only a single thread handling events, another
				// thread needs to be created to handle the request otherwise
				// there could only be a single thing using services at a time
				// and they additionally could never layer on top of each
				// other. This makes it completely asynchronous for the most
				// part and as soon as more packets are ready they will be
				// processed.
				Thread runner = new Thread(new __Runner__(pkey, p, out,
					eventhandler), "Packet-Stream-Runner");
				runner.start();
			}
		}
	}
	
	/**
	 * This actually handles requests which are sent from the remote side
	 *
	 * @since 2018/01/17
	 */
	private static final class __Runner__
		implements Runnable
	{
		/** The key. */
		protected final int key;
		
		/** The packet. */
		protected final Packet packet;
		
		/** The output where responses go. */
		protected final DatagramOut out;
		
		/** The event handler. */
		protected final PacketStreamHandler eventhandler;
		
		/**
		 * Initializes the runner for running events and returning their
		 * result.
		 *
		 * @param __key The key.
		 * @param __p The input packet data.
		 * @param __out The stream to write responses to.
		 * @param __eh The handler for events.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/17
		 */
		private __Runner__(int __key, Packet __p, DatagramOut __out,
			PacketStreamHandler __eh)
			throws NullPointerException
		{
			if (__p == null || __out == null || __eh == null)
				throw new NullPointerException("NARG");
			
			this.key = __key;
			this.packet = __p;
			this.out = __out;
			this.eventhandler = __eh;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/17
		 */
		@Override
		public final void run()
		{
			Packet packet = this.packet;
			
			// Does the remote end want a response to this?
			boolean wantsresponse = (packet.type() > 0);
			
			// Allow all exceptions to be caught so that
			// responses can be generated for them
			Packet rv = null;
			try
			{
				// Handle response
				rv = this.eventhandler.handle(packet);
				
				// A packet was returned when none was expected
				if (!wantsresponse)
					rv = null;
				
				// A response was expected, but none was given
				// Force one to exist
				else if (rv == null)
					rv = PacketFarm.createPacket(0, 0);
			}
			
			// Send failure response
			catch (Throwable t)
			{
				rv = PacketFarm.createPacket((wantsresponse ?
					Packet._RESPONSE_FAIL :
					Packet._RESPONSE_EXCEPTION));
				
				// Write in details about the exception as they
				// are known
				__ThrowableUtil__.__encode(t, rv.createWriter());
			}
			
			// Close the source packet, it is not needed anymore
			packet.close();
			
			// Send response packet to the remote end
			if (rv != null)
			{
				this.out.write(this.key, rv);
				
				// Response sent, so no longer is it needed
				rv.close();
			}
		}
	}
}

