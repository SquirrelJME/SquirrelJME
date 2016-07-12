// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * The class represents an individual test to be ran, it allows for argument
 * input along with output results for potential sub-test areas.
 *
 * @since 2016/07/12
 */
public final class IndividualTest
{
	/** The sub-test name. */
	protected final TestSubName name;
	
	/**
	 * Initializes the individual test with the given sub-name.
	 *
	 * @param __n The sub-test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public IndividualTest(TestSubName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
}

