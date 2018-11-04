// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import net.multiphasicapps.classfile.PrimitiveType;

/**
 * Generic array that can store any type.
 *
 * @since 2018/11/04
 */
public final class SpringArrayObjectGeneric
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final Object[] _elements;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __cl The component type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public SpringArrayObjectGeneric(SpringClass __self, SpringClass __cl,
		int __l)
		throws NullPointerException
	{
		super(__self, __cl, __l);
		
		// Initialize elements
		Object[] elements;
		this._elements = (elements = new Object[__l]);
		
		// Determine the initial value to use
		PrimitiveType type = __cl.name().primitiveType();
		Object v;
		if (type == null)
			v = SpringNullObject.NULL;
		else
			switch (type)
			{
				case BOOLEAN:
				case BYTE:
				case SHORT:
				case CHARACTER:
				case INTEGER:
					v = Integer.valueOf(0);
					break;
				
				case LONG:
					v = Long.valueOf(0);
					break;
				
				case FLOAT:
					v = Float.valueOf(0);
					break;
				
				case DOUBLE:
					v = Double.valueOf(0);
					break;
				
				default:
					throw new RuntimeException("OOPS");
			}
		
		// Set all elements to an initial value depending on the type
		// Set all 
		for (int i = 0; i < __l; i++)
			elements[i] = v;
	}
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param <C> The type of value to get.
	 * @param __cl The type of value to get.
	 * @param __dx The index to set.
	 * @return The contained value.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/09/16
	 */
	public final <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		// Faster to just have the host VM do bounds check
		try
		{
			return __cl.cast(this._elements[__dx]);
		}
		
		// {@squirreljme.error BK1j Out of bounds access to array.
		// (The index; The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK1j %d %d", __dx, this.length), e);
		}
	}
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param __dx The index to set.
	 * @param __v The value to set.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/09/16
	 */
	public final void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		try
		{
			// {@squirreljme.error BK1i The specified type is not compatible
			// with the values this array stores. (The input value;
			// The component type)}
			SpringClass component = this.component;
			if (!component.isCompatible(__v))
				throw new SpringArrayStoreException(String.format(
					"BK1i %s %s", __v, component));
			
			// Set
			this._elements[__dx] = __v;
		}
		
		// {@squirreljme.error BK1h Out of bounds access to array. (The index;
		// The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK1h %d %d", __dx, this.length), e);
		}
	}
}

