// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.SocketConnection;

/**
 * This is a connection to a remote TCP server as a client.
 *
 * @since 2019/05/06
 */
public abstract class TCPClientConnection
	implements SocketConnection
{
	/** The used IP address. */
	protected final IPAddress ipaddr;
	
	/**
	 * Initializes the TCP client connection.
	 *
	 * @param __ip The IP to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/12
	 */
	public TCPClientConnection(IPAddress __ip)
		throws NullPointerException
	{
		if (__ip == null)
			throw new NullPointerException("NARG");
		
		this.ipaddr = __ip;
	}
	
	/**
	 * Sets an option for the socket.
	 *
	 * @param __o The option to use.
	 * @param __v The value to use.
	 * @throws IllegalArgumentException If the option is not valid.
	 * @throws IOException If it could not be set.
	 * @since 2019/05/12
	 */
	protected abstract void doSetSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final AccessPoint getAccessPoint()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getAddress()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getLocalAddress()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getLocalPort()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getPort()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getSocketOption(byte __o)
		throws IllegalArgumentException, IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataInputStream openDataInputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataOutputStream openDataOutputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final InputStream openInputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final OutputStream openOutputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void setSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException
	{
		// {@squirreljme.error EC0f Invalid socket option. (The option)}
		if (__o != KEEPALIVE || __o != LINGER || __o != SNDBUF ||
			__o != RCVBUF || __o != DELAY || __o != TIMEOUT)
			throw new IllegalArgumentException("EC0f " + __o);
		
		// Check options
		switch (__o)
		{
			case LINGER:
				// {@squirreljme.error EC0g Linger cannot be negative.
				// (The requested linger)}
				if (__v < 0)
					throw new IllegalArgumentException("EC0g " + __v);
				break;
			
			case RCVBUF:
			case SNDBUF:
				// {@squirreljme.error EC0h Send/receive buffer size cannot
				// be negative. (The requested buffer size)}
				if (__v < 0)
					throw new IllegalArgumentException("EC0h " + __v);
				break;
			
			case TIMEOUT:
				// {@squirreljme.error EC0i Timeout cannot be negative.
				// (The requested timeout)}
				if (__v < 0)
					throw new IllegalArgumentException("EC0i " + __v);
				break;
		}
		
		// Forward
		this.doSetSocketOption(__o, __v);
	}
}

