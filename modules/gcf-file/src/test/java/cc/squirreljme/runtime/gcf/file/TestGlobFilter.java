// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.plaf.ListUI;
import net.multiphasicapps.collections.ArrayUtils;
import net.multiphasicapps.collections.UnmodifiableArrayList;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that the glob filter works properly.
 *
 * @since 2025/12/30
 */
public class TestGlobFilter
	extends TestRunnable
{
	/** The names to filter. */
	public static final List<String> NAMES =
		Arrays.asList("apple", "pear", "orange", "banana", "peach", "lemon",
			"pizza");
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public void test()
	{
		this.secondary("orange",
			TestGlobFilter.__filter("orange"));
		this.secondary("px",
			TestGlobFilter.__filter("p**"));
		this.secondary("xeax",
			TestGlobFilter.__filter("*ea***"));
		this.secondary("xrx",
			TestGlobFilter.__filter("**r*"));
		this.secondary("xaxax",
			TestGlobFilter.__filter("*a*a*"));
		this.secondary("xon",
			TestGlobFilter.__filter("*on"));
	}
	
	/**
	 * Filter with the given glob.
	 *
	 * @param __glob The glob.
	 * @return The result.
	 * @since 2025/12/30
	 */
	private static String[] __filter(String __glob)
	{
		List<String> rv = new ArrayList<>();
		Iterator<String> it = new BasicGlobFilter(__glob,
			TestGlobFilter.NAMES.iterator());
		
		while (it.hasNext())
			rv.add(it.next());
		
		return rv.toArray(new String[rv.size()]);
	}
}
