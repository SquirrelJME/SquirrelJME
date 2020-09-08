// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.Poking;

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
		// Poke any native VM stuff as it is needed during hosted testing or
		// potential other areas
		Poking.poke();
		
		// {@squirreljme.error BU0b Expected single argument specifying the
		// test to run.}
		if (__args == null || __args.length != 1 || __args[0] == null)
			throw new IllegalArgumentException("BU0b");
		
		// Find the class type
		Class<?> type;
		try
		{
			type = Class.forName(__args[0]);
		}
		catch (ClassNotFoundException e)
		{
			// {@squirreljme.error BU0g Could not find main test class.
			// (The class name)}
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
			// {@squirreljme.error BU0h Could not instantiate the class.
			// (The class name)}
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
			// {@squirreljme.error BU0i Class is not the expected type that
			// it should be. (The class name)}
			throw new IllegalArgumentException("BU0i " + __args[0]);
		}
		
		// Run the class execution
		TestExecution execution = testInstance.runExecution();
		
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
