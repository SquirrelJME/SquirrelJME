// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.ui;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * This class represents a recursive menu.
 *
 * @since 2016/05/18
 */
public class RecursiveMenu
	extends AbstractList<Object>
	implements RandomAccess
{
	/** The menu's items. */
	protected final Object[] items;
	
	/** The cursor position. */
	private volatile int _cursor;
	
	/**
	 * Initializes the recursive menu.
	 *
	 * @param __items Items that exist in the menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public RecursiveMenu(Object... __items)
		throws NullPointerException
	{
		// Check
		if (__items == null)
			throw new NullPointerException("NARG");
		
		// Set
		items = __items;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public Object get(int __i)
	{
		return this.items[__i];
	}
	
	/**
	 * Returns the cursor position.
	 *
	 * @return The cursor position.
	 * @since 2016/05/18
	 */
	public int getCursor()
	{
		return _cursor;
	}
	
	/**
	 * Sets the cursor to the next item.
	 *
	 * @return The old cursor position.
	 * @since 2016/05/18
	 */
	public int nextItem()
	{
		return setCursor(getCursor() + 1);
	}
	
	/**
	 * Sets the cursor to the previous item.
	 *
	 * @return The old cursor position.
	 * @since 2016/05/18
	 */
	public int previousItem()
	{
		return setCursor(getCursor() - 1);
	}
	
	/**
	 * Sets the position of the cursor.
	 *
	 * @param __p The cursor position.
	 * @return The old cursor position.
	 * @since 2016/05/18
	 */
	public int setCursor(int __p)
	{
		int rv = _cursor;
		_cursor = Math.max(0, Math.min(this.items.length - 1, __p));
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public int size()
	{
		return this.items.length;
	}
}

