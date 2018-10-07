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
 * This is a test which returns a result and has no parameters.
 *
 * @param <R> The return type.
 * @since 2018/10/06
 */
public abstract class TestSupplier<R>
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @return The result.
	 * @since 2018/10/06
	 */
	public abstract R test();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	final Object __runTest(Object... __args)
	{
		// {@squirreljme.error BU07 Test does not take any parameters.}
		if (__args.length != 0)
			throw new InvalidTestParameterException("BU07");
		
		// Run the test
		return this.test();
	}
}

