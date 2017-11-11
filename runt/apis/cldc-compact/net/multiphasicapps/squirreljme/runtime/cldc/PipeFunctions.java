// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class provides native virtual machine access to pipes used for
 * output to the system console.
 *
 * @since 2017/11/10
 */
public abstract class PipeFunctions
{
	/** Standard output. */
	public static final int STANDARD_OUTPUT =
		1;
	
	/** Standard error. */
	public static final int STANDARD_ERROR =
		2;
	
	/**
	 * Must be extended.
	 *
	 * @since 2017/11/10
	 */
	protected PipeFunctions()
	{
	}
	
	/**
	 * Internal implementation of {@link #write(int, int)}.
	 *
	 * @param __fd The pipe to write to.
	 * @param __b The byte to write.
	 * @since 2017/11/10
	 */
	protected abstract void protectedWrite(int __fd, byte __b);
	
	/**
	 * Internal implementation of {@link #write(int, byte[], int, int)}.
	 *
	 * @param __fd The pipe to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to write.
	 * @since 2017/11/10
	 */
	protected abstract void protectedWrite(int __id, byte[] __b, int __o,
		int __l);
	
	/**
	 * Writes a single byte to the given pipe descriptor.
	 *
	 * @param __fd The pipe to write to.
	 * @param __b The byte to write.
	 * @throws IllegalArgumentException If the descriptor is not valid.
	 * @since 2017/11/10
	 */
	public final void write(int __id, int __b)
		throws IllegalArgumentException
	{
		protectedWrite(__checkId(__id), (byte)__b);
	}
	
	/**
	 * Writes multiple bytes to the given pipe descriptor.
	 *
	 * @param __fd The pipe to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to write.
	 * @throws IllegalArgumentException If the descriptor is not valid.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/10
	 */
	public final void write(int __id, byte[] __b, int __o, int __l)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		protectedWrite(__checkId(__id), __b, __o, __l);
	}
	
	/**
	 * Ensures that the output identifier is correct.
	 *
	 * @param __id The identifier to check.
	 * @return {@code __id}.
	 * @throws IllegalArgumentException If the identifier is not valid.
	 * @since 2017/11/10
	 */
	private static final int __checkId(int __id)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ0b Attempt to write to output which is of
		// an unknown identifier.}
		if (__id != STANDARD_OUTPUT && __id != STANDARD_ERROR)
			throw new IllegalArgumentException("ZZ0b");
		return __id;
	}
}

