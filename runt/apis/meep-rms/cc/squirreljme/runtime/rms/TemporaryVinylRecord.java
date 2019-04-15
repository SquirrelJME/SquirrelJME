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
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int[] listRecords()
	{
		throw new todo.TODO();
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
}

