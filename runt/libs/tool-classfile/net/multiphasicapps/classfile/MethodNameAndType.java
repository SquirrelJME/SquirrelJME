// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the name and type for a method.
 *
 * @since 2017/10/10
 */
public final class MethodNameAndType
	implements Comparable<MethodNameAndType>
{
	/** The method name. */
	protected final MethodName name;
	
	/** The method type. */
	protected final MethodDescriptor type;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the method anme and type.
	 *
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @throws InvalidClassFormatException If the method name and type are not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public MethodNameAndType(String __n, String __t)
		throws InvalidClassFormatException, NullPointerException
	{
		this(new MethodName(__n), new MethodDescriptor(__t));
	}
	
	/**
	 * Initializes the method name and type.
	 *
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/10
	 */
	public MethodNameAndType(MethodName __n, MethodDescriptor __t)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public int compareTo(MethodNameAndType __o)
	{
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		return this.type.toString().compareTo(__o.type.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof MethodNameAndType))
			return false;
		
		MethodNameAndType o = (MethodNameAndType)__o;
		return this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @return The method name.
	 * @since 2017/10/10
	 */
	public MethodName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s:%s",
				this.name, this.type)));
		
		return rv;
	}
	
	/**
	 * Returns the type of the method.
	 *
	 * @return The method type.
	 * @since 2017/10/10
	 */
	public MethodDescriptor type()
	{
		return this.type;
	}
}

