// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

/**
 * This represents a wrapped byte array.
 *
 * @since 2018/03/01
 */
public interface ByteArray
	extends Array
{
	/**
	 * Returns the byte at the given index.
	 *
	 * @param __i The index to get.
	 * @return The value.
	 * @throws ArrayIndexOutOfBoundsException If out of bounds.
	 * @since 2018/03/02
	 */
	public abstract byte get(int __i)
		throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Obtains a chunk of data from the array.
	 *
	 * @param __i The base index.
	 * @param __b The destination array.
	 * @param __o The offset into the destination array.
	 * @param __l The number of bytes to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/04
	 */
	public abstract void get(int __i, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
}

