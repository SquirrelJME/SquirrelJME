// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a callback thread.
 *
 * @since 2020/09/15
 */
public class CallbackThread
	implements AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/09/15
	 */
	@Override
	public final void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Opens the thread.
	 * @since 2020/09/19
	 */
	public final void open()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the thread to invoke on.
	 * 
	 * @return The used thread.
	 * @since 2020/09/15
	 */
	public final SpringThread thread()
	{
		throw Debugging.todo();
	}
}
