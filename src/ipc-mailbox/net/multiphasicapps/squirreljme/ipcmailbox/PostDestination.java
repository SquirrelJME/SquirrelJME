// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipcmailbox;

import net.multiphasicapps.squirreljme.midletid.MidletVersion;

/**
 * This is used as a class which identifies destinations where post offices
 * can be setup for communication.
 *
 * @since 2016/10/13
 */
public final class PostDestination
	extends PostBase
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
	 * @since 2016/10/13
	 */
	public PostOffice accept()
		throws InterruptedException
	{
		// Lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Loop until a client accepts a mailbox
			for (;;)
			{
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
	 * Connects to the given post office.
	 *
	 * @return The post office connection.
	 * @throws InterruptedException If the connect attempt was interrupted.
	 * @since 2016/10/13
	 */
	public PostOffice connect()
		throws InterruptedException
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

