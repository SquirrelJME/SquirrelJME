// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;

/**
 * The contains the result for emulated tests.
 *
 * @since 2020/06/22
 */
public final class EmulatedTestResult
{
	/** Which test is this for? */
	protected final String className;
	
	/** The suite. */
	protected final EmulatedTestSuiteDescriptor suite;
	
	/** The console lines. */
	private final Collection<ConsoleLine> _lines =
		new ArrayList<>(128);
	
	/** The exit code of the test. */
	private int _exitCode =
		Integer.MIN_VALUE;
	
	/** The start time. */
	private long _timeStart =
		Long.MIN_VALUE;
	
	/** The end time. */
	private long _timeEnd =
		Long.MIN_VALUE;
	
	/** The tossed exception, if any. */
	private Throwable _tossed;
	
	/**
	 * Initializes the base for the test result.
	 * 
	 * @param __suite The test suite.
	 * @param __className The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public EmulatedTestResult(EmulatedTestSuiteDescriptor __suite,
		String __className)
		throws NullPointerException
	{
		if (__suite == null || __className == null)
			throw new NullPointerException("NARG");
		
		this.suite = __suite;
		this.className = __className;
	}
	
	/**
	 * Adds a line to the output.
	 * 
	 * @param __line The line to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public void addLine(ConsoleLine __line)
		throws NullPointerException
	{
		if (__line == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._lines.add(__line);
		}
	}
	
	/**
	 * Is this a test failure?
	 * 
	 * @return If this is a test failure.
	 * @since 2020/06/22
	 */
	public final boolean isFailure()
	{
		synchronized (this)
		{
			int exitCode = this._exitCode;
			return exitCode != ExitValueConstants.SUCCESS &&
				exitCode != ExitValueConstants.SKIPPED;
		}
	}
	
	/**
	 * Sends in the test report.
	 * 
	 * @param __results The output results.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public final void report(TestResultProcessor __results)
		throws NullPointerException
	{
		if (__results == null)
			throw new NullPointerException("NARG");
		
		EmulatedTestSuiteDescriptor suite = this.suite;
		String testClass = this.className;
		
		// In SquirrelJME all tests only have a single run method, so the
		// run is the actual class of execution
		EmulatedTestClassDescriptor classy =
			new EmulatedTestClassDescriptor(suite, testClass);
		EmulatedTestMethodDescriptor method =
			new EmulatedTestMethodDescriptor(classy);
		
		synchronized (this)
		{
			// Execution times
			long timeStart = this._timeStart;
			long timeEnd = this._timeEnd;
			
			// Signal start
			__results.started(classy,
				EmulatedTestUtilities.startAt(timeStart, suite));
			__results.started(method,
				EmulatedTestUtilities.startAt(timeStart, classy));
			
			// Print all the output text
			for (ConsoleLine line : this._lines)
				line.report(__results, method);
			
			// Was the test in error?
			int exitCode = this._exitCode;
			if (exitCode != ExitValueConstants.SUCCESS &&
				exitCode != ExitValueConstants.SKIPPED)
			{
				// Was an exception thrown? If not, make a generic one
				Throwable tossed = this._tossed;
				if (tossed == null)
				{
					tossed = new Throwable("Test Failed");
					
					// Make the trace empty as it has no meaning
					tossed.setStackTrace(new StackTraceElement[0]);
				}
				
				// Toss it now
				__results.failure(method.getId(), tossed);
			}
			
			// End execution
			__results.completed(method.getId(),
				EmulatedTestUtilities.passSkipOrFailAt(timeEnd, exitCode));
			__results.completed(classy.getId(),
				EmulatedTestUtilities.passSkipOrFailAt(timeEnd, exitCode));
		}
	}
	
	/**
	 * Returns standard error.
	 * 
	 * @return Standard error.
	 * @since 2020/06/22
	 */
	public OutputStream stdErr()
	{
		return new PipeOutputStream(this, true);
	}
	
	/**
	 * Returns standard output.
	 * 
	 * @return Standard output.
	 * @since 2020/06/22
	 */
	public OutputStream stdOut()
	{
		return new PipeOutputStream(this, false);
	}
	
	/**
	 * Finishes the test with the given exit code.
	 * 
	 * @param __exitCode The exit code.
	 * @param _toss The tossed exception, if any.
	 * @since 2020/06/22
	 */
	public void testFinish(int __exitCode, Throwable _toss)
	{
		synchronized (this)
		{
			if (this._timeEnd != Long.MIN_VALUE)
				throw new IllegalStateException("Test already finished.");
			
			this._timeEnd = System.currentTimeMillis();
			this._exitCode = __exitCode;
			this._tossed = _toss;
		}
	}
	
	/**
	 * Starts the test.
	 * 
	 * @since 2020/06/22
	 */
	public final void testStart()
	{
		synchronized (this)
		{
			if (this._timeStart != Long.MIN_VALUE)
				throw new IllegalStateException("Test already started.");
			
			this._timeStart = System.currentTimeMillis();
		}
	}
}
