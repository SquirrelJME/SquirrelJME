// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

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
		Class<S> search = this._search;
		__ServiceLoaderCache__<S> cache = this._cache;
		
		// Use the cached array?
		Object[] use = cache._cache;
		if (use != null)
			return new __ServiceLoaderCachedIterator__<S>(search, use);
		
		// Cache it
		else
			return new __ServiceLoaderFreshIterator__<S>(search, cache);
	}
	
	/**
	 * Clears the cache of services.
	 *
	 * @since 2018/12/06
	 */
	public void reload()
	{
		// Clear the cache
		this._cache._cache = null;
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
	public static <S> ServiceLoader<S> load(Class<S> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return new ServiceLoader<S>(__cl);
	}
}
