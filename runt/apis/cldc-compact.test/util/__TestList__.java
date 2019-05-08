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
import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests that lists work correctly and do all the things they should do.
 *
 * @since 2019/05/08
 */
abstract class __TestList__
	implements TestRunnable
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
		
		throw new todo.TODO();
	}
}

