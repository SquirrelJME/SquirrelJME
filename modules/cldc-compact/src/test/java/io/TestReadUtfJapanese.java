// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests reading Japanese modified UTF data.
 *
 * @since 2025/02/03
 */
public class TestReadUtfJapanese
	extends TestRunnable
{
	/** The input raw data. */
	private static final byte[] _RAW = new byte[]
		{
			0, 30, -17, -66, -128, -17, -66, -98, -17, -67, -77, -17, -66,
			-99, -17, -66, -101, -17, -67, -80, -17, -66, -124, -17, -66,
			-98, -28, -72, -83, 46, 46, 46
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/03
	 */
	@Override
	public void test()
		throws Throwable
	{
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(TestReadUtfJapanese._RAW)))
		{
			this.secondary("chars", in.readUTF().toCharArray());
		}
	}
}
