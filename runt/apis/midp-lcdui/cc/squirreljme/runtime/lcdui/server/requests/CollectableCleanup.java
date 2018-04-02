// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server.requests;

import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdCollectable;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;

/**
 * Cleans up after a collectable.
 *
 * @since 2018/03/23
 */
public final class CollectableCleanup
	extends LcdRequest
{
	/** The widget to cleanup. */
	protected final LcdCollectable cleanup;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __cl The collectable to clean up.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public CollectableCleanup(LcdServer __sv, LcdCollectable __cl)
		throws NullPointerException
	{
		super(__sv, LcdFunction.COLLECTABLE_CLEANUP);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.cleanup = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		throw new todo.TODO();
	}
}

