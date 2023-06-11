// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.Map;

/**
 * Represents tests that are available.
 *
 * @since 2022/04/22
 */
public final class AvailableTests
{
	/** Candidate tests. */
	public final Map<String, CandidateTestFiles> tests;
	
	/** Is this a single-test? */
	public final boolean isSingle;
	
	/**
	 * Initializes the available tests.
	 * 
	 * @param __tests The tests that are available.
	 * @param __isSingle Is this a single test?
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/22
	 */
	public AvailableTests(Map<String, CandidateTestFiles> __tests,
		boolean __isSingle)
		throws NullPointerException
	{
		if (__tests == null)
			throw new NullPointerException("NARG");
		
		this.tests = __tests;
		this.isSingle = __isSingle;
	}
}
