// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.midlet.MIDlet;
import org.jetbrains.annotations.Async;

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
	extends __CommonWidget__
{
	/** Commands/Menus which have been added to the displayable. */
	final __VolatileList__<__Action__> _actions =
		new __VolatileList__<>();
	
	/** The display this is attached to, if any. */
	volatile Display _display;
	
	/** The command listener to call into when commands are generated. */
	volatile CommandListener _cmdListener;
	
	/** The title of the displayable. */
	volatile String _userTitle;
	
	/** Display title to use. */
	volatile String _displayTitle;
	
	/** The ticker of the displayable. */
	volatile Ticker _ticker;
	
	/** The current layout, if valid this will be set. */
	private volatile __Layout__ _layout;
	
	/** The layout policy of this displayable. */
	private CommandLayoutPolicy _layoutPolicy;
	
	/** Was the last time the title update, were we fullscreen? */
	private boolean _titleFullScreen;
	
	/**
	 * Initializes the base displayable object.
	 *
	 * @since 2016/10/08
	 */
	Displayable()
	{
		this._displayTitle = Displayable.__defaultTitle();
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
		
		/* {@squirreljme.error EB1s The display does not support commands.} */
		Display cd = this.getCurrentDisplay();
		if (cd != null)
			if ((cd.getCapabilities() & Display.SUPPORTS_COMMANDS) == 0)
				throw new DisplayCapabilityException("EB1s");
		
		// Do nothing if the command has already been added
		__VolatileList__<__Action__> actions = this._actions;
		if (actions.containsUniqueObjRef(__c))
			return;
		
		// Otherwise make it part of the display
		actions.addUniqueObjRef(__c);
		
		// Re-calculate the commands shown on the display, if the display
		// is even visible
		if (this.__isShown())
			this.__layoutCommands();
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
		return this._cmdListener;
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
		List<Command> rv = new ArrayList<>();
		for (__Action__ a : this._actions)
			if (a instanceof Command)
				rv.add((Command)a);
		return rv.<Command>toArray(new Command[rv.size()]);
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
		return this._display;
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
		return this._userTitle;
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
		
		// Remove the command
		if (this._actions.remove(__c))
		{
			// Re-layout any removed commands so they are gone
			if (this.__isShown())
				this.__layoutCommands();
		}
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
		this.__layoutActionSet(__c, __p);
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
		this._cmdListener = __l;
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
		this.__layoutActionSet(__m, __p);
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
		// Cache it for later return
		this._userTitle = __t;
		
		// If no title is being set, fallback to a default one (derived from
		// the suite)
		if (__t == null)
			__t = Displayable.__defaultTitle();
		
		// Store this
		this._displayTitle = __t;
		
		// We can always set the title for the widget as the form should be
		// allocated
		this.__backend().widgetProperty(
			this.__state(__DisplayableState__.class)._uiTitle,
			UIWidgetProperty.STRING_LABEL, 0, __t);
		
		// Update the form title
		this.__updateFormTitle(false, false);
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
	 * Returns if this displayable is currently being shown.
	 *
	 * @return If the displayable is being shown.
	 * @since 2020/09/27
	 */
	final boolean __isShown()
	{
		// If there is no display then this cannot possibly be shown
		Display display = this._display;
		if (display == null)
			return false;
		
		// When checking if shown, actually probe the current form on the
		// display as another task may have taken the display from us
		UIBackend backend = this.__backend();
		return backend.equals(this.__state(__DisplayableState__.class)._uiForm,
			backend.displayCurrent(display._uiDisplay));
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
	private void __layoutActionSet(__Action__ __a, int __p)
		throws DisplayCapabilityException, IllegalArgumentException,
			IllegalStateException, NullPointerException 
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB3i The current display does not support
		commands.} */
		Display display = this._display;
		int caps = (display == null ? Display.__defaultCapabilities() :
			display.getCapabilities());
		if (0 == (caps & Display.SUPPORTS_COMMANDS))
			throw new IllegalArgumentException("EB3i");
		
		/* {@squirreljme.error EB3h The current displayable is not getting
		its layout calculated.} */
		__Layout__ layout = this._layout;
		if (layout == null)
			throw new IllegalStateException("EB3h");
		
		// Forward to the layout
		layout.set(__a, __p);
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
		}
	}
	
	/**
	 * Executes the given layout.
	 * 
	 * @param __layout The layout to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	@SerializedEvent
	@Async.Execute
	private void __layoutExecute(__Layout__ __layout)
		throws NullPointerException
	{
		if (__layout == null)
			throw new NullPointerException("NARG");
		
		// Left command item
		this.__layoutExecute(__layout, Display._SOFTKEY_LEFT_COMMAND, 
			Display.__layoutSoftKeyToPos(Display._SOFTKEY_LEFT_COMMAND));
		
		// Right command item
		this.__layoutExecute(__layout, Display._SOFTKEY_RIGHT_COMMAND,
			Display.__layoutSoftKeyToPos(Display._SOFTKEY_RIGHT_COMMAND));
	}
	
	/**
	 * Executes the given layout.
	 * 
	 * @param __layout The layout to execute.
	 * @param __from The from position, one of the softkey positions.
	 * @param __to The target position, one of {@link UIItemPosition}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/27
	 */
	@SerializedEvent
	@Async.Execute
	private void __layoutExecute(__Layout__ __layout, int __from, int __to)
		throws NullPointerException
	{
		if (__layout == null)
			throw new NullPointerException("NARG");
		
		UIFormBracket form = this.__state(__DisplayableState__.class)._uiForm;
		UIBackend backend = this.__backend();
		
		// If there is nothing here, clear it
		__Action__ action = __layout.get(__from);
		if (action == null)
		{
			// Remove anything that is in this position
			if (null != backend.formItemAtPosition(form, __to))
				backend.formItemRemove(form, __to);
			
			return;
		}
		
		// Create new widget that goes into this position
		if (action instanceof Command)
		{
			__CommandWidget__ cm = new __CommandWidget__(
				this, (Command)action);
			backend.formItemPosition(form, cm._uiItem, __to);
		}
		
		// Menu item
		else
		{
			throw Debugging.todo();
		}
	}
	
	/**
	 * Does internal work when a form is being shown.
	 * 
	 * @param __show The displayable being shown.
	 * @since 2020/09/27
	 */
	@SerializedEvent
	@Async.Execute
	final void __showNotify(Displayable __show)
		throws NullPointerException
	{
		if (__show == null)
			throw new NullPointerException("NARG");
		
		// Layout all the given commands, either they were changed or the
		// display was shown
		this.__layoutCommands();
		
		// Update the ticker for this so it is properly displayed
		this.__updateTicker();
		
		// Inform canvases that they are now hidden
		if (__show instanceof Canvas)
			((Canvas)__show).__showNotifyCanvas();
		
		// Form layout policies are now in effect
		else if (__show instanceof Form)
			((Form)__show).__update();
	}
	
	/**
	 * Updates the display title of the form.
	 * 
	 * @param __knownFull Is setting full-screen known?
	 * @param __isFull Is this full-screen?
	 * @since 2021/06/24
	 */
	@SquirrelJMEVendorApi
	final void __updateFormTitle(boolean __knownFull, boolean __isFull)
	{
		// If it is unknown whether we are full-screen, then restore the last
		// known full-screen state. Otherwise if we do know our full-screen
		// state set that.
		if (!__knownFull)
			__isFull = this._titleFullScreen;
		else
			this._titleFullScreen = __isFull;
			
		// Debug
		Debugging.debugNote("__updateFormTitle(%b, %b) -> %s",
			__knownFull, __isFull, this._displayTitle);
		
		// If we are not full-screen then the title bar is at the top, so we
		// can just say our SquirrelJME. Otherwise, that will be hidden, so
		// we can set the main window title.
		String useTitle;
		if (!__isFull)
			useTitle = "SquirrelJME";
		else
			useTitle = this._displayTitle;
		
		// Set the form title
		this.__backend().widgetProperty(
			this.__state(__DisplayableState__.class)._uiForm,
			UIWidgetProperty.STRING_FORM_TITLE, 0, useTitle);
		
		// If this is a form, since we updated the title we should update
		// all the layout since the title placement could cause the
		// locations of items to change.
		if (this instanceof Form)
			((Form)this).__update();
	}
	
	/**
	 * Updates the ticker that is displayed on this displayable.
	 * 
	 * @since 2021/11/27
	 */
	final void __updateTicker()
	{
		UIBackend backend = this.__backend();
		UIFormBracket uiForm = this.__state(
			__DisplayableState__.class)._uiForm;
		
		// Has this changed?
		boolean hasChanged;
		
		// Is the ticker being removed?
		Ticker ticker = this._ticker;
		UIItemBracket uiTicker = this.__state(
			__DisplayableState__.class)._uiTicker;
		if (ticker == null)
		{
			// Remove the ticker if it is currently being displayed
			hasChanged = backend.formItemAtPosition(uiForm,
				UIItemPosition.TICKER) == uiTicker;
			if (hasChanged)
				backend.formItemRemove(uiForm, UIItemPosition.TICKER);
		}
		
		// Setting or changing the string?
		else
		{
			// If not already set, place it here
			hasChanged = backend.formItemAtPosition(uiForm,
				UIItemPosition.TICKER) != uiTicker;
			if (hasChanged)
				backend.formItemPosition(uiForm, uiTicker,
					UIItemPosition.TICKER);
			
			// Is the string on the ticker changing at all?
			String oldString = backend.widgetPropertyStr(uiTicker,
				UIWidgetProperty.STRING_LABEL, 0);
			String newString = ticker._text;
			if (((oldString == null) != (newString == null)) ||
				(oldString != null && !oldString.equals(newString)))
			{
				backend.widgetProperty(uiTicker, UIWidgetProperty.STRING_LABEL,
					0, newString);
			}
		}
		
		// Did the state of this ticker change?
		if (hasChanged)
		{
			// Request form update as sizes and such could have changed
			if (this instanceof Form)
				((Form)this).__update();
		}
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
	
	/**
	 * Returns the displayable height.
	 *
	 * @param __d The displayable.
	 * @param __alt
	 * @return The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	static int __getHeight(Displayable __d, UIWidgetBracket __alt)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
			
		// The default maximum display height?
		Display display = __d._display;
		if (display == null)
			return Display.getDisplays(0)[0].getHeight();
		
		// Get current form size
		return __d.__backend().widgetPropertyInt(
			(__alt != null ? __alt :
				__d.__state(__DisplayableState__.class)._uiForm),
			UIWidgetProperty.INT_HEIGHT, 0);
	}
	
	/**
	 * Returns the displayable width.
	 *
	 * @param __d The displayable.
	 * @param __alt Alternative widget to check.
	 * @return The width.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	static int __getWidth(Displayable __d, UIWidgetBracket __alt)
	{
		if (__d == null)
			throw new NullPointerException("NARG");
			
		// The default maximum display height?
		Display display = __d._display;
		if (display == null)
			return Display.getDisplays(0)[0].getWidth();
		
		// Get current form size
		return __d.__backend().widgetPropertyInt(
			(__alt != null ? __alt :
				__d.__state(__DisplayableState__.class)._uiForm),
			UIWidgetProperty.INT_WIDTH, 0);
	}
	
	/**
	 * State for {@link Displayable}
	 * 
	 * @since 2023/01/14
	 */
	abstract static class __DisplayableState__
		extends __CommonWidget__.__CommonState__
	{
		/** The native form instance. */
		final UIFormBracket _uiForm;
		
		/** The title of the form. */
		@Deprecated
		final UIItemBracket _uiTitle;
		
		/** The item used for the ticker on this displayable. */
		@Deprecated
		final UIItemBracket _uiTicker;
		
		/**
		 * Initializes the backend state.
		 * 
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__DisplayableState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
			
			// Create a new form for this displayable
			UIFormBracket uiForm = __backend.formNew();
			this._uiForm = uiForm;
			
			// Register it with the global state
			StaticDisplayState.register(__self, uiForm);
			
			// Build the title item
			UIItemBracket uiTitle = __backend.itemNew(UIItemType.LABEL);
			this._uiTitle = uiTitle;
			
			// Set up the title item
			__backend.formItemPosition(uiForm, uiTitle, UIItemPosition.TITLE);
			__backend.widgetProperty(uiTitle, UIWidgetProperty.STRING_LABEL,
				0, Displayable.__defaultTitle());
			
			// Each displayable has its own ticker
			this._uiTicker = __backend.itemNew(UIItemType.LABEL);
		}
	}
}


