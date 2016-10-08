// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui;

/**
 * This is the base class for providers which provide for basic UIs.
 *
 * @since 2016/10/08
 */
public interface BasicUIProvider<U extends BasicUI>
{
	/**
	 * Returns the UIs that are available.
	 *
	 * @return The UIs that are available.
	 * @since 2016/10/08
	 */
	public abstract U[] uis();
}

