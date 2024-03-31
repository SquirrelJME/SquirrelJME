// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Math utilities.
 *
 * @since 2024/03/17
 */
@SquirrelJMEVendorApi
public final class MathUtils
{
	/**
	 * Not used. 
	 * 
	 * @since 2024/03/17
	 */
	private MathUtils()
	{
	}
	
	
	/**
	 * Rounds to the nearest power of two.
	 *
	 * @param __val The value to round.
	 * @return The resultant rounded value.
	 * @since 2024/03/17
	 */
	public static int nearestPowerOfTwo(int __val)
	{
		int hi = Math.max(1, Integer.highestOneBit(__val));
		int mask = Math.max(1, hi - 1);
		if ((__val & mask) >= (hi >>> 1))
			return hi << 1;
		return hi;
	}
}
