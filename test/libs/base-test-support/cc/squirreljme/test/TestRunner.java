// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

/**
 * This class is used to run the actual tests.
 *
 * @since 2018/03/05
 */
public final class TestRunner
	implements Runnable
{
	/**
	 * Initializes the test runner.
	 *
	 * @param __args Arguments to the runner.
	 * @param __groups Classes for test groups to run.
	 * @throws NullPointerException If no groups were specified.
	 * @since 2018/03/05
	 */
	public TestRunner(String[] __args, Class<? extends TestGroup>... __groups)
		throws NullPointerException
	{
		if (__groups == null)
			throw new NullPointerException("NARG");
		
		// Use default if unspecified
		if (__args == null)
			__args = new String[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

