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
 * @since 2016/05/24
 */
public interface UIContainer
{
	/**
	 * Adds a component to the current container.
	 *
	 * @param __uic The component to add to the container.
	 * @throws UIException If it could not be added.
	 * @since 2016/05/24
	 */
	public abstract void add(UIComponent __uic)
		throws UIException;
}

