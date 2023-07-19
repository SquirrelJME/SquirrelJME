// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import java.io.IOException;
import java.io.PrintStream;

/**
 * This represents the expected and resultant results of a test.
 *
 * @since 2020/02/23
 */
public final class TestExecution
{
	/** Print the resultant output manifest? */
	public static final String RESULT_MANIFEST =
		"net.multiphasicapps.tac.resultManifest";
	
	/** The status of the test. */
	public final TestStatus status;
	
	/** The class being tested. */
	public final Class<?> testClass;
	
	/** The result of the test. */
	public final TestResult result;
	
	/** The expected results of the test. */
	public final TestResult expected;
	
	/** The exception tossed, if there is one. */
	public final Object tossed;
	
	/**
	 * Initializes the test execution.
	 *
	 * @param __ts The test status.
	 * @param __tc The test class.
	 * @param __res The results of the test.
	 * @param __exp The expected results.
	 * @param __tossed Any exception that was tossed.
	 * @since 2020/02/23
	 */
	public TestExecution(TestStatus __ts, Class<?> __tc,
		TestResult __res, TestResult __exp, Object __tossed)
	{
		this.status = __ts;
		this.testClass = __tc;
		this.result = __res;
		this.expected = __exp;
		this.tossed = __tossed;
	}
	
	/**
	 * Prints the execution results.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/26
	 */
	public final void print(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Write manifest value?
		if (Boolean.getBoolean(TestExecution.RESULT_MANIFEST))
		{
			System.err.println("********************************");
			try
			{
				this.result.writeAsManifest(System.err);
			}
			catch (IOException ignored)
			{
			}
			System.err.println("********************************");
		}
			
		switch (this.status)
		{
				// Test passed
			case SUCCESS:
				__ps.printf("%s: PASS %s%n",
					this.testClass, this.result);
				break;
			
				// Failed test, print results
			case FAILED:
				// Failure notice
				__ps.printf("%s: FAIL %s%n",
					this.testClass, this.result);
				
				// Print comparison to show what failed
				this.result.printComparison(System.err,
					this.expected);
				break;
			
			case TEST_EXCEPTION:
				/* {@squirreljme.error BU0d The test failed to run properly.
				(The given test)} */
				__ps.printf("BU0d %s%n", this.testClass);
				break;
			
			case UNTESTABLE:
				/* {@squirreljme.error BU0c Test could not be ran
				potentially because a condition was not met. (Test class)} */
				__ps.printf("BU0c %s%n", this.testClass);
				break;
		}
		
		// Print traces of unexpected exceptions
		if (this.status != TestStatus.FAILED &&
			this.tossed instanceof Throwable)
			((Throwable)this.tossed).printStackTrace(__ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/26
	 */
	@Override
	public final String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		
		sb.append("status=");
		sb.append(this.status);
		
		sb.append(", testClass=");
		sb.append(this.testClass.getName());
		
		sb.append(", result=");
		sb.append(this.result.toString());
		
		sb.append(", expected=");
		sb.append(this.expected.toString());
		
		Object tossed = this.tossed;
		if (tossed != null)
		{
			sb.append(", tossed=");
			sb.append(tossed.toString());
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * Returns the tossed exception as a {@link Throwable}.
	 *
	 * @return The tossed as a {@link Throwable} or {@code null} if it is not
	 * one.
	 * @since 2020/06/18
	 */
	public final Throwable tossedAsThrowable()
	{
		Object rv = this.tossed;
		return (rv instanceof Throwable ? (Throwable)rv : null);
	}
}
