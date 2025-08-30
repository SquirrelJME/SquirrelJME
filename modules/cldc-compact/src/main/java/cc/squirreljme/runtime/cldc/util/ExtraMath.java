// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.jvm.mle.MathAccelShelf;
import cc.squirreljme.jvm.mle.constants.MathAccelFlag;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Extra math functions not available in {@link Math}.
 *
 * @since 2025/05/03
 */
@SquirrelJMEVendorApi
public final class ExtraMath
{
	/**
	 * Not used. 
	 *
	 * @since 2025/05/03
	 */
	private ExtraMath()
	{
	}
	
	/**
	 * Performs the {@code exp(x)} function.
	 *
	 * @param __v The value to perform the function on.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static strictfp double exp(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.EXP) != 0)
			return MathAccelShelf.exp(__v);
		throw Debugging.todo();
	}
	
	/**
	 * Returns the logarithm of the given number.
	 *
	 * @param __v The value to get the logarithm from.
	 * @return The logarithm for the given value.
	 * @since 2018/11/03
	 */
	@SquirrelJMEVendorApi
	public static strictfp double log(double __v)
	{
		if ((MathAccelShelf.accel() & MathAccelFlag.LOG) != 0)
			return MathAccelShelf.log(__v);
		return FDMLMath.log(__v);
	}
	
	/**
	 * Performs the {@code pow(x, y)} function.
	 *
	 * @param __x The first value.
	 * @param __y The second value.
	 * @return The resultant value.
	 * @since 2025/05/03
	 */
	@SquirrelJMEVendorApi
	public static strictfp double pow(double __x, double __y)
	{
		// Use normal acceleration
		int accel = MathAccelShelf.accel();
		if ((accel & MathAccelFlag.POW) != 0)
			return MathAccelShelf.pow(__x, __y);
		
		// Use alternative calculation
		else if ((accel & (MathAccelFlag.EXP | MathAccelFlag.LOG)) ==
			(MathAccelFlag.EXP | MathAccelFlag.LOG))
			return ExtraMath.exp(__y * ExtraMath.log(__x));
		
		throw Debugging.todo();
	}
}
