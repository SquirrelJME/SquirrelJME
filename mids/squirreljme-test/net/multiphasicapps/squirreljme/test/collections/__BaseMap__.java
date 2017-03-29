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
import net.multiphasicapps.squirreljme.test.CommonData;
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
		
		Map<String, String> map = this.map;
		boolean ordered = this.ordered;
		
		// Add a bunch of elements
		try
		{
			for (int i = 0;; i++)
			{
				// Need key
				String k = CommonData.__keyValue(false, i);
				if (k == null)
					break;
				
				// Add
				map.put(k, CommonData.__keyValue(true, i));
			}
		}
		catch (Throwable t)
		{
			__r.threw("put", t);
		}
		
		// Put order (significant)
		if (ordered)
			try
			{
				__r.result("putorder", __mapToArray(map));
			}
			catch (Throwable t)
			{
				__r.threw("putorder", t);
			}
	}
	
	/**
	 * Converts a map to an array of strings with key/value pairs.
	 *
	 * @param __m The map to convert.
	 * @return The mapping as key/value pairs in the specified array.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	private static String[] __mapToArray(Map<String, String> __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Map values to entries
		int n = __m.size(), i = 0;
		String[] rv = new String[n];
		for (Map.Entry<String, String> e : __m.entrySet())
			rv[i++] = e.getKey() + "+" + e.getValue();
		
		// Done
		return rv;
	}
}

