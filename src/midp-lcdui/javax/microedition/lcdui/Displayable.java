// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

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
	
	/** The title of this displayable. */
	volatile String _title;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
	}
	
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
	
	public int getHeight()
	{
		throw new Error("TODO");
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
	
	public int getWidth()
	{
		throw new Error("TODO");
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
		this._title = __a;
		
		// Lock
		synchronized (this._lock)
		{
			// Update the title used by the display as needed
			Display display = this._display;
			if (display != null)
				display.__updateTitle(getTitle());
		}
	}
	
	protected void sizeChanged(int __a, int __b)
	{
		throw new Error("TODO");
	}
}


