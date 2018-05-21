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

/**
 * This represents a constant value.
 *
 * @since 2018/05/16
 */
public abstract class ConstantValue
{
	/** The represented object. */
	protected final Object value;
	
	/** The value type. */
	protected final ConstantValueType type;
	
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value.
	 * @param __t The value type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public ConstantValue(Object __v, ConstantValueType __t)
		throws NullPointerException
	{
		if (__v == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.value = __v;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/16
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ConstantValue))
			return false;
		
		ConstantValue o = (ConstantValue)__o;
		return this.value.equals(o.value) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/16
	 */
	@Override
	public final int hashCode()
	{
		return this.value.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/16
	 */
	@Override
	public final String toString()
	{
		return this.value.toString();
	}
	
	/**
	 * The type of value which is contained here.
	 *
	 * @return The constant value type.
	 * @since 2018/05/16
	 */
	public final ConstantValueType type()
	{
		throw new todo.TODO();
	}
}

