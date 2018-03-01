// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This contains results for tests.
 *
 * @since 2017/03/25
 */
public class TestResult
{
	/** The name of the test. */
	protected final TestName name;
	
	/** Test results. */
	protected final Map<String, Object> results =
		new SortedTreeMap<>();
	
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
	 * Checks whether the result contains the specified sub-test.
	 *
	 * @param __n The sub-test to check.
	 * @return {@code true} if the result contains the given sub-test.
	 * @since 2017/03/28
	 */
	public boolean contains(String __n)
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.results.containsKey(__n);
	}
	
	/**
	 * Returns the result that was obtained for the given test.
	 *
	 * @return The result for the test.
	 * @throws NoSuchElementException If there is no result for the given
	 * sub-test.
	 * @throws NullPointerException On null arguments.
	 * @since 2017//03/28
	 */
	public Object get(String __n)
		throws NoSuchElementException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA03 There is no result for the specified
		// sub-test. (The sub-test name)}
		Map<String, Object> results = this.results;
		if (!results.containsKey(__n))
			throw new NoSuchElementException(String.format("BA03 %s", __n));
		return results.get(__n);
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
		
		// Print all results
		Map<String, Object> results = this.results;
		for (Map.Entry<String, Object> e : this.results.entrySet())
		{
			// Name
			__ps.print(this.name);
			__ps.print('#');
			__ps.print(e.getKey());
			__ps.println(" PROF");
			
			// Value
			__ps.print('\t');
			__ps.println(__toString(e.getValue()));
		}
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
		
		// Print all results and compare
		Map<String, Object> results = this.results;
		for (Map.Entry<String, Object> e : this.results.entrySet())
		{
			// Check if this is a pass condition
			String k = e.getKey();
			boolean pass = __exp.contains(k),
				contained = pass;
			Object other;
			if (pass)
				pass = __equals(e.getValue(), (other = __exp.get(k)));
			else
				other = null;
			
			// Name
			__ps.print(this.name);
			__ps.print('#');
			__ps.print(e.getKey());
			__ps.println((pass ? " PASS" : " FAIL"));
			
			// Results (expected and was)
			__ps.print('\t');
			__ps.println((contained ? __toString(other) : "noresult"));
			__ps.print('\t');
			__ps.println(__toString(other));
		}
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/27
	 */
	public void result(String __n, boolean __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/27
	 */
	public void result(String __n, int __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, long __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, float __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, double __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, String __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, boolean[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, byte[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, short[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, char[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, int[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, long[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, float[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, double[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Provides the specified result.
	 *
	 * @param __n The sub-test name.
	 * @param __v The result.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	public void result(String __n, String[] __v)
		throws IllegalStateException, NullPointerException
	{
		__checkResult(__n, __v);
	}
	
	/**
	 * Inidicates that something was thrown.
	 *
	 * If a result was set then it is removed.
	 *
	 * @param __n The sub-test name.
	 * @param __t The value which was thrown.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public void threw(String __n, Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		results.put(__n, new TestException(__t));
	}
	
	/**
	 * Sets the result of the test if it has not been set.
	 *
	 * @param __n The name of the sub-test.
	 * @param __v The result of the test.
	 * @throws IllegalStateException If a result is already set.
	 * @throws NullPointerException If no sub-test was specified.
	 * @since 2017/03/28
	 */
	private void __checkResult(String __n, Object __v)
		throws IllegalStateException, NullPointerException
	{
		// {@squirreljme.error BA04 A result has already been set. (The test
		// name; The sub-test name)}
		Map<String, Object> results = this.results;
		if (results.containsKey(__n))
			throw new IllegalStateException(String.format("BA04 %s %s",
				this.name, __n));
		
		// Set
		results.put(__n, __v);
	}
	
	/**
	 * Checks if the two values are equal to each other.
	 *
	 * @param __a The first value.
	 * @param __b The second value.
	 * @return Whether the values are equal.
	 * @since 2017/03/28
	 */
	private static boolean __equals(Object __a, Object __b)
	{
		if (__a instanceof TestException && __b instanceof TestException)
			return ((TestException)__a).isCompatible((TestException)__b);
		else if (__a instanceof boolean[] && __b instanceof boolean[])
			return Arrays.equals((boolean[])__a, (boolean[])__b);
		else if (__a instanceof byte[] && __b instanceof byte[])
			return Arrays.equals((byte[])__a, (byte[])__b);
		else if (__a instanceof short[] && __b instanceof short[])
			return Arrays.equals((short[])__a, (short[])__b);
		else if (__a instanceof char[] && __b instanceof char[])
			return Arrays.equals((char[])__a, (char[])__b);
		else if (__a instanceof int[] && __b instanceof int[])
			return Arrays.equals((int[])__a, (int[])__b);
		else if (__a instanceof long[] && __b instanceof long[])
			return Arrays.equals((long[])__a, (long[])__b);
		else if (__a instanceof float[] && __b instanceof float[])
			return Arrays.equals((float[])__a, (float[])__b);
		else if (__a instanceof double[] && __b instanceof double[])
			return Arrays.equals((double[])__a, (double[])__b);
		else if (__a instanceof String[] && __b instanceof String[])
			return Arrays.equals((String[])__a, (String[])__b);
		else
			return Objects.equals(__a, __b);
	}
	
	/**
	 * Escapes the specified string.
	 *
	 * @param __v The string to escape.
	 * @return The escaped representation of the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	static String __escapeString(String __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Escape
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __v.length(); i < n; i++)
		{
			char c = __v.charAt(i);
			
			// Encode (pipe is included because it splits)
			// Backslash because it is used for escapes
			// and ! is used for null
			if (c <= ' ' || c >= 0x7E || c == '|' || c == '\\' || c == '!')
			{
				sb.append("\\x");
				sb.append(Character.forDigit(c >>> 4, 16));
				sb.append(Character.forDigit(c & 0xF, 16));
			}
			
			// As-is
			else
				sb.append(c);
		}
		
		return sb.toString();
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
		
		// Boolean array
		else if (__v instanceof boolean[])
		{
			sb.append("bool[]:");
			for (boolean v : (boolean[])__v)
				sb.append((v ? 'T' : 'F'));
		}
		
		// Byte array
		else if (__v instanceof byte[])
			throw new todo.TODO();
		
		// Short array
		else if (__v instanceof short[])
			throw new todo.TODO();
		
		// Character array
		else if (__v instanceof char[])
			throw new todo.TODO();
		
		// Integer array
		else if (__v instanceof int[])
			throw new todo.TODO();
		
		// Long array
		else if (__v instanceof long[])
			throw new todo.TODO();
		
		// Float array
		else if (__v instanceof float[])
			throw new todo.TODO();
		
		// Double array
		else if (__v instanceof double[])
			throw new todo.TODO();
		
		// String array
		else if (__v instanceof String[])
		{
			sb.append("string[]:");
			String[] a = (String[])__v;
			for (int i = 0, n = a.length; i < n; i++)
			{
				// Pipe splits strings
				if (i > 0)
					sb.append('|');
				
				// Use normal output
				String s = a[i];
				if (s == null)
					sb.append("!null");
				else
					sb.append(__escapeString(s));
			}
		}
		
		// Represented exception
		else if (__v instanceof TestException)
		{
			sb.append("throwable:");
			sb.append(__v);
		}
		
		// Strings
		else if (__v instanceof String)
		{
			sb.append("string:");
			sb.append(__escapeString((String)__v));
		}
		
		// Boolean
		else if (__v instanceof Boolean)
		{
			sb.append("bool:");
			sb.append(__v);
		}
		
		// Unknown
		else
			sb.append("unknown");
		
		return sb.toString();
	}
}

