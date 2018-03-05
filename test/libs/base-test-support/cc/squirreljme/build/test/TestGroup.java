// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.build.test;

/**
 * This represents a group of sub-tests which may be run together.
 *
 * @since 2018/03/05
 */
public abstract class TestGroup
{
	/**
	 * Initializes the test group with the specified sub-tests.
	 *
	 * @param __subs Sub-tests which belong to this group.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public TestGroup(Class<? extends SubTest>... __subs)
		throws NullPointerException
	{
		if (__subs == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

