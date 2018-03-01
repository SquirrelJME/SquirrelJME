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
 * This is used to describe a test that may be ran.
 *
 * @see TestDefaultFunction
 * @since 2017/03/27
 */
public interface TestFunction
{
	/**
	 * Runs the specified test and places the result in the specified result.
	 *
	 * @param __r Where the result of the test is to be stored.
	 * @throws Throwable On any errors or exceptions.
	 * @since 2017/03/27
	 */
	public abstract void run(TestResult __r)
		throws Throwable;
}

