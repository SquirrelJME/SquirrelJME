// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.WeakHashMap;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests {@link WeakHashMap}.
 *
 * @since 2023/02/09
 */
public class TestWeakHashMap
	extends TestHashMap
{
	/**
	 * Initializes the test.
	 * 
	 * @since 2023/02/09
	 */
	public TestWeakHashMap()
	{
		super(new WeakHashMap<Integer, String>());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/02/09
	 */
	@Override
	public void test()
	{
		// Call super test for common hash map details
		super.test();
	}
}
