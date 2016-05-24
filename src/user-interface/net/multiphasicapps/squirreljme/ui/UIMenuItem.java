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
	implements UIIconAndText
{
	/** Icon and text data. */
	private final __IconText__ _it =
		new __IconText__(this);
	
	/** The menu which contains this item. */
	private volatile UIMenu _parent;
	
	/**
	 * Initializes the menu item.
	 *
	 * @param __dm The display manager owning this.
	 * @since 2016/05/23
	 */
	public UIMenuItem(UIManager __dm)
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
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final UIImage getIcon()
	{
		return this._it.getIcon();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final String getText()
	{
		return this._it.getText();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public final UIImage setIcon(UIImage __icon)
		throws UIException
	{
		return this._it.setIcon(__icon);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public final String setText(String __text)
		throws UIException
	{
		return this._it.setText(__text);
	}
	
	/**
	 * Sets the parent for this menu item.
	 *
	 * @param __p The parent menu, {@code null} clears it.
	 * @return The old parent.
	 * @since 2016/05/23
	 */
	final UIMenu __setParent(UIMenu __p)
	{
		// Lock
		synchronized (this.lock)
		{
			UIMenu rv = this._parent;
			this._parent = __p;
			return rv;
		}
	}
}

