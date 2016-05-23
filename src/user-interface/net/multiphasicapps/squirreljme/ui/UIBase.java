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
public class UIBase
{
	/** Element lock. */
	protected final Object lock;
	
	/** The external display manager. */
	protected final UIManager manager;
	
	/**
	 * Initializes the base element.
	 *
	 * @param __dm The external display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	UIBase(UIManager __dm)
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
	 * @since 2016/05/22
	 */
	protected final <P extends PIBase> P platform(Class<P> __cl)
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
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
	final void __linkBack(PIBase __i)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

