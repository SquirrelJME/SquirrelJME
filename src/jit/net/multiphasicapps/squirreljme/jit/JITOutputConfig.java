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
import java.util.Objects;
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
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public JITCacheCreator cacheCreator()
	{
		return this._cache;
	}
	
	/**
	 * Returns an immutable copy of the current configuration.
	 *
	 * @return An immutable copy of the configuration.
	 * @throws IllegalArgumentException If required configuration settings
	 * were not set.
	 * @since 2016/07/05
	 */
	public Immutable immutable()
		throws IllegalArgumentException
	{
		// Lock
		synchronized (this.lock)
		{
			return new Immutable(this);
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
	public JITCacheCreator setCacheCreator(JITCacheCreator __c)
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
	public JITTriplet setTriplet(JITTriplet __t)
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
			(__ccg.cacheCreator() != null) + "}";
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
			}
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

