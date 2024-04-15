// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests string trim.
 *
 * @since 2018/12/05
 */
public class TestStringTrim
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/05
	 */
	@Override
	public void test()
	{
		String cute = "squirrels are cute";
		
		this.secondary("a", cute.trim());
		this.secondary("b", "  \t      squirrels are cute".trim());
		this.secondary("c", "squirrels are cute    \t".trim());
		this.secondary("d", "       \tsquirrels are cute \t    ".trim());
		this.secondary("e", "           ".trim());
		this.secondary("f", "           ".trim());
		this.secondary("g", cute.trim());
	}
}

