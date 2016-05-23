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
	public abstract PIDisplay createDisplay(
		Reference<UIDisplay> __ref)
		throws UIException;
	
	/**
	 * Creates a new internal image.
	 *
	 * @param __ref The reference to the external image.
	 * @return The internal image.
	 * @throws UIException If the image could not be created.
	 * @since 2016/05/22
	 */
	public abstract PIImage createImage(Reference<UIImage> __ref)
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
	public abstract PIMenuItem createMenuItem(
		Reference<UIMenuItem> __ref)
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
	 * Obtains an internal element from an external one.
	 *
	 * @param <E> The type of element to obtain.
	 * @param __cl The type of element that is expected.
	 * @param __e The internal element to get the external element for.
	 * @return The internal element or {@code null} if the external element
	 * does not exist.
	 * @since 2016/05/22
	 */
	protected final <E extends InternalElement> E getInternal(Class<E> __cl,
		UIElement __e)
	{
		return this.displaymanager.<E>__getInternal(__cl, __e);
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
}

