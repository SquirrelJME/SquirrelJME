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
 * This is used to provide a default profile for results within tests. Since
 * tests are completely comparison based, if there is no other test sequence to
 * compare to then this interface provides a specially defined.
 *
 * @see TestFunction
 * @since 2017/03/27
 */
public interface TestDefaultFunction
{
	/**
	 * May potentially run a test or just places the expected result of the
	 * test.
	 *
	 * @param __r Where the result of the test is to be stored.
	 * @throws Throwable On any errors or exceptions.
	 * @since 2017/03/27
	 */
	public abstract void defaultRun(TestResult __r)
		throws Throwable;
}

