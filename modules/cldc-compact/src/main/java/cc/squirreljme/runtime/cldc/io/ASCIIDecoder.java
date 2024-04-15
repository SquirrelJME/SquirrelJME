// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Decoder for ASCII.
 *
 * @since 2018/12/23
 */
public final class ASCIIDecoder
	implements Decoder
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/23
	 */
	@Override
	public final double averageSequenceLength()
	{
		return 1.0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/23
	 */
	@Override
	public final int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Not enough to decode a character
		if (__l <= 0)
			return -1;
		
		// Negative values are 128 and up
		byte b = __b[__o];
		if (b < 0)
			return 0x1_0000 | 0xFFFD;
		return 0x1_0000 | (b & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/23
	 */
	@Override
	public final String encodingName()
	{
		return "ascii";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/23
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 1;
	}
}

