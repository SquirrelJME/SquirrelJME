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
	
	/** The directory for the project root (source code). */
	protected final Path sourceroot;
	
	/** Arguments to the builder command. */
	private final String[] _args;
	
	/** The directory for each timespace binaries. */
	private final Path[] _bin;
	
	/** The source managers for each timespace. */
	private final SourceManager[] _sourcemanagers =
		new SourceManager[TimeSpaceType.values().length];
	
	/** Binary managers for each timespace. */
	private final BinaryManager[] _binarymanagers =
		new BinaryManager[TimeSpaceType.values().length];
	
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
		
		// Use default paths based on system properties
		Path sourceroot = Paths.get(
			System.getProperty("cc.squirreljme.builder.root",
				System.getProperty("user.dir", "squirreljme"))),
			binroot = Paths.get(
				System.getProperty(
					"cc.squirreljme.builder.output", "bins")),
			binruntime = null,
			binjit = null,
			bintest = null,
			binbuild = null;
		
		// Allow paths to be modified
		String[] parse;
		while (null != (parse = __getopts(":?s:o:j:t:b:", args)))
			switch (parse[0])
			{
					// Change source code root
				case "-s":
					sourceroot = Paths.get(parse[1]);
					break;
					
					// Change binary output base root
				case "-o":
					binroot = Paths.get(parse[1]);
					break;
					
					// Run-time build path
				case "-r":
					binruntime = Paths.get(parse[1]);
					break;
					
					// JIT-time build path
				case "-j":
					binjit = Paths.get(parse[1]);
					break;
					
					// Test-time build path
				case "-t":
					bintest = Paths.get(parse[1]);
					break;
					
					// Build-time build path
				case "-b":
					binbuild = Paths.get(parse[1]);
					break;
				
					// {@squirreljme.error AU0d Unknown argument.
					// Usage: [-s path] [-o path] [-r path] [-j path] [-b path]
					// command (command arguments...);
					// -s: The project source path;
					// -o: The base directory for binary output;
					// -r: The binary path for the run-time;
					// -j: The binary path for the jit-time;
					// -t: The binary path for the tests;
					// -b: The binary path for the build-time;
					// Valid commands are:
					// build, c, sdk, suite, task, wintercoat
					// .(The switch)}
				default:
					throw new IllegalArgumentException(
						String.format("AU0d %s", parse[1]));
			}
		
		// Fill with defaults if missing
		if (binruntime == null)
			binruntime = binroot.resolve("brun");
		if (binjit == null)
			binjit = binroot.resolve("bjit");
		if (bintest == null)
			bintest = binroot.resolve("btst");
		if (binbuild == null)
			binbuild = binroot.resolve("bbld");
		
		// {@squirreljme.error AU0e No command given.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0e");
		this.command = command;
		
		// Set paths
		this.sourceroot = sourceroot;
		this._bin = new Path[]{binruntime, binjit, bintest, binbuild};
		
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
		if (__t == null)
			throw new NullPointerException("NARG");
		
		int i = __t.ordinal();
		BinaryManager[] binarymanagers = this._binarymanagers;
		BinaryManager rv = binarymanagers[i];
		if (rv == null)
			binarymanagers[i] =
				(rv = new BinaryManager(this._bin[i], sourceManager(__t)));
		return rv;
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
		if (__t == null || __p == null)
			throw new NullPointerException("NARG");
		
		System.err.printf("DEBUG -- %s: %s%n", __t, Arrays.asList(__p));
		
		// Need the binary manager to build these projects
		BinaryManager bm;
		try
		{
			bm = this.binaryManager(__t);
		}
		
		// {@squirreljme.error AU0f Could not obtain the binary manager.}
		catch (IOException e)
		{
			throw new RuntimeException("AU0f", e);
		}
		
		// Get binaries
		int n = __p.length;
		Binary[] bins = new Binary[n];
		for (int i = 0; i < n; i++)
			bins[i] = bm.get(__p[i]);
		
		// Do not return duplicate binaries
		Set<Binary> rv = new LinkedHashSet<>();
		
		// Compile all of the project and return required class path for
		// it to operate
		for (Binary i : bins)
			for (Binary b : bm.compile(i))
				rv.add(b);
		
		// Return the completed set
		return rv.<Binary>toArray(new Binary[rv.size()]);
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
				
				// Generate wintercoat ROM file (the emulator/simulator)
			case "wintercoat":
				this.winterCoat(
					args.<String>toArray(new String[args.size()]));
				break;
			
				// {@squirreljme.error AU0h The specified command is not
				// valid. (The command)}
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
		if (__t == null)
			throw new NullPointerException("NARG");
		
		int i = __t.ordinal();
		SourceManager[] sourcemanagers = this._sourcemanagers;
		SourceManager rv = sourcemanagers[i];
		if (rv == null)
			sourcemanagers[i] =
				(rv = new SourceManagerFactory(this.sourceroot).get(__t));
		return rv;
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
	 * Performs WinterCoat related operations.
	 *
	 * @param __args Arguments to the command.
	 * @since 2018/02/21
	 */
	public void winterCoat(String... __args)
	{
		try
		{
			new WinterCoatFactory(this.binaryManager(TimeSpaceType.RUNTIME),
				__args).run();
		}
		
		// {@squirreljme.error AU12 Could not obtain the binary manager for
		// WinterCoat operations.}
		catch (IOException e)
		{
			throw new RuntimeException("AU12", e);
		}
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
	static String[] __getopts(String __optstring, Deque<String> __q)
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
		String peek = __q.peekFirst();
		
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
			__q.removeFirst();
			
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
					String next = __q.peekFirst();
					if (next == null)
						throw new IllegalArgumentException(
							String.format("AU0i %c", c));
					
					return new String[]{rva, __q.removeFirst()};
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

