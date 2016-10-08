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
	
	public Display getCurrentDisplay()
	{
		throw new Error("TODO");
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
	
	public String getTitle()
	{
		throw new Error("TODO");
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
	
	public void setTitle(String __a)
	{
		throw new Error("TODO");
	}
	
	protected void sizeChanged(int __a, int __b)
	{
		throw new Error("TODO");
	}
}


