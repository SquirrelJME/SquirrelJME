// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Stack;

/**
 * Tests the stack class but solely to make sure that the {@link Vector} it
 * inherits from has not been broken.
 *
 * @since 2019/05/08
 */
public class TestStackAsList
	extends __TestList__
{
	/**
	 * Initializes the test.
	 *
	 * @since 2019/05/08
	 */
	public TestStackAsList()
	{
		super(new Stack<String>());
	}
}

