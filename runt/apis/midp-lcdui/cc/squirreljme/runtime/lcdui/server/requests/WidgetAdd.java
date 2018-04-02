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
 * Adds a widget to a widget.
 *
 * @since 2018/03/23
 */
public final class WidgetAdd
	extends LcdRequest
{
	/** The destination widget. */
	protected final LcdWidget destination;
	
	/** The widget to add. */
	protected final LcdWidget add;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __dest The destination widget.
	 * @param __add The widget to add.
	 * @throws NullPointerException
	 * @since 2018/03/23
	 */
	public WidgetAdd(LcdServer __sv, LcdWidget __dest, LcdWidget __add)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_ADD);
		
		if (__dest == null || __add == null)
			throw new NullPointerException("NARG");
		
		this.destination = __dest;
		this.add = __add;
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

