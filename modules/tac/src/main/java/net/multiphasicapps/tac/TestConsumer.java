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
 * This is a test which takes a single parameter and has no result.
 *
 * @param <A> The first argument type.
 * @since 2018/10/06
 */
public abstract class TestConsumer<A>
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @param __a The first parameter.
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	public abstract void test(A __a)
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
		// If the first parameter is optional, ignore it
		Object testArg;
		if (this instanceof OptionalFirstParameter)
			testArg = (__args.length == 0 ? null : __args[0]);
		
		// {@squirreljme.error BU05 Test takes one parameter.}
		else if (__args.length != 1)
			throw new InvalidTestParameterException("BU05");
		
		// Load first argument
		else
			testArg = __args[0];
		
		// Run the test
		this.test((A)testArg);
		
		// No result is generated
		return new __NoResult__();
	}
}

