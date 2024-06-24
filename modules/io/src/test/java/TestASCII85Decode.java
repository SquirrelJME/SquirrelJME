// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ByteIntegerArray;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import net.multiphasicapps.io.ASCII85Decoder;
import net.multiphasicapps.io.StringReader;
import net.multiphasicapps.tac.TestFunction;

/**
 * Test of the ASCII85 Decoder.
 *
 * @since 2024/06/09
 */
public class TestASCII85Decode
	extends TestFunction<String, String>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/06/09
	 */
	@Override
	public String test(String __s)
		throws Throwable
	{
		// Debug
		Debugging.debugNote("Input: %s", __s);
		
		// Decode
		byte[] data;
		try (ASCII85Decoder decoder = new ASCII85Decoder(
			new StringReader(__s)))
		{
			data = StreamUtils.readAll(decoder);
			
			// Debug
			Debugging.debugNote("Buffer: %s",
				new IntegerArrayList(new ByteIntegerArray(data)));
		}
		
		// Decode and use it
		return new String(data, "utf-8");
	}
}
