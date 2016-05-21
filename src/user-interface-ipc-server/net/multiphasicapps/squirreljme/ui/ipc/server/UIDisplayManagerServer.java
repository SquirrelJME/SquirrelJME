// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui.ipc.server;

import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This is a server which is given a socket along with a display manager and
 * wraps the given display manager through the socket so that clients may
 * access it.
 *
 * @since 2016/05/21
 */
public class UIDisplayManagerServer
	implements Runnable
{
	/** The socket which acts as the display manager server. */
	protected final KIOSocket socket;
	
	/** The display manager which is wrapped by the IPC. */
	protected final UIDisplayManager manager;
	
	/**
	 * Initializes the wrapper around a display manager and provides an IPC
	 * service
	 *
	 * @param __sock The socket which acts as the server
	 * @param __dm The display manager which this server interface interacts
	 * with to provide an interface to the client.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public UIDisplayManagerServer(KIOSocket __sock, UIDisplayManager __dm)
		throws NullPointerException
	{
		// Check
		if (__sock == null || __dm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.socket = __sock;
		this.manager = __dm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}

