// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.util.Objects;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.IMCConnection;
import javax.microedition.midlet.MIDletIdentity;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteID;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteIDFormat;
import net.multiphasicapps.squirreljme.suiteid.MidletVersion;
import net.multiphasicapps.squirreljme.unsafe.SquirrelJME;

/**
 * This implements the client side of the IMC connection.
 *
 * @since 2016/10/13
 */
public class IMCClient
	implements IMCConnection
{
	/** The actually connected remote end. */
	protected final MidletSuiteID connectid;
	
	/** The server name. */
	protected final String servername;
	
	/** The server version. */
	protected final MidletVersion serverversion;
	
	/** Use authorization? */
	protected final boolean authmode;
	
	/** Should interrupts be generated? */
	protected final boolean interrupt;
	
	/** The client mailbox descriptor. */
	private final int _clientfd;
	
	/** Has a stream been opened? */
	private volatile boolean _opened;
	
	/** Has this been closed. */
	private volatile boolean _closed;
	
	/**
	 * Initializes the inter-midlet communication client.
	 *
	 * @param __id The midlet to connect to, if {@code null} then the first
	 * midlet found is used.
	 * @param __sv The server to connect to.
	 * @param __ver The server version.
	 * @param __authmode Is the connection authorized?
	 * @param __interrupt Are interrupts permitted?
	 * @throws ConnectionNotFoundException If the server does not exist.
	 * @throws IOException On other connection errors.
	 * @throws NullPointerException If no server name or version were
	 * specified.
	 * @since 2016/10/13
	 */
	public IMCClient(MidletSuiteID __id, String __sv, MidletVersion __ver,
		boolean __authmode, boolean __interrupt)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		// Check
		if (__sv == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.servername = __sv;
		this.serverversion = __ver;
		this.authmode = __authmode;
		this.interrupt = __interrupt;
		
		// Encode target midlet
		byte[] midb = (__id == null ? null :
			__id.toString().getBytes("utf-8"));
		int mido, midl;
		if (midb == null)
			mido = midl = -1;
		else
		{
			mido = 0;
			midl = midb.length;
		}
		
		// Encode server name
		byte[] svnb = __sv.getBytes("utf-8");
		
		// Connect to server
		int fd;
		try
		{
			// Connect to the remote server
			this._clientfd = (fd = SquirrelJME.mailboxConnect(midb, mido, midl,
				svnb, 0, svnb.length, __ver.hashCode(), __authmode));
		}
		
		// {@squirreljme.error EC0d Could not connect to the remote server.}
		catch (BindException e)
		{
			throw new ConnectionNotFoundException(Objects.toString(
				e.getMessage(), "EC0d"));
		}
		
		// If specified, use the given MIDlet
		if (__id != null)
			this.connectid = __id;
		
		// Otherwise obtain it from the socket
		else
			this.connectid = new MidletSuiteID(new String(
				SquirrelJME.mailboxRemoteID(fd), "utf-8"),
				MidletSuiteIDFormat.JAR);
	}
	
	/**
	 * Initializes a client connection which is obtained its descriptor
	 * from the server.
	 *
	 * @param __clfd The descriptor to bind to.
	 * @param __name The name of the server.
	 * @param __ver The version of the server.
	 * @param __auth Use authentication?
	 * @param __interrupt Are interrupts permitted?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	IMCClient(int __clfd, String __name, MidletVersion __ver, boolean __auth,
		boolean __interrupt)
		throws NullPointerException
	{
		// Check
		if (__name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._clientfd = __clfd;
		this.servername = __name;
		this.serverversion = __ver;
		this.authmode = __auth;
		this.interrupt = __interrupt;
		
		// Determine remote end
		try
		{
			this.connectid = new MidletSuiteID(new String(
				SquirrelJME.mailboxRemoteID(__clfd), "utf-8"),
				MidletSuiteIDFormat.JAR);
		}
		
		// Should never occur
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("OOPS", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
		throws IOException
	{
		// Ignore
		if (this._closed)
			return;
		
		// Only mark closed, because if any data streams are open they could
		// be closed later on.
		this._closed = true;
		
		// If no streams were opened then the client descriptor must be closed
		// so that the descriptors do not leak
		if (!this._opened)
			SquirrelJME.mailboxClose(this._clientfd);
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public MIDletIdentity getRemoteIdentity()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public String getRequestedServerVersion()
	{
		return this.serverversion.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public String getServerName()
	{
		return this.servername;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		// {@squirreljme.error EC0e Cannot open input stream for a closed
		// connection.}
		if (this._closed)
			throw new IOException("EC0e");
		
		// Open stream
		InputStream rv = new __IMCInputStream__(this._clientfd,
			this.interrupt);
		this._opened = true;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		// {@squirreljme.error EC0f Cannot open input stream for a closed
		// connection.}
		if (this._closed)
			throw new IOException("EC0f");
		
		// Open stream
		OutputStream rv = new __IMCOutputStream__(this._clientfd);
		this._opened = true;
		return rv;
	}
}

