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
 * This is a test which has no input and produces no output, it just runs.
 *
 * @since 2018/10/06
 */
@SquirrelJMEVendorApi
public abstract class TestRunnable
	extends __CoreTest__
{
	/**
	 * Runs the specified test.
	 *
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	@Test
	@SquirrelJMEVendorApi
	public abstract void test()
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	@SquirrelJMEVendorApi
	final Object __runTest(Object... __args)
		throws Throwable
	{
		/* {@squirreljme.error BU08 Test does not take any parameters.} */
		if (__args.length != 0)
			throw new InvalidTestParameterException("BU08");
		
		// Run the test
		this.test();
		
		// No result is generated
		return new __NoResult__();
	}
}

