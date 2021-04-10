// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPArray;
import cc.squirreljme.jdwp.JDWPClass;
import cc.squirreljme.jdwp.JDWPValue;
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
	implements JDWPArray, SpringObject
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
		
		// {@squirreljme.error BK01 Attempt to allocate an array of a
		// negative size. (The length requested)}
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
	public abstract <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException;
	
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
	public abstract void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException;
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/19
	 */
	@Override
	public int debuggerArrayLength()
	{
		return this.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/20
	 */
	@Override
	public boolean debuggerArrayGet(int __i, JDWPValue __value)
	{
		if (__i < 0 || __i >= this.length)
			return false;
		
		__value.set(this.get(Object.class, __i));
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/20
	 */
	@Override
	public String debuggerComponentDescriptor()
	{
		return this.type().componentType().debuggerFieldDescriptor();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final int debuggerId()
	{
		return System.identityHashCode(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final JDWPClass debuggerClass()
	{
		return this.type();
	}
	
	/**
	 * Returns the length of this array.
	 *
	 * @return The array length.
	 * @since 2018/09/16
	 */
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

