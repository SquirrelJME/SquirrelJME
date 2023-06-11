// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * This handles the input and output communication of JDWPa.
 *
 * @since 2021/03/09
 */
public final class CommLink
	implements Closeable
{
	/** Handshake sequence, sent by both sides. */
	private static final byte[] _HANDSHAKE_SEQUENCE =
		{'J', 'D', 'W', 'P', '-', 'H', 'a', 'n', 'd', 's', 'h', 'a', 'k', 'e'};
	
	/** Initial data buffer length. */
	private static final int _INIT_DATA_LEN =
		1024;
	
	/** The size of the packet header. */
	static final int _HEADER_SIZE =
		11;
	
	/** The input communication stream. */
	protected final DataInputStream in;
	
	/** The output communication stream. */
	protected final DataOutputStream out;
	
	/** The queue of packets which are freed, they will go back here. */
	private final Deque<JDWPPacket> _freePackets =
		new LinkedList<>();
	
	/** The header bytes. */
	private final byte[] _header =
		new byte[CommLink._HEADER_SIZE];
	
	/** The monitor used for the output object. */
	private final Object _outMonitor =
		new Object();
	
	/** Read position for the header. */
	private volatile int _headerAt;
	
	/** The data. */
	private volatile byte[] _data =
		new byte[CommLink._INIT_DATA_LEN];
	
	/** Read position for read data. */
	private volatile int _dataAt;
	
	/** Length of read data. */
	private volatile int _dataLen;
	
	/** Did we do our handshake? */
	private volatile boolean _didHandshake;
	
	/** Are we in shutdown? */
	volatile boolean _shutdown;
	
	/**
	 * Initializes the communication link.
	 *
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public CommLink(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.out = new DataOutputStream(__out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/08
	 */
	@Override
	public void close()
		throws JDWPException
	{
		IOException fail = null;
		
		// Enter shut-down mode
		synchronized (this)
		{
			this._shutdown = true;
		}
		
		// Close the input
		try
		{
			this.in.close();
		}
		catch (IOException e)
		{
			fail = e;
		}
		
		// And the output
		try
		{
			this.out.close();
		}
		catch (IOException e)
		{
			if (fail == null)
				fail = e;
			else
				fail.addSuppressed(e);
		}
		
		// {@squirreljme.error AG09 Could not close communication link.}
		if (fail != null)
			throw new JDWPException("AG09", fail);
	}
	
	/**
	 * Polls for the next event, if there is any.
	 * 
	 * @throws JDWPException If there is an issue with the connection.
	 * @return A packet or {@code null} if there are none.
	 * @since 2021/03/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public JDWPPacket poll()
		throws JDWPException
	{
		// If the handshake did not happen, do it now
		synchronized (this)
		{
			if (!this._didHandshake)
				this.__handshake();
		}
		
		// Used to read in the header as needed
		byte[] header = this._header;
		int headerAt = this._headerAt;
		
		// Data and whatever length
		byte[] data = this._data;
		int dataLen = this._dataLen;
		int dataAt = this._dataAt;
		
		// Constant reading in loop
		for (InputStream in = this.in;;)
			try
			{
				// Shutting down?
				if (this._shutdown)
					return null;
				
				// Still reading in the header?
				int headerLeft = CommLink._HEADER_SIZE - headerAt;
				if (headerLeft > 0)
				{
					int rc = in.read(header, headerAt, headerLeft);
					
					// EOF?
					if (rc < 0)
					{
						// {@squirreljme.error AG07 Short header read.}
						if (headerAt > 0)
							throw new EOFException("AG07");
							
						this._shutdown = true;
						return null;
					}
					
					headerLeft -= rc;
					headerAt += rc;
				}
				
				// Still need the header to be read?
				if (headerLeft > 0)
					continue;
				
				// Do not know the data length yet?
				if (dataLen < 0)
				{
					// Figure out the data length, note that it includes our
					// own header!!
					dataLen = Math.max(((header[0] & 0xFF) << 24) |
						((header[1] & 0xFF) << 16) |
						((header[2] & 0xFF) << 8) |
						(header[3] & 0xFF) - 11, 0);
						
					// If our buffer is too small, grow it just enough to fit
					if (dataLen > data.length)
						data = new byte[dataLen];
				}
				
				// Read in any associated data
				int dataLeft = dataLen - dataAt;
				if (dataLen >= 0 && dataLeft > 0)
				{
					int rc = in.read(data, dataAt, dataLeft);
					
					// EOF?
					if (rc < 0)
					{
						// {@squirreljme.error AG08 Short header read.}
						if (dataAt > 0)
							throw new EOFException("AG08");
							
						this._shutdown = true;
						return null;
					}
					
					dataLeft -= rc;
					dataAt += rc;
				}
				
				// Still need more data to be read
				if (dataLen >= 0 && dataLeft > 0)
					continue;
				
				// Setup a fresh packet
				JDWPPacket packet = this.__getPacket(false);
				packet.__load(header, data, dataLen);
				
				// Reset state for the next run
				headerAt = 0;
				dataAt = 0;
				dataLen = -1;
				
				// This packet is ready so use it now
				return packet;
			}
			
			// If we get interrupted, we just either shutdown or continue
			catch (InterruptedIOException ignored)
			{
				return null;
			}
	
			// {@squirreljme.error AG06 Read error.}
			catch (IOException e)
			{
				throw new JDWPException("AG06", e);
			}
			
			// Store resultant state
			finally
			{
				this._headerAt = headerAt;
				this._data = data;
				this._dataLen = dataLen;
				this._dataAt = dataAt;
			}
	}
	
	/**
	 * Sends the packet to the remote end.
	 * 
	 * @param __packet The packet to send.
	 * @throws JDWPException If the packet could not be sent.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/12
	 */
	protected void send(JDWPPacket __packet)
		throws JDWPException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// Debug
		if (JDWPController._DEBUG)
			Debugging.debugNote("JDWP: -> %s", __packet);
		
		// Write to the destination
		try
		{
			// Different threads could be sending different replies, so if
			// these get mashed together then that would be a very bad thing
			// But we only need to protect the output
			synchronized (this._outMonitor)
			{
				// Write then make sure it is instantly available
				__packet.writeTo(this.out);
				this.out.flush();
			}
		}
		
		// {@squirreljme.error AG01 Could not send the packet. (The packet)}
		catch (IOException e)
		{
			throw new JDWPException("AG01 " + __packet, e);
		}
	}
	
	/**
	 * Performs the handshake for JDWP.
	 * 
	 * @throws JDWPException If the handshake could not happen.
	 * @since 2021/03/08
	 */
	private void __handshake()
		throws JDWPException
	{
		try
		{
			// Debug
			if (JDWPController._DEBUG)
				Debugging.debugNote("JDWP: Handshake.");
			
			// The debugger sends the handshake sequence first
			int seqLen = CommLink._HANDSHAKE_SEQUENCE.length;
			byte[] debuggerShake = new byte[seqLen];
			
			// Read in the handshake
			for (int i = 0; i < seqLen; i++)
			{
				int read = this.in.read();
				
				// {@squirreljme.error AG02 EOF reading handshake.}
				if (read < 0)
					throw new JDWPException("AG02");
				
				debuggerShake[i] = (byte)read;
			}
			
			// {@squirreljme.error AG03 Debugger sent an invalid handshake.}
			if (!Arrays.equals(debuggerShake, CommLink._HANDSHAKE_SEQUENCE))
				throw new JDWPException("AG03");
			
			// We then reply with our own handshake
			this.out.write(CommLink._HANDSHAKE_SEQUENCE);
			this.out.flush();
			
			// We did the handshake
			this._didHandshake = true;
			
			// Debug
			if (JDWPController._DEBUG)
				Debugging.debugNote("JDWP: Hands shaken at a distance.");
		}
		catch (IOException e)
		{
			// {@squirreljme.error AG04 Failed to handshake.}
			throw new JDWPException("AG04", e);
		}
	}
	
	/**
	 * Returns a fresh packet.
	 * 
	 * @param __open Should this be opened?
	 * @return A packet that is fresh, this may be recycled from a previous
	 * packet or taken from another.
	 * @since 2021/03/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	JDWPPacket __getPacket(boolean __open)
	{
		Deque<JDWPPacket> freePackets = this._freePackets;
		synchronized (freePackets)
		{
			// If there are no free packets then make a new one
			JDWPPacket rv;
			if (freePackets.isEmpty())
				rv = new JDWPPacket(freePackets);
			
			// Grab the next free one
			else
				rv = freePackets.remove();
			
			// Clear it for the next run
			rv.resetAndOpen(__open);
			
			return rv;
		}
	}
}
