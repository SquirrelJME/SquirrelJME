// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.LinkedList;

/**
 * Tests linked list.
 *
 * @since 2019/05/08
 */
public class TestLinkedList
	extends __TestList__
{
	/**
	 * Initializes the test.
	 *
	 * @since 2019/05/08
	 */
	public TestLinkedList()
	{
		super(new LinkedList<String>());
	}
}
