// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.SortedTreeSet;

/**
 * This class is used to provide access to source code that is available as
 * compilation sources.
 *
 * @since 2017/10/31
 */
public final class SourceManager
{
	/** Sources which are available. */
	private final Map<SourceName, Source> _sources;
	
	/**
	 * Initializes the source code manager.
	 *
	 * @param __roots The root directories which contain namespaces where
	 * source projects are located.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public SourceManager(Path... __roots)
		throws IOException, NullPointerException
	{
		if (__roots == null)
			throw new NullPointerException("NARG");
		
		// Go through all the roots and detect the namespaces which are
		// valid
		Map<ProjectType, Set<Path>> namespaces = new SortedTreeMap<>();
		for (Path p : __roots)
			__scanNamespaces(p, namespaces);
		
		// Debug
		System.err.printf("DEBUG -- Found namespaces %s%n", namespaces);
		
		// Go through all namespaces and initialize any projects which may be
		// within directories
		Map<SourceName, Source> sources = new SortedTreeMap<>();
		for (Map.Entry<ProjectType, Set<Path>> e : namespaces.entrySet())
		{
			ProjectType type = e.getKey();
			
			// Go through path and decode projects
			for (Path p : e.getValue())
				throw new todo.TODO();
		}
		
		// Set
		this._sources = sources;
	}
	
	/**
	 * Obtains the source by the given project name.
	 *
	 * @param __n The name of the source project.
	 * @return The source for the given project.
	 * @throws NoSuchSourceException If no source exists for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Source get(String __n)
		throws NoSuchSourceException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return get(new SourceName(__n));
	}
	
	/**
	 * Obtains the source by the given project name.
	 *
	 * @param __n The name of the source project.
	 * @return The source for the given project.
	 * @throws NoSuchSourceException If no source exists for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Source get(SourceName __n)
		throws NoSuchSourceException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AO0g No such source project exists. (The name
		// of the source project)}
		Source rv = this._sources.get(__n);
		if (rv == null)
			throw new NoSuchSourceException(String.format("AO0g %s", __n));
		return rv;
	}
	
	/**
	 * This scans the given directory for namespace directories and adds them
	 * to the given output path.
	 *
	 * @param __base The base path to scan for namespaces.
	 * @param __out The output map for namespace scans.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	private static final void __scanNamespaces(Path __base,
		Map<ProjectType, Set<Path>> __out)
		throws IOException, NullPointerException
	{
		if (__base == null || __out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

