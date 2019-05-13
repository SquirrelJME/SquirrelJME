// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.runtime.gcf.IPAddress;
import cc.squirreljme.runtime.gcf.IPConnectionFactory;
import cc.squirreljme.runtime.gcf.TCPClientConnection;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.SocketConnection;

/**
 * This is a TCP client connection that is backed on top of Java SE.
 *
 * @since 2019/05/12
 */
public class JavaSETCPClientConnection
	extends TCPClientConnection
{
	/** The socket this connects through. */
	protected final Socket socket;
	
	/** Default receive buffer size for this socket. */
	protected final int defrbs;
	
	/** Default send buffer size for this socket. */
	protected final int defsbs;
	
	/** Default timeout. */
	protected final int deftim;
	
	/**
	 * Initializes the wrapped socket.
	 *
	 * @param __addr The target address.
	 * @param __sock The socket to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/12
	 */
	public JavaSETCPClientConnection(IPAddress __addr, Socket __sock)
		throws NullPointerException
	{
		super(__addr);
		
		if (__sock == null)
			throw new NullPointerException("NARG");
		
		this.socket = __sock;
		
		// Get default buffer sizes for setting back
		int defrbs = 512,
			defsbs = 512,
			deftim = 0;
		try
		{
			defrbs = __sock.getReceiveBufferSize();
			defsbs = __sock.getSendBufferSize();
			deftim = __sock.getSoTimeout();
		}
		catch (SocketException e)
		{
		}
		
		// Set
		this.defrbs = defrbs;
		this.defsbs = defsbs;
		this.deftim = deftim;
	}
	
	/**
	 * Performs connection close.
	 *
	 * @throws IOException If it could not be closed.
	 * @since 2019/05/13
	 */
	protected final void doClose()
		throws IOException
	{
		this.socket.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	protected final InputStream doOpenInputStream()
		throws IOException
	{
		return socket.getInputStream();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	protected final OutputStream doOpenOutputStream()
		throws IOException
	{
		return socket.getOutputStream();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	protected final void doSetSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException
	{
		Socket socket = this.socket;
		switch (__o)
		{
			case DELAY:
				socket.setTcpNoDelay(__v == 0);
				break;
			
			case KEEPALIVE:
				socket.setKeepAlive(__v != 0);
				break;
			
			case LINGER:
				socket.setSoLinger(__v != 0, __v);
				break;
			
			case RCVBUF:
				if (__v == 0)
					socket.setReceiveBufferSize(this.defrbs);
				else
					socket.setReceiveBufferSize(__v);
				break;
			
			case SNDBUF:
				if (__v == 0)
					socket.setSendBufferSize(this.defsbs);
				else
					socket.setSendBufferSize(__v);
				break;
			
			case TIMEOUT:
				if (__v == 0)
					socket.setSoTimeout(this.deftim);
				else
					socket.setSoTimeout(__v);
				break;
			
			default:
				throw new todo.TODO();
		}
	}
}

