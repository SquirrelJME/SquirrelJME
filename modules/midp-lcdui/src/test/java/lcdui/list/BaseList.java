// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.list;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import lcdui.BaseDisplay;

/**
 * Base class for testing LCDUI {@link List}.
 *
 * @since 2020/11/01
 */
public abstract class BaseList
	extends BaseDisplay
{
	/**
	 * Tests on the given list.
	 * 
	 * @param __display The display being tested.
	 * @param __list The list to test on.
	 * @param __type The type of list used.
	 * @param __typeName The type name of the list.
	 * @throws Throwable On any exception.
	 * @since 2020/11/01
	 */
	protected abstract void test(Display __display, List __list, int __type,
		String __typeName)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/11
	 */
	@Override
	public void test(Display __display)
		throws Throwable
	{
		// Every type of list needs to be tested
		for (int i = Choice.EXCLUSIVE; i <= Choice.IMPLICIT; i++)
		{
			String typeName = (i == Choice.EXCLUSIVE ? "exclusive" :
				(i == Choice.IMPLICIT ? "implicit" : "multiple"));
			
			List list = new List("List " + typeName, i);
			
			__display.setCurrent(list);
			
			this.test(__display, list, i, typeName);
		}
	}
}
