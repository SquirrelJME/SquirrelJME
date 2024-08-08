// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is an object which acts as an array, which stores some kind of data.
 *
 * @since 2018/09/15
 */
public abstract class SpringArrayObject
	implements SpringArray, SpringObject
{
	/** The monitor for this array. */
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/** The reference link holder. */
	protected final RefLinkHolder refLink =
		new RefLinkHolder();
	
	/** The type of this object itself. */
	protected final SpringClass selftype;
	
	/** The component type. */
	protected final SpringClass component;
	
	/** The length of the array. */
	protected final int length;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __l The array length.
	 * @throws IllegalArgumentException If the type is not an array.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public SpringArrayObject(SpringClass __self, int __l)
		throws IllegalArgumentException, NullPointerException,
			SpringNegativeArraySizeException
	{
		if (__self == null)
			throw new NullPointerException("NARG");
		
		// The passed type must always be an array
		if (!__self.isArray())
			throw new IllegalArgumentException("Type not an array: " + __self);
		
		/* {@squirreljme.error BK01 Attempt to allocate an array of a
		negative size. (The length requested)} */
		if (__l < 0)
			throw new SpringNegativeArraySizeException(
				String.format("BK01 %d", __l));
		
		this.selftype = __self;
		this.component = __self.componentType();
		this.length = __l;
	}
	
	/**
	 * Returns the raw backing array.
	 *
	 * @return The array.
	 * @since 2018/11/19
	 */
	@Override
	public abstract Object array();
	
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
	@Override
	public abstract <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isArray()
	{
		return true;
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
	@Override
	public abstract void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException;
	
	/**
	 * Returns the length of this array.
	 *
	 * @return The array length.
	 * @since 2018/09/16
	 */
	@Override
	public final int length()
	{
		return this.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/31
	 */
	@Override
	public final RefLinkHolder refLink()
	{
		return this.refLink;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s@%08x", this.selftype.name(),
				System.identityHashCode(this))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringClass type()
	{
		return this.selftype;
	}
}

