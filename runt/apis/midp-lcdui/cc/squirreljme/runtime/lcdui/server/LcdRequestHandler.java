// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This is the handler for any requests that are made to the LCD server, this
 * enables client threads to call into the server from any thread while the
 * GUI remains in a single thread at all time.
 *
 * @since 2018/03/17
 */
public final class LcdRequestHandler
	implements Runnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

