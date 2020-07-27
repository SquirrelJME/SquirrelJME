// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.DisplayListener;
import javax.microedition.lcdui.Displayable;

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
	
	/** The cached forms for {@link Displayable}. */
	private static final Map<Reference<Displayable>, UIFormBracket> _FORMS =
		new LinkedHashMap<>();
	
	/** Queue which is used for garbage collection of forms. */
	private static final ReferenceQueue<Displayable> _FORM_QUEUE =
		new ReferenceQueue<>();
	
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
	 * Locates the form for the given displayable.
	 * 
	 * @param __displayable The displayable to locate.
	 * @return The form for the given displayable.
	 * @throws NoSuchElementException If none were found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/26
	 */
	public static UIFormBracket locate(Displayable __displayable)
		throws NoSuchElementException, NullPointerException
	{
		if (__displayable == null)
			throw new NullPointerException("NARG");
		
		// Would be previously cached
		synchronized (StaticDisplayState.class)
		{
			for (Map.Entry<Reference<Displayable>, UIFormBracket> e :
				StaticDisplayState._FORMS.entrySet())
			{
				if (__displayable == e.getKey().get())
					return e.getValue();
			}
		}
		
		// {@squirreljme.error EB3c No form exists for the given
		// displayable.}
		throw new NoSuchElementException("EB3c");
	}
	
	/**
	 * Garbage collects the displays and forms.
	 * 
	 * @since 2020/07/01
	 */
	public static void gc()
	{
		// Prevent thread mishaps between threads doing this
		synchronized (StaticDisplayState.class)
		{
			ReferenceQueue<Displayable> queue = StaticDisplayState._FORM_QUEUE;
			Map<Reference<Displayable>, UIFormBracket> forms =
				StaticDisplayState._FORMS;
			
			// If there is anything in the queue, clear it out
			for (Reference<? extends Displayable> ref = queue.poll();
				ref != null; ref = queue.poll())
			{
				UIFormBracket form = forms.get(ref);
				
				// Remove from the mapping since it is gone now
				forms.remove(ref);
				
				// Perform collection on it
				UIBackendFactory.getInstance().formDelete(form);
			}
		}
	}
	
	/**
	 * Registers the displayable with the given form.
	 * 
	 * @param __displayable The displayable.
	 * @param __form The form to link.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public static void register(Displayable __displayable,
		UIFormBracket __form)
		throws NullPointerException
	{
		if (__displayable == null || __form == null)
			throw new NullPointerException("NARG");
		
		// Prevent thread mishaps between threads doing this
		synchronized (StaticDisplayState.class)
		{
			// Perform quick garbage collection on forms in the event any
			// have gone away
			StaticDisplayState.gc();
			
			// Queue the form for future cleanup
			Reference<Displayable> ref = new WeakReference<>(__displayable,
				StaticDisplayState._FORM_QUEUE);
			
			// Bind this displayable to the form
			StaticDisplayState._FORMS.put(ref, __form);
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
}
