// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

/**
 * This is a factory which can invoke the build system using a common set
 * of input arguments.
 *
 * @since 2017/11/09
 */
public class BuilderFactory
	implements Runnable
{
	/** The command to execute. */
	protected final String command;
	
	/** The project manager to use. */
	protected final ProjectManager projectmanager;
	
	/** Arguments to the builder command. */
	private final String[] _args;
	
	/**
	 * Initializes the build factory.
	 *
	 * @param __args Program arguments.
	 * @throws IllegalArgumentException If the factory arguments are missing
	 * the primary command.
	 * @since 2017/11/09
	 */
	public BuilderFactory(String... __args)
		throws IllegalArgumentException
	{
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// Parse options and such for the project
		ProjectManager projectmanager = ProjectManager.fromArguments(args);
		this.projectmanager = projectmanager;
		
		// {@squirreljme.error AU0e No command given.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0e");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * Returns the binary manager to use for binary project retrieval.
	 *
	 * @param __t The timespace to build for.
	 * @return The manager for the given timespace.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public BinaryManager binaryManager(TimeSpaceType __t)
		throws IOException, NullPointerException
	{
		return this.projectmanager.binaryManager(__t);
	}
	
	/**
	 * Builds the specified projects.
	 *
	 * @param __t The timespace to use for projects.
	 * @param __p The projects to be built.
	 * @return The binaries which are associated with the given project.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public Binary[] build(TimeSpaceType __t, String... __p)
		throws NullPointerException
	{
		return this.projectmanager.build(__t, __p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/09
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
				// Build the specified project
			case "build":
				{
					TimeSpaceType space = TimeSpaceType.RUNTIME;
					
					// Try to determine the timespace to use, which determines
					// the available projects
					String[] parse;
					while (null != (parse = __getopts(":?rjtb", args)))
						switch (parse[0])
						{
							case "r":
								space = TimeSpaceType.RUNTIME;
								break;
							
							case "j":
								space = TimeSpaceType.JIT;
								break;
								
							case "t":
								space = TimeSpaceType.TEST;
								break;
							
							case "b":
								space = TimeSpaceType.BUILD;
								break;
							
								// {@squirreljme.error AU0g Unknown argument.
								// Usage: build [-r] [-j] [-t] [-b]
								// (projects...);
								// -r: Build for run-time;
								// -j: Build for jit-time;
								// -t: Build for tests;
								// -b: Build for build-time;
								// (The switch)}
							default:
								throw new IllegalArgumentException(
									String.format("AU0g %s", parse[1]));
						}
					
					// Run the builder
					build(space,
						args.<String>toArray(new String[args.size()]));
				}
				break;
				
				// Compile project to C code for bootstrap purposes
			case "c":
				throw new todo.TODO();
				
				// Perform SDK actions
			case "sdk":
				this.sdk(
					args.<String>toArray(new String[args.size()]));
				break;
				
				// Perform suite related operations
			case "suite":
				this.suite(
					args.<String>toArray(new String[args.size()]));
				break;
				
				// Perform task related operations
			case "task":
				this.task(
					args.<String>toArray(new String[args.size()]));
				break;
				
				// {@squirreljme.error AU0h Unknown command specified.
				// Usage: command (command arguments...);
				// Valid commands are:
				// build, c, sdk, suite, task
				// .(The switch)}
			default:
				throw new IllegalArgumentException(String.format("AU0h %s",
					command));
		}
	}
	
	/**
	 * Returns the source manager to use for source code retrieval.
	 *
	 * @param __t The timespace to build for.
	 * @return The manager for the given timespace.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	public SourceManager sourceManager(TimeSpaceType __t)
		throws IOException, NullPointerException
	{
		return this.sourceManager(__t);
	}
	
	/**
	 * Performs SDK related actions.
	 *
	 * @param __args Arguments to the SDK command.
	 * @since 2018/01/27
	 */
	public void sdk(String... __args)
	{
		try
		{
			new SDKFactory(this.binaryManager(TimeSpaceType.JIT), __args).
				run();
		}
		
		// {@squirreljme.error AU0x Could not initialize the SDK factory.}
		catch (IOException e)
		{
			throw new RuntimeException("AU0x", e);
		}
	}
	
	/**
	 * Performs suite related operations.
	 *
	 * @param __args Arguments to the suite command.
	 * @since 2017/12/08
	 */
	public void suite(String... __args)
	{
		new SuiteFactory(__args).run();
	}
	
	/**
	 * Performs task related operations.
	 *
	 * @param __args Arguments to the task commands.
	 * @since 2017/12/05
	 */
	public void task(String... __args)
	{
		new TaskFactory(__args).run();
	}
	
	/**
	 * This is mostly similar to the way the POSIX getopts works but with
	 * some limitations.
	 *
	 * @param __optstring The option string, these are single characters. If a
	 * colon is placed after a character then
	 * @param __q The input command line argument queue.
	 * @return The switch which was parsed, it's character followed by the
	 * argument if there is one; {@code null} is returned when there are no
	 * arguments to parse. If the starting character is a colon then unknown
	 * arguments will return a question mark rather than failing.
	 * @throws IllegalArgumentException If an option requires an argument and
	 * it was not specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/16
	 */
	static String[] __getopts(String __optstring, Queue<String> __q)
		throws IllegalArgumentException, NullPointerException
	{
		if (__optstring == null || __q == null)
			throw new NullPointerException("NARG");
		
		// No more arguments
		if (__q.isEmpty())
			return null;
		
		// Indicate the failed switch
		boolean colonfail = false;
		if (__optstring.startsWith(":"))
		{
			__optstring = __optstring.substring(1);
			colonfail = true;
		}
		int optstrlen = __optstring.length();
		
		// Peek the next argument
		String peek = __q.peek();
		
		// Stop parsing arguments, or nothing to parse
		if (peek.equals("--") || !peek.startsWith("-"))
			return null;
		
		// Parse switch 
		for (int i = 1, n = peek.length(); i < n; i++)
		{
			char c = peek.charAt(i);
			
			// Find the option in the option string
			int odx = __optstring.indexOf(c);
			if (odx < 0)
				return (colonfail ? new String[]{"?", Character.toString(c)} :
					new String[]{"?"});
			
			// Is an argument desired?
			boolean wantsarg = false;
			if (odx + 1 < optstrlen)
				wantsarg = (__optstring.charAt(odx + 1) == ':');
			
			// At this point the argument is consumed
			__q.remove();
			
			// Wants an argument?
			String rva = Character.toString(c);
			if (wantsarg)
			{
				// If this is the last argument then the value is passed
				// in the following switch
				if (i == n - 1)
				{
					// {@squirreljme.error AU0i The specified option argument
					// requires a value set to it. (The option argument)}
					String next = __q.peek();
					if (next == null)
						throw new IllegalArgumentException(
							String.format("AU0i %c", c));
					
					return new String[]{rva, __q.remove()};
				}
				
				// Otherwise it is just anything after the sequence
				else
					return new String[]{rva, peek.substring(i + 1)};
			}
			
			// Otherwise return just that switch
			else
				return new String[]{rva};
		}
		
		// Nothing parsed, indicate failure
		return new String[]{"?"};
	}
}

