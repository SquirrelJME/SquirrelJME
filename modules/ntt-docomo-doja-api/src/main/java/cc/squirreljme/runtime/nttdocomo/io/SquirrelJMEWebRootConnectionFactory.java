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
	/** The base scheme for this connection. */
	public static final String URI_SCHEME =
		"squirreljme+doja";
	
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
			SquirrelJMEWebRootConnectionFactory.__fixAddress(manager, __part),
			new SquirrelJMEWebRootHTTPAgentConnector(manager)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String scheme()
	{
		return SquirrelJMEWebRootConnectionFactory.URI_SCHEME;
	}
	
	/**
	 * Some SoJa software may be hardcoded in terms of character and address
	 * lengths so that it is unable to figure out or determine the hostname
	 * for an address.
	 * 
	 * @param __manager The manager used.
	 * @param __part The URI part to decode.
	 * @return The fixed or potentially normalized HTTP address.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/11
	 */
	private static HTTPAddress __fixAddress(
		SquirrelJMEWebRootManager __manager, String __part)
		throws NullPointerException
	{
		if (__manager == null || __part == null)
			throw new NullPointerException("NARG");
		
		// Some addresses might be missing their hostname due to truncation
		// and expected hardcoded URI or hostname lengths.
		HTTPAddress added = HTTPAddress.fromUriPartUnchecked(
			"//squirreljme/" + __part);
		HTTPAddress normal = HTTPAddress.fromUriPartUnchecked(__part);
		
		// If we add the hostname so that the URI path leads to an actual
		// valid file then we use this added onto path as a basis to obtain
		// whatever resource we are trying to get.
		if (added != null && __manager.pathExists(added.file))
			return added;
		return normal;
	}
}
