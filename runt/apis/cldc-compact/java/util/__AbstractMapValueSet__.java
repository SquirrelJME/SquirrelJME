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
 * @param <V> The value type.
 * @since 2018/10/29
 */
final class __AbstractMapValueSet__<V>
	extends AbstractSet<V>
{
	/** The entry set. */
	protected final Set<? extends Map.Entry<?, V>> entries;
	
	/**
	 * Initializes the set.
	 *
	 * @param __set The entry set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/19
	 */
	__AbstractMapValueSet__(Set<? extends Map.Entry<?, V>> __set)
		throws NullPointerException
	{
		if (__set == null)
			throw new NullPointerException("NARG");
		
		this.entries = __set;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/19
	 */
	@Override
	public final void clear()
	{
		this.entries.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/19
	 */
	@Override
	public final Iterator<V> iterator()
	{
		return new __MapEntryValueIterator__<V>(this.entries.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/19
	 */
	@Override
	public final boolean remove(Object __v)
	{
		// Need to go through the iterator and find any entries
		for (Iterator<? extends Map.Entry<?, V>> it = this.entries.iterator();
			it.hasNext();)
		{
			Map.Entry<?, V> e = it.next();
			
			if (Objects.equals(e.getValue(), __v))
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
	 * @since 2018/10/19
	 */
	@Override
	public final int size()
	{
		return this.entries.size();
	}
}

