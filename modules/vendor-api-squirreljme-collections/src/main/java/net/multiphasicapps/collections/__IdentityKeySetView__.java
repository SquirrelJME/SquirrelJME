// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A key set view of an identity map.
 *
 * @since 2021/11/28
 */
final class __IdentityKeySetView__<K, V>
	extends AbstractSet<Map.Entry<K, V>>
{
	/** Entries in the map. */
	private final Set<Map.Entry<Identity<K>, V>> _entries;
	
	/**
	 * Initializes the wrapper.
	 * 
	 * @param __entries The entries to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	__IdentityKeySetView__(Set<Map.Entry<Identity<K>, V>> __entries)
		throws NullPointerException
	{
		if (__entries == null)
			throw new NullPointerException("NARG");
		
		this._entries = __entries;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator()
	{
		return new __IdentityKeySetIterator__<K, V>(this._entries.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public int size()
	{
		return this._entries.size();
	}
}
