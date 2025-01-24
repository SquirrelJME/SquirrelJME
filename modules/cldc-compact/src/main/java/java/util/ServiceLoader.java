// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * The service loader is used to lookup services which may be defined in the
 * classpath.
 *
 * Services are stored as a list of class in resources within the JAR, these
 * services lists are declared in files specifying the class name. So the
 * format is {@code META-INF/services/fully.qualified.class.name}.
 *
 * The iterator may throw {@code ServiceConfigurationError}.
 *
 * @param <S> The class to provide a service for.
 * @since 2018/12/06
 */
@Api
public final class ServiceLoader<S>
	implements Iterable<S>
{
	/** The class to search for. */
	private final Class<S> _search;
	
	/** The service cache. */
	private final __ServiceLoaderCache__<S> _cache =
		new __ServiceLoaderCache__<S>();
	
	/**
	 * Initializes the service loader.
	 *
	 * @param __cl The class to search.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/06
	 */
	@Api
	private ServiceLoader(Class<S> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this._search = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public Iterator<S> iterator()
	{
		synchronized (this)
		{
			Class<S> search = this._search;
			__ServiceLoaderCache__<S> cache = this._cache;
			
			// Use the cached array?
			Object[] use = cache._cache;
			if (use != null)
				return new __ServiceLoaderCachedIterator__<S>(search, use);
			
			// Otherwise load everything in with the fresh iterator
			Iterator<S> it = new __ServiceLoaderFreshIterator__<S>(search,
				cache);
			while (it.hasNext())
				it.next();
			
			// Use cached set
			return new __ServiceLoaderCachedIterator__<S>(search,
				cache._cache);
		}
	}
	
	/**
	 * Clears the cache of services.
	 *
	 * @since 2018/12/06
	 */
	@Api
	public void reload()
	{
		synchronized (this)
		{
			// Clear the cache
			this._cache._cache = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public String toString()
	{
		// Just matches what Java SE gives
		return "java.util.ServiceLoader[" + this._search.getName() + "]";
	}
	
	/**
	 * Create a service loader for the given class.
	 *
	 * @param <S> The class to handle services for.
	 * @param __cl The class to load a service for.
	 * @return The service loader for this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/06
	 */
	@Api
	public static <S> ServiceLoader<S> load(Class<S> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return new ServiceLoader<S>(__cl);
	}
}
