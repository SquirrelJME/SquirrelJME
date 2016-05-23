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
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This is the base class for all displayable elements.
 *
 * @since 2016/05/21
 */
public abstract class UIElement
{
	/** Element lock. */
	protected final Object lock;
	
	/** The external display manager. */
	protected final UIDisplayManager displaymanager;
	
	/** Link back to the internal element. */
	private volatile InternalElement _internal;
	
	/**
	 * Initializes the base element.
	 *
	 * @param __dm The external display manager.
	 * @throws NullPointerException On null arguments if this is not a display
	 * manager.
	 * @since 2016/05/21
	 */
	UIElement(UIDisplayManager __dm)
		throws NullPointerException
	{
		// If this is a display manager then null is acceptable because the
		// value to set is just this.
		if (this instanceof UIDisplayManager)
		{
			this.displaymanager = (UIDisplayManager)this;
			this.lock = ((UIDisplayManager)this).__lock();
		}
		
		// Otherwise this is some other element type.
		else
		{
			// Check
			if (__dm == null)
				throw new NullPointerException("NARG");
		
			// Set
			this.displaymanager = __dm;
			this.lock = __dm.__lock();
		}
	}
	
	/**
	 * Returns the external display manager owning this element.
	 *
	 * @return The owning display manager.
	 * @since 2016/05/22
	 */
	public final UIDisplayManager displayManager()
	{
		return this.displaymanager;
	}
	
	/**
	 * Returns the internal element which was linked to this external one.
	 *
	 * @param <E> The internal element type.
	 * @param __cl The class for that element.
	 * @return The internal element.
	 * @since 2016/05/22
	 */
	final <E extends InternalElement<?>> E __internal(Class<E> __cl)
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD04 No internal element was linked to
			// this external one.}
			return __cl.cast(Objects.requireNonNull(this._internal, "BD04"));
		}
	}
	
	/**
	 * Links back an internal element so that the external element may
	 * reference it and perform other actions with it.
	 *
	 * @param __i The internal element to link back.
	 * @throws IllegalStateException If there already is a link.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	final void __linkBack(InternalElement __i)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD03 Element has already been linked to an
			// internal one.}
			if (this._internal != null)
				throw new IllegalStateException("BD03");
			
			// Set
			this._internal = __i;
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
}

