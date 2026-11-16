// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.io.ByteDeque;

/**
 * PNG reading utilities.
 *
 * @since 2026/11/16
 */
final class __PNGUtil__
{
	/**
	 * Not used.
	 *
	 * @since 2026/11/16
	 */
	private __PNGUtil__()
	{
	}
	
	/**
	 * Reads all the input data and returns a byte array for the data, so it
	 * may be processed later.
	 *
	 * @param __in The stream to read from.
	 * @return The read data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	static byte[] __chunkLater(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The final glue point
		ByteDeque glue = new ByteDeque();
		
		// Read in all the various chunks as much as possible
		byte[] buf = StreamUtils.buffer(__in);
		for (;;)
		{
			// Read in the chunk data
			int rc = __in.read(buf, 0, buf.length);
			
			// EOF?
			if (rc < 0)
				break;
			
			// Add to the buffer
			glue.addLast(buf, 0, rc);
		}
		
		return glue.toByteArray();
	}
	
	/**
	 * Rounds the number of bits to bytes according to the PNG specification.
	 * 
	 * @param __numBits The number of bits.
	 * @return The number of bytes that represent the bits, rounded up.
	 * @since 2022/06/14
	 */
	static int __roundNumBitsToByte(int __numBits)
	{
		// Divide by 8 for bits, flooring... then round up for any other bits
		return (__numBits >>> 3) + (((__numBits & 0b111) == 0) ? 0 : 1);
	}
}
