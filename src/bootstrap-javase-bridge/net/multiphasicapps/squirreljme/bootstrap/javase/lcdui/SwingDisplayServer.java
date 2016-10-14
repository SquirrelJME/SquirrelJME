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
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer;
import net.multiphasicapps.squirreljme.midp.lcdui.VirtualDisplay;

/**
 * This implements the display server which uses Swing as its backing
 * widget system.
 *
 * @since 2016/10/11
 */
public class SwingDisplayServer
	extends DisplayServer
{
	/** There is only a single display used by the Swing code. */
	protected final SwingVirtualDisplay display =
		new SwingVirtualDisplay(this, (byte)0);
	
	/**
	 * Initializes the swing display server.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/10/11
	 */
	public SwingDisplayServer()
		throws IOException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	protected void modifyThread(Thread __t)
	{
		// Make the display server thread a daemon so it is not considered
		// as a user thread
		if (__t != null)
			__t.setDaemon(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/14
	 */
	@Override
	protected VirtualDisplay[] queryDisplays()
	{
		return new VirtualDisplay[]{this.display};
	}
}

