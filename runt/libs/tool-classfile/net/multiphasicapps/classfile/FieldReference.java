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
 * This describes a reference to a field.
 *
 * @since 2017/06/12
 */
public final class FieldReference
	extends MemberReference
{
	/** The name of the field. */
	protected final FieldName name;
	
	/** The member type. */
	protected final FieldDescriptor type;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the field reference.
	 *
	 * @param __c The class the member resides in.
	 * @param __i The name of the member.
	 * @param __t The descriptor of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public FieldReference(ClassName __c, FieldName __i, FieldDescriptor __t)
		throws NullPointerException
	{
		super(__c);
		
		// Check
		if (__t == null || __i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __i;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/08
	 */
	@Override
	public final FieldName memberName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"field %s::%s%s", this.classname, this.name,
				this.type)));
		
		return rv;
	}
}

