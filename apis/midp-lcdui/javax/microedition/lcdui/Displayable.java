// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.lcdui.DisplayManager;
import net.multiphasicapps.squirreljme.lcdui.event.EventType;
import net.multiphasicapps.squirreljme.lcdui.NativeResourceManager;
import net.multiphasicapps.squirreljme.lcdui.widget.DisplayableWidget;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
public abstract class Displayable
{
	/** The widget container. */
	final DisplayableWidget _widget;
	
	/** The display this is currently associated with. */
	volatile Display _current;
	
	/** The command listener to call into when commands are generated. */
	private volatile CommandListener _cmdlistener;
	
	/** The title of this displayable. */
	private volatile String _title;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
		// Setup widget for this displayable
		DisplayManager dm = DisplayManager.DISPLAY_MANAGER;
		DisplayableWidget widget = dm.createDisplayableWidget(
			new WeakReference<>(this));
		this._widget = widget;
		
		// Register it natively
		NativeResourceManager.RESOURCE_MANAGER.register(widget, this);
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
	
	public void addCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public Command getCommand(int __p)
	{
		throw new todo.TODO();
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new todo.TODO();
	}
	
	public Command[] getCommands()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display.
	 * @since 2016/10/08
	 */
	public Display getCurrentDisplay()
	{
		return this._current;
	}
	
	public Menu getMenu(int __p)
	{
		throw new todo.TODO();
	}
	
	public Ticker getTicker()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	public boolean isShown()
	{
		throw new todo.TODO();
	}
	
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void removeCommandOrMenu(int __p)
	{
		throw new todo.TODO();
	}
	
	public void setCommand(Command __c, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setCommandLayoutPolicy(CommandLayoutPolicy __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the command listener for this given displayable.
	 *
	 * @param __l The listener to use for callbacks, if {@code null} this
	 * the listener is cleared.
	 * @since 2017/08/19
	 */
	public void setCommandListener(CommandListener __l)
	{
		this._cmdlistener = __l;
	}
	
	public void setMenu(Menu __m, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setTicker(Ticker __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __a The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	public void setTitle(String __a)
	{
		this._title = __a;
		
		// TItle changed so an update is required
		Display current = this._current;
		if (current != null)
			current.__update();
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new heigh of the displayable.
	 * @since 2016/10/10
	 */
	@__SerializedEvent__
	protected void sizeChanged(int __w, int __h)
	{
		// Implemented by sub-classes
	}
	
	/**
	 * This returns the current display, one which is not potentially modified
	 * by sub-classes.
	 *
	 * @return The current display.
	 * @since 2017/10/01
	 */
	final Display __currentDisplay()
	{
		return this._current;
	}
	
	/**
	 * Returns the height of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable height or the maximum height of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getHeight()
	{
		Display d = __currentDisplay();
		return (d != null ? d.getHeight() :
			Display.getDisplays(0)[0].getHeight());
	}
	
	/**
	 * Returns the width of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable width or the maximum width of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getWidth()
	{
		Display d = __currentDisplay();
		return (d != null ? d.getWidth() :
			Display.getDisplays(0)[0].getWidth());
	}
	
	/**
	 * Handles an event.
	 *
	 * @param __t The type of event to perform.
	 * @param __c The activated command, if this is a command.
	 * @param __a The first parameter.
	 * @param __b The second parameter.
	 * @return {@code true} if the command was handled.
	 * @throws NullPointerException If no event type was specified.
	 * @since 2017/08/19
	 */
	boolean __handleEvent(EventType __t, Command __c, int __a, int __b)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends on the event
		switch (__t)
		{
				// Command activated
			case COMMAND:
				CommandListener cmdlistener = this._cmdlistener;
				if (cmdlistener != null)
					cmdlistener.commandAction(__c, this);
				return true;
			
				// Un-handled
			default:
				return false;
		}
	}
}


