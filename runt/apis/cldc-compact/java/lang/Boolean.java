// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;

public final class Boolean
	implements Comparable<Boolean>
{
	/** The false value. */
	public static final Boolean FALSE =
		new Boolean(false);
	
	/** The true value. */
	public static final Boolean TRUE =
		new Boolean(true);
	
	/** The class representing the primitive type. */
	public static final Class<Boolean> TYPE =
		ObjectAccess.<Boolean>classByNameType("boolean");
	
	/** The value of this boolean. */
	private final boolean _value;
	
	/**
	 * Initializes the boolean with the given value.
	 *
	 * @param __v The value to use.
	 * @since 2018/09/23
	 */
	public Boolean(boolean __v)
	{
		this._value = __v;
	}
	
	public Boolean(String __a)
	{
		super();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the value of this boolean.
	 *
	 * @return The boolean value.
	 * @since 2018/09/23
	 */
	public boolean booleanValue()
	{
		return this._value;
	}
	
	public int compareTo(Boolean __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public String toString()
	{
		return Boolean.toString(this._value);
	}
	
	public static boolean getBoolean(String __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean parseBoolean(String __a)
	{
		return (__a != null && __a.equalsIgnoreCase("true"));
	}
	
	/**
	 * Returns a string representing the given value.
	 *
	 * @param __a The boolean to represent.
	 * @return The string representation of the boolean.
	 * @since 2018/09/23
	 */
	public static String toString(boolean __a)
	{
		if (__a)
			return "true";
		return "false";
	}
	
	/**
	 * Boxes the given boolean value.
	 *
	 * @param __a The boolean to box.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2016/03/21
	 */
	public static Boolean valueOf(boolean __a)
	{
		if (__a)
			return TRUE;
		return FALSE;
	}
	
	public static Boolean valueOf(String __a)
	{
		if (__a != null && __a.equalsIgnoreCase("true"))
			return TRUE;
		return FALSE;
	}
}

