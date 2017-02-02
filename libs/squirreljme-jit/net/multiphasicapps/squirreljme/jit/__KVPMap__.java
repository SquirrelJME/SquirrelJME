// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * This wraps an array of a key/value mapping to an actual map.
 *
 * @since 2017/02/02
 */
class __KVPMap__
	extends AbstractMap<String, String>
{
	/** The entry set for the map. */
	protected final Set<Map.Entry<String, String>> set;
	
	/**
	 * Initializes the mappings of key and value pairs.
	 *
	 * @param __kvp The key value pairs to use.
	 * @since 2017/02/02
	 */
	__KVPMap__(String... __kvp)
	{
		// Set
		this.set = new __EntrySet__(__kvp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/02
	 */
	@Override
	public Set<Map.Entry<String, String>> entrySet()
	{
		return this.set;
	}
	
	/**
	 * Entry of the set.
	 *
	 * @since 2017/02/02
	 */
	private static final class __Entry__
		implements Map.Entry<String, String>
	{
		/** The key. */
		protected final String key;
		
		/** The value. */
		protected final String value;
		
		/**
		 * Initializes the entry.
		 *
		 * @param __k The key used.
		 * @param __v The value used.
		 * @since 2017/02/02
		 */
		private __Entry__(String __k, String __v)
		{
			this.key = __k;
			this.value = __v;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public boolean equals(Object __a)
		{
			if (!(__a instanceof Map.Entry))
				return false;
			
			Map.Entry<?, ?> o = (Map.Entry<?, ?>)__a;
			return Objects.equals(this.key, o.getKey()) &&
				Objects.equals(this.value, o.getValue());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public String getKey()
		{
			return this.key;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public String getValue()
		{
			return this.value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public int hashCode()
		{
			return Objects.hashCode(this.key) ^
				Objects.hashCode(this.value);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public String setValue(String __a)
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
	
	/**
	 * This provides an entry set for the map.
	 *
	 * @since 2017/02/02
	 */
	private static final class __EntrySet__
		extends AbstractSet<Map.Entry<String, String>>
	{
		/** Mappings used. */
		private final String[] _pairs;
		
		/**
		 * Initializes the entry set.
		 *
		 * @param __kvp The key value pairs.
		 * @since 2017/02/02
		 */
		private __EntrySet__(String... __kvp)
		{
			this._pairs = (__kvp != null ? __kvp : new String[0]);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public Iterator<Map.Entry<String, String>> iterator()
		{
			return new __Iterator__(this._pairs);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public int size()
		{
			return this._pairs.length / 2;
		}
	}
	
	/**
	 * The iterator over the set.
	 *
	 * @since 2017/02/02
	 */
	private static final class __Iterator__
		implements Iterator<Map.Entry<String, String>>
	{
		/** Mappings used. */
		private final String[] _pairs;
		
		/** The current position for reading. */
		private volatile int _at;
		
		/**
		 * Initializes the iterator over the set.
		 *
		 * @param __kvp The key value pairs.
		 * @since 2017/02/02
		 */
		private __Iterator__(String... __kvp)
		{
			this._pairs = (__kvp != null ? __kvp : new String[0]);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public boolean hasNext()
		{
			return this._at < this._pairs.length;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public Map.Entry<String, String> next()
		{
			String[] pairs = this._pairs;
			int at = this._at;
			int len = pairs.length;
			
			// {@squirreljme.error ED02 No more entries.}
			if (at >= len)
				throw new NoSuchElementException("ED02");
			
			// Setup return value
			Map.Entry<String, String> rv = new __Entry__(pairs[at],
				(at + 1 < len ? pairs[at + 1] : null));
			
			// Increase next index
			this._at = at + 2;
			
			// Return it
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/02
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

