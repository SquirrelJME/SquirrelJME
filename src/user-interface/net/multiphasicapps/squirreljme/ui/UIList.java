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
 * @param <E> The type of element to store in the list.
 * @since 2016/05/24
 */
public class UIList<E>
	extends UIComponent
{
	/** The type of data to store in this list. */
	protected final Class<E> type;
	
	/** The class data to use. */
	protected final UIListData<E> data;
	
	/**
	 * Initializes the icon box.
	 *
	 * @param __dm The owning display manager.
	 * @param __cl The type of data to store in the list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/26
	 */
	public UIList(UIManager __dm, Class<E> __cl)
	{
		this(__dm, __cl, new UIListData<E>(__cl));
	}
	
	/**
	 * Initializes the list using the specified list data.
	 *
	 * @param __dm The owning display manager.
	 * @param __cl The type of data to store in the list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/24
	 */
	public UIList(UIManager __dm, Class<E> __cl, UIListData<E> __ld)
	{
		super(__dm);
		
		// Check
		if (__cl == null || __ld == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __cl;
		this.data = __ld;
	}
}

