// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Hashtable;
import java.util.Map;

/**
 * This is a test for {@link Hashtable}.
 *
 * @since 2019/05/05
 */
public class TestHashtable
	extends TestHashMap
{
	/**
	 * Initializes the test using the base map.
	 *
	 * @since 2019/05/05
	 */
	public TestHashtable()
	{
		this(new Hashtable<Integer, String>());
	}
	
	/**
	 * Initializes the test using the given map.
	 *
	 * @param __m The map to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public TestHashtable(Map<Integer, String> __m)
		throws NullPointerException
	{
		super(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public void test()
	{
		// Run basic HashMap test
		super.test();
		
		// Initialize a map to work with
		Map<Integer, String> map = this.map;
		
		// Null key should fail
		boolean nullkey = false;
		try
		{
			map.put(null, "Hiya!");
		}
		catch (NullPointerException e)
		{
			nullkey = true;
		}
		this.secondary("nullkey", nullkey);
		
		// Null value should fail
		boolean nullval = false;
		try
		{
			map.put(9876, null);
		}
		catch (NullPointerException e)
		{
			nullval = true;
		}
		this.secondary("nullval", nullval);
		
		// Cannot set values via the iterator either
		Map.Entry<Integer, String> ent = map.entrySet().iterator().next();
		boolean nullset = false;
		try
		{
			ent.setValue(null);
		}
		catch (NullPointerException e)
		{
			nullset = true;
		}
		this.secondary("nullset", nullset);
	}
}

