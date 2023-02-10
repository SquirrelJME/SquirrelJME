// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.midlet.CleanupHandler;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.DisplayListener;

/**
 * Static state of the LCDUI sub-system.
 *
 * @since 2020/07/01
 */
public final class StaticDisplayState
{	
	/** The displays that are initialized currently. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	public static Display[] DISPLAYS;
	
	/** Listeners for the display. */
	private static final List<DisplayListener> _LISTENERS =
		new LinkedList<>();
	
	/** The cached forms for {@link DisplayWidget}. */
	private static final Map<DisplayWidget, List<UIDrawableBracket>> _WIDGETS =
		new WeakHashMap<>();
	
	/** Graphics handling thread. */
	private static Thread _BACKGROUND_THREAD;
	
	/** The callback used for form events. */
	private static UIFormCallback _CALLBACK;
	
	/** Did we add the cleanup handler yet? */
	private static volatile boolean _addedCleanup;
	
	/** Is this terminating? */
	private static volatile boolean _IS_TERMINATING;
	
	/**
	 * Adds the specified listener for changes to displays.
	 *
	 * The order in which listeners are executed in is
	 * implementation specified.
	 *
	 * @param __dl The listener to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public static void addListener(DisplayListener __dl)
		throws NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");
		
		List<DisplayListener> listeners = StaticDisplayState._LISTENERS;
		synchronized (StaticDisplayState.class)
		{
			// Do nothing if it is already in there
			for (DisplayListener __listener : listeners)
				if (__listener == __dl)
					return;
			
			// Add it, if it is not there
			listeners.add(__dl);
		}
	}
	
	/**
	 * Returns the background thread used for LCDUI callbacks.
	 * 
	 * @return The background thread, or {@code null} if not set.
	 * @since 2020/09/12
	 */
	public static Thread backgroundThread()
	{
		synchronized (StaticDisplayState.class)
		{
			return StaticDisplayState._BACKGROUND_THREAD;
		}
	}
	
	/**
	 * Returns the callback event handler.
	 * 
	 * @return The callback for events.
	 * @since 2020/09/12
	 */
	public static UIFormCallback callback()
	{
		synchronized (StaticDisplayState.class)
		{
			return StaticDisplayState._CALLBACK;
		}
	}
	
	/**
	 * Attempts to destroy the user interface.
	 * 
	 * @since 2020/09/12
	 */
	public static void destroy()
	{
		synchronized (StaticDisplayState.class)
		{
			// Remove all listeners
			for (DisplayListener listener : StaticDisplayState.listeners())
				StaticDisplayState.removeListener(listener);
			
			// Delete everything
			StaticDisplayState._WIDGETS.clear();
		}
	}
	
	/**
	 * Returns an array of all the attached listeners.
	 *
	 * @return An array of listeners.
	 * @since 2018/03/24
	 */
	public static DisplayListener[] listeners()
	{
		List<DisplayListener> listeners = StaticDisplayState._LISTENERS;
		synchronized (StaticDisplayState.class)
		{
			return listeners.<DisplayListener>toArray(new DisplayListener[
				listeners.size()]);
		}
	}
	
	/**
	 * Locates the widget for the given native.
	 * 
	 * @param __native The native to locate.
	 * @return The widget for the given native or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/20
	 */
	public static DisplayWidget locate(UIDrawableBracket __native)
		throws NoSuchElementException, NullPointerException
	{
		if (__native == null)
			throw new NullPointerException("NARG");
		
		// Would be previously cached
		UIBackend instance = UIBackendFactory.getInstance(true);
		synchronized (StaticDisplayState.class)
		{
			for (Map.Entry<DisplayWidget, List<UIDrawableBracket>> e :
				StaticDisplayState._WIDGETS.entrySet())
			{
				List<UIDrawableBracket> items = e.getValue();
				if (items != null)
					for (UIDrawableBracket bracket : items)
						if (instance.equals(__native, bracket))
							return e.getKey();
			}
		}
		
		// {@squirreljme.error EB3e No widget exists for the given
		// native.}
		return null;
	}
	
	/**
	 * Locates the widget for the given display widget.
	 * 
	 * @param __widget The displayable to locate.
	 * @return The widget for the given displayable.
	 * @throws NoSuchElementException If none were found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/14
	 */
	public static UIDrawableBracket locate(DisplayWidget __widget)
		throws NoSuchElementException, NullPointerException
	{
		return StaticDisplayState.locate(__widget, Integer.MIN_VALUE);
	}
	
	/**
	 * Locates the widget for the given display widget.
	 * 
	 * @param __widget The displayable to locate.
	 * @param __type The {@link UIItemType} to look for, may be
	 * {@link Integer#MIN_VALUE} if not considered.
	 * @return The widget for the given displayable.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NoSuchElementException If none were found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/14
	 */
	public static UIDrawableBracket locate(DisplayWidget __widget, int __type)
		throws IllegalArgumentException, NoSuchElementException,
			NullPointerException
	{
		return StaticDisplayState.locate(__widget, __type,
			UIBackendFactory.getInstance(true));
	}
	
	/**
	 * Locates the widget for the given display widget.
	 * 
	 * @param __widget The displayable to locate.
	 * @param __type The {@link UIItemType} to look for, may be
	 * {@link Integer#MIN_VALUE} if not considered.
	 * @param __backend The backend to reference for properties, this is
	 * likely to only be used for testing purposes.
	 * @return The widget for the given displayable.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NoSuchElementException If none were found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/26
	 */
	public static UIDrawableBracket locate(DisplayWidget __widget, int __type,
		UIBackend __backend)
		throws IllegalArgumentException, NoSuchElementException,
			NullPointerException
	{
		if (__widget == null || __backend == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB39 Invalid item type. (The type)}
		if ((__type < UIItemType.DISPLAY && __type != Integer.MIN_VALUE) ||
			__type > UIItemType.NUM_TYPES)
			throw new IllegalArgumentException("EB39 " + __type);
		
		// Would be previously cached
		synchronized (StaticDisplayState.class)
		{
			// Go through drawables that are available
			List<UIDrawableBracket> drawables =
				StaticDisplayState._WIDGETS.get(__widget);
			if (drawables != null)
				for (UIDrawableBracket drawable : drawables)
				{
					// Are we looking for a specific type of item?
					if (__type != Integer.MIN_VALUE)
						if (__type == UIItemType.DISPLAY &&
							!(drawable instanceof UIDisplayBracket))
							continue;
						else if (__type != __backend.widgetPropertyInt(
							(UIWidgetBracket)drawable,
							UIWidgetProperty.INT_UIITEM_TYPE, 0))
							continue;
					
					return drawable;
				}
		}
		
		// {@squirreljme.error EB3c No form exists for the given
		// displayable.}
		throw new NoSuchElementException("EB3c");
	}
	
	/**
	 * Checks if the UI is in the terminating state.
	 * 
	 * @return If this is terminating.
	 * @since 2020/09/12
	 */
	public static boolean isTerminating()
	{
		synchronized (StaticDisplayState.class)
		{
			return StaticDisplayState._IS_TERMINATING;
		}
	}
	
	/**
	 * Registers the displayable with the given form.
	 * 
	 * @param __widget The displayable.
	 * @param __native The native to link.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public static void register(DisplayWidget __widget,
		UIDrawableBracket __native)
		throws NullPointerException
	{
		if (__widget == null || __native == null)
			throw new NullPointerException("NARG");
		
		// Prevent thread mishaps between threads doing this
		Map<DisplayWidget, List<UIDrawableBracket>> widgets =
			StaticDisplayState._WIDGETS;
		synchronized (StaticDisplayState.class)
		{
			// When terminating, destroy and cleanup all the display state
			if (!StaticDisplayState._addedCleanup)
			{
				CleanupHandler.add(new __TerminateDisplay__());
				
				// This should only be done once!
				StaticDisplayState._addedCleanup = true;
			}
			
			// Make sure the list exists
			List<UIDrawableBracket> drawables =
				widgets.get(__widget);
			if (drawables == null)
				widgets.put(__widget, (drawables = new ArrayList<>()));
			
			// Add to the bracket set
			drawables.add(__native);
		}
	}
	
	/**
	 * Removes the specified display listener so that it is no longer called
	 * when events occur.
	 *
	 * @param __dl The listener to remove.
	 * @throws IllegalStateException If the listener is not in the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public static void removeListener(DisplayListener __dl)
		throws IllegalStateException, NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");	
		
		List<DisplayListener> listeners = StaticDisplayState._LISTENERS;
		synchronized (StaticDisplayState.class)
		{
			boolean didRemove = false;
			for (int i = 0, n = listeners.size(); i < n; i++)
				if (listeners.get(i) == __dl)
				{
					listeners.remove(i);
					didRemove = true;
				}
			
			// {@squirreljme.error EB1r The listener was never added to the
			// listener set.}
			if (!didRemove)
				throw new IllegalStateException("EB1r");
		}
	}
	
	/**
	 * Sets the background thread.
	 * 
	 * @param __thread The thread to set.
	 * @param __callback The callback for forms.
	 * @throws IllegalStateException If a thread was already set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/12
	 */
	public static void setBackgroundThread(Thread __thread,
		UIFormCallback __callback)
		throws IllegalStateException, NullPointerException
	{
		if (__thread == null || __callback == null)
			throw new NullPointerException("NARG");
		
		synchronized (StaticDisplayState.class)
		{
			// {@squirreljme.error EB3d There is already a background thread
			// present.}
			if (StaticDisplayState._BACKGROUND_THREAD != null)
				throw new IllegalStateException("EB3d");
			
			StaticDisplayState._BACKGROUND_THREAD = __thread;
			StaticDisplayState._CALLBACK = __callback;
		}
	}
	
	/**
	 * Terminates the user interface.
	 * 
	 * @since 2020/09/12
	 */
	public static void terminate()
	{
		synchronized (StaticDisplayState.class)
		{
			// Mark as terminating
			StaticDisplayState._IS_TERMINATING = true;
			
			// Interrupt the LCDUI thread so it knows to exit
			Thread bgThread = StaticDisplayState._BACKGROUND_THREAD;
			if (bgThread != null)
				bgThread.interrupt();
			
			// Notify everyone of the state change
			StaticDisplayState.class.notifyAll();
		}
	}
}
