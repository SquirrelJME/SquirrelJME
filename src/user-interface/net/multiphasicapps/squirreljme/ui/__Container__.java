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

/**
 * This is a class which manages the containing of elements in a common manner
 * to prevent code duplication across anything which can contain components.
 *
 * @param <U> The UI container type.
 * @param <P> The platform container type.
 * @since 2016/05/24
 */
final class __Container__<U extends UIContainer, P extends PIContainer>
	implements UIContainer
{
	/** The locking object. */
	protected final Object lock;
	
	/** The owning display manager. */
	protected final UIManager manager;
	
	/** The container element. */
	protected final U container;
	
	/**
	 * Initializes the container.
	 *
	 * @param __dm The owning display manager.
	 * @param __u The container which owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/24
	 */
	__Container__(UIManager __dm, U __u)
		throws NullPointerException
	{
		// Check
		if (__dm == null || __u == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __dm;
		this.lock = __dm.__lock();
		this.container = __u;
	}
	
	/**
	 * Adds an element to be contained by this container.
	 *
	 * @param __c The component to add to the container.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If it could not be added.
	 * @since 2016/05/24
	 */
	public void add(UIComponent __c)
		throws NullPointerException, UIException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

