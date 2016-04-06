// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This represents an object which is an array.
 *
 * @since 2016/04/06
 */
public class JVMArray
	extends JVMObject
{
	/** The length of this array . */
	protected final int arraylength;
	
	/**
	 * Initializes the array type.
	 *
	 * @param __objs The object manager.
	 * @param __type The array type.
	 * @param __length The array length.
	 * @throws JVMClassCastException If the type is not an array.
	 * @throws JVMNegativeArraySizeException If the length is negative.
	 * @since 2016/04/06
	 */
	JVMArray(JVMObjects __objs, JVMClass __type, int __length)
		throws JVMClassCastException, JVMNegativeArraySizeException
	{
		super(__objs, __type);
		
		// {@squirreljme.error IN0d Attempting to create a new instance of an
		// object which is not an array. (The type; The length of the array)}
		if (__type.isArray())
			throw new JVMClassCastException(String.format("IN0d %s %d", __type,
				__length));
		
		// {@squirreljme.error IN0e Attempting to create an array with a
		// negative length. (The negative length)}
		if (__length < 0)
			throw new JVMNegativeArraySizeException(String.format("IN0e %s",
				__length));
		
		// Set
		arraylength = __length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/06
	 */
	@Override
	public boolean isArray()
	{
		return true;
	}
	
	/**
	 * Sets the element of the array.
	 *
	 * @param __i Index to set.
	 * @param __v The value to set.
	 * @return The old value at the given index.
	 * @throws JVMArrayIndexOutOfBoundsException if an attempt is made to
	 * write to an index which exceeds the array bounds.
	 * @since 2016/04/05
	 */
	public Object setArrayElement(int __i, Object __v)
		throws JVMArrayIndexOutOfBoundsException
	{
		// {@squirreljme.error IN0f An attempt was made to set the element of
		// and array which is not within bounds of the array. (The index;
		// The size of the array)}
		if (__i < 0 || __i >= arraylength)
			throw new JVMArrayIndexOutOfBoundsException(String.format(
				"IN0f", __i, arraylength));
		
		throw new Error("TODO");
	}
}

