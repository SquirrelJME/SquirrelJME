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
	 * @since 2018/10/06
	 */
	public abstract void test(A __a);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	final Object __runTest(Object... __args)
	{
		// {@squirreljme.error BU08 Test takes one parameter.}
		if (__args.length != 1)
			throw new InvalidTestParameterException("BU08");
		
		// Run the test
		this.test((A)__args[0]);
		
		// No result is generated
		return new __NoResult__();
	}
}

