// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is that status of a test.
 *
 * @since 2018/10/07
 */
public enum TestStatus
{
	/** Success. */
	SUCCESS,
	
	/** Failed. */
	FAILED,
	
	/** Failed due to test exception. */
	TEST_EXCEPTION,
	
	/** Test was not run yet. */
	NOT_RUN,
	
	/** Untestable, so this must be skipped. */
	UNTESTABLE,
	
	/* End. */
	;
}

