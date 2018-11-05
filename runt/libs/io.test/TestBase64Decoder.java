// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestFunction;

import net.multiphasicapps.io.Base64Alphabet;
import net.multiphasicapps.io.Base64Decoder;

/**
 * This tests the base 64 decoder.
 *
 * @since 2018/03/06
 */
public class TestBase64Decoder
	extends TestFunction<String, String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06	
	 */
	@Override
	public String test(String __i)
		throws Throwable
	{
		return new String(Base64Decoder.decode(__i, Base64Alphabet.BASIC,
			true), "utf-8");
	}
}

