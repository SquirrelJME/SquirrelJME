// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.HTTPAddress;
import cc.squirreljme.runtime.gcf.HTTPAgentConnector;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 * The connector to access {@link SquirrelJMEWebRootManager}.
 *
 * @since 2022/10/07
 */
@SquirrelJMEVendorApi
public class SquirrelJMEWebRootHTTPAgentConnector
	implements HTTPAgentConnector
{
	/** This manager this accesses. */
	@SquirrelJMEVendorApi
	protected final SquirrelJMEWebRootManager manager;
	
	/**
	 * Initializes the agent connector to the given manager.
	 * 
	 * @param __manager The manager to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/07
	 */
	@SquirrelJMEVendorApi
	public SquirrelJMEWebRootHTTPAgentConnector(
		SquirrelJMEWebRootManager __manager)
		throws NullPointerException
	{
		if (__manager == null)
			throw new NullPointerException("NARG");
		
		this.manager = __manager;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public StreamConnection connectStream(HTTPAddress __address)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
