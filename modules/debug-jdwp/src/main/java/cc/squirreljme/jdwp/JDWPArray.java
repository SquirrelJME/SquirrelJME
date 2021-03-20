// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents an array.
 *
 * @since 2021/03/17
 */
public interface JDWPArray
	extends JDWPObject
{
	/**
	 * Returns the array length.
	 * 
	 * @return The array length.
	 * @since 2021/03/19
	 */
	int debuggerArrayLength();
	
	/**
	 * Obtains the value from the given array.
	 * 
	 * @param __i The index to get.
	 * @param __value The resultant value.
	 * @return If this value is valid.
	 * @since 2021/03/20
	 */
	boolean debuggerArrayGet(int __i, JDWPValue __value);
	
	/**
	 * Returns the field descriptor of the component type.
	 * 
	 * @return The field descriptor for the component type.
	 * @since 2021/03/20
	 */
	String debuggerComponentDescriptor();
}
