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
 * This is an interface which singifies that it can contain other components.
 *
 * @param <T> The component type to store.
 * @since 2016/05/24
 */
public interface UIContainer<T extends UIComponent>
{
	/**
	 * Adds a component to the current container.
	 *
	 * @param __i The index to add the component at.
	 * @param __uic The component to add to the container.
	 * @throws UIException If it could not be added.
	 * @since 2016/05/24
	 */
	public abstract void add(int __i, T __uic)
		throws UIException;
	
	/**
	 * Obtains the given component from the container.
	 *
	 * @param __i The index of the component to get.
	 * @return The component at the given index.
	 * @throws UIException If it could not be obtained.
	 * @since 2016/05/24
	 */
	public abstract T get(int __i)
		throws UIException;
	
	/**
	 * Returns the number of components in this container.
	 *
	 * @return The component count.
	 * @throws UIException If it could not be determined.
	 * @since 2016/05/24
	 */
	public abstract int size()
		throws UIException;
}

