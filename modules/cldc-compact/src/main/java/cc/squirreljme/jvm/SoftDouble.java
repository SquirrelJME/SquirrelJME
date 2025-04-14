// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Software math operations on 64-bit double.
 *
 * @since 2019/05/24
 */
@SquirrelJMEVendorApi
public final class SoftDouble
{
	/** The zero check mask. */
	@SquirrelJMEVendorApi
	public static final long ZERO_CHECK_MASK =
		0x7FFFFFFFFFFFFFFFL;
	
	/** The mask for NaN values. */
	@SquirrelJMEVendorApi
	public static final long NAN_MASK =
		0b0111111111111000000000000000000000000000000000000000000000000000L;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/24
	 */
	private SoftDouble()
	{
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double add(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double add(double __a, double __b)
	{
		return SoftDouble.add(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static int cmpl(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static int cmpl(double __a, double __b)
	{
		return SoftDouble.cmpl(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static int cmpg(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static int cmpg(double __a, double __b)
	{
		return SoftDouble.cmpg(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double div(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double div(double __a, double __b)
	{
		return SoftDouble.div(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Is this Not a Number?
	 * 
	 * @param __a The value to check.
	 * @return If this is not a number.
	 * @since 2022/01/06
	 */
	@SquirrelJMEVendorApi
	public static boolean isNaN(long __a)
	{
		return SoftDouble.NAN_MASK == (__a & SoftDouble.NAN_MASK);
	}
	
	/**
	 * Is this Not a Number?
	 * 
	 * @param __a The value to check.
	 * @return If this is not a number.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static boolean isNaN(double __a)
	{
		return SoftDouble.isNaN(MathShelf.rawDoubleToLong(__a));
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double mul(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double mul(double __a, double __b)
	{
		return SoftDouble.mul(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double neg(long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double neg(double __a)
	{
		return SoftDouble.neg(MathShelf.rawDoubleToLong(__a));
	}
	
	/**
	 * ORs value, used for constants.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/27
	 */
	@SquirrelJMEVendorApi
	public static double or(long __a, long __b)
	{
		return __a | __b;
	}
	
	/**
	 * ORs value, used for constants.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double or(double __a, double __b)
	{
		return SoftDouble.or(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double rem(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double rem(double __a, double __b)
	{
		return SoftDouble.rem(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double sub(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static double sub(double __a, double __b)
	{
		return SoftDouble.sub(MathShelf.rawDoubleToLong(__a),
			MathShelf.rawDoubleToLong(__b));
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static float toFloat(long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static float toFloat(double __a)
	{
		return SoftDouble.toFloat(MathShelf.rawDoubleToLong(__a));
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static int toInteger(long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static int toInteger(double __a)
	{
		return SoftDouble.toInteger(MathShelf.rawDoubleToLong(__a));
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static long toLong(long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __a A value.
	 * @return The result.
	 * @since 2023/08/02
	 */
	@SquirrelJMEVendorApi
	public static long toLong(double __a)
	{
		return SoftDouble.toLong(MathShelf.rawDoubleToLong(__a));
	}
}

