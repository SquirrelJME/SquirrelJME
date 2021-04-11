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
 * A trip on a value change.
 *
 * @since 2021/04/11
 */
public interface JDWPTripValue
	extends JDWPTrip
{
	/**
	 * Is this a read trip?
	 * 
	 * @return If this is a read trip.
	 * @since 2021/04/11
	 */
	boolean isRead();
	
	/**
	 * Is this a write trip?
	 * 
	 * @return If this is a write trip.
	 * @since 2021/04/11
	 */
	boolean isWrite();
	
	/**
	 * Signals that the object state has changed.
	 * 
	 * @param __thread The thread this was called from.
	 * @param __what Which object is being set?
	 * @param __dx The index of the field being set.
	 * @since 2021/04/11
	 */
	void signalTrip(Object __thread, Object __what, int __dx);
}
