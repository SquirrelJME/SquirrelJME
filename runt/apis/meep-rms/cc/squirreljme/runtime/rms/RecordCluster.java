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

import javax.microedition.rms.RecordStoreException;

/**
 * This is a class which provides access to a cluster of record stores which
 * are owned by the specified suite.
 *
 * @since 2017/02/27
 */
@Deprecated
public abstract class RecordCluster
{
	/** Lock on operations in the record cluster, since they are atomic. */
	protected final Object lock =
		new Object();
	
	/** The owner of the cluster. */
	protected final RecordStoreOwner owner;
	
	/**
	 * Initializes the record cluster.
	 *
	 * @param __o The owner of the cluster.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/27
	 */
	public RecordCluster(RecordStoreOwner __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __o;
	}
	
	/**
	 * Returns the list of record stores that exist.
	 *
	 * @return The list of available record stores, the order is unspecified
	 * and implementation dependent. If there are no records then {@code null}
	 * will be returned.
	 * @throws RecordStoreException If there was an error reading the list
	 * of record stores.
	 * @since 2017/02/27
	 */
	public abstract String[] listRecordStores()
		throws RecordStoreException;
}

