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

import java.util.ArrayList;
import java.util.List;

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
	
	/** The list of items contained in this container. */
	private final List<UIComponent> _components =
		new ArrayList<>();
	
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
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void addComponent(UIComponent __c, int __i)
		throws NullPointerException, UIException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD0e Cannot add component to this container
			// because it is already inside of another container.}
			if (__c.inContainer() != null)
				throw new UIException("BD0e");
			
			// Add it to the internal list
			List<UIComponent> components = this._components;
			try
			{
				components.add(__i, __c);
			}
			
			// {@squirreljme.error BD0f Could not add the component to the
			// container.}
			catch (IndexOutOfBoundsException|OutOfMemoryError e)
			{
				throw new UIException("BD0f", e);
			}
			
			// Set component parent
			UIContainer container = this.container;
			__c.__setContainer(container);
			
			// Cause an update
			((UIBase)container).<PIContainer>__platform(PIContainer.class).
				containeesChanged();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public int numComponents()
		throws UIException
	{
		// Lock
		synchronized (lock)
		{
			return this._components.size();
		}
	}
}

