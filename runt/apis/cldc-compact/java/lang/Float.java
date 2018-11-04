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

import cc.squirreljme.runtime.cldc.asm.PrimitiveAccess;

public final class Float
	extends Number
	implements Comparable<Float>
{
	public static final int MAX_EXPONENT =
		127;
	
	public static final float MAX_VALUE =
		+0x1.FFFFFEp127F;
	
	public static final int MIN_EXPONENT =
		-126;
	
	public static final float MIN_NORMAL =
		+0x1.0p-126F;
	
	public static final float MIN_VALUE =
		+0x1.0p-149F;
	
	public static final float NEGATIVE_INFINITY =
		-1.0F / 0.0F;
	
	public static final float NaN =
		0.0F / 0.0F;
	
	public static final float POSITIVE_INFINITY =
		1.0F / 0.0F;
	
	public static final int SIZE =
		32;
	
	public static final Class<Float> TYPE =
		__getType();
	
	public Float(float __a)
	{
		super();
		throw new todo.TODO();
	}
	
	public Float(double __a)
	{
		super();
		throw new todo.TODO();
	}
	
	public Float(String __a)
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
	
	public int compareTo(Float __a)
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
	
	public static int compare(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	public static int floatToIntBits(float __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the specified float into its raw integer bit form.
	 *
	 * @param __v The input value.
	 * @return The bits that make up the float.
	 * @since 2018/11/04
	 */
	public static int floatToRawIntBits(float __v)
	{
		return PrimitiveAccess.floatToRawIntBits(__v);
	}
	
	/**
	 * Converts the specified integer bits into a floating point value.
	 *
	 * Note that this value might be modified depending on the platform if
	 * the platform based conversion cannot handle signaling NaNs or illegal
	 * floating point values.
	 *
	 * @param __b The input bits.
	 * @return The resulting float.
	 * @since 2018/11/04
	 */
	public static float intBitsToFloat(int __b)
	{
		return PrimitiveAccess.intBitsToFloat(__b);
	}
	
	public static boolean isInfinite(float __a)
	{
		throw new todo.TODO();
	}
	
	public static boolean isNaN(float __a)
	{
		throw new todo.TODO();
	}
	
	public static float parseFloat(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static String toString(float __a)
	{
		throw new todo.TODO();
	}
	
	public static Float valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Float valueOf(float __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * The {@link #TYPE} field is magically initialized by the virtual machine.
	 *
	 * @return {@link #TYPE}.
	 * @since 2016/06/16
	 */
	private static Class<Float> __getType()
	{
		return TYPE;
	}
}

