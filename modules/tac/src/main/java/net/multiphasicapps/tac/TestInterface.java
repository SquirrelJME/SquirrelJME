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

/**
 * This is an interface for anything which is something that can be tested
 * within the SquirrelJME test framework.
 *
 * @since 2020/02/23
 */
@SquirrelJMEVendorApi
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface TestInterface
{
	/**
	 * Runs the test.
	 *
	 * @param __mainargs Arguments to the test.
	 * @return The execution result of the test.
	 * @since 2020/02/23
	 */
	TestExecution runExecution(String... __mainargs);
}
