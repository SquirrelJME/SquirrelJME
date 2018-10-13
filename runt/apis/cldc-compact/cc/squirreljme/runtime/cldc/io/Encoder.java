// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * This interface represents an encoder that is used to turn characters into
 * potentially multiple bytes.
 *
 * @since 2018/09/16
 */
public interface Encoder
	extends NamedCodec
{
	/**
	 * Encodes the given character and writes it to the output byte array.
	 *
	 * @param __c The character to encode.
	 * @param __b The output byte array.
	 * @param __o The offset to the array.
	 * @param __l The length of the area to write.
	 * @return The number of bytes written, {@code -1} means the character
	 * cannot be encoded because it will not fit in the buffer.
	 * @throws IndexOutOfBoundsException If the offset and or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/21
	 */
	public abstract int encode(char __c, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
}

