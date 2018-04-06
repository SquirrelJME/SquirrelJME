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
 * This is a menu which contains multiple menu items.
 *
 * @since 2018/04/04
 */
public final class UiMenu
	extends UiMenuItem
	implements UiCollectable, UiHasSettableImage, UiHasSettableLabel,
		UiHasSettableLongLabel, UiInterface
{
	/** The handle for the menu. */
	protected final int handle;
	
	/**
	 * Initializes the menu.
	 *
	 * @param __handle The handle for the menu.
	 * @since 2018/04/05
	 */
	public UiMenu(int __handle)
	{
		this.handle = __handle;
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final UiImage getImage()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final String getLabel()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final String getLongLabel()
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
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final void setImage(UiImage __i)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final void setLabel(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final void setLongLabel(String __s)
	{
		throw new todo.TODO();
	}
}

