// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * This is a map which cannot be modified.
 *
 * @param <K> Key type to use.
 * @param <V> Value type to store.
 * @since 2016/02/29
 */
@UnmodifiableView
public final class UnmodifiableMap<K, V>
	extends AbstractMap<K, V>
{
	/** The existing map to wrap. */
	protected final Map<K, V> wrapped;
	
	/** Entry set cache, since it is not always needed. */
	private Reference<Set<Map.Entry<K, V>>> _escache;
	
	/**
	 * Initializes the unmodifiable wrapped map.
	 *
	 * @param __w The map to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/29
	 */
	private UnmodifiableMap(Map<K, V> __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.wrapped = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		return this.wrapped.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsValue(Object __o)
	{
		return this.wrapped.containsValue(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		// Get reference to the map
		Reference<Set<Map.Entry<K, V>>> ref = this._escache;
		Set<Map.Entry<K, V>> rv = null;
		
		// In reference?
		if (ref != null)
			rv = ref.get();
		
		// Needs initialization?
		if (rv == null)
			this._escache = new WeakReference<>((rv = new __SetView__()));
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 * @param __k
	 */
	@Override
	public V get(Object __k)
	{
		return this.wrapped.get(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return this.wrapped.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/29
	 */
	@Override
	public int size()
	{
		return this.wrapped.size();
	}
	
	/**
	 * This creates a view of the specified map which cannot be modified.
	 *
	 * @param <K> The map key.
	 * @param <V> The map value.
	 * @param __m The map to wrap.
	 * @return An unmodifiable view of the map.
	 * @since 2016/02/28
	 */
	public static <K, V> Map<K, V> of(Map<K, V> __m)
	{
		// If already one, return it
		if (__m instanceof UnmodifiableMap)
			return __m;
		
		// Wrap
		return new UnmodifiableMap<K, V>(__m);
	}
	
	/**
	 * This is the iterator over the entry set of the map.
	 *
	 * @since 2016/02/29
	 */
	private final class __SetIterator__
		implements Iterator<Map.Entry<K, V>>
	{
		/** The base iterator. */
		protected final Iterator<Map.Entry<K, V>> from =
			UnmodifiableMap.this.wrapped.entrySet().iterator();
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public boolean hasNext()
		{
			return this.from.hasNext();
		}
	
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public Map.Entry<K, V> next()
		{
			// Get the next entry
			Map.Entry<K, V> ent = this.from.next();
			
			// If null, this is a bad map but possibly might be valid
			if (ent == null)
				return null;
			
			// Wrap it
			return new __SetEntry__<K, V>(ent);
		}
		
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
	
	/**
	 * This implements the unmodifiable set view.
	 *
	 * @since 2016/02/29
	 */
	private final class __SetView__
		extends AbstractSet<Map.Entry<K, V>>
	{
		
		/**	
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public Iterator<Map.Entry<K, V>> iterator()
		{
			return new __SetIterator__();
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public int size()
		{
			return UnmodifiableMap.this.wrapped.size();
		}
	}
	
	/**
	 * This is a single entry in the set.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @since 2016/02/29
	 */
	private static final class __SetEntry__<K, V>
		implements Map.Entry<K, V>
	{
		/** The base entry. */
		protected final Map.Entry<K, V> base;
		
		/**
		 * Initializes the set entry.
		 *
		 * @param __e The entry to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/02/29
		 */
		private __SetEntry__(Map.Entry<K, V> __e)
			throws NullPointerException
		{
			// Check
			if (__e == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.base = __e;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public boolean equals(Object __a)
		{
			return this.base.equals(__a);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public K getKey()
		{
			return this.base.getKey();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public V getValue()
		{
			return this.base.getValue();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public int hashCode()
		{
			return this.base.hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/02/29
		 */
		@Override
		public V setValue(V __v)
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

