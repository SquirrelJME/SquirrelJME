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
 * This is an iterator over map entry values.
 *
 * @param <V> The value type.
 * @since 2018/10/29
 */
final class __MapEntryValueIterator__<V>
	implements Iterator<V>
{
	/** The iterator to source from. */
	protected final Iterator<? extends Map.Entry<?, V>> iterator;
	
	/**
	 * Initializes the iterator over map entry keys.
	 *
	 * @param __it The iterator used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/29
	 */
	__MapEntryValueIterator__(Iterator<? extends Map.Entry<?, V>> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public final boolean hasNext()
	{
		return this.iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public final V next()
	{
		return this.iterator.next().getValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public final void remove()
	{
		this.iterator.remove();
	}
}

