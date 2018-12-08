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

/**
 * This is a boxed boolean value.
 *
 * @since 2018/12/07
 */
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
	
	/**
	 * Initializes the boolean from the given string.
	 *
	 * @param __s String boolean representation.
	 * @since 2018/12/07
	 */
	public Boolean(String __s)
	{
		this(Boolean.valueOf(__s)._value);
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
	
	/**
	 * Compares this boolean to another.
	 *
	 * True comes before false.
	 *
	 * @param __o The other value.
	 * @return The comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public int compareTo(Boolean __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		boolean a = this._value,
			b = __o._value;
		
		if (a == b)
			return 0;
		else if (a)
			return 1;
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Boolean))
			return false;
		
		return this._value == ((Boolean)__o)._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int hashCode()
	{
		return (this._value ? 1231 : 1237);
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
	
	/**
	 * Gets boolean value from the system property.
	 *
	 * @param __p The property to get.
	 * @return The value of the boolean.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If it is not permitted to get the property.
	 * @since 2018/12/07
	 */
	public static boolean getBoolean(String __p)
		throws NullPointerException, SecurityException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return Boolean.parseBoolean(System.getProperty(__p));
	}
	
	/**
	 * Parses the given boolean value without regards to case.
	 *
	 * @param __v The value to parse.
	 * @return The boolean of the parse.
	 * @since 2018/12/07
	 */
	public static boolean parseBoolean(String __v)
	{
		return (__v != null && __v.equalsIgnoreCase("true"));
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
	
	/**
	 * Returns the value of the given boolean.
	 *
	 * @param __v The value to parse.
	 * @return The boolean value.
	 * @since 2018/12/07
	 */
	public static Boolean valueOf(String __v)
	{
		if (__v != null && __v.equalsIgnoreCase("true"))
			return TRUE;
		return FALSE;
	}
}

