// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayInstance;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
public abstract class Displayable
{
	/** The lock on this displayable. */
	final Object _lock =
		new Object();
	
	/** The display that this is currently associated with. */
	volatile Display _display;
	
	/** The instance that allows this to directly interact with the item. */
	volatile DisplayInstance _instance;
	
	/** The title of this displayable. */
	volatile String _title;
	
	/** The image that backs this displayable. */
	volatile Image _image;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
	}
	
	/**
	 * Returns the height of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current height of this displayable in pixels, if it is not
	 * visible then the default height is returned.
	 * @since 2017/02/08
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the width of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current width of this displayable in pixels, if it is not
	 * visible then the default width is returned.
	 * @since 2017/02/08
	 */
	public abstract int getWidth();
	
	/**
	 * Creates a connector which can better interact with this displayable.
	 *
	 * @return The displayable for connecting to this object, it may be
	 * cached.
	 * @since 2017/02/08
	 */
	abstract DisplayConnector __connector();
	
	public void addCommand(Command __a)
	{
		throw new Error("TODO");
	}
	
	public Command getCommand(int __p)
	{
		throw new Error("TODO");
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new Error("TODO");
	}
	
	public Command[] getCommands()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display.
	 * @since 2016/10/08
	 */
	public Display getCurrentDisplay()
	{
		return this._display;
	}
	
	public Menu getMenu(int __p)
	{
		throw new Error("TODO");
	}
	
	public Ticker getTicker()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the title of this displayable.
	 *
	 * @return The title of this displayable.
	 * @since 2016/10/08
	 */
	public String getTitle()
	{
		return this._title;
	}
	
	public void invalidateCommandLayout()
	{
		throw new Error("TODO");
	}
	
	public boolean isShown()
	{
		throw new Error("TODO");
	}
	
	public void removeCommand(Command __a)
	{
		throw new Error("TODO");
	}
	
	public void removeCommandOrMenu(int __p)
	{
		throw new Error("TODO");
	}
	
	public void setCommand(Command __c, int __p)
	{
		throw new Error("TODO");
	}
	
	public void setCommandLayoutPolicy(CommandLayoutPolicy __p)
	{
		throw new Error("TODO");
	}
	
	public void setCommandListener(CommandListener __a)
	{
		throw new Error("TODO");
	}
	
	public void setMenu(Menu __m, int __p)
	{
		throw new Error("TODO");
	}
	
	public void setTicker(Ticker __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __a The title to use.
	 * @since 2016/10/08
	 */
	public void setTitle(String __a)
	{
		// Set
		this._title = __a;
		
		// Set the title to use
		DisplayInstance instance = this._instance;
		if (instance != null)
			instance.setTitle(__a);
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new heigh of the displayable.
	 * @since 2016/10/10
	 */
	protected void sizeChanged(int __w, int __h)
	{
	}
}


