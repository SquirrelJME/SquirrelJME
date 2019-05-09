// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.lang.ApiLevel;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This is the core test framework which handles reading test information and
 * parameters, it forwards internally to other classes which handle
 * parameters and such.
 *
 * @since 2018/10/06
 */
abstract class __CoreTest__
	extends MIDlet
{
	/** Final result of the test. */
	final TestResultBuilder _runresult =
		new TestResultBuilder();
	
	/** The status of the test. */
	volatile TestStatus _status =
		TestStatus.NOT_RUN;
	
	/**
	 * Runs the given test with the given arguments and resulting in the
	 * given result.
	 *
	 * @param __args The arguments to the test.
	 * @return The result of the test.
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	abstract Object __runTest(Object... __args)
		throws Throwable;
	
	/**
	 * Tests the minimum API level.
	 *
	 * @param __lv The level to test.
	 * @throws InvalidTestException If the API level is not met.
	 * @since 2019/03/14
	 */
	public final void checkApiLevel(int __lv)
		throws InvalidTestException
	{
		// {@squirreljme.error BU0b Minimum API level has not been met.
		// (The required API level)}
		if (!ApiLevel.minimumLevel(__lv))
			throw new InvalidTestException(String.format("BU0b %x", __lv));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	protected final void destroyApp(boolean __u)
	{
		// Not used
	}
	
	/**
	 * Runs the specified test using the given main arguments, if any.
	 *
	 * @param __mainargs The main arguments to the test which allow parameters
	 * to be used accordingly.
	 * @since 2018/10/06
	 */
	public final void runTest(String... __mainargs)
	{
		if (__mainargs == null)
			__mainargs = new String[0];
		
		// Use to name this test
		Class<?> self = this.getClass();
		String classname = self.getName();
		
		// Decode the expected result
		TestResult expected = TestResult.loadForClass(self);
		
		// Read the inputs for the test
		Object[] args = this.__parseInput(self, __mainargs);
		
		// Remember the old output stream because it will be replaced with
		// stderr, this way when tests run they do not inadvertently output
		// to standard output. Standard output being printed to will mess up
		// the test results generated at the end of tac-runner
		PrintStream oldout = System.out;
		
		// This is the result of the test
		TestResultBuilder runresult = this._runresult;
		
		// Run the test, catch any exception to report it
		Object thrown;
		try
		{
			// Set the output stream to standard error as noted above
			try
			{
				System.setOut(System.err);
			}
			catch (SecurityException e)
			{
				// Ignore, oh well
			}
			
			// Run the test
			runresult.setReturnValue(this.__runTest(args));
			runresult.setThrownValue((thrown = new __NoExceptionThrown__()));
		}
		
		// Cannot be tested
		catch (UntestableException e)
		{
			// {@squirreljme.error BU0d Test could not be ran.
			// (The given test)}
			System.err.printf("BU0d %s%n", classname);
			e.printStackTrace(System.err);
			
			// Cannot be tested so it shall fail
			this._status = TestStatus.UNTESTABLE;
			return;
		}
		
		// Test failure
		catch (Throwable t)
		{
			// The test parameter is not valid, so whoops!
			if (t instanceof InvalidTestException)
			{
				// Exception was thrown
				this._status = TestStatus.TEST_EXCEPTION;
				
				// {@squirreljme.error BU08 The test failed to run properly.
				// (The given test)}
				System.err.printf("BU08 %s%n", classname);
				t.printStackTrace(System.err);
				return;
			}
			
			// Indicate an exception was thrown
			runresult.setReturnValue(new __ExceptionThrown__());
			runresult.setThrownValue((thrown = t));
		}
		finally
		{
			// Restore the old output stream
			try
			{
				System.setOut(oldout);
			}
			catch (SecurityException e)
			{
				// Ignore, things happen
			}
		}
		
		// Return actual result
		TestResult actual = runresult.build();
		
		// The test result is exactly the same!
		boolean passed = expected.equals(actual);
		if (passed)
			oldout.printf("%s: PASS %s%n",
				classname, actual);
		
		// Otherwise print information on what has differed within the test
		// so that bugs may potentially be found
		else
		{
			// Failure notice
			oldout.printf("%s: FAIL %s%n",
				classname, actual);
			
			// Print comparison on another stream so it is not used in output
			expected.printComparison(System.err, actual);
		
			// Print the throwable stack since this was not expected
			if (thrown instanceof Throwable)
				((Throwable)thrown).printStackTrace();
		}
		
		// Set test status
		this._status = (passed ? TestStatus.SUCCESS : TestStatus.FAILED);
	}
	
	/**
	 * Stores a secondary value which can be additionally used as test
	 * comparison.
	 *
	 * @param __key The key to check.
	 * @param __v The value to check.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/10/07
	 */
	public final void secondary(String __key, Object __v)
		throws NullPointerException
	{
		this._runresult.putSecondaryValue(__key, __v);
	}
	
	/**
	 * Runs the MIDlet, parses input test data then runs the test performing
	 * any test work that is needed.
	 *
	 * @since 2018/10/06
	 */
	protected final void startApp()
	{
		// Just forward to run, no main arguments are used at all
		this.runTest((String[])null);
		
		// There is just a single program, so exit with the test status
		System.exit(this._status.ordinal());
	}
	
	/**
	 * Returns the test status.
	 *
	 * @return The test status.
	 * @since 2018/10/07
	 */
	public final TestStatus status()
	{
		return this._status;
	}
	
	/**
	 * Parses the input file for arguments.
	 *
	 * @param __class The class for this test, used to load off manifests.
	 * @param __mainargs Main program arguments.
	 * @return The input arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private Object[] __parseInput(Class<?> __class, String[] __mainargs)
		throws NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		__mainargs = (__mainargs == null ? new String[0] : __mainargs.clone());
		
		// This is used to determine the system property prefix along with
		// which class to use
		String classname = __class.getName();
		
		// Determine the base name which is used for resources
		int ld = classname.lastIndexOf('.');
		String basename = (ld < 0 ? classname : classname.substring(ld + 1));
		
		// The system property prefix is just the class name but lowercased
		String sysprefix = classname.toLowerCase();
		
		// Try to see if there are any arguments in the test file
		JavaManifestAttributes attr = null;
		try (InputStream in = __class.getResourceAsStream(basename + ".in"))
		{
			// If the input exists parse and extract the manifest attributes
			if (in != null)
				attr = new JavaManifest(in).getMainAttributes();
		}
		
		// Ignore
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Read argument values in this order, to allow new ones to be
		// specified accordingly: main arguments, system properties, the
		// default input manifest
		List<Object> rv = new ArrayList<>();
		for (int i = 1; i >= 1; i++)
		{
			String parse;
			
			// Main arguments first
			if (__mainargs != null && (i - 1) < __mainargs.length)
				parse = __mainargs[i - 1];
			
			// Use other default sources instead
			else
			{
				// System property
				String maybe = System.getProperty(sysprefix + "." + i);
				if (maybe != null)
					parse = maybe;
				
				// Otherwise just read a value from the manifest
				else if (attr != null)
					parse = attr.getValue("argument-" + i);
				
				// Nothing
				else
					parse = null;
			}
			
			// Nothing to parse
			if (parse == null)
				break;
			
			// Deserialize the argument value
			rv.add(DataDeserialization.deserialize(parse));
		}
		
		return rv.<Object>toArray(new Object[rv.size()]);
	}
	
	/**
	 * Compares the map of strings to see that they are equal.
	 *
	 * @param __act The actual values.
	 * @param __exp The expected values.
	 * @return If the maps are a match.
	 * @throws InvalidTestParameterException If a throwable is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	private static boolean __equals(Map<String, String> __act,
		Map<String, String> __exp)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__act == null || __exp == null)
			throw new NullPointerException("NARG");
		
		// Compare from the first map
		for (Map.Entry<String, String> a : __act.entrySet())
		{
			String key = a.getKey();
			
			// Second is missing key
			if (!__exp.containsKey(key))
				return false;
			
			// Match value
			if (!TestResult.valueEquals(a.getValue(), __exp.get(key)))
				return false;
		}
		
		// Just scan through the keys in the second map, if any keys are
		// missing then extra keys were added
		for (String k : __exp.keySet())
			if (!__act.containsKey(k))
				return false;
		
		// Is a match
		return true;
	}
}

