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
	 * Determines the address of the value with the given length as if it were
	 * accessed from the base of the stack (the frame pointer) which is at the
	 * low end of the stack.
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
		// {@squirreljme.error AR08 Illegal stack offset and/or length, the
		// length is zero, or the combined values overflow. (The stack offset;
		// The length)}
		if (__len <= 0 || (__soff + __len) < __soff)
			throw new IllegalArgumentException(String.format("AR08 %d %d",
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
	
	/**
	 * Modifies a value and returns the value which is used for its direction,
	 * positive values are for higher positions while negative values are for
	 * lower positions. Depending on the direction, the value will be modified
	 * accordingly so the stack pointer can move a given direction.
	 *
	 * @param __d The value to modify, more positive values are higher stack
	 * values.
	 * @return The modified value for the stack direction.
	 * @since 2016/09/25
	 */
	public final int direction(int __d)
	{
		// Depends
		switch (this)
		{
				// Known
			case LOW_TO_HIGH: return __d;
			case HIGH_TO_LOW: return -__d;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * This calculates the position of the specified entry from the top of the
	 * stack (the stack pointer).
	 *
	 * Since this is from the top of the stack, the size of the stack must also
	 * be passed.
	 *
	 * @param __soff The offset of the entry on the stack, from the base of
	 * the stack.
	 * @param __len The length of the entry.
	 * @param __ss The current size of the stack.
	 * @return The offset from the top of the stack, where the entry is
	 * located.
	 * @throws IllegalArgumentException If the length is zero or negative, or
	 * the stack size is negative.
	 * @since 2016/09/27
	 */
	public final int top(int __soff, int __len, int __ss)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AR0d The size of the stack is negative.}
		if (__ss < 0)
			throw new IllegalArgumentException("AR0d");
		
		// {@squirreljme.error AR0e Illegal stack offset and/or length, the
		// length is zero, or the combined values overflow. (The stack offset;
		// The length)}
		if (__len <= 0 || (__soff + __len) < __soff)
			throw new IllegalArgumentException(String.format("AR0e %d %d",
				__soff, __len));
		
		// Depends
		switch (this)
		{
				// Known
			case LOW_TO_HIGH: return (__ss - __soff);
			case HIGH_TO_LOW: return __ss - (__soff + __len);
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

