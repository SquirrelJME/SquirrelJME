// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.FDMLMath;
import java.util.Random;

/**
 * This class contains mathematical functions.
 *
 * @since 2018/12/08
 */
@Api
@SuppressWarnings("FieldNamingConvention")
@ImplementationNote("This class uses strict floating point.")
public final strictfp class Math
{
	/** E. */
	@Api
	public static final double E =
		+0x1.5BF0A8B145769p1D;
	
	/** Pi. */
	@Api
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
	@Api
	public static strictfp int abs(int __v)
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
	@Api
	public static strictfp long abs(long __v)
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
	@Api
	public static strictfp float abs(float __v)
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
	@Api
	public static strictfp double abs(double __v)
	{
		return Double.longBitsToDouble(0x7FFFFFFFFFFFFFFFL &
			Double.doubleToLongBits(__v));
	}
	
	@Api
	public static strictfp double acos(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double asin(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double atan(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double atan2(double __a, double __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double ceil(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double copySign(double __a, double __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp float copySign(float __a, float __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double cos(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double floor(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp int getExponent(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp int getExponent(double __a)
	{
		throw Debugging.todo();
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
	@Api
	@Deprecated
	@ImplementationNote("This method does not exist in the CLDC library.")
	public static strictfp double log(double __v)
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
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp int max(int __a, int __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the higher of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The higher value.
	 * @since 2024/03/05
	 */
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp long max(long __a, long __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the higher of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The higher value.
	 * @since 2024/03/05
	 */
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp float max(float __a, float __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the higher of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The higher value.
	 * @since 2024/03/05
	 */
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp double max(double __a, double __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/09/29
	 */
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp int min(int __a, int __b)
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
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp long min(long __a, long __b)
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
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp float min(float __a, float __b)
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
	@Api
	@SuppressWarnings("ManualMinMaxCalculation")
	public static strictfp double min(double __a, double __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns a random number using the same means
	 * as {@link Random#nextDouble()}. A new instance of {@link Random} is
	 * created just to obtain the value, so its seed will be the same
	 * as specified in {@link Random#Random()}.
	 *
	 * @return The same as {@link Random#nextDouble()}.
	 * @since 2024/03/05
	 */
	@Api
	public static strictfp double random()
	{
		return new Random().nextDouble();
	}
	
	@Api
	public static strictfp int round(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp long round(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double signum(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp float signum(float __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double sin(double __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the square root of the given number.
	 *
	 * @param __v The input value.
	 * @return The square root.
	 * @since 2018/11/03
	 */
	@Api
	public static strictfp double sqrt(double __v)
	{
		return FDMLMath.sqrt(__v);
	}
	
	@Api
	public static strictfp double tan(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double toDegrees(double __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double toRadians(double __a)
	{
		throw Debugging.todo();
	}
}

