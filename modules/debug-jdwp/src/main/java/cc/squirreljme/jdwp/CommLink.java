// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	/** The input communication stream. */
	protected final DataInputStream in;
	
	/** The output communication stream. */
	protected final DataOutputStream out;
	
	/** The queue of packets which are freed, they will go back here. */
	private final Deque<JDWPPacket> _freePackets =
		new LinkedList<>();
	
	/** Did we do our handshake? */
	private volatile boolean _didHandshake;
	
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
		throws IOException
	{
		IOException fail = null;
		
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
		
		if (fail != null)
			throw fail;
	}
	
	/**
	 * Polls for the next event, if there is any.
	 * 
	 * @throws JDWPException If there is an issue with the connection.
	 * @return A packet or {@code null} if there are none.
	 * @since 2021/03/10
	 */
	public JDWPPacket poll()
		throws JDWPException
	{
		synchronized (this)
		{
			// If the handshake did not happen, do it now
			if (!this._didHandshake)
				this.__handshake();
			
			throw Debugging.todo();
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
	 * @return A packet that is fresh, this may be recycled from a previous
	 * packet or taken from another.
	 * @since 2021/03/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private JDWPPacket __getPacket()
	{
		Deque<JDWPPacket> freePackets = this._freePackets;
		synchronized (freePackets)
		{
			// If there are no free packets then make a new one
			if (freePackets.isEmpty())
				return new JDWPPacket(freePackets);
			
			// Grab the next free one
			return freePackets.remove();
		}
	}
}
