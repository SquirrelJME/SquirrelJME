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

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main entry point for the test framework.
 *
 * @since 2017/03/25
 */
public class Main
{
	/**
	 * Main entry point for the test framework.
	 *
	 * @param __args Program arguments.
	 * @since 2017/03/25
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Run the first test directory
		try
		{
			runTestDirectory(new TestName(""));
		}
		
		// {@squirreljme.error BA02 Failed to run the test framework.}
		catch (Throwable e)
		{
			throw new RuntimeException("BA02", e);
		}
	}
	
	/**
	 * Runs the specified test.
	 *
	 * @param __tn The test to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/27
	 */
	public static void runTest(TestName __tn)
		throws NullPointerException
	{
		// Check
		if (__tn == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Test: %s%n", __tn);
		
		// Future result
		TestResult result = new TestResult(__tn);
		
		// The class instance of the test
		TestFunction func;
		try
		{
			Class<?> cl = Class.forName(__tn.prefixed());
			func = (TestFunction)(cl.newInstance());
		}
		
		// Could not locate
		catch (ClassCastException|ClassNotFoundException|
			IllegalAccessException|InstantiationException e)
		{
			e.printStackTrace();
			return;
		}
		
		//
		throw new todo.TODO();
	}
	
	/**
	 * Recursively runs the specified test directory.
	 *
	 * @param __tn The test directory to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/27
	 */
	public static void runTestDirectory(TestName __tn)
		throws IOException, NullPointerException
	{
		// Check
		if (__tn == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Testdir: %s%n", __tn);
		
		// Process test listing
		List<TestName> dirs = new ArrayList<>(),
			tests = new ArrayList<>();
		try (InputStream list = Main.class.getResourceAsStream(
			__tn.resourceList()))
		{
			// No directory listing
			if (list == null)
				return;
			
			// Load in list directory
			StringBuilder sb = new StringBuilder();
			for (boolean eof = false; !eof;)
			{
				// Treat EOF as a newline
				int c = list.read();
				if ((eof = (c < 0)))
					c = '\n';
				
				// Treat CR as LN
				if (c == '\r')
					c = '\n';
				
				// Append test
				if (c == '\n')
				{
					// Handle
					String sub = sb.toString();
					sb.setLength(0);
					if (sub.length() <= 0)
						continue;
					
					// Detect directory of tests
					int n = sub.length();
					boolean isdir = (sub.charAt(n - 1) == '/');
					if (isdir)
						sub = sub.substring(0, n - 1);
					
					// Add to lists
					TestName tn = __tn.resolve(sub);
					if (isdir)
						dirs.add(tn);
					else
						tests.add(tn);
				}
				
				// Otherwise the name of a test or a test directory
				else
					sb.append((char)c);
			}
		}
		
		// {@squirreljme.error BA01 Failed to run the specified test
		// directory due to a read/write error. (The test name)}
		catch (IOException e)
		{
			throw new RuntimeException(String.format("BA01 %s", __tn));
		}
		
		// Process tests directories first
		for (TestName subdir : dirs)
			runTestDirectory(subdir);
		
		// Run individual tests
		for (TestName subtest : tests)
			runTest(subtest);
	}
}

