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
 * This is the value set for an abstract map.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2018/10/29
 */
final class __AbstractMapValues__<K, V>
	extends AbstractCollection<V>
{
	/** The backing map. */
	protected final Map<K, V> map;
	
	/**
	 * Initializes the set.
	 *
	 * @param __map The backing map
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/01
	 */
	__AbstractMapValues__(Map<K, V> __map)
		throws NullPointerException
	{
		if (__map == null)
			throw new NullPointerException("NARG");
		
		this.map = __map;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@Override
	public final boolean contains(Object __o)
	{
		return this.map.containsValue(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/19
	 */
	@Override
	public final Iterator<V> iterator()
	{
		return new __Iterator__<K, V>(this.map.entrySet().iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/19
	 */
	@Override
	public final int size()
	{
		return this.map.size();
	}
	
	/**
	 * This is the iterator over the map's value set.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @since 2018/11/01
	 */
	static final class __Iterator__<K, V>
		implements Iterator<V>
	{
		/** The entry set iterator. */
		protected final Iterator<Map.Entry<K, V>> iterator;
		
		/**
		 * Initializes the iterator.
		 *
		 * @param __it The backing iterator.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/01
		 */
		__Iterator__(Iterator<Map.Entry<K, V>> __it)
			throws NullPointerException
		{
			if (__it == null)
				throw new NullPointerException("NARG");
			
			this.iterator = __it;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public boolean hasNext()
		{
			return this.iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public V next()
			throws NoSuchElementException
		{
			return this.iterator.next().getValue();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/01
		 */
		@Override
		public void remove()
		{
			this.iterator.remove();
		}
	}
}

