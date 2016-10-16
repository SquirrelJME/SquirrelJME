// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.lcdui.common.DisplayProperty;
import net.multiphasicapps.squirreljme.lcdui.common.DisplayProtocol;

/**
 * This class manages the connection to/from the server.
 *
 * @since 2016/10/15
 */
class __ServerConnection__
	implements Runnable
{
	/** Input data. */
	protected final DataInputStream in;
	
	/** Output data. */
	protected final DataOutputStream out;
	
	/**
	 * Opens the default server connection.
	 *
	 * @since 2016/10/15
	 */
	__ServerConnection__()
	{
		this(__openDefaultConnection());
	}
	
	/**
	 * Sets up the server connection using the given connection.
	 *
	 * @param __sc The connection to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/15
	 */
	__ServerConnection__(StreamConnection __sc)
		throws NullPointerException
	{
		// Check
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		// Open streams
		try (StreamConnection client = __sc)
		{
			this.in = client.openDataInputStream();
			this.out = client.openDataOutputStream();
		}
		
		// {@squirreljme.error EB07 Could not open the data streams.}
		catch (IOException e)
		{
			throw new RuntimeException("EB07", e);
		}
		
		// Create socket thread
		Thread t = new Thread(this, "SquirrelJMEDisplayClient");
		
		// Start it
		t.start();
	}
	
	/**
	 * Returns the displays available on the server.
	 *
	 * @return The server display set.
	 * @since 2016/10/15
	 */
	public Display[] getDisplays()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/15
	 */
	@Override
	public void run()
	{
		// The client virtually always reads to try to update any display
		// and cause events to fire for displayables
		try (DataInputStream in = this.in)
		{
			// Read for a very long time
			for (int cmd;;)
				switch ((cmd = in.readUnsignedByte()))
				{
						// {@squirreljme.error EB0b Unknown display command
						// passed by the server. (The command used)}
					default:
						throw new RuntimeException(String.format("EB0b %d",
							cmd));
				}
		}
		
		// {@squirreljme.error EB0a If there was a read error.}
		catch (IOException e)
		{
			throw new RuntimeException("EB0a", e);
		}
	}
	
	/**
	 * Opens the default connection to the server.
	 *
	 * @return The connection to the server.
	 * @since 2016/10/15
	 */
	private static StreamConnection __openDefaultConnection()
	{
		// Could fail
		try
		{
			return (StreamConnection)Connector.open(
				Objects.toString(
				System.getProperty(DisplayProtocol.DISPLAY_CLIENT_PROPERTY),
				DisplayProtocol.DEFAULT_CLIENT_URI));
		}
		
		// {@squirreljme.error EB06 Could not open the default connection to
		// the server.}
		catch (IOException e)
		{
			throw new RuntimeException("EB06");
		}
	}
}

