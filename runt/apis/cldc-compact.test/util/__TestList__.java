// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests that lists work correctly and do all the things they should do.
 *
 * @since 2019/05/08
 */
abstract class __TestList__
	extends TestRunnable
{
	/** The list being tested. */
	protected final List<String> list;
	
	/**
	 * Initializes the test with the list to work with.
	 *
	 * @param __l The list to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/08
	 */
	__TestList__(List<String> __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.list = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public void test()
	{
		List<String> list = this.list;
		
		// Initial list is empty and has zero size
		this.secondary("initialzerosize", list.size());
		this.secondary("initialisempty", list.isEmpty());
		
		// Initial list does not contain value
		this.secondary("initialnocont", list.contains("Cutie squirrels!"));
		
		// Initial list throws IOOB
		try
		{
			this.secondary("initialioob", list.get(29));
		}
		catch (Throwable t)
		{
			this.secondary("initialioob", t);
		}
		
		// Add various entries to the list
		this.secondary("add1", list.add("Hello!"));
		this.secondary("add2", list.add("Squirrels!"));
		this.secondary("add3", list.add("Are!"));
		this.secondary("add4", list.add("Cute!"));
		
		// Must be the correct size
		this.secondary("addempty", list.isEmpty());
		this.secondary("addsize", list.size());
		
		// Remove the second element
		this.secondary("removeindex", list.remove(2));
		
		// Remove by value
		this.secondary("hascutebefore", list.contains("Cute!"));
		this.secondary("removevalue", list.remove("Cute!"));
		this.secondary("hascuteafter", list.contains("Cute!"));
		
		// Remove size
		this.secondary("removeempty", list.isEmpty());
		this.secondary("removesize", list.size());
		
		// Add a bunch of entries
		for (int i = 1; i <= 17; i++)
			this.secondary("add" + i, list.add(Integer.toString(i)));
			
		// Count sizes more
		this.secondary("bulkempty", list.isEmpty());
		this.secondary("bulksize", list.size());
		
		// Add another squirrels, then remove it
		this.secondary("moresquirrels", list.add("Squirrels!"));
		this.secondary("hasmoresquirrels", list.contains("Squirrels!"));
		this.secondary("removemoresquirrels", list.remove("Squirrels!"));
		this.secondary("hasmoresquirrelsstill", list.contains("Squirrels!"));
		
		// Hashcode
		this.secondary("hashcode", list.hashCode());
		
		// Go through and check iteration sequence
		Object[] array = list.toArray();
		ListIterator<String> it = list.listIterator();
		for (int i = 0, n = array.length; i < n; i++)
		{
			// Must be equal
			this.secondary("itequal" + i, Objects.equals(array[i], it.next()));
			
			// Remove some elements
			if (i == 3 || i == 7 || i == 12)
				it.remove();
		}
		
		// The iterator should be at the end
		try
		{
			this.secondary("noneleft", it.next());
		}
		catch (Throwable t)
		{
			this.secondary("noneleft", t);
		}
		
		// Go back down the iterator
		array = list.toArray();
		for (int i = array.length - 1; i >= 0; i--)
		{
			// Must be equal
			this.secondary("previtequal" + i,
				Objects.equals(array[i], it.previous()));
			
			// Remove some elements
			if (i == 3 || i == 7 || i == 12)
				it.remove();
		}
		
		// The iterator should be at the end
		try
		{
			this.secondary("prevnoneleft", it.previous());
		}
		catch (Throwable t)
		{
			this.secondary("prevnoneleft", t);
		}
		
		// As array form
		this.secondary("array",
			list.<String>toArray(new String[list.size()]));
		this.secondary("arrowgrow",
			list.<String>toArray(new String[list.size() / 2]));
		this.secondary("arrayover",
			list.<String>toArray(new String[list.size() + 17]));
		
		// Check string result
		this.secondary("stringform", list.toString());
		
		// Clear and count size
		list.clear();
		this.secondary("clearempty", list.isEmpty());
		this.secondary("clearsize", list.size());
	}
}

