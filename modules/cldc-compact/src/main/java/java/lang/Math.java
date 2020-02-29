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
import cc.squirreljme.runtime.cldc.util.FDMLMath;

/**
 * This class contains mathematical functions.
 *
 * @since 2018/12/08
 */
@ImplementationNote("This class uses strict floating point.")
public final strictfp class Math
{
	/** E. */
	public static final double E =
		+0x1.5BF0A8B145769p1D;
	
	/** Pi. */
	public static final double PI =
		+0x1.921FB54442D18p1D;
	
	/**
	 * Not used.
	 *
	 * @since 2018/12/08
	 */
	private Math()
	{
	}
	
	/**
	 * Returns the absolute value.
	 *
	 * @param __v The input value.
	 * @return The absolute value.
	 * @since 2019/04/14
	 */
	public static int abs(int __v)
	{
		return (__v < 0 ? -__v : __v);
	}
	
	/**
	 * Returns the absolute value.
	 *
	 * @param __v The input value.
	 * @return The absolute value.
	 * @since 2019/04/14
	 */
	public static long abs(long __v)
	{
		return (__v < 0 ? -__v : __v);
	}
	
	/**
	 * Returns the absolute value.
	 *
	 * @param __v The input value.
	 * @return The absolute value.
	 * @since 2019/04/14
	 */
	public static float abs(float __v)
	{
		return Float.intBitsToFloat(0x7FFFFFFF & Float.floatToIntBits(__v));
	}
	
	/**
	 * Returns the absolute value.
	 *
	 * @param __v The input value.
	 * @return The absolute value.
	 * @since 2019/04/14
	 */
	public static double abs(double __v)
	{
		return Double.longBitsToDouble(0x7FFFFFFFFFFFFFFFL &
			Double.doubleToLongBits(__v));
	}
	
	public static double acos(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double asin(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double atan(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double atan2(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	public static double ceil(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double copySign(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	public static float copySign(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	public static double cos(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double floor(double __a)
	{
		throw new todo.TODO();
	}
	
	public static int getExponent(float __a)
	{
		throw new todo.TODO();
	}
	
	public static int getExponent(double __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the logarithm of the given number.
	 *
	 * This method does not exist in CLDC and is SquirrelJME specified.
	 *
	 * @param __v The value to get the logarithm from.
	 * @return The logarithm for the given value.
	 * @since 2018/11/03
	 */
	@Deprecated
	@ImplementationNote("This method does not exist in the CLDC library.")
	public static double log(double __v)
	{
		return FDMLMath.log(__v);
	}
	
	/**
	 * Returns the higher of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The higher value.
	 * @since 2018/09/29
	 */
	public static int max(int __a, int __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	public static long max(long __a, long __b)
	{
		throw new todo.TODO();
	}
	
	public static float max(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	public static double max(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/09/29
	 */
	public static int min(int __a, int __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/12/08
	 */
	public static long min(long __a, long __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/12/08
	 */
	public static float min(float __a, float __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/12/08
	 */
	public static double min(double __a, double __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
	
	public static double random()
	{
		throw new todo.TODO();
	}
	
	public static int round(float __a)
	{
		throw new todo.TODO();
	}
	
	public static long round(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double signum(double __a)
	{
		throw new todo.TODO();
	}
	
	public static float signum(float __a)
	{
		throw new todo.TODO();
	}
	
	public static double sin(double __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the square root of the given number.
	 *
	 * @param __v The input value.
	 * @return The square root.
	 * @since 2018/11/03
	 */
	public static double sqrt(double __v)
	{
		return FDMLMath.sqrt(__v);
	}
	
	public static double tan(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double toDegrees(double __a)
	{
		throw new todo.TODO();
	}
	
	public static double toRadians(double __a)
	{
		throw new todo.TODO();
	}
}

