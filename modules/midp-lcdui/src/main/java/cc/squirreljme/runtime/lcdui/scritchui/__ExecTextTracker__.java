// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Executes text tracker changes in the event loop.
 *
 * @since 2024/07/18
 */
final class __ExecTextTracker__
	implements Runnable
{
	/** The listener used. */
	protected final TextTrackerListener listener;
	
	/** The text to set. */
	protected final String text;
	
	/**
	 * Initializes the executor.
	 *
	 * @param __listener The listener to call.
	 * @param __t The text being set.
	 * @throws NullPointerException If no listener was specified.
	 * @since 2024/07/18
	 */
	__ExecTextTracker__(TextTrackerListener __listener, String __t)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		this.listener = __listener;
		this.text = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	public void run()
	{
		this.listener.textUpdated(this.text);
	}
}
