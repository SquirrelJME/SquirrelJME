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
import cc.squirreljme.runtime.cldc.asm.SuiteAccess;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		for (String as : SuiteAccess.availableSuites())
		{
			Suite s = __SystemSuiteManager__.__getSuite(as);
			
			// Matching suite type
			if (s.getSuiteType() == __t)
				rv.add(s);
		}
		
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
}

