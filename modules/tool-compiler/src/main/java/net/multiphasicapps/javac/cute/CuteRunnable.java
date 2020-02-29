// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.collections.UnmodifiableMap;
import net.multiphasicapps.javac.classtree.Packages;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerLogger;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.LocationAware;
import net.multiphasicapps.javac.MessageType;
import net.multiphasicapps.javac.structure.RuntimeInput;

/**
 * This is the runnable which performs the actual compilation tasks and
 * generates the output code accordingly as needed.
 *
 * @since 2018/03/06
 */
public class CuteRunnable
	implements Runnable
{
	/** Output for the compiler. */
	protected final CompilerOutput out;
	
	/** Logging for the compiler. */
	protected final CompilerLogger log;
	
	/** Options for the compiler. */
	protected final CompilerOptions options;
	
	/** Paths for the compiler. */
	protected final Map<CompilerInputLocation, List<CompilerPathSet>> paths;
	
	/** Input for the compiler. */
	protected final List<CompilerInput> input;
	
	/**
	 * Initializes the compiler.
	 *
	 * @param __out The output for generated classes.
	 * @param __log The output for console logs.
	 * @param __opt The options for the compiler.
	 * @param __paths Input paths for compiler input, implied source code and
	 * class files.
	 * @param __input The input files to compile.
	 * @throws CompilerException If the input or options are not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public CuteRunnable(CompilerOutput __out,
		PrintStream __log, CompilerOptions __opt,
		CompilerPathSet[][] __paths, CompilerInput[] __input)
		throws CompilerException, NullPointerException
	{
		if (__out == null || __log == null || __opt == null ||
			__paths == null || __input == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
		this.log = new CompilerLogger(__log);
		this.options = __opt;
		
		// Copy paths
		Map<CompilerInputLocation, List<CompilerPathSet>> paths =
			new HashMap<>();
		for (CompilerInputLocation cil : CompilerInputLocation.values())
			paths.put(cil, UnmodifiableList.<CompilerPathSet>of(
				Arrays.<CompilerPathSet>asList(
				__paths[cil.ordinal()].clone())));
		this.paths = UnmodifiableMap.<CompilerInputLocation,
			List<CompilerPathSet>>of(paths);
		
		// Copy input
		this.input = UnmodifiableList.<CompilerInput>of(
			Arrays.<CompilerInput>asList(__input.clone()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public void run()
		throws CompilerException
	{
		CompilerLogger log = this.log;
		
		// Load packages and the classes which belong in each package
		Map<CompilerInputLocation, List<CompilerPathSet>> paths = this.paths;
		Packages packages = Packages.loadPackages(
			paths.get(CompilerInputLocation.CLASS),
			paths.get(CompilerInputLocation.SOURCE));
		todo.DEBUG.note("Packages: %s", packages);
		
		// OLDER CODE BELOW
		if (true)
			throw new todo.TODO();
		
		// Setup input which processes input classes
		RuntimeInput ri = new RuntimeInput(
			paths.get(CompilerInputLocation.CLASS),
			paths.get(CompilerInputLocation.SOURCE));
		
		// Run compilation step
		try
		{
			// Process input files to load their structures
			for (CompilerInput ci : this.input)
				ri.processSourceFiles(ci.fileName());
			
			if (true)
				throw new todo.TODO();
		}
		
		// Caught compiler exception so report it and retoss
		catch (CompilerException|InvalidClassFormatException e)
		{
			// Print the root causes of the exception
			Throwable t = e;
			while (t != null)
			{
				// {@squirreljme.error AQ0j Failed to compile.}
				log.message(MessageType.ERROR,
					((t instanceof LocationAware) ?
						(LocationAware)t : null),
					"AQ0j %s", t.getMessage());
				
				// {@squirreljme.error AQ0k Failed to compile. (An exception
				// or error was suppressed)}
				for (Throwable s : t.getSuppressed())
					log.message(MessageType.ERROR,
						((s instanceof LocationAware) ?
							(LocationAware)s : null),
						"AQ0k %s", s.getMessage());
				
				// Keep going down
				t = t.getCause();
			}
			
			// Print trace
			e.printStackTrace();
			
			// Keep it going
			throw e;
		}
	}
	
	/**
	 * Converts a file name to a class name.
	 *
	 * @param __n The file name to convert.
	 * @return The resulting class name.
	 * @throws CompilerException If the name could not be converted to a
	 * class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	static final ClassName __fileToClassName(String __n)
		throws CompilerException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ0l Cannot determine classname of file because
		// it has an unknown suffix. (The file name)}
		String suffix;
		if (!__n.endsWith(".java"))
			if (!__n.endsWith(".class"))
				throw new CompilerException(String.format("AQ0l %s", __n));
			else
				suffix = ".class";
		else
			suffix = ".java";
		
		// Convert path form to class form
		return new ClassName(__n.substring(0, __n.length() - suffix.length()));
	}
}

