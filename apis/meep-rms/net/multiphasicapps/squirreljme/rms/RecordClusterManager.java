// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.rms;

/**
 * This is the base class for a manager which provides access to a record
 * store.
 *
 * @since 2017/02/27
 */
public abstract class RecordClusterManager
{
	/**
	 * Opens the cluster manager for the given suite.
	 *
	 * @param __o The suite to open the cluster for, if it is already open
	 * then the existing cluster will be returned.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/27
	 */
	public final RecordCluster open(RecordStoreOwner __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

