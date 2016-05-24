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
	final PIManager _pimanager;
	
	/** The UI element cleanup thread. */
	private final Thread _cleanupthread;
	
	/** The reference queue for element cleanup. */
	private final ReferenceQueue<UIBase> _queue =
		new ReferenceQueue<>();
	
	/** The mapping between references and internal elements. */
	private final Map<Reference<? extends UIBase>, PIBase> _xtoi =
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
		this._pimanager = __pi;
		__pi.__chain(this);
		
		// {@squirreljme.error BD0b The platform interface does not define a
		// lock.}
		this.lock = Objects.requireNonNull(__pi.lock(), "BD0b");
		
		// Setup cleanup thread
		Thread _cleanupthread = new Thread(new __Cleanup__());
		this._cleanupthread = _cleanupthread;
		_cleanupthread.start();
	}
	
	/**
	 * Creates a new display.
	 *
	 * @return The newly created display.
	 * @throws UIException If the display could not be created.
	 * @since 2016/05/21
	 */
	public final UIDisplay createDisplay()
		throws UIException
	{
		try
		{
			UIDisplay rv = new UIDisplay(this);
			Reference<UIDisplay> ref = new WeakReference<UIDisplay>(rv,
				this._queue);
			PIBase in = this._pimanager.createDisplay(ref);
			__register(ref, in);
			return rv;
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
	public final UIImage createImage()
		throws UIException
	{
		try
		{
			return new UIImage();
		}
		
		// {@squirreljme.error BD06 Ran out of memory creating image.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD06");
		}
	}
	
	/**
	 * Creates a new label.
	 *
	 * @return The newly created label.
	 * @throws UIException If the label could not be created.
	 * @since 2016/05/24
	 */
	public final UILabel createLabel()
		throws UIException
	{
		try
		{
			UILabel rv = new UILabel(this);
			Reference<UILabel> ref = new WeakReference<UILabel>(rv,
				this._queue);
			PIBase in = this._pimanager.createLabel(ref);
			__register(ref, in);
			return rv;
		}
		
		// {@squirreljme.error BD0i Ran out of memory creating label.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD0i");
		}
	}
	
	/**
	 * Creates a new list which contains elements to be displayed.
	 *
	 * @return The newly created list.
	 * @throws UIException If the list could not created.
	 * @since 2016/05/24
	 */
	public final UIList createList()
		throws UIException
	{
		try
		{
			UIList rv = new UIList(this);
			Reference<UIList> ref = new WeakReference<UIList>(rv,
				this._queue);
			PIBase in = this._pimanager.createList(ref);
			__register(ref, in);
			return rv;
		}
		
		// {@squirreljme.error BD0c Ran out of memory creating list.}
		catch (OutOfMemoryError e)
		{
			throw new UIException("BD0c");
		}
	}
	
	/**
	 * Creates a new menu which may be associated with a given display.
	 *
	 * @return The newly created menu.
	 * @throws UIException If the menu could not be created.
	 * @since 2016/05/23
	 */
	public final UIMenu createMenu()
		throws UIException
	{
		try
		{
			UIMenu rv = new UIMenu(this);
			Reference<UIMenu> ref = new WeakReference<UIMenu>(rv,
				this._queue);
			PIBase in = this._pimanager.createMenu(ref);
			__register(ref, in);
			return rv;
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
	public final UIMenuItem createMenuItem()
		throws UIException
	{
		try
		{
			UIMenuItem rv = new UIMenuItem(this);
			Reference<UIMenuItem> ref = new WeakReference<UIMenuItem>(rv,
				this._queue);
			PIBase in = this._pimanager.createMenuItem(ref);
			__register(ref, in);
			return rv;
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
	public final int[] preferredIconSizes()
		throws UIException
	{
		try
		{
			// Get icons
			int[] rv = this._pimanager.preferredIconSizes();
		
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
	 * Returns an internal platform interface object from an external one.
	 *
	 * @param <P> The type of internal to fine.
	 * @param __cl The class type of that internal.
	 * @return The internal which belongs to the specified external, or
	 * {@code null} if it was garbage collected or does not exist.
	 * @throws ClassCastException If the internal is not of the given type.
	 * @throws UIException On other errors.
	 * @since 2016/05/23
	 */
	final <P extends PIBase> P __internal(Class<P> __cl, UIBase __x)
		throws ClassCastException, NullPointerException, UIException
	{
		// Check
		if (__cl == null || __x == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Look in the element mapping
			Map<Reference<? extends UIBase>, PIBase> xtoi =
				this._xtoi;
			for (Map.Entry<Reference<? extends UIBase>, PIBase> e :
				xtoi.entrySet())
			{
				if (e.getKey().get() == __x)
					return __cl.cast(e.getValue());
			}
			
			// Not found or GCed
			return null;
		}
	}
	
	/**
	 * Registers the external element to an internal one.
	 *
	 * @param __x The reference to the external element.
	 * @param __i The internal element.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/23
	 */
	final void __register(Reference<? extends UIBase> __x, PIBase __i)
		throws NullPointerException
	{
		// Check
		if (__x == null || __i == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Register the base
			__x.get().__registerPlatform(__i);
			
			// Add to the mapping
			this._xtoi.put(__x, __i);
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

