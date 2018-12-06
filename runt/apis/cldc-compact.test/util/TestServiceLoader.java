// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.ServiceLoader;
import net.multiphasicapps.tac.TestRunnable;
import util.serviceloader.ServiceThingy;

/**
 * Tests the service loader.
 *
 * @since 2018/12/06
 */
public class TestServiceLoader
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public void test()
	{
		for (ServiceThingy sv : ServiceLoader.<ServiceThingy>load(
			ServiceThingy.class))
			this.secondary(sv.getClass().getName(), sv.toString());
	}
}

