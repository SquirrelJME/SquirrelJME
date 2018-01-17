// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.packets;

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
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	__PacketStreamReader__(DatagramIn __in, DatagramOut __out,
		__ResponseHandler__ __rh, PacketStreamHandler __handler,
		PacketStreamCounter __counter)
		throws NullPointerException
	{
		if (__in == null || __out == null || __rh == null ||
			__handler == null || __counter == null)
			throw new NullPointerException("");
		
		this.in = __in;
		this.out = __out;
		this.eventhandler = __handler;
		this.counter = __counter;
		this._rhandler = __rh;
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
				
				// Is this information about a remote exception which was
				// thrown during a response?
				boolean isexception = (ptype == Packet._RESPONSE_EXCEPTION),
					wantsresponse = (ptype > 0);
				
				// Allow all exceptions to be caught so that
				// responses can be generated for them
				Packet rv = null;
				try
				{
					// Handle response
					rv = eventhandler.handle(p);
					
					// A packet was returned when none was expected
					if (ptype <= 0)
						rv = null;
					
					// A response was expected, but none was given
					// Force one to exist
					else if (rv == null)
						rv = PacketFarm.createPacket(0, 0);
				}
				
				// Send failure response
				catch (Throwable t)
				{
					// Do not generate exception responses if the
					// exception type was not handled because it will
					// end up being a gigantic recursive mess
					if (!isexception)
					{
						rv = PacketFarm.createPacket((wantsresponse ?
							Packet._RESPONSE_FAIL :
							Packet._RESPONSE_EXCEPTION));
						
						// Write in details about the exception as they
						// are known
						__ThrowableUtil__.__encode(t, rv.createWriter());
					}
				}
				
				// Close the source packet, it is not needed anymore
				p.close();
				
				// Send response packet to the remote end
				if (rv != null)
				{
					out.write(pkey, rv);
					
					// Response sent, so no longer is it needed
					rv.close();
				}
			}
		}
	}
}

