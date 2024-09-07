// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.vm.ResourceBasedClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a suite manager which is based on resources for accessing various
 * classes and other resources.
 *
 * @since 2018/11/14
 */
public final class ResourceBasedSuiteManager
	implements VMSuiteManager
{
	/** The class to get resources from. */
	protected final Class<?> actingclass;
	
	/** The prefix for libraries. */
	protected final String prefix;
	
	/** Cache of libraries. */
	private final Map<String, VMClassLibrary> _cache =
		new HashMap<>();
	
	/** Cache of all available suites. */
	private String[] _libs;
	
	/**
	 * Initializes the suite manager.
	 *
	 * @param __act The class to source resources from.
	 * @param __prefix The prefix for library lookup.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/14
	 */
	public ResourceBasedSuiteManager(Class<?> __act, String __prefix)
		throws NullPointerException
	{
		if (__act == null || __prefix == null)
			throw new NullPointerException("NARG");
		
		this.actingclass = __act;
		this.prefix = __prefix;
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
		
		Map<String, VMClassLibrary> cache = this._cache;
		synchronized (cache)
		{
			// The library must have been previously loaded first
			if (!cache.values().contains(__lib))
				throw new IllegalArgumentException(
					"Unknown library: " + __lib);
			
			Path path = __lib.path();
			return (path != null ? path.hashCode() : __lib.name().hashCode());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final String[] listLibraryNames()
	{
		// Resources are fixed, so if this was done already use the cached
		// form instead
		String[] libs = this._libs;
		if (libs != null)
			return libs.clone();
		
		// There should be a suite list
		try (InputStream in = this.actingclass.getResourceAsStream(
			this.prefix + "suites.list"))
		{
			// Does not exist, so there is no known library list
			if (in == null)
			{
				this._libs = (libs = new String[0]);
				return libs;
			}
			
			// The suites are a UTF encoded list
			List<String> rv = new ArrayList<>();
			try (DataInputStream dis = new DataInputStream(in))
			{
				int n = dis.readInt();
				for (int i = 0; i < n; i++)
					rv.add(dis.readUTF());
			}
			
			// Cache and return
			this._libs = (libs = rv.<String>toArray(new String[rv.size()]));
			return libs;
		}
		
		/* {@squirreljme.error AK01 Could not read the library list.} */
		catch (IOException e)
		{
			throw new RuntimeException("AK01", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final VMClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		Map<String, VMClassLibrary> cache = this._cache;
		synchronized (cache)
		{
			// Already got?
			VMClassLibrary rv = cache.get(__s);
			if (rv != null)
				return rv;
			
			// Pre-cached to not exist
			if (cache.containsKey(__s))
				return null;
			
			// Make sure it is actually valid
			boolean found = false;
			for (String q : this.listLibraryNames())
				if ((found |= q.equals(__s)))
					break;
			
			// If it was not found, it does not exist so cache it and fail
			if (!found)
			{
				cache.put(__s, null);
				return null;
			}
			
			// Load and store it
			cache.put(__s,
				(rv = new ResourceBasedClassLibrary(this.actingclass,
					this.prefix + __s + '/', __s)));
			
			return rv;
		}
	}
}

