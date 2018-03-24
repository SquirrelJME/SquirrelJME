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
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * Vibrates the display.
 *
 * @since 2018/03/23
 */
public class DisplayVibrate
	extends LcdRequest
{
	/** The widget to vibrate. */
	protected final LcdWidget widget;
	
	/** The number of milliseconds to vibrate for. */
	protected final int milliseconds;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __w The widget to vibrate.
	 * @param __ms The number of milliseconds to vibrate for.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public DisplayVibrate(LcdServer __sv, LcdWidget __w, int __ms)
		throws NullPointerException
	{
		super(__sv, LcdFunction.DISPLAY_VIBRATE);
		
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.widget = __w;
		this.milliseconds = __ms;
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

