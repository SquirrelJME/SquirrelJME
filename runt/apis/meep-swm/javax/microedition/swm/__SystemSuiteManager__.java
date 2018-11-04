// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.swm.ByteArrayJarStreamSupplier;
import cc.squirreljme.runtime.swm.DependencyInfo;
import cc.squirreljme.runtime.swm.MatchResult;
import cc.squirreljme.runtime.swm.ProvidedInfo;
import cc.squirreljme.runtime.cldc.asm.SuiteAccess;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This class manages the bridge for the suite manager to the native program
 * manager.
 *
 * @since 2017/12/08
 */
final class __SystemSuiteManager__
	implements SuiteManager
{
	/** Cache of suites which are available. */
	private static final Map<String, Reference<Suite>> _SUITES =
		new HashMap<>();
	
	/** Internal lock for suite management. */
	protected final Object lock =
		new Object();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void addSuiteListener(SuiteListener __sl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public Suite getSuite(String __vendor, String __name)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public SuiteInstaller getSuiteInstaller(byte[] __b, int __o,
		int __l, boolean __ignuplock)
		throws IllegalArgumentException, SecurityException
	{
		return new SuiteInstaller(
			new ByteArrayJarStreamSupplier(__b, __o, __l));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public SuiteInstaller getSuiteInstaller(String __url,
		boolean __ignuplock)
		throws IllegalArgumentException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public List<Suite> getSuites(SuiteType __t)
		throws IllegalArgumentException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through suites and find suites to return
		List<Suite> rv = new ArrayList<>();
		for (Suite s : __SystemSuiteManager__.__allSuites())
			if (s.getSuiteType() == __t)
				rv.add(s);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void removeSuite(Suite __s, boolean __ignuplock)
		throws IllegalArgumentException, SuiteLockedException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void removeSuiteListener(SuiteListener __sl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the suites which are available.
	 *
	 * @return All of the available suites.
	 * @since 2018/10/30
	 */
	static List<Suite> __allSuites()
	{
		// Just get every suite
		List<Suite> rv = new ArrayList<>();
		for (String as : SuiteAccess.availableSuites())
			rv.add(__SystemSuiteManager__.__getSuite(as));
		
		return rv;
	}
	
	/**
	 * Gets the specified suite.
	 *
	 * @param __s The suite to get.
	 * @return The suite for the given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/26
	 */
	static Suite __getSuite(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		Map<String, Reference<Suite>> suites = __SystemSuiteManager__._SUITES;
		synchronized (suites)
		{
			Reference<Suite> ref = suites.get(__s);
			Suite rv;
			
			if (ref == null || null == (rv = ref.get()))
				suites.put(__s, new WeakReference<>((rv = new Suite(__s))));
			
			return rv;
		}
	}
	
	/**
	 * Returns the suites which match the given dependency set.
	 *
	 * This is copied from the SquirrelJME build system.
	 *
	 * @param __set The set of dependencies to get.
	 * @param __opt If {@code true} include optional dependencies.
	 * @return Suites which statisfy the given dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/02
	 */
	static Suite[] __matchDependencies(DependencyInfo __set, boolean __opt)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		// Clear all optionals if they are not included
		if (!__opt)
			__set = __set.noOptionals();
		
		// Debug
		todo.DEBUG.note("Input dependencies: %s", __set);
		
		// No dependencies to search for
		if (__set.isEmpty())
			return new Suite[0];
		
		// Remember the original set for recursive dependency checks
		DependencyInfo original = __set;
		
		// The returning set
		Set<Suite> rv = new LinkedHashSet<>();
		
		// Go through all binaries and attempt to match
		for (Suite bin : __SystemSuiteManager__.__allSuites())
		{
			// Only consider matches
			MatchResult result = bin.__matchedDependencies(__set);
			if (!result.hasMatches())
				continue;
			
			// Use this as a dependency
			rv.add(bin);
			
			// Recursively go down
			for (Suite sub : __SystemSuiteManager__.__matchDependencies(
				bin.__suiteInfo().dependencies(), false))
				rv.add(sub);
			
			// Use remaining unmatched set
			__set = result.unmatched();
			
			// If the set was emptied then it will never have any more matches
			if (__set.isEmpty())
				break;
		}
		
		// {@squirreljme.error DG0a Could not locate the suite which
		// statifies the given dependency. (The dependency to look for)}
		if (rv.isEmpty())
			throw new RuntimeException(
				String.format("DG0a %s", __set));
		
		// Debug
		todo.DEBUG.note("Returning suites: %s", rv);
		
		return rv.<Suite>toArray(new Suite[rv.size()]);
	}
}

