// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

/**
 * General utilities.
 *
 * @since 2023/12/16
 */
public final class Utils
{
	/**
	 * Not used.
	 * 
	 * @since 2023/12/16
	 */
	private Utils()
	{
	}
	
	/**
	 * Is the system big endian?
	 * 
	 * @return {@code true} if the system is big endian.
	 * @since 2023/12/16
	 */
	public static boolean isBigEndian()
	{
		return AllocSizeOf.IS_BIG_ENDIAN.size() != 0;
	}
	
	/**
	 * Returns the pointer size.
	 *
	 * @return The pointer size.
	 * @since 2023/12/16
	 */
	public static int pointerSize()
	{
		return AllocSizeOf.POINTER.size();
	}
}
