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
 * This represents a menu item inside of a menu, it has a label and an assigned
 * label.
 *
 * @since 2016/05/23
 */
public class UIMenuItem
	extends UIBase
{
	/** The menu which contains this item. */
	private volatile UIMenu _parent;
	
	/** The current menu text. */
	private volatile String _text =
		"";
	
	/** The current icon used. */
	private volatile UIImage _icon;
	
	/**
	 * Initializes the menu item.
	 *
	 * @param __dm The display manager owning this.
	 * @since 2016/05/23
	 */
	UIMenuItem(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * Returns the parent menu if this is a sub-menu of another menu.
	 *
	 * @return The menu which is the parent of this menu or {@code null} if
	 * this is a top level menu.
	 * @since 2016/05/23
	 */
	public final UIMenu getParent()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._parent;
		}
	}
	
	/**
	 * Sets the icon that this menu item should use when it is displayed.
	 *
	 * @param __icon The image to use for the item, {@code null} removes the
	 * set icon.
	 * @return The old icon which was previously used.
	 * @throws UIException If the icon could not be set.
	 * @since 2016/05/23
	 */
	public final UIImage setIcon(UIImage __icon)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Get old
			UIImage rv = this._icon;
			
			// Set the new icon
			this.<PIMenuItem>platform(PIMenuItem.class).setIcon(__icon);
			this._icon = __icon;
			
			// Return the old one
			return rv;
		}
	}
	
	/**
	 * Sets the text of the menu item.
	 *
	 * @param __text The text to display for the menu item, {@code null} is
	 * treated as a blank string.
	 * @return The old text which was previously used.
	 * @throws UIException If the text could not be set.
	 * @since 2016/05/23
	 */
	public final String setText(String __text)
		throws UIException
	{
		// If null, becomes blank
		if (__text == null)
			__text = "";
		
		// Lock
		synchronized (this.lock)
		{
			// Get old
			String rv = this._text;
			
			// Set the new text
			this.<PIMenuItem>platform(PIMenuItem.class).setText(__text);
			this._text = __text;
			
			// Return it
			return rv;
		}
	}
}

