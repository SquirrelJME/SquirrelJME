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

import java.io.PrintStream;

/**
 * This contains results for tests.
 *
 * @since 2017/03/25
 */
public class TestResult
{
	/** The name of the test. */
	protected final TestName name;
	
	/**
	 * Initializes the storage for the test result.
	 *
	 * @param __n The name of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/25
	 */
	public TestResult(TestName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
	
	/**
	 * Compares this result against another.
	 *
	 * @param __o The result to compare to.
	 * @return The result of the comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public boolean compareResult(TestResult __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Inidicates that something was thrown.
	 *
	 * @param __t The value which was thrown.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public void threw(Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This prints the result to the specified output stream.
	 *
	 * @param __ps The stream to write the result to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public void print(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This prints the result to the specified output stream.
	 *
	 * @param __exp The expected result.
	 * @param __ps The stream to write the result to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public void print(TestResult __exp, PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__exp == null || __ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __v The result.
	 * @since 2017/03/27
	 */
	public void result(int __v)
	{
		throw new todo.TODO();
	}
}

