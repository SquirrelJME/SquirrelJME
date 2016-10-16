// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * This is an interface which is given so that all tests may be ran.
 *
 * This class is used with the {@link ServiceLoader} to locate tests that
 * should be run in a system.
 *
 * @since 2016/03/03
 */
public interface TestInvoker
{
	/**
	 * Returns the name of this test collection.
	 *
	 * @return The test collection name.
	 * @since 2016/07/12
	 */
	public abstract TestFamily testFamily();
	
	/**
	 * This runs an individual test.
	 *
	 * It is not required for the actual test to verify that the group name is
	 * valid.
	 *
	 * @param __t The test to run.
	 * @throws Throwable On any exception that the test may generate.
	 * @since 2016/03/03
	 */
	public abstract void runTest(IndividualTest __t)
		throws Throwable;
}

