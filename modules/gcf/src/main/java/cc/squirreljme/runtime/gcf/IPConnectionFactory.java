// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;
import java.util.ServiceLoader;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * This factory is used in the creation of IP based connections thus allowing
 * SquirrelJME access to the internet.
 *
 * @since 2019/05/12
 */
public abstract class IPConnectionFactory
{
	/** The existing connection factory. */
	private static volatile IPConnectionFactory _FACTORY;
	
	/**
	 * Resolves the specified IP address.
	 *
	 * @param __addr The IP address.
	 * @return The resolved address.
	 * @throws ConnectionNotFoundException If the host was not found.
	 * @throws IOException If the address could not resolved.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/12
	 */
	public abstract IPAddress resolveAddress(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException;
	
	/**
	 * Opens a TCP client connection to the remote address.
	 *
	 * @param __addr The address to connect to.
	 * @return The connection.
	 * @throws ConnectionNotFoundException If the connection was not found.
	 * @throws IOException On connection issues.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public abstract TCPClientConnection tcpClientConnect(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException;
	
	/**
	 * Returns the factory which manages IP based connections.
	 *
	 * @return The IP connection factory.
	 * @since 2019/05/12
	 */
	public static IPConnectionFactory factory()
	{
		// Already created?
		IPConnectionFactory rv = IPConnectionFactory._FACTORY;
		
		// Create factory?
		if (rv == null)
			synchronized (IPConnectionFactory.class)
			{
				// Check for double create
				rv = IPConnectionFactory._FACTORY;
				if (rv != null)
					return rv;
				
				// Use implementation for this
				/*String ipf = SystemProperties.implementationClass(
					"cc.squirreljme.runtime.gcf.IPConnectionFactory");
				if (ipf != null)
					try
					{
						rv = (IPConnectionFactory)Class.forName(ipf).
							newInstance();
					}
					catch (ClassNotFoundException|InstantiationException|
						IllegalAccessException e)
					{
						e.printStackTrace();
					}*/
				
				// Use the first found service
				if (rv == null)
					for (IPConnectionFactory mb : ServiceLoader.
						<IPConnectionFactory>load(IPConnectionFactory.class))
					{
						rv = mb;
						break;
					}
				
				// Fallback to no connection factory (IP not supported)
				if (rv == null)
					rv = new NullIPConnectionFactory();
				
				// Use it for later and return
				IPConnectionFactory._FACTORY = rv;
			}
		
		return rv;
	}
}

