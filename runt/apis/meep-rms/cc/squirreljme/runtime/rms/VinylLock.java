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
 * This is a lock for a vinyl record which may be unlocked when closed.
 *
 * @since 2018/12/14
 */
public interface VinylLock
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/14
	 */
	@Override
	public abstract void close();
}

