// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.trips.JDWPTripValue;

/**
 * An object viewer.
 *
 * @since 2021/04/10
 */
public interface JDWPViewObject
	extends JDWPView
{
	/**
	 * Checks if the given object is an array.
	 * 
	 * @param __what What is being checked?
	 * @return If the object is an array.
	 * @since 2021/04/10
	 */
	boolean isArray(Object __what);
	
	/**
	 * Reads the value of an instance field within the object.
	 *
	 * @param __what What is being read from?
	 * @param __index The index of the field to read from the object.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readValue(Object __what, int __index, JDWPValue __out);
	
	/**
	 * Sets the trip on the field value.
	 * 
	 * @param __what What is a trip being set on?
	 * @param __index The index of the field to trip on.
	 * @param __trip The trip to register, may be {@code null} to clear.
	 * @return If the trip was valid and became set.
	 * @since 2021/04/11
	 */
	boolean setTrip(Object __what, int __index, JDWPTripValue __trip);
}
