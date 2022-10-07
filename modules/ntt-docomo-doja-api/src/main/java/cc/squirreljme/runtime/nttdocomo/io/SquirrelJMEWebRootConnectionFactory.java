// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.HTTPAddress;
import cc.squirreljme.runtime.gcf.HTTPClientConnection;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionOption;

/**
 * This is a factory that creates an HTTP connection which
 * are used for DoJa applications to access their web resources. Note that this
 * is intended to work standalone and not require internet access.
 *
 * @see SquirrelJMEWebRootManager
 * @since 2022/10/07
 */
public class SquirrelJMEWebRootConnectionFactory
	implements CustomConnectionFactory
{
	/** Manager state. */
	private static volatile SquirrelJMEWebRootManager _MANAGER;
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		// Get the existing manager, create one if not yet created
		SquirrelJMEWebRootManager manager;
		synchronized (SquirrelJMEWebRootConnectionFactory.class)
		{
			manager = SquirrelJMEWebRootConnectionFactory._MANAGER;
			if (manager == null)
				SquirrelJMEWebRootConnectionFactory._MANAGER =
					(manager = new SquirrelJMEWebRootManager());
		}
		
		// Setup non-network traversing HTTP connection to the manager
		// Uses DoJa specific HttpConnection
		return new DoJaHttpConnectionAdapter(new HTTPClientConnection(
			HTTPAddress.fromUriPart(__part),
			new SquirrelJMEWebRootHTTPAgentConnector(manager)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String scheme()
	{
		return "squirreljme+webroot";
	}
}
