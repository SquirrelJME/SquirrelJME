// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.SocketConnection;

/**
 * This is a connection factory which does not support any IP based
 * connections. Any attempt to open any kind of connection will result in
 * a connection not being made.
 *
 * @since 2019/05/12
 */
public final class NullIPConnectionFactory
	extends IPConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final TCPClientConnection tcpClientConnect(IPAddress __addr)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		// {@squirreljme.error EC0d TCP client connections are not
		// supported.}
		throw new ConnectionNotFoundException("EC0d");
	}
}

