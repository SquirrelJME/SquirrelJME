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
 * This is a test which returns a boolean.
 *
 * @since 2019/12/25
 */
@SquirrelJMEVendorApi
public abstract class TestBoolean
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @return The result.
	 * @throws Throwable On any thrown exception.
	 * @since 2019/12/25
	 */
	@Test
	@SquirrelJMEVendorApi
	public abstract boolean test()
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	@SquirrelJMEVendorApi
	final Object __runTest(Object... __args)
		throws Throwable
	{
		/* {@squirreljme.error BU0f Test does not take any parameters.} */
		if (__args.length != 0)
			throw new InvalidTestParameterException("BU0e");
		
		// Run the test
		return Boolean.valueOf(this.test());
	}
}

