// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This is a class which provides the ability to create {@link UIDisplay}s as
 * needed to interact and show information to the user. A display manager can
 * create multiple displays at the same time, however there may be limitations
 * as to the number of displays which are active at any one time. For example,
 * on modern desktop systems there may be a single window for each unique
 * display, while on a mobile device there might only be just a single display
 * at a time.
 *
 * This wraps a {@link PIManager} so that if it lacks
 * specific functionality it can be emulated by the front layer set of classes.
 *
 * @since 2016/05/20
 */
public class UIManager
{
	/** Zero sized integer array. */
	private static final int[] _ZERO_SIZE_INT_ARRAY =
		new int[0];
	
	/** The global lock. */
	protected final Object lock;
	
	/** The platform interface manager. */
	protected final PIManager pimanager;
	
	/** The UI element cleanup thread. */
	protected final Thread cleanupthread;
	
	/** The reference queue for element cleanup. */
	protected final ReferenceQueue<UIBase> rqueue =
		new ReferenceQueue<>();
	
	/** The mapping between references and internal elements. */
	protected final Map<Reference<? extends UIBase>, PIBase> elements =
		new HashMap<>();
	
	/**
	 * Initializes the display manager which wraps the internal representation
	 * of the user interface.
	 *
	 * @param __pi The platform interface which provides direct access to
	 * the native widget system of the host.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the display manager could not be created.
	 * @since 2016/05/21
	 */
	public UIManager(PIManager __pi)
		throws NullPointerException, UIException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pimanager = __pi;
		
		// {@squirreljme.error BD0b The platform interface does not define a
		// lock.}
		this.lock = Objects.requireNonNull(__pi.lock(), "BD0b");
		
		// Setup cleanup thread
		Thread cleanupthread = new Thread(new __Cleanup__());
		this.cleanupthread = cleanupthread;
		cleanupthread.start();
	}
	
	/**
	 * Creates a new display.
	 *
	 * @return The newly created display.
	 * @throws UIException If the display could not be created.
	 * @since 2016/05/21
	 */
	public UIDisplay createDisplay()
		throws UIException
	{
		try
		{
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BD07 Ran out of memory creating display.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD07");
		}
	}
	
	/**
	 * Creates a new image which may be displayed.
	 *
	 * @return The newly created image.
	 * @throws UIException If the image could not be created.
	 * @since 2016/05/22
	 */
	public UIImage createImage()
		throws UIException
	{
		try
		{
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BD06 Ran out of memory creating image.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD06");
		}
	}
	
	/**
	 * Creates a new menu which may be associated with a given display.
	 *
	 * @return The newly created menu.
	 * @throws UIException If the menu could not be created.
	 * @since 2016/05/23
	 */
	public UIMenu createMenu()
		throws UIException
	{
		try
		{
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BD09 Ran out of memory creating menu.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD09");
		}
	}
	
	/**
	 * Creates a new menu item which may be assigned to a menu.
	 *
	 * @return The newly created menu item.
	 * @throws UIException If the menu item could not be created.
	 * @since 2016/05/23
	 */
	public UIMenuItem createMenuItem()
		throws UIException
	{
		try
		{
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BD09 Ran out of memory creating menu.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD09");
		}
	}
	
	/**
	 * Returns an array with width/height pairs which indicates the preferred
	 * sizes of the icons to use.
	 *
	 * @return The preferred sizes which icons should be in width/height pairs.
	 * @throws UIException If the preferred sizes could not be determined.
	 * @since 2016/05/22
	 */
	public int[] preferredIconSizes()
		throws UIException
	{
		try
		{
			// Get icons
			int[] rv = this.pimanager.preferredIconSizes();
		
			// No preferred size, no icons
			if (rv == null || rv.length <= 1)
				return _ZERO_SIZE_INT_ARRAY;
			
			// If not a multiple of two, clip
			int l = rv.length;
			if ((l & 1) != 0)
			{
				// Setup
				int nl = l - 1;
				int[] cv = new int[nl];
				
				// Fill
				for (int i = 0; i < nl; i++)
					cv[i] = rv[i];
				
				// Set
				rv = cv;
			}
			
			// If a dimension is zero or negative, make it at least a single
			// pixel
			l = rv.length;
			for (int i = 0; i < l; i++)
				if (rv[i] <= 0)
					rv[i] = 1;
			
			// Return it
			return rv;
		}
		
		// Failed to determine or out of memory, use no icons
		catch (OutOfMemoryError|UIException e)
		{
			return _ZERO_SIZE_INT_ARRAY;
		}
	}
	
	/**
	 * Obtains an external element from an internal one.
	 *
	 * @param <E> The type of element to obtain.
	 * @param __cl The type of element that is expected.
	 * @param __e The internal element to get the external element for.
	 * @return The external element or {@code null} if it was cleaned up or
	 * was not found.
	 * @throws ClassCastException If the external element is not of the
	 * expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	final <E extends UIBase> E __getExternal(Class<E> __cl,
		PIBase __e)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Reference<? extends UIBase>, PIBase> e =
			this.elements;
		synchronized (e)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains an internal element from an external one.
	 *
	 * @param <E> The type of element to obtain.
	 * @param __cl The type of element that is expected.
	 * @param __e The internal element to get the external element for.
	 * @return The internal element or {@code null} if the external element
	 * does not exist in this manager or was garbage collected.
	 * @throws ClassCastException If the internal element is not of the
	 * expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	final <E extends PIBase> E __getInternal(Class<E> __cl,
		UIBase __e)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Reference<? extends UIBase>, PIBase> m =
			this.elements;
		synchronized (m)
		{
			// Go through all entries
			for (Map.Entry<Reference<? extends UIBase>, PIBase> e :
				m.entrySet())
			{
				// Compare key
				if (e.getKey().get() == __e)
					return __cl.cast(e.getValue());
			}
			
			// Not found
			return null;
		}
	}
	
	/**
	 * Returns the lock of the external element, this is used so that internal
	 * elements and internal elements share the same lock.
	 *
	 * @return The locking object that the external element uses.
	 * @since 2016/05/22
	 */
	final Object __lock()
	{
		return this.lock;
	}
	
	/**
	 * Registers that a new element was created under the UI and adds it
	 * and its reference to the internal mapping.
	 *
	 * @param __ref The reference to the external element.
	 * @param __ie The internal element representation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	final void __newElement(Reference<? extends UIBase> __ref,
		PIBase __ie)
		throws NullPointerException
	{
		// Check
		if (__ref == null || __ie == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Reference<? extends UIBase>, PIBase> e =
			this.elements;
		synchronized (e)
		{
			// Add it
			e.put(__ref, __ie);
		}
	}
	
	/**
	 * This is the cleanup thread which removes elements and garbage collects
	 * them so that native resources (if any) are freed automatically.
	 *
	 * @since 2016/05/22
	 */
	private	final class __Cleanup__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/05/22
		 */
		@Override
		public void run()
		{
			// Infinite loop
			for (;;)
			{
				// Sleep for a bit
				try
				{
					Thread.sleep(3_000L);
				}
				
				// Yield instead
				catch (InterruptedException e)
				{
					Thread.yield();
				}
			}
		}
	}
}

