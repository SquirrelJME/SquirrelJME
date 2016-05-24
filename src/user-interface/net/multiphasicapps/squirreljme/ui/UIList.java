// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This handles a list which contains items to be displayed, the displayed
 * items may have an icon and a text associated with them.
 *
 * @since 2016/05/24
 */
public class UIList
	extends UIComponent
{
	/**
	 * Initializes the icon box.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/24
	 */
	public UIList(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * Obtains the item at the specified index.
	 *
	 * @param __i The index to get the item for.
	 * @return The item at the given position.
	 * @throws UIException If the item could not be obtained.
	 * @since 2016/05/24
	 */
	public final Object get(int __i)
		throws UIException
	{
		// {@squirreljme.error BD0i Request for a list item which has a
		// negative index.}
		if (__i < 0)
			throw new UIException("BD0i");
		
		// Lock
		synchronized (this.lock)
		{
			try
			{
				throw new Error("TODO");
			}
			
			// {@squirreljme.error BD0j Request for a list item which exceeds
			// the bounds of the list.}
			catch (IndexOutOfBoundsException e)
			{
				throw new UIException("BD0j", e);
			}
		}
	}
	
	/**
	 * Returns the number of elements in the list.
	 *
	 * @return The elements in the list.
	 * @throws UIException If the size could not be determined.
	 * @since 2016/05/24
	 */
	public final int size()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

