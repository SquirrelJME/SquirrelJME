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

/**
 * Tests a basic bi consumer.
 *
 * @since 2018/10/06
 */
public class DoBiConsumer
	extends TestBiConsumer<String, Integer>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public void test(String __s, Integer __i)
	{
		this.secondary("a", __s);
		this.secondary("b", __i);
	}
}

