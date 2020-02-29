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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is the root of the class hierachy which contains all of the various
 * packages with classes in them.
 *
 * @since 2019/01/17
 */
public final class Packages
{
	/** The mapping of available packages. */
	private final Map<String, Package> _packages;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the package tree.
	 *
	 * @param __m The input map.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/18
	 */
	public Packages(Map<String, Map<String, Unit>> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Translate everything
		Map<String, Package> packages = new SortedTreeMap<>();
		for (Map.Entry<String, Map<String, Unit>> e : __m.entrySet())
		{
			// Make sure there are no nulls
			String k = e.getKey();
			Map<String, Unit> v = e.getValue();
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			packages.put(k, new Package(k, v));
		}
		
		this._packages = packages;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/19
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this._packages.toString()));
		
		return rv;
	}
	
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
		
		// Packages and classes in each package
		Map<String, Map<String, Unit>> rv = new HashMap<>();
		
		// Iterate through everything and load accordingly
		for (Iterable<CompilerPathSet> it : __its)
			for (CompilerPathSet s : it)
				for (CompilerInput i : s)
				{
					String fn = i.fileName();
					
					// Extract directory (package) and the base name
					String dirname, basename, ident;
					int ls = fn.lastIndexOf('/');
					if (ls >= 0)
					{
						dirname = fn.substring(0, ls);
						basename = fn.substring(ls + 1);
					}
					else
					{
						dirname = "";
						basename = fn;
					}
					
					// Is source file?
					boolean issource;
					
					// Ignore anything with slashes in it, since those are not
					// valid Java files
					if (fn.indexOf('-') >= 0)
						continue;
					
					// Source code file to parse
					else if (fn.endsWith(".java"))
					{
						issource = true;
						ident = basename.substring(0, basename.length() - 5);
					}
					
					// Class file to parse
					else if (fn.endsWith(".class"))
					{
						issource = false;
						ident = basename.substring(0, basename.length() - 6);
					}
					
					// Unknown, ignore
					else
						continue;
					
					// Determine the package the piece belongs in
					Map<String, Unit> pkg = rv.get(dirname);
					if (pkg == null)
						rv.put(dirname, (pkg = new HashMap<>()));
					
					// Make a unit for the class
					pkg.put(ident, (issource ? new SourceUnit(i) :
						new BinaryUnit(i)));
				}
		
		return new Packages(rv);
	}
}

