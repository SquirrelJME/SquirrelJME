// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
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
	 * @since 2016/03/03
	 */
	public abstract String invokerName();
	
	/**
	 * This runs tests.
	 *
	 * @param __tc The checker to interface with when running tests.
	 * @since 2016/03/03
	 */
	public abstract void runTests(TestChecker __tc);
}

