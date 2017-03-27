// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test;

/**
 * This contains results for tests.
 *
 * @since 2017/03/25
 */
public class TestResult
{
	/** The name of the test. */
	protected final String name;
	
	/**
	 * Initializes the storage for the test result.
	 *
	 * @param __n The name of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/25
	 */
	public TestResult(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
}

