// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.suite.DependencyInfo;
import cc.squirreljme.jvm.suite.MarkedDependency;
import cc.squirreljme.jvm.suite.MatchResult;
import cc.squirreljme.jvm.suite.SuiteInfo;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Lazily loaded library handling, for later dependency handling.
 *
 * @since 2020/12/29
 */
final class __Libraries__
{
	/** The available libraries. */
	private final List<Library> _libs =
		new LinkedList<>();
	
	/**
	 * Returns the suites which match the given dependency set.
	 *
	 * @param __set The set of dependencies to get.
	 * @param __opt If {@code true} include optional dependencies.
	 * @return Libraries which satisfy the given dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/31
	 */
	public Library[] matchDependencies(DependencyInfo __set, boolean __opt)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		// Extra libraries to obtain
		Library[] from;
		synchronized (this)
		{
			List<Library> libs = this._libs;
			from = libs.<Library>toArray(new Library[libs.size()]);
		}
		
		// Perform the lookup with these specific libraries
		return __Libraries__.__matchDependencies(__set, __opt,
			new LinkedHashSet<Library>(), from);
	}
	
	/**
	 * Returns the suites which match the given dependency set.
	 *
	 * @param __set The set of dependencies to get.
	 * @param __opt If {@code true} include optional dependencies.
	 * @param __alreadyRoved The libraries that we already looked at.
	 * @param __from From which libraries do we take from?
	 * @return Libraries which satisfy the given dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/31
	 */
	static Library[] __matchDependencies(DependencyInfo __set, boolean __opt,
		Set<Library> __alreadyRoved, Library... __from)
		throws NullPointerException
	{
		if (__set == null || __alreadyRoved == null)
			throw new NullPointerException("NARG");
		
		// Clear all optionals if they are not included
		if (!__opt)
			__set = __set.noOptionals();
		
		// No dependencies to search for
		if (__set.isEmpty())
			return new Library[0];
		
		// The returning set
		Set<Library> rv = new LinkedHashSet<>();
		
		// Go through all binaries and attempt to match
		for (Library lib : __from)
		{
			// Determine if this library matches one of dependencies we are
			// looking for
			MatchResult result = __set.match(lib.provided());
			if (!result.hasMatches())
				continue;
			
			// Add this to an existing match so that we do not infinitely
			// recurse through optional and required dependencies depending
			// on the order of their entries. If it was not yet processed
			// then recursively go down to find dependencies of our
			// dependency so it loads properly
			if (__alreadyRoved.add(lib))
				Collections.addAll(rv, __Libraries__.__matchDependencies(
					lib.dependencies(), true, __alreadyRoved, __from));
			
			// Use this as a dependency
			rv.add(lib);
			
			// Use remaining unmatched set
			__set = result.unmatched();
			
			// If the set was emptied then it will never have any more matches
			if (__set.isEmpty())
				break;
		}
		
		// No dependencies were discovered?
		if (rv.isEmpty())
		{
			// We do not want to hard fail if we just have optional
			// dependencies
			if (__opt)
			{
				// Since nothing was found, count the number of optional
				// dependencies which are left
				int numOptionals = 0;
				for (MarkedDependency dependency : __set)
					if (dependency.isOptional())
						numOptionals++;
				
				// If the rest of our items are all optionals then we just
				// return here
				if (numOptionals == __set.count())
					return new Library[0];
			}
			
			/* {@squirreljme.error DG0p Could not locate the suite which
			satisfies the given dependency. (The dependency to look for)} */
			throw new RuntimeException(
				String.format("DG0p %s", __set));
		}
		
		return rv.<Library>toArray(new Library[rv.size()]);
	}
	
	/**
	 * Registers a library for later dependency handling.
	 * 
	 * @param __info The JAR information.
	 * @param __jar The JAR itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	void __register(SuiteInfo __info, JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__info == null || __jar == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._libs.add(new Library(__info, __jar));
		}
	}
}
