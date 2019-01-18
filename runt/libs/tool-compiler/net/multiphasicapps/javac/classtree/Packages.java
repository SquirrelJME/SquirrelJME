// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.classtree;

import java.util.Arrays;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is the root of the class hierachy which contains all of the various
 * packages with classes in them.
 *
 * @since 2019/01/17
 */
public final class Packages
{
	/**
	 * Loads packages from the input path sets, from classes and sources and
	 * performing resolution of them.
	 *
	 * @param __css The path sets to iterate through and load packages from.
	 * @return The packages and all of the defined classes within the entire
	 * sets of input.
	 * @throws CompilerException If an input could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/17
	 */
	public static final Packages loadPackages(CompilerPathSet... __css)
		throws CompilerException, NullPointerException
	{
		return Packages.loadPackages(Arrays.<CompilerPathSet>asList(__css));
	}
	
	/**
	 * Loads packages from the input path sets, from classes and sources and
	 * performing resolution of them.
	 *
	 * @param __its The path sets to iterate through and load packages from.
	 * @return The packages and all of the defined classes within the entire
	 * sets of input.
	 * @throws CompilerException If an input could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/17
	 */
	public static final Packages loadPackages(
		Iterable<CompilerPathSet>... __its)
		throws CompilerException, NullPointerException
	{
		if (__its == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

