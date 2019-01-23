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

import java.io.PrintStream;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class contains the partial test report which contains all of the
 * information on which tests have passed and which have failed.
 *
 * @since 2019/01/23
 */
public final class Report
{
	/** Report mapping. */
	private final Map<String, ReportItem> _items =
		new SortedTreeMap<>();
	
	/** Number of tests ran. */
	private volatile int _numtests;
	
	/** Number of passes. */
	private volatile int _numpass;
	
	/** Number of failures. */
	private volatile int _numfail;
	
	/** The time spent running tests. */
	private volatile long _totalns;
	
	/**
	 * Adds a single test to the report.
	 *
	 * @param __n The test name.
	 * @param __p Did the test pass?
	 * @param __ns The duration of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void add(String __n, boolean __p, long __ns)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Pass/fail counts
			this._numtests++;
			if (__p)
				this._numpass++;
			else
				this._numfail++;
			
			// Time spent in test
			this._totalns += __ns;
			
			// Add item
			this._items.put(__n, new ReportItem(__n, __p, __ns));
		}
	}
	
	/**
	 * Generates the test report.
	 *
	 * @param __out The stream to write to.
	 * @param __t The type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void generate(PrintStream __out, ReportType __t)
		throws NullPointerException
	{
		if (__out == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Generate report depending on the format
		switch (__t)
		{
				// JUnit test report
			case JUNIT:
				this.generateJUnit(__out);
				break;
			
				// {@squirreljme.error AI01 Unknown report type.}
			default:
				throw new RuntimeException("AI01 " + __t);
		}
	}
	
	/**
	 * Generates a JUnit test report.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void generateJUnit(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
}

