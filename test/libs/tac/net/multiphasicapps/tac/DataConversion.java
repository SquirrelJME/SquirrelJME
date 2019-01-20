// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This class contains helpers for data conversion.
 *
 * @since 2019/01/20
 */
public final class DataConversion
{
	/**
	 * Converts the character array to a char array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final char[] arrayCharacterToChar(Character[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		char[] rv = new char[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].charValue();
		return rv;
	}
	
	/**
	 * Converts the number array to a byte array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final byte[] arrayNumberToByte(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		byte[] rv = new byte[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].byteValue();
		return rv;
	}
	
	/**
	 * Converts the number array to a double array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final double[] arrayNumberToDouble(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		double[] rv = new double[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].doubleValue();
		return rv;
	}
	
	/**
	 * Converts the number array to a float array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final float[] arrayNumberToFloat(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		float[] rv = new float[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].floatValue();
		return rv;
	}
	
	/**
	 * Converts the number array to an int array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final int[] arrayNumberToInt(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].intValue();
		return rv;
	}
	
	/**
	 * Converts the number array to a long array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final long[] arrayNumberToLong(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		long[] rv = new long[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].longValue();
		return rv;
	}
	
	/**
	 * Converts the number array to a short array.
	 *
	 * @param __a The input array.
	 * @return The converted array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/20
	 */
	public static final short[] arrayNumberToShort(Number[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		int n = __a.length;
		short[] rv = new short[n];
		for (int i = 0; i < n; i++)
			rv[i] = __a[i].shortValue();
		return rv;
	}
}

