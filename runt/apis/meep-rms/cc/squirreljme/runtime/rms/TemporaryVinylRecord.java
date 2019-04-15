// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a vinyl record which stores all of its data within in memory
 * buffers.
 *
 * @since 2018/12/13
 */
public final class TemporaryVinylRecord
	extends VinylRecord
{
	/** The lock for this record. */
	protected final BasicVinylLock lock =
		new BasicVinylLock();
	
	/** Tracks which are available. */
	private final Map<Integer, Track> _tracks =
		new LinkedHashMap<>();
	
	/** Next ID for storage. */
	private final int _nextid =
		1;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int[] listRecords()
	{
		Set<Integer> keys = this._tracks.keySet();
		
		// Setup basic integer array
		int n = keys.size();
		int[] rv = new int[n];
		
		// Fill in keys
		int at = 0;
		for (int v : keys)
			rv[at++] = v;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/14
	 */
	@Override
	public final VinylLock lock()
	{
		return this.lock.lock();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final String recordName(int __rid)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final long recordSuiteIdentifier(int __rid)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Represents a track in the record.
	 *
	 * @since 2019/04/14
	 */
	public static final class Track
	{
	}
}

