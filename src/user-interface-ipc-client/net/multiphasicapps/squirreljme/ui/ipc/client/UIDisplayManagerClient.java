// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui.ipc.client;

import net.multiphasicapps.squirreljme.kernel.KIOConnectionClosedException;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.ipc.DMCommandID;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This implements the client side of a display manager and is.
 *
 * @since 2016/05/21
 */
public class UIDisplayManagerClient
	extends UIDisplayManager
{
	/** The socket connection to the server. */
	protected final KIOSocket socket;
	
	/**
	 * Initializes the display manager client which uses the given socket
	 * connection to the server to access the native UI interface.
	 *
	 * @param __sock The socket to the server.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public UIDisplayManagerClient(KIOSocket __sock)
		throws NullPointerException
	{
		// Check
		if (__sock == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.socket = __sock;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public UIDisplay createDisplay()
	{
		throw new Error("TODO");
	}
}

