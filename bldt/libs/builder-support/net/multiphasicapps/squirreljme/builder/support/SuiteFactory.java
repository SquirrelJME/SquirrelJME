// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteManager;
import javax.microedition.swm.SuiteStateFlag;
import javax.microedition.swm.SuiteType;

/**
 * This manages suites which are available to the build system.
 *
 * @since 2017/12/08
 */
public class SuiteFactory
	implements Runnable
{
	/** The command to execute. */
	protected final String command;
	
	/** The manager for suites, which is required. */
	protected final SuiteManager manager;
	
	/** Arguments to the task command. */
	private final String[] _args;
	
	/**
	 * Initializes the suite factory.
	 *
	 * @param __args The argument to the factory.
	 * @since 2017/12/08
	 */
	public SuiteFactory(String... __args)
	{
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// Obtain the manager because it is possible that there are
		// no permissions to do so
		this.manager = ManagerFactory.getSuiteManager();
		
		// {@squirreljme.error AU0v Expected command for suite operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0v");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * Lists the suites which are available for usage.
	 *
	 * @param __out Where the suite list is output.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public void listSuites(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Print application suites
		SuiteManager manager = this.manager;
		for (Suite s : manager.getSuites(SuiteType.APPLICATION))
			printSuite(__out, s);
		
		// Then print libraries
		for (Suite s : manager.getSuites(SuiteType.LIBRARY))
			printSuite(__out, s);
	}
	
	/**
	 * {@inheritDoc}
	 * @sincd 2017/12/08
	 */
	@Override
	public void run()
	{
		// Load arguments into a queue
		Deque<String> args =
			new ArrayDeque<>(Arrays.<String>asList(this._args));
		
		// Depends on the command
		String command = this.command;
		switch (command)
		{
				// List tasks
			case "ls":
			case "list":
				listSuites(System.out);
				break;
				
				// {@squirreljme.error AU0w The specified suite command is not
				// valid. Valid commands are:
				// ls, list
				// .(The command)}
			default:
				throw new IllegalArgumentException(String.format("AU0w %s",
					command));
		}
	}
	
	/**
	 * Prints the specified suite to the output stream.
	 *
	 * @param __out The stream to print the suite information to.
	 * @param __s The suite to print.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public static void printSuite(PrintStream __out, Suite __s)
		throws NullPointerException
	{
		if (__out == null || __s == null)
			throw new NullPointerException("NARG");
		
		__out.printf("Suite: %s (%s)%n", __s.getName(), __s.getSuiteType());
		
		__out.printf("\tInstalled ? %s%n", __s.isInstalled());
		__out.printf("\tTrusted   ? %s%n", __s.isTrusted());
		__out.printf("\tVendor    : %s%n", __s.getVendor());
		__out.printf("\tVersion   : %s%n", __s.getVersion());
		__out.printf("\tSource URL: %s%n", __s.getDownloadUrl());
		
		__out.println("\tFlags:");
		for (SuiteStateFlag f : SuiteStateFlag.values())
			__out.printf("\t\tFlag %s is %s%n", f,
				(__s.isSuiteState(f) ? "set" : "not set"));
		
		__out.println("\tMIDlets:");
		for (Iterator<String> it = __s.getMIDlets(); it.hasNext();)
			__out.printf("\t\t%s%n", it.next());
		
		__out.println("\tAttributes:");
		for (Iterator<String> it = __s.getAttributes(); it.hasNext();)
		{
			String key = it.next();
			__out.printf("\t\t%s: %s%n", key, __s.getAttributeValue(key));
		}
			
		__out.println("\tDependencies:");
		for (Iterator<Suite> it = __s.getDependencies(); it.hasNext();)
		{
			Suite dep = it.next();
			__out.printf("\t\t%s %s%n", dep.getName(), dep.getVendor());
		}
	}
}

