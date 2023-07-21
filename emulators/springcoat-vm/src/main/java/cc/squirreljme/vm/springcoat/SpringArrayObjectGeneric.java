// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;

/**
 * Generic array that can store any type.
 *
 * @since 2018/11/04
 */
public final class SpringArrayObjectGeneric
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final SpringObject[] _elements;
	
	/** The last class which was checked for compatibility. */
	private SpringClass _lastValid;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __l The array length.
	 * @throws IllegalArgumentException If the given type is primitive.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public SpringArrayObjectGeneric(SpringClass __self, int __l)
		throws IllegalArgumentException, NullPointerException,
			SpringNegativeArraySizeException
	{
		super(__self, __l);
		
		// Previously this was permitted, however since there are other more
		// optimal forms for arrays this is no longer needed to have a generic
		// array to store these values
		if (__self.componentType().name().primitiveType() != null)
			throw new IllegalArgumentException("Cannot have a generic " +
				"array of primitive types: " + __self);
		
		// Initialize elements
		SpringObject[] elements;
		this._elements = (elements = new SpringObject[__l]);
		
		// Determine the initial value to use
		SpringObject v = SpringNullObject.NULL;
		
		// Set all elements to an initial value depending on the type
		for (int i = 0; i < __l; i++)
			elements[i] = v;
	}
	
	/**
	 * Wraps the given array as a generic array.
	 *
	 * @param __self The self type.
	 * @param __elements The array elements.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/13
	 */
	public SpringArrayObjectGeneric(SpringClass __self,
		SpringObject[] __elements)
		throws NullPointerException
	{
		super(__self, __elements.length);
		
		this._elements = __elements;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public final SpringObject[] array()
	{
		return this._elements;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		// Faster to just have the host VM do bounds check
		try
		{
			SpringObject rv = this._elements[__dx];
			
			// Always have the special null be used here
			if (rv == null)
				rv = SpringNullObject.NULL;
			
			return __cl.cast(rv);
		}
		
		/* {@squirreljme.error BK0h Out of bounds access to array.
		(The index; The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0h %d %d", __dx, this.length), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/13
	 */
	@Override
	public final void set(int __dx, Object __v)
		throws ClassCastException, SpringArrayStoreException,
			SpringArrayIndexOutOfBoundsException
	{
		this.set(__dx, (SpringObject)__v);
	}
	
	/**
	 * Sets the value in the array.
	 *
	 * @param __dx The index.
	 * @param __v The value to set.
	 * @throws SpringArrayStoreException If the type is not valid.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is out of
	 * bounds.
	 * @since 2018/11/14
	 */
	public final void set(int __dx, SpringObject __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		try
		{
			// This is a cached type for setting because an array of one type
			// will usually in most cases set with objects which are compatible
			// so the rather involved instanceof check will take awhile and
			// compound for setting single elements.
			SpringClass lastValid = this._lastValid;
			SpringClass wouldSet = (__v == null ? null : __v.type());
			
			// Ghost objects may only be placed in their own array
			/*if (__v instanceof AbstractGhostObject)
			{
				SpringClass component = this.component;
				if (!component.name.toRuntimeString()
					.equals(((AbstractGhostObject)__v).represents.getName()))
					throw new SpringArrayStoreException(String.format(
						"Array assign of wrong ghost: %s %s", __v, component));
			}*/
			
			// Performing the check for cache?
			/*else*/ if (wouldSet != lastValid)
			{
				/* {@squirreljme.error BK0i The specified type is not
				compatible with the values this array stores. (The input
				value; The component type)} */
				SpringClass component = this.component;
				if (!component.isCompatible(__v))
					throw new SpringArrayStoreException(String.format(
						"BK0i %s %s", __v, component));
				
				// Next validity check would be set if done on an object
				// Ignore setting it back to null, if one was previously
				// valid
				if (wouldSet != null)
					this._lastValid = wouldSet;
			}
			
			// Set
			this._elements[__dx] = (__v == null ? SpringNullObject.NULL : __v);
		}
		
		/* {@squirreljme.error BK0j Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0j %d %d", __dx, this.length), e);
		}
	}
}

