// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test.collections;

import java.util.Map;
import net.multiphasicapps.squirreljme.test.TestFunction;
import net.multiphasicapps.squirreljme.test.TestResult;

/**
 * Since all maps follow a common interface, this is able to handle multiple
 * tests for classes which implement that interface.
 *
 * @since 2017/03/28
 */
abstract class __BaseMap__
	implements TestFunction
{
	/** The map to test on. */
	protected final Map<String, String> map;
	
	/** Is the test ordered? */
	protected final boolean ordered;
	
	/**
	 * Initializes the base map.
	 *
	 * @param __ordered If the map is ordered then iteration will be used to
	 * make the values are in the correct sequence.
	 */
	__BaseMap__(boolean __ordered, Map<String, String> __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.ordered = __ordered;
		this.map = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/28
	 */
	@Override
	public void run(TestResult __r)
		throws Throwable
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

