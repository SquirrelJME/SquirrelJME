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
 * This class provides a decoder for UTF-8 bytes.
 *
 * @since 2018/10/13
 */
public final class UTF8Decoder
	implements Decoder
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/06
	 */
	@Override
	public final double averageSequenceLength()
	{
		return 1.3333;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Not enough to decode a character
		if (__l <= 0)
			return -1;
		
		// Determine the length of the sequence
		byte a = __b[__o];
		int seqlen = 
			(((a & 0b1_0000000) == 0b0_0000000) ? 1 :
			(((a & 0b111_00000) == 0b110_00000) ? 2 :
			(((a & 0b1111_0000) == 0b1110_0000) ? 3 :
			(((a & 0b11111_000) == 0b11110_000) ? 4 :
			-1))));
		
		// Either some unknown sequence or it is a character after U+FFFF which
		// is not supported
		if (seqlen < 0 || seqlen == 4)
			return 0xFFFD | (seqlen > 0 ? (seqlen << 16) : 0x1_0000);
		
		// Cannot decode the entire sequence because there is not enough
		// room
		if (__l < seqlen)
			return -seqlen;
		
		// Decode the character
		switch (seqlen)
		{
				// U+0000 to U+007F
			case 1:
				return (a & 0xFF) | 0x1_0000;
				
				// U+0080 to U+07FF
			case 2:
				return (((__b[__o] & 0b11111) << 6) |
					(__b[__o + 1] & 0b111111)) | 0x2_0000;
				
				// U+0800 to U+FFFF
			case 3:
				return (((__b[__o] & 0b1111) << 12) |
					((__b[__o + 1] & 0b111111) << 6) |
					(__b[__o + 2] & 0b111111)) | 0x3_0000;
				
				// Should not occur
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final String encodingName()
	{
		return "utf-8";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 4;
	}
}

