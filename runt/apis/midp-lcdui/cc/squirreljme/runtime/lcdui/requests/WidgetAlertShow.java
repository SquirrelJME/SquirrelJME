// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.requests;

import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * This shows an alert using a modal dialog, it is shown until it gets
 * dismissed eventually.
 *
 * @since 2018/03/23
 */
public final class WidgetAlertShow
	extends LcdRequest
{
	/** The widget to show as an alert. */
	protected final LcdWidget alert;
	
	/** The widget to show on exit. */
	protected final LcdWidget exit;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __alert The widget to show as an alert.
	 * @param __exit The widget to show on exit.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public WidgetAlertShow(LcdServer __sv, LcdWidget __alert,
		LcdWidget __exit)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_ALERT_SHOW);
		
		if (__alert == null || __exit == null)
			throw new NullPointerException("NARG");
		
		this.alert = __alert;
		this.exit = __exit;
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

