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
 * This represents a test which takes one parameter and returns a result.
 *
 * @param <A> The first parameter type.
 * @param <R> The result type.
 * @since 2018/10/06
 */
public abstract class TestFunction<A, R>
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @param __a The first parameter.
	 * @return The result.
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	public abstract R test(A __a)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	final Object __runTest(Object... __args)
		throws Throwable
	{
		// {@squirreljme.error BU04 Test takes one parameter.}
		if (__args.length != 1)
			throw new InvalidTestParameterException("BU04");
		
		// Run the test
		return this.test((A)__args[0]);
	}
}

