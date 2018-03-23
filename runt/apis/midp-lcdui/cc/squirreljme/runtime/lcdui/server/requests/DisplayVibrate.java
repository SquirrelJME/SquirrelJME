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
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;

/**
 * Vibrates the display.
 *
 * @since 2018/03/23
 */
public class DisplayVibrate
	extends LcdRequest
{
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @since 2018/03/23
	 */
	public DisplayVibrate(LcdServer __sv)
	{
		super(__sv, LcdFunction.DISPLAY_VIBRATE);
		
		throw new todo.TODO();
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

