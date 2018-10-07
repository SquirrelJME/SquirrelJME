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
 * This is a test which takes two parameters and produces no result.
 *
 * @param <A> The first argument type.
 * @param <B> The second argument type.
 * @since 2018/10/06
 */
public abstract class TestBiConsumer<A, B>
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @param __a The first parameter.
	 * @param __b The second parameter.
	 * @since 2018/10/06
	 */
	public abstract void test(A __a, B __b);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	final Object __runTest(Object... __args)
	{
		// {@squirreljme.error BU08 Test takes two parameters.}
		if (__args.length != 2)
			throw new InvalidTestParameterException("BU08");
		
		// Run the test
		this.test((A)__args[0], (B)__args[1]);
		
		// No result is generated
		return new __NoResult__();
	}
}

