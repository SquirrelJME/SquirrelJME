// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.constants.MathAccelFlag;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Hardware math support.
 *
 * @since 2025/05/03
 */
@SquirrelJMEVendorApi
public class EmulatedMathAccelShelf
{
	/**
	 * Not used.
	 *
	 * @since 2025/05/03
	 */
	private EmulatedMathAccelShelf()
	{
	}
	
	/**
	 * Returns the math functions which are accelerated.
	 *
	 * @return The accelerated math functions.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = MathAccelFlag.class)
	public static int accel()
	{
		return -1;
	}
	
	/**
	 * Performs the {@code acos(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double acos(double __v)
	{
		return Math.acos(__v);
	}
	
	/**
	 * Performs the {@code asin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double asin(double __v)
	{
		return Math.asin(__v);
	}
	
	/**
	 * Performs the {@code asin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double atan(double __v)
	{
		return Math.atan(__v);
	}
	
	/**
	 * Performs the {@code atan2(x, y)} function.
	 *
	 * @param __a The first value.
	 * @param __b The second value.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double atan2(double __a, double __b)
	{
		return Math.atan2(__a, __b);
	}
	
	/**
	 * Performs the {@code ceil(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double ceil(double __v)
	{
		return Math.ceil(__v);
	}
	
	/**
	 * Performs the {@code cos(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double cos(double __v)
	{
		return Math.cos(__v);
	}
	
	/**
	 * Performs the {@code floor(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double floor(double __v)
	{
		return Math.floor(__v);
	}
	
	/**
	 * Returns the logarithm of the given number.
	 *
	 * @param __v The value to get the logarithm from.
	 * @return The logarithm for the given value.
	 * @since 2018/11/03
	 */
	@SquirrelJMEVendorApi
	public static double log(double __v)
	{
		return Math.log(__v);
	}
	
	/**
	 * Performs the {@code round(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static long round(double __v)
	{
		return Math.round(__v);
	}
	
	/**
	 * Performs the {@code signum(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double signum(double __v)
	{
		return Math.signum(__v);
	}
	
	/**
	 * Performs the {@code sin(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double sin(double __v)
	{
		return Math.sin(__v);
	}
	
	/**
	 * Performs the {@code sqrt(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2018/11/03
	 */
	@SquirrelJMEVendorApi
	public static double sqrt(double __v)
	{
		return Math.sqrt(__v);
	}
	
	/**
	 * Performs the {@code tan(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static double tan(double __v)
	{
		return Math.tan(__v);
	}
}
