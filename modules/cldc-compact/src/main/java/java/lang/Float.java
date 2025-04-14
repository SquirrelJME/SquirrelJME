// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.SoftFloat;
import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class Float
	extends Number
	implements Comparable<Float>
{
	@Api
	public static final int MAX_EXPONENT =
		127;
	
	@Api
	public static final float MAX_VALUE =
		+0x1.FFFFFEp127F;
	
	@Api
	public static final int MIN_EXPONENT =
		-126;
	
	@Api
	public static final float MIN_NORMAL =
		+0x1.0p-126F;
	
	@Api
	public static final float MIN_VALUE =
		+0x1.0p-149F;
	
	@Api
	public static final float NEGATIVE_INFINITY =
		Float.intBitsToFloat(-8388608);
	
	@Api
	public static final float NaN =
		Float.intBitsToFloat(2143289344);
	
	@Api
	public static final float POSITIVE_INFINITY =
		Float.intBitsToFloat(2139095040);
	
	/** The number of bits float requires for storage. */
	@Api
	public static final int SIZE =
		32;
	
	/** The class representing the primitive type. */
	@Api
	public static final Class<Float> TYPE =
		TypeShelf.<Float>typeToClass(TypeShelf.typeOfFloat());
	
	/** The stored value. */
	private final float _value;
	
	/**
	 * Stores the specified float.
	 *
	 * @param __v The value to store.
	 * @since 2018/11/04
	 */
	@Api
	public Float(float __v)
	{
		this._value = __v;
	}
	
	@Api
	public Float(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Float(String __a)
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
	public int compareTo(Float __a)
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
	 * @since 2018/11/04
	 */
	@Override
	@ProgrammerTip("NaN values are equal to each other, positive and " +
		"negative zero are not equal to each other.")
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Float))
			return false;
		
		float a = this._value,
			b = ((Float)__o)._value;
		
		// Both values are NaN, consider it equal
		if (Float.isNaN(a) && Float.isNaN(b))
			return true;
		
		// If both values are zero, the sign is not important
		int ra = Float.floatToRawIntBits(a),
			rb = Float.floatToRawIntBits(b);
		if ((ra & SoftFloat.ZERO_CHECK_MASK) == 0 &&
			(rb & SoftFloat.ZERO_CHECK_MASK) == 0)
			return ra == rb;
		
		// Otherwise standard comparison
		return a == b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public float floatValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public int hashCode()
	{
		return Float.floatToIntBits(this._value);
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
	 * @since 2018/11/04
	 */
	@Api
	public boolean isNaN()
	{
		return Float.isNaN(this._value);
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
	public static int compare(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Converts the specified float into its integer bit form with NaNs losing
	 * all their signaling and turning into non-signaling NaNs.
	 *
	 * @param __v The value the get the bit representation of.
	 * @return The bit representation of the float.
	 * @since 2018/11/04
	 */
	@Api
	public static int floatToIntBits(float __v)
	{
		int raw = Float.floatToRawIntBits(__v);
		
		// Collapse all NaN values to a single form
		if (SoftFloat.isNaN(raw))
			return SoftFloat.NAN_MASK;
		
		return raw;
	}
	
	/**
	 * Converts the specified float into its raw integer bit form.
	 *
	 * @param __v The input value.
	 * @return The bits that make up the float.
	 * @since 2018/11/04
	 */
	@Api
	public static int floatToRawIntBits(float __v)
	{
		return MathShelf.rawFloatToInt(__v);
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
	@Api
	public static float intBitsToFloat(int __b)
	{
		return MathShelf.rawIntToFloat(__b);
	}
	
	@Api
	public static boolean isInfinite(float __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is the specified value a NaN?
	 *
	 * @param __v The value to check.
	 * @return If it is NaN or not.
	 * @since 2018/11/04
	 */
	@Api
	public static boolean isNaN(float __v)
	{
		return SoftFloat.isNaN(Float.floatToRawIntBits(__v));
	}
	
	@Api
	public static float parseFloat(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static String toString(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Float valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the boxed representation of the given float.
	 *
	 * @param __v The float value.
	 * @return The boxed float.
	 * @since 2018/11/04
	 */
	@SuppressWarnings("UnnecessaryBoxing")
	@Api
	public static Float valueOf(float __v)
	{
		return new Float(__v);
	}
}

