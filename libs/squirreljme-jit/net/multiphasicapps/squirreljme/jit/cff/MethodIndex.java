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
 * This is an index which specifies the name of a class, the name of the
 * method, and the type of the method. It is intended to be used mostly as an
 * index to identify a method uniquely.
 *
 * @since 2017/10/14
 */
public final class MethodIndex
	implements Comparable<MethodIndex>
{
	/** The name of the class. */
	protected final ClassName classname;
	
	/** The method name. */
	protected final MethodName methodname;
	
	/** The method type. */
	protected final MethodDescriptor methodtype;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the method index.
	 *
	 * @param __c The class the method is within.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/14
	 */
	public MethodIndex(ClassName __c, MethodName __n, MethodDescriptor __t)
		throws NullPointerException
	{
		if (__c == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.classname = __c;
		this.methodname = __n;
		this.methodtype = __t;
	}
	
	/**
	 * Returns the class the method is within.
	 *
	 * @return The class name.
	 * @since 2017/10/14
	 */
	public ClassName className()
	{
		return this.classname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/14
	 */
	@Override
	public int compareTo(MethodIndex __o)
	{
		int rv;
		if ((rv = this.classname.compareTo(__o.classname)) != 0)
			return rv;
		if ((rv = this.methodname.compareTo(__o.methodname)) != 0)
			return rv;
		return this.methodtype.toString().compareTo(__o.methodtype.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof MethodIndex))
			return false;
		
		MethodIndex o = (MethodIndex)__o;
		return this.classname.equals(o.classname) &&
			this.methodname.equals(o.methodname) &&
			this.methodtype.equals(o.methodtype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/14
	 */
	@Override
	public int hashCode()
	{
		return this.classname.hashCode() ^ this.methodname.hashCode() ^
			this.methodtype.hashCode();
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @return The method name.
	 * @since 2017/10/14
	 */
	public MethodName methodName()
	{
		return this.methodname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/14
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s::%s:%s",
				this.classname, this.methodname, this.methodtype)));
		
		return rv;
	}
	
	/**
	 * Returns the type of the method.
	 *
	 * @return The method type.
	 * @since 2017/10/14
	 */
	public MethodDescriptor methodType()
	{
		return this.methodtype;
	}
}

