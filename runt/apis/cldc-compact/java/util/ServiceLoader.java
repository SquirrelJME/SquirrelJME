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

import cc.squirreljme.runtime.cldc.asm.ResourceAccess;
import cc.squirreljme.runtime.cldc.asm.SuiteAccess;
import cc.squirreljme.runtime.cldc.io.ResourceInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

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
	private final __Cache__<S> _cache =
		new __Cache__<S>();
	
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
		__Cache__<S> cache = this._cache;
		
		// Use the cached array?
		Object[] use = cache._cache;
		if (use != null)
			return new __CachedIterator__<S>(search, use);
		
		// Cache it
		else
			return new __FreshIterator__<S>(search, cache);
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
	
	/**
	 * Cache for the service loader.
	 *
	 * @param <S> The class type.
	 * @since 2018/12/06
	 */
	private static final class __Cache__<S>
	{
		/** The cache of services. */
		volatile Object[] _cache;
	}
	
	/**
	 * Iterator over the cached set.
	 *
	 * @param <S> The class type.
	 * @since 2018/12/06
	 */
	private static final class __CachedIterator__<S>
		implements Iterator<S>
	{
		/** The search class. */
		private final Class<S> _search;
		
		/** The array to use for this. */
		private final Object[] _items;
		
		/** The next index. */
		private int _next;
		
		/**
		 * Wraps the given array and provides an iterator of it.
		 *
		 * @param __s The search class.
		 * @param __it The iterator to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/12/06
		 */
		__CachedIterator__(Class<S> __s, Object[] __it)
			throws NullPointerException
		{
			if (__s == null || __it == null)
				throw new NullPointerException("NARG");
			
			this._search = __s;
			this._items = __it;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final boolean hasNext()
		{
			return this._next < this._items.length;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final S next()
		{
			Object[] items = this._items;
			int next = this._next;
			
			// No more?
			if (next >= items.length)
				throw new NoSuchElementException("NSEE");
			
			// Get and iterator
			Object rv = items[next];
			this._next = next + 1;
			return this._search.cast(rv);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
	
	/**
	 * Iterator over a fresh search pouring into the cache.
	 *
	 * @param <S> The class type.
	 * @since 2018/12/06
	 */
	private static final class __FreshIterator__<S>
		implements Iterator<S>
	{
		/** The search class. */
		private final Class<S> _search;
		
		/** The cache to put in. */
		private final __Cache__<S> _cache;
		
		/** Temporary cache building. */
		private final List<Object> _cachebuild =
			new ArrayList<>();
		
		/** Suites left to parse. */
		private final Queue<String> _suites =
			new LinkedList<>();
		
		/** Classes left to create and scan. */
		private final Queue<String> _classes =
			new LinkedList<>();
		
		/** The next service to run. */
		private S _next;
		
		/** Is this finished? */
		private boolean _finished;
		
		/**
		 * Initializes the iterator for fresh service lookup.
		 *
		 * @param __s The search class.
		 * @param __c The cache class.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/12/06
		 */
		__FreshIterator__(Class<S> __s, __Cache__<S> __c)
			throws NullPointerException
		{
			if (__s == null || __c == null)
				throw new NullPointerException("NARG");
			
			this._search = __s;
			this._cache = __c;
			
			// Seed suites left with the
			Queue<String> suites = this._suites;
			for (String s : SuiteAccess.currentClassPath())
				suites.add(s);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final boolean hasNext()
		{
			// If already finished, nothing can be done
			if (this._finished)
				return false;
			
			// Already cached?
			if (this._next != null)
				return true;
			
			Queue<String> suites = this._suites;
			Queue<String> classes = this._classes;
			
			// Loops until an element was found
			for (;;)
			{
				// If there is an entry in the class list, try to load that
				// class
				String maybe = classes.poll();
				if (maybe != null)
				{
					// Try to load this class
					try
					{
						// If this fails an exception is thrown
						Class<?> cl = Class.forName(maybe);
						
						// Try to create a new instance since this is new,
						// any exceptions thrown cause errors
						Object rv = cl.newInstance();
						
						// Cache this instance for later
						this._cachebuild.add(rv);
						
						// Cast this class
						this._next = this._search.cast(rv);
						return true;
					}
					
					// {@squirreljme.error ZZ3c Could not load the service
					// class.}
					catch (ClassCastException|IllegalAccessException|
						InstantiationException|ClassNotFoundException e)
					{
						throw new ServiceConfigurationError("ZZ3c", e);
					}
				}
				
				// Nothing is in the classes, so we need to pull a suite
				// resource
				String pull = suites.poll();
				if (pull == null)
				{
					// Mark as finished
					this._finished = true;
					
					// Store in the cache for later since it is all done
					// now
					List<Object> cb = this._cachebuild;
					this._cache._cache = cb.<Object>toArray(
						new Object[cb.size()]);
					
					// Clear from this iterator
					cb.clear();
					
					// Nothing else to do
					return false;
				}
				
				// Could services list
				else
				{
					// Load resources
					try (InputStream in = ResourceInputStream.open(pull,
						"META-INF/services/" + this._search.getName()))
					{
						// Ignore unknown resources
						if (in == null)
							continue;
						
						// Read by line
						try (BufferedReader br = new BufferedReader(
							new InputStreamReader(in, "utf-8")))
						{
							for (;;)
							{
								// Stop on EOF
								String ln = br.readLine();
								if (ln == null)
									break;
								
								// Trim the line
								ln = ln.trim();
								
								// Ignore blank lines
								if (ln.isEmpty())
									continue;
								
								// Ignore comments
								if (ln.startsWith("#"))
									continue;
								
								// Add otherwise
								classes.add(ln);
							}
						}
					}
					
					// {@squirreljme.error ZZ3d Could not read the services
					// list}
					catch (IOException e)
					{
						throw new ServiceConfigurationError("ZZ3d", e);
					}
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final S next()
		{
			// Cache
			if (!this.hasNext())
				throw new NoSuchElementException("NSEE");
			
			// Return the cached element
			S rv = this._next;
			this._next = null;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/06
		 */
		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

