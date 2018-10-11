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
 * This is the key set for an abstract map.
 *
 * @param <K> The key type.
 * @since 2018/10/10
 */
final class __AbstractMapKeySet__<K>
	extends AbstractSet<K>
{
	/** The entry set. */
	protected final Set<? extends Map.Entry<K, ?>> entries;
	
	/**
	 * Initializes the set.
	 *
	 * @param __set The entry set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/10
	 */
	__AbstractMapKeySet__(Set<? extends Map.Entry<K, ?>> __set)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		this.entries = __set;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final void clear()
	{
		this.entries.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final Iterator<K> iterator()
	{
		return new __MapEntryKeyIterator__<K>(this.entries.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final boolean remove(Object __k)
	{
		// Need to go through the iterator and find any entries
		for (Iterator<? extends Map.Entry<K, ?>> it = this.entries.iterator();
			it.hasNext();)
		{
			Map.Entry<K, ?> e = it.next();
			
			if (Objects.equals(e.getKey(), __k))
			{
				it.remove();
				return true;
			}
		}
		
		// Not modified
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final boolean removeAll(Collection<?> __from)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final boolean retainAll(Collection<?> __from)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final int size()
	{
		return this.entries.size();
	}
}

