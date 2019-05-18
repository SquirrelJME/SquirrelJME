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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.phoneui.ExposedDisplayable;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.midlet.MIDlet;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
public abstract class Displayable
	extends ExposedDisplayable
{
	/** Commands which have been added to the displayable. */
	final __VolatileList__<Command> _commands =
		new __VolatileList__<>();
	
	/** The display this is attached to, if any. */
	volatile Display _display;
	
	/** The command listener to call into when commands are generated. */
	volatile CommandListener _cmdlistener;
	
	/** The title of the displayable. */
	volatile String _title;
	
	/** Display title to use. */
	volatile String _dtitle;
	
	/** The ticker of the displayable. */
	volatile Ticker _ticker;
	
	/** Is this widget shown? */
	volatile boolean _isshown;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
		// Use a default title for now
		this._dtitle = Displayable.__defaultTitle();
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
	 * Adds the specified command to this displayable, if it was already added
	 * then there is no effect (object refefences are checked).
	 *
	 * @param __c The command to add.
	 * @throws DisplayCapabilityException If this is being displayed and
	 * the display does not support commands.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	public void addCommand(Command __c)
		throws DisplayCapabilityException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB27 The display does not support commands.}
		Display cd = this.getCurrentDisplay();
		if (cd != null)
			if ((cd.getCapabilities() & Display.SUPPORTS_COMMANDS) == 0)
				throw new DisplayCapabilityException("EB27");
		
		// Add the command
		this._commands.addUniqueObjRef(__c);
		
		// Repaint display?
		Display d = this._display;
		if (d != null)
			d._phoneui.repaint();
	}
	
	public Command getCommand(int __p)
	{
		throw new todo.TODO();
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Gets the commands which are available to use.
	 *
	 * @return The available commands.
	 * @since 2019/05/17
	 */
	public Command[] getCommands()
	{
		return this._commands.toArray(new Command[0]);
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display or {@code null} if not found.
	 * @since 2016/10/08
	 */
	public Display getCurrentDisplay()
	{
		return this._display;
	}
	
	public Menu getMenu(int __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Gets the ticker which is being shown on this displayable.
	 *
	 * @return The ticker being shown or {@code null} if there is none.
	 * @since 2018/03/26
	 */
	public Ticker getTicker()
	{
		return this._ticker;
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
	
	/**
	 * Returns if this displayable is currently being shown.
	 *
	 * @return If the displayable is being shown.
	 * @since 2018/12/02
	 */
	public boolean isShown()
	{
		throw new todo.TODO();
		/*
		// Must be shown and have a parent, because anything without a
		// parent is invisible
		return this._isshown && this._parent != null;
		*/
	}
	
	/**
	 * Removes the specified command. If the command is {@code null} or it
	 * has never been added, this does nothing. If a command is removed then
	 * the display will be updated.
	 *
	 * @param __c The command to remove.
	 * @since 2019/04/15
	 */
	public void removeCommand(Command __c)
	{
		if (__c == null)
			return;
		
		throw new todo.TODO();
		/*
		// If the command was removed, then do an update
		if (this._commands.remove(__c))
		{
			// Update if the display is attached
			Display cd = this.__currentDisplay();
			if (cd != null)
				cd.__updateUIStack();
		}
		*/
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
	
	/**
	 * Sets or clears the ticker to be shown on this displayable.
	 *
	 * @param __t The ticker to be shown on the displayable or {@code null}
	 * to clear it.
	 * @since 2018/03/26
	 */
	public void setTicker(Ticker __t)
	{
		// Removing old ticker?
		Ticker old = this._ticker;
		if (__t == null)
		{
			// Nothing to do?
			if (old == null)
				return;
			
			// Clear
			this._ticker = null;
			
			// Remove from display list
			old._displayables.remove(this);
		}
		
		// Setting the same ticker?
		else if (old == __t)
			return;
		
		// Add new ticker, note they can be associated with many displays
		else
		{
			// Add to displayable list
			__t._displayables.addUniqueObjRef(this);
			
			// Set
			this._ticker = __t;
			
			// Update display
			Display d = this._display;
			if (d != null)
				d._phoneui.repaint();
		}
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __t The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	public void setTitle(String __t)
	{
		// Cache it for later return
		this._title = __t;
		
		// If no title is being set, fallback to a default one (derived from
		// the suite)
		if (__t == null)
			__t = Displayable.__defaultTitle();
		
		// Store this
		this._dtitle = __t;
		
		// Set the title of the display
		Display d = this._display;
		if (d != null)
			d._phoneui.setTitle(__t);
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new heigh of the displayable.
	 * @since 2016/10/10
	 */
	@SerializedEvent
	protected void sizeChanged(int __w, int __h)
	{
		// Implemented by sub-classes
	}
	
	/**
	 * Returns a default title to use for the application.
	 *
	 * @return Application default title.
	 * @since 2019/05/16
	 */
	private static final String __defaultTitle()
	{
		// Try getting a sensible name from a system property
		MIDlet amid = ActiveMidlet.optional();
		if (amid != null)
		{
			// MIDlet Name
			String midname = amid.getAppProperty("midlet-name");
			if (midname != null)
				return midname;
			
			// Otherwise this might not be a MIDlet, so just use the main
			// class instead
			String midclass = amid.getAppProperty("main-class");
			if (midclass != null)
				return midclass;
		}
		
		// Fallback to just using SquirrelJME
		return "SquirrelJME";
	}
	
	/**
	 * Returns the displayable height.
	 *
	 * @param __d The displayable.
	 * @param __full Return the full screen?
	 * @return The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	static final int __getHeight(Displayable __d, boolean __full)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Use dimension from default display
		Display display = __d._display;
		if (display == null)
			return Display.getDisplays(0)[0].getHeight();
		
		// Use content area size
		if (__full)
			return display._phoneui.height;
		return display._phoneui.contentHeight();
	}
	
	/**
	 * Returns the displayable width.
	 *
	 * @param __d The displayable.
	 * @param __full Return the full screen?
	 * @return The width.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	static final int __getWidth(Displayable __d, boolean __full)
	{
		if (__d == null)
			throw new NullPointerException("NARG");
			
		// Use dimension from default display
		Display display = __d._display;
		if (display == null)
			return Display.getDisplays(0)[0].getWidth();
		
		// Use content area size
		if (__full)
			return display._phoneui.width;
		return display._phoneui.contentWidth();
	}
}


