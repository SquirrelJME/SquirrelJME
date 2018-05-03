// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This class contians the input for the .
 *
 * @since 2018/05/03
 */
public final class RuntimeInput
{
	/** The class path. */
	private final CompilerPathSet[] _classpath;
	
	/** The source path. */
	private final CompilerPathSet[] _sourcepath;
	
	/**
	 * Initializes the runtime input.
	 *
	 * @param __class The class path.
	 * @param __src The source path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public RuntimeInput(CompilerPathSet[] __class, CompilerPathSet[] __src)
		throws NullPointerException
	{
		this(Arrays.<CompilerPathSet>asList((__class == null ?
			new CompilerPathSet[0] : __class)),
			Arrays.<CompilerPathSet>asList((__src == null ?
			new CompilerPathSet[0] : __src)));
	}
	
	/**
	 * Initializes the runtime input.
	 *
	 * @param __class The class path.
	 * @param __src The source path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public RuntimeInput(Iterable<CompilerPathSet> __class,
		Iterable<CompilerPathSet> __src)
		throws NullPointerException
	{
		if (__class == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Check classpath
		List<CompilerPathSet> classes = new ArrayList<>();
		for (CompilerPathSet p : __class)
			if (p == null)
				throw new NullPointerException("NARG");
			else
				classes.add(p);
		
		// Check sources
		List<CompilerPathSet> sources = new ArrayList<>();
		for (CompilerPathSet p : __src)
			if (p == null)
				throw new NullPointerException("NARG");
			else
				sources.add(p);
		
		this._classpath = classes.<CompilerPathSet>toArray(
			new CompilerPathSet[classes.size()]);
		this._sourcepath = sources.<CompilerPathSet>toArray(
			new CompilerPathSet[sources.size()]);
	}
	
	/**
	 * Processes source code files and loads their required structure
	 * information performing basic compilation of them.
	 *
	 * @param __fn The source file name to process.
	 * @throws StructureException If the parsed structures are not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public final void processSources(String... __fn)
		throws StructureException, NullPointerException
	{
		this.processSources(Arrays.<String>asList((__fn == null ?
			new String[0] : __fn)));
	}
	
	/**
	 * Processes source code files and loads their required structure
	 * information performing basic compilation of them.
	 *
	 * @param __fn The source file name to process.
	 * @throws StructureException If the parsed structures are not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public final void processSources(Iterable<String> __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

