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

/**
 * This is the internal implementation of the display manager.
 *
 * Internal classes are not meant to be used by the end user, but only by
 * the implementation of a display manager.
 *
 * @see UIDisplayManager
 * @since 2016/05/21
 */
public abstract class PIManager
{
	/** The global state lock. */
	protected final Object lock =
		new Object();
	
	/** The externally linked UI manager. */
	private volatile UIManager _uimanager;
	
	/**
	 * Initializes the internal display manager.
	 *
	 * @since 2016/05/21
	 */
	public PIManager()
	{
	}
	
	/**
	 * Creates a new internal display element.
	 *
	 * @param __ref The reference to the external display.
	 * @return The internal display element.
	 * @throws UIException If it could not be created.
	 * @since 2016/05/22
	 */
	public abstract PIDisplay createDisplay(Reference<UIDisplay> __ref)
		throws UIException;
	
	/**
	 * Creates a new label which displays an icon and text.
	 *
	 * @param __ref The reference to the external label.
	 * @retrun The internal label.
	 * @throws UIException If it could not be created.
	 * @since 2016/05/24
	 */
	public abstract PILabel createLabel(Reference<UILabel> __ref)
		throws UIException;
	
	/**
	 * Creates a new platform dependent list.
	 *
	 * @param __ref The reference to the external list.
	 * @param __ld The used list data.
	 * @return The platform list.
	 * @throws UIException If it could not be created.
	 * @since 2016/05/24
	 */
	public abstract PIList createList(Reference<? extends UIList<?>> __ref,
		UIListData<?> __ld)
		throws UIException;
	
	/**
	 * Creates a new internal menu.
	 *
	 * @param __ref The reference to the external menu.
	 * @return The internal menu.
	 * @throws UIException If the menu could not be created.
	 * @since 2016/05/23
	 */
	public abstract PIMenu createMenu(Reference<UIMenu> __ref)
		throws UIException;
	
	/**
	 * Creates a new internal menu item.
	 *
	 * @param __ref The reference to the external menu item.
	 * @return The internal menu item.
	 * @throws UIException If the menu item could not be created.
	 * @since 2016/05/23
	 */
	public abstract PIMenuItem createMenuItem(Reference<UIMenuItem> __ref)
		throws UIException;
	
	/**
	 * Returns an array with width/height pairs which indicates the preferred
	 * sizes of the icons to use.
	 *
	 * Dimensions returned by the array will be corrected to a minimal bound
	 * of a single pixel.
	 *
	 * @return The preferred sizes which icons should be in width/height pairs,
	 * may return {@code null} to indicate that no icons should be displayed.
	 * @throws UIException If the preferred sizes could not be determined.
	 * @since 2016/05/22
	 */
	public abstract int[] preferredIconSizes()
		throws UIException;
	
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
	public final <P extends PIBase> P internal(Class<P> __cl, UIBase __x)
		throws ClassCastException, NullPointerException, UIException
	{
		// Check
		if (__cl == null || __x == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			return this._uimanager.<P>__internal(__cl, __x);
		}
	}
	
	/**
	 * This returns the global locking object which is used for the entire
	 * display state.
	 *
	 * @return The used locking object.
	 * @since 2016/05/23
	 */
	public final Object lock()
	{
		return this.lock;
	}
	
	/**
	 * Chains an external manager to this internal one.
	 *
	 * @param __uim The external manager to chain to.
	 * @throws IllegalStateException If an external manager was already
	 * chained.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/23
	 */
	final void __chain(UIManager __uim)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__uim == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD03 }
			if (null != this._uimanager)
				throw new IllegalStateException("BD03");
			
			// Set
			this._uimanager = __uim;
		}
	}
}

