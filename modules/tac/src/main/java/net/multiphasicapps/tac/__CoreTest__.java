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
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.midlet.MIDlet;
import cc.squirreljme.runtime.midlet.OverrideActiveMidletRestriction;
import net.multiphasicapps.tool.manifest.JavaManifest;
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
	implements OverrideActiveMidletRestriction, TestInterface
{
	/** {@squirreljme.property test.dump=bool Dump test result manifests?} */
	public static final String DUMP_ACTUAL =
		"test.dump";
	
	/** Final result of the test, used during the test. */
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
	@Deprecated
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
	 * {@inheritDoc}
	 * @since 2020/02/23
	 */
	@Override
	public final TestExecution runExecution(String... __mainargs)
	{
		// Use to name this test
		Class<?> self = this.getClass();
		String classname = self.getName();
		
		// Decode the expected result
		TestResult expected = TestResult.loadForClass(self);
		
		// Read the inputs for the test
		Object[] args = this.__parseInput(self, __mainargs);
		
		// This is the result of the test
		TestResultBuilder runresult = this._runresult;
		
		// Our test result
		TestStatus status = null;
		
		// Run the test, catch any exception to report it
		Object thrown = null;
		try
		{
			// Run the test
			runresult.setReturnValue(this.__runTest(args));
			runresult.setThrownValue((thrown = new __NoExceptionThrown__()));
		}
		
		// Cannot be tested
		catch (UntestableException e)
		{
			// Cannot be tested so it shall fail
			status = TestStatus.UNTESTABLE;
			thrown = e;
		}
		
		// Test failure
		catch (Throwable t)
		{
			// Errors are bad, stop testing and just fail here
			if (t instanceof Error)
				throw (Error)t;
			
			// The test parameter is not valid, so whoops!
			if (t instanceof InvalidTestException)
			{
				// Exception was thrown
				status = TestStatus.TEST_EXCEPTION;
				thrown = t;
			}
			
			// Normal test which threw a possibly valid exception
			else
			{
				runresult.setReturnValue(new __ExceptionThrown__());
				runresult.setThrownValue((thrown = t));
			}
		}
		
		// If the status is not yet known, do a comparison with the results to
		// see if there is a match
		TestResult result = runresult.build();
		if (status == null)
			status = (expected.equals(result) ? TestStatus.SUCCESS :
				TestStatus.FAILED);
		
		// Store the status of the test that just ran
		this._status = status;
		
		// Return the result
		return new TestExecution(status, self, expected, result, thrown);
	}
	
	/**
	 * Runs the specified test using the given main arguments as if it
	 * were a program to be run, if any.
	 *
	 * This method will handle dead-locks and otherwise.
	 *
	 * @param __mainargs The main arguments to the test which allow parameters
	 * to be used accordingly.
	 * @since 2018/10/06
	 */
	public final void runTest(String... __mainargs)
	{
		if (__mainargs == null)
			__mainargs = new String[0];
		
		// Set a watchdog in case our test takes a very long time to execute
		DeadlockTimeout dtimeout = new DeadlockTimeout();
		dtimeout.start();
		
		// Remember the old output stream because it will be replaced with
		// stderr, this way when tests run they do not inadvertently output
		// to standard output. Standard output being printed to will mess up
		// the test results generated at the end of tac-runner
		PrintStream oldout = System.out;
		
		// Replace standard output from the test!
		TestExecution execution;
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
			
			// Execute the test
			execution = this.runExecution(__mainargs);
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
		
		// Dump test result
		try
		{
			if (Boolean.getBoolean(DUMP_ACTUAL))
				execution.result.writeAsManifest(System.err);
		}
		catch (IOException|SecurityException e)
		{
			// Ignore, could not dump it?
		}
		
		// Set output according to the status
		switch (execution.status)
		{
				// Test passed
			case SUCCESS:
				oldout.printf("%s: PASS %s%n",
					execution.testClass, execution.result);
				break;
			
				// Failed test, print results
			case FAILED:
				// Failure notice
				oldout.printf("%s: FAIL %s%n",
					execution.testClass, execution.result);
				
				// Print comparison to show what failed
				execution.expected.printComparison(System.err,
					execution.result);
				break;
			
			case TEST_EXCEPTION:
				// {@squirreljme.error BU0d The test failed to run properly.
				// (The given test)}
				System.err.printf("BU0d %s%n", execution.testClass);
				break;
			
			case UNTESTABLE:
				// {@squirreljme.error BU0c Test could not be ran
				// potentially because a condition was not met. (Test class)}
				System.err.printf("BU0c %s%n", execution.testClass);
				break;
		}
		
		// Print traces of unexpected exceptions
		if (execution.status != TestStatus.FAILED &&
			execution.tossed instanceof Throwable)
			((Throwable)execution.tossed).printStackTrace();
		
		// Stop the watchdog so we do not exit
		dtimeout.expire();
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
}

