// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.lcdui.common.DisplayProtocol;

/**
 * This represents a connected client to a display.
 *
 * The display client may provide access to multiple virtualized displays or
 * just have a single display be made available.
 *
 * @since 2016/10/15
 */
public abstract class DisplayClient
	implements Runnable
{
	/** The owning server. */
	protected final DisplayServer server;
	
	/** The lock for output. */
	protected final Object outlock =
		new Object();
	
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
	 * Refreshes the set of displays.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/16
	 */
	public final void refreshDisplays()
		throws IOException
	{
		// 
		if (true)
			throw new Error("TODO");
		
		// Send to other side
		synchronized (this.outlock)
		{
			DataOutputStream out = this.out;
			throw new Error("TODO");
		}
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
						// Update displays
					case DisplayProtocol.CLIENT_COMMAND_GET_DISPLAYS:
						refreshDisplays();
						break;
					
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

