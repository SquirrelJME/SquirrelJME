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
{
	/** Commands which have been added to the displayable. */
	final __VolatileList__<Command> _commands =
		new __VolatileList__<>();
	
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
		this.__setTitle(null);
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
		
		throw new todo.TODO();
		/*
		// Update the display if attached
		if (cd != null)
			cd.__updateUIStack();
		*/
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
		throw new todo.TODO();
		/*
		return this.__currentDisplay();
		*/
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
		throw new todo.TODO();
		/*
		return __Queue__.INSTANCE.<Ticker>__get(Ticker.class,
			LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.WIDGET_GET_TICKER, this._handle));
		*/
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
		if (true)
			throw new todo.TODO();
		/*
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.WIDGET_SET_TICKER, this._handle, (__t == null ?
				Integer.MIN_VALUE : __t._handle));
		*/
		
		// Cache it
		this._ticker = __t;
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __t The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	public void setTitle(String __t)
	{
		// Call internal title set
		this.__setTitle(__t);
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
	 * Sets the title of this displayable, internal logic to.
	 *
	 * @param __t The title to use, {@code null} clears it.
	 * @since 2019/04/14
	 */
	void __setTitle(String __t)
	{
		// Cache it for later return
		this._title = __t;
		
		// If no title is being set, fallback to a default one (derived from
		// the suite)
		if (__t == null)
		{
			// Try getting a sensible name from a system property
			MIDlet amid = ActiveMidlet.optional();
			if (amid != null)
			{
				// MIDlet Name
				__t = amid.getAppProperty("midlet-name");
				
				// Otherwise this might not be a MIDlet, so just use the main
				// class instead
				if (__t == null)
					__t = amid.getAppProperty("main-class");
			}
			
			// Fallback to just using SquirrelJME
			if (__t == null)
				__t = "SquirrelJME";
		}
		
		// Store this
		this._dtitle = __t;
		
		// Set the title of the display
		throw new todo.TODO();
		/*
		Display d = this.__currentDisplay();
		if (d != null)
			d._phoneui.setTitle(__t);
		*/
	}
}


