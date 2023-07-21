// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.StreamConnection;

/**
 * Creates a socket to the target.
 *
 * @since 2022/10/07
 */
public class SocketHTTPAgentConnector
	implements HTTPAgentConnector
{
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public StreamConnection connectStream(HTTPAddress __address)
		throws IOException, NullPointerException
	{
		if (__address == null)
			throw new NullPointerException("NARG");
		
		return (SocketConnection)Connector.open(
			"socket://" + __address.ipaddr);
	}
}
