// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * This is used to configure the input and output of a JIT operation. This
 * is used as a singular source for configuration to simplify passing of
 * arguments along with the potential of adding more details when required.
 *
 * @since 2016/07/04
 */
public final class JITOutputConfig
	implements __CommonConfigGet__
{
	/** Internal consistency lock. */
	protected final Object lock =
		new Object();
	
	/** Static class calls which are to be rewritten. */
	protected final Set<JITClassNameRewrite> rewrites =
		new HashSet<>();
	
	/** Object mappings. */
	private final Map<Class<?>, Object> _objects =
		new HashMap<>();
	
	/** The triplet to target. */
	private volatile JITTriplet _triplet;
	
	/** The output cache creator. */
	private volatile JITCacheCreator _cache;
	
	/** Cached string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes a blank configuration.
	 *
	 * @since 2016/07/04
	 */
	public JITOutputConfig()
	{
	}
	
	/**
	 * Adds a static call rewrite.
	 *
	 * @param __scr The rewrite to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/07
	 */
	public final void addStaticCallRewrite(JITClassNameRewrite __scr)
		throws NullPointerException
	{
		// Check
		if (__scr == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this.rewrites.add(__scr);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public final JITCacheCreator cacheCreator()
	{
		return this._cache;
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public final JITClassNameRewrite[] classNameRewrites()
	{
		// Lock
		synchronized (this.lock)
		{
			Set<JITClassNameRewrite> rewrites = this.rewrites;
			return rewrites.<JITClassNameRewrite>toArray(
				new JITClassNameRewrite[rewrites.size()]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public <C extends Object & JITObjectProperties> C getObject(
		Class<C> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			return __cl.cast(this._objects.get(__cl));
		}
	}
	
	/**
	 * Returns an immutable copy of the current configuration.
	 *
	 * @return An immutable copy of the configuration.
	 * @throws IllegalArgumentException If required configuration settings
	 * were not set.
	 * @since 2016/07/05
	 */
	public final Immutable immutable()
		throws IllegalArgumentException
	{
		// Lock
		synchronized (this.lock)
		{
			return new Immutable(this);
		}
	}
	
	/**
	 * Registers the given object with this configuration.
	 *
	 * @param <C> The type to register it to.
	 * @param __cl The type to register it to.
	 * @param __o The object to associate.
	 * @throws ClassCastException If the input class or object are not of
	 * the associated types.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public final <C extends Object & JITObjectProperties> void registerObject(
		Class<C> __cl, C __o)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __o == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ED0u The class does not extend
		// {@code JITObjectProperties} or the object does not extend the
		// input class. (The input class type)}
		if (JITObjectProperties.class.isAssignableFrom(__cl) ||
			!__cl.isInstance(__o))
			throw new ClassCastException(String.format("ED0u %s", __cl));
		
		// Lock
		synchronized (this.lock)
		{
			this._objects.put(__cl, __o);
		}
	}
	
	/**
	 * Sets or clears the cache creator which is used when the user of the JIT
	 * requests that executables be written to the disk or some other
	 * serialized cache rather than directly executable code in memory.
	 *
	 * @param __c The cache creator to use, if {@code null} then it is cleared.
	 * @return The previously set cache creator, may be {@code null}.
	 * @since 2016/07/06
	 */
	public final JITCacheCreator setCacheCreator(JITCacheCreator __c)
	{
		// Lock
		synchronized (this.lock)
		{
			JITCacheCreator rv = this._cache;
			this._cache = __c;
			return rv;
		}
	}
	
	/**
	 * Sets the triplet to target.
	 *
	 * @param __t The triplet to target.
	 * @return The old triplet or {@code null} if it was not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public final JITTriplet setTriplet(JITTriplet __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			JITTriplet rv = this._triplet;
			this._triplet = __t;
			return rv;
		}
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = __createString(this)));
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public JITTriplet triplet()
	{
		return this._triplet;
	}
	
	/**
	 * Common string creation.
	 *
	 * @param __ccg The common get configuration.
	 * @return The string representation of it.
	 * @since 2016/07/26
	 */
	private static String __createString(__CommonConfigGet__ __ccg)
	{
		return "{triplet=" + __ccg.triplet() + ", cache=" +
			(__ccg.cacheCreator() != null) + ", rewrites=" +
			Arrays.<JITClassNameRewrite>asList(__ccg.classNameRewrites()) +
			"}";
	}
	
	/**
	 * This is an immutable copy of an output configuration which does not
	 * change so that it may safely be used by the {@link JITOutputFactory}
	 * without worrying about state changes.
	 *
	 * @since 2016/07/04
	 */
	public static final class Immutable
		implements __CommonConfigGet__
	{
		/** The target triplet. */
		protected final JITTriplet triplet;
		
		/** The cache creator to use (optional). */
		protected final JITCacheCreator cache;
		
		/** Object associations. */
		private final Map<Class<?>, Object> _objects;
		
		/** Rewrites to perform. */
		private final JITClassNameRewrite[] _rewrites;
		
		/** Cached string representation. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes an immutable configuration which does not change.
		 *
		 * @param __joc The base configuration.
		 * @throws IllegalArgumentException If required options were not set.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/04
		 */
		private Immutable(JITOutputConfig __joc)
			throws IllegalArgumentException, NullPointerException
		{
			// Lock
			if (__joc == null)
				throw new NullPointerException("NARG");
			
			// Lock
			synchronized (__joc.lock)
			{
				// {@squirreljme.error ED09 A triplet was never set.}
				JITTriplet triplet = __joc._triplet;
				if (triplet == null)
					throw new IllegalArgumentException("ED09");
				this.triplet = triplet;
				
				// The cache creator is optional
				this.cache = __joc._cache;
				
				// Set rewrites
				this._rewrites = __joc.classNameRewrites();
				
				// Copy objects
				this._objects = new LinkedHashMap<>(__joc._objects);
			}
		}
		
		/**
		 * Returns all properties which are associated with the given output
		 * configuration to be placed in the global system property mappings.
		 *
		 * @return An array of paired strings representing the system property
		 * key and the next element being the value of it.
		 * @since 2016/08/30
		 */
		public final String[] allProperties()
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/07
		 */
		@Override
		public final JITClassNameRewrite[] classNameRewrites()
		{
			return this._rewrites.clone();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/06
		 */
		@Override
		public final JITCacheCreator cacheCreator()
		{
			return this.cache;
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/08/30
		 */
		@Override
		public final <C extends Object & JITObjectProperties> C getObject(
			Class<C> __cl)
			throws NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
	
			// Get
			return __cl.cast(this._objects.get(__cl));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/26
		 */
		@Override
		public final String toString()
		{
			// Get
			Reference<String> ref = this._string;
			String rv;
			
			// Create?
			if (ref == null || null == (rv = ref.get()))
				this._string = new WeakReference<>(
					(rv = __createString(this)));
			
			// Return
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/05
		 */
		@Override
		public final JITTriplet triplet()
		{
			return this.triplet;
		}
	}
}

