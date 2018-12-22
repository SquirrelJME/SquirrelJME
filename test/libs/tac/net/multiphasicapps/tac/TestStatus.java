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
	
	/** End. */
	;
}

