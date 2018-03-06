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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.collections.UnmodifiableMap;
import net.multiphasicapps.collections.UnmodifiableSet;
import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;

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
	protected final PrintStream log;
	
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
		this.log = __log;
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
		throw new todo.TODO();
	}
}

