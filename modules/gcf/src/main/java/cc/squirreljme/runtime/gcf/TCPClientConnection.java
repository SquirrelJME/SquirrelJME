// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.AccessPoint;
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
	
	/** State tracker. */
	protected final ConnectionStateTracker tracker =
		new ConnectionStateTracker();
	
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
	 * Performs connection close.
	 *
	 * @throws IOException If it could not be closed.
	 * @since 2019/05/13
	 */
	protected abstract void doClose()
		throws IOException;
	
	/**
	 * Performs the actual input stream open.
	 *
	 * @return The resulting stream.
	 * @throws IOException If it could not be opened.
	 * @since 2019/05/13
	 */
	protected abstract InputStream doOpenInputStream()
		throws IOException;
	
	/**
	 * Performs the actual output stream open.
	 *
	 * @return The resulting stream.
	 * @throws IOException If it could not be opened.
	 * @since 2019/05/13
	 */
	protected abstract OutputStream doOpenOutputStream()
		throws IOException;
	
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
		// Tracker knows if the streams have been closed...
		ConnectionStateTracker tracker = this.tracker;
		
		// Close input
		IOException supin = null;
		if (!tracker._inclosed)
			try
			{
				this.openInputStream().close();
			}
			catch (IOException e)
			{
				supin = e;
			}
		
		// Close output
		IOException supout = null;
		if (!tracker._outclosed)
			try
			{
				this.openOutputStream().close();
			}
			catch (IOException e)
			{
				supout = e;
			}
		
		// Forward close
		IOException supclo = null;
		try
		{
			this.doClose();
		}
		catch (IOException e)
		{
			supclo = e;
		}
		
		// Set tracker as closed
		tracker._inclosed = true;
		tracker._outclosed = true;
		
		// Exceptions were thrown?
		if (supin != null || supout != null || supclo != null)
		{
			/* {@squirreljme.error EC0k The connection could not be closed
			properly.} */
			IOException t = new IOException("EC0k");
			
			// Add suppressed exceptions
			if (supin != null)
				t.addSuppressed(supin);
			if (supout != null)
				t.addSuppressed(supout);
			if (supclo != null)
				t.addSuppressed(supclo);
			
			// Toss it
			throw t;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final AccessPoint getAccessPoint()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getAddress()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getLocalAddress()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getLocalPort()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getPort()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getSocketOption(byte __o)
		throws IllegalArgumentException, IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(this.openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final InputStream openInputStream()
		throws IOException
	{
		/* {@squirreljme.error EC0l The input has been closed.} */
		ConnectionStateTracker tracker = this.tracker;
		if (tracker._inclosed)
			throw new IOException("EC0l");
		return new TrackedInputStream(tracker, this.doOpenInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final OutputStream openOutputStream()
		throws IOException
	{
		/* {@squirreljme.error EC0m The output has been closed.} */
		ConnectionStateTracker tracker = this.tracker;
		if (tracker._outclosed)
			throw new IOException("EC0m");
		return new TrackedOutputStream(tracker, this.doOpenOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void setSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException
	{
		/* {@squirreljme.error EC0n Invalid socket option. (The option)} */
		if (__o != SocketConnection.KEEPALIVE || __o != SocketConnection.LINGER || __o != SocketConnection.SNDBUF ||
			__o != SocketConnection.RCVBUF || __o != SocketConnection.DELAY || __o != SocketConnection.TIMEOUT)
			throw new IllegalArgumentException("EC0n " + __o);
		
		// Check options
		switch (__o)
		{
			case SocketConnection.LINGER:
				/* {@squirreljme.error EC0o Linger cannot be negative.
				(The requested linger)} */
				if (__v < 0)
					throw new IllegalArgumentException("EC0o " + __v);
				break;
			
			case SocketConnection.RCVBUF:
			case SocketConnection.SNDBUF:
				/* {@squirreljme.error EC0p Send/receive buffer size cannot
				be negative. (The requested buffer size)} */
				if (__v < 0)
					throw new IllegalArgumentException("EC0p " + __v);
				break;
			
			case SocketConnection.TIMEOUT:
				/* {@squirreljme.error EC0q Timeout cannot be negative.
				(The requested timeout)} */
				if (__v < 0)
					throw new IllegalArgumentException("EC0q " + __v);
				break;
		}
		
		// Forward
		this.doSetSocketOption(__o, __v);
	}
}

