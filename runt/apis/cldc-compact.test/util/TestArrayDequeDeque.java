// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.ArrayDeque;

/**
 * Tests array deques.
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
		super(new ArrayDeque<Number>());
	}
}
