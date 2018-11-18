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

import cc.squirreljme.runtime.lcdui.event.EventType;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
public abstract class Displayable
	extends __Widget__
{
	/** Commands which have been added to the displayable. */
	final __VolatileList__<Command> _commands =
		new __VolatileList__<>();
	
	/** The command listener to call into when commands are generated. */
	volatile CommandListener _cmdlistener;
	
	/** The title of the displayable. */
	volatile String _title;
	
	/** The ticker of the displayable. */
	volatile Ticker _ticker;
	
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
		
		this._commands.addUniqueObjRef(__c);
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
		// Since the widget might be part of a tabbed pane the parent will
		// technically not be a display, so recursively go up until one is
		// reached
		for (__Widget__ w = this._parent; w != null; w = w._parent)
			if (w instanceof Display)
				return (Display)w;
		
		return null;
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
		// Return the cached title so that a remote call does not need to
		// be performed
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
	 * @param __a The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	public void setTitle(String __a)
	{
		// Cache it for later return
		this._title = __a;
		
		// Set title remotely
		if (true)
			throw new todo.TODO();
		/*
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.WIDGET_SET_TITLE, this._handle, __a);
		*/
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
	 * This returns the current display, one which is not potentially modified
	 * by sub-classes.
	 *
	 * @return The current display.
	 * @since 2017/10/01
	 */
	final Display __currentDisplay()
	{
		throw new todo.TODO();
	}
}


