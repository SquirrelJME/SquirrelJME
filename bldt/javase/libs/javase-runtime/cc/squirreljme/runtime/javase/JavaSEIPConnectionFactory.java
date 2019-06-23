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
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.SocketConnection;

/**
 * This is a connection factory for IP based connection which is built on top
 * of Java SE's networking support.
 *
 * @since 2019/05/12
 */
public class JavaSEIPConnectionFactory
	extends IPConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final IPAddress resolveAddress(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		// Try to resolve it
		try
		{
			// Resolve and build new address
			return new IPAddress(InetAddress.getByName(__addr.hostname).
				getHostAddress(), __addr.port);
		}
		
		// {@squirreljme.error AF05 Unknown host.}
		catch (UnknownHostException e)
		{
			IOException t = new ConnectionNotFoundException("AF05");
			t.initCause(e);
			throw t;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final TCPClientConnection tcpClientConnect(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		// Create and wrap socket
		return new JavaSETCPClientConnection(__addr,
			new Socket(__addr.hostname, __addr.port));
	}
}

