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

import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.server.LcdTicker;
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * Gets the ticker from a widget.
 *
 * @since 2018/03/26
 */
public final class WidgetGetTicker
	extends LcdRequest
{
	/** The widget to get the ticker from. */
	protected final LcdWidget widget;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __w The widget to get the ticker from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public WidgetGetTicker(LcdServer __sv, LcdWidget __w)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_SET_TITLE);
		
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.widget = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	protected final Object invoke()
	{
		LcdTicker rv = this.widget.getTicker();
		if (rv == null)
			return Integer.MIN_VALUE;
		return rv.handle();
	}
}

