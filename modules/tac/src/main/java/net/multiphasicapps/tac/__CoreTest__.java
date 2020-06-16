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

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * This is the core test framework which handles reading test information and
 * parameters, it forwards internally to other classes which handle
 * parameters and such.
 *
 * @since 2018/10/06
 */
abstract class __CoreTest__
	implements TestInterface
{
	/** Final result of the test, used during the test. */
	final TestResultBuilder _runResult =
		new TestResultBuilder();
	
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
	 * Runs the test for TestNG.
	 *
	 * @since 2020/02/23
	 */
	@Test
	public final void runForTestNG()
	{
		// Run our execution with the default arguments!
		TestExecution execution = this.runExecution();
		
		// Always print the result
		execution.print(System.err);
		
		// If the test did not pass, throw an exception
		if (execution.status != TestStatus.SUCCESS)
		{
			// Only use as a cause if this is even valid
			Object tossed = execution.tossed;
			Throwable tossedThrown = ((tossed instanceof Throwable) ?
				(Throwable)tossed : null);
			
			// If skippable, try throwing a TestNG skip exception if it exists
			if (execution.status == TestStatus.UNTESTABLE)
				throw new SkipException("SKIPPED", tossedThrown);
			
			// Fail otherwise
			throw new ThrownTestExecution(execution, tossedThrown);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/23
	 */
	@SuppressWarnings("FeatureEnvy")
	@Override
	public final TestExecution runExecution(String... __mainargs)
	{
		// Use to name this test
		Class<?> self = this.getClass();
		
		// Decode the expected result
		Map<String, String> otherKeys = new HashMap<>();
		TestResult expected = TestResult.loadForClass(self, otherKeys);
		
		// Read the inputs for the test
		Object[] args = this.__parseInput(self, __mainargs);
		
		// This is the result of the test
		TestResultBuilder runresult = this._runResult;
		
		// Our test result
		TestStatus status = null;
		
		// Run the test, catch any exception to report it
		Object thrown;
		try
		{
			// Determine the system that the test needs to be on, if one was
			// ever specified in the results
			int vmType = -1;
			String onlyIn = otherKeys.get("only-in");
			if (onlyIn != null)
				switch (onlyIn)
				{
					case "javase":		vmType = VMType.JAVA_SE; break;
					case "springcoat":	vmType = VMType.SPRINGCOAT; break;
					case "summercoat":	vmType = VMType.SUMMERCOAT; break;
				}
			
			// {@squirreljme.error BU0j Test cannot run on a different VM.}
			if (vmType >= 0 && vmType != RuntimeShelf.vmType())
				throw new UntestableException("BU0j " + vmType);
			
			// Run the test
			runresult.setReturnValue(this.__runTest(args));
			runresult.setThrownValue((thrown = new __NoExceptionThrown__()));
		}
		
		// Cannot be tested at all, so must stop here
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
			status = (expected.isSatisfiedBy(result) ? TestStatus.SUCCESS :
				TestStatus.FAILED);
		
		// Return the result
		return new TestExecution(status, self, expected, result, thrown);
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
		this._runResult.putSecondaryValue(__key, __v);
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
		try (InputStream in = __class.getResourceAsStream(
			basename + ".in"))
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
		for (int i = 1;; i++)
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

