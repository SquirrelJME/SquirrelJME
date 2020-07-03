// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIDaemon;

/**
 * This is the user interface daemon which manages and sends events around
 * accordingly.
 *
 * @since 2020/07/03
 */
final class __UIDaemon__
	implements UIDaemon
{
	/** Has this been stopped? */
	private boolean _stopped;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/03
	 */
	@Override
	public void close()
	{
		this._stopped = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/03
	 */
	@Override
	public void run()
	{
		// Debug
		Debugging.debugNote("Passive UIDaemon starting...");
		
		for (;;)
			synchronized (this)
			{
				// Stop running?
				if (this._stopped)
					break;
				
				// Wait for a signal to occur
				try
				{
					this.wait(500);
				}
				catch (InterruptedException ignored)
				{
				}
			}
		
		// Debug
		Debugging.debugNote("Passive UIDaemon stopping...");
		
		// Cleanup anything UI related which we can do
		StaticDisplayState.gc();
	}
}
