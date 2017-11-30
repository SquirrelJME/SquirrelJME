// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.gcf;

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
import net.multiphasicapps.squirreljme.runtime.cldc.MailboxException;
import net.multiphasicapps.squirreljme.runtime.cldc.MailboxFunctions;
import net.multiphasicapps.squirreljme.runtime.cldc.RuntimeBridge;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteVersion;

/**
 * This implements the client side of the IMC connection.
 *
 * @since 2016/10/13
 */
public class IMCClient
	implements IMCConnection
{
	/** The actually connected remote end. */
	protected final IMCHostName connectid;
	
	/** The server name. */
	protected final String servername;
	
	/** The server version. */
	protected final SuiteVersion serverversion;
	
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
	public IMCClient(IMCHostName __id, String __sv, SuiteVersion __ver,
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
		
		// Need the mailbox
		MailboxFunctions mbfunc = RuntimeBridge.MAILBOX;
		
		// Connect to server
		int fd;
		try
		{
			// Connect to the remote server
			this._clientfd = (fd = mbfunc.connect(midb, mido, midl,
				svnb, 0, svnb.length, __ver.hashCode(), __authmode));
		}
		
		// {@squirreljme.error EC05 Could not connect to the remote server.}
		catch (MailboxException e)
		{
			throw new ConnectionNotFoundException(Objects.toString(
				e.getMessage(), "EC05"));
		}
		
		// If specified, use the given MIDlet
		if (__id != null)
			this.connectid = __id;
		
		// Otherwise obtain it from the socket
		else
			try
			{
				this.connectid = new IMCHostName(new String(
					mbfunc.remoteId(fd), "utf-8"));
			}
			
			// {@squirreljme.error EC06 Could not determine the identifier
			// of the remote system. (The descriptor)}
			catch (MailboxException e)
			{
				throw new IOException(String.format("EC06 %d", fd), e);
			}
	}
	
	/**
	 * Initializes a client connection which obtains its descriptor
	 * from the server.
	 *
	 * @param __clfd The descriptor to bind to.
	 * @param __name The name of the server.
	 * @param __ver The version of the server.
	 * @param __auth Use authentication?
	 * @param __interrupt Are interrupts permitted?
	 * @throws IOException If the connection could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	IMCClient(int __clfd, String __name, SuiteVersion __ver, boolean __auth,
		boolean __interrupt)
		throws IOException, NullPointerException
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
		
		// Need the mailbox
		MailboxFunctions mbfunc = RuntimeBridge.MAILBOX;
		
		// Determine remote end
		try
		{
			this.connectid = new IMCHostName(new String(
				mbfunc.remoteId(__clfd), "utf-8"));
		}
		
		// {@squrireljme.error EC0t Could not determine the name of the
		// remote connection. (The descriptor)}
		catch (MailboxException e)
		{
			throw new IOException(String.format("EC0t %d", __clfd), e);
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
		
		// Need the mailbox
		MailboxFunctions mbfunc = RuntimeBridge.MAILBOX;
		
		// If no streams were opened then the client descriptor must be closed
		// so that the descriptors do not leak
		if (!this._opened)
			try
			{
				mbfunc.close(this._clientfd);
			}
			
			// {@squirreljme.error EC07 Could not close the client connection.
			// (The client descriptor)}
			catch (MailboxException e)
			{
				throw new IOException(String.format("EC07 %d", this._clientfd),
					e);
			}
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public MIDletIdentity getRemoteIdentity()
	{
		throw new todo.TODO();
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
		// {@squirreljme.error EC08 Cannot open input stream for a closed
		// connection.}
		if (this._closed)
			throw new IOException("EC08");
		
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
		// {@squirreljme.error EC09 Cannot open input stream for a closed
		// connection.}
		if (this._closed)
			throw new IOException("EC09");
		
		// Open stream
		OutputStream rv = new __IMCOutputStream__(this._clientfd);
		this._opened = true;
		return rv;
	}
}

