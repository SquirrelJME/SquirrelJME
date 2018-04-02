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
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * Clears all of the widgets which are being displayed on this widget and
 * sets the single widget to be shown.
 *
 * @since 2018/03/23
 */
public final class WidgetClearAndSet
	extends LcdRequest
{
	/** The destination widget. */
	protected final LcdWidget destination;
	
	/** The widget to add. */
	protected final LcdWidget set;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __dest The destination widget.
	 * @param __set The widget to set.
	 * @throws NullPointerException
	 * @since 2018/03/23
	 */
	public WidgetClearAndSet(LcdServer __sv, LcdWidget __dest, LcdWidget __set)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_CLEAR_AND_SET);
		
		if (__dest == null || __set == null)
			throw new NullPointerException("NARG");
		
		this.destination = __dest;
		this.set = __set;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		LcdWidget destination = this.destination,
			set = this.set;
		
		// Clear widgets first
		destination.removeAll();
		
		// Then add one back
		destination.add(set);
		
		return VoidType.INSTANCE;
	}
}

