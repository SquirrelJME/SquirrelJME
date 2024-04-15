// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	public void test(Display __display, String __param)
		throws Throwable
	{
		// Which type of list is used?
		int listType;
		switch (__param)
		{
			case "EXCLUSIVE":
				listType = Choice.EXCLUSIVE;
				break;
				
			case "IMPLICIT":
				listType = Choice.IMPLICIT;
				break;
				
			case "MULTIPLE":
				listType = Choice.MULTIPLE;
				break;
			
			default:
				throw new IllegalArgumentException(__param);
		}
		
		// Setup and test list
		List list = new List("List " + __param, listType);
		
		__display.setCurrent(list);
		
		this.test(__display, list, listType, __param);
	}
}
