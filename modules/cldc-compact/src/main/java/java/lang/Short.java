// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

@Api
public final class Short
	extends Number
	implements Comparable<Short>
{
	@Api
	public static final short MAX_VALUE =
		32767;
	
	@Api
	public static final short MIN_VALUE =
		-32768;
	
	@Api
	public static final int SIZE =
		16;
	
	/** The class representing the primitive type. */
	@Api
	public static final Class<Short> TYPE =
		TypeShelf.<Short>typeToClass(TypeShelf.typeOfShort());
	
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
	@Api
	public Short(short __v)
	{
		this._value = __v;
	}
	
	@Api
	public Short(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/03
	 */
	@Override
	public byte byteValue()
	{
		return (byte)this._value;
	}
	
	@Override
	public int compareTo(Short __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public double doubleValue()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(this instanceof Short))
			return false;
		
		return this._value == ((Short)__o)._value;
	}
	
	@Override
	public float floatValue()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/03
	 */
	@Override
	public int hashCode()
	{
		return this.intValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	public int intValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	public long longValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
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
				(rv = Short.toString(this._value)));
		
		return rv;
	}
	
	@Api
	public static Short decode(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static short parseShort(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static short parseShort(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * Reverses the given bytes.
	 * 
	 * @param __i The value to reverse.
	 * @return The reversed value.
	 * @since 2021/02/18
	 */
	@Api
	public static short reverseBytes(short __i)
	{
		// 0xBBAA -> 0xAABB
		return (short)((__i >>> 8) | (__i << 8));
	}
	
	/**
	 * Returns the string representation of the short value.
	 *
	 * @param __v The value to represent.
	 * @return The string representation of it.
	 * @since 2018/11/14
	 */
	@Api
	public static String toString(short __v)
	{
		return Integer.toString(__v, 10);
	}
	
	@Api
	public static Short valueOf(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static Short valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * Boxes the given short value.
	 *
	 * @param __v The value to box.
	 * @return The boxed value.
	 * @since 2018/11/14
	 */
	@Api
	public static Short valueOf(short __v)
	{
		return new Short(__v);
	}
}

