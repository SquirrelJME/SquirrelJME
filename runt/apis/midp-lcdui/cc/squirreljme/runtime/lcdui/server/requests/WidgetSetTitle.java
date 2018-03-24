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
 * Sets the title of a widget.
 *
 * @since 2018/03/23
 */
public class WidgetSetTitle
	extends LcdRequest
{
	/** The widget to set the title for. */
	protected final LcdWidget widget;
	
	/** The title to set. */
	protected final String title;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __w The widget to set the title for.
	 * @param __t The title to set.
	 * @throws NullPointerException On null arguments, except for the null
	 * title.
	 * @since 2018/03/23
	 */
	public WidgetSetTitle(LcdServer __sv, LcdWidget __w, String __t)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_SET_TITLE);
		
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.widget = __w;
		this.title = __t;
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

