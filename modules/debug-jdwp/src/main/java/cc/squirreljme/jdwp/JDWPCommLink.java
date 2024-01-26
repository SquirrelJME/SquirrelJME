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
public final class JDWPCommLink
	implements Closeable
{
	/** Should debugging be enabled? */
	public static final boolean DEBUG =
		Boolean.getBoolean("cc.squirreljme.jdwp.debug");
	
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
	
	/** The direction of the channel. */
	protected final JDWPCommLinkDirection direction;
	
	/** The queue of packets which are freed, they will go back here. */
	private final Deque<JDWPPacket> _freePackets =
		new LinkedList<>();
	
	/** The header bytes. */
	private final byte[] _header =
		new byte[JDWPCommLink._HEADER_SIZE];
	
	/** The monitor used for the output object. */
	private final Object _outMonitor =
		new Object();
	
	/** Identifier sizes, needed for reading IDs. */
	private volatile JDWPIdSizes _idSizes;
	
	/** Read position for the header. */
	private volatile int _headerAt;
	
	/** The data. */
	private volatile byte[] _data =
		new byte[JDWPCommLink._INIT_DATA_LEN];
	
	/** Read position for read data. */
	private volatile int _dataAt;
	
	/** Length of read data. */
	private volatile int _dataLen =
		-1;
	
	/** Did we do our handshake? */
	private volatile boolean _didHandshake;
	
	/** Are we in shutdown? */
	volatile boolean _shutdown;
	
	/** Next packet ID number. */
	private volatile int _nextId;
	
	/**
	 * Initializes the communication link.
	 *
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPCommLink(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		this(__in, __out, JDWPCommLinkDirection.CLIENT_TO_DEBUGGER);
	}
	
	/**
	 * Initializes the communication link.
	 *
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @param __direction The direction of communication.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPCommLink(InputStream __in, OutputStream __out,
		JDWPCommLinkDirection __direction)
		throws NullPointerException
	{
		if (__in == null || __out == null || __direction == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.out = new DataOutputStream(__out);
		this.direction = __direction;
	}
	
	/**
	 * Are the ID sizes now known?
	 *
	 * @return If the sizes are known.
	 * @since 2024/01/23
	 */
	public boolean areSizesKnown()
	{
		synchronized (this)
		{
			return this._idSizes != null;
		}
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
		
		/* {@squirreljme.error AG09 Could not close communication link.} */
		if (fail != null)
			throw new JDWPException("AG09", fail);
	}
	
	/**
	 * Gets a blank packet.
	 *
	 * @return A blank packet.
	 * @since 2024/01/22
	 */
	public JDWPPacket getPacket()
	{
		return this.__getPacket(true);
	}
	
	/**
	 * Returns the ID sizes of the communication link.
	 *
	 * @return The link's ID sizes.
	 * @since 2024/01/23
	 */
	public JDWPIdSizes idSizes()
	{
		synchronized (this)
		{
			return this._idSizes;
		}
	}
	
	/**
	 * Is the debug link shutdown?
	 *
	 * @return If this is shutdown.
	 * @since 2024/01/19
	 */
	public boolean isShutdown()
	{
		synchronized (this)
		{
			return this._shutdown;
		}
	}
	
	/**
	 * The next ID number.
	 * 
	 * @return Returns a new ID number.
	 * @since 2021/03/13
	 */
	public final int nextId()
	{
		synchronized (this)
		{
			return ++this._nextId;
		}
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
				int headerLeft = JDWPCommLink._HEADER_SIZE - headerAt;
				if (headerLeft > 0)
				{
					int rc = in.read(header, headerAt, headerLeft);
					
					// EOF?
					if (rc < 0)
					{
						/* {@squirreljme.error AG07 Short header read.} */
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
					dataLen = Math.max((((header[0] & 0xFF) << 24) |
						((header[1] & 0xFF) << 16) |
						((header[2] & 0xFF) << 8) |
						(header[3] & 0xFF)) - 11, 0);
						
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
						/* {@squirreljme.error AG08 Short header read.} */
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
	
			/* {@squirreljme.error AG06 Read error.} */
			catch (IOException e)
			{
				// Shutdown the link
				this._shutdown = true;
				
				// Fail here
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
	 * Creates a reply packet.
	 *
	 * @param __id The raw packet ID that is being responded to.
	 * @param __error The error to use for the packet.
	 * @return The resultant reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public JDWPPacket reply(int __id, JDWPErrorType __error)
		throws NullPointerException
	{
		if (__error == null)
			throw new NullPointerException("NARG");
		
		JDWPPacket rv = this.__getPacket(true);
		
		rv._id = __id;
		rv._errorCode = __error;
		rv._rawErrorCode = __error.id;
		rv._flags = JDWPPacket.FLAG_REPLY;
		
		return rv;
	}
	
	/**
	 * Creates a reply packet.
	 *
	 * @param __packet The packet to reply to.
	 * @param __error The error to use for the packet.
	 * @return The resultant reply packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public JDWPPacket reply(JDWPPacket __packet, JDWPErrorType __error)
		throws NullPointerException
	{
		if (__packet == null || __error == null)
			throw new NullPointerException("NARG");
		
		return this.reply(__packet.id(), __error);
	}
	
	/**
	 * Creates a packet for a request.
	 *
	 * @param __commandSet The command set to use.
	 * @param __command The command to use.
	 * @return The newly created packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public JDWPPacket request(JDWPCommandSet __commandSet,
		JDWPCommand __command)
		throws NullPointerException
	{
		if (__commandSet == null || __command == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.request(__commandSet.id, __command.debuggerId());
	}
	
	/**
	 * Creates a packet for a request.
	 *
	 * @param __commandSet The command set to use.
	 * @param __command The command to use.
	 * @return The newly created packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public JDWPPacket request(int __commandSet, int __command)
	{
		JDWPPacket rv = this.__getPacket(true);
		
		// Use the next ID for this packet
		rv._id = this.nextId();
		
		// Packet type information
		rv._commandSet = __commandSet;
		rv._command = __command;
		rv._flags = 0;
		
		// There is no error technically
		rv._errorCode = JDWPErrorType.NO_ERROR;
		rv._rawErrorCode = 0;
		
		return rv;
	}
	
	/**
	 * Sends the packet to the remote end.
	 * 
	 * @param __packet The packet to send.
	 * @throws JDWPException If the packet could not be sent.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/12
	 */
	public void send(JDWPPacket __packet)
		throws JDWPException, NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// If the handshake did not happen, do it now
		synchronized (this)
		{
			if (!this._didHandshake)
				this.__handshake();
		}
		
		// Debug
		if (JDWPCommLink.DEBUG)
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
		
		/* {@squirreljme.error AG01 Could not send the packet. (The packet)} */
		catch (IOException e)
		{
			throw new JDWPException("AG01 " + __packet, e);
		}
	}
	
	/**
	 * Sets the ID sizes.
	 *
	 * @param __idSizes The ID sizes to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public final void setIdSizes(JDWPIdSizes __idSizes)
		throws NullPointerException
	{
		if (__idSizes == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._idSizes = __idSizes;
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
			rv.__resetAndOpen(__open, this._idSizes);
			
			return rv;
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
			if (JDWPCommLink.DEBUG)
				Debugging.debugNote("JDWP: Handshake.");
			
			// The debugger sends the handshake sequence first, so as a client
			// we read from the remote end
			if (this.direction == JDWPCommLinkDirection.CLIENT_TO_DEBUGGER)
			{
				this.__handshakeRead();
				this.__handshakeWrite();
			}
			
			// Otherwise we are the debugger, so we write our handshake first
			else
			{
				this.__handshakeWrite();
				this.__handshakeRead();
			}
			
			// We did the handshake
			this._didHandshake = true;
			
			// Debug
			if (JDWPCommLink.DEBUG)
				Debugging.debugNote("JDWP: Hands shaken at a distance.");
		}
		catch (IOException e)
		{
			/* {@squirreljme.error AG04 Failed to handshake.} */
			throw new JDWPException("AG04", e);
		}
	}
	
	/**
	 * Reads the handshake.
	 *
	 * @throws IOException On read errors.
	 * @since 2024/01/19
	 */
	private void __handshakeRead()
		throws IOException
	{
		// The debugger sends the handshake sequence first
		int seqLen = JDWPCommLink._HANDSHAKE_SEQUENCE.length;
		byte[] debuggerShake = new byte[seqLen];
		
		// Read in the handshake
		for (int i = 0; i < seqLen; i++)
		{
			int read = this.in.read();
			
			/* {@squirreljme.error AG02 EOF reading handshake.} */
			if (read < 0)
				throw new JDWPException("AG02");
			
			debuggerShake[i] = (byte)read;
		}
		
		/* {@squirreljme.error AG03 Debugger sent an invalid handshake.} */
		if (!Arrays.equals(debuggerShake, JDWPCommLink._HANDSHAKE_SEQUENCE))
			throw new JDWPException("AG03");
	}
	
	/**
	 * Writes the handshake.
	 *
	 * @throws IOException On write errors.
	 * @since 2024/01/19
	 */
	private void __handshakeWrite()
		throws IOException
	{			
		// We then reply with our own handshake
		this.out.write(JDWPCommLink._HANDSHAKE_SEQUENCE);
		this.out.flush();
	}
}
