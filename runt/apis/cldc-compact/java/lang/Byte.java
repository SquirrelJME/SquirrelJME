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

public final class Byte
	extends Number
	implements Comparable<Byte>
{
	public static final byte MAX_VALUE =
		127;
	
	public static final byte MIN_VALUE =
		-128;
	
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
	
	public Byte(String __a)
		throws NumberFormatException
	{
		super();
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	@Override
	public byte byteValue()
	{
		throw new todo.TODO();
	}
	
	public int compareTo(Byte __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public double doubleValue()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public float floatValue()
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int intValue()
	{
		throw new todo.TODO();
	}
	
	@Override
	public long longValue()
	{
		throw new todo.TODO();
	}
	
	@Override
	public short shortValue()
	{
		throw new todo.TODO();
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
	
	public static int compare(byte __a, byte __b)
	{
		throw new todo.TODO();
	}
	
	public static Byte decode(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static byte parseByte(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static byte parseByte(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
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
	
	public static Byte valueOf(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Byte valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
}

