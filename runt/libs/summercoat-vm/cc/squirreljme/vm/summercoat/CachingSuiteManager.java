// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMSuiteManager;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a suite manager which.
 *
 * @since 2019/03/09
 */
public final class CachingSuiteManager
{
	/** The base suite manager. */
	protected final VMSuiteManager suites;
	
	/** Libraries which have been pre-cached. */
	protected final Map<VMClassLibrary, CachingClassLibrary> _cache =
		new HashMap<>();
	
	/**
	 * Initializes the caching suite manager.
	 *
	 * @param __s The suite manager to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/09
	 */
	public CachingSuiteManager(VMSuiteManager __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.suites = __s;
	}
	
	/**
	 * Caches the specified libraries.
	 *
	 * @param __l The library to parse.
	 * @return The caching class library.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/09
	 */
	public CachingClassLibrary loadLibrary(VMClassLibrary __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		Map<VMClassLibrary, CachingClassLibrary> cache = this._cache;
		synchronized (this)
		{
			CachingClassLibrary rv = cache.get(__l);
			if (rv != null)
				return rv;
			
			// Wrap and cache it
			cache.put(__l, (rv = new CachingClassLibrary(__l)));
			return rv;
		}
	}
}

