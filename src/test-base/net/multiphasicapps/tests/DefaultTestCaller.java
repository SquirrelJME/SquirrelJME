// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This is the default test caller which handles command line arguments and
 * outputs test results to standard output.
 *
 * @since 2016/07/12
 */
public class DefaultTestCaller
	extends TestCaller
{
	/** Test results output. */
	protected final PrintStream output;
	
	/**
	 * Initializes the default test caller with the given output and input
	 * arguments.
	 *
	 * @param __ps The output for test results.
	 * @param __args The arguments to the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public DefaultTestCaller(PrintStream __ps, String... __args)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		output = __ps;
		
		// Handle some arguments?
		if (__args != null)
			for (String arg : __args)
				if (arg != null)
					switch (arg)
					{
							// Print usage
						case "-?":
						case "-help":
						case "--help":
							__usage();
							
							// {@squirreljme.error AG02 Help was printed.}
							throw new IllegalArgumentException("AG02");
						
							// Ignore passes
						case "-ip":
							setOption(TestOption.IGNORE_PASS);
							break;
							
							// Ignore failures
						case "-if":
							setOption(TestOption.IGNORE_FAIL);
							break;
							
							// Ignore notes
						case "-in":
							setOption(TestOption.IGNORE_NOTE);
							break;
							
							// Ignore tossed exceptions
						case "-ie":
						case "-it":
						case "-ix":
							setOption(TestOption.IGNORE_EXCEPTION);
							break;
						
							// Unknown, treat as test to run
						default:
							// {@squirreljme.error AG01 Unknown command line
							// switch. (The unknown switch)}
							if (arg.startsWith("-"))
								throw new IllegalArgumentException(
									String.format("AG01 %s", arg));
							
							// Add match otherwise
							super.addMatcher(new TestMatcher(arg));
							break;
					}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	protected void finishedTest(IndividualTest __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through all results
		PrintStream output = this.output;
		for (TestResult result : __t.results())
		{
			// Print status first
			output.print(result.status());
			output.print(' ' );
			
			// Print test name and information
			output.print(__t.groupName());
			output.print('@');
			output.print(__t.subName());
			output.print('#');
			output.println(result.fragmentName());
			
			// Print data points associated with a given test
			Object[] data = result.data();
			int nd = data.length;
			for (int i = 0; i < nd; i++)
			{
				// Print header
				output.print("@@@ ");
				output.println(i + 1);
				
				// Print data in escaped form
				__escape(output, data[i]);
				output.println();
			}
			
			// Is there an associated exception?
			// Print trace to find where the problem is better than guessing
			Throwable t = result.exception();
			if (t != null)
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream st = new PrintStream(baos, true, "utf-8"))
				{
					// Print trace into the stream
					t.printStackTrace(st);
			
					// Write 
					try (BufferedReader br = new BufferedReader(
						new InputStreamReader(
							new ByteArrayInputStream(baos.toByteArray()),
								"utf-8")))
					{
						for (;;)
						{
							// Get line
							String ln = br.readLine();
					
							// EOF?
							if (ln == null)
								break;
					
							// Escape print it
							output.print('\t');
							__escape(output, ln.trim());
							output.println();
						}
					}
				}
		
				// Fail, but indicate it
				catch (IOException ioe)
				{
					output.println("\t IOE-DURING-TRACE-PRINT");
				}
		}
	}
	
	/**
	 * Returns the target print stream.
	 *
	 * @return The target print stream.
	 * @since 2016/03/03
	 */
	public final PrintStream printStream()
	{
		return output;
	}
	
	/**
	 * Escapes the string representation of the object. This is so that any
	 * parsers that parse the pass/failure state need not worry about special
	 * characters, especially if the input includes a newline or similar.
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
	 * Runs all tests by default using the command line interface.
	 *
	 * @param __args Program arguments.
	 * @since 2016/03/03
	 */
	public static void main(String... __args)
	{
		// Create a new test caller
		DefaultTestCaller tc = new DefaultTestCaller(System.out, __args);
		
		// Run tests
		tc.runTests();
	}
	
	/**
	 * Prints how the test caller is used.
	 *
	 * @since 2016/05/04
	 */
	private static void __usage()
	{
		// Much faster
		PrintStream o = System.err;
		
		throw new Error("TODO");
	}
}

