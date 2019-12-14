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
 * This represents a boxed byte.
 *
 * @since 2019/05/25
 */
public final class Byte
	extends Number
{
	/** The value of this byte. */
	private final byte _value;
	
	/**
	 * Initializes the byte.
	 *
	 * @param __v The value.
	 * @since 2019/12/14
	 */
	public Byte(byte __v)
	{
		this._value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/14
	 */
	@Override
	public String toString()
	{
		return Integer.toString(this._value, 10);
	}
	
	/**
	 * Wraps the given byte value.
	 *
	 * @param __v The value to wrap.
	 * @return The wrapped byte.
	 * @since 2019/12/14
	 */
	public static final Byte valueOf(byte __v)
	{
		return new Byte(__v);
	}
}

