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

import cc.squirreljme.runtime.cldc.system.SystemCall;
import java.io.Closeable;
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
	implements Closeable
{
	/** Special key for name transmission. */
	static final int _SPECIAL_NAME_KEY =
		-1;
	
	/** Lock used to generate unique keys. */
	protected final Object lock =
		new Object();
	
	/** Used to measure bytes transmitted. */
	protected final PacketStreamCounter counter =
		new PacketStreamCounter();
	
	/** The output datagram sink. */
	protected final DatagramOut out;
	
	/** The name of the local packet stream. */
	protected final String localname;
	
	/** The name fo the remote packet stream. */
	private final String[] _remotename;
	
	/** The response handler. */
	private final __ResponseHandler__ _rhandler =
		new __ResponseHandler__();
	
	/** The next key to use for a packet. */
	private volatile int _nextkey =
		1;
	
	/**
	 * Initializes the packet stream.
	 *
	 * @param __in The input source for datagrams.
	 * @param __out The output sink for datagrams.
	 * @param __handler The handler which is called on all input events.
	 * @param __name The local name of this packet stream.
	 * @throws DatagramIOException If the connection could not be established.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public PacketStream(DatagramIn __in, DatagramOut __out,
		PacketStreamHandler __handler, String __name)
		throws DatagramIOException, NullPointerException
	{
		if (__in == null || __out == null || __handler == null ||
			__name == null)
			throw new NullPointerException("NARG");
		
		// This class on the outside just sends responses to the remote end
		// while another thread handles input events
		this.out = __out;
		
		// Transmit name to the remote end
		this.localname = __name;
		try (Packet p = PacketFarm.createPacket(0))
		{
			p.writeString(0, __name);
			__out.write(PacketStream._SPECIAL_NAME_KEY, p);
		}
		
		// Let the reader handle it so the constructor does not lock
		String[] remotename = new String[1];
		this._remotename = remotename;
		
		// Create thread which reads the input side and allows for handling
		Thread thread = new Thread(new __PacketStreamReader__(__in, __out,
			this._rhandler, __handler, this.counter, __name, remotename),
			String.format("Packet-Stream-%s", __handler.getClass().getName()));
		
		// Make sure it is a daemon thread so that it terminates when every
		// other thread is terminated
		try
		{
			SystemCall.MNEMONIC.setDaemonThread(thread);
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
		throws DatagramIOException
	{
		// Closing the output end will cause the remote side to no longer
		// read datagrams, in which case
		this.out.close();
	}
	
	/**
	 * Returns the counter for packet streams.
	 *
	 * @return The packet stream counter.
	 * @since 2019/01/12
	 */
	public final PacketStreamCounter counter()
	{
		return this.counter;
	}
	
	/**
	 * Returns the local name of the stream.
	 *
	 * @return The stream local name.
	 * @since 2018/01/18
	 */
	public final String localName()
	{
		return this.localname;
	}
	
	/**
	 * Returns the remote name of the stream.
	 *
	 * @return The stream remote name.
	 * @since 2018/01/18
	 */
	public final String remoteName()
	{
		String[] remotename = this._remotename;
		synchronized (remotename)
		{
			return Objects.toString(remotename[0], "Unknown-Remote");
		}
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
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this.send(__p, false);
	}
	
	/**
	 * Sends the specified packet to the remote end and optionally closes it
	 * once the packet has been written to the target stream.
	 *
	 * @param __p The packet to send to the remote end.
	 * @return The resulting packet, if the type of one that does not generate
	 * a response then this will be {@code null}. The return value of a
	 * response should be used with try-with-resources.
	 * @throws NullPointerException On null arguments.
	 * @throws RemoteThrowable If the remote handler threw an exception.
	 * @since 2018/01/13
	 */
	public final Packet send(Packet __p, boolean __close)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this.__send(0, false, __p, __close);
	}
	
	/**
	 * Sends the specified packet to the remote end.
	 *
	 * @param __key The key to use, if {@code 0} then one is generated.
	 * @param __forceresponse Force a response type to be used?
	 * @param __p The packet to send to the remote end.
	 * @param __close Should the packet be closed right after it is written to
	 * the target stream? This can be used when it is not really needed
	 * anymore.
	 * @return The resulting packet, if the type of one that does not generate
	 * a response then this will be {@code null}. The return value of a
	 * response should be used with try-with-resources.
	 * @throws NullPointerException On null arguments.
	 * @throws RemoteThrowable If the remote handler threw an exception.
	 * @since 2018/01/01
	 */
	final Packet __send(int __key, boolean __forceresponse, Packet __p,
		boolean __close)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// A lock needs to be performed on the key because if multiple threads
		// are trying to send packets at the same time then they might share
		// key numbers which will completely destroy responses
		if (__key == 0)
			synchronized (this.lock)
			{
				int nextkey = this._nextkey;
				__key = nextkey;
			
				// Zero is used as a special value to indicate that a key
				// should be generated, so do not allow it to overflow because
				// then something will break for the zero key when the remote
				// side tries to send a response for it
				nextkey = nextkey + 1;
				if (nextkey == -1)
					nextkey = 1;
				this._nextkey = nextkey;
			}
		
		// Need to determine the packet type so it may potentially be forced
		// as needed
		int ptype = __p.type();
		if (__forceresponse && (ptype != Packet._RESPONSE_OKAY &&
			ptype != Packet._RESPONSE_FAIL))
		{
			__p = __p.duplicateAsType(Packet._RESPONSE_OKAY);
			ptype = Packet._RESPONSE_OKAY;
		}
		
		// Send the datagram
		this.out.write(__key, __p);
		
		// Count it for statistics
		this.counter.__countWrite(__p.length());
		
		// Close after send?
		if (__close)
		{
			__p.close();
			__p = null;
		}
		
		// Wait for a response if one is desired
		if (ptype > 0)
		{
			Packet rv = this._rhandler.__await(__key);
			
			// Remote end threw an exception, so decode it and throw it
			if (rv.type() == Packet._RESPONSE_FAIL)
			{
				RemoteThrowable t = __ThrowableUtil__.__decode(
					rv.createReader(), this.localname, this.remoteName());
				
				// Make sure the exception does not leak resources
				rv.close();
				
				// Throw the decoded exception or error
				if (t instanceof RuntimeException)
					throw (RuntimeException)t;
				else
					throw (Error)t;
			}
			
			// Use given response
			return rv;
		}
		
		return null;
	}
}

