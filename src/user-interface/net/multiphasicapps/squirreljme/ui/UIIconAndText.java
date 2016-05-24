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
 * This is an element which can be given an icon and text.
 *
 * @since 2016/05/24
 */
public interface UIIconAndText
{
	/**
	 * Returns the currently set icon.
	 *
	 * @return The image used for the icon, or {@code null} if none is set.
	 * @throws UIException If the icon could not be read.
	 * @since 2016/05/24
	 */
	public abstract UIImage getIcon()
		throws UIException;
	
	/**
	 * Returns the current set text.
	 *
	 * @return The text used for this element, or an empty string if none
	 * has been set.
	 * @since 2016/05/24
	 */
	public abstract String getText()
		throws UIException;
	
	/**
	 * Sets the icon for the element.
	 *
	 * @param __icon The icon to use, {@code null} clears it.
	 * @return The previously set icon.
	 * @throws UIException If the icon could not be set.
	 * @since 2016/05/24
	 */
	public abstract UIImage setIcon(UIImage __icon)
		throws UIException;
	
	/**
	 * Sets the text for the element.
	 *
	 * @param __text The text to use, {@code null} means an empty string.
	 * @return The previously set text.
	 * @throws UIException If the text could not be set.
	 * @since 2016/05/24
	 */
	public abstract String setText(String __text)
		throws UIException;
}

