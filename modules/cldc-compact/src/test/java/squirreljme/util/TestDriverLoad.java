// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.util;

import cc.squirreljme.runtime.cldc.lang.ComplexDriverLoader;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that drivers can be loaded.
 *
 * @since 2021/08/08
 */
public class TestDriverLoad
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/08/08
	 */
	@Override
	public void test()
	{
		NoDriverInterface none =
			ComplexDriverLoader.<NoDriverInterface>factory(
				NoDriverInterface.class);
			
		this.secondary("none", none == null);
		
		FakeDriverInterface found =
			ComplexDriverLoader.<FakeDriverInterface>factory(
				FakeDriverInterface.class);
		
		this.secondary("first",
			found.getClass() == FakeFirstDriverFactory.class);
	}
}
