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
import cc.squirreljme.runtime.lcdui.ui.UiDisplay;
import cc.squirreljme.runtime.lcdui.ui.UiDisplayable;
import cc.squirreljme.runtime.lcdui.ui.UiDisplayHead;

/**
 * This represents a local display used for an application being displayed
 * on the Swing interface.
 *
 * @since 2018/04/28
 */
public class SwingDisplay
	implements UiDisplay
{
	/** The display handle. */
	protected final int handle;
	
	/** The display head. */
	protected final SwingDisplayHead head;
	
	/**
	 * Initializes the swing display.
	 *
	 * @param __handle The handle of this display.
	 * @param __head The display head which is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/28
	 */
	public SwingDisplay(int __handle, SwingDisplayHead __head)
		throws NullPointerException
	{
		if (__head == null)
			throw new NullPointerException("NARG");
		
		this.handle = __handle;
		this.head = __head;
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
	public final UiDisplayable getCurrent()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final UiDisplayHead getDisplayHead()
	{
		throw new todo.TODO();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final void setCurrent(UiDisplayable __d)
	{
		throw new todo.TODO();
	}
}

