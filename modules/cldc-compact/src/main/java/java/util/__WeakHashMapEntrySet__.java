// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Entry set for weak hash maps.
 *
 * @since 2023/02/09
 */
final class __WeakHashMapEntrySet__<K, V>
	extends AbstractSet<Map.Entry<K, V>>
{
	/** Base entries. */
	private final Set<Map.Entry<__WeakHashMapKey__<K>, V>> _entries;
	
	/**
	 * Initializes the entry set for the hash map.
	 * 
	 * @param __entries The entries used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/09
	 */
	__WeakHashMapEntrySet__(
		Set<Map.Entry<__WeakHashMapKey__<K>, V>> __entries)
		throws NullPointerException
	{
		if (__entries == null)
			throw new NullPointerException("NARG");
		
		this._entries = __entries;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public void clear()
	{
		this._entries.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator()
	{
		return new __WeakHashMapIterator__<>(this._entries.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public int size()
	{
		return this._entries.size();
	}
}
