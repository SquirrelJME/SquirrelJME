// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

/**
 * This represents a single report item.
 *
 * @since 2019/01/23
 */
public final class ReportItem
{
	/** The test name. */
	public final String name;
	
	/** Did this test pass? */
	public final boolean passed;
	
	/** Time spent in this test. */
	public final long duration;
	
	/**
	 * Initializes the report item.
	 *
	 * @param __n The test name.
	 * @param __p Did the test pass?
	 * @param __ns The duration of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public ReportItem(String __n, boolean __p, long __ns)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.passed = __p;
		this.duration = __ns;
	}
}

