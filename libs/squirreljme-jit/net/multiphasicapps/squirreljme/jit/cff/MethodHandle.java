// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a method's class, name, and type.
 *
 * @since 2017/09/16
 */
public final class MethodHandle
	implements Comparable<MethodHandle>
{
	/** The class the method is in. */
	protected final ClassName outerclass;
	
	/** The name of the method. */
	protected final MethodName name;
	
	/** The descriptor of the method. */
	protected final MethodDescriptor descriptor;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the method handle.
	 *
	 * @param __cl The class the method is in.
	 * @param __n The name of the method.
	 * @param __d The descriptor of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/16
	 */
	public MethodHandle(ClassName __cl, MethodName __n, MethodDescriptor __d)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __n == null || __d == null)
			throw new NullPointerException("NARG");
		
		this.outerclass = __cl;
		this.name = __n;
		this.descriptor = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/14
	 */
	@Override
	public int compareTo(MethodHandle __o)
	{
		int rv;
		if ((rv = this.outerclass.compareTo(__o.outerclass)) != 0)
			return rv;
		if ((rv = this.name.compareTo(__o.name)) != 0)
			return rv;
		return this.descriptor.toString().compareTo(__o.descriptor.toString());
	}
	
	/**
	 * Returns the descriptor of the method.
	 *
	 * @return The method descriptor.
	 * @since 2017/09/16
	 */
	public MethodDescriptor descriptor()
	{
		return this.descriptor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MethodHandle))
			return false;
		
		MethodHandle o = (MethodHandle)__o;
		return this.outerclass.equals(o.outerclass) &&
			this.name.equals(o.name) &&
			this.descriptor.equals(o.descriptor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public int hashCode()
	{
		return this.outerclass.hashCode() ^
			this.name.hashCode() ^
			this.descriptor.hashCode();
	}
	
	/**
	 * Returns {@code true} if this represents the instance initializer.
	 *
	 * @return {@code true} if this is the instance initializer.
	 * @since 2017/09/18
	 */
	public boolean isInstanceInitializer()
	{
		return this.name.isInstanceInitializer();
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @return The method name.
	 * @since 2017/09/16
	 */
	public MethodName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the class this is contained within.
	 *
	 * @return The outer class.
	 * @since 2017/09/16
	 */
	public ClassName outerClass()
	{
		return this.outerclass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s::%s%s",
				this.outerclass, this.name, this.descriptor)));
		
		return rv;
	}
}

