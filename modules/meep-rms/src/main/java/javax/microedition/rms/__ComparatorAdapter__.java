// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Comparator;

/**
 * Adapts {@link RecordComparator} to an integer comparison for lists.
 *
 * @since 2025/06/24
 */
final class __ComparatorAdapter__
	implements Comparator<Integer>
{
	/** The comparator used. */
	final RecordComparator _comparator;
	
	/** The record store to access. */
	final RecordStore _store;
	
	/**
	 * Initializes the adapter.
	 *
	 * @param __store The store to access.
	 * @param __comparator The comparator to use for comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/24
	 */
	__ComparatorAdapter__(RecordStore __store,
		RecordComparator __comparator)
		throws NullPointerException
	{
		if (__store == null || __comparator == null)
			throw new NullPointerException("NARG");
		
		this._store = __store;
		this._comparator = __comparator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/24
	 */
	@Override
	public int compare(Integer __a, Integer __b)
	{
		try
		{
			// Get the individual record data to compare with
			RecordStore store = this._store;
			byte[] a = store.getRecord(__a);
			byte[] b = store.getRecord(__b);
			
			// Perform the comparison
			return this._comparator.compare(a, b);
		}
		catch (RecordStoreException __e)
		{
			throw new __FailedCompare__(__e);
		}
	}
}
