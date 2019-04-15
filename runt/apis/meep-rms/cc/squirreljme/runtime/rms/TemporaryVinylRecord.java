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
	private final Map<Integer, Volume> _volumes =
		new LinkedHashMap<>();
	
	/** Next ID for storage. */
	private volatile int _nextvid =
		1;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int createVolume(long __sid, String __n, boolean __wo)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Claim next ID
		int rv = this._nextvid++;
		
		// Make the track and store it
		this._volumes.put(rv, new Volume(rv, __sid, __n, __wo));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int[] listVolumes()
	{
		Set<Integer> keys = this._volumes.keySet();
		
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
	public final String volumeName(int __vid)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final long volumeSuiteIdentifier(int __vid)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Represents a track in the record.
	 *
	 * @since 2019/04/14
	 */
	public static final class Volume
	{
		/** The volume ID. */
		protected final int vid;
		
		/** The suite identifier. */
		protected final long sid;
		
		/** The suite name. */
		protected final String name;
		
		/** Allow write by others? */
		protected final boolean writeother;
		
		/**
		 * Initializes the volume.
		 *
		 * @param __rid The volume ID.
		 * @param __sid The suite identifier.
		 * @param __name The name of the record.
		 * @param __wo Allow write by others?
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/14
		 */
		public Volume(int __vid, long __sid, String __name, boolean __wo)
			throws NullPointerException
		{
			if (__name == null)
				throw new NullPointerException("NARG");
			
			this.vid = __vid;
			this.sid = __sid;
			this.name = __name;
			this.writeother = __wo;
		}
	}
}

