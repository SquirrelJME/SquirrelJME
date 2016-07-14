// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

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
		throw new Error("TODO");
	}
	
	public Float(double __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Float(String __a)
		throws NumberFormatException
	{
		super();
		if (false)
			throw new NumberFormatException();
		throw new Error("TODO");
	}
	
	@Override
	public byte byteValue()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(Float __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public double doubleValue()
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public float floatValue()
	{
		throw new Error("TODO");
	}
	
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int intValue()
	{
		throw new Error("TODO");
	}
	
	public boolean isInfinite()
	{
		throw new Error("TODO");
	}
	
	public boolean isNaN()
	{
		throw new Error("TODO");
	}
	
	@Override
	public long longValue()
	{
		throw new Error("TODO");
	}
	
	@Override
	public short shortValue()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static int compare(float __a, float __b)
	{
		throw new Error("TODO");
	}
	
	public static int floatToIntBits(float __a)
	{
		throw new Error("TODO");
	}
	
	public static int floatToRawIntBits(float __a)
	{
		throw new Error("TODO");
	}
	
	public static float intBitsToFloat(int __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean isInfinite(float __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean isNaN(float __a)
	{
		throw new Error("TODO");
	}
	
	public static float parseFloat(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new Error("TODO");
	}
	
	public static String toString(float __a)
	{
		throw new Error("TODO");
	}
	
	public static Float valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new Error("TODO");
	}
	
	public static Float valueOf(float __a)
	{
		throw new Error("TODO");
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

