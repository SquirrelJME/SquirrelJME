// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

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
		-1.0D / 0.0D;
	
	public static final double NaN =
		0.0D / 0.0D;
	
	public static final double POSITIVE_INFINITY =
		1.0D / 0.0D;
	
	public static final int SIZE =
		64;
	
	public static final Class<Double> TYPE =
		__getType();
	
	public Double(double __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Double(String __a)
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
	
	public int compareTo(Double __a)
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
	
	public static int compare(double __a, double __b)
	{
		throw new Error("TODO");
	}
	
	public static long doubleToLongBits(double __a)
	{
		throw new Error("TODO");
	}
	
	public static long doubleToRawLongBits(double __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean isInfinite(double __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean isNaN(double __a)
	{
		throw new Error("TODO");
	}
	
	public static double longBitsToDouble(long __a)
	{
		throw new Error("TODO");
	}
	
	public static double parseDouble(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new Error("TODO");
	}
	
	public static String toString(double __a)
	{
		throw new Error("TODO");
	}
	
	public static Double valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new Error("TODO");
	}
	
	public static Double valueOf(double __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * The {@link #TYPE} field is magically initialized by the virtual machine.
	 *
	 * @return {@link #TYPE}.
	 * @since 2016/06/16
	 */
	private static Class<Double> __getType()
	{
		return TYPE;
	}
}

