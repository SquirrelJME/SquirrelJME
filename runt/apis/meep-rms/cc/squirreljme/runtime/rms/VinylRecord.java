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
 * @since 2018/12/13
 */
public abstract class VinylRecord
{
	/**
	 * Creates a new record.
	 *
	 * @param __sid The suite identifier.
	 * @param __n The name of the suite.
	 * @param __wo Allow write other?
	 * @return The identifier of the suite.
	 * @since 2019/04/14
	 */
	public abstract int createVolume(long __sid, String __n, boolean __wo);
	
	/**
	 * Returns the list of all available stores.
	 *
	 * @return The list of available stores.
	 * @since 2019/04/14
	 */
	public abstract int[] listVolumes();
	
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
	 * @param __vid Volume ID.
	 * @return The name of the record or {@code null} if there is no name.
	 * @since 2019/04/14
	 */
	public abstract String volumeName(int __vid);
	
	/**
	 * Returns the suite identifier for the given record.
	 *
	 * @param __vid Volume ID.
	 * @return The suite identifier or {@code 0} if it is not valid.
	 * @since 2019/04/14
	 */
	public abstract long volumeSuiteIdentifier(int __vid);
}

