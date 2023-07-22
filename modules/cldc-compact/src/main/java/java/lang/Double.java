// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.SoftDouble;
import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class Double
	extends Number
	implements Comparable<Double>
{
	@Api
	public static final int MAX_EXPONENT =
		1023;
	
	@Api
	public static final double MAX_VALUE =
		+0x1.FFFFFFFFFFFFFp1023D;
	
	@Api
	public static final int MIN_EXPONENT =
		-1022;
	
	@Api
	public static final double MIN_NORMAL =
		+0x1.0p-1022D;
	
	@Api
	public static final double MIN_VALUE =
		+0x0.0000000000001p-1022D;
	
	@Api
	public static final double NEGATIVE_INFINITY =
		Double.longBitsToDouble(-4503599627370496L);
	
	@Api
	public static final double NaN =
		Double.longBitsToDouble(9221120237041090560L);
	
	@Api
	public static final double POSITIVE_INFINITY =
		Double.longBitsToDouble(9218868437227405312L);
	
	/** The number of bits double requires for storage. */
	@Api
	public static final int SIZE =
		64;
	
	/** The class representing the primitive type. */
	@Api
	public static final Class<Double> TYPE =
		TypeShelf.<Double>typeToClass(TypeShelf.typeOfDouble());
	
	/** The value for this double. */
	private final double _value;
	
	/**
	 * Initializes the double value.
	 * 
	 * @param __v The value to set.
	 * @since 2022/01/06
	 */
	@Api
	public Double(double __v)
	{
		this._value = __v;
	}
	
	@Api
	public Double(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public byte byteValue()
	{
		return (byte)this._value;
	}
	
	@Override
	public int compareTo(Double __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public double doubleValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Double))
			return false;
		
		double a = this._value;
		double b = ((Double)__o)._value;
		
		// Both values are NaN, consider it equal
		if (Double.isNaN(a) && Double.isNaN(b))
			return true;
		
		// If both values are zero, the sign is not important
		long ra = Double.doubleToRawLongBits(a);
		long rb = Double.doubleToRawLongBits(b);
		if ((ra & SoftDouble.ZERO_CHECK_MASK) == 0 &&
			(rb & SoftDouble.ZERO_CHECK_MASK) == 0)
			return ra == rb;
		
		// Otherwise standard comparison
		return a == b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public float floatValue()
	{
		return (float)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public int hashCode()
	{
		long v = Double.doubleToLongBits(this._value); 
		return (int)(v ^ (v >>> 32));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public int intValue()
	{
		return (int)this._value;
	}
	
	@Api
	public boolean isInfinite()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is this the NaN value.
	 *
	 * @return If this is the NaN value.
	 * @since 2022/01/06
	 */
	@Api
	public boolean isNaN()
	{
		return Double.isNaN(this._value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public long longValue()
	{
		return (long)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public short shortValue()
	{
		return (short)this._value;
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int compare(double __a, double __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the bits which represent the double value with all NaN values
	 * collapsed into a single form.
	 *
	 * @param __v The input value.
	 * @return The bits for the value.
	 * @since 2018/11/04
	 */
	@Api
	public static long doubleToLongBits(double __v)
	{
		long raw = Double.doubleToRawLongBits(__v);
		
		// Collapse all NaN values to a single form
		if ((raw & SoftDouble.NAN_MASK) == (SoftDouble.NAN_MASK))
			return SoftDouble.NAN_MASK;
		
		return raw;
	}
	
	/**
	 * Returns the raw bits which represent the double value.
	 *
	 * @param __v The input value.
	 * @return The raw bits for the value.
	 * @since 2018/11/03
	 */
	@Api
	public static long doubleToRawLongBits(double __v)
	{
		return MathShelf.rawDoubleToLong(__v);
	}
	
	@Api
	public static boolean isInfinite(double __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is the specified value a NaN?
	 *
	 * @param __v The value to check.
	 * @return If it is NaN or not.
	 * @since 2022/01/06
	 */
	@Api
	public static boolean isNaN(double __v)
	{
		return SoftDouble.isNaN(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Converts the given long bits to a double.
	 *
	 * Note that this value might be modified depending on the platform if
	 * the platform based conversion cannot handle signaling NaNs or illegal
	 * floating point values.
	 *
	 * @param __b The bits to convert.
	 * @return The resulting double.
	 * @since 2018/11/03
	 */
	@Api
	public static double longBitsToDouble(long __b)
	{
		return MathShelf.rawLongToDouble(__b);
	}
	
	@Api
	public static double parseDouble(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static String toString(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Double valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the boxed representation of the given double.
	 *
	 * @param __v The double value.
	 * @return The boxed double.
	 * @since 2022/01/06
	 */
	@SuppressWarnings("UnnecessaryBoxing")
	@Api
	public static Double valueOf(double __v)
	{
		return new Double(__v);
	}
}

