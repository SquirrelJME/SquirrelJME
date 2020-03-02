// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
		this.secondary("a", "squirrels are cute".trim());
		this.secondary("b", "  \t      squirrels are cute".trim());
		this.secondary("c", "squirrels are cute    \t".trim());
		this.secondary("d", "       \tsquirrels are cute \t    ".trim());
		this.secondary("e", "           ".trim());
		this.secondary("f", "           ".trim());
	}
}

