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
 * This is the base class for displays. Displays contain canvases, widgets,
 * and may provide input events.
 *
 * This is equivalent to the LCD UI {@code Display} class.
 *
 * @since 2016/05/20
 */
public final class UIDisplay
	extends UIElement
{
	/** The title the display uses. */
	private volatile String _title;
	
	/** The icon that the display uses. */
	private volatile UIImage _icon;
	
	/** The menu that the display uses. */
	private volatile UIMenu _menu;

	/**
	 * Initializes the display wrapper.
	 *
	 * @param __dm The external display manager used.
	 * @since 2016/05/21
	 */
	UIDisplay(UIDisplayManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * Returns {@code true} if this display is visible to the user, but not
	 * necessarily seen by the user.
	 *
	 * In the event that the display is fully obscured by an overlay or perhaps
	 * another running application, the display will still be visible to the
	 * virtual machine itself. If the UI manager does not support multiple
	 * displays visible at the same time, then only a single display will
	 * ever be visible.
	 *
	 * @return {@code true} if the display is visible to the user.
	 * @throws UIException If the visibility state could not be determined.
	 * @since 2016/05/20
	 */
	public boolean isVisible()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return __internal().internalIsVisible();
		}
	}
	
	/**
	 * Sets the icon used to be displayed on the display.
	 *
	 * A display might not be capable of updating the icon if the image data
	 * changes, thus this should be called when an icon is required to take
	 * effect.
	 *
	 * @param __icon The image to use as the program icon, if {@code null} the
	 * icon is removed.
	 * @return The old icon.
	 * @throws UIException If the icon could not be changed or if the icon
	 * belongs to another display manager.
	 * @since 2016/05/22
	 */
	public UIImage setIcon(UIImage __icon)
		throws UIException
	{
		// {@squirreljme.error BD05 The image to use as the icon for a display
		// belongs to another display manager.}
		if (__icon != null && __icon.displayManager() != displayManager())
			throw new UIException("BD05");
		
		// Lock
		synchronized (this.lock)
		{
			// Get the old icon
			UIImage rv = this._icon;
			
			// Set the new icon
			__internal().internalSetIcon(__icon);
			
			// Set as used
			this._icon = __icon;
			
			// Return old
			return rv;
		}
	}
	
	/**
	 * Sets the menu that the display uses.
	 *
	 * @param __menu The menu to use, if {@code null} then no menu is used.
	 * @return The old menu which was used.
	 * @throws UIException If the menu could not be set.
	 * @since 2016/05/23
	 */
	public UIMenu setMenu(UIMenu __menu)
		throws UIException
	{
		// {@squirreljme.error BD08 The menu to be associated with a display
		// belongs to another display manager.}
		if (__menu != null && __menu.displayManager() != displayManager())
			throw new UIException("BD08");
		
		// Lock
		synchronized (this.lock)
		{
			// Get the old menu
			UIMenu rv = this._menu;
			
			// Get the display that owns this menu and remove it from another
			// display, since menus are only associated with a single display.
			UIDisplay disp = __menu.getDisplay();
			if (disp != null)
				if (disp == this)
					return __menu;
				else
					disp.setMenu(null);
				
			// Before a new menu can be set, the old menu must be cleared away
			if (rv != null)
			{
				__internal().internalSetMenu(null);
				this._menu = null;
				rv.__setDisplay(null);
			}
			
			// Set the new menu
			if (__menu != null)
			{
				__internal().internalSetMenu(__menu);
				this._menu = __menu;
				__menu.__setDisplay(this);
			}
			
			// Return old
			return rv;
		}
	}
	
	/**
	 * Sets the title of the display.
	 *
	 * @param __nt The new title to display, if {@code null} it is removed.
	 * @return The old title.
	 * @throws UIException If the title could not be set.
	 * @since 2016/05/22
	 */
	public String setTitle(String __nt)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Get the old title
			String rv = this._title;
			
			// Set new title
			__internal().internalSetTitle(__nt);
			
			// Set used title
			this._title = __nt;
			
			// Return old
			return rv;
		}
	}
	
	/**
	 * Sets whether or not this display is visible on the screen.
	 *
	 * In the event that the display manager can only display a single display
	 * at a time and this is the currently visible display, this will have no
	 * effect if visibility is being cleared.
	 *
	 * @param __vis If {@code true} then the display should be made visible to
	 * the user, otherwise it should be hidden.
	 * @return {@code true} if the visibility change was accepted and took
	 * effect or if the display was already in the given visibility state.
	 * @throws UIException If the visibility state could not be changed.
	 * @since 2016/05/21
	 */
	public boolean setVisible(boolean __vis)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Obtain the old visibility state
			boolean was = isVisible();
			
			// Set the new visibility state internally
			__internal().internalSetVisible(__vis);
			
			// Check if visibility changed
			return was != isVisible();
		}
	}
	
	/**
	 * Returns the internal display.
	 *
	 * @return The internal display.
	 * @since 2016/05/22
	 */
	final InternalDisplay __internal()
	{
		return super.<InternalDisplay>__internal(InternalDisplay.class);
	}
}

