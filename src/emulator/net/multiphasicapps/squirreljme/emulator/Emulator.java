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
 * This class contains the main controller for a given emulator.
 *
 * @since 2016/07/30
 */
public final class Emulator
	implements Runnable
{
	/** The display output to use when. */
	final DisplayOutput _displayout;
	
	/**
	 * Initializes an emulator using the given builder.
	 *
	 * @param __eb The emulator builder to grab initial state from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	Emulator(EmulatorBuilder __eb)
		throws NullPointerException
	{
		// Check
		if (__eb == null)
			throw new NullPointerException("NARG");
		
		// Setup display output where console text goes
		DisplayOutput displayout = __eb._displayout;
		if (displayout == null)
			displayout = new DefaultDisplayOutput();
		this._displayout = displayout;
		
		throw new Error("TODO");
	}
	
	/**
	 * Runs emulation until it terminates.
	 *
	 * @since 2016/07/30
	 */
	public void run()
	{
		throw new Error("TODO");
	}
}

