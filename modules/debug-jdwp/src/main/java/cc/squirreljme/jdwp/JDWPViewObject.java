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
	extends JDWPViewValidObject
{
	/**
	 * Returns the array length.
	 * 
	 * @param __which The object to get the length of.
	 * @return The array length, negative for objects that are not arrays.
	 * @since 2021/04/11
	 */
	int arrayLength(Object __which);
	
	/**
	 * Reads the value of an array index within the object.
	 *
	 * @param __which What is being read from?
	 * @param __index The index of the array to read from.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readArray(Object __which, int __index, JDWPValue __out);
	
	/**
	 * Reads the value of an instance field within the object.
	 *
	 * @param __which What is being read from?
	 * @param __index The index of the field to read from the object.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readValue(Object __which, int __index, JDWPValue __out);
	
	/**
	 * Sets the trip on the field value.
	 * 
	 * @param __which What is a trip being set on?
	 * @param __index The index of the field to trip on.
	 * @param __trip The trip to register, may be {@code null} to clear.
	 * @return If the trip was valid and became set.
	 * @since 2021/04/11
	 */
	boolean setTrip(Object __which, int __index, JDWPTripValue __trip);
	
	/**
	 * Returns the object type.
	 * 
	 * @param __which Which object to get the type of.
	 * @return The type of the given object.
	 * @since 2021/04/11
	 */
	Object type(Object __which);
}
