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

import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * Repaints a widget.
 *
 * @since 2018/03/23
 */
public final class WidgetRepaint
	extends LcdRequest
{
	/** The widget to repaint. */
	protected final LcdWidget widget;
	
	/** The X coordinate. */
	protected final int x;
	
	/** The Y coordinate. */
	protected final int y;
	
	/** The width. */
	protected final int width;
	
	/** The height. */
	protected final int height;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __wid The widget to repaint.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public WidgetRepaint(LcdServer __sv, LcdWidget __wid, int __x, int __y,
		int __w, int __h)
		throws NullPointerException
	{
		super(__sv, LcdFunction.WIDGET_REPAINT);
		
		if (__wid == null)
			throw new NullPointerException("NARG");
		
		this.widget = __wid;
		this.x = __x;
		this.y = __y;
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		this.widget.repaint(this.x, this.y, this.width, this.height);
		return VoidType.INSTANCE;
	}
}

