// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;

/**
 * General utilities for SummerCoat.
 *
 * @since 2021/01/17
 */
public final class SummerCoatUtil
{
	/**
	 * Not used.
	 * 
	 * @since 2021/01/17
	 */
	private SummerCoatUtil()
	{
	}
	
	/**
	 * Is this an array kind of {@link MemHandleKind}.
	 * 
	 * @param __kind The {@link MemHandleKind} to check.
	 * @return If this is an array kind.
	 * @throws IllegalArgumentException If the kind is not valid.
	 * @since 2021/01/17
	 */
	public static boolean isArrayKind(int __kind)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ4l Invalid memory handle kind. (The kind)}
		if (__kind <= 0 || __kind >= MemHandleKind.NUM_KINDS)
			throw new IllegalArgumentException("ZZ4l " + __kind);
		
		switch (__kind)
		{
			case MemHandleKind.BOOLEAN_ARRAY:
			case MemHandleKind.BYTE_ARRAY:
			case MemHandleKind.SHORT_ARRAY:
			case MemHandleKind.CHARACTER_ARRAY:
			case MemHandleKind.INTEGER_ARRAY:
			case MemHandleKind.LONG_ARRAY:
			case MemHandleKind.FLOAT_ARRAY:
			case MemHandleKind.DOUBLE_ARRAY:
			case MemHandleKind.OBJECT_ARRAY:
				return true;
		}
		
		return false;
	}
}
