// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * This class acts as the main controller interface for JDWP and acts as a kind
 * of polling system to interface with something.
 *
 * @since 2021/03/08
 */
public final class JDWPController
	implements Closeable
{
	/** Handshake bytes. */
	private static final byte[] _HANDSHAKE_SEQUENCE =
		{'J', 'D', 'W', 'P', '-', 'H', 'a', 'n', 'd', 's', 'h', 'a', 'k', 'e'};
	
	/** The binding, which is called to perform any actions. */
	protected final JDWPBinding bind;
	
	/** The input communication stream. */
	protected final DataInputStream in;
	
	/** The output communication stream. */
	protected final DataOutputStream out;
	
	/**
	 * Initializes the controller which manages the communication of JDWP.
	 * 
	 * @param __bind The binding to use.
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPController(JDWPBinding __bind, InputStream __in,
		OutputStream __out)
		throws NullPointerException
	{
		if (__bind == null || __in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.bind = __bind;
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
	}
	
	/**
	 * Performs the handshake for JDWP.
	 * 
	 * @throws IOException On read/write errors.
	 * @since 2021/03/08
	 */
	protected void handshake()
		throws IOException
	{
		// The debugger sends the handshake sequence first
		int seqLen = JDWPController._HANDSHAKE_SEQUENCE.length;
		byte[] debuggerShake = new byte[seqLen];
		
		// Read in the handshake
		for (int i = 0; i < seqLen; i++)
		{
			int read = this.in.read();
			
			// {@squirreljme.error AG02 EOF reading handshake.}
			if (read < 0)
				throw new IOException("AG02");
			
			debuggerShake[i] = (byte)read;
		}
		
		// {@squirreljme.error AG03 Debugger sent an invalid handshake.}
		if (!Arrays.equals(debuggerShake, JDWPController._HANDSHAKE_SEQUENCE))
			throw new IOException("AG03");
		
		// We then reply with our own handshake
		this.out.write(JDWPController._HANDSHAKE_SEQUENCE);
		this.out.flush();
	}
}
