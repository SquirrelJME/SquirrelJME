// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.MenuAction;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionHasChildren;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNode;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNodeOnly;
import cc.squirreljme.runtime.lcdui.scritchui.MenuLayoutLock;
import cc.squirreljme.runtime.lcdui.scritchui.StringTracker;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.midlet.MIDlet;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

/**
 * A displayable is a primary container such as a form or a canvas that can be
 * set on a display. A display may only have a single displayable associated
 * with it and a displayable may only be associated with a single display.
 *
 * @since 2016/10/08
 */
@Api
@SuppressWarnings("OverlyComplexClass")
public abstract class Displayable
	extends MenuActionNodeOnly
	implements MenuActionHasChildren
{
	/** The displayable state. */
	private final DisplayableState _state;
	
	/** The command listener to call into when commands are generated. */
	volatile CommandListener _cmdListener;
	
	/** The ticker of the displayable. */
	@Deprecated
	volatile Ticker _ticker;
	
	/** The layout policy of this displayable. */
	@Deprecated
	private CommandLayoutPolicy _layoutPolicy;
	
	/** The tracker for title text. */
	final StringTracker _trackerTitle;
	
	/** The lock for layout editing and otherwise. */
	final MenuLayoutLock _layoutLock =
		new MenuLayoutLock();
	
	/** The default menu. */
	final Menu _menuDefault;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
		// Setup new state
		DisplayableState state = new DisplayableState(this);
		this._state = state;
		
		// Setup tracker for title changes, it needs the event loop handler
		this._trackerTitle = new StringTracker(state.scritchApi().eventLoop(),
			Displayable.__defaultTitle());
		
		// Setup default menu
		Menu menuDefault = new Menu("App", "Application",
			null);
		this._menuDefault = menuDefault;
		
		// Root menu bar for the displayable
		MenuActionNode menuNode = MenuActionNodeOnly.node(this);
		
		// Make sure the menu is actually in it
		menuNode.insert(0, menuDefault);
	}
	
	/**
	 * Returns the height of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current height of this displayable in pixels, if it is not
	 * visible then the default height is returned.
	 * @since 2017/02/08
	 */
	@Api
	public abstract int getHeight();
	
	/**
	 * Returns the width of this displayable's content area in pixels (the
	 * area the developer can use).
	 *
	 * @return The current width of this displayable in pixels, if it is not
	 * visible then the default width is returned.
	 * @since 2017/02/08
	 */
	@Api
	public abstract int getWidth();
	
	/**
	 * Adds the specified command to this displayable, if it was already added
	 * then there is no effect (object references are checked).
	 *
	 * @param __c The command to add.
	 * @throws DisplayCapabilityException If this is being displayed and
	 * the display does not support commands.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	@Api
	public void addCommand(Command __c)
		throws DisplayCapabilityException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Have the event loop handle this
		this.__state().scritchApi().eventLoop()
			.loopExecute(new __ExecDisplayableDefaultCommand__(this,
				__c, true));
	}
	
	@Api
	public Command getCommand(int __p)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current command layout policy. The policy here takes
	 * precedence over the one set in {@link Display}.
	 * 
	 * @return The current command layout policy, may be {@code null}.
	 * @since 2020/09/27
	 */
	@Api
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		return this._layoutPolicy;
	}
	
	/**
	 * Returns the command listener.
	 * 
	 * @return The command listener.
	 * @since 2019/05/18
	 */
	@Api
	protected CommandListener getCommandListener()
	{
		return this.__getCommandListener();
	}
	
	/**
	 * Gets the commands which are available to use.
	 *
	 * @return The available commands.
	 * @since 2019/05/17
	 */
	@Api
	public Command[] getCommands()
	{
		try (MenuLayoutLock lock = this._layoutLock.open(false))
		{
			if (true)
				throw Debugging.todo();
		}
		
		throw Debugging.todo();
		/*
		List<Command> rv = new ArrayList<>();
		for (__Action__ a : this._actions)
			if (a instanceof Command)
				rv.add((Command)a);
		return rv.<Command>toArray(new Command[rv.size()]);
		
		 */
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display or {@code null} if not found.
	 * @since 2016/10/08
	 */
	@Api
	public Display getCurrentDisplay()
	{
		return this.__getCurrentDisplay();
	}
	
	@Api
	public Menu getMenu(int __p)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Gets the ticker which is being shown on this displayable.
	 *
	 * @return The ticker being shown or {@code null} if there is none.
	 * @since 2018/03/26
	 */
	@Api
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
	@Api
	public String getTitle()
	{
		return this._trackerTitle.get();
	}
	
	/**
	 * Invalidates the command layout and forces it to be recalculated, if the
	 * display is not visible or focused then this will be ignored.
	 * 
	 * @since 2020/09/27
	 */
	@Api
	public void invalidateCommandLayout()
	{
		if (this.__isShown())
			this.__layoutCommands();
	}
	
	/**
	 * Returns if this displayable is currently being shown.
	 *
	 * @return If the displayable is being shown.
	 * @since 2018/12/02
	 */
	@Api
	public boolean isShown()
	{
		return this.__isShown();
	}
	
	/**
	 * Removes the specified command. If the command is {@code null} or it
	 * has never been added, this does nothing. If a command is removed then
	 * the display will be updated.
	 *
	 * @param __c The command to remove.
	 * @since 2019/04/15
	 */
	@Api
	public void removeCommand(Command __c)
	{
		if (__c == null)
			return;
		
		// There is an implicit action if this is a list where this will
		// clear the select command if it is removed in this way
		if (this instanceof List)
		{
			List self = ((List)this);
			synchronized (this)
			{
				if (self._selCommand == __c)
				{
					// Calling this will clear the select command first
					// then follow with an actual removal
					self.__setSelectCommand(null);
					return;
				}
			}
		}
		
		// Have the event loop handle this
		this.__state().scritchApi().eventLoop()
			.loopExecute(new __ExecDisplayableDefaultCommand__(this,
				__c, false));
	}
	
	@Api
	public void removeCommandOrMenu(int __p)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the command at the given position.
	 * 
	 * @param __c The command to set.
	 * @param __p The position to set.
	 * @throws DisplayCapabilityException If the display does not support
	 * commands.
	 * @throws IllegalArgumentException If the placement is not valid.
	 * @throws IllegalStateException If this was not called from within the
	 * {@link CommandLayoutPolicy#onCommandLayout(Displayable)} method or
	 * the command layout passed is not the same.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	@Api
	public void setCommand(Command __c, int __p)
		throws DisplayCapabilityException, IllegalArgumentException,
			IllegalStateException, NullPointerException 
	{
		try (MenuLayoutLock lock = this._layoutLock.open(false))
		{
			if (true)
				throw Debugging.todo();
		}
		
		throw Debugging.todo();
		/*
		this.__layoutActionSet(__c, __p);
		
		 */
	}
	
	/**
	 * Sets the command layout policy to use for any commands or menu items.
	 * 
	 * @param __p The policy to use, {@code null} will use the default one for
	 * the display.
	 * @since 2021/11/30
	 */
	@Api
	public void setCommandLayoutPolicy(CommandLayoutPolicy __p)
	{
		this._layoutPolicy = __p;
	}
	
	/**
	 * Sets the command listener for this given displayable.
	 *
	 * @param __l The listener to use for callbacks, if {@code null} this
	 * the listener is cleared.
	 * @since 2017/08/19
	 */
	@Api
	public void setCommandListener(CommandListener __l)
	{
		synchronized (this)
		{
			this._cmdListener = __l;
		}
	}
	
	/**
	 * Sets the menu at the given position.
	 * 
	 * @param __m The menu to set.
	 * @param __p The position to set.
	 * @throws DisplayCapabilityException If the display does not support
	 * commands.
	 * @throws IllegalArgumentException If the placement is not valid.
	 * @throws IllegalStateException If this was not called from within the
	 * {@link CommandLayoutPolicy#onCommandLayout(Displayable)} method or
	 * the command layout passed is not the same.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	@Api
	public void setMenu(Menu __m, int __p)
		throws DisplayCapabilityException, IllegalArgumentException,
			IllegalStateException, NullPointerException 
	{
		try (MenuLayoutLock lock = this._layoutLock.open(false))
		{
			if (true)
				throw Debugging.todo();
		}
		
		throw Debugging.todo();
		/*
		this.__layoutActionSet(__m, __p);
		
		 */
	}
	
	/**
	 * Sets or clears the ticker to be shown on this displayable.
	 *
	 * @param __t The ticker to be shown on the displayable or {@code null}
	 * to clear it.
	 * @since 2018/03/26
	 */
	@Api
	public void setTicker(Ticker __t)
	{
		throw Debugging.todo();
		/*
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
			
			// Perform ticker update
			this.__updateTicker();
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
			
			// Perform ticker updates
			this.__updateTicker();
		}
		
		 */
	}
	
	/**
	 * Sets the title of this displayable.
	 *
	 * @param __t The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	@Api
	public void setTitle(String __t)
	{
		this._trackerTitle.set(__t);
	}
	
	/**
	 * This is called when the size of the displayable has changed.
	 *
	 * @param __w The new width of the displayable.
	 * @param __h The new height of the displayable.
	 * @since 2016/10/10
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	protected void sizeChanged(int __w, int __h)
	{
		// Implemented by subclasses
	}
	
	/**
	 * Rebuilds the displayable menu.
	 * 
	 * @since 2024/07/18
	 */
	@ScritchEventLoop
	@SerializedEvent
	@Async.Execute
	void __execMenuRebuild()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Performs revalidation for ScritchUI, overridden as needed.
	 *
	 * @param __parent The parent display.
	 * @since 2024/03/18
	 */
	@ScritchEventLoop
	@SerializedEvent
	@Async.Execute
	@MustBeInvokedByOverriders
	void __execRevalidate(DisplayState __parent)
	{
		// Reparent the display
		this.__state().setParent(__parent);
		
		// Get the display scale to determine how big the widget should be
		DisplayScale scale = __parent.display()._scale;
		
		// Get the current texture size of the window
		int w = Math.max(1, scale.textureW());
		int h = Math.max(1, scale.textureH());
		
		// Set absolute bounds of this displayable
		DisplayableState state = this.__state();
		state.scritchApi().container().containerSetBounds(
			__parent.scritchWindow(),
			state.scritchPanel(), 0, 0, w, h);
	}
	
	/**
	 * Returns the command listener.
	 * 
	 * @return The command listener.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	CommandListener __getCommandListener()
	{
		synchronized (this)
		{
			return this._cmdListener;
		}
	}
	
	/**
	 * Returns the display that is associated with this displayable.
	 *
	 * @return The owning display or {@code null} if not found.
	 * @since 2017/07/18
	 */
	@SquirrelJMEVendorApi
	private Display __getCurrentDisplay()
	{
		DisplayState display = this.__state().currentDisplay();
		
		if (display != null)
			return display.display();
		return null;
	}
	
	/**
	 * Returns if this displayable is currently being shown.
	 *
	 * @return If the displayable is being shown.
	 * @since 2020/09/27
	 */
	final boolean __isShown()
	{
		throw Debugging.todo();
		/*
		// If there is no display then this cannot possibly be shown
		Display display = this._display;
		if (display == null)
			return false;
		
		// When checking if shown, actually probe the current form on the
		// display as another task may have taken the display from us
		UIBackend backend = this.__backend();
		return backend.equals(this.__state(__DisplayableState__.class)._uiForm,
			backend.displayCurrent(display._uiDisplay));
			
		 */
	}
	
	/**
	 * Sets the command or menu at the given position.
	 * 
	 * @param __a The action to set.
	 * @param __p The position to set.
	 * @throws DisplayCapabilityException If the display does not support
	 * commands.
	 * @throws IllegalArgumentException If the placement is not valid.
	 * @throws IllegalStateException If this was not called from within the
	 * {@link CommandLayoutPolicy#onCommandLayout(Displayable)} method or
	 * the command layout passed is not the same.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	private void __layoutActionSet(MenuAction __a, int __p)
		throws DisplayCapabilityException, IllegalArgumentException,
			IllegalStateException, NullPointerException 
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
		/*
		/* {@squirreljme.error EB3i The current display does not support
		commands.} * /
		Display display = this._display;
		int caps = (display == null ? Display.__defaultCapabilities() :
			display.getCapabilities());
		if (0 == (caps & Display.SUPPORTS_COMMANDS))
			throw new IllegalArgumentException("EB3i");
		
		/* {@squirreljme.error EB3h The current displayable is not getting
		its layout calculated.} * /
		__Layout__ layout = this._layout;
		if (layout == null)
			throw new IllegalStateException("EB3h");
		
		// Forward to the layout
		layout.set(__a, __p);
		
		 */
	}
	
	/**
	 * Performs the laying out the commands, in the event they have changed
	 * or otherwise.
	 * 
	 * @since 2020/09/27
	 */
	@SerializedEvent
	@Async.Execute
	private void __layoutCommands()
	{
		throw Debugging.todo();
		/*
		// Get our own policy or the one specified by the display
		Display display = this._display;
		CommandLayoutPolicy policy = this.getCommandLayoutPolicy();
		if (policy == null && display != null)
			policy = display.getCommandLayoutPolicy();
		
		// Setup new layout and set state
		try (__Layout__ layout = new __Layout__())
		{
			// Any layout calls will affect this one
			this._layout = layout;
			
			// Either use the user specified policy or a default one
			if (policy != null)
				policy.onCommandLayout(this);
			else
				this.__layoutDefault(layout);
			
			// Make whatever state was set in the layout as set
			this.__layoutExecute(layout);
		}
		
		// If this failed, print it out
		catch (RuntimeException e)
		{
			e.printStackTrace();
			
			// re-toss
			throw e;
		}
		
		// Cancel the layout state
		finally
		{
			this._layout = null;
		}
		
		 */
	}
	
	/**
	 * Lays out commands using a default means.
	 * 
	 * @param __layout The layout state.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	private void __layoutDefault(__Layout__ __layout)
		throws NullPointerException
	{
		if (__layout == null)
			throw new NullPointerException("NARG");
			
		throw Debugging.todo();
		/*
		Display display = this._display;
		
		// Go through commands and menu items, try to place them in their
		// default normal positions where possible
		for (__Action__ action : this._actions)
		{
			// Get the preferred placement for these items
			int[] prefPlace = ((action instanceof Command) ?
				display.getCommandPreferredPlacements(((Command)action)._type)
				: display.getMenuPreferredPlacements());
			
			// Use the first available place for anything that is empty
			int usePlace = -1;
			for (int place : prefPlace)
				if (__layout.get(place) == null)
				{
					usePlace = place;
					break;
				}
			
			// Otherwise, try to set the item at the lowest placement
			if (usePlace < 0)
			{
				// Determine the priority of this item
				int actPriority = __Action__.__getPriority(action);
				
				// The position and the currently lowest scoring slot
				int plopPlace = -1;
				int plopPriority = Integer.MIN_VALUE;
				
				// Find the spot with the lowest priority
				for (int place : prefPlace)
				{
					int tickPriority = __layout.getPriority(place);
					
					// Does this have a lowest priority?
					if (plopPlace < 0 || tickPriority > plopPriority)
					{
						plopPlace = place;
						plopPriority = tickPriority;
					}
				}
				
				// If our item has a higher priority than the the lowest
				// priority item, that gets replaced
				if (plopPlace >= 0 && actPriority < plopPriority)
					usePlace = plopPlace;
			}
			
			// If we could place the item here, do that placement
			if (usePlace > 0)
				__layout.set(action, usePlace);
		}*/
	}
	
	/**
	 * Returns the current displayable state holder.
	 *
	 * @return The current displayable state.
	 * @throws IllegalStateException If it has been garbage collected.
	 * @since 2024/08/13
	 */
	final DisplayableState __state()
		throws IllegalStateException
	{
		return this._state;
	}
	
	/**
	 * Returns a default title to use for the application.
	 *
	 * @return Application default title.
	 * @since 2019/05/16
	 */
	static String __defaultTitle()
	{
		// Try getting a sensible name from a system property
		MIDlet amid = ActiveMidlet.optional();
		if (amid != null)
		{
			// MIDlet Name
			String midName = amid.getAppProperty("midlet-name");
			if (midName != null)
				return midName;
			
			// Otherwise this might not be a MIDlet, so just use the main
			// class instead
			String midClass = amid.getAppProperty("main-class");
			if (midClass != null)
				return midClass;
		}
		
		// Use basic name of the application, if there is one
		String basicName = ApplicationHandler.currentName();
		if (basicName != null)
			return basicName;
		
		// Fallback to just using SquirrelJME
		return "SquirrelJME";
	}
}


