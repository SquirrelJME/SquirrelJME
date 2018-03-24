// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.WidgetType;

/**
 * This is the base class for a widget which may have operations performed
 * on it through widget handles.
 *
 * @since 2018/03/23
 */
public abstract class LcdWidget
{
	/** The handle of this widget. */
	protected final int handle;
	
	/** The type of widget this is. */
	protected final WidgetType type;
	
	/** The parent widget. */
	private LcdWidget _parent;
	
	/**
	 * Initializes the base widget.
	 *
	 * @param __handle The widget handle.
	 * @param __type The widget type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public LcdWidget(int __handle, WidgetType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.handle = __handle;
		this.type = __type;
	}
	
	/**
	 * Returns the height of the widget.
	 *
	 * @return The widget height.
	 * @since 2018/03/23
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the title of the widget.
	 *
	 * @return The widget title.
	 * @since 2018/03/23
	 */
	public abstract String getTitle();
	
	/**
	 * Returns the widget of the widget.
	 *
	 * @return The widget width.
	 * @since 2018/03/23
	 */
	public abstract int getWidth();
	
	/**
	 * Specifies that the widget should be repainted.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/23
	 */
	public abstract void repaint(int __x, int __y, int __w, int __h);
	
	/**
	 * Sets the title of the widget.
	 *
	 * @param __t The title to set, if {@code null} then a default should be
	 * used.
	 * @since 2018/03/23
	 */
	public abstract void setTitle(String __t);
	
	/**
	 * Returns the display that this is visible under or {@code null} if it is
	 * not associated with a display.
	 *
	 * @return The associated display or {@code null} if it is not associated
	 * with a display.
	 * @since 2018/03/23
	 */
	public final LcdDisplay getDisplay()
	{
		// Constantly go up until a display is reached
		for (LcdWidget w = this;;)
		{
			// No widget
			if (w == null)
				return null;
			
			// If it is a display then stop
			if (w instanceof LcdDisplay)
				return (LcdDisplay)w;
			
			// Go to the parent widget
			w = w._parent;
		}
	}
	
	/**
	 * Returns the widget handle.
	 *
	 * @return The widget handle.
	 * @since 2018/03/18
	 */
	public final int handle()
	{
		return this.handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return this.handle;
	}
}

