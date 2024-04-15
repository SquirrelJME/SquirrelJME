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
import javax.microedition.io.ConnectionNotFoundException;

/**
 * This is a connection factory which does not support any IP based
 * connections. Any attempt to open any kind of connection will result in
 * a connection not being made.
 *
 * @since 2019/05/12
 */
@SuppressWarnings("DuplicateThrows")
public final class NullIPConnectionFactory
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
		/* {@squirreljme.error EC0i Host name resolution not supported.} */
		throw new ConnectionNotFoundException("EC0i");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final TCPClientConnection tcpClientConnect(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		/* {@squirreljme.error EC0j TCP client connections are not
		supported.} */
		throw new ConnectionNotFoundException("EC0j");
	}
}

