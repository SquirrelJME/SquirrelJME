// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.IdentityMap;

/**
 * This is a suite manager which merges multiple suite managers into one so
 * that more libraries from different sources may be included.
 *
 * @since 2018/12/08
 */
public final class MergedSuiteManager
	implements VMSuiteManager
{
	/** The source managers. */
	private final VMSuiteManager[] _sources;
	
	/** The library to ID mapping. */
	private final Map<VMClassLibrary, Integer> _libToId =
		new IdentityMap<>(new LinkedHashMap<>());
	
	/** The next library ID. */
	private volatile int _nextLibId;
	
	/**
	 * Initializes the merged suite manager.
	 *
	 * @param __s The sources to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	public MergedSuiteManager(VMSuiteManager... __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy and check for nulls
		__s = __s.clone();
		for (VMSuiteManager s : __s)
			if (s == null)
				throw new NullPointerException("NARG");
		
		this._sources = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/18
	 */
	@Override
	public int libraryId(VMClassLibrary __lib)
		throws IllegalArgumentException, NullPointerException
	{
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		Map<VMClassLibrary, Integer> libToId = this._libToId;
		synchronized (libToId)
		{
			Integer rv = libToId.get(__lib);
			if (rv == null)
				throw new IllegalArgumentException(
					"Unknown library: " + __lib);
			
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final String[] listLibraryNames()
	{
		Set<String> rv = new LinkedHashSet<>();
		
		// Add libraries that exist, but do not duplicate them!
		for (VMSuiteManager m : this._sources)
			for (String s : m.listLibraryNames())
				rv.add(s);
		
		return rv.<String>toArray(new String[rv.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final VMClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Go through the libraries and see if any exist
		for (VMSuiteManager m : this._sources)
		{
			VMClassLibrary rv = m.loadLibrary(__s);
			if (rv != null)
			{
				// Do we need to initialize a library ID here?
				Map<VMClassLibrary, Integer> libToId = this._libToId;
				synchronized (libToId)
				{
					if (!libToId.containsKey(rv))
						libToId.put(rv, ++this._nextLibId);
				}
				
				// Use it
				return rv;
			}
		}
		
		// Not found
		return null;
	}
}

