// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * Runs a single test.
 *
 * @since 2020/03/07
 */
public class MainSingleRunner
{
	/**
	 * Main entry point for test running.
	 *
	 * @param __args Program arguments.
	 * @since 2020/03/06
	 */
	public static void main(String... __args)
	{
		/* {@squirreljme.error BU0b Expected single argument specifying the
		test to run.} */
		if (__args == null || __args.length != 1 || __args[0] == null)
			throw new IllegalArgumentException("BU0b");
			
		String singleArg = __args[0];
		
		// Determine if this is a multi-parameter test
		String typeName;
		String multiParameter;
		int iat = singleArg.indexOf('@');
		if (iat >= 0)
		{
			typeName = singleArg.substring(0, iat);
			multiParameter = singleArg.substring(iat + 1);
		}
		
		// Normal test with no multi-parameter
		else
		{
			typeName = singleArg;
			multiParameter = null;
		}
		
		// Find the class type
		Class<?> type;
		try
		{
			type = Class.forName(typeName);
		}
		catch (ClassNotFoundException e)
		{
			/* {@squirreljme.error BU0g Could not find main test class.
			(The class name)} */
			throw new IllegalArgumentException("BU0g " + __args[0]);
		}
		
		// Create instance of it
		Object instance;
		try
		{
			instance = type.newInstance();
		}
		catch (InstantiationException|IllegalAccessException e)
		{
			/* {@squirreljme.error BU0h Could not instantiate the class.
			(The class name)} */
			throw new IllegalArgumentException("BU0h " + __args[0], e);
		}
		
		// Cast the class to the interface for running
		TestInterface testInstance;
		try
		{
			testInstance = (TestInterface)instance;
		}
		catch (ClassCastException e)
		{
			/* {@squirreljme.error BU0i Class is not the expected type that
			it should be. (The class name)} */
			throw new IllegalArgumentException("BU0i " + __args[0]);
		}
		
		// Run the class execution, there might be a multi-parameter which will
		// be passed in accordingly
		TestExecution execution =
			(multiParameter == null ? testInstance.runExecution() :
			testInstance.runExecution(
				DataSerialization.serialize(multiParameter)));
		
		// Print the test execution results
		execution.print(System.err);
		
		// Print the tossed exception so that it is in the trace no matter
		// what (even on pass since it might be a false pass)
		Throwable tossed = execution.tossedAsThrowable();
		if (tossed != null)
			tossed.printStackTrace();
		
		// How do we exit?
		switch (execution.status)
		{
			case SUCCESS:
				System.exit(ExitValueConstants.SUCCESS);
				break;
				
			case FAILED:
			case TEST_EXCEPTION:
				System.exit(ExitValueConstants.FAILURE);
				break;
			
			case UNTESTABLE:
				System.exit(ExitValueConstants.SKIPPED);
				break;
		}
	}
}
