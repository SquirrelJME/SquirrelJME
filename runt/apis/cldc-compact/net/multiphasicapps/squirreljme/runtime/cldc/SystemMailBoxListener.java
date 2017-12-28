// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

import java.io.Closeable;

/**
 * This represents a listener for incoming mailbox connections and acts as
 * a server.
 *
 * @since 2017/12/10
 */
public interface SystemMailBoxListener
	extends AutoCloseable
{
	/**
	 * Accepts an incoming mailbox request to create a mailbox connection.
	 *
	 * @return The mailbox descriptor for the server end of the mailbox.
	 * @throws InterruptedException If the thread was interrupted accepting
	 * a connection.
	 * @throws KernelMailBoxException If the mailbox is closed.
	 * @since 2016/10/13
	 */
	public abstract SystemMailBoxConnection accept()
		throws IllegalArgumentException, InterruptedException,
			SystemMailBoxException;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/10
	 */
	@Override
	public abstract void close()
		throws SystemMailBoxException;
}

