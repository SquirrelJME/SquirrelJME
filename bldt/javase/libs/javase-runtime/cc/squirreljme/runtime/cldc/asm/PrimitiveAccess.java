// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * Access to primitive.
 *
 * @since 2018/11/03
 */
public final class PrimitiveAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/11/03
	 */
	private PrimitiveAccess()
	{
	}
	
	/**
	 * Double to raw long bits.
	 *
	 * @param __d The input double.
	 * @return The raw long bits.
	 * @since 2018/11/03
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final long doubleToRawLongBits(double __d)
	{
		return Double.doubleToRawLongBits(__d);
	}
	
	/**
	 * Float to raw int bits.
	 *
	 * @param __d The input float.
	 * @return The raw int bits.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int floatToRawIntBits(float __f)
	{
		return Float.floatToRawIntBits(__f);
	}
	
	/**
	 * Integer bits to float.
	 *
	 * @param __b The input bits.
	 * @return The resulting float.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final float intBitsToFloat(int __b)
	{
		return Float.intBitsToFloat(__b);
	}
	
	/**
	 * Long bits to double.
	 *
	 * @param __b The input bits.
	 * @return The resulting double.
	 * @since 2018/11/03
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final double longBitsToDouble(long __b)
	{
		return Double.longBitsToDouble(__b);
	}
}

