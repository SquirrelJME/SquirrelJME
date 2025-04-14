// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.junit.Test;

/**
 * This represents a test which takes one parameter and returns a result.
 *
 * @param <A> The first parameter type.
 * @param <R> The result type.
 * @since 2018/10/06
 */
@SquirrelJMEVendorApi
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
	@Test
	@SquirrelJMEVendorApi
	public abstract R test(A __a)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	@SquirrelJMEVendorApi
	final Object __runTest(Object... __args)
		throws Throwable
	{
		/* {@squirreljme.error BU06 Test takes one parameter.} */
		if (__args.length != 1)
			throw new InvalidTestParameterException("BU06");
		
		// Run the test
		return this.test((A)__args[0]);
	}
}

