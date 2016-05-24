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
 * This is used for any elements which can have an icon and text associated
 * with it.
 *
 * @since 2016/05/24
 */
public interface PIIconAndText
	extends PIBase
{
	/**
	 * Sets the icon for the element.
	 *
	 * @param __icon The icon to use, {@code null} clears it.
	 * @throws UIException If the icon could not be set.
	 * @since 2016/05/24
	 */
	public abstract void setIcon(UIImage __icon)
		throws UIException;
	
	/**
	 * Sets the text for the element.
	 *
	 * @param __text The text to use, {@code null} means an empty string.
	 * @throws UIException If the text could not be set.
	 * @since 2016/05/24
	 */
	public abstract void setText(String __text)
		throws UIException;
}

