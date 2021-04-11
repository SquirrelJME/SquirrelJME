// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.trips;

/**
 * A trip on a thread.
 *
 * @since 2021/04/11
 */
public interface JDWPTripThread
	extends JDWPTrip
{
	/**
	 * Indicates that the given thread is alive or death.
	 * 
	 * @param __thread The thread state being changed.
	 * @param __isAlive Is this thread alive?
	 * @since 2021/04/11
	 */
	void alive(Object __thread, boolean __isAlive);
}
