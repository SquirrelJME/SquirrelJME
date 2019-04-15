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

/**
 * This is a single record which stores multiple tracks of data.
 *
 * Vinyls have a single lock on them.
 *
 * @see VinylTrack
 * @since 2018/12/13
 */
public abstract class VinylRecord
{	
	/**
	 * Returns the list of all available records.
	 *
	 * @return The list of available records.
	 * @since 2019/04/14
	 */
	public abstract int[] listRecords();
	
	/**
	 * Locks this record so only a single set of actions can be performed on
	 * them, even for the same thread.
	 *
	 * @return The lock used to eventually unlock, to be used with
	 * try-with-resources.
	 * @since 2018/12/14
	 */
	public abstract VinylLock lock();
	
	/**
	 * Returns the name of the given record.
	 *
	 * @param __rid Record ID.
	 * @return The name of the record or {@code null} if there is no name.
	 * @since 2019/04/14
	 */
	public abstract String recordName(int __rid);
	
	/**
	 * Returns the suite identifier for the given record.
	 *
	 * @param __rid Record ID.
	 * @return The suite identifier or {@code 0} if it is not valid.
	 * @since 2019/04/14
	 */
	public abstract long recordSuiteIdentifier(int __rid);
}

