// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This is a test which returns an integer.
 *
 * @since 2019/12/24
 */
public abstract class TestInteger
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @return The result.
	 * @throws Throwable On any thrown exception.
	 * @since 2019/12/24
	 */
	public abstract int test()
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/24
	 */
	@Override
	final Object __runTest(Object... __args)
		throws Throwable
	{
		// {@squirreljme.error BU0e Test does not take any parameters.}
		if (__args.length != 0)
			throw new InvalidTestParameterException("BU0e");
		
		// Run the test
		return Integer.valueOf(this.test());
	}
}

