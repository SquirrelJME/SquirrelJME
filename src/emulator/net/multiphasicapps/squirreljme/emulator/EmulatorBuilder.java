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

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

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
	
	/** Associated volumes. */
	final Map<String, Volume> _volumes =
		new LinkedHashMap<>();
	
	/** The current display output. */
	volatile DisplayOutput _displayout;
	
	/** The system manager. */
	volatile SystemManager _sm;
	
	/**
	 * Adds a volume where filesystem data may be accessed.
	 *
	 * @param __n The name of the volume.
	 * @param __v The volume for data accessing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final void addVolume(String __n, Volume __v)
		throws NullPointerException
	{
		// Check
		if (__n == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._volumes.put(__n, __v);
		}
	}
	
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

