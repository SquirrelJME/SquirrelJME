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
import java.io.InterruptedIOException;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProtocol;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer;

/**
 * This class manages a second thread that is used to receive communications
 * from the server when stuff happens.
 *
 * @since 2016/10/15
 */
final class __DisplayClient__
	implements AutoCloseable, Runnable
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The owning display. */
	protected final Display display;
	
	/** The thread that processes client connections. */
	private volatile Thread _thread;
	
	/** Output connection. */
	private volatile DataOutputStream _out;
	
	/** The active count. */
	private volatile int _count;
	
	/** Stay open? */
	private volatile boolean _stay;
	
	/**
	 * Initializes the display client.
	 *
	 * @param __d The display owning this.
	 * @throws NullPointerException On null arguments
	 * @since 2016/10/15
	 */
	__DisplayClient__(Display __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.display = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/15
	 */
	@Override
	public void close()
	{
		// Lock
		synchronized (this.lock)
		{
			// Reduce count
			int count = --this._count;
			
			// Kill the running thread?
			if (!this._stay && count == 0)
			{
				Thread thread = this._thread;
				if (thread != null)
				{
					// Clear it
					this._thread = null;
					this._out = null;
					
					// Interrupt it so it does not wait on IO
					thread.interrupt();	
				}
			}
		}
	}
	
	/**
	 * If the client is closed, it will be opened, bound, and returned.
	 *
	 * @param __stay If {@code true} then even when the count reaches zero
	 * the client will remain open. This should be used when there 
	 * @return {@code this}.
	 * @since 2016/10/15
	 */
	public __DisplayClient__ open(boolean __stay)
	{
		// Lock
		synchronized (this.lock)
		{
			// Increase count
			int count = this._count++;
			
			// Potentially create the given thread?
			if (count == 0)
			{
				Thread thread = this._thread;
				if (thread == null)
				{
					// Create new thread
					Thread t = new Thread(this, "SquirrelJMEDisplayClient");
					
					// Set
					this._thread = t;
					
					// Start it
					t.start();
					
					// Stay open?
					if (__stay)
						this._stay = true;
				}
			}
			
			// Self, because of AutoCloseable
			return this;
		}
	}
	
	/**
	 * Returns the output connection.
	 *
	 * @return The output connection.
	 * @since 2016/10/15
	 */
	public DataOutputStream out()
	{
		return this._out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/15
	 */
	@Override
	public void run()
	{
		// Get display
		Display display = this.display;
		
		// Create connection
		try (StreamConnection sock = (StreamConnection)Connector.open(
			DisplayServer.CLIENT_URI, Connector.READ_WRITE, true);
			DataInputStream in = sock.openDataInputStream();
			DataOutputStream out = sock.openDataOutputStream())
		{
			// Set output connection
			this._out = out;
			
			// Bind to it
			out.write(DisplayProtocol.COMMAND_BIND_DISPLAY);
			out.write(display._descriptor);
			out.flush();
			
			// Loop after bind
			for (;;)
				try
				{
					// This thread is not to be used anymore
					if (this._thread != Thread.currentThread())
						break;
				
					// Read command
					int cmd = in.readUnsignedByte();
					switch (cmd)
					{
							// {@squirreljme.error EB0b Unknown incoming
							// display command. (The command)}
						default:
							throw new RuntimeException(String.format("EB0b %d",
								cmd));
					}
				}
				catch (InterruptedIOException e)
				{
					// Ignore
				}
		}
		
		// {@squirreljme.error EB0a There was an error reading from the
		// display thread.}
		catch (IOException e)
		{
			throw new RuntimeException("EB0a", e);
		}
	}
}

