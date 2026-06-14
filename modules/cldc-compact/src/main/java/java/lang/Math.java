// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.MathAccelShelf;
import cc.squirreljme.jvm.mle.constants.MathAccelFlag;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import cc.squirreljme.runtime.cldc.util.FDMLMath;
import java.util.Random;

/**
 * This class contains mathematical functions.
 *
 * Java ME CLDC does not have every math function that Java SE has, thus
 * SquirrelJME has implementations of these functions in {@link ExtraMath}. If
 * you plan to write a portable application meant to run on any runtime, you
 * will have to implement the function you need yourself.
 * 
 * @see ExtraMath
 * @since 2018/12/08
 */
@Api
@SuppressWarnings({"FieldNamingConvention", "ManualMinMaxCalculation"})
@ImplementationNote("This class uses strict floating point.")
public final strictfp class Math
{
	/** E. */
	@Api
	public static final double E =
		Double.longBitsToDouble(4613303445314885481L);
	
	/** Pi. */
	@Api
	public static final double PI =
		Double.longBitsToDouble(4614256656552045848L);
	
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
	
	/**
	 * Performs the {@code acos(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double acos(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.ACOS) != 0)
			return MathAccelShelf.acos(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code asin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double asin(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.ASIN) != 0)
			return MathAccelShelf.asin(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code asin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double atan(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.ATAN) != 0)
			return MathAccelShelf.atan(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code atan2(x, y)} function.
	 *
	 * @param __a The first value.
	 * @param __b The second value.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double atan2(double __a, double __b)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.ATAN2) != 0)
			return MathAccelShelf.atan2(__a, __b);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code ceil(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double ceil(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.CEIL) != 0)
			return MathAccelShelf.ceil(__v);
		throw Debugging.todo();
	}
	
	@Api
	public static strictfp double copySign(double __a, double __b)
	{
		return Double.longBitsToDouble(
			(Double.doubleToLongBits(__a) & 0x7FFFFFFFFFFFFFFFL) |
			(Double.doubleToLongBits(__b) & 0x8000000000000000L));
	}
	
	@Api
	public static strictfp float copySign(float __a, float __b)
	{
		return Float.intBitsToFloat(
			(Float.floatToIntBits(__a) & 0x7FFFFFFF) |
			(Float.floatToIntBits(__b) & 0x80000000));
	}
	
	/**
	 * Performs the {@code cos(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double cos(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.COS) != 0)
			return MathAccelShelf.cos(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code floor(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double floor(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.FLOOR) != 0)
			return MathAccelShelf.floor(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Returns the exponent of the given value.
	 *
	 * @param __v The value to get the exponent of.
	 * @return The resultant exponent.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp int getExponent(float __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the exponent of the given value.
	 *
	 * @param __v The value to get the exponent of.
	 * @return The resultant exponent.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp int getExponent(double __v)
	{
		throw Debugging.todo();
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
	
	/**
	 * Performs the {@code round(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp int round(float __v)
	{
		return (int)Math.round((double)__v);
	}
	
	/**
	 * Performs the {@code round(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp long round(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.ROUND) != 0)
			return MathAccelShelf.round(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code signum(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double signum(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.SIGNUM) != 0)
			return MathAccelShelf.signum(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code signum(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp float signum(float __v)
	{
		return (float)Math.signum((double)__v);
	}
	
	/**
	 * Performs the {@code sin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double sin(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.SIN) != 0)
			return MathAccelShelf.sin(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code sqrt(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2018/11/03
	 */
	@Api
	public static strictfp double sqrt(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.SQRT) != 0)
			return MathAccelShelf.sqrt(__v);
		return FDMLMath.sqrt(__v);
	}
	
	/**
	 * Performs the {@code tan(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double tan(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.TAN) != 0)
			return MathAccelShelf.tan(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Performs the {@code toDegrees(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double toDegrees(double __v)
	{
		return __v * (180.0 / Math.PI);
	}
	
	/**
	 * Performs the {@code toRadians(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@Api
	public static strictfp double toRadians(double __v)
	{
		return __v * (Math.PI / 180.0);
	}
}

