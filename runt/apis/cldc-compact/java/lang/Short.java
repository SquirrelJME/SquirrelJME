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

public final class Short
	extends Number
	implements Comparable<Short>
{
	public static final short MAX_VALUE =
		32767;
	
	public static final short MIN_VALUE =
		-32768;
	
	public static final int SIZE =
		16;
	
	/** The class representing the primitive type. */
	public static final Class<Short> TYPE =
		ObjectAccess.<Short>classByNameType("short");
	
	/** The value of this integer. */
	private final short _value;
	
	/** The string representation of this value. */
	private Reference<String> _string;
	
	/**
	 * Wraps the boxed value.
	 *
	 * @param __v The value to wrap.
	 * @since 2018/11/14
	 */
	public Short(short __v)
	{
		this._value = __v;
	}
	
	public Short(String __a)
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
	
	public int compareTo(Short __a)
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
				(rv = Short.toString(this._value)));
		
		return rv;
	}
	
	public static Short decode(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static short parseShort(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static short parseShort(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static short reverseBytes(short __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the string representation of the short value.
	 *
	 * @param __v The value to represent.
	 * @return The string representation of it.
	 * @since 2018/11/14
	 */
	public static String toString(short __v)
	{
		return Integer.toString(__v, 10);
	}
	
	public static Short valueOf(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Short valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	/**
	 * Boxes the given short value.
	 *
	 * @param __v The value to box.
	 * @return The boxed value.
	 * @since 2018/11/14
	 */
	public static Short valueOf(short __v)
	{
		return new Short(__v);
	}
	
	/**
	 * The {@link #TYPE} field is magically initialized by the virtual machine.
	 *
	 * @return {@link #TYPE}.
	 * @since 2016/06/16
	 */
	private static Class<Short> __getType()
	{
		return TYPE;
	}
}

