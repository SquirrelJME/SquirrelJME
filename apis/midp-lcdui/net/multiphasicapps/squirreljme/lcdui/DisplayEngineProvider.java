// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is accessed via the {@link ServiceLoader} to provide the ability to
 * use displays to interact with the user.
 *
 * @since 2017/02/08
 */
public interface DisplayEngineProvider
{
	/**
	 * Returns the display engines which are available.
	 *
	 * @return The display engines that are available, the first engine must
	 * be a primary display for this provider.
	 * @since 2017/02/08
	 */
	public abstract DisplayEngine[] engine();
}

