// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 * This represents a connected client to a display.
 *
 * @since 2016/10/15
 */
public abstract class DisplayClient
	implements Runnable
{
	/** The owning server. */
	protected final DisplayServer server;
	
	/** The output stream. */
	protected final DataOutputStream out;
	
	/** The input stream (this is internally handled). */
	private final DataInputStream _in;
	
	/**
	 * Initializes the client connection.
	 *
	 * @param __ds The owning display server.
	 * @param __con The connection used.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/15
	 */
	public DisplayClient(DisplayServer __ds, StreamConnection __con)
		throws IOException, NullPointerException
	{
		// Check
		if (__ds == null || __con == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.server = __ds;
		this.out = __con.openDataOutputStream();
		this._in = __con.openDataInputStream();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/15
	 */
	@Override
	public final void run()
	{
		// Open the input
		try (DataInputStream in = this._in)
		{
			// Handle all input commands
			for (int cmd;;)
				switch ((cmd = in.readUnsignedByte()))
				{
						// {@squirreljme.error DX04 Unknown display command
						// sent by the client. (The command code)}
					default:
						throw new RuntimeException(String.format("DX04 %d",
							cmd));
				}
		}
		
		// {@squirreljme.error DX03 Failed to read from the display client.}
		catch (IOException e)
		{
			throw new RuntimeException("DX03", e);
		}
	}
}

