// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This represents a boxed boolean value.
 *
 * @since 2019/05/25
 */
public final class Boolean
{
	/** The boolean value. */
	private final boolean _value;
	
	/**
	 * Initializes the boolean value.
	 *
	 * @param __v The value to use.
	 * @since 2019/09/22
	 */
	public Boolean(boolean __v)
	{
		this._value = __v;
	}
	
	/**
	 * Returns the value of the boolean.
	 *
	 * @return The boolean value.
	 * @since 2019/09/22.
	 */
	public final boolean booleanValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof Boolean))
			return false;
		
		return this._value == ((Boolean)__o)._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final int hashCode()
	{
		return (this._value ? 1 : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public final String toString()
	{
		return (this._value ? "true" : "false");
	}
	
	/**
	 * Boxes the given boolean.
	 *
	 * @param __v The boolean to box.
	 * @return The boxed boolean value.
	 * @since 2019/09/22
	 */
	public static final Boolean valueOf(boolean __v)
	{
		return new Boolean(__v);
	}
}

