// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Iterator over a fresh search pouring into the cache.
 *
 * @param <S> The class type.
 * @since 2018/12/06
 */
final class __ServiceLoaderFreshIterator__<S>
	implements Iterator<S>
{
	/** The search class. */
	private final Class<S> _search;
	
	/** The cache to put in. */
	private final __ServiceLoaderCache__<S> _cache;
	
	/** Temporary cache building. */
	private final List<Object> _cacheBuild =
		new ArrayList<>();
	
	/** JARs left to parse. */
	private final Queue<JarPackageBracket> _jarsLeft =
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
	__ServiceLoaderFreshIterator__(Class<S> __s, __ServiceLoaderCache__<S> __c)
		throws NullPointerException
	{
		if (__s == null || __c == null)
			throw new NullPointerException("NARG");
		
		this._search = __s;
		this._cache = __c;
		
		// All of the suites to look within
		this._jarsLeft.addAll(
			Arrays.<JarPackageBracket>asList(JarPackageShelf.classPath()));
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
		
		// Loops until an element was found
		for (Queue<String> classes = this._classes;;)
		{
			// If there are no classes, consume the next one
			if (classes.isEmpty())
			{
				this.__consumeNextJar();
				
				// Early out when nothing is left
				if (this._finished)
					return false;
			}
			
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
					this._cacheBuild.add(rv);
					
					// Cast this class
					this._next = this._search.cast(rv);
					return true;
				}
				
				/* {@squirreljme.error ZZ30 Could not load the service
				class.} */
				catch (ClassCastException|IllegalAccessException|
					InstantiationException|ClassNotFoundException e)
				{
					throw new ServiceConfigurationError("ZZ30", e);
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
	
	/**
	 * Consume the next JAR and loads the classes.
	 *
	 * @since 2020/06/18
	 */
	private void __consumeNextJar()
	{
		// If there are no JARs left, then just stop processing
		JarPackageBracket nextJar = this._jarsLeft.poll();
		if (nextJar == null)
		{
			// Mark as done
			this._finished = true;
			
			// Push these services into the cache
			List<Object> cb = this._cacheBuild;
			this._cache._cache = cb.<Object>toArray(
				new Object[cb.size()]);
			
			// Clear from this fresh iterator
			cb.clear();
			
			return;
		}
		
		// Load the Jar otherwise
		this.__loadJar(nextJar);
	}
	
	/**
	 * Loads the specified JAR.
	 *
	 * @param __jar The Jar to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	@SuppressWarnings("UnnecessaryLocalVariable")
	private void __loadJar(JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		// Load the services for the given Jar
		try (InputStream in = JarPackageShelf.openResource(__jar,
			"META-INF/services/" + this._search.getName()))
		{
			// If this JAR has no services, ignore it
			if (in == null)
				return;
			
			// Read by line
			Queue<String> classes = this._classes;
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in, "utf-8")))
			{
				// Read in every service line
				for (;;)
				{
					// Stop on EOF
					String ln = br.readLine();
					if (ln == null)
						break;
					
					// Trim the line to remove useless spaces
					ln = ln.trim();
					
					// Ignore blank lines
					if (ln.isEmpty())
						continue;
					
					// Ignore comments
					if (ln.charAt(0) == '#')
						continue;
					
					// Add otherwise
					classes.add(ln);
				}
			}
		}
		
		/* {@squirreljme.error ZZ31 Could not read the services
		list.} */
		catch (IOException e)
		{
			throw new ServiceConfigurationError("ZZ31", e);
		}
	}
}
