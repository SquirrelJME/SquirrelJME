// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcduilui;

/**
 * This interface is used by the common display manager for implementation of
 * display providers.
 *
 * @param <R> The raw display type used.
 * @param <D> The type of displays to enumerate.
 * @since 2016/10/08
 */
public interface CommonDisplayProvider<R, D extends CommonDisplay<R>>
{
	/**
	 * Returns the displays that are available to this provider.
	 *
	 * @return The displays provided by the provider.
	 * @since 2016/10/08
	 */
	public abstract D[] getDisplays();
}

