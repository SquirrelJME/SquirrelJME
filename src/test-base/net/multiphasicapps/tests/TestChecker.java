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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
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
	
	/** The current sub-test being ran. */
	private volatile String _subtest =
		"?";
	
	/** Ignoring passing tests? */
	private volatile boolean _ignorepass;
	
	/** Ignoring failing tests? */
	private volatile boolean _ignorefail;
	
	/** Ignoring tossed exceptions? */
	private volatile boolean _ignoretoss;
	
	/** The current test number. */
	private volatile int _testid =
	 	1;
	
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
			throw new NullPointerException("NARG");
		
		// Set
		caller = __tc;
		invoker = __ti;
	}
	
	/**
	 * Checks that both objects are equal to each other.
	 *
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @since 2016/03/03
	 */
	public void checkEquals(Object __exp, Object __was)
	{
		// If both are arrays of the same type then use array comparison
		// because otherwise they will only be equal if they refer to the same
		// object. Also this method may be called on unknown values.
		if (__exp instanceof byte[] && __was instanceof byte[])
			checkEquals((byte[])__exp, (byte[])__was);
		
		// Use object comparison instead
		else
			__passFail(Objects.equals(__exp, __was), __exp, __was);
	}
	
	/**
	 * Checks that both byte arrays are equal to each other.
	 *
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @since 2016/03/10
	 */
	public void checkEquals(byte[] __exp, byte[] __was)
	{
		__passFail(Arrays.equals(__exp, __was),
			__byteArrayToString(__exp), __byteArrayToString(__was));
	}
	
	/**
	 * Checks that both integer array are equal to each other.
	 *
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @since 2016/06/18
	 */
	public void checkEquals(int[] __exp, int[] __was)
	{
		__passFail(Arrays.equals(__exp, __was),
			__intArrayToString(__exp), __intArrayToString(__was));
	}
	
	/**
	 * Indicates that an exception was thrown.
	 *
	 * @param __t The exception which was thrown.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public void exception(Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Ignoring?
		if (_ignoretoss)
			return;		
		
		// Get the output stream
		PrintStream ps = caller.printStream();
		
		// Print the name
		ps.print("TOSS ");
		ps.print(invoker.invokerName());
		ps.print('@');
		ps.print(_subtest);
		
		// And the exception
		ps.print(' ');
		ps.println(__t.getClass().getName());
		
		// Is there a message?
		String msg = __t.getMessage();
		if (msg != null)
		{
			// Print Message
			ps.print('\t');
			__escape(ps, msg);
			ps.println();
		}
		
		// Print trace to find where the problem is better
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream st = new PrintStream(baos, true, "utf-8"))
		{
			// Print trace into the stream
			__t.printStackTrace(st);
			
			// Write 
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(
					new ByteArrayInputStream(baos.toByteArray()), "utf-8")))
			{
				for (;;)
				{
					// Get line
					String ln = br.readLine();
					
					// EOF?
					if (ln == null)
						break;
					
					// Escape print it
					ps.print('\t');
					__escape(ps, ln.trim());
					ps.println();
				}
			}
		}
		
		// Fail, but indicate it
		catch (IOException ioe)
		{
			ps.println("\t IOE-DURING-TRACE-PRINT");
		}
	}
	
	/**
	 * Sets whether exceptions are ignored or not.
	 *
	 * @param __v If {@code true} then exceptions are ignored.
	 * @return {@code this}.
	 * @since 2016/03/23
	 */
	public TestChecker setIgnoreException(boolean __v)
	{
		_ignoretoss = __v;
		return this;
	}
	
	/**
	 * Sets whether failing tests are ignored or not.
	 *
	 * @param __v If {@code true} then exceptions are ignored.
	 * @return {@code this}.
	 * @since 2016/03/23
	 */
	public TestChecker setIgnoreFail(boolean __v)
	{
		_ignorefail = __v;
		return this;
	}
	
	/**
	 * Sets whether passing tests are ignored or not.
	 *
	 * @param __v If {@code true} then exceptions are ignored.
	 * @return {@code this}.
	 * @since 2016/03/23
	 */
	public TestChecker setIgnorePass(boolean __v)
	{
		_ignorepass = __v;
		return this;
	}
	
	/**
	 * Sets the name of the sub-test currently used.
	 *
	 * @param __stn The name of the sub-test.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/04
	 */
	public TestChecker setSubTest(String __stn)
		throws NullPointerException
	{
		// Check
		if (__stn == null)
			throw new NullPointerException("NARG");
		
		// Set
		_subtest = __stn;
		_testid = 1;
		
		// Self
		return this;
	}
	
	/**
	 * Escapes the string representation of the object.
	 *
	 * @param __ps The print stream to write to.
	 * @param __v The object to escape.
	 * @throws NullPointerException If no print stream was specified.
	 * @since 2016/03/03
	 */
	private void __escape(PrintStream __ps, Object __v)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Null is always null
		if (__v == null)
		{
			__ps.print("null");
			return;
		}
		
		// Get as a string
		String str = __v.toString();
		
		// Build escaped form
		int n = str.length();
		for (int i = 0; i < n; i++)
		{
			// Get character here
			char c = str.charAt(i);
			
			// If normal ASCII, print it normal
			if (c >= 0x21 && c <= 0x7F)
				__ps.print(c);
			
			// Single slash escape space
			else if (c == 0x20)
				__ps.print("\\ ");
			
			// Newline?
			else if (c == '\n')
				__ps.print("\\n");
			
			// Carriage return?
			else if (c == '\r')
				__ps.print("\\r");
			
			// Tab?
			else if (c == '\t')
				__ps.print("\\t");
			
			// Compact hex form
			else if (c <= 0xFF)
			{
				__ps.print("\\");
				__ps.print(Character.forDigit((c & 0700) >>> 6, 8));
				__ps.print(Character.forDigit((c & 0070) >>> 3, 8));
				__ps.print(Character.forDigit((c & 0007), 8));
			}
			
			// Long character form
			else
			{
				__ps.print("\\u");
				__ps.print(Character.forDigit((c & 0xF000) >>> 12, 16));
				__ps.print(Character.forDigit((c & 0x0F00) >>> 8, 16));
				__ps.print(Character.forDigit((c & 0x00F0) >>> 4, 16));
				__ps.print(Character.forDigit((c & 0x000F), 16));
			}
		}
	}
	
	/**
	 * Prints pass or failure status and the input objects.
	 *
	 * @param __pass If {@code true} then the test passed.
	 * @param __exp The expected value.
	 * @param __was The value that it was.
	 * @since 2016/03/03
	 */
	private void __passFail(boolean __pass, Object __exp,
		Object __was)
	{
		// Ignoring?
		if ((_ignorepass && __pass) || (_ignorefail && !__pass))
			return;
		
		// Get the output stream
		PrintStream ps = caller.printStream();
		
		// Print it
		ps.print((__pass ? "PASS" : "FAIL"));
		ps.print(' ');
		ps.print(invoker.invokerName());
		ps.print('@');
		ps.print(_subtest);
		ps.print('#');
		ps.println(_testid++);
		
		// The expected result
		ps.print('\t');
		__escape(ps, __exp);
		ps.println();
		
		// The actual result
		ps.print('\t');
		__escape(ps, __was);
		ps.println();
	}
	
	/**
	 * Outputs a byte array to a string.
	 *
	 * @param __b The byte array to write.
	 * @return The string representation of the byte array.
	 * @since 2016/03/10
	 */
	private static String __byteArrayToString(byte... __b)
	{
		// If null then there is no valid translation
		if (__b == null)
			return null;
		
		// Is converted to hex
		int n = __b.length;
		char cha[] = new char[Math.max(0, (n * 3) - 1)];
		
		// Convert
		for (int i = 0, x = 0; i < n; i++)
		{
			byte b = __b[i];
			cha[x++] = Character.forDigit((b & 0xF0) >>> 4, 16);
			cha[x++] = Character.forDigit((b & 0x0F), 16);
			
			// Separator to make reading easier
			if (i + 1 < n)
				cha[x++] = '.';
		}
		
		// Make string
		return new String(cha);
	}
	
	/**
	 * Outputs an integer array to a string.
	 *
	 * @param __v The input array.
	 * @return The integer array as a string.
	 * @since 2016/06/18
	 */
	private static String __intArrayToString(int... __v)
	{
		// Is converted to hex
		int n = __v.length;
		char cha[] = new char[Math.max(0, (n * 9) - 1)];
		
		// Convert
		for (int i = 0, x = 0; i < n; i++)
		{
			int v = __v[i];
			cha[x++] = Character.forDigit((v >>> 28) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 24) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 20) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 16) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 12) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 8) & 0xF, 16);
			cha[x++] = Character.forDigit((v >>> 4) & 0xF, 16);
			cha[x++] = Character.forDigit(v & 0xF, 16);
			
			// Separator to make reading easier
			if (i + 1 < n)
				cha[x++] = '.';
		}
		
		// Make string
		return new String(cha);
	}
}

