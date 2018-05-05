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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.NoSuchInputException;

/**
 * This class contians the input for the .
 *
 * @since 2018/05/03
 */
public final class RuntimeInput
{
	/** Output structure information. */
	protected final Structures structures =
		new Structures();
	
	/** The class path. */
	private final CompilerPathSet[] _classpath;
	
	/** The source path. */
	private final CompilerPathSet[] _sourcepath;
	
	/** Files which have been processed. */
	private final Set<String> _didfiles =
		new HashSet<>();
	
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
	 * Prcoesses a single class file.
	 *
	 * @param __fn The class file name to process.
	 * @throws StructureException If the source structure is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public final void processClassFile(String __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Processes a single source file.
	 *
	 * @param __fn The input file to process.
	 * @throws StructureException If the source structure is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public final void processSourceFile(String __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Only process class files once
		Set<String> didfiles = this._didfiles;
		if (didfiles.contains(__fn))
			return;
		didfiles.add(__fn);
		
		// Search for the source file
		CompilerInput ci = null;
		for (CompilerPathSet ps : this._sourcepath)
			try
			{
				if (null != (ci = ps.input(__fn)))
					break;
			}
			catch (NoSuchInputException e)
			{
			}
		
		// {@squirreljme.error AQ52 The specified source file does not
		// exist in the source path. (The source file)}
		if (ci == null)
			throw new StructureException(String.format("AQ52", __fn));
		
		// Need to search for the source file and then process them into
		// syntax then load them into structures
		throw new todo.TODO();
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
	public final void processSourceFiles(String... __fn)
		throws StructureException, NullPointerException
	{
		this.processSourceFiles(Arrays.<String>asList((__fn == null ?
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
	public final void processSourceFiles(Iterable<String> __fn)
		throws StructureException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Process each file
		for (String filename : __fn)
		{
			if (filename == null)
				throw new NullPointerException("NARG");	
			
			this.processSourceFile(filename);
		}
	}
	
	/**
	 * Returns the structures where classes have been read into.
	 *
	 * @return The structures used.
	 * @since 2018/05/05
	 */
	public final Structures structures()
	{
		return this.structures;
	}
}

