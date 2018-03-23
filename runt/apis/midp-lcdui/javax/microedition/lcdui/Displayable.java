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

import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.event.EventType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
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
	extends __Cleanupable__
{
	/** Held current display to prevent garbage collection. */
	volatile Display _heldcurrent;
	
	/** The command listener to call into when commands are generated. */
	private volatile CommandListener _cmdlistener;
	
	/** Cached title of the displayable. */
	private volatile String _cachetitle;
	
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
		throw new todo.TODO();
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
		// Return the cached title so that a remote call does not need to
		// be performed
		return this._cachetitle;
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
		// Cache it for later return
		this._cachetitle = __a;
		
		// Set title remotely
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.DISPLAYABLE_SET_TITLE, this._handle, __a);
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
		throw new todo.TODO();
	}
	
	/**
	 * Specifies that this canvas is to be repainted.
	 *
	 * @param __g The graphics to draw in.
	 * @since 2017/10/24
	 */
	@__SerializedEvent__
	void __doRepaint(Graphics __g)
	{
		// Default implementation does nothing
	}
	
	/**
	 * This is called when the displayable has changed size.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/23
	 */
	void __doResize(int __w, int __h)
	{
		this.sizeChanged(__w, __h);
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
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.DISPLAYABLE_GET_HEIGHT, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getHeight();
		return rv;
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
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.DISPLAYABLE_GET_WIDTH, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getWidth();
		return rv;
	}
	
	/**
	 * Returns the display head identifier.
	 *
	 * @return The ID of the current display head.
	 * @since 2017/10/27
	 */
	@Deprecated
	final int __headId()
	{
		throw new todo.TODO();
		/*
		Display d = __currentDisplay();
		if (d == null)
			return -1;
		
		return d._head.headId();*/
	}
}


