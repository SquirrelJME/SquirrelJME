// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package io;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests read line on EOF.
 *
 * @since 2018/12/08
 */
public class TestReadLineEOF
	extends TestSupplier<Integer>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public Integer test()
		throws Throwable
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(
			"Rodents are softly cute!\nSquirrels!\r\nMice!\rAnd rats!".
			getBytes("utf-8")), "utf-8"));
		
		String ln;
		int i = 1;
		while (null != (ln = br.readLine()))
			this.secondary("line-" + (i++), ln);
		
		return i;
	}
}

