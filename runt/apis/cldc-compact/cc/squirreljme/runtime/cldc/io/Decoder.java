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
 * This is a decoder which is able to decode input characters and return
 * characters for the input sequence.
 *
 * @since 2018/10/13
 */
public interface Decoder
	extends NamedCodec
{
	/**
	 * Decodes the input bytes.
	 *
	 * @param __b The input byte array.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The decoded character, if a negative value is used it is a hint
	 * on the number of bytes needed to decode the character. Note that the
	 * hint may always be {@code -1} if in the event it will not be known how
	 * many bytes to read for a given character.
	 * @throws IndexOutOfBoundsException If the offset and or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public abstract int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
}

