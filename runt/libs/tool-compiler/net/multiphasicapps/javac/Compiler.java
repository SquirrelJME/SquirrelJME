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

/**
 * This interfaced is used to describe a compiler which is used to compile
 * Java source code into Java class files.
 *
 * Where the compilation log is initially output is undefined and unspecified
 * and it may be to standard error, a file on the disk, or not output at all.
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
public interface Compiler
{
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
	public abstract boolean addInput(CompilerInput __i)
		throws CompilerException, NullPointerException;
	
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
	public abstract Runnable compile(CompilerOutput __o)
		throws CompilerException, NullPointerException;
	
	/**
	 * Returns all of the input which has been specified for compilation.
	 *
	 * @return An array containing all of the input for the compiler.
	 * @throws CompilerException If this could not be obtained.
	 * @since 2017/11/28
	 */
	public abstract CompilerInput[] input()
		throws CompilerException;
	
	/**
	 * Returns the path set which is used for the given location.
	 *
	 * @param __l The location to get the path set for.
	 * @return The current path set used for the given location.
	 * @throws CompilerException If there was an error removing it.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public abstract CompilerPathSet location(CompilerInputLocation __l)
		throws CompilerException, NullPointerException;
	
	/**
	 * Returns the options which are currently specified for this compiler.
	 *
	 * @return The current compiler options.
	 * @throws CompilerException If the options could not be obtained.
	 * @since 2017/11/28
	 */
	public abstract CompilerOptions options()
		throws CompilerException;
	
	/**
	 * Removes the specified input file so that it is not compiled.
	 *
	 * @param __i The input to remove.
	 * @return If the input was removed or not.
	 * @throws CompilerException If there was an issue removing the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public abstract boolean removeInput(CompilerInput __i)
		throws CompilerException, NullPointerException;
	
	/**
	 * Sets the specified location to the given path set.
	 *
	 * @param __l The locatioin to set.
	 * @param __s The path set to use instead.
	 * @return The path set which was previously specified.
	 * @throws CompilerException If it could not be set for some reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public abstract CompilerPathSet setLocation(CompilerInputLocation __l,
		CompilerPathSet __s)
		throws CompilerException, NullPointerException;
	
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
	public abstract CompilerOptions setOptions(CompilerOptions __o)
		throws CompilerException, NullPointerException;
}

