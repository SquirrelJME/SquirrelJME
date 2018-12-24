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

import cc.squirreljme.builder.support.dist.DistBuilder;
import cc.squirreljme.builder.support.vm.VMMain;
import cc.squirreljme.builder.support.vmshader.Shader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import net.multiphasicapps.javac.ZipCompilerOutput;

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
	 * Builds the given distributions.
	 *
	 * @param __args The distributions to build.
	 * @since 2018/12/24
	 */
	public void dist(String... __args)
	{
		ProjectManager projectmanager = this.projectmanager;
		
		// Build each requested distribution
		boolean builtone = false;
		for (String d : __args)
		{
			if (d == null)
				continue;
			
			// Get the builder for this
			DistBuilder db = DistBuilder.builder(d);
			
			// Work with a temporary file so nothing breaks
			Path tempfile = null;
			try
			{
				// Need a temporary file
				tempfile = Files.createTempFile("squirreljme-shaded", ".ja_");
				
				// Write to temporary stream first
				try (ZipCompilerOutput zco = new ZipCompilerOutput(
					Files.newOutputStream(tempfile,
					StandardOpenOption.CREATE,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING)))
				{
					db.build(projectmanager, zco);
				}
				
				// Move the file to the output since it was built!
				Files.move(tempfile, Paths.get(
					"squirreljme-" + db.name() + ".zip"),
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// {@squirreljme.error AU19 Could not build the distribution.
			// (The failed distribution)}
			catch (IOException e)
			{
				throw new RuntimeException("AU19 " + d, e);
			}
			
			// Cleanup temp file
			finally
			{
				if (tempfile != null)
					try
					{
						Files.delete(tempfile);
					}
					catch (IOException e)
					{
					}
			}
			
			// Did build one
			builtone = true;
		}
		
		// {@squirreljme.error AU1a No distributions were specified for
		// building.}
		if (!builtone)
			throw new IllegalArgumentException("AU1a");
	}
	
	/**
	 * Builds all distributions.
	 *
	 * @param __args Arguments.
	 * @since 2018/12/24
	 */
	public void distAll(String... __args)
	{
		// {@squirreljme.error AU18 Could not build at least one distribution.}
		RuntimeException fail = new RuntimeException("AU18");
		boolean failed = false;
		
		// Build them one at a time so all of them are made regardless if they
		// all fail or not
		for (String d : DistBuilder.listBuilders())
			try
			{
				this.dist(d);
			}
			catch (RuntimeException t)
			{
				// Suppress it
				failed = true;
				fail.addSuppressed(t);
			}
		
		// If any failed, we throw the exception we made before
		if (failed)
			throw fail;
	}
	
	/**
	 * Lists distributions available to standard output.
	 *
	 * @since 2018/12/24
	 */
	public void distList()
	{
		PrintStream out = System.out;
		for (String d : DistBuilder.listBuilders())
			out.println(d);
	}
	
	/**
	 * Launch program.
	 *
	 * @param __args Arguments to use.
	 * @since 2018/12/22
	 */
	public void launch(String... __args)
	{
		// Load arguments into a queue
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// {@squirreljme.error AU14 Launch of program using a SquirrelJME
		// VM requires a program to be launched.}
		String program = args.pollFirst();
		if (program == null)
			throw new IllegalArgumentException("AU14");
		
		// Run the VM
		VMMain.main(this.projectmanager, program,
			args.<String>toArray(new String[args.size()]));
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
					while (null != (parse = __getopts(":?rjtbRJTB", args)))
						switch (parse[0])
						{
							case "r":
							case "R":
								space = TimeSpaceType.RUNTIME;
								break;
							
							case "j":
							case "J":
								space = TimeSpaceType.JIT;
								break;
								
							case "t":
							case "T":
								space = TimeSpaceType.TEST;
								break;
							
							case "b":
							case "B":
								space = TimeSpaceType.BUILD;
								break;
							
								// {@squirreljme.error AU0f Unknown argument.
								// Usage: build [-R] [-J] [-T] [-B]
								// (projects...);
								// -R: Build for run-time;
								// -J: Build for jit-time;
								// -T: Build for tests;
								// -B: Build for build-time;
								// (The switch)}
							default:
								throw new IllegalArgumentException(
									String.format("AU0f %s", parse[0]));
						}
					
					// Run the builder
					this.build(space,
						args.<String>toArray(new String[args.size()]));
				}
				break;
				
				// Build distribution
			case "dist":
				this.dist(args.<String>toArray(new String[args.size()]));
				break;
			
			case "lsdist":
			case "distlist":
			case "dist-list":
				this.distList();
				break;
				
				// Build all distributions
			case "distall":
			case "dist-all":
				this.distAll(args.<String>toArray(new String[args.size()]));
				break;
				
				// Launch project within a VM
			case "launch":
				this.launch(args.<String>toArray(new String[args.size()]));
				break;
				
				// Perform SDK actions
			case "sdk":
				this.sdk(args.<String>toArray(new String[args.size()]));
				break;
				
				// Perform suite related operations
			case "suite":
				this.suite(args.<String>toArray(new String[args.size()]));
				break;
				
				// Perform task related operations
			case "task":
				this.task(args.<String>toArray(new String[args.size()]));
				break;
				
				// Shade VM provided JAR
			case "vmshade":
			case "vmshaded":
			case "shadevm":
			case "shadedvm":
				this.vmShade(args.<String>toArray(new String[args.size()]));
				break;
				
				// {@squirreljme.error AU0g Unknown command specified.
				// Usage: command (command arguments...);
				// Valid commands are:
				// build, launch, sdk, suite, task, vmshade
				// .(The switch)}
			default:
				throw new IllegalArgumentException(String.format("AU0g %s",
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
		return this.projectmanager.sourceManager(__t);
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
		
		// {@squirreljme.error AU0h Could not initialize the SDK factory.}
		catch (IOException e)
		{
			throw new RuntimeException("AU0h", e);
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
	 * Shaded VM output.
	 *
	 * @param __args Shader arguments.
	 * @since 2018/12/22
	 */
	public void vmShade(String... __args)
	{
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// Bootstrap JAR path, for bootstrap variants (not pure ME)
		Path bootstrapjar = Paths.get("bootsjme", "javase-runtime.jar");
		
		// Build with the bootstrap?
		boolean withbootstrap = false;
		
		// Override the timespace?
		ProjectManager pm = this.projectmanager;
		TimeSpaceType pmts = TimeSpaceType.RUNTIME;
		
		// Determine how shading is to be handled
		String[] parse;
		while (null != (parse = __getopts(":?p:bRJTB", args)))
			switch (parse[0])
			{
					// Bootstrap path
				case "p":
					bootstrapjar = Paths.get(parse[1]);
					break;
					
					// Build with bootstrap JAR
				case "b":
					withbootstrap = true;
					break;
					
					// Use run-time timespace
				case "R":
					pmts = TimeSpaceType.RUNTIME;
					break;
					
					// Use JIT timespace
				case "J":
					pmts = TimeSpaceType.JIT;
					break;
					
					// Use test timespace
				case "T":
					pmts = TimeSpaceType.TEST;
					break;
					
					// Use build timespace
				case "B":
					pmts = TimeSpaceType.BUILD;
					break;
				
					// {@squirreljme.error AU15 Unknown argument.
					// Usage: vmshade [-b JAR] [output];
					// -p: Bootstrap JAR Path, defaults to
					// {@code bootsjme/javase-runtime.jar};
					// -b: Build and include the bootstrap;
					// -R: Build with run-time level (the default);
					// -J: Build with jit-time level;
					// -T: Build with tests level;
					// -B: Build with build-time level;
					// (The switch)}
				case "?":
				default:
					throw new IllegalArgumentException(
						String.format("AU15 %s", parse[0]));
			}
		
		// Output file name, which is optional
		String outfile = args.pollFirst();
		if (outfile == null)
			outfile = "squirreljme.jar";
		
		// Need to write to temporary to not kludge files
		Path tempfile = null;
		try
		{
			// Need a temporary file
			tempfile = Files.createTempFile("squirreljme-shaded", ".ja_");
			
			// Write to temporary stream first
			try (OutputStream out = Files.newOutputStream(tempfile,
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING))
			{
				Shader.shade(pm, pmts, withbootstrap, bootstrapjar, out);
			}
			
			// Move the file to the output since it was built!
			Files.move(tempfile, Paths.get(outfile),
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			// {@squirreljme.error AU16 Could not build the shaded JAR.}
			throw new RuntimeException("AU16", e);
		}
		finally
		{
			// Delete temporary file if there is one
			if (tempfile != null)
				try
				{
					Files.delete(tempfile);
				}
				catch (IOException e)
				{
					// Ignore
				}
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

