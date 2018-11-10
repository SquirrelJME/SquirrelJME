// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This is a test which has no input and produces no output, it just runs.
 *
 * @since 2018/10/06
 */
public abstract class TestRunnable
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	public abstract void test()
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	final Object __runTest(Object... __args)
		throws Throwable
	{
		// {@squirreljme.error BU05 Test does not take any parameters.}
		if (__args.length != 0)
			throw new InvalidTestParameterException("BU05");
		
		// Run the test
		this.test();
		
		// No result is generated
		return new __NoResult__();
	}
}

