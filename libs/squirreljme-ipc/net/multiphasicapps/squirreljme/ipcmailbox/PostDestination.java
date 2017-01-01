// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipcmailbox;

import java.io.Closeable;
import java.io.IOException;
import net.multiphasicapps.squirreljme.midlet.MidletVersion;

/**
 * This is used as a class which identifies destinations where post offices
 * can be setup for communication.
 *
 * @since 2016/10/13
 */
public final class PostDestination
	extends PostBase
	implements Closeable
{
	/** The post office lock. */
	protected final Object lock =
		new Object();
	
	/** The name of the server. */
	protected final String name;
	
	/** The version the server uses. */
	protected final MidletVersion version;
	
	/** Authorized mode? */
	protected final boolean authmode;
	
	/** The number of clients wanting post offices. */
	private volatile int _wantoffices;
	
	/** The post office that just opened. */
	private volatile PostOffice _openoffice;
	
	/** Was this closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the post destination.
	 *
	 * @since 2016/10/13
	 */
	public PostDestination(String __name, MidletVersion __ver, boolean __am)
		throws NullPointerException
	{
		// Check
		if (__name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.version = __ver;
		this.authmode = __am;
	}
	
	/**
	 * Accepts a connection to this post office.
	 *
	 * Return The post office connection.
	 * @throws InterruptedException If the accept request was interrupted.
	 * @throws IOException If the destination was closed during acceptance.
	 * @since 2016/10/13
	 */
	public PostOffice accept()
		throws InterruptedException, IOException
	{
		// Lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Loop until a client accepts a mailbox
			for (;;)
			{
				// {@squirreljme.error BW05 Cannot accept more post boxes
				// because the destination has closed.}
				if (this._closed)
					throw new IOException("BW05");
				
				// A thread wants a post office?
				int wants = this._wantoffices;
				if (wants > 0)
				{
					// Create it
					PostOffice rv = new PostOffice();
					this._openoffice = rv;
				
					// Notify all threads that are waiting
					lock.notifyAll();
				
					// Return it
					return rv;
				}
			
				// Wait for a post office request
				lock.wait();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
	{
		// Only close once
		if (this._closed)
			return;
		this._closed = true;
		
		// Lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Tell all threads that this is now closed
			lock.notifyAll();
		}
	}
	
	/**
	 * Connects to the given post office.
	 *
	 * @return The post office connection.
	 * @throws InterruptedException If the connect attempt was interrupted.
	 * @throws IOException If the destination was closed.
	 * @since 2016/10/13
	 */
	public PostOffice connect()
		throws InterruptedException, IOException
	{
		// Lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Try to get a new office
			try
			{
				// Mark that a client wants a new post office
				this._wantoffices++;
				
				// Notify the destination that an office is requested
				lock.notifyAll();
				
				// We got the lock back, check if there is an office waiting
				for (;;)
				{
					// {@squirreljme.error BW04 The destination was closed.}
					if (this._closed)
						throw new IOException("BW04");
					
					// See if an office was created
					PostOffice rv = this._openoffice;
					if (rv != null)
						return rv;
				
					// If it was not, then wait for one to appear
					lock.wait();
				}
			}
			
			// Lower the number of clients that want offices
			finally
			{
				this._wantoffices--;
			}
		}
	}
	
	/**
	 * Returns the name of the server.
	 *
	 * @return The server name.
	 * @since 2016/10/13
	 */
	public final String serverName()
	{
		return this.name;
	}
	
	/**
	 * Returns the version of the server.
	 *
	 * @return The server version.
	 * @since 2016/10/13
	 */
	public final MidletVersion serverVersion()
	{
		return this.version;
	}
}

