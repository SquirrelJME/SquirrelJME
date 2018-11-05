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

import net.multiphasicapps.tac.TestRunnable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Tests that {@link HashMap} works properly.
 *
 * @since 2018/10/07
 */
public class TestHashMap
	extends TestRunnable
{
	/** The map to test on. */
	protected final Map<Integer, String> map;
	
	/**
	 * Initializes the test using the base map.
	 *
	 * @since 2018/11/05
	 */
	public TestHashMap()
	{
		this(new HashMap<Integer, String>());
	}
	
	/**
	 * Initializes the test using the given map.
	 *
	 * @param __m The map to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/05
	 */
	public TestHashMap(Map<Integer, String> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.map = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public void test()
	{
		// Initialize a map to work with
		Map<Integer, String> map = this.map;
		
		// Initial size and such
		this.secondary("initempty", map.isEmpty());
		this.secondary("initsize", map.size());
		
		// Some initial values to use
		Integer ka = Integer.valueOf(1989),
			kb = Integer.valueOf(2018);
		String va = "Squirrels",
			vb = "Cute";
		
		// Store into the map
		map.put(ka, va);
		
		// Some checks for that initial value
		this.secondary("firstcontained", map.containsKey(ka));
		this.secondary("firstvalue", map.get(ka));
		
		// Has the same equality, despite a different object?
		this.secondary("firstcontainednew",
			map.containsKey(new Integer(1989)));
		this.secondary("firstvaluenew",
			map.get(new Integer(1989)));
		this.secondary("firstsize", map.size());
		this.secondary("firsthassecond", map.containsKey(kb));
		
		// Add second value
		map.put(kb, vb);
		
		// Check for second value
		this.secondary("secondcontained", map.containsKey(kb));
		this.secondary("secondvalue", map.get(kb));
		this.secondary("secondsize", map.size());
		
		// Replace first value
		this.secondary("replaceput", map.put(ka, vb));
		
		// Checks for replaced value
		this.secondary("replacevalue", map.get(ka));
		this.secondary("replacesecondvalue", map.get(kb));
		this.secondary("replacesize", map.size());
		
		// Add in a bunch of values
		for (int i = 0; i < 128; i++)
			map.put(i, "" + i);
		
		// Check the size
		this.secondary("dumpsize", map.size());
		
		// Search for entry by iterator
		for (Iterator<Map.Entry<Integer, String>> it =
			map.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Integer, String> e = it.next();
			
			// Matches first value
			if (ka.equals(e.getKey()))
			{
				this.secondary("ithasfirst", e.getValue());
				
				// Remove it
				it.remove();
			}
		}
		
		// Must not have first value
		this.secondary("itafterhasfirst", map.containsKey(ka));
		this.secondary("itputfirstagain", map.put(ka, vb));
		
		// Remove second value
		this.secondary("removesecond", map.remove(kb));
		this.secondary("removesize", map.size());
		
		// Add new value
		map.put(-1, "Adorable");
		
		// Check that
		this.secondary("hasanother", map.get(-1));
		this.secondary("hasanothersize", map.size());
		
		// Empty the map
		map.clear();
		
		// Check that
		this.secondary("clearsize", map.size());
		
		// Store into the map again
		map.put(ka, va);
		
		// Reused values
		this.secondary("reusedcontained", map.containsKey(ka));
		this.secondary("reusedvalue", map.get(ka));
		this.secondary("reusedcontainednew",
			map.containsKey(new Integer(1989)));
		this.secondary("reusedvaluenew",
			map.get(new Integer(1989)));
		this.secondary("reusedsize", map.size());
		this.secondary("reusedhassecond", map.containsKey(kb));
	}
}

