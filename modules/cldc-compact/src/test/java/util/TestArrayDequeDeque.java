// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.ArrayDeque;

/**
 * Tests {@link ArrayDeque}.
 *
 * @since 2019/01/20
 */
public class TestArrayDequeDeque
	extends __TestDeque__
{
	/**
	 * Initializes the test.
	 *
	 * @since 2019/01/20
	 */
	public TestArrayDequeDeque()
	{
		super(new ArrayDeque<Integer>(), false);
	}
}
