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
 * Decoder for ISO-8859-15.
 *
 * @since 2019/04/29
 */
public class ISO885915Decoder
	implements Decoder
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final double averageSequenceLength()
	{
		return 1.0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
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
		
		// Remap some characters?
		int c = (__b[__o] & 0xFF);
		switch (c)
		{
			case 0x00A4:	c = 0x20AC; break;
			case 0x00A6:	c = 0x0160; break;
			case 0x00A8:	c = 0x0161; break;
			case 0x00B4:	c = 0x017D; break;
			case 0x00B8:	c = 0x017E; break;
			case 0x00BC:	c = 0x0152; break;
			case 0x00BD:	c = 0x0153; break;
			case 0x00BE:	c = 0x0178; break;
		}
		
		return 0x1_0000 | c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final String encodingName()
	{
		return "iso-8859-15";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 1;
	}
}

