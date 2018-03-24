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
 * Gets the width of a widget.
 *
 * @since 2018/03/23
 */
public class WidgetGetWidth
	extends LcdRequest
{
	/** The widget to get the width of. */
	protected final LcdWidget widget;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __w The widget to get the property from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public WidgetGetWidth(LcdServer __sv, LcdWidget __w)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_GET_WIDTH);
		
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.widget = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		return this.widget.getWidth();
	}
}

