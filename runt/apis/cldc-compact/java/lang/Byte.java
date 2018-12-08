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

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a boxed byte value.
 *
 * @since 2018/12/07
 */
public final class Byte
	extends Number
	implements Comparable<Byte>
{
	/** The maximum value. */
	public static final byte MAX_VALUE =
		127;
	
	/** The minimum value. */
	public static final byte MIN_VALUE =
		-128;
	
	/** The number of bits in a byte. */
	public static final int SIZE =
		8;
	
	/** The class representing the primitive type. */
	public static final Class<Byte> TYPE =
		ObjectAccess.<Byte>classByNameType("byte");
	
	/** The value of the byte. */
	private final byte _value;
	
	/** The string representation of this value. */
	private Reference<String> _string;
	
	/**
	 * Initializes the boxed value.
	 *
	 * @param __v The value to store.
	 * @since 2018/11/14
	 */
	public Byte(byte __v)
	{
		this._value = __v;
	}
	
	/**
	 * Initializes the byte value from the string.
	 *
	 * @param __s The string to parse.
	 * @throws NumberFormatException If the string is not a valid number.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public Byte(String __s)
		throws NumberFormatException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this._value = Byte.parseByte(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public byte byteValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int compareTo(Byte __o)
	{
		return Byte.compare(this._value, __o._value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public double doubleValue()
	{
		return this._value;
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
		
		if (!(__o instanceof Byte))
			return false;
		
		return this._value == ((Byte)__o)._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public float floatValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int hashCode()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int intValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public long longValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public short shortValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = Byte.toString(this._value)));
		
		return rv;
	}
	
	/**
	 * Decodes a byte value from the string in the same form as
	 * {@link Integer#decode(String)}.
	 *
	 * @param __s The string to decode.
	 * @return The byte value.
	 * @throws NumberFormatException If the value is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public static Byte decode(String __s)
		throws NumberFormatException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3e Byte value out of bounds.}
		int val = Integer.decode(__s)
		if (val < MIN_VALUE || val > MAX_VALUE)
			throw new NumberFormatException("ZZ3e");
		
		return Byte.valueOf((byte)val);
	}
	
	/**
	 * Parses the given byte using the given radix.
	 *
	 * @param __s The string to parse.
	 * @param __r The radix of the value.
	 * @return The parsed value.
	 * @throws NumberFormatException If the byte is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public static byte parseByte(String __s, int __r)
		throws NumberFormatException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3f Byte value out of range.}
		int val = Integer.parseInt(__s, __r);
		if (val < MIN_VALUE || val > MAX_VALUE)
			throw new NumberFormatException("ZZ3f");
		
		return (byte)val;
	}
	
	/**
	 * Parses the given string to a byte.
	 *
	 * @param __s The string to parse.
	 * @return The parsed value.
	 * @throws NumberFormatException If the byte is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public static byte parseByte(String __s)
		throws NumberFormatException, NullPointerException
	{
		return Byte.parseByte(__s, 10);
	}
	
	/**
	 * Returns the string representation of the given byte value.
	 *
	 * @param __v The value to represent.
	 * @return The string value.
	 * @since 2018/11/14
	 */
	public static String toString(byte __v)
	{
		return Integer.toString(__v, 10);
	}
	
	/**
	 * Boxes the specified byte value.
	 *
	 * @param __v The value to box.
	 * @return The boxed value.
	 * @since 2018/11/14
	 */
	public static Byte valueOf(byte __v)
	{
		return new Byte(__v);
	}
	
	/**
	 * Parses the string to the given byte value.
	 *
	 * @param __s The string to parse.
	 * @param __r The radix.
	 * @return The decoded value.
	 * @throws NumberFormatException If the byte is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public static Byte valueOf(String __s, int __r)
		throws NumberFormatException, NullPointerException
	{
		return new Byte(Byte.parseByte(__s, __r));
	}
	
	/**
	 * Parses the string to the given byte value.
	 *
	 * @param __s The string to parse.
	 * @return The decoded value.
	 * @throws NumberFormatException If the byte is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/07
	 */
	public static Byte valueOf(String __s)
		throws NumberFormatException, NullPointerException
	{
		return new Byte(Byte.parseByte(__s, 10));
	}
}

