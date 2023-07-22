// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
		ServiceLoader<ServiceThingy> sl = ServiceLoader.<ServiceThingy>load(
			ServiceThingy.class);
		
		// Initial run
		for (ServiceThingy sv : sl)
			this.secondary("init-" + sv.getClass().getName(), sv.toString());
		
		// Cached run
		for (ServiceThingy sv : sl)
			this.secondary("cache-" + sv.getClass().getName(), sv.toString());
		
		// Clear cache
		sl.reload();
		
		// Should be fresh
		for (ServiceThingy sv : sl)
			this.secondary("again-" + sv.getClass().getName(), sv.toString());
	}
}

