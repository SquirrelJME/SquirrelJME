// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents a ticker which can be used to display scrollable text on
 * the screen and show some important information as needed.
 *
 * @since 2018/04/04
 */
public final class UiTicker
	implements UiCollectable, UiInterface
{
	/** The handle for the ticker. */
	protected final int handle;
	
	/**
	 * Initializes the ticker.
	 *
	 * @param __handle The handle for the ticker.
	 * @since 2018/04/05
	 */
	public UiTicker(int __handle)
	{
		this.handle = __handle;
	}
	
	/**
	 * Adds a listener which is used as a callback when the text of a ticker
	 * has been changed.
	 *
	 * @param __l The listener to send updates to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void addTickerListener(UiTickerListener __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final void cleanup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final CollectableType collectableType()
	{
		return CollectableType.TICKER;
	}
	
	/**
	 * Gets the string being displayed on the ticker.
	 *
	 * @return The string currently being displayed.
	 * @since 2018/04/04
	 */
	public final String getString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final int handle()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes the specified ticker listener so that it is no longer informed
	 * of changes to this ticker.
	 *
	 * @param __l The listener to remove.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void removeTickerListener(UiTickerListener __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets the string to be displayed on the ticker.
	 *
	 * @param __s The string to display on the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void setString(String __s)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

