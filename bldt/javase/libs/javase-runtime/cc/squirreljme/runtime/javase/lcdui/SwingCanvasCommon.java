// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.ui.UiHasShownCallback;
import cc.squirreljme.runtime.lcdui.ui.UiInputEvents;
import cc.squirreljme.runtime.lcdui.ui.UiPaintable;
import cc.squirreljme.runtime.lcdui.ui.UiWidget;

/**
 * This acts as the base class for the canvases and custom items since they
 * are essentially the same.
 *
 * @since 2018/04/28
 */
public abstract class SwingCanvasCommon
	implements UiHasShownCallback, UiInputEvents, UiPaintable, UiWidget
{
	/** The handle of the canvas. */
	protected final int handle;
	
	/**
	 * Initializes the common canvas.
	 *
	 * @param __handle The handle of the canvas.
	 * @since 2018/04/28
	 */
	public SwingCanvasCommon(int __handle)
	{
		this.handle = __handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int getHeight()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int getWidth()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int handle()
	{
		return this.handle;
	}
}

