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
	
	/** The result of the test. */
	private volatile Object _result;
	
	/** Has a result been set? */
	private volatile boolean _set;
	
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
	 * Returns the result that was obtained.
	 *
	 * @return The result.
	 * @since 2017//03/28
	 */
	public Object get()
	{
		return this._result;
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
		
		__ps.print(this.name);
		__ps.print(" PROF ");
		__ps.println((this._set ? __toString(this._result) : "noresult"));
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
		
		__ps.print(this.name);
		__ps.print(' ');
		__ps.print((compareResult(__exp) ? "PASS" : "FAIL"));
		__ps.print(' ');
		__ps.print((this._set ? __toString(this._result) : "noresult"));
		__ps.print(' ');
		__ps.println((__exp._set ? __toString(__exp._result) : "noresult"));
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @since 2017/03/27
	 */
	public void result(int __v)
		throws IllegalStateException
	{
		__checkResult(__v);
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
		
		// Set
		this._set = true;
		this._result = __t;
	}
	
	/**
	 * Sets the result of the test if it has not been set.
	 *
	 * @param __v The result of the test.
	 * @throws IllegalStateException If a result is already set.
	 * @since 2017/03/28
	 */
	private void __checkResult(Object __v)
		throws IllegalStateException
	{
		// {@squirreljme.error BA03 A result has already been set. (The test
		// name)}
		if (this._set)
			throw new IllegalStateException(String.format("BA03 %s",
				this.name));
		
		// Set
		this._set = true;
		this._result = __v;
	}
	
	/**
	 * Encodes the specified object as a string.
	 *
	 * @param __v The object to encode.
	 * @return The string representation of the object.
	 * @since 2017/03/28
	 */
	private static String __toString(Object __v)
	{
		// No object
		if (__v == null)
			return "null";
		
		StringBuilder sb = new StringBuilder();
		
		// Numbers
		if (__v instanceof Number)
		{
			// Prefix type
			if (__v instanceof Integer)
				sb.append("int:");
			else if (__v instanceof Long)
				sb.append("long:");
			else if (__v instanceof Float)
				sb.append("float:");
			else if (__v instanceof Double)
				sb.append("double:");
			else
				sb.append("number:");
			
			// values use the same representation
			sb.append(__v);
		}
		
		// Strings
		else if (__v instanceof String)
		{
			sb.append("string:");
			String v = (String)__v;
			for (int i = 0, n = v.length(); i < n; i++)
			{
				char c = v.charAt(i);
				
				// Encode
				if (c <= ' ' || c >= 0x7E)
				{
					sb.append("\\x");
					sb.append(Character.forDigit(c >>> 4, 16));
					sb.append(Character.forDigit(c & 0xF, 16));
				}
				
				// As-is
				else
					sb.append(c);
			}
		}
		
		// Unknown
		else
			sb.append("unknown");
		
		return sb.toString();
	}
}

