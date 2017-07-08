// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

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
		throw new todo.TODO();
	}
}

