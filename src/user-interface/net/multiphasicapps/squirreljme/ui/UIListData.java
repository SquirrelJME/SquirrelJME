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

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * This class is used to internally provide a representation of data to be
 * stored within a list so that it may be displayed by a {@link UIList}. A
 * class which extends this can also optionally specificy alternative icons
 * and text which may be used when displaying elements.
 *
 * @param <E> The type of value stored in the list.
 * @since 2016/05/25
 */
public class UIListData<E>
	final AbstractList<E>
	implements RandomAccess
{
	/** The lock object. */
	protected final Object lock =
		this;
	
	/**
	 * This generates the icon which should be displayed when the list is
	 * drawn.
	 *
	 * @param __dx The index of the element.
	 * @param __v The value of the element.
	 * @return The icon to use for the item or {@code null} if it should have
	 * no icon.
	 * @throws UIException If the icon could not be generated.
	 * @since 2016/05/25
	 */
	public UIImage generateIcon(int __dx, E __v)
		throws UIException
	{
		return null;
	}
	
	/**
	 * This generates the text which should be displayed when the list is
	 * drawn.
	 *
	 * @param __dx The index of the element.
	 * @param __v The value of the element.
	 * @return The text that should be displayed on the label.
	 * @throws UIException If the icon could not be generated.
	 * @since 2016/05/25
	 */
	public String generateText(int __dx, E __v)
		throws UIException
	{
		synchronized (this.lock)
		{
			return String.valueOf(__v);
		}
	}
}

