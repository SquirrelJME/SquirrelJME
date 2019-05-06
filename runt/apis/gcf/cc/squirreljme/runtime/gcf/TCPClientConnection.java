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
 * This is a connection to a remote TCP server.
 *
 * @since 2019/05/06
 */
public final class TCPClientConnection
	implements SocketConnection
{
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
	public final int getSocketOption(byte __a)
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
	public final void setSocketOption(byte __a, int __b)
		throws IllegalArgumentException, IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Opens a client connection to the server.
	 *
	 * @param __addr The address to connect to.
	 * @return The connection.
	 * @throws ConnectionNotFoundException If the connection was not found.
	 * @throws IOException On connection issues.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final TCPClientConnection connect(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		throw new ConnectionNotFoundException("TODO " + __addr);
	}
}

