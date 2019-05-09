// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import net.multiphasicapps.tac.TestRunnable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tests to ensure that the eldest entry removal works correctly.
 *
 * @since 2019/05/09
 */
public class TestLinkedHashMapEldest
	extends TestRunnable
{
	/** Maximum entries in the map. */
	public static final int MAX_ENTRIES = 10;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public void test()
	{
		Map<Integer, String> map = new EldestMap();
		
		// Add a bunch of values
		for (int i = 0; i < MAX_ENTRIES * 5; i++)
			map.put(i, "Value" + i);
		
		// Array should be in same order!
		this.secondary("ordkeys", map.keySet().toArray());
		this.secondary("ordvals", map.values().toArray());
	}
	
	/**
	 * Map which overrides the eldest entry check.
	 *
	 * @since 2109/05/09
	 */
	public final class EldestMap
		extends LinkedHashMap<Integer, String>
	{
		/** Eldest order. */
		private int _order;
		
		/**
		 * {@inheritDoc}
		 * @since 2019/05/09
		 */
		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, String> __e)
		{
			// Used to make sure the order is correct
			int order = this._order++;
			TestLinkedHashMapEldest.this.secondary(
				"eldest" + order + "key", __e.getKey());
			TestLinkedHashMapEldest.this.secondary(
				"eldest" + order + "val", __e.getValue());
			
			// This comes from the documentation example which specifies that
			// a value should be removed
			return this.size() > MAX_ENTRIES;
		}
	}
}

