// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This contains functions which are used to interface with the clock.
 *
 * @since 2017/11/10
 */
public abstract class ClockFunctions
{
	/**
	 * Returns a value within the constraints of
	 * {@link System#currentTimeMillis()}.
	 *
	 * @return The milliseconds since the epoch, in UTC.
	 * @since 2017/11/10
	 */
	public abstract long currentTimeMillis();
	
	/**
	 * Returns a value within the constraints of {@link System#nanoTime()}.
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2017/11/10
	 */
	public abstract long nanoTime();
}

