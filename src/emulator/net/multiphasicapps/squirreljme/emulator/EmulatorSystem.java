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
 * This represents a single system which is being emulated, each system has
 * their own filesystem representation and memory pool. All
 * {@link EmulatorProcess}es run on this class.
 *
 * It is possible to directly read the memory that the emulated system is using
 * although it is undefined what the layout is.
 *
 * @since 2016/07/25
 */
public final class EmulatorSystem
{
	/**
	 * Calculates the time of the next event.
	 *
	 * @return The next event time index.
	 * @since 2016/07/25
	 */
	final long __nextEventTime()
	{
		throw new Error("TODO");
	}
}

