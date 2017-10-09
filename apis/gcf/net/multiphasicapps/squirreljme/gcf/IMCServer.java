// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

import java.io.InterruptedIOException;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.IMCServerConnection;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.midlet.MidletVersion;
import net.multiphasicapps.squirreljme.unsafe.SystemMail;
import net.multiphasicapps.squirreljme.unsafe.SystemMailException;

/**
 * This implements the server side of an IMC connection.
 *
 * @since 2016/10/12
 */
public class IMCServer
	implements IMCServerConnection
{
	/** The server name. */
	protected final String name;
	
	/** The server version. */
	protected final MidletVersion version;
	
	/** Use authentication mode? */
	protected final boolean authmode;
	
	/** Create interrupts? */
	protected final boolean interrupt;
	
	/** The file descriptor for the mailbox. */
	private final int _mailfd;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the server IMC connection.
	 *
	 * @param __name The name of the server.
	 * @param __ver The version of the server.
	 * @param __auth Is authorization mode used?
	 * @param __int Should interrupts be generated?
	 * @throws IOException On socket initialization error.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public IMCServer(String __name, MidletVersion __ver, boolean __auth,
		boolean __int)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.version = __ver;
		this.authmode = __auth;
		this.interrupt = __int;
		
		// Listen on the mailbox
		byte[] encname = __name.getBytes("utf-8");
		try
		{
			this._mailfd = SystemMail.mailboxListen(encname, 0, encname.length,
				__ver.hashCode(), __auth);
		}
		
		// {@squirreljme.error EC0p Could not open a mailbox for listening.}
		catch (SystemMailException e)
		{
			throw new IOException("EC0p", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public StreamConnection acceptAndOpen()
		throws IOException
	{
		// {@squirreljme.error EC0i Cannot accept new connections after the
		// server has been closed.}
		if (this._closed)
			throw new IOException("EC0i");
		
		// Listen on socket
		int mailfd = this._mailfd;
		boolean interrupt = this.interrupt;
		for (;;)
			try
			{
				// Accept socket
				int clfd = SystemMail.mailboxAccept(mailfd);
				
				// {@squirreljme.error EC0j Connection closed during accept.}
				if (clfd < 0)
					throw new IOException("EC0j");
				
				// Create client socket
				return new IMCClient(clfd, this.name, this.version,
					this.authmode, interrupt);
			}
			
			// {@squirreljme.error EC0q Could not accept the mailbox
			// connection. (The descriptor)}
			catch (SystemMailException e)
			{
				throw new IOException(String.format("EC0q %d", mailfd), e);
			}
			
			// Request interrupted
			catch (InterruptedException e)
			{
				// {@squirreljme.error EC0c Accept of connection interrupted.}
				if (interrupt)
					throw new InterruptedIOException("EC0c");
				
				// Ignore otherwise
				continue;
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public void close()
		throws IOException
	{
		// Ignore if already closed
		if (this._closed)
			return;
		this._closed = true;
		
		// Close it
		try
		{
			SystemMail.mailboxClose(this._mailfd);
		}
		
		// {@squirreljme.error EC0r Could not close the server mailbox.
		// (The descriptor)}
		catch (SystemMailException e)
		{
			throw new IOException(String.format("EC0r %d", this._mailfd), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String getVersion()
	{
		return this.version.toString();
	}
}

