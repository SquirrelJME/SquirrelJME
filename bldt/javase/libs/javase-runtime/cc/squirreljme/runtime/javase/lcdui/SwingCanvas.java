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
import cc.squirreljme.runtime.lcdui.ui.UiCanvas;
import cc.squirreljme.runtime.lcdui.ui.UiMenuLayout;

/**
 * This represents a canvas which is used to display graphics in Swing in
 * a dedicated displayable.
 *
 * @since 2018/04/28
 */
public class SwingCanvas
	extends SwingCanvasCommon
	implements UiCanvas
{
	/**
	 * Initializes the canvas.
	 *
	 * @param __handle The handle of the canvas.
	 * @since 2018/04/28
	 */
	public SwingCanvas(int __handle)
	{
		super(__handle);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final void cleanup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final CollectableType collectableType()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String getTitle()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final void layoutMenuItems(UiMenuLayout __ml)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final void setTitle(String __t)
	{
		throw new todo.TODO();
	}
}

