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

import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;
import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
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
	
	/** The number of bits float requires for storage. */
	public static final int SIZE =
		32;
	
	/** The mask for NaN values. */
	private static final int _NAN_MASK =
		0b0111_1111_1000_0000_0000_0000_0000_0000;
	
	/** The class representing the primitive type. */
	public static final Class<Float> TYPE =
		ObjectAccess.<Float>classByNameType("float");
	
	/** The stored value. */
	private final float _value;
	
	/**
	 * Stores the specified float.
	 *
	 * @param __v The value to store.
	 * @since 2018/11/04
	 */
	public Float(float __v)
	{
		this._value = __v;
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
		
		// If both values are zero, the sign is important
		int ra = Float.floatToRawIntBits(a),
			rb = Float.floatToRawIntBits(b);
		if ((ra & 0x7FFFFFFF) == 0 && (rb & 0x7FFFFFFF) == 0)
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
	
	@Override
	public int intValue()
	{
		throw new todo.TODO();
	}
	
	public boolean isInfinite()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is this the NaN value.
	 *
	 * @return If this is the NaN value.
	 * @since 2018/11/04
	 */
	public boolean isNaN()
	{
		return Float.isNaN(this._value);
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
	
	/**
	 * Converts the specified float into its integer bit form with NaNs losing
	 * all their signaling and turning into non-signaling NaNs.
	 *
	 * @param __v The value the get the bit representation of.
	 * @return The bit representation of the float.
	 * @since 2018/11/04
	 */
	public static int floatToIntBits(float __v)
	{
		int raw = Float.floatToRawIntBits(__v);
		
		// Collapse all NaN values to a single form
		if ((raw & _NAN_MASK) == (_NAN_MASK))
			return _NAN_MASK;
		
		return raw;
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
	
	/**
	 * Is the specified value a NaN?
	 *
	 * @param __v The value to check.
	 * @return If it is NaN or not.
	 * @since 2018/11/04
	 */
	public static boolean isNaN(float __v)
	{
		return (Float.floatToRawIntBits(__v) & _NAN_MASK) == _NAN_MASK;
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
	
	/**
	 * Returns the boxed representation of the given float.
	 *
	 * @param __v The float value.
	 * @return The boxed float.
	 * @since 2018/11/04
	 */
	public static Float valueOf(float __v)
	{
		return new Float(__v);
	}
}

