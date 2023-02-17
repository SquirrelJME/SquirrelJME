// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Hash for record stores.
 *
 * @since 2023/02/16
 */
public final class RecordStoreHash
	implements Comparable<RecordStoreHash>
{
	/** The suite this record is in. */
	public final SuiteHash suiteHash;
	
	/**
	 * Initializes the record store hash.
	 * 
	 * @param __suite The suite this is in.
	 * @since 2023/02/16
	 */
	public RecordStoreHash(SuiteHash __suite)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/16
	 */
	@Override
	public int compareTo(RecordStoreHash __o)
	{
		throw Debugging.todo();
	}
}
