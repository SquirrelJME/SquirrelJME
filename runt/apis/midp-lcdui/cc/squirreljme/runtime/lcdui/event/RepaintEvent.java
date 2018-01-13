// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import javax.microedition.lcdui.Canvas;

/**
 * This specifies that the current displayable should be repainted.
 *
 * @since 2017/10/24
 */
public final class RepaintEvent
	implements Event
{
	/** The display ID. */
	public final int headid;
	
	/** The coordinates. */
	public final int x, y;
	
	/** The dimensions. */
	public final int width, height;
	
	/**
	 * Initializes the repaint event.
	 *
	 * @param __id The display head ID.
	 * @param __x The x coordinate.
	 * @param __y The y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public RepaintEvent(int __id, int __x, int __y, int __w, int __h)
	{
		this.headid = __id;
		this.x = __x;
		this.y = __y;
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/24
	 */
	@Override
	public EventType type()
	{
		return EventType.REPAINT;
	}
}

