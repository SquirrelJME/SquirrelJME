// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	implements Comparable<MethodNameAndType>, MemberNameAndType
{
	/** The method name. */
	protected final MethodName name;
	
	/** The method type. */
	protected final MethodDescriptor type;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the method name and type.
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
	 * Initializes the method anme and type.
	 *
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @throws InvalidClassFormatException If the method name and type are not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	public MethodNameAndType(String __n, MethodDescriptor __t)
		throws InvalidClassFormatException, NullPointerException
	{
		this(new MethodName(__n), __t);
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
		return this.type.compareTo(__o.type);
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
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public MethodDescriptor type()
	{
		return this.type;
	}
	
	/**
	 * Creates a name and type from the given input strings.
	 * 
	 * @param __name The method name.
	 * @param __rv The return value of the method, may be {@code null}.
	 * @param __args The arguments of the method.
	 * @return The descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/15
	 */
	public static MethodNameAndType ofArguments(String __name, String __rv,
		String... __args)
		throws NullPointerException
	{
		if (__name == null || __args == null)
			throw new NullPointerException("NARG");
		
		return new MethodNameAndType(__name,
			MethodDescriptor.ofArguments(__rv, __args));
	}
}

