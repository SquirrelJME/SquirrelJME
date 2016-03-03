// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test;

import java.util.Objects;

/**
 * This is a checker for tests, tests which are run invoke this to make sure
 * that tests operate correctly.
 *
 * @since 2016/03/03
 */
public class TestChecker
{
	/** The test caller. */
	protected final TestCaller caller;
	
	/** The test being invoked. */
	protected final TestInvoker invoker;	
	
	/**
	 * This initializes the test checker.
	 *
	 * @param __tc The test caller.
	 * @param __ti The test invoker.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	public TestChecker(TestCaller __tc, TestInvoker __ti)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __ti == null)
			throw new NullPointerException();
		
		// Set
		caller = __tc;
		invoker = __ti;
	}
	
	/**
	 * Checks that both objects are equal to each other.
	 *
	 * @param __name The name of the test.
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @since 2016/03/03
	 */
	public void checkEquals(String __name, Object __exp, Object __was)
	{
		__passFail(Objects.equals(__exp, __was), __name, __exp, __was);
	}
	
	/**
	 * Escapes the string representation of the object.
	 *
	 * @param __v The object to escape.
	 * @return The escaped string form.
	 * @since 2016/03/03
	 */
	private String __escape(Object __v)
	{
		// Null is always null
		if (__v == null)
			return "null";
		
		// Get as a string
		String str = __v.toString();
		
		// Build escaped form
		StringBuilder sb = new StringBuilder();
		int n = str.length();
		for (int i = 0; i < n; i++)
		{
			// Get character here
			char c = str.charAt(i);
			
			// If normal ASCII, print it normal
			if (c >= 0x21 && c <= 0x7F)
				sb.append(c);
			
			// Compact hex form
			else if (c <= 0xFF)
			{
				sb.append("\\");
				sb.append(Character.forDigit((c & 0700) >>> 6, 8));
				sb.append(Character.forDigit((c & 0070) >>> 3, 8));
				sb.append(Character.forDigit((c & 0007), 8));
			}
			
			// Long character form
			else
			{
				sb.append("\\u");
				sb.append(Character.forDigit((c & 0xF000) >>> 12, 16));
				sb.append(Character.forDigit((c & 0x0F00) >>> 8, 16));
				sb.append(Character.forDigit((c & 0x00F0) >>> 4, 16));
				sb.append(Character.forDigit((c & 0x000F), 16));
			}
		}
		
		// Done
		return sb.toString();
	}
	
	/**
	 * Prints pass or failure status and the input objects.
	 *
	 * @param __pass If {@code true} then the test passed.
	 * @param __name The name of the test.
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	private void __passFail(boolean __pass, String __name, Object __exp,
		Object __was)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException();
		
		// Print it
		caller.printStream().printf("%4s %s: %s %s%n",
			(__pass ? "PASS" : "FAIL"), __name,
			__escape(__exp), __escape(__was));
	}
}

