// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.tac.TestRunnable;

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
	
	/** Eldest order. */
	int _order;
	
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
		{
			// Used for ordering
			int order = TestLinkedHashMapEldest.this._order++;
			this.secondary("eldest" + order + "add", i);
			
			// Put in value
			map.put(i, "Value" + i);
		}
		
		// Array should be in same order!
		this.secondary("ordkeys",
			map.keySet().<Integer>toArray(new Integer[map.size()]));
		this.secondary("ordvals",
			map.values().<String>toArray(new String[map.size()]));
	}
	
	/**
	 * Map which overrides the eldest entry check.
	 *
	 * @since 2109/05/09
	 */
	public final class EldestMap
		extends LinkedHashMap<Integer, String>
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/09
		 */
		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, String> __e)
		{
			// Used to make sure the order is correct
			int order = TestLinkedHashMapEldest.this._order++;
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

