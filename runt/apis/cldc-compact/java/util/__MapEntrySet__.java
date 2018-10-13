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
 * Entry set for the bucket map.
 *
 * @since 2018/10/13
 */
final class __MapEntrySet__<K, V>
	extends AbstractSet<__BucketMap__.__Entry__<K, V>>
{
	/** The map this refers to. */
	protected final __BucketMap__<K, V> map;
	
	/**
	 * Initializes the entry set.
	 *
	 * @param __map The owning map.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	__MapEntrySet__(__BucketMap__<K, V> __map)
		throws NullPointerException
	{
		if (__map == null)
			throw new NullPointerException("NARG");
		
		this.map = __map;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final Iterator<__BucketMap__.__Entry__<K, V>> iterator()
	{
		return this.map.new __EntryIterator__();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final int size()
	{
		return this.map.size();
	}
}

