// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This class is used to construct new emulators since it is possible that new
 * features may be added to the emulator.
 *
 * This class should ease when the emulator changes or adds/removes some new
 * functionality
 *
 * @since 2016/07/30
 */
public final class EmulatorBuilder
{
	/** Lock. */
	final Object _lock =
		new Object();
	
	/** The current display output. */
	volatile DisplayOutput _displayout;
	
	/**
	 * Builds the emulator instance for later emulation.
	 *
	 * @return The instance of the emulator.
	 * @throws IllegalStateException On null arguments.
	 * @since 2016/07/30
	 */
	public final Emulator build()
		throws IllegalStateException
	{
		// Lock
		synchronized (this._lock)
		{
			return new Emulator(this);
		}
	}
	
	/**
	 * Sets the display output to use.
	 *
	 * @param __do The output to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final void setDisplayOutput(DisplayOutput __do)
		throws NullPointerException
	{
		// Check
		if (__do == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._displayout = __do;
		}
	}
}

