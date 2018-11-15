// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	implements SpringSuiteManager
{
	/** The class to get resources from. */
	protected final Class<?> actingclass;
	
	/** The prefix for libraries. */
	protected final String prefix;
	
	/** Cache of libraries. */
	private final Map<String, Reference<SpringClassLibrary>> _cache =
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
			
			// Will just be a normal list
			List<String> rv = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in, "utf-8")))
			{
				for (;;)
				{
					String ln = br.readLine();
					
					// EOF
					if (ln == null)
						break;
					
					// Trim and ignore empty lines
					ln = ln.trim();
					if (ln.isEmpty())
						continue;
					
					// Add otherwise
					rv.add(ln);
				}
			}
			
			// Cache and return
			this._libs = (libs = rv.<String>toArray(new String[rv.size()]));
			return libs;
		}
		
		// {@squirreljme.error BK33 Could not read the library list.}
		catch (IOException e)
		{
			throw new RuntimeException("BK33", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final SpringClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		Map<String, Reference<SpringClassLibrary>> cache = this._cache;
		synchronized (cache)
		{
			Reference<SpringClassLibrary> ref = cache.get(__s);
			SpringClassLibrary rv;
			
			if (ref == null || null == (rv = ref.get()))
				cache.put(__s, new WeakReference<>((
					rv = new ResourceBasedClassLibrary(this.actingclass,
						this.prefix + __s + '/', __s))));
			
			return rv;
		}
	}
}

