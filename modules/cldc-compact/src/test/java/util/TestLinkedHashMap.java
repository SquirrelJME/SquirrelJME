// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.tac.TestRunnable;

/**
 * This is used to test that LinkedHashMap works.
 *
 * @since 2019/05/11
 */
public class TestLinkedHashMap
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/11
	 */
	@Override
	public void test()
	{
		// Initialize a map to work with
		Map<Integer, String> map = new LinkedHashMap<>();
		
		// Go through this twice!
		for (char x = 'a'; x != 'c'; x = (char)(x + 1))
		{
			// Initial size and such
			this.secondary(x + "initempty", map.isEmpty());
			this.secondary(x + "initsize", map.size());
			
			// Some initial values to use
			Integer ka = Integer.valueOf(1989),
				kb = Integer.valueOf(2018);
			String va = "Squirrels",
				vb = "Cute";
			
			// Store into the map
			map.put(ka, va);
			
			// Some checks for that initial value
			this.secondary(x + "firstcontained", map.containsKey(ka));
			this.secondary(x + "firstvalue", map.get(ka));
			
			// Has the same equality, despite a different object?
			this.secondary(x + "firstcontainednew",
				map.containsKey(new Integer(1989)));
			this.secondary(x + "firstvaluenew",
				map.get(new Integer(1989)));
			this.secondary(x + "firstsize", map.size());
			this.secondary(x + "firsthassecond", map.containsKey(kb));
			
			// Add second value
			map.put(kb, vb);
			
			// Check for second value
			this.secondary(x + "secondcontained", map.containsKey(kb));
			this.secondary(x + "secondvalue", map.get(kb));
			this.secondary(x + "secondsize", map.size());
			
			// Replace first value
			this.secondary(x + "replaceput", map.put(ka, vb));
			
			// Checks for replaced value
			this.secondary(x + "replacevalue", map.get(ka));
			this.secondary(x + "replacesecondvalue", map.get(kb));
			this.secondary(x + "replacesize", map.size());
			
			// Add in a bunch of values
			for (int i = 0; i < 128; i++)
				map.put(i, "" + i);
			
			// Contents
			this.secondary(x + "contentsa", map.toString());
			
			// Hash map
			this.secondary(x + "hashcode", map.hashCode());
			
			// Check the size
			this.secondary(x + "dumpsize", map.size());
			
			// Search for entry by iterator
			for (Iterator<Map.Entry<Integer, String>> it =
				map.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<Integer, String> e = it.next();
				
				// Matches first value
				if (ka.equals(e.getKey()))
				{
					this.secondary(x + "ithasfirst", e.getValue());
					
					// Remove it
					it.remove();
				}
			}
			
			// Must not have first value
			this.secondary(x + "itafterhasfirst", map.containsKey(ka));
			this.secondary(x + "itputfirstagain", map.put(ka, vb));
			
			// Remove second value
			this.secondary(x + "removesecond", map.remove(kb));
			this.secondary(x + "removesize", map.size());
			
			// Add new value
			map.put(-1, "Adorable");
			
			// Check that
			this.secondary(x + "hasanother", map.get(-1));
			this.secondary(x + "hasanothersize", map.size());
			
			// Contents
			this.secondary(x + "contentsb", map.toString());
			
			// Store into the map again
			map.put(ka, va);
			
			// Reused values
			this.secondary(x + "reusedcontained", map.containsKey(ka));
			this.secondary(x + "reusedvalue", map.get(ka));
			this.secondary(x + "reusedcontainednew",
				map.containsKey(new Integer(1989)));
			this.secondary(x + "reusedvaluenew",
				map.get(new Integer(1989)));
			this.secondary(x + "reusedsize", map.size());
			this.secondary(x + "reusedhassecond", map.containsKey(kb));
			
			
			// Contents
			this.secondary(x + "contentsc", map.toString());
		}
	}
}

