// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This represents the direction that the stack moves in.
 *
 * @since 2016/09/01
 */
public enum NativeStackDirection
{
	/** Stack grows from lower addresses to higher ones. */
	LOW_TO_HIGH,
	
	/** Stack grows from higher addresses to lower ones. */
	HIGH_TO_LOW,
	
	/** End. */
	;
	
	/**
	 * Determines the base address at which to start writing values to on the
	 * stack.
	 *
	 * @param __soff The offset from the base of the stack.
	 * @param __len The number of bytes that make up the value.
	 * @return The base address relative to the stack base where a value is
	 * located.
	 * @throws IllegalArgumentException If any input is negative, the length
	 * is zero, or the resulting values overflow.
	 * @since 2016/09/23
	 */
	public final int base(int __soff, int __len)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BX02 Illegal stack offset and/or length, the
		// length is zero, the stack offset is negative, or the combined values
		// overflow. (The stack offset; The length)}
		if (__soff < 0 || __len <= 0 || (__soff + __len) <= 0)
			throw new IllegalArgumentException(String.format("BX02 %d %d",
				__soff, __len));
		
		// Depends
		switch (this)
		{
				// Known
			case LOW_TO_HIGH: return __soff;
			case HIGH_TO_LOW: return -(__soff + __len);
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

