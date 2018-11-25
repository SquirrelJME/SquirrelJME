// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestBiConsumer;
import net.multiphasicapps.io.Base64Alphabet;
import net.multiphasicapps.io.Base64Decoder;

/**
 * This tests the base 64 decoder.
 *
 * @since 2018/03/06
 */
public class TestBase64Decoder
	extends TestBiConsumer<String, String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06	
	 */
	@Override
	public void test(String __a, String __b)
		throws Throwable
	{
		this.secondary("a", new String(Base64Decoder.decode(__a,
			Base64Alphabet.BASIC), "utf-8"));
		this.secondary("b", new String(Base64Decoder.decode(__b,
			Base64Alphabet.BASIC), "utf-8"));
	}
}

