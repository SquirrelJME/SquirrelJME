// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.collections;

import cc.squirreljme.test.CommonData;
import cc.squirreljme.test.TestFunction;
import cc.squirreljme.test.TestResult;
import java.util.Map;
import java.util.Objects;

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
		int lasti = 0;
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
				lasti = i;
			}
		}
		catch (Throwable t)
		{
			__r.threw("1put", t);
		}
		
		// Put order (significant)
		if (ordered)
			try
			{
				__r.result("1putorder", __mapToArray(map));
			}
			catch (Throwable t)
			{
				__r.threw("1putorder", t);
			}
		
		// Get every element, in reverse order
		try
		{
			boolean[] res = new boolean[lasti + 1];
			String a, b, k;
			for (int i = lasti; i >= 0; i--)
			{
				k = CommonData.__keyValue(false, i);
				res[i] = Objects.equals(
					(a = CommonData.__keyValue(true, i)),
					(b = map.get(k)));
			}
			__r.result("2get", res);
		}
		catch (Throwable t)
		{
			__r.threw("2get", t);
		}
		
		// Get order (for access order in LinkedHashMap)
		if (ordered)
			try
			{
				__r.result("2getorder", __mapToArray(map));
			}
			catch (Throwable t)
			{
				__r.threw("2getorder", t);
			}
		
		// Remove
		try
		{
			// Count before remove
			__r.result("3countbeforeremove", map.size());
			
			// Perform the remove
			int removecount = (lasti + 1) / 2, at = 0;
			boolean[] res = new boolean[removecount + 1];
			String a, b, k;
			for (int i = lasti; i >= 0; i -= 2)
			{
				k = CommonData.__keyValue(false, i);
				res[at++] = Objects.equals(
					(a = CommonData.__keyValue(true, i)),
					(b = map.remove(k)));
			}
			__r.result("3remove", res);
			
			// Count after remove
			__r.result("3countafterremove", map.size());
		}
		catch (Throwable t)
		{
			__r.threw("3remove", t);
		}
		
		// Remove order
		if (ordered)
			try
			{
				__r.result("3removeorder", __mapToArray(map));
			}
			catch (Throwable t)
			{
				__r.threw("3removeorder", t);
			}
		
		// Put every element back into the map again
		try
		{
			// Count before more put
			__r.result("4countbeforeputmore", map.size());
			
			// Put
			for (int i = 0;; i++)
			{
				// Need key
				String k = CommonData.__keyValue(false, i);
				if (k == null)
					break;
				
				// Add
				map.put(k, CommonData.__keyValue(true, i));
			}
			
			// Count after more put
			__r.result("4countafterputmore", map.size());
		}
		catch (Throwable t)
		{
			__r.threw("4putmore", t);
		}
		
		// Put order (significant)
		if (ordered)
			try
			{
				__r.result("4putmoreorder", __mapToArray(map));
			}
			catch (Throwable t)
			{
				__r.threw("4putmoreorder", t);
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

