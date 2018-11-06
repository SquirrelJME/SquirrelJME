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
	 * The average sequence length used for characters, used to estimate how
	 * big of an array to allocate for characters.
	 *
	 * @return The average sequence length.
	 * @since 2018/11/06
	 */
	public abstract double averageSequenceLength();
	
	/**
	 * Decodes the input bytes.
	 *
	 * @param __b The input byte array.
	 * @param __o The offset.
	 * @param __l The length. Due to the way the return value is used, the
	 * maximum supported length must be limited to 32,767 characters.
	 * @return The decoded character with the upper 16-bits specifying the
	 * number of read bytes, if a negative value is used it is a hint
	 * on the number of bytes needed to decode the character. Note that the
	 * hint may always be {@code -1} if in the event it will not be known how
	 * many bytes to read for a given character. The only result which will
	 * return the length in the upper bytes are sequences which generate
	 * characters, so incomplete sequences will always be negative. When
	 * casting the result to {@code char} the upper bits will be truncated.
	 * @throws IndexOutOfBoundsException If the offset and or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	public abstract int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Returns a hint which specifies the maximum length of a byte sequence
	 * for decoding.
	 *
	 * @return The maximum sequence length for decoding.
	 * @since 2018/10/13
	 */
	public abstract int maximumSequenceLength();
}

