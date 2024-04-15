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
 * This is that status of a test.
 *
 * @since 2018/10/07
 */
@SquirrelJMEVendorApi
public enum TestStatus
{
	/** Success. */
	@SquirrelJMEVendorApi
	SUCCESS,
	
	/** Failed. */
	@SquirrelJMEVendorApi
	FAILED,
	
	/** Failed due to test exception. */
	@SquirrelJMEVendorApi
	TEST_EXCEPTION,
	
	/** Test was not run yet. */
	@SquirrelJMEVendorApi
	NOT_RUN,
	
	/** Untestable, so this must be skipped. */
	@SquirrelJMEVendorApi
	UNTESTABLE,
	
	/* End. */
	;
}

