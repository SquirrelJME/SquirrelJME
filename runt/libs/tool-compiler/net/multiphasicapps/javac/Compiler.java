// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This interface is used to describe a compiler which is used to compile
 * Java source code into Java class files.
 *
 * Where the compilation log is initially output is undefined and unspecified
 * and it may be to standard error, a file on the disk, or not output at all.
 *
 * This class must be thread safe.
 *
 * This class is mutable, when {@link #compile()} is called all the options
 * will be passed to the compiler instance and any instance of this class may
 * be used again or modified as such.
 *
 * There should be a default set of options specified for the compiler.
 *
 * By default all locations should have {@link EmptyPathSet} as their initial
 * instance.
 *
 * @since 2017/11/28
 */
public abstract class Compiler
{
	/** Internal lock. */
	private final Object _lock =
		new Object();
	
	/** Input for the compiler. */
	private final Set<CompilerInput> _input =
		new LinkedHashSet<>();
	
	/** Paths for each location. */
	private final Map<CompilerInputLocation, CompilerPathSet[]> _locations =
		new HashMap<>();
	
	/**
	 * Base initialization.
	 *
	 * @since 2017/11/29
	 */
	{
		// Setup path sets with default locations
		Map<CompilerInputLocation, CompilerPathSet[]> locs = this._locations;
		for (CompilerInputLocation l : CompilerInputLocation.values())
			locs.put(l, new CompilerPathSet[]{EmptyPathSet.instance()});
	}
	
	/**
	 * Adds the specified input to be compiled by the compiler.
	 *
	 * @param __i The input file to add to the compiler.
	 * @return If the input was added and it resulted in the change of the
	 * inputs, that is this follows the same semantics as
	 * {@link java.util.Collection#add(Object)}.
	 * @throws CompilerException If the input could not be added.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final boolean addInput(CompilerInput __i)
		throws CompilerException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		synchronized (this._lock)
		{
			return this._input.add(__i);
		}
	}
	
	/**
	 * Creates an instance of {@link Runnable} which can be used to perform
	 * compilation.
	 *
	 * @param __o The output where the result of compilation is placed.
	 * @return {@code true} if compilation has succeeded.
	 * @throws CompilerException If any parameter used for compilation is
	 * not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final Runnable compile(CompilerOutput __o)
		throws CompilerException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the input which has been specified for compilation.
	 *
	 * @return An array containing all of the input for the compiler.
	 * @throws CompilerException If this could not be obtained.
	 * @since 2017/11/28
	 */
	public final CompilerInput[] input()
		throws CompilerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the path set which is used for the given location.
	 *
	 * @param __l The location to get the path set for.
	 * @return The current path set used for the given location.
	 * @throws CompilerException If there was an error removing it.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final CompilerPathSet[] location(CompilerInputLocation __l)
		throws CompilerException, NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the options which are currently specified for this compiler.
	 *
	 * @return The current compiler options.
	 * @throws CompilerException If the options could not be obtained.
	 * @since 2017/11/28
	 */
	public final CompilerOptions options()
		throws CompilerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes the specified input file so that it is not compiled.
	 *
	 * @param __i The input to remove.
	 * @return If the input was removed or not.
	 * @throws CompilerException If there was an issue removing the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final boolean removeInput(CompilerInput __i)
		throws CompilerException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the specified location to the given path set.
	 *
	 * @param __l The locatioin to set.
	 * @param __s The path sets to use.
	 * @return The path set which was previously specified.
	 * @throws CompilerException If it could not be set for some reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final CompilerPathSet[] setLocation(CompilerInputLocation __l,
		CompilerPathSet... __s)
		throws CompilerException, NullPointerException
	{
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Defensive check for nulls
		__s = __s.clone();
		for (CompilerPathSet s : __s)
			if (s == null)
				throw new NullPointerException("NARG");
		
		Map<CompilerInputLocation, CompilerPathSet[]> locs = this._locations;
		synchronized (this._lock)
		{
			return locs.put(__l, __s).clone();
		}
	}
	
	/**
	 * Sets the compilation options that are to be used during compilation.
	 *
	 * @param __o The options to set.
	 * @return The old compilation options.
	 * @throws CompilerException If the options are not valid for this
	 * compiler.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public final CompilerOptions setOptions(CompilerOptions __o)
		throws CompilerException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

