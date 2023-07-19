// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decoder for Shift-JIS.
 * 
 * https://en.wikipedia.org/wiki/Shift_JIS
 * http://x0213.org/codetable/sjis-0213-2004-std.txt
 *
 * @since 2021/06/13
 */
public class ShiftJisDecoder
	extends ShiftJisBase
	implements Decoder
{
	/** The loaded Shift-JIS table. */
	private static volatile DoubleByteTable _TABLE;
	
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
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Not enough to decode a character
		if (__l <= 0)
			return -1;
		
		// Standard ASCII
		int a = __b[__o] & 0xFF;
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
		
		// Single byte half-width Katakana
		else if (a >= 0xA1 && a <= 0xDF)
			return ShiftJisDecoder.__singleByteHalfWidthKatakana(a) | 0x1_0000;
		
		// Two byte characters
		else if ((a >= 0x81 && a <= 0x9F) || (a >= 0xE0 && a <= 0xEF))
		{
			// Not enough to read from
			if (__l < 2)
				return -1;
			
			// Decode
			int z = ShiftJisDecoder.loadTable().decode((byte)a, __b[__o + 1]);
			return (z < 0 ? 0xFFFD : z) | 0x2_0000;
		}
		
		// Unknown or invalid character
		return 0xFFFD | 0x1_0000;
	}
	
	/**
	 * Decodes a single byte half-width katakana character
	 * 
	 * @param __a The byte.
	 * @return The decoded byte.
	 * @since 2022/02/14
	 */
	@SuppressWarnings("MagicNumber")
	private static int __singleByteHalfWidthKatakana(int __a)
	{
		switch (__a)
		{
			case 0xA1:	return '\uFF61';
			case 0xA2:	return '\uFF62';
			case 0xA3:	return '\uFF63';
			case 0xA4:	return '\uFF64';
			case 0xA5:	return '\uFF65';
			case 0xA6:	return '\uFF66';
			case 0xA7:	return '\uFF67';
			case 0xA8:	return '\uFF68';
			case 0xA9:	return '\uFF69';
			case 0xAA:	return '\uFF6A';
			case 0xAB:	return '\uFF6B';
			case 0xAC:	return '\uFF6C';
			case 0xAD:	return '\uFF6D';
			case 0xAE:	return '\uFF6E';
			case 0xAF:	return '\uFF6F';
			case 0xB0:	return '\uFF70';
			case 0xB1:	return '\uFF71';
			case 0xB2:	return '\uFF72';
			case 0xB3:	return '\uFF73';
			case 0xB4:	return '\uFF74';
			case 0xB5:	return '\uFF75';
			case 0xB6:	return '\uFF76';
			case 0xB7:	return '\uFF77';
			case 0xB8:	return '\uFF78';
			case 0xB9:	return '\uFF79';
			case 0xBA:	return '\uFF7A';
			case 0xBB:	return '\uFF7B';
			case 0xBC:	return '\uFF7C';
			case 0xBD:	return '\uFF7D';
			case 0xBE:	return '\uFF7E';
			case 0xBF:	return '\uFF7F';
			case 0xC0:	return '\uFF80';
			case 0xC1:	return '\uFF81';
			case 0xC2:	return '\uFF82';
			case 0xC3:	return '\uFF83';
			case 0xC4:	return '\uFF84';
			case 0xC5:	return '\uFF85';
			case 0xC6:	return '\uFF86';
			case 0xC7:	return '\uFF87';
			case 0xC8:	return '\uFF88';
			case 0xC9:	return '\uFF89';
			case 0xCA:	return '\uFF8A';
			case 0xCB:	return '\uFF8B';
			case 0xCC:	return '\uFF8C';
			case 0xCD:	return '\uFF8D';
			case 0xCE:	return '\uFF8E';
			case 0xCF:	return '\uFF8F';
			case 0xD0:	return '\uFF90';
			case 0xD1:	return '\uFF91';
			case 0xD2:	return '\uFF92';
			case 0xD3:	return '\uFF93';
			case 0xD4:	return '\uFF94';
			case 0xD5:	return '\uFF95';
			case 0xD6:	return '\uFF96';
			case 0xD7:	return '\uFF97';
			case 0xD8:	return '\uFF98';
			case 0xD9:	return '\uFF99';
			case 0xDA:	return '\uFF9A';
			case 0xDB:	return '\uFF9B';
			case 0xDC:	return '\uFF9C';
			case 0xDD:	return '\uFF9D';
			case 0xDE:	return '\uFF9E';
			case 0xDF:	return '\uFF9F';
		}
		
		return 0xFFFD;
	}
	
	/**
	 * Loads the table for Shift-JIS.
	 * 
	 * @return The read table.
	 * @since 2022/02/14
	 */
	public static DoubleByteTable loadTable()
	{
		// Already read?
		DoubleByteTable rv = ShiftJisDecoder._TABLE;
		if (rv != null)
			return rv;
		
		// Load in the table
		try (InputStream in = DoubleByteTable.class.getResourceAsStream(
			"shiftjis.double"))
		{
			// Load the table in
			rv = DoubleByteTable.loadTable(in);
			
			// Cache and use it
			ShiftJisDecoder._TABLE = rv;
			return rv;
		}
		catch (IOException e)
		{
			/* {@squirreljme.error ZZ4i Could not load the Shift-JIS table.} */
			throw new RuntimeException("ZZ4i", e);
		}
	}
}
