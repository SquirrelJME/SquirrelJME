// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host.views;

import cc.squirreljme.jdwp.host.JDWPHostValue;

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
	 * Is this a null object?
	 *
	 * @param __value The object to check.
	 * @return If it is {@code null} or not.
	 * @since 2022/09/21
	 */
	boolean isNullObject(Object __value);
	
	/**
	 * Reads the value of an array index within the object.
	 *
	 * @param __which What is being read from?
	 * @param __index The index of the array to read from.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readArray(Object __which, int __index, JDWPHostValue __out);
	
	/**
	 * Reads the value of an instance field within the object.
	 *
	 * @param __which What is being read from?
	 * @param __index The index of the field to read from the object.
	 * @param __out Where the value is to be stored.
	 * @return {@code true} if this is a valid value.
	 * @since 2021/04/10
	 */
	boolean readValue(Object __which, int __index, JDWPHostValue __out);
	
	/**
	 * Returns the object type.
	 * 
	 * @param __which Which object to get the type of.
	 * @return The type of the given object.
	 * @since 2021/04/11
	 */
	Object type(Object __which);
}
