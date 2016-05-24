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
public abstract class UIBase
{
	/** Element lock. */
	protected final Object lock;
	
	/** The external display manager. */
	protected final UIManager manager;
	
	/** The linked platform interface. */
	private volatile PIBase _platform;
	
	/**
	 * Initializes the base element.
	 *
	 * @param __dm The external display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public UIBase(UIManager __dm)
		throws NullPointerException
	{
		// Check
		if (__dm == null)
			throw new NullPointerException("NARG");
	
		// Set
		this.manager = __dm;
		this.lock = __dm.__lock();
	}
	
	/**
	 * Returns the external display manager owning this element.
	 *
	 * @return The owning display manager.
	 * @since 2016/05/22
	 */
	public final UIManager displayManager()
	{
		return this.manager;
	}
	
	/**
	 * Returns the platform interface element which was linked to this external
	 * one.
	 *
	 * @param <E> The internal element type.
	 * @param __cl The class for that element.
	 * @return The internal element.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If no platform interface was registered.
	 * @since 2016/05/22
	 */
	final <P extends PIBase> P __platform(Class<P> __cl)
		throws NullPointerException, UIException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD01 The platform interface was never
			// bound to the user interface base.}
			PIBase rv = this._platform;
			if (rv == null)
				throw new UIException("BD01");
			
			// Cast
			return __cl.cast(rv);
		}
	}
	
	/**
	 * Registers the platform interface with this user interface.
	 *
	 * @param __pi The native platform interface type.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If one was already registered.
	 * @since 2016/05/23
	 */
	final void __registerPlatform(PIBase __pi)
		throws NullPointerException, UIException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD02 A native platform interface was
			// already registered with this user interface base.}
			if (this._platform != null)
				throw new UIException("BD02");
			
			// Set
			this._platform = __pi;
		}
	}
}

