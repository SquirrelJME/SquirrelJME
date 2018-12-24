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
 * This encoder encodes input characters to standard UTF-8 sequences.
 *
 * Surrogate pairs will be encoded as 6 bytes.
 *
 * @since 2018/09/19
 */
public final class UTF8Encoder
	implements Encoder
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
	 * @since 2018/09/21
	 */
	@Override
	public int encode(char __c, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// One byte
		if (__c >= 0x0000 && __c <= 0x007F)
		{
			if (__l < 1)
				return -1;
			
			__b[__o] = (byte)__c;
			
			return 1;
		}
		
		// Two Byte
		else if (__c >= 0x0080 && __c <= 0x07FF)
		{
			if (__l < 2)
				return -2;
			
			__b[__o + 0] = (byte)(((__c >>> 6) & 0b11111) | 0b110_00000); 
			__b[__o + 1] = (byte)((__c & 0b111111) | 0b10_000000);
			
			return 2;
		}
		
		// Three byte (__c >= 0x0800 && __c <= 0xFFFF)
		else
		{
			if (__l < 3)
				return -3;
				
			__b[__o + 0] = (byte)(((__c >>> 12) & 0b1111) | 0b1110_0000); 
			__b[__o + 1] = (byte)(((__c >>> 6) & 0b111111) | 0b10_000000);
			__b[__o + 2] = (byte)((__c & 0b111111) | 0b10_000000);
			
			return 3;
		}
	}
	
	/**
	 * {@inheritDc}
	 * @since 2018/10/13
	 */
	@Override
	public final String encodingName()
	{
		return "utf-8";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 4;
	}
}

