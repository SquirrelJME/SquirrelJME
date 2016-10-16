// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase.lcdui;

import java.io.IOException;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.lcdui.server.DisplayClient;
import net.multiphasicapps.squirreljme.lcdui.server.DisplayServer;

/**
 * This is the display client which creates a JFrame to show the client
 * display.
 *
 * @since 2016/10/15
 */
public class SwingDisplayClient
	extends DisplayClient
{
	/**
	 * Initializes the swing display client.
	 *
	 * @param __ds The display server.
	 * @param __co The stream connection.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/15
	 */
	public SwingDisplayClient(SwingDisplayServer __ds, StreamConnection __co)
		throws IOException
	{
		super(__ds, __co);
	}
}

