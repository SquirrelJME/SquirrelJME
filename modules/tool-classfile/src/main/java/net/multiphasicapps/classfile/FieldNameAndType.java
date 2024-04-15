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
 * This represents the name and type for a field.
 *
 * @since 2017/10/12
 */
public final class FieldNameAndType
	implements Comparable<FieldNameAndType>, MemberNameAndType
{
	/** The field name. */
	protected final FieldName name;
	
	/** The field type. */
	protected final FieldDescriptor type;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the field name and type.
	 *
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/12
	 */
	public FieldNameAndType(FieldName __n, FieldDescriptor __t)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.type = __t;
	}
	
	/**
	 * Initializes the field name and type.
	 *
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/12
	 */
	public FieldNameAndType(String __n, String __t)
		throws NullPointerException
	{
		this(new FieldName(__n), new FieldDescriptor(__t));
	}
	
	/**
	 * Initializes the field name and type.
	 *
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/12
	 */
	public FieldNameAndType(String __n, FieldDescriptor __t)
		throws NullPointerException
	{
		this(new FieldName(__n), __t);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
	 */
	@Override
	public int compareTo(FieldNameAndType __o)
	{
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		return this.type.compareTo(__o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof FieldNameAndType))
			return false;
		
		FieldNameAndType o = (FieldNameAndType)__o;
		return this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
	 */
	@Override
	public FieldName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/12
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
	 * @since 2017/10/12
	 */
	@Override
	public FieldDescriptor type()
	{
		return this.type;
	}
}


