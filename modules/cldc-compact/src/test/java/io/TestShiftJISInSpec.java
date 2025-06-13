// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Decoder;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests a specific set of Shift-JIS characters.
 *
 * @since 2025/06/04
 */
public class TestShiftJISInSpec
	extends TestRunnable
{
	/** The input sequence. */
	public static final byte[] INPUT =
		new byte[]{0x5C, 0x7E, (byte)0x81, 0x5C, (byte)0x81, 0x5F};
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/06/04
	 */
	@Override
	public void test()
		throws Throwable
	{
		Decoder decoder = CodecFactory.decoder("shift-jis");
		
		int[] result = new int[4];
		int at = 0;
		for (int i = 0, n = TestShiftJISInSpec.INPUT.length; i < n;)
		{
			// Decode sequence
			int decode = decoder.decode(TestShiftJISInSpec.INPUT, i,
				n - i);
			
			// Stop? No more characters can be decoded
			if (decode < 0)
				break;
			
			// Record the result
			result[at++] = (decode & 0xFFFF);
			i += (decode >>> 16);
		}
		
		// Give the result
		this.secondary("decoded", result);
	}
}
