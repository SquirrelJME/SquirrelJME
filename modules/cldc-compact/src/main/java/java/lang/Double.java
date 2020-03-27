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

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.JVMFunction;

public final class Double
	extends Number
	implements Comparable<Double>
{
	public static final int MAX_EXPONENT =
		1023;
	
	public static final double MAX_VALUE =
		+0x1.FFFFFFFFFFFFFp1023D;
	
	public static final int MIN_EXPONENT =
		-1022;
	
	public static final double MIN_NORMAL =
		+0x1.0p-1022D;
	
	public static final double MIN_VALUE =
		+0x0.0000000000001p-1022D;
	
	public static final double NEGATIVE_INFINITY =
		Double.longBitsToDouble(-4503599627370496L);
	
	public static final double NaN =
		Double.longBitsToDouble(9221120237041090560L);
	
	public static final double POSITIVE_INFINITY =
		Double.longBitsToDouble(9218868437227405312L);
	
	/** The number of bits double requires for storage. */
	public static final int SIZE =
		64;
	
	/** The class representing the primitive type. */
	public static final Class<Double> TYPE =
		JVMFunction.jvmLoadClass(Assembly.classInfoOfDouble());
	
	/** The mask for NaN values. */
	private static final long _NAN_MASK =
		0b0111111111111000000000000000000000000000000000000000000000000000L;
	
	/** The value for this double. */
	private final double _value;
	
	public Double(double __a)
	{
		throw new todo.TODO();
	}
	
	public Double(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	@Override
	public byte byteValue()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int compareTo(Double __a)
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public int hashCode()
	{
		long v = this.doubleToLongBits(this._value); 
		return (int)(v ^ (v >>> 32));
	}
	
	@Override
	public int intValue()
	{
		throw new todo.TODO();
	}
	
	public boolean isInfinite()
	{
		throw new todo.TODO();
	}
	
	public boolean isNaN()
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
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static int compare(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the bits which represent the double value with all NaN values
	 * collapsed into a single form.
	 *
	 * @param __v The input value.
	 * @return The bits for the value.
	 * @since 2018/11/04
	 */
	public static long doubleToLongBits(double __v)
	{
		long raw = Double.doubleToRawLongBits(__v);
		
		// Collapse all NaN values to a single form
		if ((raw & Double._NAN_MASK) == (Double._NAN_MASK))
			return Double._NAN_MASK;
		
		return raw;
	}
	
	/**
	 * Returns the raw bits which represent the double value.
	 *
	 * @param __v The input value.
	 * @return The raw bits for the value.
	 * @since 2018/11/03
	 */
	public static long doubleToRawLongBits(double __v)
	{
		return Assembly.doubleToRawLongBits(__v);
	}
	
	public static boolean isInfinite(double __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isNaN(double __a)
	{
		throw new todo.TODO();
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
	public static double longBitsToDouble(long __b)
	{
		return Assembly.longBitsToDouble(__b);
	}
	
	public static double parseDouble(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static String toString(double __a)
	{
		throw new todo.TODO();
	}
	
	public static Double valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Double valueOf(double __a)
	{
		throw new todo.TODO();
	}
}

