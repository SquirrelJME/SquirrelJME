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
 * This is used to throw the test execution.
 *
 * @since 2020/02/26
 */
@SquirrelJMEVendorApi
public class ThrownTestExecution
	extends RuntimeException
{
	/** The tossed execution. */
	@SquirrelJMEVendorApi
	public final TestExecution execution;
	
	/**
	 * Initializes the exception.
	 *
	 * @param __exec The execution to trace.
	 * @param __cause The cause of it.
	 * @since 2020/02/26
	 */
	@SquirrelJMEVendorApi
	public ThrownTestExecution(TestExecution __exec, Throwable __cause)
	{
		super((__exec == null ? "NULL" : __exec.toString()), __cause);
		
		this.execution = __exec;
	}
}
