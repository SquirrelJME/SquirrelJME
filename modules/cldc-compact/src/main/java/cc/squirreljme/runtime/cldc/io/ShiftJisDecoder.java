// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Decoder for Shift-JIS.
 * 
 * https://en.wikipedia.org/wiki/Shift_JIS
 * http://x0213.org/codetable/sjis-0213-2004-std.txt
 *
 * @since 2021/06/13
 */
public class ShiftJisDecoder
	implements Decoder
{
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public double averageSequenceLength()
	{ 
		return 2.0D;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Not enough to decode a character
		if (__l <= 0)
			return -1;
		
		// Standard ASCII
		int a = __b[0] & 0xFF;
		if (a <= 0x7F)
		{
			// Yen
			if (a == 0x5C)
				return '\u00A5' | 0x1_0000;
			
			// Overline
			else if (a == 0x7E)
				return '\u203E' | 0x1_0000;
			
			return (char)a | 0x1_0000;
		}
		
		// Two byte characters
		if ((a >= 0x81 && a <= 0x9F) || (a >= 0xE0 && a <= 0xEF))
		{
			// Not enough to read from
			if (__l < 2)
				return -1;
			
			// Unknown two-byte sequence
			return 0xFFFD | 0x2_0000;
		}
		
		// TODO: Decode more characters
		return 0xFFFD | 0x1_0000;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String encodingName()
	{
		return "shift-jis";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public int maximumSequenceLength()
	{
		return 2;
	}
}
